
package com.purdueplaza;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.view.KeyEvent;
        import android.view.inputmethod.EditorInfo;
        import android.widget.TextView;
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
public class RegisterAccount extends Activity {

    private EditText name;
    private EditText email;
    private EditText password;
    private Button register_button;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_account);
        name = (EditText) findViewById(R.id.name_field);
        password = (EditText) findViewById(R.id.password_field);
        email = (EditText) findViewById(R.id.email_field);
        addListenerOnButton();
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                submit(v);
                return true;
            }
        });
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
        String nme = name.getText().toString();
        String eml = email.getText().toString();
        String psswrd = password.getText().toString();
        RequestParams params = new RequestParams();
        /*  We don't need to check if parameters are correct, they are checked on the server side.  */
        params.put("name", nme);
        params.put("email", eml);
        params.put("password", psswrd);
        invokeWS(params);
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://167.88.118.116/register",params ,new AsyncHttpResponseHandler() {
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
            if(obj.getBoolean("error") == false){
                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                navigateToLoginActivity(obj.getString("apiKey"));
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
    public void navigateToLoginActivity(String key){
        /*  Store the new found key in storage for future use.  */
        SharedPreferences settings = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("key", key);
        editor.commit();

        Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(homeIntent);
    }

}