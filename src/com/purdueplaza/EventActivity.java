package com.purdueplaza;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class EventActivity extends Activity{

    String event_id;
    boolean isOwner;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        event_id = getIntent().getStringExtra("event_id");
        invokeWS();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);
        Button attend_button = (Button) findViewById(R.id.attend_button);
        attend_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attend();
            }
        });
        Button unattend_button = (Button) findViewById(R.id.unattend_button);
        unattend_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unattend();
            }
        });
        Button all_attend_button = (Button) findViewById(R.id.all_attending_button);
        all_attend_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent userIntent = new Intent(getApplicationContext(),EventAttendance.class);
                userIntent.putExtra("event_id", event_id);
                startActivity(userIntent);
            }
        });
    }

    public void attend() {
        invokeWSAttend();
    }

    public void unattend() {
        invokeWSUnattend();
    }

    public void invokeWS() {
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
                TextView nme = (TextView) findViewById(R.id.eventTitle);
                nme.setText(obj.getString("name"));
                TextView dsc = (TextView) findViewById(R.id.eventDescription);
                dsc.setText(obj.getString("desc"));
                TextView start = (TextView) findViewById(R.id.startTime);
                start.setText(obj.getString("start"));
                TextView end = (TextView) findViewById(R.id.endTime);
                end.setText(obj.getString("end"));
                TextView attendance_text = (TextView) findViewById(R.id.all_attending_text);
                attendance_text.setText(obj.getInt("numAttending") + " in attendance.");
                Button attendance = (Button) findViewById(R.id.all_attending_button);
                attendance.setVisibility(View.VISIBLE);
                if(isOwner) {
                    //Don't show any buttons.
                }else if(obj.getBoolean("attending")) {
                    Button button = (Button) findViewById(R.id.unattend_button);
                    button.setVisibility(View.VISIBLE);
                } else {
                    Button button = (Button) findViewById(R.id.attend_button);
                    button.setVisibility(View.VISIBLE);
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
                Intent editIntent = new Intent(getApplicationContext(),EditEvent.class);
                editIntent.putExtra("event_id", event_id);
                startActivity(editIntent);
                return true;
            case R.id.delete:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                invokeWSDelete();
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

    public void invokeWSDelete() {
        AsyncHttpClient client = new AsyncHttpClient();

                /*  Load API key from prefs.    */
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String key = settings.getString("key", "");
        client.addHeader("Authorization", key);

        client.delete("http://167.88.118.116/events/" + event_id, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                displayDeleteResponse(response);
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                displayDeleteResponse(content);
            }
        });
    }
    public void displayDeleteResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
            if(!obj.getBoolean("error")) {
                Intent mainIntent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(mainIntent);
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

    public void invokeWSAttend() {
        AsyncHttpClient client = new AsyncHttpClient();

                /*  Load API key from prefs.    */
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String key = settings.getString("key", "");
        client.addHeader("Authorization", key);

        client.post("http://167.88.118.116/events/attend/" + event_id, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                displayAttendResponse(response);
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                displayAttendResponse(content);
            }
        });
    }
    public void displayAttendResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")) {
                Toast.makeText(getApplicationContext(), "You are now marked as attending!", Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0,0);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

    public void invokeWSUnattend() {
        AsyncHttpClient client = new AsyncHttpClient();

                /*  Load API key from prefs.    */
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String key = settings.getString("key", "");
        client.addHeader("Authorization", key);

        client.delete("http://167.88.118.116/events/attend/" + event_id, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                displayUnattendResponse(response);
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                displayUnattendResponse(content);
            }
        });
    }
    public void displayUnattendResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")) {
                Toast.makeText(getApplicationContext(), "You have are no longer attending the event.", Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0,0);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You cannot unattend an event that you are hosting!", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

    @Override
    public void onBackPressed() {
        Intent eventIntent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(eventIntent);
    }


}
