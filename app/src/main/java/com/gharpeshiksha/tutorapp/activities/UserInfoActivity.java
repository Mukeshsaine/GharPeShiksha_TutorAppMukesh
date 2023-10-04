package com.gharpeshiksha.tutorapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {

    TextView letsBegin;
    //For forms details
    EditText newPassword;
    EditText newCnfPassword;
    EditText emailId;
    EditText referalCode;
    long phone;
    String userStatus = "no";
    private String Token = "";
    private String mSource = "";
    private VolleyHelper volleyHelper;
    ProgressDialog dialog;
    //SharedPreferences global variable
    SessionConfig sessionObj;
    //RequestQueue is an class of Volley class(http library) to make network requests on server url in background and manage cache management
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        letsBegin = findViewById(R.id.loginbutton);
        //get SharedPreferences file instance using SessionConfig class constructor
        sessionObj = new SessionConfig(UserInfoActivity.this);
        //To show details forms
        newPassword = findViewById(R.id.password_new_edit);
        newCnfPassword = findViewById(R.id.password_new_cnf_edit);
        referalCode = findViewById(R.id.referral_new_edit);
//        emailId = findViewById(R.id.email_new_edit);
        letsBegin = findViewById(R.id.loginbutton);
        Token = sessionObj.getToken();
        mSource = sessionObj.getSource();
        final Intent intent = getIntent();
        dialog = new ProgressDialog(getApplicationContext());
        dialog.setMessage("Please Wait...");
        //user status is new or old comes from Intent
        userStatus = intent.getStringExtra("userStatus");
        //phone number of user from intent
        phone = sessionObj.getPhone();
        //by this letsBegin(LoginButton) View onClick() method user get signup on the api or server
        letsBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ValueCheck", "onClick: " + "phone = " + Long.toString(phone)
//                        + "\nemail = " + emailId.getText().toString()
                        + "\npassword = " + newPassword.getText().toString()
                        + "\ncnfpassword = " + newCnfPassword.getText().toString()
                        + "\nrefereal = " + referalCode.getText().toString());
                if (userStatus.equals("new") || userStatus.equals("old")) {
                    if (newPassword.getText().toString().isEmpty() || newCnfPassword.getText().toString().isEmpty()) {
                        showAlertDialog("Please Complete All Fields");
                    } else {
                        if (!newPassword.getText().toString().equals(newCnfPassword.getText().toString())) {
                            showAlertDialog("Password and confirm password does not match");
                        } else {
                            saveUserDetails(Long.toString(phone), newPassword.getText().toString(), referalCode.getText().toString());
                        }
                    }
                } else {
                    showAlertDialog("letsBegin clicked");
                }
            }
        });
    }

    private void saveUserDetails(String phone, String password, String referral) {
        RequestQueue que = Volley.newRequestQueue(UserInfoActivity.this);
        String serverUrl = "https://api.gharpeshiksha.com" + "/Tutor/setPassAndReferral";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsResponse = new JSONObject(response);
                    Log.v("chhecking---",phone+ "  pass  "+password+ " " + referral + " ");

                    if (jsResponse.get("Status").equals("Success")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);
                        sharedPreferences.edit().clear();
                        sessionObj.LoginStatusWrite(true);
                        sessionObj.setPhone(Long.parseLong(phone.trim()));
                        Log.v("UserInfoActivity.java", "open dashboard after filling data");
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else if (jsResponse.get("Status").equals("Error") && jsResponse.get("Message").equals("Invalid Contact")) {
                        showAlertDialog("Invalid Number");
                    } else {
                        showAlertDialog("You already have an account. Please Sign In");
                        RegistrationStatus(Long.parseLong(phone.trim()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("UserInfoActivity.java", e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("UserInfoActivity.java", error.getMessage() + "");
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone.trim());
                params.put("password", password);
                params.put("referralId", referral);
                return params;
            }
        };
        que.add(stringRequest);

    }

    public void showAlertDialog(String title) {
        new AlertDialog.Builder(UserInfoActivity.this, R.style.AppCompatAlertDialogStyle)
                .setMessage(title)
                .setPositiveButton("Okay", null).show();
    }

    //Called from SaveVerified() method with user phone number that is comes from intent.getExtra() method means from extras of intent request
    //for OTPActivity.java
    public void RegistrationStatus(final long phone) {

        Log.e("TAG", "RegistrationStatus:");

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        String Server_url = "https://api.gharpeshiksha.com" + "/Tutor/checkactivity_new";

        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   progress.dismiss();

                        Log.e("TAG", "RegistrationStatus: onResponse" + response);
                        try {
                            JSONObject jsResponse = new JSONObject(response);

                            String Status = jsResponse.getString("Status");

                            requestQueue.stop();
                            if (Status.equals("Success")) {
                                String ActivityToFill = jsResponse.getString("ActivityToFill");

                                switch (ActivityToFill) {

                                    case "Classes":
                                        startActivity(new Intent(getApplicationContext(), ClassesActivity.class)
                                                .putExtra("contact", phone)
                                                .putExtra("verifyotp", true));

                                        finish();
                                        break;

                                    case "Qualification":
                                        startActivity(new Intent(getApplicationContext(), QualificationActivity.class)
                                                .putExtra("verifyotp", true));
                                        finish();
                                        break;
                                    case "PersonalInfo":
                                        startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class)
                                                .putExtra("verifyotp", true));
                                        finish();
                                        break;

                                    case "AreaRangeSelection":
                                        startActivity(new Intent(getApplicationContext(), AreaRangeSelection.class)
                                                .putExtra("status", true)
                                                .putExtra("camegt", true)
                                                .putExtra("verifyotp", true));
                                        finish();
                                        break;

                                    case "UserInfoActivity":
                                        startActivity(new Intent(getApplicationContext(), UserInfoActivity.class)
                                                .putExtra("status", true)
                                                .putExtra("camegt", true)
                                                .putExtra("verifyotp", true));
                                        finish();
                                        break;

                                        /*default:
                                            Toast.makeText(OTPActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(OTPActivity.this, SignInActivity.class));
                                            break;*/

                                }
                            } else {
                                sessionObj.setPhone(phone);
                                sessionObj.LoginStatusWrite(true);
                                checkstatus();//checking status of user, if user not exist toasting msg "there was an issue, Please try again"
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*new BasicFeature.Builder(OTPActivity.this)

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                        Log.e("TAG", "RegistrationStatus: Error.Response" + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + phone);
                return params;
            }
        };
        requestQueue.add(postRequest);

    }

    //call from RegistrationStatus method if user status is not success response from server post request
    public void checkstatus() {

        String alurl = "https://api.gharpeshiksha.com" + "/Tutor/basicinfo";
        RequestQueue requestQueue = Volley.newRequestQueue(UserInfoActivity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, alurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            Log.v("TAG", "onResponse: on slideactivity check status");
                            JSONObject jsonObject = new JSONObject(response);

                            String uploaddocument = jsonObject.getString("status");
                            Log.v("OTPActivity.java", "" + uploaddocument);
                            startActivity(new Intent(getApplicationContext(), Dashboard.class).putExtra("uploaddoc", uploaddocument).
                                    putExtra("sortDis", "firsttime").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();


                        } catch (Exception e) {
                            startActivity(new Intent(getApplicationContext(), SignUpAndIn.class));
                            Toast.makeText(UserInfoActivity.this, "There was an issue, Please try again.", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onResponse Error: " + e.getMessage());
                            Log.v("OTPActivity.java", "" + e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       /* new android.app.BasicFeature.Builder(OTPActivity.this)

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                        Log.d("TAG", "onErrorResponse: " + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + phone);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(postRequest);


    }
}