package com.orm.EntityManager;

public interface EntityManager<T> {
    void create(T entity);
    T find(Class<T> entityClass, Object primaryKey);
    void update(T entity);
    void delete(T entity);
}
