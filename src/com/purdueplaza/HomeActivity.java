package com.purdueplaza;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private Button register_event_button;

    ArrayList<String> event_id_array = new ArrayList<String>();
    ArrayList<String> name_array = new ArrayList<String>();
    ArrayList<String> desc_array = new ArrayList<String>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        addListenerOnButton();
        name_array = new ArrayList<String>();
        desc_array = new ArrayList<String>();
        /*  Fetch Event Data    */
        invokeWS(0);
        list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedEvent(event_id_array.get(position));
            }
        });
    }


    public void invokeWS(int page) {
        AsyncHttpClient client = new AsyncHttpClient();

        /*  Load API key from prefs.    */
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String key = settings.getString("key", "");
        client.addHeader("Authorization", key);

        client.get("http://167.88.118.116/events?page="+page, null, new AsyncHttpResponseHandler() {
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
            JSONArray events = obj.getJSONArray("events");
            if(obj.getBoolean("error") == false){
                for (int i = 0, count = events.length(); i < count; i++) {
                    try {
                        event_id_array.add(events.getJSONObject(i).getString("_id"));
                        name_array.add(events.getJSONObject(i).getString("name"));
                        desc_array.add(events.getJSONObject(i).getString("desc"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, name_array);
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);

            } else {
                Toast.makeText(getApplicationContext(), "You've hit the bottom!", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
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
    }

    public void register(View view){
        Intent registerIntent = new Intent(getApplicationContext(),RegisterEvent.class);
        startActivity(registerIntent);
    }

    public void clickedEvent(String id) {
        String newId = id.substring(8);
        newId = newId.substring(0, newId.length() - 2);
        Intent eventIntent = new Intent(getApplicationContext(),EventActivity.class);
        eventIntent.putExtra("event_id",newId);
        startActivity(eventIntent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        moveTaskToBack(true);
                    }
                }).create().show();
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
            case R.id.refresh:
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0,0);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}