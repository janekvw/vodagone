package dao.interfaces;

import dao.util.DatabaseProperties;

import java.util.List;

public interface IGenericDao< T > {
    T findById(int id);

    List<T> findAll();

    void save(final T entity);

    void update(final T entity);

    void delete(final T entity);

    void tryLoadJdbcDriver(DatabaseProperties databaseProperties);
}
