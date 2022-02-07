package com.myfirstapp.testing.model;

public class User {
    public User(String firstName) {
        mFirstName = firstName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    private String mFirstName;


}
