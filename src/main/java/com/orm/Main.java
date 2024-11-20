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
        User user = new User(Long.valueOf(1),"John");
        entityManager.create(user);
        User readUser = (User) entityManager.read(User.class,1);
        System.out.println(readUser);
        User updateUser = new User(Long.valueOf(1),"Johnathan");
        entityManager.update(updateUser);
        User readUpdatedUser = (User) entityManager.read(User.class,1);
        System.out.println(readUpdatedUser);
        entityManager.delete(readUpdatedUser);
    }
}