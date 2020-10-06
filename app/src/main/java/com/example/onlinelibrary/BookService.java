package com.example.onlinelibrary;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.onlinelibrary.models.Book;
import com.example.onlinelibrary.models.Rent;
import com.example.onlinelibrary.models.RentBody;
import com.example.onlinelibrary.models.User;
import com.example.onlinelibrary.utils.Urls;
import com.example.onlinelibrary.utils.volley.VolleyResponseListener;
import com.example.onlinelibrary.utils.volley.VolleyUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class BookService {

    private VolleyUtils service;

    public BookService(Context context) {
        service = new VolleyUtils(context);
    }

    public void getBookById(int id, final VolleyResponseListener<Book> listener) {
        service.requestObject(Request.Method.GET, Urls.ALL_BOOKS_URL + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Book book = new Gson().fromJson(response.toString(), Book.class);
                        listener.onResponse(book);
                    }
                });
    }

    public void getAllBooks(final VolleyResponseListener<List<Book>> listener) {
        service.requestArray(Request.Method.GET, Urls.ALL_BOOKS_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<Book> books = Arrays.asList(new Gson().fromJson(response.toString(), Book[].class));
                        listener.onResponse(books);
                    }
                });
    }

    public void addBook(Book book , final VolleyResponseListener<Book> listener) {
        service.requestObject(Request.Method.POST, Urls.ADD_BOOK_URL, book,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Book book = new Gson().fromJson(response.toString(), Book.class);
                        listener.onResponse(book);
                    }
                });
    }

    public void getBookByAuthor(String title, final VolleyResponseListener<Book> listener) {
        service.requestObject(Request.Method.GET, Urls.BOOKS_BY_TITLE + title, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Book book = new Gson().fromJson(response.toString(), Book.class);
                        listener.onResponse(book);
                    }
                });
    }
    public void getRentedBooksByUser(int userId, final VolleyResponseListener<List<Rent>> listener) {
        service.requestArray(Request.Method.GET, Urls.USER_RENTED_BOOKS + userId, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<Rent> rents = Arrays.asList(new Gson().fromJson(response.toString(), Rent[].class));
                        listener.onResponse(rents);
                    }
                });
    }

    public void getAllRentedBooks(final VolleyResponseListener<List<Rent>> listener) {
        service.requestArray(Request.Method.GET, Urls.ALL_RENTED_BOOKS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<Rent> rents = Arrays.asList(new Gson().fromJson(response.toString(), Rent[].class));
                        listener.onResponse(rents);
                    }
                });
    }

    public void login(User user, final VolleyResponseListener<User> listener) {
        service.requestObject(Request.Method.POST, Urls.USER, user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        User user = new Gson().fromJson(response.toString(), User.class);
                        listener.onResponse(user);
                    }
                });
    }
        public void rent(RentBody rentBody, final VolleyResponseListener<Rent> listener) {
            service.requestObject(Request.Method.POST, Urls.ADD_RENT, rentBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Rent rent = new Gson().fromJson(response.toString(), Rent.class);
                            listener.onResponse(rent);
                        }
                    });

        }
        public void register(User user, final VolleyResponseListener<User> listener) {
                service.requestObject(Request.Method.POST, Urls.REGISTER, user,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                User user = new Gson().fromJson(response.toString(), User.class);
                                listener.onResponse(user);
                            }
                        });

    }
    public void returnBook (int rentId, final VolleyResponseListener<Rent> listener) {
        service.requestObject(Request.Method.PUT, Urls.RENT + rentId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Rent rent = new Gson().fromJson(response.toString(), Rent.class);
                        listener.onResponse(rent);
                    }
                });
    }
    public void deleteBook (int bookId, final VolleyResponseListener<String> listener) {
        service.requestString(Request.Method.DELETE, Urls.DELETE_BOOK + bookId, null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);
                    }
                });
    }
}

