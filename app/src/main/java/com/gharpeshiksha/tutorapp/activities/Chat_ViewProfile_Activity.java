package com.gharpeshiksha.tutorapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gharpeshiksha.tutorapp.BasicFeatures;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.UpgradeActivity;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.data_model.ClassesEnquiryModel;
import com.gharpeshiksha.tutorapp.data_model.ClassesForMe;
import com.gharpeshiksha.tutorapp.data_model.StudentDetailsModel;
import com.gharpeshiksha.tutorapp.databinding.ActivityChatViewProfileBinding;
import com.gharpeshiksha.tutorapp.retrofit.ApiInterface;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.gharpeshiksha.tutorapp.utils.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jp.wasabeef.blurry.Blurry;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat_ViewProfile_Activity extends AppCompatActivity implements OnMapReadyCallback {
    private String TAG = "ViewProfile.java", studentUUId = "NA", tutorUUId = "NA", conversationEnqId = "";
    ActivityChatViewProfileBinding binding;
    private ApiInterface apiInterface;
    private String tempId;
    private Intent receivedIntent;
    private GoogleMap mGoogleMap;
    private double lat, lng;
    private SessionConfig sessionConfig;
    private BasicFeatures basicFeatures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = ActivityChatViewProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.Studentname.setCompoundDrawables
                    (getResources().getDrawable(R.drawable.lock_icon), null, null, null);
        }
        receivedIntent = getIntent();
        sessionConfig = new SessionConfig(Chat_ViewProfile_Activity.this);
        if (receivedIntent.hasExtra("studentUUId")) {
            studentUUId = receivedIntent.getStringExtra("studentUUId");
            tutorUUId = receivedIntent.getStringExtra("tutorUUId");
            conversationEnqId = receivedIntent.getStringExtra("enqId");
        }
        Log.d(TAG, "onCreate enq id: " + conversationEnqId);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        setListeners();
        basicFeatures = new BasicFeatures();
        init();
        binding.swiperfresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
            }
        });
    }

    private void init() {
        basicFeatures.CheckPaymentStatus(Chat_ViewProfile_Activity.this, new SessionConfig(this).getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {
                Log.d(TAG, "Result: " + status);
                if (status.matches("active")) {
                    getStudentDetails(status);
                } else if (status.matches("nonpaid") || status.matches("expired")) {
                    getStudentDetails(status);
                } else if (status.matches("free")) {
                    getStudentDetails(status);
                } else if (status.matches("freeactivation")) {
                    getStudentDetails(status);
                }
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError: error");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        showLocation();
    }

    private void showLocation() {
        LatLng latLng = new LatLng(lng, lat);
        mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    private void getStudentDetails(String status) {
        RequestQueue que = Volley.newRequestQueue(Chat_ViewProfile_Activity.this);
        String url = "https://api.gharpeshiksha.com/Chats/viewStudentDetail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    binding.swiperfresh.setRefreshing(false);
                    JSONArray arr = new JSONArray(response);
                    JSONObject studentJs = arr.getJSONObject(0);
                    JSONObject classesJs = arr.getJSONObject(1);
                    StudentDetailsModel res = new Gson().fromJson(studentJs.toString(), StudentDetailsModel.class);
                    ClassesEnquiryModel classesForMe = new Gson().fromJson(classesJs.toString(), ClassesEnquiryModel.class);
                    tempId = classesForMe.getEnqId().toString();
                    Log.d(TAG, "onResponse requirement: " + classesJs);

                    if (status.matches("active")) {
                        Glide.with(Chat_ViewProfile_Activity.this).load(res.getProfileURL()).placeholder(R.drawable.person).into(binding.imageStudent);
                        binding.Studentname.setText(res.getName());
                        binding.AddressStudents.setText(res.getAddress());
                        binding.lastActiveTime.setText("Last Active: " + res.getLastActiveTime());
                        binding.cls.setText(res.getCls());
                        binding.sub.setText(res.getSub());
                        binding.enquiryRL.setBackgroundColor(getResources().getColor(R.color.white));
                        binding.mapRL.setVisibility(View.GONE);
                        binding.upgradeBtn2.setVisibility(View.GONE);
                        binding.upgradeBtn.setVisibility(View.GONE);
                        lat = res.getLat();
                        lng = res.getLon();

                        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.profileMap);
                        map.getMapAsync(Chat_ViewProfile_Activity.this);

                        /**Requirement data inject code */
                        binding.textMins.setText("More than " + classesJs.getString("time"));
                        binding.textClaTutorReq.setText("Tutor Requirement for " + classesForMe.getClass_() + " for " +
                                classesForMe.getSubjects());
                        binding.textClaName.setText(classesForMe.getName());
                        binding.textBudget.setText(classesForMe.getBudget());
                        binding.textLoc.setText(classesForMe.getArea());
                        binding.textDis.setText(classesForMe.getDistance() + " Kms");

                        if (TextUtils.equals(classesForMe.getTutorsContacted().toString(), "0")) {
                            binding.textViews.setText("Be the first one to contact");
                        } else {
                            binding.textViews.setText(classesForMe.getTutorsContacted() + " Tutors Contacted");
                        }

                        if (TextUtils.equals(classesJs.getString("favorite"), "true")) {
                            binding.favourite.setImageResource(R.drawable.favourite_true);
                        } else {
                            binding.favourite.setImageResource(R.drawable.favourite_false);
                        }

                        binding.share.setOnClickListener(v -> {
                            String shareBody =   // STRING TO BE SHARED
                                    "\uD83D\uDCDA Requirement of subjects : "
                                            + "Tutor Requirement for " + classesForMe.getClass_() + " for " + classesForMe.getSubjects() // SUBJECTS
                                            + "\n\n\u20B9 Amount offered : "
                                            + classesForMe.getBudget()  // BUDGET
                                            + "\n\n\uD83D\uDCCD Location of teaching : "
                                            + classesForMe.getArea()  // LOCATION
                                            + "\n\n\uD83D\uDCF1 Find more classes at " + "https://play.google.com/store/apps/details?id=com.gharpeshiksha.tutorapp"
                                            + "\n\nOR"
                                            + "\n\n\uD83D\uDCF1 Explore our website : https://gharpeshiksha.com/";
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Share via..."));
                        });

                        Map<String, String> PostFavDataMap = new HashMap<>();
                        binding.favourite.setOnClickListener(v -> {
//                            Toast.makeText(context, "" + classesForMe.getTextViews(), Toast.LENGTH_SHORT).show();
                            if (TextUtils.equals(classesForMe.getFavorite(), "true")) {
                                binding.favourite.setImageResource(R.drawable.favourite_false);
                                PostFavDataMap.put("phone", "" + sessionConfig.getPhone());
                                PostFavDataMap.put("enq_id", "" + classesForMe.getEnqId());
                                PostFavDataMap.put("action", "remove");
                                String Text4Toast = "Removed from favourite";
                                VolleyForFavourite(PostFavDataMap, Text4Toast);
                            } else {
                                binding.favourite.setImageResource(R.drawable.favourite_true);
                                PostFavDataMap.put("phone", "" + sessionConfig.getPhone());
                                PostFavDataMap.put("enq_id", "" + classesForMe.getEnqId());
                                PostFavDataMap.put("action", "add");
                                String Text4Toast = "Added to favourite";
                                VolleyForFavourite(PostFavDataMap, Text4Toast);
                            }
                        });

                        binding.enquiryCard.setOnClickListener(v -> {
                            if (Utility.isNetworkAvailable(Chat_ViewProfile_Activity.this)) {
                                try {
                                    Intent intent = new Intent(Chat_ViewProfile_Activity.this, ClassesForMeActivity.class);
                                    intent.putExtra("minDis", classesForMe.getDistance());
                                    intent.putExtra("enqId", Long.parseLong(classesForMe.getEnqId().toString()));
                                    intent.putExtra("viewContact", "noView");
                                    startActivity(intent);
                                } catch (Exception e) {
                                    Log.v("ClassesFor.java", e + "");
                                }
                            } else {
                                noNetworkDialog("", classesForMe);
                            }
                        });

                        binding.textCon.setOnClickListener(view -> {
                            if (Utility.isNetworkAvailable(Chat_ViewProfile_Activity.this)) {
                                BasicFeatures basicFeatures = new BasicFeatures();
                                if (status.matches("active")) {
                                    Log.v("Classes---", "active2");
                                    showMsgBeforecall(classesForMe);
                                } else if (status.matches("nonpaid") || status.matches("expired")){
                                    startActivity(new Intent(Chat_ViewProfile_Activity.this, UpgradeActivity.class));
                                    Log.v("Classes---", "expired");
                                }
                                else if (status.matches("freeactivation"))
                                    showMsgBeforecall(classesForMe);
                                else if (status.matches("free"))
                                    basicFeatures.showFreePaidDialog(Chat_ViewProfile_Activity.this);
                            } else {
                                noNetworkDialog(status, classesForMe);
                            }
                        });

                        if(classesForMe.getEnqViewed()) {
                            binding.textCon.setVisibility(View.GONE);
                            binding.textcall.setVisibility(View.VISIBLE);
                            binding.textcall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RetrofitClient.getClient().create(ApiServies.class)
                                            .getContactNum(sessionConfig.getPhone() + "", classesForMe.getEnqId().toString())
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if(response.isSuccessful() && response.body() != null) {
                                                        try {
                                                            Intent i = new Intent(Intent.ACTION_DIAL);
                                                            String con = new JSONObject(response.body().string()).getString("contact");
                                                            /**Below is to set Data and pass in Intent broadcast request to dialer.
                                                             * in URI(Uniform Resource Identifier) format can be understand by android system*/
                                                            i.setData(Uri.parse("tel:" + con));
                                                            startActivity(i);
                                                        } catch (Exception e) {
                                                            Log.d(TAG, "onResponse: " + e);
                                                        }}
                                                }
                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Log.d(TAG, "onFailure: " + t);
                                                }
                                            });
                                }
                            });
                        }

                    } else {
                        lockProfile();

                        Glide.with(Chat_ViewProfile_Activity.this).load(res.getProfileURL()).placeholder(R.drawable.person)
                                .into(binding.imageStudent);
                        binding.Studentname.setText(res.getName().substring(0, 4) + Utility.getStars(res.getName().length()));
                        binding.AddressStudents.setText(res.getAddress().substring(0, 4) + Utility.getStars(res.getAddress().length()));
                        binding.lastActiveTime.setText("Last Active: " + res.getLastActiveTime());
                        binding.cls.setText(res.getCls().substring(0, 4) + Utility.getStars(res.getCls().length()));
                        binding.sub.setText(res.getSub().substring(0, 4) + Utility.getStars(res.getSub().length()));
                    }
                } catch (Exception e) {
                    lockProfile();
                    Log.d(TAG, "onResponse: " + e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lockProfile();
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("studentUUID", studentUUId);
                map.put("tutorUUID", tutorUUId);
                map.put("enqId", conversationEnqId);
                Log.d(TAG, "getParams: " + map);
                return map;
            }
        };

        que.add(stringRequest);
    }

    private void showMsgBeforecall(ClassesEnquiryModel classesForMe) {
        final ProgressDialog progressDialog1 = new ProgressDialog(Chat_ViewProfile_Activity.this);
        progressDialog1.setCancelable(false);
        progressDialog1.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                progressDialog1.dismiss();
                return true;
            }

            return false;
        });
        progressDialog1.setMessage("Fetching data...");
        progressDialog1.show();
        ActiveDialog(classesForMe, progressDialog1);
    }

    protected void noNetworkDialog(String status, ClassesEnquiryModel classesForMe) {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.no_network_dialog);
        View retry = dialog.findViewById(R.id.retry);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        retry.setOnClickListener(v -> {
            dialog.dismiss();
            if (Utility.isNetworkAvailable(Chat_ViewProfile_Activity.this)) {
                dialog.dismiss();
                if (!status.isEmpty()) {
                    BasicFeatures basicFeatures = new BasicFeatures();
                    if (status.matches("active")) {
                        Log.v("Classes---", "active2");
                        showMsgBeforecall(classesForMe);
                    } else if (status.matches("nonpaid") || status.matches("expired")){
                        startActivity(new Intent(Chat_ViewProfile_Activity.this, UpgradeActivity.class));
                        Log.v("Classes---", "expired");
                    }
                    else if (status.matches("freeactivation"))
                        showMsgBeforecall(classesForMe);
                    else if (status.matches("free"))
                        basicFeatures.showFreePaidDialog(Chat_ViewProfile_Activity.this);
                } else {
                    try {
                        Intent intent = new Intent(Chat_ViewProfile_Activity.this, ClassesForMeActivity.class);
                        intent.putExtra("minDis", classesForMe.getDistance());
                        intent.putExtra("enqId", Long.parseLong(classesForMe.getEnqId().toString()));
                        intent.putExtra("viewContact", "noView");
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.v("ClassesFor.java", e + "");
                    }
                }
            } else {
                dialog.dismiss();
                noNetworkDialog(status, classesForMe);
            }
        });

    }

    private void ActiveDialog(final ClassesEnquiryModel classesForMe, final ProgressDialog prg) {
//        Log.v("Classes---", tempId + ", " + sessionConfig.getPhone());
        String url = "https://api.gharpeshiksha.com" + "/Tutor/viewContactMsg";

        RequestQueue requestQueue = Volley.newRequestQueue(Chat_ViewProfile_Activity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {

                prg.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                if (TextUtils.equals(jsonObject.getString("status"), "success")) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Chat_ViewProfile_Activity.this, R.style.AppCompatAlertDialogStyle);
                    alertDialog.setTitle("View Contact");

//                    alertDialog.setMessage(jsonObject.getString("message"));
                    String htmlMessage = null;
                    if (jsonObject.has("message")) {
                        htmlMessage = jsonObject.getString("message");
                    }
                    //Below Html.fromHtml(String stringDoc, flag) is work only for api lever 24(N) or above else use fromHtml() method without flag
                    //and if you want to render img tag image from url then you need to Picasso library to load image from url and convert it in
                    //drawable by extend ImageGetter subclass of Html class then you have to pass ImageGetter and TagHandler additional to fromHtml() params
                    //and if you want functioning anchor text fromHtml to TextView use TagHandler class instance and pass it in fromHtml() method params.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        alertDialog.setMessage(Html.fromHtml(htmlMessage, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        alertDialog.setMessage(htmlMessage);
                    }
                    alertDialog.setIcon(R.drawable.icon_phone);

                    alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                        viewContactFun(Chat_ViewProfile_Activity.this, tempId);
                    });
                    alertDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());


                    alertDialog.show();


                } else if (TextUtils.equals(jsonObject.getString("status"), "error")) {

                    if (jsonObject.has("time_left")) {
                        new BasicFeatures().AlertDialogFreeViews(Chat_ViewProfile_Activity.this, jsonObject.getLong("time_left"));
                    }
                }

            } catch (JSONException e) {

                prg.dismiss();
            }


        }, error -> {

            prg.dismiss();
//            noNetworkDialog();

        }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("phone", "" + sessionConfig.getPhone());

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

    VolleyHelper volleyHelper;

    private void viewContactFun(Context context, String id) {
        String RealnumberURL = "https://api.gharpeshiksha.com" + "/GetClasses/getContact";
        Map<String, String> params = new HashMap<>();
        params.put("phone", sessionConfig.getPhone() + "");
        params.put("enq_id", id);
        Log.v("Classes---", params + "");
        if (volleyHelper == null) {
            volleyHelper = new VolleyHelper();
        }
        volleyHelper.VolleyPostRequest(context, RealnumberURL, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    final JSONObject part = new JSONObject(response);

                    String result = part.getString("result");
                    if (result.equals("success")) {
                        Log.v("Classes---", "student contacted" + part.getString("phone"));
                        String num = part.getString("phone");
                        Toast.makeText(context, "1 contact viewed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_DIAL); // opens up all available dialers
                        intent.setData(Uri.parse("tel:" + num)); // tel:  is required or else app will crash
                        context.startActivity(intent); // startActivity


                    } else {
                        if (result.matches("error")) {
                            if (part.has("time_left")) {
                                new BasicFeatures().AlertDialogFreeViews(context, part.getLong("time_left"));
                            } else {
                                try {
                                    new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                                            .setMessage(part.getString("message"))
                                            .setPositiveButton("Okay", null)
                                            .show();
                                } catch (Exception e) {
                                    Toast.makeText(context, "Unable to view this class", Toast.LENGTH_SHORT).show();
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

    private void VolleyForFavourite(final Map<String, String> postFavDataMap, final String Text4Toast) {
        String favourite_action_URL = "https://api.gharpeshiksha.com" + "/GetClasses/favorite";

        RequestQueue requestQueue = Volley.newRequestQueue(Chat_ViewProfile_Activity.this);
        StringRequest favouriteRequest = new StringRequest(Request.Method.POST, favourite_action_URL, response -> Toast.makeText(Chat_ViewProfile_Activity.this, Text4Toast, Toast.LENGTH_SHORT).show(), error -> Log.d("Server Error : ", "error : " + error.getMessage())) {

            @Override
            protected Map<String, String> getParams() {
                Log.e("checkPassedJSON", postFavDataMap.toString());
                return postFavDataMap;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        favouriteRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(favouriteRequest);
    }

    private void intent(Class intentClass, ClassesEnquiryModel classesForMe, String view) {
        try {
            Intent intent = new Intent(Chat_ViewProfile_Activity.this, intentClass);
            intent.putExtra("minDis", classesForMe.getDistance());
            intent.putExtra("enqId", classesForMe.getEnqId());
            intent.putExtra("viewContact", view);
            startActivity(intent);
        } catch (Exception e) {
            Log.v("ClassesFor.java", e + "");
        }
    }

    /**
     * Below method is give a blur effect on images
     */
    private void lockProfile() {
        binding.enquiryRL.setBackgroundColor(getResources().getColor(R.color.blur_color));
        binding.mapRL.setBackgroundColor(getResources().getColor(R.color.blur_color));

        binding.upgradeBtn2.setOnClickListener(view -> {
            Intent i = new Intent(Chat_ViewProfile_Activity.this, UpgradeActivity.class);
            startActivity(i);
        });

        binding.upgradeBtn.setOnClickListener(view -> {
            Intent i = new Intent(Chat_ViewProfile_Activity.this, UpgradeActivity.class);
            startActivity(i);
        });

        Blurry.with(Chat_ViewProfile_Activity.this)
                .radius(10)
                .sampling(8)
                .color(Color.argb(66, 0, 0, 0))
                .async()
                .animate(500)
                .onto(binding.enquiryRL);

        Blurry.with(Chat_ViewProfile_Activity.this)
                .radius(10)
                .sampling(8)
                .color(Color.argb(5, 0, 0, 0))
                .async()
                .animate(500)
                .onto(binding.mapRL);
    }

    private void setListeners() {
        binding.backchat.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }
}