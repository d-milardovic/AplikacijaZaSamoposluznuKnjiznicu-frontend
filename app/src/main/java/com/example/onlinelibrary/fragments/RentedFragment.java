package com.example.onlinelibrary.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.onlinelibrary.Authentication;
import com.example.onlinelibrary.BookService;
import com.example.onlinelibrary.R;
import com.example.onlinelibrary.models.Book;
import com.example.onlinelibrary.models.Rent;
import com.example.onlinelibrary.models.RentBody;
import com.example.onlinelibrary.utils.volley.VolleyResponseListener;

import java.util.ArrayList;
import java.util.List;

public class RentedFragment extends ListFragment {
    private ArrayAdapter adapter;
    private ArrayList<Rent> list = new ArrayList<>();
    private ArrayList<Rent> filtered = new ArrayList<>();
    private BookService bookService;
    private Toolbar toolbar;
    private TextView search;

    private void loadData() {
        bookService = new BookService(getActivity());
        if(Authentication.loggedInUser.administrator) {
            bookService.getAllRentedBooks(responseListener);
        } else {
            bookService.getRentedBooksByUser(Authentication.loggedInUser.id, responseListener);
        }
    }
    public RentedFragment() {
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

    VolleyResponseListener<List<Rent>> responseListener = new VolleyResponseListener<List<Rent>>() {
        @Override
        public void onResponse(List<Rent> response) {
            list.clear();
            filtered.clear();
            for (Rent rent : response) {
                if(rent.rentEnd == null)
                    list.add(rent);
            }
            adapter.notifyDataSetChanged();
            filterListByBookName(search.getText().toString());
        }
    };



    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        loadData();
    }

    @Override
    public void onListItemClick(ListView l, View v, final int pos, long id) {
        super.onListItemClick(l, v, pos, id);

        if(Authentication.loggedInUser.administrator)
            return; // ako je administrator nemoj radit ništa na klik

        new AlertDialog.Builder(getActivity())
                .setTitle("Return")
                .setMessage("Do you sure you want to return this book?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final Rent rent = filtered.get(pos);
                        bookService.returnBook(rent.id, new VolleyResponseListener<Rent>() {
                            @Override
                            public void onResponse(Rent response) {
                                filtered.remove(pos);
                                list.remove(rent);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "Knjiga " + rent.book.title + " vraćena!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

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