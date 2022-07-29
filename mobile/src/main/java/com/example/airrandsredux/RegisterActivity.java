package com.example.airrandsredux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {

    public String userEmail = "dans@test.com";
    public String hashedPass = "test1234";
    public String userFirstName = "Dan";
    public String userLastName = "Smith";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //declare message boxes
        EditText regEmail = findViewById(R.id.regEmailBox);
        EditText regPass = findViewById(R.id.regPassBox);
        EditText regConfirmPass = findViewById(R.id.regPassConfBox);
        EditText regFirstName = findViewById(R.id.regFirstBox);
        EditText regLastName = findViewById(R.id.regLastBox);
        EditText regGroup = findViewById(R.id.regGroupBox);
        EditText regEmp = findViewById(R.id.regEmpID);
        EditText regManager = findViewById(R.id.regManagerBox);

        //boolean to keep track of managers
        //final boolean[] isManager = {false};
        final int[] isManager = {0};

        //for clearing screen later
        Intent regReset = new Intent(this, RegisterActivity.class);


        //this is basically all the register login - nothing runs until the button is pressed
        Button registerRegScreen = findViewById(R.id.regRegButton);
        //creating the Intent to go to the register activity
        Intent regMainIntent = new Intent(this, MainActivity.class);
        //button listener
        registerRegScreen.setOnClickListener(new View.OnClickListener(){
                public void onClick (View v) {

                    //all these ifs are to make sure that the user has filled out all the fields
                    if (regEmail.getText().toString().compareTo("") == 0) {
                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                    }
                    else if (regPass.getText().toString().compareTo("") == 0) {
                        Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                    }
                    else if (regConfirmPass.getText().toString().compareTo("") == 0) {
                        Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                    }
                    else if (regFirstName.getText().toString().compareTo("") == 0) {
                        Toast.makeText(getApplicationContext(), "Invalid first name", Toast.LENGTH_SHORT).show();
                    }
                    else if (regLastName.getText().toString().compareTo("") == 0) {
                        Toast.makeText(getApplicationContext(), "Invalid last name", Toast.LENGTH_SHORT).show();
                    }
                    else if (regGroup.getText().toString().compareTo("") == 0) {
                        Toast.makeText(getApplicationContext(), "Invalid group number", Toast.LENGTH_SHORT).show();
                    }
                    else if (regEmp.getText().toString().compareTo("") == 0) {
                        Toast.makeText(getApplicationContext(), "Invalid employee ID", Toast.LENGTH_SHORT).show();
                    }
                    //password same check
                    else if (regPass.getText().toString().compareTo(regConfirmPass.getText().toString()) != 0) {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        //checks if user has the right manager code, sets boolean to true if so
                        // code is "iamthemanager"
                        if (regManager.getText().toString().compareTo("iamthemanager") == 0) {
                            Toast.makeText(getApplicationContext(), "Manager enabled", Toast.LENGTH_SHORT).show();
                            isManager[0] = 1;
                        }

                        //if all the fields are filled out, then we send to the database and go to the main activity - if the user is not a manager, the mainActivity will send them into employee view
                        //new thread for network connection

                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try  {
                                    try (Connection con = DriverManager.getConnection("jdbc:mysql://72.85.159.59:3306/mysql", "airrandsapp", "pass")) {
                                        try (Statement stmt = con.createStatement()) {
                                            //creates the query to insert the user into the database
                                            String query = "INSERT INTO company.employee (email, empPassword, employeeFirstName, employeeLastName, groupNum, isManager, employeeID) VALUES ('" + regEmail.getText().toString() + "', '" + DigestUtils.sha256Hex(regPass.getText().toString()) + "', '" + regFirstName.getText().toString() + "', '" + regLastName.getText().toString() + "', '" + Integer.parseInt(regGroup.getText().toString()) + "', '" + isManager[0] + "', '" + regEmp.getText().toString() + "')";
                                            //executes the query
                                            stmt.executeUpdate(query);
                                            //send number of rows to log
                                            int rows = stmt.getUpdateCount();
                                            Log.d("Database update:", rows + " rows updated");
                                            //send back data with the intent
                                            regMainIntent.putExtra("userEmail", regEmail.getText().toString());
                                            regMainIntent.putExtra("hashedPass", DigestUtils.sha256Hex(regPass.getText().toString()));
                                            regMainIntent.putExtra("firstName", regFirstName.getText().toString());
                                            regMainIntent.putExtra("lastName", regLastName.getText().toString());
                                            regMainIntent.putExtra("group", regGroup.getText().toString());
                                            regMainIntent.putExtra("empID", regEmp.getText().toString());
                                            regMainIntent.putExtra("manager", isManager[0]);
                                            startActivity(regMainIntent);
                                        }
                                        catch (Exception e){
                                            Log.d("DB Error", "error adding to db");
                                            e.printStackTrace();
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        Log.d("DB Error", "error connecting to db");
                                    }
                                }
                                catch (Exception e) {
                                    Log.d("DB Error", "can't connect to DB");
                                    e.printStackTrace();
                                }
                            }
                        }).start();



                    }
                    //clear the screen if it breaks
                    startActivity(regReset);
                }
            }
        );

    }
}