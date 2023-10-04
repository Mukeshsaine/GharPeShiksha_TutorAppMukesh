package com.gharpeshiksha.tutorapp.activities;

import android.annotation.SuppressLint;
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
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.gharpeshiksha.tutorapp.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity {

    private final String TAG = PasswordActivity.this.toString();
    ImageView ProfileImage;
    String img_url, tutor, forgetPassURL;
    TextView teacher_name;
    Button continue1;
    String uploaddocument;
    long phone;
    EditText password;
    TextView password_validation;
    TextWatcher textWatcher;
    Cache cache;
    Network network;
    RequestQueue requestQueue;
    SessionConfig sessionConfig;
    RelativeLayout relativeLayout;
    SharedPreferences sharedPreferences;
    String Server_url;
    private ProgressDialog progress;
    private Dialog dialog;
    private Button retry;
    TextView forgotpassword;
    ProgressDialog progressDialog12;
    String userStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        sessionConfig = new SessionConfig(getApplicationContext());

        relativeLayout = findViewById(R.id.passwordlayout);
        ProfileImage = findViewById(R.id.profile_image);
        teacher_name = findViewById(R.id.teacher_name);
        continue1 = findViewById(R.id.continue1);
        forgotpassword = findViewById(R.id.forgotpassword);
        password = findViewById(R.id.password);
        password_validation = findViewById(R.id.password_validation);
        sharedPreferences = this.getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);
        Intent i = getIntent();
        userStatus = i.getStringExtra("userStatus");
