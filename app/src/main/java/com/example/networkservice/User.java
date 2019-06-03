package com.example.networkservice;

public class User {
    private String id;
    private String name;
    private String job;
    private String createdAt;
    private String updatedAt;

    public User(String name, String job) {
        this.name = name;
        this.job = job;
    }
}
