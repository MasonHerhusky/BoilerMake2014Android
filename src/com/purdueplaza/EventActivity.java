package com.purdueplaza;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;

public class EventActivity extends Activity{

    String userId;
    boolean isOwner;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        invokeWS(getIntent().getStringExtra("event_id"));
        super.onCreate(savedInstanceState);
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

                /*  Load API key from prefs.    */
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String key = settings.getString("key", "");
        client.addHeader("Authorization", key);

        client.get("http://167.88.118.116/events/" + event_id, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                displayResponse(response);
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                displayResponse(content);
            }
        });
    }

    public void displayResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.getBoolean("error") == false) {
                MenuItem delete = menu.findItem(R.id.delete);
                MenuItem edit = menu.findItem(R.id.edit);
                isOwner = obj.getBoolean("isOwner");
                if(isOwner) {
                    edit.setVisible(true);
                    delete.setVisible(true);
                } else {
                    edit.setVisible(false);
                    delete.setVisible(false);
                }
                //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                TextView nme = (TextView) findViewById(R.id.eventTitle);
                nme.setText(obj.getString("name"));
                TextView dsc = (TextView) findViewById(R.id.eventDescription);
                dsc.setText(obj.getString("desc"));
                TextView start = (TextView) findViewById(R.id.startTime);
                start.setText(obj.getString("start"));
                TextView end = (TextView) findViewById(R.id.endTime);
                end.setText(obj.getString("end"));
                Button button = (Button) findViewById(R.id.attend_button);
                button.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.event_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(getApplicationContext(), "Editing..", Toast.LENGTH_LONG).show();
                return true;
            case R.id.delete:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(getApplicationContext(), "Not implemented yet.", Toast.LENGTH_LONG).show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
