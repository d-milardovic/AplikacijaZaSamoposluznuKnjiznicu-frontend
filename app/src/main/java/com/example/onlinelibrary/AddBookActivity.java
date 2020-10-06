package com.example.onlinelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.onlinelibrary.models.Book;
import com.example.onlinelibrary.utils.volley.VolleyResponseListener;

public class AddBookActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;

    EditText editAuthor;
    EditText editName;
    EditText editYear;
    EditText editISBN;

    BookService bookservice = new BookService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);

        editAuthor = findViewById(R.id.book_author);
        editName = findViewById(R.id.book_name);
        editYear = findViewById(R.id.book_year);
        editISBN = findViewById(R.id.book_ISBN);

        Button buttonScan = findViewById(R.id.button_scan);
        buttonScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBookActivity.this, CameraActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String author = editAuthor.getText().toString();
                String name = editName.getText().toString();
                String year = editYear.getText().toString();
                String isbn = editISBN.getText().toString();

                if(author.isEmpty() || name.isEmpty() || year.isEmpty() || isbn.isEmpty()) {
                    Toast.makeText(AddBookActivity.this, "Potrebno je unijeti sve podatke!",Toast.LENGTH_LONG).show();
                    return;
                }
                Book book = new Book(editName.getText().toString(), editAuthor.getText().toString(),Integer.parseInt
                        (editYear.getText().toString()),editISBN.getText().toString(),false, "");
                bookservice.addBook(book, new VolleyResponseListener<Book>() {
                    @Override
                    public void onResponse(Book response) {
                        Log.e("znak","Uspijesno dodano");
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        });

        Button buttonCancel = findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBookActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {

                String name = data.getStringExtra("name");
                String author = data.getStringExtra("author");
                String year = data.getStringExtra("year");
                String ISBN = data.getStringExtra("isbn");

                editName.setText(name);
                editAuthor.setText(author);
                editYear.setText(year);
                editISBN.setText(ISBN);


            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

}
