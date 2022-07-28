package com.example.airrandsredux;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        Button newList = findViewById(R.id.listButton);
        newList.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                // TODO: Pull from DB tasks for this user
                    /*tasks.add(message.getText().toString());
                    //modify the view by slapping more text on
                    tasksEmp.setText(textView.getText() + "\n" + message.getText().toString());
                    Log.d("Debug text", message.getText().toString() + " added");
                    //clear textbox
                    message.setText(null);
                    */
            }
        }
        );
    }
}