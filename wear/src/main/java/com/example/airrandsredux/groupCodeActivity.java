package com.example.airrandsredux;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class groupCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_code);

        TextView textView = (TextView) findViewById(R.id.groupCode);

        //go to the group code screen
        Button groupScreen = findViewById(R.id.sendGroup);
        //creating the Intent to go to the login activity
        Intent goToGroup = new Intent(this, MainActivity.class);
        //button listener
        groupScreen.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //start the main activity
                    goToGroup.putExtra("group", textView.getText().toString());
                    startActivity(goToGroup);
                }
            }
        );
    }
}