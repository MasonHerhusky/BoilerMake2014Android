package com.purdueplaza;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.app.ListActivity;
import android.widget.AdapterView.OnItemClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONObject;
import java.io.FileReader;

import java.util.ArrayList;

import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



/**
 * Created by masonherhusky on 10/18/14.
 */
public class HomeActivity extends Activity{

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = (ListView) findViewById(R.id.listView);
        ArrayList<String> values = new ArrayList<String>();

      /*  AsyncHttpClient client = new AsyncHttpClient();
        client.get("167.88.118.116/events" ,new AsyncHttpResponseHandler() {
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String[] events = new String[10];
                    for(int i = 0 ; i < 10 ; i++){
                        String x = (String) obj.get("email");
                        System.out.println(x);
                    }
        }
        */



        FileReader reader = new FileReader(filePath);

        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

        String firstName = (String) jsonObject.get("firstname");

        System.out.println("The first name is: " + firstName);




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }

        });
    }

    public void searchEvents() {

    }


}
