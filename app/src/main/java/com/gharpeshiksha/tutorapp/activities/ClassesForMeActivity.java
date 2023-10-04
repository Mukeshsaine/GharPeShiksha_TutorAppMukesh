package com.gharpeshiksha.tutorapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.BasicFeatures;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.UpgradeActivity;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClassesForMeActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String TAG = "ClassesForMeActivity.java";

    Toolbar toolbar;
    TextView textView, textClaName, textClaTutorReq, textClaContact, textClaBudget, textClaPref, textClaMes,
            textClaLoc, textClaDis, textClaViews, textClaId, textClaCon, textminsD, viewContact, closedtext,
            calltext, textUpgradeAPI;
    RelativeLayout upgradeLay;
    String enquiryUrl, statusUrl, contactUrl, RealnumberURL, favorite;
    long phone;
    ImageView share, favoriteB;
    ProgressDialog progressDialog;
    String budgetShare, areaShare, tutorReqStringShare, Realnumber;
    double latitude, longitude;
    LatLng sydney;
    GoogleMap googleMap;
    private SessionConfig sessionConfig;
    private Boolean paid = false;
    private String contact;
    private Map<String, String> PostFavDataMap = new HashMap<>();
    private String[] DynamicURLData = new String[10];
    private String dynamicENQ_ID, dynamicPhone, dynamicView_Info;
    private String favourite_action_URL = "https://api.gharpeshiksha.com" + "/GetClasses/favorite";
    boolean FIRST_CALL = true;
    private BasicFeatures basicFeatures;
    private VolleyHelper volleyHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_classes_for_me);

        volleyHelper = new VolleyHelper();
        progressDialog = new ProgressDialog(ClassesForMeActivity.this);
        basicFeatures = new BasicFeatures();
        DynamicURLData[0] = "not_received";
        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                progressDialog.dismiss();
                finish();
                return true;
            }

            return false;
        });
        progressDialog.setCancelable(false);
        sessionConfig = new SessionConfig(getApplicationContext());
        phone = sessionConfig.getPhone();
        //"9990461910";

        toolbar = findViewById(R.id.toolbarCla);
        toolbar.setTitle("Enquiry Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);

        calltext = findViewById(R.id.textcalhhhhl);
        closedtext = findViewById(R.id.closeddddd);
        viewContact = findViewById(R.id.textviewcon);
        favoriteB = findViewById(R.id.favButton);
        textView = findViewById(R.id.textClaUpgrade);
        textClaName = findViewById(R.id.textClaName);
        textClaTutorReq = findViewById(R.id.textClaTutorReq);
        textClaContact = findViewById(R.id.textClaContact);
        textClaBudget = findViewById(R.id.textClaBudget);
        textClaPref = findViewById(R.id.textClaPref);
        textClaMes = findViewById(R.id.textClaMes);
        textClaLoc = findViewById(R.id.textClaLoc);
        textClaDis = findViewById(R.id.textClaDis);
        textClaViews = findViewById(R.id.textClaViews);
        textClaId = findViewById(R.id.textClaId);
        textminsD = findViewById(R.id.textMinsD);
        share = findViewById(R.id.shareB);
        textUpgradeAPI = findViewById(R.id.text5);


        upgradeLay = findViewById(R.id.relLay8);


        enquiryUrl = "https://api.gharpeshiksha.com" + "/GetClasses/enqinfoTest";
        contactUrl = "https://api.gharpeshiksha.com" + "/ViewContact/view";
        statusUrl = "https://api.gharpeshiksha.com" + "/PaymentStatus/statusnew";
        RealnumberURL = "https://api.gharpeshiksha.com" + "/GetClasses/getContact";


        if (getIntent().hasExtra("enqId")) {
            Log.d(TAG, "onClick: on dakbhjgdjgfkjhd come from adapter");
            createResponse(enquiryUrl);
        }


        calltext.setOnClickListener(view -> enq_viewed());

        viewContact.setOnClickListener(v -> ActiveDialog());

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(ClassesForMeActivity.this, pendingDynamicLinkData -> {
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();

                        Log.e(TAG, "linkReceived " + deepLink);

                        String[] a = deepLink.toString().split("&");
                        a[0] = "received";
                        DynamicURLData = a;
                        Log.e(TAG, DynamicURLData[1] + DynamicURLData[2] + "");
                        for (int i = 0; i < a.length; i++) {
                            Log.e(TAG, DynamicURLData[i]);
                        }
                        dynamicPhone = DynamicURLData[1];
                        dynamicENQ_ID = DynamicURLData[2];
//                        //dynamicView_Info = DynamicURLData[3];
                        createResponse("https://api.gharpeshiksha.com" + "/GetClasses/enqinfoTest");
                    }

                }).addOnFailureListener(this, e -> Log.w(TAG, "getDynamicLink:onFailure", e));


        share.setOnClickListener(v -> {
            String shareBody =   // STRING TO BE SHARED
                    "\uD83D\uDCDA Requirement of subjects : "
                            + tutorReqStringShare // SUBJECTS
                            + "\n\n\u20B9 Amount offered : "
                            + budgetShare  // BUDGET
                            + "\n\n\uD83D\uDCCD Location of teaching : "
                            + areaShare  // LOCATION
                            + "\n\n\uD83D\uDCF1 Find more classes at " + "https://play.google.com/store/apps/details?id=com.gharpeshiksha.tutorapp"
                            + "\n\nOR"
                            + "\n\n\uD83D\uDCF1 Explore our website : https://gharpeshiksha.com/";
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via..."));
        });

        favoriteB.setOnClickListener(v -> {
            if (favorite.equals("true")) {
                favoriteB.setImageResource(R.drawable.favourite_false);
                PostFavDataMap.put("phone", "" + phone);
                PostFavDataMap.put("enq_id", "" + getIntent().getLongExtra("enqId", 0));
                PostFavDataMap.put("action", "remove");
                String Text4Toast = "Removed from favourite";
                VolleyForFavourite(PostFavDataMap, Text4Toast);
            } else {
                favoriteB.setImageResource(R.drawable.favourite_true);
                PostFavDataMap.put("phone", "" + phone);
                PostFavDataMap.put("enq_id", "" + getIntent().getLongExtra("enqId", 0));
                PostFavDataMap.put("action", "add");
                String Text4Toast = "Added to favourite";
                VolleyForFavourite(PostFavDataMap, Text4Toast);
            }
        });

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UpgradeActivity.class);
            startActivity(intent);
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }


        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        viewContact();

        intentViewContact();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void VolleyForFavourite(final Map<String, String> postFavDataMap, final String text4Toast) {

        volleyHelper.VolleyPostRequest(ClassesForMeActivity.this, favourite_action_URL, postFavDataMap, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                Toast.makeText(ClassesForMeActivity.this, text4Toast + "hello", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {

            }
        });


    }

    // FOR GOOGLE MAPS
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        showlocation();

    }

    private void showlocation() {

        sydney = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(sydney));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
    }

    private void createResponse(String url) {

        Map<String, String> params = new HashMap<>();

        Log.v("hasphone---", getIntent().getStringExtra("phone") + "1");
        if (DynamicURLData[0].equals("received")) {
            Log.v("hasphone---", getIntent().getStringExtra("phone") + "2");
            Log.e("DynamicLinksSplashPOST", dynamicENQ_ID + dynamicPhone);
            params.put("phone", dynamicPhone);
            params.put("enq_id", dynamicENQ_ID);
        } else {
            if (getIntent().hasExtra("phone")) {
                params.put("phone", getIntent().getStringExtra("phone"));
                params.put("enq_id", getIntent().getStringExtra("enqId"));
            } else {
                if (getIntent().getLongExtra("enqId", 0) == 0) {
                    ClassesForMeActivity.this.finish();
                } else {
                    params.put("phone", "" + phone);
                    params.put("enq_id", "" + getIntent().getLongExtra("enqId", 0));
                }
            }
        }

        Log.d(TAG, "createResponse: parameters: " + params);

        volleyHelper.VolleyPostRequest(ClassesForMeActivity.this, url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {

                try {
                    Log.d(TAG, "onResponse: " + response);

                    final JSONObject part = new JSONObject(response);


                    longitude = part.getDouble("longitude");
                    latitude = part.getDouble("latitude");
                    showlocation();

                    contact = part.getString("contact");

                    if (getIntent().hasExtra("viewContact")) {
                        if (getIntent().getStringExtra("viewContact").matches("calldirect")) {
                            if (FIRST_CALL) {
                                FIRST_CALL = false;
                                enq_viewed();
                            }
                        }
                    }

                    if (!DynamicURLData[0].equals("received")) {

                        if (getIntent().getStringExtra("viewContact").matches("enq_viewed")) {
                            if (FIRST_CALL) {
                                FIRST_CALL = false;
                                enq_viewed();
                            }
                        }
                    }

                    String tutors_contacted = part.getString("tutors_contacted");
                    String name = part.getString("name");
                    String classes = part.getString("class");
                    String subjects = part.getString("subjects");
                    tutorReqStringShare = subjects;
                    String budget = part.getString("budget");
                    budgetShare = budget;
                    String area = part.getString("area");
                    String upgradeText = part.getString("upgradetext");
                    String upgradeUTF8 = new String(upgradeText.getBytes("ISO-8859-1"), "UTF-8");
                    textUpgradeAPI.setText(upgradeUTF8);

                    String utf8String = new String(area.getBytes("ISO-8859-1"), "UTF-8");
                    areaShare = utf8String;
                    Log.e(TAG, "convertedtexttohindi " + utf8String);


                    String message = part.getString("message");
                    String gender = part.getString("gender");
                    favorite = part.getString("favorite");
                    String enqid = part.getString("enq_id");
                    String distance = part.getString("distance");
                    String time = part.getString("time");


                    String tutorReqString = "Tutor Requirement for " + subjects + " for " + classes;
                    String tutorsConString = tutors_contacted + " Tutors contacted";

                    String paymentstatus = part.getString("paymentstatus");

                    Log.d(TAG, "onResponse: bahjkafdhkjafshkfa0" + part.getBoolean("enq_viewed"));


                    Boolean viewedcontact = false;


                    if (!part.getString("enq_viewed").isEmpty()) {

                        viewedcontact = part.getBoolean("enq_viewed");

                    }


                    if (paymentstatus.matches("active") || paymentstatus.matches("expired")) {
                        upgradeLay.setVisibility(View.GONE);
                        viewContact.setVisibility(View.VISIBLE);

                        if (TextUtils.equals(part.getString("status"), "Cancelled")) {
                            closedtext.setVisibility(View.VISIBLE);
                            viewContact.setVisibility(View.GONE);
                            if (viewedcontact) {
                                calltext.setVisibility(View.VISIBLE);
                                closedtext.setVisibility(View.GONE);
                            }
                        } else if (TextUtils.equals(part.getString("status"), "submitted")) {
                            closedtext.setVisibility(View.GONE);
                            viewContact.setVisibility(View.VISIBLE);

                            if (viewedcontact) {
                                calltext.setVisibility(View.VISIBLE);
                                viewContact.setVisibility(View.GONE);
                            }
                        }


                    } else {
                        upgradeLay.setVisibility(View.VISIBLE);
                        viewContact.setVisibility(View.GONE);
                    }


                    if (favorite.equals("true")) {
                        favoriteB.setImageResource(R.drawable.favourite_true);
                    } else {
                        favoriteB.setImageResource(R.drawable.favourite_false);
                    }

                    textClaName.setText(name);
                    textClaContact.setText(contact);
                    textClaTutorReq.setText(tutorReqString);
                    textClaBudget.setText(budget);
                    textClaLoc.setText(utf8String);
                    textClaMes.setText(message);
                    textClaPref.setText(gender);
                    textClaDis.setText(distance + " Km");
                    textClaId.setText(enqid);
                    textminsD.setText(time);


                    if (TextUtils.equals(tutors_contacted, "0")) {
                        textClaViews.setText("Be the first one to contact student");
                    } else {
                        textClaViews.setText(tutorsConString);
                    }
                    progressDialog.dismiss();

                } catch (Exception e) {
                    Log.d(TAG, "onResponse Error: " + e.getMessage());
                    Toast.makeText(ClassesForMeActivity.this, e.getMessage() + "hello222", Toast.LENGTH_SHORT).show();
                    // progressDialog.dismiss();
                }
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "onFailure: " + error);
            }
        });
    }

    private void viewContact() {
        try {
            textClaCon.setOnClickListener(v -> {
                switch (textClaCon.getText().toString()) {
                    case "View Contact":
                        ActiveDialog();
                        break;
                    case "Call Student":
                        Toast.makeText(getApplicationContext(), "Called!", Toast.LENGTH_LONG).show();
                        break;
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "viewContact: " + e.getMessage());
        }
    }

    private void viewContactFun() {

        Map<String, String> params = new HashMap<>();
        if (DynamicURLData[0].equals("received")) {
            params.put("phone", dynamicPhone);
            params.put("enq_id", dynamicENQ_ID);
        } else {
            if (getIntent().hasExtra("from")) {
                params.put("phone", "" + getIntent().getStringExtra("phone"));
                params.put("enq_id", "" + getIntent().getStringExtra("enqId"));
            } else {
                params.put("phone", "" + phone);
                params.put("enq_id", "" + getIntent().getLongExtra("enqId", 0));
            }
        }


        volleyHelper.VolleyPostRequest(ClassesForMeActivity.this, RealnumberURL, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    final JSONObject part = new JSONObject(response);

                    String result = part.getString("result");
                    if (result.equals("success")) {


                        Realnumber = part.getString("phone");
                        Toast.makeText(getApplicationContext(), "1 contact viewed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_DIAL); // opens up all available dialers
                        intent.setData(Uri.parse("tel:" + Realnumber)); // tel:  is required or else app will crash
                        startActivity(intent); // startActivity


                    } else {
                        if (result.matches("error")) {
                            if (part.has("time_left")) {
                                basicFeatures.AlertDialogFreeViews(ClassesForMeActivity.this, part.getLong("time_left"));
                            } else {
                                try {
                                    new AlertDialog.Builder(ClassesForMeActivity.this, R.style.AppCompatAlertDialogStyle)
                                            .setMessage(part.getString("message"))
                                            .setPositiveButton("Okay", null)
                                            .show();
                                } catch (Exception e) {
                                    Toast.makeText(ClassesForMeActivity.this, "Unable to view this class", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void intentViewContact() {
        try {
            switch (getIntent().getStringExtra("viewContact")) {
                case "viewed":
                    viewContactFun();
                    break;

               /* case "calldirect":

                    enq_viewed();
                    break;*/

                case "free_activation":
                    freeActivationViewContact();
                    break;
            }
        } catch (Exception e) {

        }
    }

    private void freeActivationViewContact() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, RealnumberURL, response -> {
            try {
                final JSONObject part = new JSONObject(response);

                String result = part.getString("result");
                if (result.equals("success")) {

                    ActiveDialog();

                } else {

                    if (result.matches("error")) {
                        if (part.has("time_left")) {
                            basicFeatures.AlertDialogFreeViews(ClassesForMeActivity.this, part.getLong("time_left"));
                        }
                    }
                }
            } catch (Exception e) {
            }
        }, error -> {
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (DynamicURLData[0].equals("received")) {
                    Log.e("DynamicLinksSplashPOST", dynamicENQ_ID + dynamicPhone);
                    params.put("phone", dynamicPhone);
                    params.put("enq_id", dynamicENQ_ID);
                } else {
                    if (getIntent().hasExtra("from")) {
                        params.put("phone", "" + getIntent().getStringExtra("phone"));
                        params.put("enq_id", "" + getIntent().getStringExtra("enq_id"));
                    } else {
                        params.put("phone", "" + phone);
                        params.put("enq_id", "" + getIntent().getLongExtra("enqId", 0));
                    }
                }

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

    private void enq_viewed() {
        Intent intent = new Intent(Intent.ACTION_DIAL); // opens up all available dialer
//        Log.d(TAG, "enq_viewed: ConYACT " + contact);
        intent.setData(Uri.parse("tel:" + contact));
        // tel:  is required or else app will crash
        startActivity(intent); // startActivity
    }

    @Override
    protected void onResume() {
        super.onResume();

        // createResponse(enquiryUrl);
    }


    private void ActiveDialog() {

        String url = "https://api.gharpeshiksha.com" + "/Tutor/viewcontactmsg";
        Map<String, String> params = new HashMap<>();

        if (DynamicURLData[0].equals("received")) {
            Log.e("DynamicLinksSplashPOST", dynamicPhone);
            params.put("phone", dynamicPhone);

        } else {
            params.put("phone", "" + phone);
        }
        volleyHelper.VolleyPostRequest(ClassesForMeActivity.this, url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {

                try {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClassesForMeActivity.this, R.style.AppCompatAlertDialogStyle);
                    alertDialog.setTitle("View Contact");
                    alertDialog.setIcon(R.drawable.icon_phone);

                    JSONObject jsonObject = new JSONObject(response);
                    if (TextUtils.equals(jsonObject.getString("status"), "success")) {

                        alertDialog.setMessage(jsonObject.getString("message"));

                        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                            if (phone == 333) {
                                startActivity(new Intent(ClassesForMeActivity.this, Splash.class));
                                Toast.makeText(ClassesForMeActivity.this, "Please Login First", Toast.LENGTH_SHORT).show();
                            } else {
                                viewContactFun();
                            }
                        });
                        alertDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                        if (!isFinishing()) {
                            alertDialog.show();
                        }
                    } else if (TextUtils.equals(jsonObject.getString("status"), "error")) {
                        alertDialog.setMessage(jsonObject.getString("message"));
                        alertDialog.setPositiveButton("Okay", (dialog, which) -> dialog.cancel());
                        alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                        alertDialog.show();
                    }

                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        try {

            if (getIntent().hasExtra("from")) {
                startActivity(new Intent(ClassesForMeActivity.this, Dashboard.class));
                finish();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            super.onBackPressed();
        }

    }
}
