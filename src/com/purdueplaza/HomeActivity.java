package com.purdueplaza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends Activity{

    private ListView list;
    final ArrayList<String> names = new ArrayList<String>();
    final ArrayList<String> descriptions = new ArrayList<String>();
    private Button register_event_button;
    private Button search_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = (ListView) findViewById(R.id.listView);
/*
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, null, null);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clickedEvent(id);
            }
        });
*/
        /*  Fetch Event Data    */
        RequestParams params = new RequestParams();
        params.put("page", 0);
        invokeWS(params);
    }

    public void invokeWS(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();

        /*  Load API key from prefs.    */
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String key = settings.getString("key", "");
        client.addHeader("Authorization", key);

        client.get("http://167.88.118.116/events", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                displayResponse(response);
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if(content == null)
                    Toast.makeText(getApplicationContext(), "Something has gone very wrong! Please check your internet connection!", Toast.LENGTH_LONG).show();
                else
                    displayResponse(content);
            }
        });
    }

    public void displayResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.getBoolean("error") == false){
                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                JSONArray events = obj.getJSONArray("events");
                for(int i = 0; i < events.length(); i++)
                    System.out.println("EVENT #" + i + ": " + events.get(i));
                //Populate list
            }
            else{
                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void addListenerOnButton() {

        register_event_button = (Button) findViewById(R.id.register_button);

        register_event_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                register(v);

            }

        });

        search_button = (Button) findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                searchEvents(v);

            }

        });
    }

    public void register(View view){
        Intent registerIntent = new Intent(getApplicationContext(),RegisterEvent.class);
        startActivity(registerIntent);
    }


    public void searchEvents(View view) {

    }

    public void clickedEvent(long id) {
        Intent eventIntent = new Intent(getApplicationContext(),Event.class);
        startActivity(eventIntent);
        //TODO store event ID in intent to send it to Event activity
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);  //On back button press on this page, close the app.
    }

}
