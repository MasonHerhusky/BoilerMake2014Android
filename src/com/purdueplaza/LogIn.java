package com.purdueplaza;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.content.Intent;



import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LogIn extends Activity {

    private EditText username;
    private EditText password;
    private Button submit_button;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.login);
        addListenerOnButton();

    }

    public void addListenerOnButton() {

        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);
        submit_button = (Button) findViewById(R.id.submit_button);

        submit_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                submit(v);

            }

        });
    }

    public void submit(View view) {
        String email = username.getText().toString();
        String pass = password.getText().toString();
        RequestParams params = new RequestParams();
        /*  We don't need to check if parameters are correct, they are checked on the server side.  */
        params.put("email", email);
        params.put("password", pass);
        invokeWS(params);
    }

    public void invokeWS(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://167.88.118.116/login",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    //how to access the obj's api code and store in memory so user can stay logged in?

                    if(obj.getBoolean("error") == false){
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();

                        /*  Store the new found key in storage for future use.  */
                        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("key", obj.getString("apiKey"));
                        editor.commit();

                        navigateToMainActivity();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Error 404: Requested resource not found.", Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Error 500: Something went wrong at server end.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error " + statusCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void navigateToMainActivity(){
        Intent mainIntent = new Intent(getApplicationContext(),MyActivity.class);
        startActivity(mainIntent);
    }

}
