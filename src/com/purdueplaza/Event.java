package com.purdueplaza;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;

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
        RequestParams params = new RequestParams();
        invokeWS(event_id);
    }

    public void invokeWS(String event_id) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://167.88.118.116/attend/" + event_id, null ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("error") == false){
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        //TODO: After attending
                    }
                    else{
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
