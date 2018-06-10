package net.whitewalker.shopmanager.persistence;

import java.util.List;

public interface DAO<T> {

    List<T> findAll();
    T findById(String identifier);

    boolean insert(T dto);
    boolean update(T dto);
    boolean delete(T dto);

}
