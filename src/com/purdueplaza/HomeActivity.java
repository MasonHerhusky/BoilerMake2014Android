package com.purdueplaza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.view.View;
import android.app.ListActivity;
import android.widget.AdapterView.OnItemClickListener;
import java.lang.CharSequence;


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
        params.put("page", 0);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://167.88.118.116/events", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error") == false) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "Error " + statusCode + "; " + content, Toast.LENGTH_LONG).show();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        list.setAdapter(arrayAdapter);

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
/* supposedly this is how to search
    EditText inputSearch;
    inputSearch = (EditText) findViewById(R.id.search_field);
    inputSearch.addTextChangedListener(new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            // When user changed the Text
            HomeActivity.this.adapter.getFilter().filter(cs);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
        int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
        }
    }
    */

    public void clickedEvent(long id) {
        Intent eventIntent = new Intent(getApplicationContext(),Event.class);
        startActivity(eventIntent);
        //TODO store event ID in intent to send it to Event activity
    }


}
