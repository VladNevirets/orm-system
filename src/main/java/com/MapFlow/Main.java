package com.MapFlow;

import com.MapFlow.config.ConfigurationManager;
import com.MapFlow.entityManager.EntityManager;
import com.MapFlow.entityManager.EntityManagerImpl;
import com.MapFlow.test.User;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigurationManager config = new ConfigurationManager();
        EntityManager<User> entityManager = new EntityManagerImpl(config.getConnection());
        User newUser = new User();
        User user =entityManager.read(newUser.getClass(),3L);


        System.out.println(user);




    }
}