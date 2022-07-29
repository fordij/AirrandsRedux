/*
AIRRANDS - A HYBRID MOBILE/SMARTWATCH TO-DO APP
CODED BY JACE FORDI
DATABASE BY HUA VAN

public libraries used:
- MySQL Connector for Java 5.1.49 (Android depreciated some functionality newer versions relied on)
    - while there are more Java-first SQL libraries (like jOOQ), I have more experience
      with running direct SQL queries on a MySQL database from my senior co-op
- Apache Common Codec for hashing passwords
- Multiple built-in Android libraries

notes on how this was built:

- an emulated Pixel 4 was used for testing the mobile app
- an emulated Galaxy Watch 4 was used for testing the smartwatch app
- both were built using the Android Studio IDE
- we used a MySQL database to store the data
    - integration hell is called integration hell for a reason

 */

package com.example.airrandsredux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.*;
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


        //check to see if we were given an intent that we're not a manager
        if (getIntent().getIntExtra("manager", 1) == 0) {
            //if we were, go to the manager view
            Intent employeeIntent = new Intent(this, employeeViewTasks.class);
            //forward all mainIntent data to the employeeIntent
            employeeIntent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
            employeeIntent.putExtra("hashedPass", getIntent().getStringExtra("hashedPass"));
            employeeIntent.putExtra("firstName", getIntent().getStringExtra("firstName"));
            employeeIntent.putExtra("lastName", getIntent().getStringExtra("lastName"));
            employeeIntent.putExtra("group", getIntent().getStringExtra("group"));
            employeeIntent.putExtra("manager", getIntent().getIntExtra("manager", 0));
            startActivity(employeeIntent);
        }

        //this handles the "create list" button on the home screen, updates the textview, and adds them to an arraylist
        Button newList = findViewById(R.id.listButton);
        newList.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {
                                           //check if the input is empty, throw a toast if it is
                                           if (message.getText().toString().compareTo("") == 0) {
                                               Toast.makeText(getApplicationContext(), "Empty goal", Toast.LENGTH_SHORT).show();
                                               //newList.setVisibility(View.INVISIBLE);
                                           } else {
                                               //add the task
                                               tasks.add(message.getText().toString());
                                               //modify the view by slapping more text on
                                               textView.setText(textView.getText() + "\n" + message.getText().toString());
                                               //debug message
                                               Log.d("Debug text", tasks.get(0) + " added");
                                               //message.getText().toString()
                                               //clear textbox
                                               message.setText(null);
                                               //send toast to user
                                               Toast.makeText(getApplicationContext(), "Task added", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   }
        );

        //this updates the firstname textview with "Sent!" if the user clicks the "send" button
        Button sendButton = findViewById(R.id.sendTo);
        sendButton.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {

                                              //every instance of this "new Thread" code makes a new thread to run network code on
                                              //Android doesn't allow you to run network code on the main thread, so we need to make a new thread for it
                                              new Thread(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      try {
                                                          //define the driver
                                                          Class.forName("com.mysql.jdbc.Driver");
                                                          //connect to the database
                                                          Connection con = DriverManager.getConnection("jdbc:mysql://72.85.159.59:3306/company?autoReconnect=true", "airrandsapp", "pass");
                                                          Log.d("Debug", "Connected to database");

                                                          //start running the query
                                                          try (Statement stmt = con.createStatement()) {
                                                              for (int i = 0; i < tasks.size(); i++) {
                                                                  //creates the query to insert the user into the database
                                                                  String query = "INSERT INTO work.task (taskID, taskName, groupNum, beginDate) VALUES ('" + Math.floor(Math.random() * (9999999 - 0 + 1) + 0) + "', '" + tasks.get(i) + "', '" + Integer.parseInt(getIntent().getStringExtra("group")) + "', '" + new Timestamp(System.currentTimeMillis()) + "')";
                                                                  //executes the query
                                                                  stmt.executeUpdate(query);
                                                                  //send number of rows to log
                                                                  int rows = stmt.getUpdateCount();
                                                                  Log.d("Database update:", rows + " rows updated, i = " + i);

                                                              }
                                                          } catch (Exception e) {
                                                              Log.d("DB Error", "error adding to db");
                                                              e.printStackTrace();
                                                          }
                                                      } catch (Exception e) {
                                                          Log.d("DB Error", "can't connect to DB");
                                                          e.printStackTrace();
                                                      }
                                                  }
                                              }).start();
                                              //quick to send a toast to the user
                                              Log.d("Debug", "updated text");
                                              Toast.makeText(getApplicationContext(), "Tasks sent", Toast.LENGTH_SHORT).show();
                                              firstName.setText("Sent!");

                                          }
                                      }
        );

        //go to the login screen
        Button loginScreen = findViewById(R.id.signButton);
        //creating the Intent to go to the login activity
        Intent loginIntent = new Intent(this, LoginActivity.class);
        //button listener
        loginScreen.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {
                                               startActivity(loginIntent);
                                           }
                                       }
        );

        //delete group tasks from db
        Button deleteButton = findViewById(R.id.resetGroupTasks);
        //button listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //every instance of this "new Thread" code makes a new thread to run network code on
                //Android doesn't allow you to run network code on the main thread, so we need to make a new thread for it
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //define the driver
                            Class.forName("com.mysql.jdbc.Driver");
                            //connect to the database
                            Connection con = DriverManager.getConnection("jdbc:mysql://72.85.159.59:3306/company?autoReconnect=true", "airrandsapp", "pass");
                            Log.d("Debug", "Connected to database");

                            //start running the query
                            try (Statement stmt = con.createStatement()) {
                                String query = "DELETE FROM work.task WHERE groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                stmt.executeUpdate(query);
                                int rows = stmt.getUpdateCount();
                                Log.d("Database update:", rows + " rows updated");
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "Tasks deleted", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d("DB Error", "error removing to db");
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            Log.d("DB Error", "can't connect to DB");
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

}