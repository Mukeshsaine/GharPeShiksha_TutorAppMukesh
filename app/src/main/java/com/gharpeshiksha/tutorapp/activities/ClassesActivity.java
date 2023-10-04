package com.gharpeshiksha.tutorapp.activities;

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
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.Adapter.CustomExpendibleViewAdaptor;
import com.gharpeshiksha.tutorapp.constant.ConstantManager;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ClassesActivity extends AppCompatActivity {

    String TAG = "ClassesActivity";
    Button next;
    static List<String> course;

    static LinkedHashMap<String, List<String>> subjects = new LinkedHashMap<>();
    List<String> selectedsubjects = new ArrayList<>();
    ExpandableListView Exp_list;
    String status[][];
    String status1[];
    Toolbar toolbar;

    SessionConfig sessionConfig;


    RequestQueue requestQueue;
    String Server_url;


    long phone;
    private String moveto = "";
    private ProgressDialog progress;
    private Dialog dialog;
    private Button retry;
    private int flage = 0;

    String userStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        sessionConfig = new SessionConfig(ClassesActivity.this);

        final Intent intent = getIntent();


        //user status is new or old comes from Intent
        userStatus = intent.getStringExtra("userStatus");

        next = findViewById(R.id.next);
        Exp_list = findViewById(R.id.exp_list);

        try {
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("GharPeShiksha");
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            setSupportActionBar(toolbar);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        phone = sessionConfig.getPhone();
        if (isNetworkAvailable()) {
            RequestAllClasses(phone);

            Log.d(TAG, "onCreate() returned: " + phone);
        } else {
            noNetworkDialog();
        }
    }


    private void statusInitialize() {

        status = new String[course.size()][50];
        status1 = new String[course.size()];
        for (int i = 0; i < course.size(); i++) {

            status1[i] = ConstantManager.CHECK_BOX_CHECKED_FALSE;
            for (int j = 0; j < subjects.get(course.get(i)).size(); j++) {

                status[i][j] = ConstantManager.CHECK_BOX_CHECKED_FALSE;
            }
        }

        listeners();
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
                    if (flage == 0) {
                        RequestAllClasses(phone);
                    }


                } else {

                    dialog.dismiss();
                    noNetworkDialog();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNetworkAvailable()) {

            noNetworkDialog();

        }

    }

    private void listeners() {
        Exp_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Log.e("listns:groupPosition: " + groupPosition, "childPosition: " + childPosition);

                Log.e(TAG, "(status[groupPosition][childPosition]" + status[groupPosition][childPosition]);

                if (status[groupPosition][childPosition].equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                    Log.e("YES  :groupPosition: " + groupPosition, "childPosition: " + childPosition);

                    ((CheckedTextView) v.findViewById(R.id.child_item)).setChecked(false);
                    status[groupPosition][childPosition] = ConstantManager.CHECK_BOX_CHECKED_FALSE;
                } else {
                    Log.e("NO  :groupPosition: " + groupPosition, "childPosition: " + childPosition);
                    status[groupPosition][childPosition] = ConstantManager.CHECK_BOX_CHECKED_TRUE;
                    ((CheckedTextView) v.findViewById(R.id.child_item)).setChecked(true);
                }

                String selectedItem = ((TextView) v).getText().toString();
                if (selectedsubjects.contains(selectedItem)) {
                    selectedsubjects.remove(selectedItem); //add item for check
                } else {

                    selectedsubjects.add(selectedItem);  //remove item  for uncheck
                }

                if (selectedsubjects.isEmpty()) {
                    next.setVisibility(View.GONE);
                } else {
                    next.setVisibility(View.VISIBLE);
                }


                return true;
            }
        });


        Exp_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {


                if (status1[groupPosition] == ConstantManager.CHECK_BOX_CHECKED_TRUE) {

                    Exp_list.expandGroup(groupPosition, true);
                    status1[groupPosition] = ConstantManager.CHECK_BOX_CHECKED_FALSE;
                } else {
                    Exp_list.collapseGroup(groupPosition);
                    status1[groupPosition] = ConstantManager.CHECK_BOX_CHECKED_TRUE;
                }

                return false;
            }

        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] selectedsubjectsstr = selectedsubjects.toArray(new String[0]);
                HashMap<String, String> course_subject = new HashMap<String, String>();

                for (String Selectedsubjectsstr : selectedsubjectsstr) {
                    String course_subjects[] = Selectedsubjectsstr.split(" -  ");
                    if (course_subject.get(course_subjects[0]) == null) {
                        course_subject.put(course_subjects[0], course_subjects[1] + ",");
                    } else {
                        course_subject.put(course_subjects[0], course_subject.get(course_subjects[0]) + course_subjects[1] + ",");
                    }
                }
                String dataFormate = "";
                for (String key : course_subject.keySet()) {
                    dataFormate += key + "<" + course_subject.get(key).substring(0, course_subject.get(key).length() - 1) + "@";
                }

                if (isNetworkAvailable()) {

                    try {
                        //changed on 11/28/2022 2nd param instead of phone variable
                        CreateResponse(dataFormate.substring(0, dataFormate.length() - 1), phone);
                    } catch (IndexOutOfBoundsException e) {

                    }

                } else {
                    noNetworkDialog();
                }

            }
        });


    }


    private void getSelectedCourse(String course_subject) {

        String get_selected_courses_subjects[] = course_subject.split("@");
        List<String> get_selected_course = new ArrayList<>();
        HashMap<String, List<String>> get_selected_subjects = new HashMap<>();

        try {
            for (String courses_subjects : get_selected_courses_subjects) {
                get_selected_course.add(courses_subjects.split("<")[0]);
                String subj[] = courses_subjects.split("<")[1].split(",");
                List<String> subs = new ArrayList<>();

                for (String s : subj) {
                    subs.add(courses_subjects.split("<")[0] + " -  " + s);
                }
                get_selected_subjects.put(courses_subjects.split("<")[0], subs);
            }
        } catch (Exception e) {
            Log.e(TAG, "getSelectedCourse: " + e);
        }
        int i = 0;
        for (String key : subjects.keySet()) {
            if (get_selected_subjects.containsKey(key)) {
                status1[i] = ConstantManager.CHECK_BOX_CHECKED_TRUE;
                List<String> all_sub = subjects.get(key);
                List<String> selected_subjects = get_selected_subjects.get(key);

                for (String sub : selected_subjects) {

                    if (all_sub.contains(sub)) {

                        int j = all_sub.indexOf(sub);
                        selectedsubjects.add(all_sub.get(j));
                        status[i][j] = ConstantManager.CHECK_BOX_CHECKED_TRUE;
                    }
                }
            }
            i++;
        }
    }


    private void CreateResponse(final String selectedCourse, final long phone) {

        start_request_loadingProcess();
        Log.e(TAG, "CreateResponse: selectedCourse: " + selectedCourse);
        requestQueue.start();
        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/Classes_Selected";
        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e(TAG, "CreateResponse: onResponse" + response);
                        progress.dismiss();
                        try {
                            JSONObject jsResponse = new JSONObject(response);
                            //   Log.e(TAG,"sResponse.get()"+jsResponse.get("Status"));
                            if (jsResponse.get("Status").equals("Success")) {


                                //changes in equals() method string with 1 28/11/2022
                                if (moveto.equals("Dashboard")) {
                                    startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    finish();
                                } else {
                                    if (getIntent().hasExtra("verifyotp")) {
                                        if (getIntent().getBooleanExtra("verifyotp", false)) {
                                            startActivity(new Intent(getApplicationContext(), QualificationActivity.class)
                                                    .putExtra("verifyotp", true).putExtra("userStatus", userStatus));
                                        }
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), QualificationActivity.class).putExtra("userStatus", "" + userStatus));
                                    }


                                    finish();
                                }

                            } else {
                                Log.e(TAG, response + "not opening Qualification");
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
                       /* new BasicFeature.Builder(getApplicationContext())

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
                Log.e(TAG, "CreateResponse:  getParams()");
                Map<String, String> params = new HashMap<String, String>();
                params.put("SelectedCourse", selectedCourse);
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


    void RequestAllClasses(long phone) {
        start_request_loadingProcess();
        course = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        //?phone="+contact_no+"&password="+password+"&email="+email;
        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/Classes?phone=" + phone;//changes in number instead of phone 28/11/2022
        StringRequest postRequest = new StringRequest(Request.Method.GET, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String course_subject = "";
                        Log.e(TAG, "onResponse:response " + response);
                        flage = 1;

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject selectedcourse = new JSONObject(jsonArray.get(0).toString());//     (JSONObject) jsonArray.get(0);
                            course_subject = selectedcourse.get("selectedcourse").toString();

                            for (int i = 1; i < jsonArray.length() - 1; i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                course.add(jsonObject.get("Course").toString());
                                JSONArray jsonArray1 = new JSONArray(jsonObject.get("Subjects").toString());
                                jsonArray1.length();
                                List<String> sub = new ArrayList<>();
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    sub.add(course.get(i - 1) + " -  " + jsonArray1.get(j).toString());
                                }
                                subjects.put(course.get(i - 1), sub);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        statusInitialize();

                        if (!course_subject.equalsIgnoreCase("No data")) {

                            getSelectedCourse(course_subject);
                            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_nevigation_icon));

                            nevigationenable();
                            moveto = "Dashboard";
                            next.setText("Submit");
                            next.setVisibility(View.VISIBLE);
                        }

                        Exp_list.setAdapter(new CustomExpendibleViewAdaptor(getApplicationContext(), course, subjects, false, status, status1, Exp_list = findViewById(R.id.exp_list)));
                        progress.dismiss();
                        requestQueue.stop();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        /*new BasicFeature.Builder(getApplicationContext())

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                        flage = 0;
                        progress.dismiss();
                        Log.e(TAG, "CreateResponse: Error.Response" + error.getMessage());
                    }
                });
        requestQueue.add(postRequest);
    }

    private void nevigationenable() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}