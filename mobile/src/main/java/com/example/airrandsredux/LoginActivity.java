package com.example.airrandsredux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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

        //reset intent
        Intent resetIntent = new Intent(this, LoginActivity.class);


        //save the passwords for use
        Button loginSave = findViewById(R.id.loginButton);
        //creating the Intent to go to back to main
        Intent mainIntent = new Intent(this, MainActivity.class);
        //button listener
        loginSave.setOnClickListener(new View.OnClickListener(){
                public void onClick (View v){
                    //email string so we don't keep grabbing the same one
                    String tempUserEmail = emailBox.getText().toString();

                    //send back data with the intent
                    mainIntent.putExtra("userEmail", tempUserEmail);
                    mainIntent.putExtra("hashedPass", DigestUtils.sha256Hex(passBox.getText().toString()));

                    //start doing database grabs here
                    //every instance of this "new Thread" code makes a new thread to run network code on
                    //Android doesn't allow you to run network code on the main thread, so we need to make a new thread for it
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try  {
                                //define the driver
                                Class.forName("com.mysql.jdbc.Driver");
                                //connect to the database
                                Connection con = DriverManager.getConnection("jdbc:mysql://72.85.159.59:3306/company?autoReconnect=true", "airrandsapp", "pass");
                                Log.d("Debug", "Connected to database");

                                //start running the query
                                try (Statement stmt = con.createStatement()) {
                                    Log.d("Debug", "Started query 1");
                                    //check email/password against database
                                    String loginQuery = "SELECT * FROM company.employee WHERE email = '" + tempUserEmail + "' AND empPassword = '" + DigestUtils.sha256Hex(passBox.getText().toString()) + "'";

                                    //JDBC doesn't let you run multiple queries on the same statement, so there will be an extra try/catch block here
                                    try (ResultSet resultSet = stmt.executeQuery(loginQuery)) {
                                        Log.d("Debug", "Started query 2");
                                        resultSet.next();
                                        //check email/password against database
                                        if (resultSet.getString("email").equals(tempUserEmail) && resultSet.getString("empPassword").equals(DigestUtils.sha256Hex(passBox.getText().toString()))) {
                                            Log.d("Debug: ", "Login successful");
                                            try (Statement stmt2 = con.createStatement()) {
                                                try (ResultSet resultSet2 = stmt2.executeQuery("SELECT * FROM company.employee WHERE email = '" + tempUserEmail + "'")) {
                                                    //go to the first row of the result set and start reading
                                                    resultSet2.next();

                                                    mainIntent.putExtra("email", resultSet2.getString("email"));
                                                    Log.d("Debug", "Email: " + resultSet2.getString("email"));

                                                    mainIntent.putExtra("hashedPass", resultSet2.getString("empPassword"));
                                                    Log.d("Debug", "Password: " + resultSet2.getString("empPassword"));

                                                    mainIntent.putExtra("firstName", resultSet2.getString("employeeFirstName"));
                                                    Log.d("Debug", "First Name: " + resultSet2.getString("employeeFirstName"));

                                                    mainIntent.putExtra("lastName", resultSet2.getString("employeeLastName"));
                                                    Log.d("Debug", "Last Name: " + resultSet2.getString("employeeLastName"));

                                                    mainIntent.putExtra("employeeID", resultSet2.getString("employeeID"));
                                                    Log.d("Debug", "Employee ID: " + resultSet2.getString("employeeID"));

                                                    mainIntent.putExtra("group", resultSet2.getString("groupNum"));
                                                    Log.d("Debug", "Group: " + resultSet2.getString("groupNum"));

                                                    //android really hates functions it doesn't know, so we have to convert it to a string and THEN say what it is
                                                    if (resultSet2.getString("isManager").equals("1")) {
                                                        mainIntent.putExtra(",anager", 1);
                                                        Log.d("Debug", "isManager: " + resultSet2.getString("isManager"));
                                                    } else {
                                                        mainIntent.putExtra("manager", 0);
                                                        Log.d("Debug", "isManager: " + resultSet2.getString("isManager"));
                                                    }
                                                    Log.d("Debug: ", "Grabbed all data, going to main");
                                                    startActivity(mainIntent);
                                                }

                                            }
                                        }
                                            else {
                                                //toast and reset
                                                Looper.prepare();
                                                Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                                                Log.d("Debug: ", "Login failed");
                                                startActivity(resetIntent);
                                            }
                                        }
                                    }
                                }
                                catch (Exception e){
                                    //if it can't sign in, throw an error, toast, and reset page
                                    //techncially this is done by catching a crash when checking a
                                    //nonexistent email/password but if it works it works!
                                    Log.d("DB Error", "error signing into");
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                    startActivity(resetIntent);
                                }
                            }

                        }).start();
                    }

                    /* legacy code
                    try (Connection con = DriverManager.getConnection("jdbc:mysql://72.85.159.59:3306/company?autoReconnect=true", "airrandsapp", "pass")) {
                        try (Statement stmt = con.createStatement()) {
                            //check email/password against database
                            String loginQuery = "SELECT * FROM users WHERE email = '" + tempUserEmail + "' AND password = '" + DigestUtils.sha256Hex(passBox.getText().toString()) + "'";
                            if (true) {
                                //start making queries to get user info
                                String query = "SELECT firstName FROM company.employees WHERE email = '" + tempUserEmail + "'";
                                mainIntent.putExtra("firstName", stmt.executeQuery(query).toString());
                                query = "SELECT lastName FROM users WHERE email = '" + tempUserEmail + "'";
                                mainIntent.putExtra("lastName", stmt.executeQuery(query).toString());
                                query = "SELECT groupNumber FROM users WHERE email = '" + tempUserEmail + "'";
                                mainIntent.putExtra("group", stmt.executeQuery(query).toString());
                                query = "SELECT isManager FROM users WHERE email = '" + tempUserEmail + "'";
                                //android really hates functions it doesn't know, so we have to convert it to a string and THEN say what it is
                                if (stmt.executeQuery(query).toString().equals("1")) {
                                    mainIntent.putExtra("isManager", true);
                                } else {
                                    mainIntent.putExtra("isManager", false);
                                }
                            }
                            else {
                                //toast and reset
                                Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                                startActivity(resetIntent);
                            }
                        }
                        catch (Exception e){
                            Log.d("DB Error", "error selecting to db");
                            e.printStackTrace();
                        }
                    }
                    catch (SQLException throwables) {
                        Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_LONG).show();
                        throwables.printStackTrace();
                    }

                     */


            }
        );


    }
}