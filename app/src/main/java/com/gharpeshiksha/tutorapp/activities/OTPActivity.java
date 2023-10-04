package com.gharpeshiksha.tutorapp.activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.gharpeshiksha.tutorapp.ChangrNumberActivity;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.data_model.OtoPojo;
import com.gharpeshiksha.tutorapp.retrofit.ApiInterface;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitInstance;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.gharpeshiksha.tutorapp.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class OTPActivity extends AppCompatActivity {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    final String TAG = OTPActivity.this.toString();
    PinView pinView;
    Boolean camefromOnScreenActivity;

    SharedPreferences sharedPreferences;
    TextView contactno;
    long phone;
    Cache cache;
    Boolean camefromAR = false;
    Network network;
    RequestQueue requestQueue;
    String Server_url;
    SessionConfig sessionObj;
    String otp_SessionId, otp;
    ProgressDialog progress;
    TextWatcher textWatcher;
    boolean permissionGrated = false;
    TextView resend_count;
    CardView resend;
    CardView change_number;
    Thread t;

    //For forms details
    EditText newPassword;
    EditText newCnfPassword;
    EditText emailId;
    EditText referalCode;
    TextView letsBegin;
    String userStatus;
    private String Token = "";
    private String mSource = "";
    private VolleyHelper volleyHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        sessionObj = new SessionConfig(getApplicationContext());
        Token = sessionObj.getToken();
        mSource = sessionObj.getSource();

        pinView = findViewById(R.id.pinview);
        change_number = findViewById(R.id.change_number);

        contactno = findViewById(R.id.contact);
        resend_count = findViewById(R.id.resend_count);

        //To show details forms
        newPassword = findViewById(R.id.password_new_edit);
        newCnfPassword = findViewById(R.id.password_new_cnf_edit);
        referalCode = findViewById(R.id.referral_new_edit);
        emailId = findViewById(R.id.email_new_edit);
        letsBegin = findViewById(R.id.loginbutton);
        final Intent intent = getIntent();


        //user status is new or old comes from Intent
        userStatus = intent.getStringExtra("userStatus");
        volleyHelper = new VolleyHelper();
        Log.v("OTPActivity.java", "status: " + userStatus);

        phone = intent.getLongExtra("contact", 12345);
        progress = new ProgressDialog(getApplicationContext());
        progress.setMessage("Please wait...");


        camefromAR = intent.getBooleanExtra("camefromarearange", false);

        camefromOnScreenActivity = intent.getBooleanExtra("camefromOn", false);

        if (camefromAR || camefromOnScreenActivity) {
            change_number.setVisibility(View.VISIBLE);
        } else {
            change_number.setVisibility(View.GONE);
        }


        contactno.setText("+91 " + phone);
        resend = findViewById(R.id.resend);
        resend.setEnabled(false);
        resend.setBackgroundColor(getResources().getColor(R.color.colorText));

        checkAndRequestPermissions();
        textwatcher();
        resendcountFunction();

        if (camefromAR || camefromOnScreenActivity) {

            change_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(getApplicationContext(), ChangrNumberActivity.class);
                    intent1.putExtra("Oldnumber", phone);
                    intent1.putExtra("camefromsignup", true);
                    startActivity(intent1);
                    finish();
                }
            });
        }


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    resend_count.setVisibility(View.VISIBLE);
                    resend.setEnabled(false);
                    resend.setBackgroundColor(getResources().getColor(R.color.colorText));
                    resend.setEnabled(false);
                    resend.setBackgroundColor(getResources().getColor(R.color.colorText));
                    if (!isFinishing()) {
                        progress.show();
                    }
                } catch (Exception e) {

                }
                resendcountFunction();
                RequestOtp();
//                checkAndRequestPermissions();
            }
        });

        //by this letsBegin(LoginButton) View onClick() method user get signup on the api or server
