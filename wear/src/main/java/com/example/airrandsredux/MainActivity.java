package com.example.airrandsredux;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.airrandsredux.databinding.ActivityMainBinding;

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

        //debug tasks, will replace with database grab
        tasks.add("Debug 1");
        tasks.add("Debug 2");
        tasks.add("Debug 3");
        tasks.add("Debug 4");
        tasks.add("Debug 5");

        //init switch
        Switch switch1 = (Switch) findViewById(R.id.switch1);
        Switch switch2 = (Switch) findViewById(R.id.switch2);
        Switch switch3 = (Switch) findViewById(R.id.switch3);
        Switch switch4 = (Switch) findViewById(R.id.switch4);
        Switch switch5 = (Switch) findViewById(R.id.switch5);

        //mTextView = binding.text;

        //creating list
        Button sendButton = (Button) findViewById(R.id.getButton);
        sendButton.setOnClickListener( new View.OnClickListener(){
            public void onClick (View v){

                //each just sets text of a switch, makes it visible, and gives a debug log

                switch1.setText(tasks.get(0));
                switch1.setVisibility(View.VISIBLE);
                Log.d("Debug tasks",tasks.get(0));

                switch2.setText(tasks.get(1));
                switch2.setVisibility(View.VISIBLE);
                Log.d("Debug tasks",tasks.get(1));

                switch3.setText(tasks.get(2));
                switch3.setVisibility(View.VISIBLE);
                Log.d("Debug tasks",tasks.get(2));

                switch4.setText(tasks.get(3));
                switch4.setVisibility(View.VISIBLE);
                Log.d("Debug tasks",tasks.get(3));

                switch5.setText(tasks.get(4));
                switch5.setVisibility(View.VISIBLE);
                Log.d("Debug tasks",tasks.get(4));

            }
        });
    }
}