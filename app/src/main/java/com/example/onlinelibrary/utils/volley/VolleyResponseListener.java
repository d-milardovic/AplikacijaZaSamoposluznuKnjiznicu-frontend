package com.example.onlinelibrary.utils.volley;

public interface VolleyResponseListener<T> {
    void onResponse(T response);
    // not exposed since all errors are handled with a toast
    //void onError(String message);
}