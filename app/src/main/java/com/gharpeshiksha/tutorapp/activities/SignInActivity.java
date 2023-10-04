package com.gharpeshiksha.tutorapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    int RC_SIGN_IN = 0;
    private final String TAG = SignInActivity.this.toString();
    LinearLayout SignUp;
    CardView Otp, Password, Googlesignupbuttona;
    EditText phone;
    TextView phone_validation;
    RequestQueue requestQueue;
    SessionConfig sessionObj;
    FrameLayout activitylayout;
    String Server_url;
    SharedPreferences sharedPreferences;
    Dialog dialog;
    Boolean doubleTapBack = false;
    String phnumber;
    private ProgressDialog progress;
    private String Token = "";

    String personEmail;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (phone.getText().toString().equals("")) {

                phone_validation.setVisibility(View.VISIBLE);
                phone_validation.setText("Please Fill First");
            } else {
                phone_validation.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private Button retry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        activitylayout = findViewById(R.id.signupactivitylinearlayout);
        sharedPreferences = this.getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);


        initilize();

        listeners();

        if (!isNetworkAvailable()) {

            noNetworkDialog();
        }

        activitylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) SignInActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });


    }


    public void RegistrationStatus(final long phone) {

        Log.e(TAG, "RegistrationStatus:");


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/checkactivity";

        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //          progress.dismiss();

                        Log.e(TAG, "RegistrationStatus: onResponse" + response);
                        try {
                            JSONObject jsResponse = new JSONObject(response);

                            String Status = jsResponse.getString("Status");

                            requestQueue.stop();
                            if (Status.equals("Success")) {
                                String ActivityToFill = jsResponse.getString("ActivityToFill");

                                switch (ActivityToFill) {

                                    case "Classes":
                                        startActivity(new Intent(getApplicationContext(), ClassesActivity.class)
                                                .putExtra("contact", phone));
                                        finish();
                                        break;

                                    case "Qualification":
                                        startActivity(new Intent(getApplicationContext(), QualificationActivity.class));
                                        finish();
                                        break;
                                    case "PersonalInfo":
                                        startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
                                        finish();
                                        break;

                                    case "AreaRangeSelection":
                                        startActivity(new Intent(getApplicationContext(), AreaRangeSelection.class)
                                                .putExtra("status", true)
                                                .putExtra("camegt", true));
                                        finish();
                                        break;
                                    case "Otp_verification":
                                        startActivity(new Intent(getApplicationContext(), OTPActivity.class)
                                                .putExtra("contact", phone));
                                        finish();
                                        break;

                                }
                            } else {
                                //               progress.dismiss();
                                sessionObj.setPhone(phone);
                                sessionObj.LoginStatusWrite(true);
                                startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*new android.app.BasicFeature.Builder(SignInActivity.this)

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                        progress.dismiss();
                        Log.e(TAG, "RegistrationStatus: Error.Response" + error.getMessage());
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


    private void initilize() {

        Googlesignupbuttona = findViewById(R.id.Googlesignupbuttona);
        sessionObj = new SessionConfig(getApplicationContext());
        Token = sessionObj.getToken();
        SignUp = findViewById(R.id.sign_up);
        phone = findViewById(R.id.phone);
        Password = findViewById(R.id.password);
        phone_validation = findViewById(R.id.phone_validation);
        phone.addTextChangedListener(textWatcher);
        Otp = findViewById(R.id.otp);
    }

    private void listeners() {


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                //finish();

            }
        });

        Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isNetworkAvailable()) {

                    noNetworkDialog();
                } else {

                    if (!phone.getText().toString().equals("")) {


                        if (phone.getText().toString().length() == 10) {

                            checkuserhaveclass();
                            /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            Intent intent=  new Intent(getApplicationContext(),OTPActivity.class);
                            intent.putExtra("contact", Long.parseLong( phone.getText().toString()) );
                            startActivity(intent);*/
                        } else {
                            phone_validation.setVisibility(View.VISIBLE);
                            phone_validation.setText("Enter Valid Number");
                        }
                    } else {

                        phone_validation.setVisibility(View.VISIBLE);
                        phone_validation.setText("Please Fill First");
                    }
                }
            }
        });


        Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!phone.getText().toString().equals("")) {


                    if (!phone.getText().toString().equals("")) {

                        if (phone.getText().toString().length() == 10) {

                            if (!isNetworkAvailable()) {

                                noNetworkDialog();
                            } else {


                                CreateRequest();


                            }


                            //
                            // RegistrationStatus(Long.parseLong(phone.getText().toString()));

                        } else {
                            phone_validation.setVisibility(View.VISIBLE);
                            phone_validation.setText("Enter Valid Number");
                        }
                    }
                } else {
                    phone_validation.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void noNetworkDialog() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.no_network_dialog);
        retry = dialog.findViewById(R.id.retry);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isNetworkAvailable()) {

                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    noNetworkDialog();
                }
            }
        });


    }

    public void checkuserhaveclass() {


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/SignInPassword";

        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    progress.dismiss();

                        //      Log.e(TAG,"CreateResponse: onResponse"+response);
                        try {
                            JSONObject jsResponse = new JSONObject(response);
                            //    Log.e(TAG,"sResponse.get()"+jsResponse.get("Status"));
                            if (jsResponse.get("Status").equals("Success")) {


                                Log.d(TAG, "onResponse: useralreadylogin " + "true");
                                Log.d(TAG, "onResponse: useralreadylogin " + phone.getText().toString());

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                                intent.putExtra("contact", Long.parseLong(phone.getText().toString()));
                                startActivity(intent);
                                //finish();

                            } else {

                                new AlertDialog.Builder(SignInActivity.this, R.style.AppCompatAlertDialogStyle)
                                        .setMessage("Please create a new account")
                                        .setPositiveButton("Create Account", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                                            }
                                        }).show();
                                Toast.makeText(SignInActivity.this, "Create new account", Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestQueue.stop();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*new android.app.BasicFeature.Builder(SignInActivity.this)

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                       /* progress.dismiss();
                        Log.e(TAG,"CreateResponse: Error.Response"+error.getMessage());*/
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<>();
                params.put("phone", phone.getText().toString());


                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    @Override
    public void onBackPressed() {


        if (doubleTapBack) {
            super.onBackPressed();
        } else {
            Toast.makeText(SignInActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            this.doubleTapBack = true;

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleTapBack = false;
                }
            }, 2000);
        }
    }

    public void start_request_loadingProcess() {

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                    progress.dismiss();
                    return true;
                }

                return false;
            }
        });// disable dismiss by tapping outside of the dialog
        progress.show();


    }

    private void CreateRequest() {
        start_request_loadingProcess();
        Log.e(TAG, "CreateResponse:");

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/SignInPassword";


        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();

                        Log.e(TAG, "CreateResponse: onResponse" + response);
                        try {
                            JSONObject jsResponse = new JSONObject(response);
                            Log.e(TAG, "sResponse.get()" + jsResponse.get("Status"));
                            if (jsResponse.get("Status").equals("Success")) {


                                sessionObj.setPhone(Long.parseLong(phone.getText().toString().trim()));

                                Log.d(TAG, "onResponse: imag url : " + jsResponse.getString("Pic_url"));

                                startActivity(new Intent(getApplicationContext(), PasswordActivity.class)
                                        .putExtra("tutor", jsResponse.get("name").toString())
                                        .putExtra("Image_url", jsResponse.getString("Pic_url")));
                                //finish();
                            } else {

                                new AlertDialog.Builder(SignInActivity.this, R.style.AppCompatAlertDialogStyle)
                                        .setMessage("Please create a new account")
                                        .setPositiveButton("Create Account", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                                            }
                                        }).show();
                                Toast.makeText(getApplicationContext(), "Create new Account", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestQueue.stop();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*new android.app.BasicFeature.Builder(SignInActivity.this)

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                        progress.dismiss();
                        Log.e(TAG, "CreateResponse: Error.Response" + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Log.e(TAG, "getParams: phone" + phone.getText().toString());
                Log.e(TAG, "getParams: firebaseToken" + Token);
                Log.e(TAG, "CreateResponse:  getParams()");
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone.getText().toString().trim());
                params.put("firebaseToken", Token);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }

}
