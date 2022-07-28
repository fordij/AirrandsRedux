package com.example.airrandsredux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check to see if we were given an intent that we're not a manager
        if (getIntent().getBooleanExtra("manager", false)) {
            //if we were, go to the manager view
            Intent employeeIntent = new Intent(this, employeeViewTasks.class);
            //forward all mainIntent data to the employeeIntent
            employeeIntent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
            employeeIntent.putExtra("hashedPass", getIntent().getStringExtra("hashedPass"));
            employeeIntent.putExtra("firstName", getIntent().getStringExtra("firstName"));
            employeeIntent.putExtra("lastName", getIntent().getStringExtra("lastName"));
            employeeIntent.putExtra("group", getIntent().getStringExtra("group"));
            employeeIntent.putExtra("manager", getIntent().getBooleanExtra("manager", false));
            startActivity(employeeIntent);
        }

        //init textview
        TextView textView = findViewById(R.id.toDos);

        //init arraylist for goals (probably will make a sister array of booleans if they're done)
        ArrayList<String> tasks = new ArrayList<String>();

        //declare message box
        EditText message = findViewById(R.id.listInput);

        String firstNameIntent = getIntent().getStringExtra("firstName");

        TextView firstName = findViewById(R.id.nameView);
        if (firstNameIntent != null) {
            firstName.setText("Good morning, " + firstNameIntent + "!");
        }

        //this handles the "create list" button on the home screen, updates the textview, and adds them to an arraylist
        Button newList = findViewById(R.id.listButton);
        newList.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                Log.d("debug group", getIntent().getStringExtra("group"));
                //check if the input is empty, throw a toast if it is
                if (message.getText().toString().compareTo("") == 0) {
                    Toast.makeText(getApplicationContext(), "Empty goal", Toast.LENGTH_SHORT).show();
                    //newList.setVisibility(View.INVISIBLE);
                }
                else {
                    //send each task to the database
                    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost", "airrandsapp", "pass")) {
                        try (Statement stmt = con.createStatement()) {
                            //creates the query to insert the user into the database
                            String query = "INSERT INTO work.task (taskID, taskName, group, beginDate) VALUES ('" + Math.floor(Math.random()*(9999999-0+1)+0) + "', '" + message.getText().toString() + "', '" + Integer.parseInt(getIntent().getStringExtra("group")) + "', '" + new Timestamp(System.currentTimeMillis()) + "', '" + "')";
                            //executes the query
                            stmt.executeUpdate(query);
                            //send number of rows to log
                            int rows = stmt.getUpdateCount();
                            Log.d("Database update:", rows + " rows updated");
                            Toast.makeText(getApplicationContext(), "Task added", Toast.LENGTH_LONG).show();
                            //add the task
                            tasks.add(message.getText().toString());
                            //modify the view by slapping more text on
                            textView.setText(textView.getText() + "\n" + message.getText().toString());
                            Log.d("Debug text", message.getText().toString() + " added");
                            //clear textbox
                            message.setText(null);
                        }
                        catch (Exception e){
                            Log.d("DB Error", "error adding to db");
                            e.printStackTrace();
                        }

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

            }
        }
        );

        //this updates the firstname textview with "Sent!" if the user clicks the "send" button
        Button sendButton = findViewById(R.id.sendTo);
        sendButton.setOnClickListener(new View.OnClickListener(){
                public void onClick (View v){
                    firstName.setText("Sent!");
                }
            }
        );

        //go to the login screen
        Button loginScreen = findViewById(R.id.signButton);
        //creating the Intent to go to the login activity
        Intent loginIntent = new Intent(this, LoginActivity.class);
        //button listener
        loginScreen.setOnClickListener(new View.OnClickListener(){
                public void onClick (View v){
                    startActivity(loginIntent);
                }
            }
        );



    }




}