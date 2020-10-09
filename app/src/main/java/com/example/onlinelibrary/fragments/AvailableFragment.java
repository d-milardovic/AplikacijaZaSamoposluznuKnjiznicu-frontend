package com.example.onlinelibrary.fragments;

import android.app.AlertDialog;
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

public class AvailableFragment extends ListFragment {
    private ArrayAdapter adapter;
    private ArrayList<Book> list = new ArrayList<>();
    private ArrayList<Book> filtered = new ArrayList<>();
    private BookService bookService;
    private Toolbar toolbar;
    private TextView search;

    private void loadData() {

        bookService = new BookService(getActivity());
        bookService.getAllBooks(new VolleyResponseListener<List<Book>>() {
            @Override
            public void onResponse(List<Book> response) {
                list.clear();
                list.addAll(response);
                adapter.notifyDataSetChanged();
                filterListByBookName(search.getText().toString());
            }
        });

    }

    public AvailableFragment() {
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
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        loadData();
    }

    @Override
    public void onListItemClick(ListView l, View v, final int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        if(Authentication.loggedInUser.administrator)
        {
            showDeleteDialog(pos);
        } else {
            showRentDialog(pos);
        }
    }
    private void showRentDialog(final int pos) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Rent")
                .setMessage("Are you sure you want to rent this book?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final Book book = filtered.get(pos);
                        bookService.rent(new RentBody(Authentication.loggedInUser.id, book.id), new VolleyResponseListener<Rent>() {
                            @Override
                            public void onResponse(Rent response) {
                                if(response.rentStart != null) {
                                    Toast.makeText(getActivity(), "Knjiga " + book.title + " je rentana!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), "Posudba ove knjige nije moguća!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showDeleteDialog(final int pos) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this book?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final Book book = filtered.get(pos);
                        bookService.deleteBook(book.id, new VolleyResponseListener<String>() {
                            @Override
                            public void onResponse(String response) {
                                list.remove(book);
                                filtered.remove(book);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "Knjiga " + book.title + " izbrisana!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void filterListByBookName(String name) {
        filtered.clear();

        for (Book book : list) {
            if(name.isEmpty() || book.title.toLowerCase().contains(name.toLowerCase()) || book.author.toLowerCase().contains(name.toLowerCase())) {
                filtered.add(book);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
