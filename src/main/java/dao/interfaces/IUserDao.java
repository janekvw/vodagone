package dao.interfaces;

import dao.util.DatabaseProperties;
import domain.user;

import java.sql.ResultSet;
import java.util.List;

public interface IUserDao {
    void tryLoadJdbcDriver(DatabaseProperties databaseProperties);
    user findById(int id);
    List<user> findAll();
    List<user> getUser(ResultSet rs);
}
