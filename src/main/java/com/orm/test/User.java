package com.orm.test;


import com.orm.annotation.Column;
import com.orm.annotation.Entity;
import com.orm.annotation.Id;
import com.orm.annotation.Table;

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

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }
}