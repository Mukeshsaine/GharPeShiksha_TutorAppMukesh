package com.gharpeshiksha.tutorapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.BasicFeatures;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.UpgradeActivity;
import com.gharpeshiksha.tutorapp.Adapter.ParentsContactedAdapter;
import com.gharpeshiksha.tutorapp.data_model.ParentsContactedModel;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParentsContacted extends AppCompatActivity {

    //We gonna display an RecyclerView list for all the parents contacted returned details
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    ArrayList<ParentsContactedModel> list;
    String phone;
    TextView noContacted;
    Toolbar toolbar;
    SessionConfig sessionConfig;
    BasicFeatures basicFeatures;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_contacted);
        Intent i = getIntent();
        phone = i.getStringExtra("phone");
        sessionConfig = new SessionConfig(ParentsContacted.this);
        basicFeatures = new BasicFeatures();
//        getParentsContactedDetails();
        View view = findViewById(R.id.parentView);
        checkStatusAndShowParents(view);
        list = new ArrayList<>();
        Log.v("size---", list.size() + "");
        noContacted = findViewById(R.id.noParCon);
        recyclerView = (RecyclerView) findViewById(R.id.parentsContactedList);
        //LinearLayoutManager is layoutManager which is handle to get, bind item of list from adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ParentsContacted.this);
        //set LinearLayout manager
        recyclerView.setLayoutManager(linearLayoutManager);

        //Use Toolbar as Activity Actionbar make sure you create Toolbar(androidx support library) variable not widget else it show error version
        //compatibility.
        toolbar = findViewById(R.id.toolbarClaParentsContacted);
        toolbar.setTitle("Parents Contacted");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        //check ActionBar is null or not if yes then do none else set actionbar(toolbar) as up enabled and setHomeAsUpIndicators to display left Arror
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
        //It is an NavigationOnClickListener interface Anonymous class in which Override and Implement NavigationOnClick() method and call onBackPressed()
        //method which is get called by android by default every time user click on system navigation buttons.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//Do default work it done when user click on system back navigation button means destroy foreground Fragment or
                //Activity
            }
        });
    }

    private void checkStatusAndShowParents(View view) {
        //Call checkPaymentStatus method on BasicFeatures instance and pass context, phone, and CheckStatus interface instance using Anonymous
        //class and Override result() and onError() method.
        basicFeatures.CheckPaymentStatus(ParentsContacted.this, sessionConfig.getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {
                if(status.matches("active")) {
                    getParentsContactedDetails();
                } else if(status.matches("expired")) {
                    Intent i = new Intent(ParentsContacted.this, UpgradeActivity.class);
                    startActivity(i);
                } else {
                    basicFeatures.showFreePaidDialog(ParentsContacted.this);
                }
            }

            @Override
            public void onError() {

            }
        });
    }
    private void showSnackBarForUpgrade(View view, CharSequence msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Upgrade", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParentsContacted.this, UpgradeActivity.class);
                startActivity(i);
            }
        });
        //If you want to allow your Snackbar disappear when user swipe-off screen set CoordinatorLayout Parent View in View hierarchy of layout
        //file or set CoordinatorLayout as layout params by get all the layout parameters of Snackbar View.
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbar.getView().getLayoutParams();
//        params.setMargins(0, 0, 0, 160);
//        //set LayoutParams on snackbar
//        snackbar.getView().setLayoutParams(params);
        snackbar.setActionTextColor(getResources().getColor(R.color.snackbarAction));
        snackbar.show();//show snackbar.

    }

    private void getParentsContactedDetails() {
        //Volley http library to do networking
        //make an post request to server of gps using http library and store all the details in ArrayList
        requestQueue = Volley.newRequestQueue(ParentsContacted.this);
        //It is the url on which we make an network request for post or get
        String url = "https://api.gharpeshiksha.com/Tutor/studentContacted";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.v("response---", response.length() + "");
                    JSONObject jsonObjects = new JSONObject(response);//convert string in JSONObject class instance or
                    Log.v("ParentsContacted.java", jsonObjects.toString());
                    if(response.length() <= 2) {
                        //gone recycler view list
                        recyclerView.setVisibility(View.GONE);
                        //visible no Parents Contacted textview
                        noContacted.setVisibility(View.VISIBLE);
                    } else {
                        //format
                        String date = jsonObjects.getString("date");
                        String fees = jsonObjects.getString("fees");
                        String address = jsonObjects.getString("address");
                        String altContact = jsonObjects.getString("alt_contact");
                        String contact = jsonObjects.getString("contact");
                        String name = jsonObjects.getString("name");
                        String userClass = jsonObjects.getString("cls");
                        int enqId = jsonObjects.getInt("enq_id");
                        String teachingMode = jsonObjects.getString("teaching_mode");
                        list.add(new ParentsContactedModel(date, fees, address, altContact, contact, name, userClass, teachingMode, enqId));
                    }

                } catch (Exception e) {
                    noContacted.setVisibility(View.VISIBLE);
                    noContacted.setText("Internal Error");
                    Log.v("ParentsContacted.java", e.getMessage() + "fff");
                }
                //Adapter of RecyclerView list
                ParentsContactedAdapter adapter = new ParentsContactedAdapter(list);
                //set adapter on list
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("ParentsContacted.java", error.getMessage() + "ff");
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("contact", phone);
                return params;
            }
        };

        requestQueue.add(postRequest);
    }
}