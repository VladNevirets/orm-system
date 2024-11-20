package com.orm.entityManager;

public interface EntityManager<T> {
    void create(T entity);
    T read(Class<?> entityClass, Object primaryKey);
    void update(T entity);
    void delete(T entity);
}
