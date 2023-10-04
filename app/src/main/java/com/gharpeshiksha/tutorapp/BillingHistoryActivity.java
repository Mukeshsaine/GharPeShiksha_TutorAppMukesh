package com.gharpeshiksha.tutorapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Adapter.BillingHistoryAdapter;
import com.gharpeshiksha.tutorapp.data_model.BillingHistory;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingHistoryActivity extends AppCompatActivity {
    final String TAG = BillingHistoryActivity.class.toString();
    RecyclerView recyclerView;
    Context context;
    Toolbar toolbar;
    BillingHistoryAdapter adapter;
    LinearLayoutManager manager;
    List<BillingHistory> arrayList = new ArrayList<>();
    SessionConfig sessionConfig;
    long phone;
    TextView billinghistory;
    RequestQueue requestQueue;
    StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_history);

        toolbar = findViewById(R.id.billToolbar);
        toolbar.setTitle("Billing History");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);

        billinghistory = (TextView) findViewById(R.id.nobillinghistory);
        billinghistory.setVisibility(View.GONE);
        sessionConfig = new SessionConfig(this);
        phone = sessionConfig.getPhone();
        Log.d("Billing.java", phone + " phone number");
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

        recyclerView = findViewById(R.id.billingHis);
        recyclerView.setHasFixedSize(true);

        createRequest();
    }

    private void createRequest() {

        Log.e(TAG, "createRequest: ");

        requestQueue = Volley.newRequestQueue(this);

        String url = "https://api.gharpeshiksha.com" + "/PaymentStatus/billinghistory";
        requestQueue.start();
        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: " + response);

                try {
                    JSONArray object = new JSONArray(response);

                    if (object.length() > 0) {
                        billinghistory.setVisibility(View.GONE);
                    } else {
                        billinghistory.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < object.length(); i++) {

                        JSONObject jsonObject = new JSONObject(object.get(i).toString());

                        String paymentAmount = jsonObject.getString("payment_amount");
                        String planName = jsonObject.getString("plan_name");
                        String payment_date = jsonObject.getString("payment_date");
                        String status = jsonObject.getString("status");


                        arrayList.add(new BillingHistory(planName, payment_date, "₹ " + paymentAmount, status));

                    }
                    setAdaptor(arrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                requestQueue.stop();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*new android.app.BasicFeature.Builder(BillingHistoryActivity.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/

                Log.e(TAG, "onErrorResponse: " + error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("phone", String.valueOf(phone));
                return param;
            }
        };
        requestQueue.add(request);


    }

    public void setAdaptor(List<BillingHistory> list) {

        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new BillingHistoryAdapter(this, list);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        //  arrayList = new ArrayList<>();


/*
        arrayList.add(new BillingHistory("Bronze", "16th March 2019", "6:45 PM", "Paytm",
                "₹5000", "20", "17th March 2019, 6:45 PM"));
        arrayList.add(new BillingHistory("Gold", "16th March 2019", "6:45 PM", "Paytm",
                "₹8000", "30", "17th March 2019, 6:45 PM"));
        arrayList.add(new BillingHistory("Platinum", "16th March 2019", "6:45 PM", "Paytm",
                "₹10000", "40", "17th March 2019, 6:45 PM"));*/


    }
}
