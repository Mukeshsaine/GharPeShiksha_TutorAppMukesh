package com.gharpeshiksha.tutorapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.Adapter.TransactionAdapter;
import com.gharpeshiksha.tutorapp.data_model.TransactionModel;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReedemPoints extends AppCompatActivity {

    private static final String TAG = "ReedemPoints";

    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView rewardwallet, redeem, totalPoints;
    TextView rewardtextview, redeemtextview, howToearn;
    TransactionAdapter transactionAdapter;
    ArrayList<TransactionModel> arrayList;
    TransactionModel model;
    private Dialog dialog;
    private int flag = 0;
    Boolean sart = false;
    private Button retry;
    RelativeLayout rewardhistoryrelativelayout, redeemRelativeLayout;
    EditText pointsedittext, paytmnumberedit;
    RadioButton contactviewradiobutton, paytmradiobutton;
    String redeemtype, pointsreedem;
    TextInputLayout pytmnumber;
    RadioGroup radioGroup;
    TextView termandcondition, nohistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reedem_points);


        arrayList = new ArrayList<>();
        paytmnumberedit = findViewById(R.id.paytmnumbereditext);
        redeemtextview = findViewById(R.id.textRedeem);
        rewardtextview = findViewById(R.id.textRewardWallet);
        rewardwallet = findViewById(R.id.buttonrewardwallet);
        redeem = findViewById(R.id.redeempoints);
        totalPoints = findViewById(R.id.totalPoints);
        recyclerView = findViewById(R.id.redeempointrecyclerview);
        redeemRelativeLayout = findViewById(R.id.redeemRelativeLayout);
        rewardhistoryrelativelayout = findViewById(R.id.rewardhistoryrelativelayout);
        pointsedittext = findViewById(R.id.pointsedittext);
        paytmradiobutton = findViewById(R.id.paytmradiobutton);
        contactviewradiobutton = findViewById(R.id.contactviewradiobutton);
        pytmnumber = findViewById(R.id.paytmnumber);
        radioGroup = findViewById(R.id.radiogroup);
        howToearn = findViewById(R.id.howtoEarn);
        termandcondition = findViewById(R.id.termcondition);
        nohistory = findViewById(R.id.noHistory);
        termandcondition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        termandcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termCondition();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(ReedemPoints.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        paytmradiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pytmnumber.setVisibility(View.VISIBLE);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (paytmradiobutton.isChecked()) {
                    pytmnumber.setVisibility(View.VISIBLE);
                } else if (contactviewradiobutton.isChecked()) {
                    pytmnumber.setVisibility(View.GONE);
                }
            }
        });


        redeemRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });


        rewardwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardhistoryrelativelayout.setVisibility(View.VISIBLE);
                redeemRelativeLayout.setVisibility(View.GONE);

                rewardwallet.setTextColor(Color.parseColor("#000000"));
                redeem.setTextColor(Color.parseColor("#BEBEBE"));
                redeem.setTypeface(null, Typeface.NORMAL);
                rewardtextview.setVisibility(View.VISIBLE);
                redeemtextview.setVisibility(View.INVISIBLE);

                nohistory.setVisibility(View.VISIBLE);

            }
        });


        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rewardhistoryrelativelayout.setVisibility(View.GONE);
                redeemRelativeLayout.setVisibility(View.VISIBLE);
                nohistory.setVisibility(View.GONE);
                redeem.setTextColor(Color.parseColor("#000000"));
                rewardwallet.setTextColor(Color.parseColor("#BEBEBE"));
                rewardwallet.setTypeface(null, Typeface.NORMAL);
                rewardtextview.setVisibility(View.INVISIBLE);
                redeemtextview.setVisibility(View.VISIBLE);

            }
        });

        howToearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howtoEarnVolley();
            }
        });


        toolbar = findViewById(R.id.redeemtoolbar);

        toolbar.setTitle("Points");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

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

        getTotalPoints();
        getReferalHistory();

    }


    private void howtoEarnVolley() {


        String url = "https://api.gharpeshiksha.com" + "/Tutor/how_to_earn_points";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                new AlertDialog.Builder(ReedemPoints.this)
                        .setTitle("How do i earn?")
                        .setMessage(response)
                        .setPositiveButton("Okay", null)
                        .show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noNetworkDialog();
                /*new BasicFeature.

               Builder(ReedemPoints.this)
                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + new SessionConfig(ReedemPoints.this).getPhone());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    private void getTotalPoints() {

        String url = "https://api.gharpeshiksha.com" + "/Tutor/getPoints";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String totalpoint = jsonObject.getString("point");


                    totalPoints.setText(totalpoint);

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noNetworkDialog();
                /*new BasicFeature.Builder(ReedemPoints.this)
                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + new SessionConfig(ReedemPoints.this).getPhone());
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

    public void getReferalHistory() {


        String url = "https://api.gharpeshiksha.com" + "/Tutor/getPointHistory";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                arrayList.clear();
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() > 0) {
                        nohistory.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String date = jsonObject.getString("date");
                        String point = jsonObject.getString("point_datewise");
                        String referedto = jsonObject.getString("tutor");
                        String referedby = jsonObject.getString("referedby");


                        model = new TransactionModel(date, point, referedto, referedby);


                        arrayList.add(model);


                        transactionAdapter = new TransactionAdapter(arrayList, ReedemPoints.this);
                        recyclerView.setAdapter(transactionAdapter);
                        transactionAdapter.notifyDataSetChanged();


                    }

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noNetworkDialog();
                /*new BasicFeature.Builder(ReedemPoints.this)
                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + new SessionConfig(ReedemPoints.this).getPhone());
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


    public void redeem(View view) {

        redeemtype = "";
        pointsreedem = pointsedittext.getText().toString();
        if (paytmradiobutton.isChecked()) {
            redeemtype = "cash";

            //  Toast.makeText(this, paytmradiobutton.getText(), Toast.LENGTH_SHORT).show();
        } else if (contactviewradiobutton.isChecked()) {

            redeemtype = "view";
            //  Toast.makeText(this, contactviewradiobutton.getText(), Toast.LENGTH_SHORT).show();
        }

        if (redeemtype.length() == 0) {
            Toast.makeText(this, "Please select reedem type", Toast.LENGTH_SHORT).show();
        } else if (pointsreedem.length() == 0) {
            Toast.makeText(this, "Please enter Points", Toast.LENGTH_SHORT).show();
        } else if (pytmnumber.getVisibility() == View.VISIBLE) {

            if (paytmnumberedit.getText().toString().length() == 0) {
                Toast.makeText(this, "Please enter number", Toast.LENGTH_SHORT).show();
            } else if (paytmnumberedit.getText().toString().length() < 10) {
                Toast.makeText(this, "Please enter valid number", Toast.LENGTH_SHORT).show();

            } else if (pointsreedem.length() > 0 && redeemtype.length() > 0 && paytmnumberedit.getText().toString().length() == 10) {

                // all conditions checked for cash

                // Toast.makeText(this, "DATA : "+pointsreedem+"\n"+redeemtype+"\n"+paytmnumberedit.getText(), Toast.LENGTH_SHORT).show();

                redeempoints(pointsreedem, redeemtype, paytmnumberedit.getText().toString(), new SessionConfig(ReedemPoints.this).getPhone() + "");

            }

        } else if (pointsreedem.length() > 0 && redeemtype.length() > 0) {

            // conditions checked for views

            // Toast.makeText(this, "DATA : "+pointsreedem+"\n"+redeemtype+"\n"+new SessionConfig(ReedemPoints.this).getPhone()+"", Toast.LENGTH_SHORT).show();

            redeempoints(pointsreedem, redeemtype, "0", new SessionConfig(ReedemPoints.this).getPhone() + "");
        }


    }

    public void redeempoints(final String points, final String type, final String Paytmnumber, final String number) {


        String url = "https://api.gharpeshiksha.com" + "/Tutor/redeemPoints";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.e(TAG, "onResponse: " + response + "\n" + points + "\n" + type + "\n" + Paytmnumber + "\n" + number);

                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject.getString("result");

                    new AlertDialog.Builder(ReedemPoints.this)
                            .setMessage(jsonObject.getString("result"))
                            .setPositiveButton("Okay", null)
                            .show();

                } catch (Exception e) {
                    Log.e("redeemPointVolley", e.getMessage());
                    new AlertDialog.Builder(ReedemPoints.this)
                            .setMessage("There was a problem submitting your request, Please try again after some time.")
                            .setPositiveButton("Okay", null)
                            .show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noNetworkDialog();
                Log.e("redeemPointVolley", error.getMessage());
                /*new android.app.BasicFeature.Builder(ReedemPoints.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("contact", number);
                params.put("points", points);
                params.put("redeem_mode", type);
                params.put("Paytm_Number", Paytmnumber);
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


    public void termCondition() {

        Toast.makeText(ReedemPoints.this, "Please read the conditions carefully", Toast.LENGTH_LONG).show();

        String url = "https://api.gharpeshiksha.com" + "/Tutor/redeem_term_condition";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: term and condition : " + response);

                new AlertDialog.Builder(ReedemPoints.this)
                        .setTitle("Terms and Conditions")
                        .setMessage(response)
                        .setPositiveButton("Okay", null)
                        .show();

           /*     BasicFeature.Builder alertDialog = new BasicFeature.Builder(ReedemPoints.this);

                alertDialog.setMessage(response);

                alertDialog.show();*/


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noNetworkDialog();
                /*new android.app.BasicFeature.Builder(ReedemPoints.this)

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

        final Dialog dialog = new Dialog(ReedemPoints.this);

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
