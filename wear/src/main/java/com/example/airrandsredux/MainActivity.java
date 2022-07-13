package com.example.airrandsredux;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.airrandsredux.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private TextView mTextView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        //mTextView = binding.text;
        Button sendButton = (Button) findViewById(R.id.getButton);
        sendButton.setOnClickListener( new View.OnClickListener(){
            public void onClick (View v){
                Log.d("Debug text","work");
            }
        });
    }
}