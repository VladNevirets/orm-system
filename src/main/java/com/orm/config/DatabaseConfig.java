package com.orm;

import com.orm.EntityManager.EntityManager;
import com.orm.EntityManager.EntityManagerImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDb {
    public void connect(String url,Object entity) {
        try {
            Connection conn = DriverManager.getConnection(url);
            EntityManager<entity> entityManager = new EntityManagerImpl();
        }catch(SQLException e) {
            e.printStackTrace();
        }

    }
}
