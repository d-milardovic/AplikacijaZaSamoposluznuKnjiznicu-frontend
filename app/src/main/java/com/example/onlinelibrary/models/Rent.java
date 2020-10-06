package com.example.onlinelibrary.models;


import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Rent {

    public int id;
    public Book book;
    public User user;
    public String rentStart;
    public String rentEnd;

    @NonNull
    @Override
    public String toString() {
        return book.title + " - " + book.author + "\n" + formatDate(rentStart) + " - " + formatDate(rentEnd) + "\n"+ user.getFullName();
    }

    public String formatDate(String date) {
        return date != null ? date.replace("T", " "):"";
    }

}
