package com.example.onlinelibrary.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.onlinelibrary.Authentication;
import com.example.onlinelibrary.BookService;
import com.example.onlinelibrary.R;
import com.example.onlinelibrary.models.Book;
import com.example.onlinelibrary.models.Rent;
import com.example.onlinelibrary.utils.volley.VolleyResponseListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends ListFragment {
    private ArrayAdapter adapter;
    private ArrayList<Rent> list = new ArrayList<>();
    private ArrayList<Rent> filtered = new ArrayList<>();
    private Toolbar toolbar;
    private TextView search;

    private void loadData() {
        BookService bookService = new BookService(getActivity());
        if(Authentication.loggedInUser.administrator) {
            bookService.getAllRentedBooks(responseListener);
        } else {
            bookService.getRentedBooksByUser(Authentication.loggedInUser.id, responseListener);
        }
    }

    VolleyResponseListener<List<Rent>> responseListener = new VolleyResponseListener<List<Rent>>() {
        @Override
        public void onResponse(List<Rent> response) {
            list.clear();
            filtered.clear();
            for (Rent rent : response) {
                if(rent.rentEnd != null)
                    list.add(rent);
            }
            filtered.addAll(list);
            adapter.notifyDataSetChanged();
            filterListByBookName(search.getText().toString());
        }
    };

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new ArrayAdapter<>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1, filtered);
        setListAdapter(adapter);


        loadData();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        loadData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(Authentication.loggedInUser.administrator) {
            toolbar = getActivity().findViewById(R.id.toolbar_admin);
        } else {
            toolbar = getActivity().findViewById(R.id.toolbar);
        }

        search = toolbar.findViewById(R.id.toolbarSearch);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterListByBookName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);

    }

    public void filterListByBookName(String name) {
        filtered.clear();

        if(Authentication.loggedInUser.administrator){
            for (Rent rent : list) {
                if(name.isEmpty() || rent.book.title.toLowerCase().contains(name.toLowerCase()) || rent.book.author.toLowerCase().contains(name.toLowerCase()) || rent.user.name.toLowerCase().contains(name.toLowerCase())) {
                    filtered.add(rent);
                }
            }
            adapter.notifyDataSetChanged();
            return;
        }

        for (Rent rent : list) {
            if(name.isEmpty() || rent.book.title.toLowerCase().contains(name.toLowerCase()) || rent.book.author.toLowerCase().contains(name.toLowerCase())) {
                filtered.add(rent);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
