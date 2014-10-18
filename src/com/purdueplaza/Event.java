package com.purdueplaza;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by masonherhusky on 10/18/14.
 */
public class Event extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.event);
        Button attend_button = (Button) findViewById(R.id.attend_button);
        attend_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String event_id = getIntent().getStringExtra("event_id");
                attend(event_id);
            }
        });
    }

    public void attend(String event_id) {

    }



}
