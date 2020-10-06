package com.example.onlinelibrary.utils.volley;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class VolleyUtils {

    private final Context mContext;

    public VolleyUtils(Context c) {
        mContext = c;
    }

    public void requestObject(int method, String path, Object jsonRequest, Response.Listener<JSONObject> listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, path, toJson(jsonRequest),
                listener, errorListener) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    public void requestArray(int method, String path, Object jsonRequest, Response.Listener<JSONArray> listener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(method, path,null,
                listener, errorListener) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONArray(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
    }

    public void requestString(int method, String path, final Object jsonRequest, Response.Listener<String> listener) {
        StringRequest stringRequest = new StringRequest(method, path,
                listener, errorListener) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };


        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private JSONObject toJson(Object object) {
        if(object == null)
            return null;

        JSONObject jsonObject = null;

        String jsonInString = new Gson().toJson(object);

        try {
            jsonObject = new JSONObject(jsonInString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                //This indicates that the request has either time out or there is no connection
                Toast.makeText(mContext, "Connection timed out", Toast.LENGTH_LONG).show();
            }else if (error.networkResponse.statusCode == 409) {
                Toast.makeText(mContext, "Email already taken!", Toast.LENGTH_LONG).show();
            } else if (error instanceof AuthFailureError) {
                // Error indicating that there was an Authentication Failure while performing the request
                Toast.makeText(mContext, "You are not authenticated", Toast.LENGTH_LONG).show();
            } else if (error instanceof ServerError) {
                //Indicates that the server responded with a error response
                Toast.makeText(mContext, "Server error", Toast.LENGTH_LONG).show();
            } else if (error instanceof NetworkError) {
                //Indicates that there was network error while performing the request
                Toast.makeText(mContext, "Network error", Toast.LENGTH_LONG).show();
            } else if (error instanceof ParseError) {
                // Indicates that the server response could not be parsed
                Toast.makeText(mContext, "Response parsing error", Toast.LENGTH_LONG).show();
            }
        }
    };
}
