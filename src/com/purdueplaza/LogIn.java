package com.purdueplaza;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class LogIn extends Activity {

    private EditText username;
    private EditText password;
    private Button submit_button;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        addListenerOnButton();

    }

    public void addListenerOnButton() {

        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);
        submit_button = (Button) findViewById(R.id.submit_button);

        submit_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(LogIn.this, username.getText(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LogIn.this, password.getText(), Toast.LENGTH_SHORT).show();

            }

        });

    }

}
