package com.purdueplaza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.view.View;
import android.app.ListActivity;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by masonherhusky on 10/18/14.
 */
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

        RequestParams params = new RequestParams();
        params.put("page", 1);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://167.88.118.116/events", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error") == false) {
                        JSONArray events = obj.getJSONArray("events");
                        for(int i = 0; i < events.length(); i++) {
                            JSONObject c = events.getJSONObject(i);
                            String name = c.getString("name");
                            String description = c.getString("desc");
                            System.out.println ("name" + i + " " + name);
                            names.add(name);
                            System.out.println ("description" + i + " " + description);
                            descriptions.add(description);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Error 404: Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Error 500: Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect email address", Toast.LENGTH_LONG).show();
                }
            }
        });

        ArrayAdapter<String> adapter = new Adapter(this, android.R.layout.simple_list_item_1, names);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clickedEvent(id);
            }
        });



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
    public void addListenerOnButton() {

        search_button = (Button) findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                searchEvents(v);

            }

        });
    }

    public void register(){
        Intent registerIntent = new Intent(getApplicationContext(),RegisterEvent.class);
        startActivity(registerIntent);
    }


    public void searchEvents() {

    }

    public void clickedEvent(long id) {
        Intent registerIntent = new Intent(getApplicationContext(),Event.class);
        startActivity(registerIntent);
        //TODO store event ID in intent to send it to Event activity
    }


}
