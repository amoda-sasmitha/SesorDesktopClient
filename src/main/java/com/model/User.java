package com.model;

public class User {

    String email;
    private static User instance;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public synchronized static User getInstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }

    public static void resetUser(){
        instance = null;
    }

}
