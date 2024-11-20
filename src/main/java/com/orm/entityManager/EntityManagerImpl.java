package com.orm.entityManager;

import com.orm.annotation.Column;
import com.orm.sql.QueryBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EntityManagerImpl implements EntityManager {
    Connection conn;

    public EntityManagerImpl() {
    }

    public EntityManagerImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Object entity) {
        String query = QueryBuilder.insertQuery(entity);
        System.out.println(query);
        try(Statement statment = conn.createStatement()) {
            statment.executeUpdate(query);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object read(Class entityClass, Object primaryKey) {
        String query = QueryBuilder.selectQuery(entityClass, primaryKey);
        System.out.println( query);

        try(Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query)){
            if(resultSet.next()) {
                Object entity = entityClass.getDeclaredConstructor().newInstance();
                for (Field field : entityClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        Object value = resultSet.getObject(column.name());
                        field.set(entity, convertValue(value, field.getType()));
                    }
                }
                return entity;

            }

        }catch(SQLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(Object entity) {
        String query = QueryBuilder.updateQuery(entity);
        System.out.println(query);
        try(Statement statment = conn.createStatement()) {
            statment.executeUpdate(query);
        }catch(SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void delete(Object entity) {
        String query = QueryBuilder.deleteQuery(entity);
        System.out.println(query);
        try(Statement statement = conn.createStatement()){
            statement.executeUpdate(query);
        }catch(SQLException e) {
            e.printStackTrace();
        }

    }
    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }

        if (targetType == Integer.class || targetType == int.class) {
            return ((Number) value).intValue();
        } else if (targetType == Long.class || targetType == long.class) {
            return ((Number) value).longValue();
        } else if (targetType == Double.class || targetType == double.class) {
            return ((Number) value).doubleValue();
        } else if (targetType == Float.class || targetType == float.class) {
            return ((Number) value).floatValue();
        } else if (targetType == String.class) {
            return value.toString();
        }

        throw new IllegalArgumentException("Cannot convert value of type " + value.getClass() + " to " + targetType);
    }
}
