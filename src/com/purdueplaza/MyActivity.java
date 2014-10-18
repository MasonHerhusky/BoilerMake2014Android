package com.purdueplaza;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

public class MyActivity extends Activity {

    private EditText password;
    private Button submit_button;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String key = settings.getString("key", "PurduePlaza");
    /*    if(key != null && key != "") {   //We have the key stored, so we're already auth'ed.
            System.out.println("KEY: " + key);
            Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(homeIntent);
        } */

        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.main);
        Button register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register(v);
            }
        });
        Button log_in_button = (Button) findViewById(R.id.log_in_button);
        log_in_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 logIn(v);
            }
        });

    }

    public void logIn(View view) {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
