package com.dev.model;

public abstract class BaseUser {

    protected int id;
    protected String name;
    protected String username;
    protected String password;

    protected BaseUser(int id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean authenticate(String username, String password)
    {
        return this.username.equalsIgnoreCase(username) && this.password.equals(password);
    }
}
