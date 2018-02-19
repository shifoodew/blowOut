package com.blowout.blowout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.blowout.blowout.app.AppConfig;
import com.blowout.blowout.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class ForgotPassword extends Activity {

    private TextView tvEmailAddress;
    private Button btnSubmit;
    private Button btnLoginScreen;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        tvEmailAddress= (TextView) findViewById(R.id.userEmail);
        btnSubmit = (Button) findViewById(R.id.btnSubmitReq);
        btnLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Button submit for password reset request
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = tvEmailAddress.getText().toString().trim();
                // Check for empty data in the form
                if (!email.isEmpty()) {
                    //Submit request
                    resetPassword(email);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter username!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        // Link to Login Screen
        btnLoginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    //Reset password request
    private void resetPassword(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_reset_pass";

        pDialog.setMessage("Requesting reset password ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FORGET_PASS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Log.d("ForgetPassActivity", "Checking JSON Object" +jsonObject);
                    // Check for error node in json
                    if(!jsonObject.isNull("request")){
                        JSONObject userJSONObject = (JSONObject) jsonObject.get("request");
                        Log.d("ForgetPassActivity", "request Object              : " + userJSONObject.toString());

                        String message = userJSONObject.getString("message");
                        Log.d("ForgetPassActivity", "-request attribute          : " + message);

                        //Alert Dialog
                        AlertDialog.Builder builder= new AlertDialog.Builder(ForgotPassword.this);
                        builder.setMessage(message)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Launch main activity
//                                        Intent intent = new Intent(ForgetPassActivity.this, LoginActivity.class);
//                                        startActivity(intent);
//                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Log.d("ForgetPassActivity", "" + e.toString());
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ForgetPassActivity Error: " + "Could not connect to the server " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Could not connect to the server ", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to forget password
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);

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
