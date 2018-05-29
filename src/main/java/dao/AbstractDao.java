package dao;

import dao.interfaces.IGenericDao;
import dao.util.DatabaseProperties;

import javax.inject.Inject;
import javax.jms.Session;
import javax.transaction.Transaction;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDao< T > implements IGenericDao< T > {
    private Class< T > entityClass;
    private Logger logger = Logger.getLogger(getClass().getName());
    private Session session;
    private Transaction tx;

    @Inject
    private DatabaseProperties databaseProperties;


    public AbstractDao(/*DatabaseProperties databaseProperties*/)
    {
        //this.databaseProperties = databaseProperties;
        tryLoadJdbcDriver(databaseProperties);
    }

    public void tryLoadJdbcDriver(DatabaseProperties databaseProperties) {
        try {
            entityClass.forName(databaseProperties.driver());
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Can't load JDBC Driver " + databaseProperties.driver(), e);
        }
    }

    public void setClass( Class< T > classToSet ){
        this.entityClass = classToSet;
    }

    public T findById(int id) {
        try {
            String sql = "SELECT * FROM " + entityClass.getName() + " WHERE id = ?";
            Connection connection = DriverManager.getConnection(databaseProperties.connectionString());
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            return (T) resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

}
