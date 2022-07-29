package com.example.airrandsredux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airrandsredux.databinding.ActivityMainBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView mTextView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        //our arraylist
        ArrayList<String> tasks = new ArrayList<String>();

        //init switch
        Switch switch1 = (Switch) findViewById(R.id.switch1);
        Switch switch2 = (Switch) findViewById(R.id.switch2);
        Switch switch3 = (Switch) findViewById(R.id.switch3);
        Switch switch4 = (Switch) findViewById(R.id.switch4);
        Switch switch5 = (Switch) findViewById(R.id.switch5);
        Switch switch6 = (Switch) findViewById(R.id.switch6);
        Switch switch7 = (Switch) findViewById(R.id.switch7);
        Switch switch8 = (Switch) findViewById(R.id.switch8);
        Switch switch9 = (Switch) findViewById(R.id.switch9);
        Switch switch10 = (Switch) findViewById(R.id.switch10);

        //mTextView = binding.text;

        //creating list
        Button sendButton = (Button) findViewById(R.id.getButton);
        sendButton.setOnClickListener( new View.OnClickListener(){
            public void onClick (View v){

                //each just sets text of a switch, makes it visible, and gives a debug log

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //define the driver
                            Class.forName("com.mysql.jdbc.Driver");
                            //connect to the database
                            Connection con = DriverManager.getConnection("jdbc:mysql://72.85.159.59:3306/company?autoReconnect=true", "airrandsapp", "pass");
                            Log.d("Debug", "Connected to database");

                            //this gets the length of the table and stores it for later
                            int length = 0;
                            try (Statement stmt = con.createStatement()) {
                                try (ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM work.task WHERE groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group"))+ "'")) {
                                    resultSet.next();
                                    length = resultSet.getInt(1);
                                    Log.d("Debug", "Finished reading length: "+length);

                                }
                                catch (Exception e) {

                                    Log.d("DB Error", "error setting switch to db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "error reading db");
                                e.printStackTrace();
                            }

                            //start running the query
                            try (Statement stmt = con.createStatement()) {
                                try (ResultSet resultSet = stmt.executeQuery("SELECT taskName FROM work.task WHERE groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group"))+ "'")) {
                                    //go to the first row of the result set and start reading
                                    //compare to previous length int to get the length of what we need

                                    for (int i = 0; i<length; i++) {
                                        resultSet.next();
                                        String task = resultSet.getString("taskName");
                                        Log.d("Debug", "Finished reading task: "+task);
                                        tasks.add(task);
                                    }

                                    //mainIntent.putExtra("group", resultSet.getString("groupNum"));
                                    //Log.d("Debug", "Group: " + resultSet.getString("groupNum"));

                                    Log.d("Debug", "Finished reading");

                                }
                                catch (Exception e) {
                                    Log.d("DB Error", "error setting switch to db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "error reading db");
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

        Button showButton = (Button) findViewById(R.id.showButton);
        showButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                //Android doesn't allow you to change the text of a switch from
                //a different thread, so we need to go back to the original to update UI
                for (int i = 0; i < tasks.size(); i++) {
                    //this switch lets us update stuff without having a
                    //crash be the error handler
                    //i am slowly losing my grip
                    switch (i) {
                        case 0:
                            switch1.setText(tasks.get(i));
                            switch1.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            switch2.setText(tasks.get(i));
                            switch2.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            switch3.setText(tasks.get(i));
                            switch3.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            switch4.setText(tasks.get(i));
                            switch4.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            switch5.setText(tasks.get(i));
                            switch5.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            switch6.setText(tasks.get(i));
                            switch6.setVisibility(View.VISIBLE);
                            break;
                        case 6:
                            switch7.setText(tasks.get(i));
                            switch7.setVisibility(View.VISIBLE);
                            break;
                        case 7:
                            switch8.setText(tasks.get(i));
                            switch8.setVisibility(View.VISIBLE);
                            break;
                        case 8:
                            switch9.setText(tasks.get(i));
                            switch9.setVisibility(View.VISIBLE);
                            break;
                        case 9:
                            switch10.setText(tasks.get(i));
                            switch10.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        });

        //these next switch liseners wait until they're checked then
        //update the database with the new value
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch1.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch2.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });


        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch3.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch3.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch3.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch4.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch5.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        switch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch6.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        switch7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch7.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        switch8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch8.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        switch9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch9.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        switch10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                                    //normally we would just do the query in the line but this is easier to read
                                    String query = "UPDATE work.task SET endDate = '"+ new Timestamp(System.currentTimeMillis()) + "' WHERE taskName = '" + switch10.getText() + "' AND groupNum = '" + Integer.parseInt(getIntent().getStringExtra("group")) + "'";
                                    stmt.executeUpdate(query);

                                    Log.d("Debug", "Finished updating");
                                    //send number of rows to log
                                    int rows = stmt.getUpdateCount();
                                    Log.d("Database update:", rows + " rows updated");
                                } catch (Exception e) {
                                    Log.d("DB Error", "error reading db");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.d("DB Error", "can't connect to DB");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else {

                }
            }
        });

        //go to the group code screen
        Button groupScreen = findViewById(R.id.groupCodeButton);
        //creating the Intent to go to the login activity
        Intent goToGroup = new Intent(this, groupCodeActivity.class);
        //button listener
        groupScreen.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(goToGroup);
                }
            }
        );
    }
}