package com.blowout.blowout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.blowout.blowout.app.AppConfig;
import com.blowout.blowout.app.AppController;
import com.blowout.blowout.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shifoodew on 12/30/2017.
 */

public class LoginActivity  extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private Button btnLogin;
    private Button btnLinkToRegister;
    private Button btnLinkToForgotPass;

    private EditText inputUsername;
    private EditText inputPassword;

    private ProgressDialog pDialog;
    private SessionManager session;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername       = findViewById(R.id.username);
        inputPassword       = findViewById(R.id.password);
        btnLogin            = findViewById(R.id.btnLogin);
        btnLinkToRegister   = findViewById(R.id.btnLinkToRegisterScreen);
        btnLinkToForgotPass = findViewById(R.id.btnLinkTForgetPass);

        // Progress dialog
        pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Link to Forget password
        btnLinkToForgotPass.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ForgotPassword.class);
                startActivity(i);
                finish();
            }
        });

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }
    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("LoginActivity", "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Log.d("LoginActivity", "Checking JSON Object" +jsonObject);

                    if(!jsonObject.isNull("login")){

                        JSONObject loginObject = (JSONObject) jsonObject.get("login");
                        // access individual json object thru jsonObject.get("FIELD_NAME")
                        Log.d(TAG, "-error attribute            : " + loginObject.get("error").toString());
                        Log.d(TAG, "-user attribute             : " + loginObject.get("user").toString());

                        // user successfully logged in
                        // Create login session

                        session.setLogin(true);

                        //usr object
                        if (!loginObject.isNull("user")) {
                            // handle user login data
                            JSONObject userJSONObject = (JSONObject) loginObject.get("user");
                                Log.d(TAG, "User Object                   : " + userJSONObject.toString());

                            String user_id = userJSONObject.getString("id");
                                Log.d(TAG, "USER -id attribute          : " + userJSONObject.get("id").toString());

                            String name = userJSONObject.getString("name");
                                Log.d(TAG, "USER -name attribute          : " + userJSONObject.get("name").toString());

                            String email = userJSONObject.getString("email");
                                Log.d(TAG, "USER -email attribute      : " + userJSONObject.get("email").toString());

                            // Launch main activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("name", name); //passing data to another activity
                            intent.putExtra("username", email); //passing data to another activity
                            startActivity(intent);
                            finish();

                        }
                        else {
                            // a new JSON string that doesn't have user in login Object
                            Log.d(TAG, "Unknown JSON String              : " + loginObject.toString());
                        }

                    }else {

                        // Error in login. Get the error message
                        String errorMsg = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Log.d("LoginActivity", "" + e.toString());
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + "Could not connect to the server " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Could not connect to the server ", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
