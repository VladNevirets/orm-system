package com.MapFlow;

import com.MapFlow.config.ConfigurationManager;
import com.MapFlow.entityManager.EntityManager;
import com.MapFlow.entityManager.EntityManagerImpl;
import com.MapFlow.test.User;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {

        ConfigurationManager config = new ConfigurationManager();
        EntityManager<User> entityManager = new EntityManagerImpl(config.getConnection());

        //Створення об'єкту класу User та присвоєння ім'я користувачу
        //User createUser = new User();
        //createUser.setUserName("Bon Jovi");
        //createUser.setEmail("jovi@gmail.com");
        //Додавання користувача до бази данних
        //entityManager.create(createUser);

        //Отримання користувача з бази данних
        User readUser = entityManager.read(User.class,5L);
        System.out.println(readUser);
        //Оновлення імені користувача


        //Видалення користувача з бази данних
        //entityManager.delete(User.class,3L);







    }
}