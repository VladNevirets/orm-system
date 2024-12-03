package com.MapFlow.test;


import com.MapFlow.annotation.Column;
import com.MapFlow.annotation.Entity;
import com.MapFlow.annotation.Id;
import com.MapFlow.annotation.Table;


@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name="user_id")
    private Long id;

    @Column(name="user_name")
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public User(String userName) {
        this.userName = userName;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User { \n" +
                "id=" + id +
                ",\nuserName='" + userName + '\'' +
                "\n}";
    }
}

