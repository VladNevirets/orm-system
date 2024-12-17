package com.MapFlow.entityManager;

public interface EntityManager<T> {
    void create(T entity);
    T read(Class<?> entityClass, Object primaryKey);
    void update(Object entity,Object primaryKey);
    void delete(Class<?> entity,Object primaryKey);
}
