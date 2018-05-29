package dao;

import dao.interfaces.IUserDao;
import dao.util.DatabaseProperties;
import domain.user;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDao implements IUserDao/*extends AbstractDao<user>*/ {
    //@Inject
    //private DatabaseProperties databaseProperties;

    private Logger logger = Logger.getLogger(getClass().getName());

    DatabaseProperties databaseProperties;

    @Inject
    public UserDao(DatabaseProperties databaseProperties)
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

    public user login(String user, String password) {
        try {
            String sql = "SELECT * FROM user WHERE user = ? AND password = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, user);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            return getUser(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public user findById(int id) {
        try {
            String sql = "SELECT * FROM user WHERE id = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            return getUser(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public user findByToken(String token) {
        try {
            String sql = "SELECT * FROM user WHERE token = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, token);

            ResultSet resultSet = statement.executeQuery();

            return getUser(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<user> findAll() {
        try {
            String sql = "SELECT * FROM user";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            return getUser(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<user> getUser(ResultSet rs) {

        try {
            if (!rs.next()) return null;
            ArrayList<user> userlist = new ArrayList<user>();

            do {
                user domUser = new user();

                domUser.id = rs.getInt("id");
                domUser.voornaam = rs.getString("voornaam");
                domUser.achternaam = rs.getString("achternaam");
                domUser.user = rs.getString("user");
                domUser.password = rs.getString("password");
                domUser.email = rs.getString("email");
                domUser.token = rs.getString("token");

                userlist.add(domUser);
            } while(rs.next());

            return userlist;

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<user> findOtherUsers(String token) {
        try {
            String sql = "SELECT * FROM user WHERE token != ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, token);

            ResultSet resultSet = statement.executeQuery();

            return getUser(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
