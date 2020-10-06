package com.example.onlinelibrary.models;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Book {

    public int id;
    public String title;
    public String author;
    public int dateOfIssue;
    public String isbn;
    public boolean isRented;
    public String issuer;
    public List<Rent> rentList = new ArrayList<>();

    public Book(String title, String author, int dateOfIssue, String isbn, boolean isRented, String issuer) {
        this.title = title;
        this.author = author;
        this.dateOfIssue = dateOfIssue;
        this.isbn = isbn;
        this.isRented = isRented;
        this.issuer = issuer;
    }

    @NonNull
    @Override
    public String toString() {
        return title + " - " + author + " - " + dateOfIssue;
    }

}
