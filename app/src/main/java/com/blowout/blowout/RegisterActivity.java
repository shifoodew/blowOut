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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button btnLinkToLogin;
    private Button btnRegister;

    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;

    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputName = findViewById(R.id.name);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputConfirmPassword =  findViewById(R.id.confirm_password);

        btnRegister = findViewById(R.id.btnRegister);
        btnLinkToLogin =  findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString();
                String confirm_password = inputConfirmPassword.getText().toString();

                if (!name.isEmpty() &&!email.isEmpty() && !password.isEmpty() && !confirm_password.isEmpty()) {
                    Log.d(TAG, "Username                    : " + email);
                    //Check if confirm password does match
                    if(password.equals(confirm_password)){
                        Log.d(TAG, "password and confirm passwod is the same");
                        registerUser(name, email, password, confirm_password);

                    }else{

                        Log.d(TAG, "Password and Confirm password does not match");
                        Toast.makeText(getApplicationContext(),
                                "Password and Confirm password does not match", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    /*
     * Method registeUSer
     */
    private void registerUser(final String name, final String email, final String password, final String confirm_password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RegisterActivity", "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    Log.d("RegisterActivity", "Checking JSON Object" +jObj);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        Log.d(TAG, "Member's info was successfully stored in members table");
                        // Now store the user in sqlite
                        JSONObject user = jObj.getJSONObject("user");
                            Log.d("USER", "User Object                  : " + user.toString());
                        String message = user.getString("message");
                            Log.d(TAG, "Message                         : " + message);

                        //Message for new registered member
                        Toast.makeText(getApplicationContext(), " "+message , Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("password_confirm", confirm_password);

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