//        letsBegin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("ValueCheck", "onClick: " + "phone = " + Long.toString(phone)
//                        + "\nemail = " + emailId.getText().toString()
//                        + "\npassword = " + newPassword.getText().toString()
//                        + "\ncnfpassword = " + newCnfPassword.getText().toString()
//                        + "\nrefereal = " + referalCode.getText().toString());
//                if (userStatus.equals("new")) {
//                    if (emailId.getText().toString().isEmpty() || newPassword.getText().toString().isEmpty() || newCnfPassword.getText().toString().isEmpty()) {
//                        showAlertDialog("Please Complete All Fields");
//                    } else {
//                        if (!newPassword.getText().toString().equals(newCnfPassword.getText().toString())) {
//                            showAlertDialog("Password and confirm password does not match");
//                        } else {
//                            startSignUpProcessVolley(Long.toString(phone), emailId.getText().toString(), newPassword.getText().toString(), referalCode.getText().toString());
//                        }
//                    }
//                }
//            }
//        });
    }


    //call from RegistrationStatus method if user status is not success response from server post request
    public void checkstatus() {

        String alurl = "https://api.gharpeshiksha.com"+"/Tutor/basicinfo";
        RequestQueue requestQueue = Volley.newRequestQueue(OTPActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, alurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.v(TAG, "onResponse: on slideactivity check status");
                            JSONObject jsonObject = new JSONObject(response);
                            String uploaddocument = jsonObject.getString("status");
                            Log.v("OTPActivity.java", "" + uploaddocument);
                            startActivity(new Intent(getApplicationContext(), Dashboard.class).putExtra("uploaddoc", uploaddocument).
                                    putExtra("sortDis", "firsttime").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        } catch (Exception e) {
                            startActivity(new Intent(getApplicationContext(), SignUpAndIn.class));
                            Toast.makeText(OTPActivity.this, "There was an issue, Please try again.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse Error: " + e.getMessage());
                            Log.v("OTPActivity.java", "" + e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       /* new android.app.BasicFeature.Builder(OTPActivity.this)

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
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


    int num = 0;
    int num2 = 30;
    CountDownTimer countDown;

    private void resendcountFunction() {
        num = 0;
        num2 = 30;
        countDown = new CountDownTimer(31000, 1000) {
            @Override
            public void onTick(long l) {
//                Log.v("OTPActivity.java", "sec: " + l);
                if (num == 30) {
                    resend.setEnabled(true);
                    resend.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    if (camefromAR || camefromOnScreenActivity) {
                        change_number.setEnabled(true);
                        //       change_number.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }
                    resend_count.setVisibility(View.INVISIBLE);
                } else {
                    resend_count.setText("Resend code in " + num2);
                }
                num++;
                num2--;

            }

            @Override
            public void onFinish() {
                countDown.cancel();
            }
        };

        countDown.start();
    }


    public void textwatcher() {

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (pinView.getText().toString().length() == 6) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    Log.d(TAG, "onTextChanged() returned: OTP PHONE " + pinView.getText().toString());
                    //passed user entered otp in string format in verfyOtp method
                    verifyOtp(pinView.getText().toString());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        pinView.addTextChangedListener(textWatcher);
    }


    public void start_request_loadingProcess() {

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading OTP...");
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
        otpTrack = true;
//        Log.v("OTPActivity.java", "requestOtp");
        RequestOtp();
    }

    private boolean otpTrack = true;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.e(TAG, "onRequestPermissionsResult: ");

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    Log.e(TAG, "onRequestPermissionsResult: REQUEST_ID_MULTIPLE_PERMISSIONS ");

                    if (grantResults[0] == -1) {
                        //if permission is not granted
                        Log.v("OTPActivity.java", "otp1");
                        //if otp track is true then send otp else not and after sending
                        //otp by API in requestOtp() method make it false in condition and true
                        //in method so start_loading_process() won't call two times.
                        if (otpTrack) {
                            Log.v("OTPActivity.java", otpTrack + "");
                            otpTrack = false;
                            start_request_loadingProcess();
                        }
                        permissionGrated = false;

                    } else {
                        for (int result : grantResults) {
                            Log.e(TAG, "onRequestPermissionsResult: result" + result);
                            if (result != PackageManager.PERMISSION_GRANTED) {
                                permissionGrated = false;
                                return;
                            }
                        }
                        //if permission granted
                        if (otpTrack) {
                            Log.v("OTPActivity.java", otpTrack + "");
                            otpTrack = false;
                            start_request_loadingProcess();
                        }
                        permissionGrated = true;
                    }
                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void checkAndRequestPermissions() {


        String permissions[] = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, POST_NOTIFICATIONS};
        } else {
            permissions = new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                Log.v("OTPActivity.java", "Otp2");
                start_request_loadingProcess();
            } else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_ID_MULTIPLE_PERMISSIONS);

            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_ID_MULTIPLE_PERMISSIONS);

        }

    }

    private ApiInterface apiInterface1 = RetrofitInstance. getRetrofitInstance();

    private void RequestOtp() {

        Log.d(TAG, "RequestOtp() returned: " + "OTP SENT");
        Log.d("OTPActivity.java", TAG);
        /**Retrofit library to make API call for one only. */
        apiInterface1.getOtp("" + phone).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull retrofit2.Response<String> response) {
                try {
                    if(response.body() != null) {
                        String otpSession = response.body().toString();
//                        Log.v("OTPActivity.java", "response: " + otpSession);
                        if (!isFinishing()) {
                            progress.dismiss();
                        }
                        otp_SessionId = otpSession;
                    }
                } catch (Exception e) {
                    Log.v("OTPActivity.java", e + "");
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                progress.dismiss();
                Log.e("OTPActivity.java", "CreateResponse: Error.Response" + t.getMessage());
            }
        });
//        Log.e(TAG, "CreateResponse:");
////        Creating cache of 20MB size
//        cache = new DiskBasedCache(getCacheDir(), 1024 * 200);
//        network = new BasicNetwork(new HurlStack());
//        //requestQueue to store and make all the request and maintain cache and maintain network call requests in background
//        requestQueue = new RequestQueue(cache, network);
//        //RequestQueue object start in the background
//        requestQueue.start();
////        RequestQueue que = Volley.newRequestQueue(OTPActivity.this);
//        //end point url on which we post or get data in Json, String, JsonArray, Bitmap
//        Server_url = "https://api.gharpeshiksha.com" + "/TutorLogin/sendotp";
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Log.v("OTPActivity.java", "response: " + response);
////                            Log.e(TAG, "CreateResponse: onResponse" + response);
//                            if(!isFinishing()) {
//                                progress.dismiss();
//                            }
//                            otp_SessionId = response;//user otp id from api to verify otp user enter in verifyOtp method
//                            //stop RequestQueue class object that is managing cache and network request in background
//                            requestQueue.stop();
//                        } catch (Exception e) {
//                            Log.v("OTPActivity.java", e + "");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                       /* new BasicFeature.Builder(OTPActivity.this)
//
//                                .setMessage("There might be an internet issue, please try again after some time.")
//                                .setPositiveButton("Okay",null)
//                                .show();*/
//
//                        progress.dismiss();
//                        Log.e("OTPActivity.java", "CreateResponse: Error.Response" + error.getMessage());
//                    }
//                }
//        ) {
//            //parameters for post request when check response from postman
//            @Override
//            protected Map<String, String> getParams() {
//                Log.e(TAG, "CreateResponse:  getParams()");
//                Map<String, String> params = new HashMap<>();
//                params.put("phone", "" + phone);
//                return params;
//            }
//        };
//        requestQueue.add(postRequest);
////        que.add(postRequest);

    }


    //Called from SaveVerified() method with user phone number that is comes from intent.getExtra() method means from extras of intent request
    //for OTPActivity.java changes made in RegistrationStatus on 12/5/2022
    public void RegistrationStatus(final long phone) {

        Log.e(TAG, "RegistrationStatus:");

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();
        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/checkactivity_new";
        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   progress.dismiss();

                        Log.e(TAG, "RegistrationStatus: onResponse" + response);
                        try {
                            JSONObject jsResponse = new JSONObject(response);

                            String Status = jsResponse.getString("Status");

                            requestQueue.stop();
                            if (Status.equals("Success")) {
                                String ActivityToFill = jsResponse.getString("ActivityToFill");

                                switch (ActivityToFill) {

                                    case "Classes":
                                        startActivity(new Intent(getApplicationContext(), ClassesActivity.class)
                                                .putExtra("contact", phone)
                                                .putExtra("verifyotp", true)
                                                .putExtra("userStatus", userStatus));
                                        finish();
                                        break;

                                    case "Qualification":
                                        startActivity(new Intent(getApplicationContext(), QualificationActivity.class)
                                                .putExtra("verifyotp", true)
                                                .putExtra("userStatus", userStatus));
                                        finish();
                                        break;
                                    case "PersonalInfo":
                                        startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class)
                                                .putExtra("verifyotp", true)
                                                .putExtra("userStatus", userStatus));
                                        finish();
                                        break;

                                    case "AreaRangeSelection":
                                        startActivity(new Intent(getApplicationContext(), AreaRangeSelection.class)
                                                .putExtra("status", true)
                                                .putExtra("camegt", true)
                                                .putExtra("verifyotp", true)
                                                .putExtra("userStatus", userStatus));
                                        finish();
                                        break;

                                    case "UserInfoActivity":
                                        startActivity(new Intent(getApplicationContext(), UserInfoActivity.class)
                                                .putExtra("status", true)
                                                .putExtra("camegt", true)
                                                .putExtra("verifyotp", true)
                                                .putExtra("userStatus", userStatus));
                                        finish();
                                        break;

                                    default:
                                        Toast.makeText(OTPActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(OTPActivity.this, SignInActivity.class));
                                        break;

                                }
                            } else {
                                if (!isFinishing()) {
                                    progress.dismiss();
                                }
                                sessionObj.setPhone(phone);
                                sessionObj.LoginStatusWrite(true);
                                checkstatus();//checking status of user, if user not exist toasting msg "there was an issue, Please try again"
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*new BasicFeature.Builder(OTPActivity.this)

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                        progress.dismiss();
                        Log.e(TAG, "RegistrationStatus: Error.Response" + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + phone);
                return params;
            }
        };
        requestQueue.add(postRequest);

    }


    //send otp from requestOtp() method
    //verify OTP of user entered in pinview and from api verify otp called from textwatcher() onTextChanged() Override method of TextWatcher interface
    private void verifyOtp(final String otp) {
        Log.e(TAG, "CreateResponse:");
        if (!progress.isShowing()) {
            try {
                progress.show();
            } catch (Exception e) {
                Log.v("OTPActivity.java", e + "");
            }
        }
        requestQueue = Volley.newRequestQueue(OTPActivity.this);
        requestQueue.start();
        Server_url = "https://api.gharpeshiksha.com" + "/TutorLogin/verifyotp";

        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e(TAG, "CreateResponse: onResponse " + response);

                        if (response.equals("Matched")) {
//                            SaveVerified(Long.toString(phone));
//                            start_request_loadingProcess();
                            try {
                                if (userStatus.equals("new")) {
//                                displayDetailsForms();
                                    //SignUp user with dummy data on server with volley http library because without registered user server
                                    //returns no subjects Json object in ClassesActivity.java ExpandableListView item data source.
//                                startSignUpProcessVolley(String.valueOf(phone), "", "", "");
                                    signUpNewUserOnVolley(String.valueOf(phone), Token);
//                                startActivity(new Intent(getApplicationContext(), ClassesActivity.class).putExtra("verifyotp", true).putExtra("userStatus", userStatus));
//                                finish();
                                    progress.dismiss();
                                } else if (userStatus.equals("old")) {
                                    SaveVerified(String.valueOf(phone));
                                    progress.dismiss();
//                                    Log.v("OTPActivity.java", "otp2");
//                                    start_request_loadingProcess();
                                }
                            } catch (Exception e) {
                                progress.dismiss();
                                Log.v("OTPActivity.java", e + "");
                            }
                        } else {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Wrong OTP, please enter correct OTP", Toast.LENGTH_SHORT).show();
                        }
                        requestQueue.stop();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       /* new BasicFeature.Builder(OTPActivity.this)

                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                        Log.e(TAG, "CreateResponse: Error.Response" + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Log.e(TAG, "CreateResponse:  getParams()");
                Map<String, String> params = new HashMap<>();
                params.put("otp", otp);
                params.put("session_id", otp_SessionId);
                return params;
            }
        };
        requestQueue.add(postRequest);

    }

    //save email on server through below api
    private void signUpNewUserOnVolley(String phone, String token) {
        String accessToken = Utility.getRandomStr();
        //RequestQueue is an class of Volley HTTP Library and it make network request in background and manage the cache and memory management of
        //requests
        //server url on which the network request is post
        String serverUrl = "https://api.gharpeshiksha.com" + "/Tutor/SignUpNew";
        StringRequest signUpStringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Do work when response comes
                try {
                    JSONObject jsResponse = new JSONObject(response);
                    if (jsResponse.get("Status").equals("Success")) {
                        sessionObj.setAccessToken(accessToken);
                        startActivity(new Intent(getApplicationContext(), ClassesActivity.class).putExtra("verifyotp", true).putExtra("userStatus", userStatus));
                        finish();

                        //remove user email from SharedPreferences after user OTPVerified and API executed and save data successfully.
//                        sessionObj.removeEmailId();
//                        showAlertDialog("adfas");
                    } else if (jsResponse.get("Status").equals("Error") && jsResponse.get("Message").equals("Invalid Contact")) {
                        showAlertDialog("Invalid Number");
                    } else {
                        showAlertDialog("You already have an account. Please Sign In");
                        RegistrationStatus(Long.parseLong(phone.trim()));
                        //remove user email from SharedPreferences after user OTPVerified and API executed and save data successfully.
//                        sessionObj.removeEmailId();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OTPActivity.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                //HashMap Data structure object contains objects in key/value pair which is params for server reqest
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone + "");
                params.put("firebaseToken", token + "");
                Log.v("OTPActivity.java", sessionObj.getEmailId() + "a");
                params.put("email", sessionObj.getEmailId());
                params.put("accessToken", accessToken);
                return params;
            }
        };
        requestQueue.add(signUpStringRequest);
    }

    //  /Tutor/setPassAndReferral
    @Override
    public void onResume() {

        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progress.isShowing()) {
            progress.dismiss();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v("OTPActivity.java", "br");
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

                Log.e(TAG, "onReceive: message " + message.substring(11, 18));
                otp = message.substring(11, 18);
                pinView.setText(otp);
                // To dismiss the dialog
                progress.dismiss();
//                try {
//                    Log.v("OTPActivity.java", otp + "sadfasdf");
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    Log.v("OTPActivity.java", e + "");
//                    e.printStackTrace();
//                }
//                if (!Thread.currentThread().isInterrupted()) {
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        Log.v("OTPActivity.java", e + "");
//                        e.printStackTrace();
//                    }
//                }
                progress.dismiss();

            }
        }


    };


    // FOR SIGN UP DETAILS

    String verifyurl = "https://api.gharpeshiksha.com" + "/TutorLogin/verifiedNew";


    //call from verifyOtp() method with user entered phone number
    public void SaveVerified(final String phone) {
        String accessTok = Utility.getRandomStr();

        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.gharpeshiksha.tutorapp", Context.MODE_PRIVATE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest postRequest = new StringRequest(Request.Method.POST, verifyurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //user session configuration for all the user details in SharedPreferences Context.MODE_PRIVATE mode file
                    SessionConfig sessionConfig = new SessionConfig(getApplicationContext());
                    sessionConfig.setPhone(Long.parseLong(phone));//set phone number of Long type in integer
                    //SharedPreferences class file object name with package name and private add key/value pair to store user save verified
                    //means store value with success response
                    sharedPreferences.edit().putString("VerifiedContact", new JSONObject(response).getString("status")).apply();
                    sessionObj.setAccessToken(accessTok);
                    Log.v("OTPActivity.javaa", response + "");
                    RegistrationStatus(Long.parseLong(phone));
                    Log.d("tag", "onResponse: persongoogle " + response);
                } catch(Exception e) {
                    Log.d("tag", "onFailure: " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*new BasicFeature.Builder(OTPActivity.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Log.d("tag", "getParams: getParams()");
                Map<String, String> params = new HashMap<>();
                params.put("accessToken", accessTok);
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

    //lets begin form on OTPActivity.java
//    public void displayDetailsForms() {
//        RelativeLayout otpLayout = findViewById(R.id.otpLayout);
//        otpLayout.setVisibility(View.GONE);
//        LinearLayout forms = findViewById(R.id.newuserLayout);
//        forms.setVisibility(View.VISIBLE);
//        letsBegin.setVisibility(View.VISIBLE);
//    }

    public void showAlertDialog(String title) {
        if (!isFinishing()) {
            new AlertDialog.Builder(OTPActivity.this, R.style.AppCompatAlertDialogStyle)
                    .setMessage(title)
                    .setPositiveButton("Okay", null).show();
        }
    }

//    private void startSignUpProcessVolley(final String phone, final String email, final String password, final String referral) {
//        Log.e("NewUser", "startSignUpProcessVolley: " + "phone = " + phone
//                + "\nemail = " + email
//                + "\npassword = " + password
//                + "\nrefereal = " + referral);
//
//        String Server_url = "https://api.gharpeshiksha.com" + "/Tutor/SignUp";
//        Map<String, String> params = new HashMap<>();
//
//        params.put("phone", phone.trim());
//        params.put("password", password);
//        params.put("email", email);
//        params.put("referralId", referral);
//        params.put("firebaseToken", Token);
//        params.put("source", mSource);
//
//        volleyHelper.VolleyPostRequest(OTPActivity.this, Server_url, params, new VolleyResponse() {
//            @Override
//            public void onSucess(String response) {
//                try {
//                    JSONObject jsResponse = new JSONObject(response);
//                    if (jsResponse.get("Status").equals("Success")) {
//                        Log.v("OTPActivity.java", "user sign up on server temp.");
//                        Toast.makeText(OTPActivity.this, "temp sign up", Toast.LENGTH_SHORT).show();
////                        SharedPreferences sharedPreferences = getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);
////                        sharedPreferences.edit().clear();
////                        sessionObj.setPhone(Long.parseLong(phone.trim()));
////                        startActivity(new Intent(getApplicationContext(), ClassesActivity.class).putExtra("verifyotp", true));
////                        finish();
//                    } else if (jsResponse.get("Status").equals("Error") && jsResponse.get("Message").equals("Invalid Contact")) {
////                        showAlertDialog("Invalid Number");
//                    } else {
////                        showAlertDialog("You already have an account. Please Sign In");
////                        RegistrationStatus(Long.parseLong(phone.trim()));
//                    }
//                } catch (JSONException e) {
////                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(String error) {
//
//            }
//        });
//
//
//    }

}
