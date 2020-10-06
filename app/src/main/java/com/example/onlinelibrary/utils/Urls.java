package com.example.onlinelibrary.utils;

public class Urls {
    private static String BASE_URL = "http://192.168.0.22:8080";

    public static String ALL_BOOKS_URL = BASE_URL + "/books/";
    public static String ADD_BOOK_URL = BASE_URL + "/addBook/";
    public static String BOOKS_BY_TITLE = BASE_URL + "/book";
    public static String USER_RENTED_BOOKS = BASE_URL + "/rentUser/";
    public static String ALL_RENTED_BOOKS = BASE_URL + "/items";
    public static String RENT = BASE_URL + "/rent/";
    public static String ADD_RENT = BASE_URL + "/addRent";
    public static String USER = BASE_URL + "/login";
    public static String REGISTER = BASE_URL + "/addUser";
    public static String DELETE_BOOK = BASE_URL + "/deleteBook/";
}
