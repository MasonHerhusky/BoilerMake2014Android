package com.purdueplaza;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 *
 * Register Activity Class
 */
public class RegisterEvent extends Activity {

    private EditText name;
    private EditText desc;
    private EditText start;
    private EditText end;
    private Button register_button;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_event);
        name = (EditText) findViewById(R.id.name_field);
        desc = (EditText) findViewById(R.id.description_field);
        start = (EditText) findViewById(R.id.startTime_field);
        end = (EditText) findViewById(R.id.endTime_field);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submit(v);
            }

        });
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void submit(View view) {
        String eventName = name.getText().toString();
        String description = desc.getText().toString();
        String startTime = start.getText().toString();
        String endTime = end.getText().toString();
        RequestParams params = new RequestParams();
        /*  We don't need to check if parameters are correct, they are checked on the server side.  */
        params.put("name", eventName);
        params.put("desc", description);
        params.put("start", startTime);
        params.put("end", endTime);
        invokeWS(params);
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();

        /*  Load API key from prefs.    */
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String key = settings.getString("key", "");
        client.addHeader("Authorization", key);

        client.post("http://167.88.118.116/events",params ,new AsyncHttpResponseHandler() {
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
                navigatetoHomeActivity();
            }
            else{
                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(homeIntent);
    }

}
