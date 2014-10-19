package com.purdueplaza;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventAttendance extends Activity {

    ArrayList<String> user_array = new ArrayList<String>();
    ListView list;

    String event_id;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        event_id = getIntent().getStringExtra("event_id");
        invokeWS();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_attendance);
        list = (ListView) findViewById(R.id.attendList);
    }

    public void invokeWS(){
        AsyncHttpClient client = new AsyncHttpClient();

         /*  Load API key from prefs.    */
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String key = settings.getString("key", "");
        client.addHeader("Authorization", key);

        client.get("http://167.88.118.116/events/attend/" + event_id, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                displayResponse(response);
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (content == null)
                    Toast.makeText(getApplicationContext(), "Something has gone very wrong! Please check your internet connection!", Toast.LENGTH_LONG).show();
                else
                    displayResponse(content);
            }
        });
    }

    public void displayResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray users = obj.getJSONArray("attending");
            if(obj.getBoolean("error") == false){
                for(int i = 0; i < users.length(); i++) {
                   user_array.add(users.get(i).toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, user_array);
                list.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "You've hit the bottom!", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent eventIntent = new Intent(getApplicationContext(),EventActivity.class);
        eventIntent.putExtra("event_id", event_id);
        startActivity(eventIntent);
    }

}