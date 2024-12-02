package com.MapFlow.entityManager;

import com.MapFlow.annotation.Column;
import com.MapFlow.annotation.Entity;
import com.MapFlow.exception.EntityNotMappedEcxeption;
import com.MapFlow.querybuilder.QueryBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.MapFlow.Convertor.convertValue;

public class EntityManagerImpl implements EntityManager {
    Connection conn;

    public EntityManagerImpl() {
    }

    public EntityManagerImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Object entity) {
        Class<?> clazz = entity.getClass();
        if(!clazz.isAnnotationPresent(Entity.class)) {
            throw new EntityNotMappedEcxeption("Insert: Class " + clazz.getSimpleName() + " is not an entity.");
        }

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
        if(!entityClass.isAnnotationPresent(Entity.class)) {
            throw new EntityNotMappedEcxeption("Select:Class " + entityClass.getSimpleName() + " is not an entity.");
        }


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
    public void update(Object entity,Object primaryKey) {
        String query = QueryBuilder.updateQuery(entity,primaryKey);
        System.out.println(query);
        Class<?> clazz = entity.getClass();
        if(!clazz.isAnnotationPresent(Entity.class)) {
            throw new EntityNotMappedEcxeption("Update: Class " + clazz.getSimpleName() + " is not an entity.");
        }
        try(Statement statment = conn.createStatement()) {
            statment.executeUpdate(query);
        }catch(SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void delete(Object entity,Object primaryKey) {
        String query = QueryBuilder.deleteQuery(entity,primaryKey);
        System.out.println(query);
        Class<?> clazz = entity.getClass();
        if(!clazz.isAnnotationPresent(Entity.class)) {
            throw new EntityNotMappedEcxeption("Delete: Class " + clazz.getSimpleName() + " is not an entity.");
        }
        try(Statement statement = conn.createStatement()){
            statement.executeUpdate(query);
        }catch(SQLException e) {
            e.printStackTrace();
        }

    }

}
