package com.example.onlinelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinelibrary.models.User;
import com.example.onlinelibrary.utils.volley.VolleyResponseListener;

public class MainActivity extends AppCompatActivity {

    EditText mTextUsername;
    EditText mTextPassword;
    BookService bookService = new BookService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final EditText mTextEmail;
        final EditText mTextPassword;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextEmail =(EditText)findViewById(R.id.email_textedit);
        mTextPassword =(EditText)findViewById(R.id.password_textedit);


        Button Loginbutton = findViewById(R.id.login_button);
        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mTextEmail.getText().toString();
                String password = mTextPassword.getText().toString();
                User loginUser = new User(email, password);
                bookService.login(loginUser, new VolleyResponseListener<User>() {
                    @Override
                    public void onResponse(User response) {
                        Authentication.loggedInUser = response;

                        if(response.administrator){
                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        Button SignUpbutton = findViewById(R.id.sign_up_button);
        SignUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}