//        Toast.makeText(PasswordActivity.this, userStatus + "", Toast.LENGTH_SHORT).show();

        forgetPassURL = "https://api.gharpeshiksha.com" + "/Tutor/forgetpassword";
        try {
            progressDialog12 = new ProgressDialog(PasswordActivity.this);
            progressDialog12.setMessage("Sending Request");
            progressDialog12.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PasswordActivity.this, R.style.AppCompatAlertDialogStyle);
                alertDialog.setTitle("FORGET PASSWORD");
                alertDialog.setMessage("Enter Phone Number");

                final EditText input = new EditText(PasswordActivity.this);
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                input.setMaxLines(1);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10) {
                }});
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input, 50, 0, 100, 0);

                alertDialog.setPositiveButton("SEND LINK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    progressDialog12.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                forgetPassVolley(input.getText().toString());


                            }
                        });
                alertDialog.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }

        });


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) PasswordActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        Intent intent = getIntent();

        phone = sessionConfig.getPhone();
        /*phone = intent.getLongExtra("contact",123);*/
        img_url = (String) intent.getCharSequenceExtra("Image_url");

        tutor = (String) intent.getCharSequenceExtra("tutor");
        Log.d(TAG, "onCreate: imahbjhka" + img_url);

        Glide.with(getApplicationContext()).setDefaultRequestOptions(RequestOptions.centerCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .load(img_url).into(ProfileImage);


        teacher_name.setText(tutor);
        listeners();
        password.addTextChangedListener(textWatcher);

    }


    private void forgetPassVolley(final String number) {

        RequestQueue requestQueue = Volley.newRequestQueue(PasswordActivity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, forgetPassURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("success")) {
                        try {
                            progressDialog12.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PasswordActivity.this);
                        alertDialog.setMessage(jsonObject.getString("message"));

                        alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                /*startActivity(new Intent(PasswordActivity.this,OnScreenSliderActivity.class));*/
                                finish();
                            }
                        });
                        alertDialog.show();

                    } else if (jsonObject.getString("status").equals("error")) {
                        try {
                            progressDialog12.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PasswordActivity.this);
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage(jsonObject.getString("message"));

                        alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                /*startActivity(new Intent(PasswordActivity.this,OnScreenSliderActivity.class));*/
                                finish();
                            }
                        });
                        alertDialog.show();

                    }

                } catch (JSONException e) {
                    try {
                        progressDialog12.dismiss();
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    progressDialog12.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*new android.app.BasicFeature.Builder(PasswordActivity.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/
            }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + number);

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


    public void listeners() {


        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (password.getText().toString().equals("")) {

                    password_validation.setVisibility(View.VISIBLE);
                } else {

                    password_validation.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!password.getText().toString().equals("")) {

                    if (isNetworkAvailable()) {

                        Log.d(TAG, "onClick: Continue");
                        CreateResponse();

                    } else {

                        noNetworkDialog();
                    }


                } else {
                    password_validation.setVisibility(View.VISIBLE);
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

    public void RegistrationStatus(final long phone) {

        Log.e(TAG, "RegistrationStatus:");

        SharedPreferences sharedPreferences = getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);
        sharedPreferences.edit().clear();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/checkactivity_new";

        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   progress.dismiss();

                        Log.e(TAG, "RegistrationStatus: onResponse" + response);
                        try {
                            JSONObject jsResponse = new JSONObject(response);

                            String Status = jsResponse.getString("Status");

                            requestQueue.stop();
                            if (Status.equals("Success")) {
                                String ActivityToFill = jsResponse.getString("ActivityToFill");

                                switch (ActivityToFill) {

                                    case "Classes":
                                        Log.d(TAG, "onResponse: registration start Classes");
                                        startActivity(new Intent(getApplicationContext(), ClassesActivity.class));
                                        finish();
                                        break;

                                    case "Qualification":
                                        Log.d(TAG, "onResponse: registration start Qualification");
                                        startActivity(new Intent(getApplicationContext(), QualificationActivity.class));
                                        finish();
                                        break;
                                    case "PersonalInfo":
                                        Log.d(TAG, "onResponse: registration start personal info");
                                        startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
                                        finish();
                                        break;

                                    case "AreaRangeSelection":
                                        Log.d(TAG, "onResponse: registration start Area Range Activity");
                                        startActivity(new Intent(getApplicationContext(), AreaRangeSelection.class)
                                                .putExtra("status", true)
                                                .putExtra("camefrompersonalactivity", true));
                                        finish();
                                        break;

                                    case "UserInfoActivity":
                                        Log.d(TAG, "onResponse: registration start OTP verification");
                                        startActivity(new Intent(getApplicationContext(), UserInfoActivity.class)
                                                .putExtra("contact", phone)
                                                .putExtra("camefromarearange", true)
                                                .putExtra("userStatus", userStatus));
                                        finish();
                                        break;


                                }
                            } else {
                                sessionConfig.LoginStatusWrite(true);
                                SaveVerified(String.valueOf(phone));
                                checkstatus();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        /*new android.app.BasicFeature.Builder(PasswordActivity.this)

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

    public void start_request_loadingProcess() {

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
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

    private void CreateResponse() {
        start_request_loadingProcess();
        Log.e(TAG, "CreateResponse:start_request_loadingProcess");

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/SignInVerifyNew";
        String accessToken = Utility.getRandomStr();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("PasswordActivity.java", password.getText().toString());
                        progress.dismiss();

                        Log.e(TAG, "CreateResponse: onResponse" + response);
                        try {
                            JSONObject jsResponse = new JSONObject(response);

                            if (jsResponse.get("Status").toString().equals("Success") && jsResponse.get("Message").toString().equals("Password Match")) {

                                sessionConfig.setAccessToken(accessToken);
                                requestQueue.stop();
                                RegistrationStatus(phone);
                                     /* sessionConfig.LoginStatusWrite(true);
                                      startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));*/

                                //  Toast.makeText(getApplicationContext(), "login successfuly ",Toast.LENGTH_SHORT ).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Entered Invalid Password ", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                            /*new android.app.BasicFeature.Builder(PasswordActivity.this)

                                    .setMessage("There might be an internet issue, please try again after some time.")
                                    .setPositiveButton("Okay",null)
                                    .show();*/
                        progress.dismiss();
                        //       Toast.makeText(PasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "CreateResponse: Error.Response" + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + phone);
                params.put("password", password.getText().toString());
                params.put("accessToken", accessToken);
                Log.e(TAG, "CreateResponse:  getParams()" + params);
                return params;
            }
        };
        requestQueue.add(postRequest);
    }


    public void checkstatus() {

        String alurl = "https://api.gharpeshiksha.com" + "/Tutor/basicinfo";
        RequestQueue requestQueue = Volley.newRequestQueue(PasswordActivity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, alurl,
                response -> {

                    try {


                        Log.d(TAG, "onResponse: on slideactivity check status");
                        JSONObject jsonObject = new JSONObject(response);
                        uploaddocument = jsonObject.getString("status");
                        sessionConfig.setPhone(phone);
                        startActivity(new Intent(getApplicationContext(), Dashboard.class).putExtra("uploaddoc", uploaddocument).
                                putExtra("sortDis", "firsttime").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();

                        Log.d(TAG, "onResponse: on slideactivity check status " + uploaddocument);

                    } catch (Exception e) {
                        Log.d(TAG, "onResponse Error: " + e.getMessage());
                    }

                },
                error -> {

                    Log.d(TAG, "onErrorResponse: " + error);
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


    /// Check for verify account


    String verifyurl = /*"https://api.gharpeshiksha.com"+*/ "https://api.gharpeshiksha.com/TutorLogin/verified";


    public void SaveVerified(final String phone) {


        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.gharpeshiksha.tutorapp", Context.MODE_PRIVATE);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, verifyurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                sharedPreferences.edit().putString("VerifiedContact", response).apply();


                Log.d("tag", "onResponse: persongoogle " + response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*new android.app.BasicFeature.Builder(PasswordActivity.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Log.d("tag", "getParams: getParams()");
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
