package com.purdueplaza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;

public class EventActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        invokeWS(getIntent().getStringExtra("event_id"));
        super.onCreate(savedInstanceState);
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
        RequestParams params = new RequestParams();
        invokeWS(event_id);
    }

    public void invokeWS(String event_id) {
        AsyncHttpClient client = new AsyncHttpClient();

                /*  Load API key from prefs.    */
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String key = settings.getString("key", "");
        client.addHeader("Authorization", key);

        client.get("http://167.88.118.116/events/" + event_id, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                displayResponse(response);
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                displayResponse(content);
            }
        });
    }

    public void displayResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.getBoolean("error") == false) {
                //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                TextView nme = (TextView) findViewById(R.id.eventTitle);
                nme.setText(obj.getString("name"));
                TextView dsc = (TextView) findViewById(R.id.eventDescription);
                dsc.setText(obj.getString("desc"));
                TextView start = (TextView) findViewById(R.id.startTime);
                start.setText(obj.getString("start"));
                TextView end = (TextView) findViewById(R.id.endTime);
                end.setText(obj.getString("end"));
                Button button = (Button) findViewById(R.id.attend_button);
                button.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

}
