package com.example.airrandsredux;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init textview
        TextView textView = (TextView) findViewById(R.id.toDos);

        //init arraylist for goals (probably will make a sister array of booleans if they're done)
        ArrayList<String> tasks = new ArrayList<String>();

        //declare message box
        EditText message = (EditText)findViewById(R.id.listInput);

        //this handles the "create list" button on the home screen, updates the textview, and adds them to an arraylist
        Button newList = (Button) findViewById(R.id.listButton);
        newList.setOnClickListener( new View.OnClickListener(){
            public void onClick (View v){
                //check if the input is empty, throw a toast if it is
                if (message.getText().toString().compareTo("") == 0) {
                    Toast.makeText(getApplicationContext(), "Empty goal", Toast.LENGTH_SHORT).show();
                    //newList.setVisibility(View.INVISIBLE);
                }
                else {
                    //add the task
                    tasks.add(message.getText().toString());
                    //modify the view by slapping more text on
                    textView.setText(textView.getText() + "\n" + message.getText().toString());
                    Log.d("Debug text", message.getText().toString() + " added");
                    //clear textbox
                    message.setText(null);
                }

            }
        }
        ); //onclicklistener

        /*create FTP objects
        FTPClient client = new FTPClient();
        FileInputStream fis = null;
        String filename = "tasks.txt";

        //sending to watch via FTP
        Button sendButton = (Button) findViewById(R.id.sendTo);
        sendButton.setOnClickListener( new View.OnClickListener()
                                       {
                                           public void onClick (View v){
                                               Log.d("Debug text",message.getText().toString());
                                               uploadFTP.FTPup(tasks);
                                           }
                                       }
        ); //onclicklistener
         */
    }
}