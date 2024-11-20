package com.orm;

import com.orm.config.DatabaseConfig;
import com.orm.entityManager.EntityManager;
import com.orm.entityManager.EntityManagerImpl;
import com.orm.test.User;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConfig config = new DatabaseConfig();
        EntityManager<User> entityManager = new EntityManagerImpl(config.getConnection());
        User user = new User(Long.valueOf(6),"Dan");
        entityManager.create(user);

    }
}