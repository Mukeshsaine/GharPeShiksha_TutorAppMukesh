package com.gharpeshiksha.tutorapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.activities.OTPActivity;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangrNumberActivity extends AppCompatActivity {

    private static final String TAG = "ChangrNumberActivity";
    RelativeLayout relativeLayout;
    EditText phonenumber;
    ProgressDialog progress;
    Boolean camefromsignup;
    CardView enter;
    SessionConfig sessionObj;
    RequestQueue requestQueue;
    String Server_url;
    String phnumber;
    Long oldnumber;
    SessionConfig sessionConfig;
    String email;
    String Token = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changr_number);

        relativeLayout = findViewById(R.id.changenuberrelativelayout);
        phonenumber = findViewById(R.id.numbers);
        enter = findViewById(R.id.enter);


        sessionObj = new SessionConfig(getApplicationContext());
        final Intent intent = getIntent();

        camefromsignup = intent.getBooleanExtra("camefromsignup", false);


        oldnumber = intent.getLongExtra("Oldnumber", 1230);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phonenumber.getText().length() == 10) {

                    phnumber = phonenumber.getText().toString();

                    ChangeNumber(oldnumber, Long.parseLong(phnumber));

                } else {
                    Toast.makeText(ChangrNumberActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) ChangrNumberActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });
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

 /*   public void checkuserhaveclass(){

        requestQueue =Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        Server_url = "https://api.gharpeshiksha.com"+"/Tutor/SignInPassword";

        StringRequest postRequest = new StringRequest(Request.Method.POST,Server_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                    //    progress.dismiss();

                  //      Log.e(TAG,"CreateResponse: onResponse"+response);
                        try {
                            JSONObject jsResponse=new JSONObject(response);
                            if( jsResponse.get("Status").equals("Success") ){
                                Toast.makeText(getApplicationContext()," You already have account on this number",Toast.LENGTH_LONG).show();
                            }
                            else {

                                ChangeNumber(oldnumber,Long.parseLong(phonenumber.getText().toString()));


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestQueue.stop();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       *//* progress.dismiss();
                        Log.e(TAG,"CreateResponse: Error.Response"+error.getMessage());*//*
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {


                Map<String, String>  params = new HashMap<>();
                params.put("phone", phonenumber.getText().toString());


                return params;
            }
        };
        requestQueue.add(postRequest);
    }
*/

   /* private void CreateRequest() {
        start_request_loadingProcess();



        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        Server_url = "https://api.gharpeshiksha.com"+"/Tutor/SignUp"; //?phone="+contact_no+"&password="+password+"&email="+email;


        StringRequest postRequest = new StringRequest(Request.Method.POST,Server_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.i("tag","CreateResponse: onResponse"+response);

                        try {
                            progress.dismiss();
                            JSONObject jsResponse=new JSONObject(response);
                            //   Log.e(TAG,"sResponse.get()"+jsResponse.get("Status"));
                            if( jsResponse.get("Status").equals("Success") ){

                                // create session here
                                sessionObj.setPhone(Long.parseLong(phonenumber.getText().toString()));
                                startActivity(new Intent(getApplicationContext(), ClassesActivity.class));
                                finish();
                            }
                            else if( jsResponse.get("Status").equals("Error") && jsResponse.get("Message").equals("Invalid Contact") ){
                                Toast.makeText(getApplicationContext(),jsResponse.getString("Message")+":"+phonenumber.getText().toString(),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"You have already a account with: "+phonenumber.getText().toString()+" \nPlease Sign In..",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        requestQueue.stop();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progress.dismiss();
                        Log.i("TAG","CreateResponse: Error.Response"+error.getMessage());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {

                Log.i("Tag","CreateResponse:  getParams()");
                Map<String, String>  params = new HashMap<>();
                params.put("phone", phonenumber.getText().toString());
                params.put("password", "123456");
                params.put("email", email);
                params.put("firebaseToken",Token );
                return params;
            }
        };


        requestQueue.add(postRequest);

    }
*/

    // Update Number


    public void ChangeNumber(final Long phone, final Long new_phone) {


        Log.d(TAG, "ChangeNumber: changenumber : " + phone + "   " + new_phone);
        String Url = "https://api.gharpeshiksha.com" + "/TutorLogin/changeNumber";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("result").equals("success")) {

                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        sessionObj.setPhone(new_phone);
                        intent.putExtra("contact", new_phone);
                        SharedPreferences sharedPreferences = getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);

                        intent.putExtra("camefromarearange", true);
                        startActivity(intent);
                        Toast.makeText(ChangrNumberActivity.this, "Number Update", Toast.LENGTH_SHORT).show();

                        finish();

                    } else if (jsonObject.getString("result").equals("error")) {

                        Toast.makeText(ChangrNumberActivity.this, "Number Already Exits.Try with another number.", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new android.app.AlertDialog.Builder(ChangrNumberActivity.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay", null)
                        .show();
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Log.d("tag", "getParams: getParams()");
                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + phone);
                params.put("newPhone", "" + new_phone);

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





