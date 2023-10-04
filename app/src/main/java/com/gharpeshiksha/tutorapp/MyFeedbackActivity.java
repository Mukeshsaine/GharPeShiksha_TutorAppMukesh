package com.gharpeshiksha.tutorapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MyFeedbackActivity extends AppCompatActivity {
    private static String TAG = "MyFeedbackActivity";


    Toolbar toolbar;
    TextView textClaName, textClaTutorReq, textClaContact, textClaBudget, textClaPref, textClaMes,
            textClaLoc, textClaDis, textClaId, textClaCon, textAppPosted, textAppViewed, my_feedback, our_feedback;

    String enquiryUrl, contactUrl;
    long phone;
    SessionConfig sessionConfig;
    View feedRel, gpsRel;
    Button giveFeedback;
    private Dialog dialog;
    private Button retry;
    private Intent intent;
    private int flag = 0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feedback);


        sessionConfig = new SessionConfig(getApplicationContext());
        phone = sessionConfig.getPhone();

        progressDialog = new ProgressDialog(MyFeedbackActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                    progressDialog.dismiss();
                    finish();
                    return true;
                }

                return false;
            }
        });
        progressDialog.show();
        toolbar = findViewById(R.id.toolbarCla);
        toolbar.setTitle("Enquiry Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        setSupportActionBar(toolbar);
        intent = getIntent();
        if (isNetworkAvailable()) {
            initialize();
        } else {
            noNetworkDialog();
        }
        listeners();
    }

    private void initialize() {
        textClaName = findViewById(R.id.textClaName);
        textClaTutorReq = findViewById(R.id.textClaTutorReq);
        textClaContact = findViewById(R.id.text2);
        textClaBudget = findViewById(R.id.textClaBudget);
        textClaPref = findViewById(R.id.textgenderPref);
        textClaMes = findViewById(R.id.textClaMes);
        textClaLoc = findViewById(R.id.textClaLoc);
        textClaDis = findViewById(R.id.textClaDis);

        textClaId = findViewById(R.id.textClaId);
        giveFeedback = findViewById(R.id.giveFeedback);
        my_feedback = findViewById(R.id.my_feedback);
        our_feedback = findViewById(R.id.our_feedback);
        gpsRel = findViewById(R.id.gpsRel);
        feedRel = findViewById(R.id.feedRel);

        textAppPosted = findViewById(R.id.textAppPosted);
        textAppViewed = findViewById(R.id.textAppViewed);
        textAppPosted.setText(intent.getStringExtra("posted_on"));
        textAppViewed.setText(intent.getStringExtra("viewed_on"));
        my_feedback.setText(intent.getStringExtra("my_feedback"));
        our_feedback.setText(intent.getStringExtra("our_feedback"));

        enquiryUrl = "https://api.gharpeshiksha.com" + "/GetClasses/enqinfo";
        String check = intent.getStringExtra("feedback_given");

        if (check.equals("false")) {
            feedRel.setVisibility(View.GONE);
            gpsRel.setVisibility(View.GONE);
            giveFeedback.setVisibility(View.VISIBLE);
        } else {

            giveFeedback.setVisibility(View.GONE);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        createResponse(enquiryUrl);
        intentViewContact();
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
                    if (flag == 0) {
                        initialize();
                    }
                } else {
                    dialog.dismiss();
                    noNetworkDialog();
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

    private void listeners() {


        giveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"given",Toast.LENGTH_SHORT).show();

                if (isNetworkAvailable()) {
                    feedBackDialog();
                } else {
                    noNetworkDialog();
                }

            }
        });
    }

    private void feedBackDialog() {
        try {
            AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView;
            final Spinner spinner, info_spin;
            final RelativeLayout relativeLayout, genRel;
            final EditText editText;

            dialogView = inflater.inflate(R.layout.layout_feedback, null);
            alert.setView(dialogView);

            alert.setCancelable(true);

            spinner = dialogView.findViewById(R.id.feedback_spin);
            info_spin = dialogView.findViewById(R.id.info_spin);
            relativeLayout = dialogView.findViewById(R.id.infoRel);
            genRel = dialogView.findViewById(R.id.genRel);
            editText = dialogView.findViewById(R.id.feedback_details);
            //  spinner.set
            spinnerAdapter(spinner, R.array.feedback_arrays);
            spinnerAdapter(info_spin, R.array.info_arrays);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    switch (String.valueOf(spinner.getSelectedItem())) {
                        case "Information mentioned in lead is incorrect":
                            relativeLayout.setVisibility(View.VISIBLE);
                            genRel.setVisibility(View.GONE);
                            break;
                        case "Parents are not genuine (They do not want any teacher)":
                            relativeLayout.setVisibility(View.GONE);
                            genRel.setVisibility(View.VISIBLE);
                            break;
                        default:
                            genRel.setVisibility(View.GONE);
                            relativeLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            alert.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String feedback = String.valueOf(spinner.getSelectedItem());
                    if (feedback.equals("Select an option")) {
                        Toast.makeText(getApplicationContext(), "Please select an option", Toast.LENGTH_LONG).show();
                    }
                    if (genRel.getVisibility() == View.VISIBLE) {
                        if (!(editText.getText().toString()).isEmpty() || !(editText.getText().toString()).equals("")) {
                            Toast.makeText(getApplicationContext(), editText.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                        if (editText.getText().toString().isEmpty() || editText.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Field can't be empty!", Toast.LENGTH_LONG).show();
                        }
                    }
                    Toast.makeText(getApplicationContext(), feedback, Toast.LENGTH_LONG).show();
                    if (getIntent().hasExtra("from")) {
                        if (getIntent().getStringExtra("from").equals("chatBot")) {
                            finish();
                        }
                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "feedBackDialog: " + e.getMessage());
        }
    }


    private void spinnerAdapter(Spinner spinner, int array) {

        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.spinner_layout, getResources().getStringArray(array));
            // adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
        } catch (Exception e) {
            Log.d(TAG, "spinnerAdapter: " + e.getMessage());
        }
    }

    private void createResponse(String url) {

        Log.e(TAG, "createResponse: url" + url);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Log.e(TAG, "onResponse: " + response);
                        flag = 1;
                        try {
                            // Log.d(TAG, "onResponse: " + response);

                            Log.e("appliedleadclass", response);

                            final JSONObject part = new JSONObject(response);

                            String name = part.getString("name");
                            String classes = part.getString("class");
                            String subjects = part.getString("subjects");
                            String budget = part.getString("budget");
                            String area = part.getString("area");
                            String message = part.getString("message");
                            String gender = part.getString("gender");

                            Log.e("appliedleadsgender", gender);

                            Log.e(TAG, "onResponse: message" + message);

                            textClaPref.setText(gender);
                            textClaMes.setText(message);
                            String tutorReqString = "Tutor Requirement for " + subjects + " for " + classes;

                            progressDialog.dismiss();

                            textClaName.setText(name);
                            textClaContact.setText(getIntent().getStringExtra("contact"));
                            textClaTutorReq.setText(tutorReqString);
                            textClaBudget.setText(budget);
                            String utf8String = new String(area.getBytes("ISO-8859-1"), "UTF-8");
                            textClaLoc.setText(utf8String);
                            textClaId.setText(getIntent().getStringExtra("enqId"));


                            Log.e(TAG, "onResponse: message" + message);

                        } catch (Exception e) {
                            Log.d(TAG, "onResponse Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*new android.app.BasicFeature.Builder(MyFeedbackActivity.this)

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                        Log.d(TAG, "onErrorResponse: " + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Log.d(TAG, "getParams: getParams()");
                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + phone);
                params.put("enq_id", getIntent().getStringExtra("enqId"));
                return params;
            }

           /* @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }*/
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(postRequest);
    }
/*
    private void checkStatus(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);

                        try {
                            Log.d(TAG, "onResponse status: " + response);

                            switch (response) {
                                case "active":
                                    upgradeLay.setVisibility(View.GONE);
                                    textClaCon.setVisibility(View.VISIBLE);
                                    break;
                                case "nonpaid":
                                    upgradeLay.setVisibility(View.VISIBLE);
                                    textClaCon.setVisibility(View.GONE);
                                    break;
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "onResponse Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Log.d(TAG, "getParams: getParams()");
                Map<String, String> params = new HashMap<>();
                params.put("phone", ""+phone);

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
    }*/

    private void getIntentData() {
        String minDis = "" + getIntent().getDoubleExtra("minDis", 0);
        String enqId = "" + getIntent().getLongExtra("enqId", 0);

        textClaDis.setText(minDis + " Km");
        textClaId.setText(enqId);
    }

   /* private void viewContact() {
        try {
            textClaCon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (textClaCon.getText().toString()) {
                        case "View Contact":
                            BasicFeature.Builder alertDialog = new BasicFeature.Builder(ClassesForMeActivity.this);
                            alertDialog.setTitle("View Contact");
                            alertDialog.setMessage("Are you sure you want to view the contact? This will decrease your contact views.");
                            alertDialog.setIcon(R.drawable.icon_phone);

                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    viewContactFun();
                                }
                            });

                            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alertDialog.show();
                            break;
                        case "Call Student":
                            Toast.makeText(getApplicationContext(), "Called!", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "viewContact: " + e.getMessage());
        }
    }*/

    private void viewContactFun() {
        createResponse(contactUrl);
        textClaCon.setText("Call Student");
        Toast.makeText(getApplicationContext(), "Viewed", Toast.LENGTH_LONG).show();
    }

    private void intentViewContact() {
        try {
            switch (getIntent().getStringExtra("viewContact")) {
                case "viewed":
                    viewContactFun();
                    break;

                case "noView":
                    getIntentData();
                    break;
            }
        } catch (Exception e) {
            Log.d(TAG, "intentViewContact: " + e.getMessage());
        }
    }
}
