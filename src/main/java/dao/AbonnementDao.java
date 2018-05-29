package dao;

import dao.util.DatabaseProperties;
import domain.abonnement;
import domain.user;
import domain.userabbo;

import javax.inject.Inject;
import javax.xml.registry.infomodel.User;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbonnementDao {

    private Logger logger = Logger.getLogger(getClass().getName());

    DatabaseProperties databaseProperties;

    @Inject
    public AbonnementDao(DatabaseProperties databaseProperties)
    {
        this.databaseProperties = databaseProperties;
        tryLoadJdbcDriver(databaseProperties);
    }

    public void tryLoadJdbcDriver(DatabaseProperties databaseProperties) {
        try {
            Class.forName(databaseProperties.driver());
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Can't load JDBC Driver " + databaseProperties.driver(), e);
        }
    }

    public List<abonnement> findAllByToken(String token) {
        try {
            String sql = "SELECT * from abonnement inner join userabbo\n" +
                    "on abonnement.id = userabbo.abonnementid INNER JOIN user \n" +
                    "on user.id = userabbo.userid\n" +
                    "where user.token = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, token);

            ResultSet resultSet = statement.executeQuery();

            return getAbonnement(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<abonnement> findAll(int userid, String filter) {
        try {
            String sql = "SELECT *, 'status' from abonnement \n" +
                    "WHERE abonnement.id NOT IN (SELECT abonnementid\n" +
                    "                            FROM userabbo\n" +
                    "                            where userabbo.userid = ?)\n" +
                    "                            AND abonnement.aanbieder LIKE ? OR abonnement.dienst = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, userid);
            statement.setString(2, "%" + filter + "%");
            statement.setString(3, "%" + filter + "%");

            ResultSet resultSet = statement.executeQuery();

            return getAbonnement(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public abonnement findAllByTokenAndId(String token, int abonnementid) {
        try {
            String sql = "SELECT * from abonnement inner join userabbo\n" +
                    "on abonnement.id = userabbo.abonnementid INNER JOIN user \n" +
                    "on user.id = userabbo.userid\n" +
                    "where user.token = ? AND abonnement.id = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, token);
            statement.setInt(2, abonnementid);

            ResultSet resultSet = statement.executeQuery();

            return getAbonnement(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<abonnement> getAbonnement(ResultSet rs) {
        try {
            if (!rs.next()) return null;
            ArrayList<abonnement> userlist = new ArrayList<abonnement>();

            do {
                abonnement domAbonnement = new abonnement();

                domAbonnement.id = rs.getInt("id");
                domAbonnement.aanbieder = rs.getString("aanbieder");
                domAbonnement.dienst = rs.getString("dienst");
                domAbonnement.prijs = rs.getBigDecimal("prijs");
                domAbonnement.startDatum = rs.getString("startDatum");
                domAbonnement.verdubbeling = rs.getString("verdubbeling");
                domAbonnement.deelbaar = rs.getBoolean("deelbaar");
                domAbonnement.status = rs.getString("status");

                BigDecimal multiplier = new BigDecimal("1.5");
                if(rs.getString("verdubbeling").equals("verdubbeld")) {
                    domAbonnement.prijs = domAbonnement.prijs.multiply(multiplier);
                    domAbonnement.prijs = domAbonnement.prijs.setScale(2, 2);
                }

                userlist.add(domAbonnement);
            } while(rs.next());

            return userlist;

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private userabbo getUserAbbo(ResultSet rs) {
        try {


            userabbo userabboDom = new userabbo();

            userabboDom.id = rs.getInt("id");
            userabboDom.userid = rs.getInt("userid");
            userabboDom.abonnementid = rs.getInt("abonnementid");
            userabboDom.status = rs.getString("status");

            return userabboDom;

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public abonnement findById(int id) {
        try {
            String sql = "SELECT * FROM abonnement WHERE id = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            return getAbonnement(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void findByIdAndToken(int abonnementid, int id) {
        try {
            String sql = "UPDATE userabbo SET status = ? WHERE userid = ? AND abonnementid = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, "opgezegd");
            statement.setInt(2, id);
            statement.setInt(3, abonnementid);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public userabbo findStatus(int abonnementid, int userid) {
        try {
            String sql = "SELECT * FROM userabbo WHERE userid = ? AND abonnementid = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, userid);
            statement.setInt(2, abonnementid);

            ResultSet resultSet = statement.executeQuery();

            return getUserAbbo(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addAbbo(int userid, int abonnementid, String token) {
        try {
            String sql = "INSERT INTO userabbo(userid, abonnementid, status, token) VALUES(?,?,?,?)";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, userid);
            statement.setInt(2, abonnementid);
            statement.setString(3, "actief");
            statement.setString(4, token);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upgradeVerdubbeling(String verdubbeling, int userid, String token) {
        try {
            String sql = "UPDATE userabbo SET verdubbeling = ? WHERE abonnementid = ? AND token = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, verdubbeling);
            statement.setInt(2, userid);
            statement.setString(3, token);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
