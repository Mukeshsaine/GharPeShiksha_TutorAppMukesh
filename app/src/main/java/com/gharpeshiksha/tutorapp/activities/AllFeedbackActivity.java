package com.gharpeshiksha.tutorapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.Adapter.AllFeedbacRecyclerviewkAdptor;
import com.gharpeshiksha.tutorapp.data_model.AppliedLeads;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllFeedbackActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    Toolbar toolbar;

    private RequestQueue requestQueue;
    private String TAG = AllFeedbackActivity.this.toString();
    List<AppliedLeads> feedbackList = new ArrayList<>();
    private long phone;
    SessionConfig sessionConfig;
    private TextView noFeedbackTextView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_feedback);

        progressDialog = new ProgressDialog(AllFeedbackActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        recyclerview = findViewById(R.id.recyclerview);
        sessionConfig = new SessionConfig(this);
        phone = sessionConfig.getPhone();

        toolbar = findViewById(R.id.toolbar);


        noFeedbackTextView = findViewById(R.id.no_feedback_textView);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_nevigation_icon));
        toolbar.setTitle("Classes Feedback");
        setSupportActionBar(toolbar);
        recyclerview.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(manager);


        createRequest();


        listeners();

    }

    private void createRequest() {


        String URL = "https://api.gharpeshiksha.com" + "/GetClasses/getfeedbacks";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        StringRequest postRequest = new StringRequest(StringRequest.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = new JSONObject(jsonArray.getString(i));

                                String area = object.getString("area");

                                String utf8String = new String(area.getBytes("ISO-8859-1"), "UTF-8");

                                String distance = object.getString("distance");
                                String subjects = object.getString("subjects");
                                String our_action = object.getString("our_action");
                                String enq_id = object.getString("enq_id");
                                String posted_on = object.getString("posted_on");
                                String viewed_on = object.getString("viewed_on");
                                String feedback = object.getString("feedback");

                                String name = object.getString("name");

                                String class1 = object.getString("class");
                                String budget = object.getString("budget");

                                String required = "Tutor Requirement for " + subjects + " for " + class1;


                                feedbackList.add(new AppliedLeads(name, posted_on, budget, viewed_on, utf8String, distance + " Km", required, "", feedback, our_action, enq_id, "true", "",""));
                            }

                            AllFeedbacRecyclerviewkAdptor adaptor = new AllFeedbacRecyclerviewkAdptor(getApplicationContext(), feedbackList);
                            if (adaptor.getItemCount() == 0) {
                                progressDialog.dismiss();
                                noFeedbackTextView.setVisibility(View.VISIBLE);
                            }
                            recyclerview.setAdapter(adaptor);
                            progressDialog.dismiss();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        // noFeedbackTextView.setVisibility(View.INVISIBLE);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                /*new BasicFeature.Builder(AllFeedbackActivity.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/
                Log.e(TAG, "checksumGeneration: error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + phone);
                return params;
            }
        };
        requestQueue.add(postRequest);

    }


    private void listeners() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
