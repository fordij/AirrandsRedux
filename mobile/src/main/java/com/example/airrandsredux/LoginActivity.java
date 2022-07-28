package com.example.airrandsredux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.codec.digest.DigestUtils;

public class LoginActivity extends AppCompatActivity {

    public String userEmail = "dans@test.com";
    public String hashedPass = "test1234";
    public String userFirstName = "Dan";
    public String userLastName = "Smith";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //declare message box
        EditText emailBox = findViewById(R.id.emailText);

        //declare message box
        EditText passBox = findViewById(R.id.passwordText);



        //go to the register screen
        Button registerScreen = findViewById(R.id.registerButton);
        //creating the Intent to go to the register activity
        Intent regIntent = new Intent(this, RegisterActivity.class);
        //button listener
        registerScreen.setOnClickListener(new View.OnClickListener(){
                public void onClick (View v){
                    startActivity(regIntent);
                }
            }
        );


        //save the passwords for use
        Button loginSave = findViewById(R.id.loginButton);
        //creating the Intent to go to back to main
        Intent mainIntent = new Intent(this, MainActivity.class);
        //button listener
        loginSave.setOnClickListener(new View.OnClickListener(){
                public void onClick (View v){
                    //send back data with the intent
                    mainIntent.putExtra("userEmail", emailBox.getText().toString());
                    mainIntent.putExtra("hashedPass", DigestUtils.sha256Hex(passBox.getText().toString()));
                    mainIntent.putExtra("firstName", userFirstName); // TODO: replace with DB grab
                    mainIntent.putExtra("lastName", userLastName); // TODO: replace with DB grab
                    mainIntent.putExtra("group", "1"); // TODO: replace with DB grab
                    mainIntent.putExtra("manager", false); //TODO: replace with DB grab
                    startActivity(mainIntent);
                }
            }
        );


    }
}