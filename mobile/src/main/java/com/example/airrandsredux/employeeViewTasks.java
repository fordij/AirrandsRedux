package com.example.airrandsredux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class employeeViewTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_view_tasks);

        //init arraylist for goals (probably will make a sister array of booleans if they're done)
        ArrayList<String> tasks = new ArrayList<String>();

        TextView empTasks = findViewById(R.id.tasksEmp);

        //set the splash name to the user's name
        TextView splashName = findViewById(R.id.splashEmpText);
        splashName.setText("Have a great day, " + getIntent().getStringExtra("firstName") + "!");




        //this handles the "create list" button on the home screen, updates the textview, and adds them to an arraylist
        Button newList = findViewById(R.id.empRefeshButton);
        newList.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
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
                                    //creates the query to grab tasks based on user group number
                                    String query = "SELECT taskName from work.task WHERE groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";

                                    try (ResultSet resultSet = stmt.executeQuery(query)) {
                                        while (resultSet.next()) {
                                            tasks.add(resultSet.getString("taskName"));
                                            Log.d("Database:", resultSet.getString("taskName"));
                                        }
                                    }

                                    catch (Exception e){
                                        Log.d("DB Error", "error running query from db");
                                        e.printStackTrace();
                                    }
                            /*
                                    //executes the query
                                    Log.d("debug select", stmt.executeQuery(query) + "");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated, i = " + i);
                                    //Integer.parseInt(getIntent().getStringExtra("group")) */

                            }
                            catch (Exception e){
                                Log.d("DB Error", "error grabbing from db");
                                e.printStackTrace();
                            }
                        }
                        catch (Exception e) {
                            Log.d("DB Error", "can't connect to DB");
                            e.printStackTrace();
                        }
                    }
                }).start();
                Log.d("ArrayList tasks 2: ", tasks.size() + "");
                for (int i = 0; i < tasks.size(); i++)  {
                    Log.d("ArrayList for loop: ", tasks.get(i));
                    empTasks.setText(empTasks.getText() + "\n" + tasks.get(i));
                }
            }
        }
        );

        //go to the login screen
        Button loginScreen = findViewById(R.id.displayTasks);
        //button listener
        loginScreen.setOnClickListener(new View.OnClickListener(){
                public void onClick (View v){
                    //for SOME reason this code won't run after the refresh tasks button
                    //i had to move it into a different button listener
                    Log.d("ArrayList tasks 2: ", tasks.size() + "");
                    for (int i = 0; i < tasks.size(); i++)  {
                        Log.d("ArrayList for loop: ", tasks.get(i));
                        empTasks.setText(empTasks.getText() + "\n" + tasks.get(i));
                    }
                }
            }
        );
    }
}