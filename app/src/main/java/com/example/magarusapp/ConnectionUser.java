package com.example.magarusapp;

public class ConnectionUser {

    private boolean connection;
    private long initTime;
    private String name;

    public boolean isConnection() {
        return this.connection;
    }

    public  long getInitTime() {
        return this.initTime;
    }

    public String getName() {
        return this.name;
    }

}
