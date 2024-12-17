package com.MapFlow.querybuilder;

import com.MapFlow.annotation.Column;
import com.MapFlow.annotation.Id;
import com.MapFlow.annotation.Table;
import com.MapFlow.exception.EntityNotMappedEcxeption;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {
    public static String insertQuery(Object entity) {
        //Отримання інформації про клас сутності
        Class<?> clazz = entity.getClass();
        //Створення запиту
        StringBuilder query = new StringBuilder("INSERT INTO ");
        //Отримання назви таблиці бд та додавання її до запиту
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new EntityNotMappedEcxeption("Class " + clazz.getSimpleName() + " is not annotated with @Table.");
        }
        query.append(table.name()).append(" (");

        Field[] fields = clazz.getDeclaredFields();
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();
        //Отримання назв колонок із сутності та отримання данних на вставку з Object entity
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {
                Column column = field.getAnnotation(Column.class);
                columns.add(column.name());

                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    if (value instanceof Number) {
                        values.add(value.toString());
                    } else {
                        values.add("'" + value.toString() + "'");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        //додавання назв колонок та данних в запит
        query.append(String.join(", ", columns));
        query.append(") VALUES (");
        query.append(String.join(", ", values));
        query.append(");");

        return query.toString();
    }


    public static String selectQuery(Class<?> clazz, Object primaryKey) {
        //Створення початкової строки
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        //Отримання ім'я таблиці
        try {
            Table table = clazz.getAnnotation(Table.class);
            query.append(table.name());
        } catch (NullPointerException e) {
            throw new EntityNotMappedEcxeption("Class " + clazz.getSimpleName() + " is not annotated with @Table.");
        }

        //Цикл, який формує умову запиту
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

    public static String updateQuery(Object entity, Object primaryKey) {
       //Отримання інформації про сутність
        Class<?> clazz = entity.getClass();
        StringBuilder query = new StringBuilder("UPDATE ");

        try {
            Table table = clazz.getAnnotation(Table.class);
            query.append(table.name());
            query.append(" SET ");
        } catch (NullPointerException e) {
            throw new EntityNotMappedEcxeption("Class " + clazz.getSimpleName() + " is not annotated with @Table.");
        }
        //Отримання данних з entity та формування запиту
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

    public static String deleteQuery(Class<?> entity, Object primaryKey) {
        if (primaryKey == null) {
            throw new IllegalArgumentException("Primary key cannot be null.");
        }

        Class<?> clazz = entity;
        StringBuilder query = new StringBuilder("DELETE FROM ");

        // Отримання назви таблиці
        try {
            Table table = clazz.getAnnotation(Table.class);
            query.append(table.name());
        } catch (NullPointerException e) {
            throw new EntityNotMappedEcxeption("Class " + clazz.getSimpleName() + " is not annotated with @Table.");
        }
        query.append(" WHERE ");

        boolean idFieldFound = false;

        // Формування частини WHERE
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idFieldFound = true;
                field.setAccessible(true);

                try {
                    Column column = field.getAnnotation(Column.class);
                    if (column != null) {
                        query.append(column.name()).append(" = ");
                        if (Number.class.isAssignableFrom(field.getType())) {
                            query.append(primaryKey); // Для чисел лапки не потрібні
                        } else {
                            query.append("'").append(field.get(entity)).append("'"); // Для текстових значень додаємо лапки
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to access field value: " + field.getName(), e);
                }

                break; // Завершуємо після першого знайденого @Id
            }
        }

        if (!idFieldFound) {
            throw new EntityNotMappedEcxeption("Class " + clazz.getSimpleName() + " does not have a field annotated with @Id.");
        }

        query.append(";");
        return query.toString();
    }



}
