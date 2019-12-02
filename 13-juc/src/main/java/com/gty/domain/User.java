package com.gty.domain;

public class User {
    public volatile int id;

    public User(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}
