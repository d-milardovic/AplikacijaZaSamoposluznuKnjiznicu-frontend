package com.example.onlinelibrary.models;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class User {

    public int id;
    public String name;
    public String surname;
    public boolean administrator;
    public String email;
    public String password;
    public List<Rent> rentList = new ArrayList<>();

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
    public User(String firstName, String lastName, String email, String password){
        this.name = firstName;
        this.surname = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}

