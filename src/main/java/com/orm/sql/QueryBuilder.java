package com.orm.sql;

import com.orm.annotation.Column;
import com.orm.annotation.Entity;
import com.orm.annotation.Id;
import com.orm.annotation.Table;
import com.orm.exception.AnnotationNotFoundException;
import com.orm.exception.EntityNotMappedEcxeption;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {
    public static String insertQuery(Object entity) {
        Class<?> clazz = entity.getClass();
        StringBuilder query = new StringBuilder("INSERT INTO ");
        Entity entityAnnotation = clazz.getAnnotation(Entity.class);

        if (entityAnnotation == null) {
            throw new EntityNotMappedEcxeption("Insert: Class " + clazz.getSimpleName() + " is not an entity.");
        }

        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new AnnotationNotFoundException("Class " + clazz.getSimpleName() + " is not annotated with @Table.");
        }

        query.append(table.name()).append(" (");

        Field[] fields = clazz.getDeclaredFields();
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) { // Пропустити поле @Id
                Column column = field.getAnnotation(Column.class);
                columns.add(column.name());

                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    if (value instanceof Number) { // Числові значення
                        values.add(value.toString());
                    } else { // Рядкові значення
                        values.add("'" + value.toString() + "'");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        query.append(String.join(", ", columns));
        query.append(") VALUES (");
        query.append(String.join(", ", values));
        query.append(");");

        return query.toString();
    }


    public static String selectQuery(Class<?> clazz, Object primaryKey) {
        StringBuilder query = new StringBuilder("SELECT * FROM ");

        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        if (entityAnnotation == null) {
            throw new EntityNotMappedEcxeption("Select:Class " + clazz.getSimpleName() + " is not an entity.");
        }
        try {
            Table table = clazz.getAnnotation(Table.class);
            query.append(table.name());
        } catch (NullPointerException e) {
            throw new AnnotationNotFoundException("Class " + clazz.getSimpleName() + " is not annotated with @Table.");
        }


        query.append(" WHERE ");
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    if (field.getType() == String.class) {
                        query.append(column.name()).append(" = '").append(primaryKey).append("'");
                    } else {
                        query.append(column.name()).append(" = ").append(primaryKey);
                    }
                }
                break;
            }
        }
        query.append(";");

        return query.toString();
    }

    public static String updateQuery(Object entity,Object primaryKey) {
        Class<?> clazz = entity.getClass();
        StringBuilder query = new StringBuilder("UPDATE ");

        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        if (entityAnnotation == null) {
            throw new EntityNotMappedEcxeption("Update:Class " + clazz.getSimpleName() + " is not an entity.");
        }
        try {
            Table table = clazz.getAnnotation(Table.class);
            query.append(table.name());
            query.append(" SET ");
        } catch (NullPointerException e) {
            throw new AnnotationNotFoundException("Class " + clazz.getSimpleName() + " is not annotated with @Table.");
        }


        Field[] fields = clazz.getDeclaredFields();

        List<String> setClauses = new ArrayList<>();
        String whereClause = "";
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    if (field.isAnnotationPresent(Id.class)) {
                        whereClause = column.name() + " = " + primaryKey + "  ";

                    } else {
                        Object value = field.get(entity);
                        if (value != null) {
                            if (value instanceof Number) {
                                setClauses.add(column.name() + " = " + value.toString());
                            } else {

                                setClauses.add(column.name() + " = '" + value.toString() + "'");
                            }
                        }
                    }


                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (setClauses.isEmpty()) {
            throw new RuntimeException("No set query");
        }

        query.append(String.join(", ", setClauses));
        query.append(" WHERE ").append(whereClause);
        query.append(";");
        return query.toString();
    }

    public static String deleteQuery(Object entity) {
        Class<?> clazz = entity.getClass();
        StringBuilder query = new StringBuilder("DELETE FROM ");
        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        if (entityAnnotation == null) {
            throw new EntityNotMappedEcxeption("Delete:Class " + clazz.getSimpleName() + " is not an entity.");
        }
        try {
            Table table = clazz.getAnnotation(Table.class);
            query.append(table.name());
        } catch (NullPointerException e) {
            throw new AnnotationNotFoundException("Class " + clazz.getSimpleName() + " is not annotated with @Table.");
        }

        query.append(" WHERE ");
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                try {
                    if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        if (Number.class.isAssignableFrom(field.getType())) {
                            query.append(column.name()).append(" = ").append(field.get(entity));
                        } else {

                            query.append(column.name()).append(" = '").append(field.get(entity)).append("'");
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        query.append(";");

        return query.toString();
    }


}
