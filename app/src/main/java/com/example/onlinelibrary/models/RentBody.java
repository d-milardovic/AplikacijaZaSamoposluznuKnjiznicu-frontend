package com.example.onlinelibrary.models;

public class RentBody {
    public int userId;
    public int bookId;

    public RentBody(int userId, int bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }
}
