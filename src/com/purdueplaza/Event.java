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
        Button rsvp_button = (Button) findViewById(R.id.rsvp_button);
        rsvp_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rsvp(v);
            }
        });
    });



}
