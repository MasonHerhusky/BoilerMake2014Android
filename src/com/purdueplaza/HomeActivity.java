package com.purdueplaza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.LinkedList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends Activity{

    private ListView eventList;
    private Button register_event_button;

    ArrayList<String> name_array = new ArrayList<String>();
    ArrayList<String> desc_array = new ArrayList<String>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        /*  Fetch Event Data    */
        RequestParams params = new RequestParams();
        params.put("page", 0);
        invokeWS(params);
        list = (ListView) findViewById(R.id.listView);
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
                    Toast.makeText(getApplicationContext(), "Check your internet connection!", Toast.LENGTH_LONG).show();
                else
                    displayResponse(content);
            }
        });
    }

    public void displayResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray events = obj.getJSONArray("events");
            if(obj.getBoolean("error") == false){
                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                name_array = new ArrayList<String>();
                desc_array = new ArrayList<String>();
                for (int i = 0, count = events.length(); i < count; i++) {
                    try {
                        name_array.add(events.getJSONObject(i).getString("name"));
                        desc_array.add(events.getJSONObject(i).getString("desc"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(obj.toString());
                System.out.println(name_array.toString());
                System.out.println(desc_array.toString());

                String[][] values = new String[2][];
                values[0] = (String[]) name_array.toArray();
                values[1] = (String[]) desc_array.toArray();
                int[] types = new int[]{android.R.id.text1, android.R.id.text2};

             //   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, values, types);
             //   list.setAdapter(adapter);
            } else {
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

                registerEvent(v);

            }

        });
    }

    public void registerEvent(View view){
        Intent registerIntent = new Intent(getApplicationContext(),RegisterEvent.class);
        startActivity(registerIntent);
    }


    public void searchEvents(View view) {

    }

    public void clickedEvent(long id) {
        Intent eventIntent = new Intent(getApplicationContext(),EventActivity.class);
        startActivity(eventIntent);
        //TODO store event ID in intent to send it to Event activity
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);  //On back button press on this page, close the app.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                SharedPreferences preferences = getSharedPreferences("Login", 0);
                preferences.edit().remove("key").commit();

                Intent mainIntent = new Intent(getApplicationContext(),MyActivity.class);
                Toast.makeText(getApplicationContext(), "You have successfully logged out.", Toast.LENGTH_LONG).show();
                startActivity(mainIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
