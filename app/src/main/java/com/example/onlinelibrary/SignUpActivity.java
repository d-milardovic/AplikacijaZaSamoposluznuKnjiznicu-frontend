package com.example.onlinelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinelibrary.models.User;
import com.example.onlinelibrary.utils.volley.VolleyResponseListener;

public class SignUpActivity extends AppCompatActivity {


    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    BookService bookService = new BookService(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final EditText mTextFirstName;
        final EditText mTextLastName;
        final EditText mTextEmail;
        final EditText mTextPassword;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mTextFirstName = (EditText) findViewById(R.id.firstname_textedit);
        mTextLastName =(EditText) findViewById(R.id.lastname_textedit);
        mTextEmail =(EditText)findViewById(R.id.Email_textedit) ;
        mTextPassword =(EditText)findViewById(R.id.password_textedit) ;

        Button SignInbutton = findViewById(R.id.sign_in_button);
        SignInbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = mTextFirstName.getText().toString();
                String lastName = mTextLastName.getText().toString();
                String email = mTextEmail.getText().toString();
                String password = mTextPassword.getText().toString();

                if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Potrebno je unijeti sve podatke!",Toast.LENGTH_LONG).show();
                    return;
                }
                User signUpUser = new User(firstName, lastName, email, password);
                bookService.register(signUpUser, new VolleyResponseListener<User>() {
                    @Override
                    public void onResponse(User response) {
                        Authentication.loggedInUser = response;

                        if(response.administrator){
                            Intent intent = new Intent(SignUpActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });


        Button AlreadyMemberbutton = findViewById(R.id.already_member_button);
        AlreadyMemberbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}