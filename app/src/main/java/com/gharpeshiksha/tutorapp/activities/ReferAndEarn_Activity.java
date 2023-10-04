package com.gharpeshiksha.tutorapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReferAndEarn_Activity extends AppCompatActivity {


    private Toolbar toolbar;
    private static final String TAG = "ReferAndEarn_Activity";
    long phone;
    private TextView refercode, terms, referText;
    private Button referNow;
    private ProgressDialog progressDialog;
    private SessionConfig sessionConfig;
    private String referid, referdetails, applink;
    private ImageView copycode;
    private String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn_);

        refercode = findViewById(R.id.refercode);
        copycode = findViewById(R.id.copycode);
        progressDialog = new ProgressDialog(ReferAndEarn_Activity.this);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        referText = findViewById(R.id.textview100);

        referNow = findViewById(R.id.referNow_Button);
        terms = findViewById(R.id.termsAndCondition);
        sessionConfig = new SessionConfig(ReferAndEarn_Activity.this);

        phone = sessionConfig.getPhone();
        getcode();


        toolbar = findViewById(R.id.rtoolbar);
        toolbar.setTitle("Refer & Earn");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        terms.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsCondition();
            }
        });

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        referNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        copycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(getApplicationContext(), code);
                Toast.makeText(getApplicationContext(), "Code Copied...", Toast.LENGTH_LONG).show();
            }
        });
        refercode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(getApplicationContext(), code);
                Toast.makeText(getApplicationContext(), "Code Copied...", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public void shareDirect(String packagename) {


        String shareBody = referid;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.setPackage(packagename);

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via..."));


    }

    public void share() {
        String shareBody = referid;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via..."));


    }


    public void getcode() {

        String url = "https://api.gharpeshiksha.com" + "/Tutor/getReferAndEarnDetail";
        RequestQueue requestQueue = Volley.newRequestQueue((getApplicationContext()));

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e(TAG, "onResponse: " + response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);


                            code = jsonObject.getString("referid");
                            referid = jsonObject.getString("invitemessage");
                            refercode.setText(code);
                            referText.setText(jsonObject.getString("refer_text"));
                            Log.d(TAG, "onResponse: referid " + referid);
                            progressDialog.dismiss();

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(ReferAndEarn_Activity.this)
                                    .setTitle("Error")
                                    .setMessage("Network connectivity not available.")
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .show();
                            Log.d(TAG, "onResponse Error: " + e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*new BasicFeature.Builder(ReferAndEarn_Activity.this)

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                        progressDialog.dismiss();
                        Log.d(TAG, "onErrorResponse: " + error);
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

    public void TermsCondition() {

        Toast.makeText(ReferAndEarn_Activity.this, "Please read the conditions carefully", Toast.LENGTH_LONG).show();

        String url = "https://api.gharpeshiksha.com" + "/Tutor/redeem_term_condition";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: term and condition : " + response);

                new AlertDialog.Builder(ReferAndEarn_Activity.this)
                        .setTitle("Terms and Conditions")
                        .setMessage(response)
                        .setPositiveButton("Okay", null)
                        .show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                noNetworkDialog();
                /*new BasicFeature.Builder(ReferAndEarn_Activity.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/
            }
        }) {

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

    protected void noNetworkDialog() {

        final Dialog dialog = new Dialog(ReferAndEarn_Activity.this);

        dialog.setContentView(R.layout.no_network_dialog);
        Button retry = dialog.findViewById(R.id.retry);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}