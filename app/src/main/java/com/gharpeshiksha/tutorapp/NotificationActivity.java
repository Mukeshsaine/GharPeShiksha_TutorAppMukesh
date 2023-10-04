package com.gharpeshiksha.tutorapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NotificationActivity extends AppCompatActivity {

    final String TAG = NotificationActivity.this.getClass().toString();
    Toolbar toolbar;
    SwitchCompat sms_notification, classes_info, general_notification;
    RequestQueue requestQueue;
    private ProgressDialog progress;
    TextView notification, password;
    CardView NotificationCardView, PasswordCardView;
    Button changePassword;
    AppCompatEditText currentPassword, newPassword, confirmPassword;
    String CurrentPass, changePasswordURL, NewPass;
    private SessionConfig sessionConfig;
    private long phone;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        toolbar = findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(NotificationActivity.this);
        progressDialog.setMessage("Please Wait...");
        changePasswordURL = "https://api.gharpeshiksha.com" + "/TutorLogin/change_password";
        sessionConfig = new SessionConfig(NotificationActivity.this);
        phone = sessionConfig.getPhone();
        sms_notification = findViewById(R.id.sms_notification);
        changePassword = findViewById(R.id.ChangePasswordButton);
        currentPassword = findViewById(R.id.CurrentPassword);
        newPassword = findViewById(R.id.NewPassword);
        confirmPassword = findViewById(R.id.ConfirmPassword);
        classes_info = findViewById(R.id.classes_info);
        general_notification = findViewById(R.id.general_notification);
        notification = findViewById(R.id.notification_TextView);
        password = findViewById(R.id.password_TextView);
        PasswordCardView = findViewById(R.id.change_password_cardview);
        NotificationCardView = findViewById(R.id.notification_settings_cardview);
        NotificationCardView.setVisibility(View.VISIBLE);
        PasswordCardView.setVisibility(View.GONE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_nevigation_icon));
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        notificationListeners();
        getNotificationStatus();

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotificationCardView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(NotificationCardView);
                    NotificationCardView.setVisibility(View.GONE);
                } else {
                    TransitionManager.beginDelayedTransition(NotificationCardView);
                    NotificationCardView.setVisibility(View.VISIBLE);
                }
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PasswordCardView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(PasswordCardView);
                    PasswordCardView.setVisibility(View.GONE);
                } else {
                    TransitionManager.beginDelayedTransition(PasswordCardView);
                    PasswordCardView.setVisibility(View.VISIBLE);
                }
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(currentPassword.getText()) || TextUtils.isEmpty(newPassword.getText()) || TextUtils.isEmpty(confirmPassword.getText())) {
                    if (TextUtils.isEmpty(currentPassword.getText())) {
                        Toast.makeText(NotificationActivity.this, "Please enter your current password", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(newPassword.getText())) {
                        Toast.makeText(NotificationActivity.this, "Please enter a new password", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(confirmPassword.getText())) {
                        Toast.makeText(NotificationActivity.this, "Please confirm your new password", Toast.LENGTH_LONG).show();
                    }
                } else {  // body for all fields entered
                    progressDialog.show();
                    if (TextUtils.equals(newPassword.getText(), confirmPassword.getText())) {
                        // Volley request

                        CurrentPass = currentPassword.getText().toString();
                        NewPass = newPassword.getText().toString();


                        RequestQueue changepasswordQ = Volley.newRequestQueue(NotificationActivity.this);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, changePasswordURL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String result = jsonObject.getString("Result");
                                    switch (result) {
                                        case "successful":
                                            Toast.makeText(NotificationActivity.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            break;
                                        case "error":
                                            Toast.makeText(NotificationActivity.this, "Wrong password entered", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            break;
                                        default:
                                            Toast.makeText(NotificationActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            break;

                                    }
                                } catch (Exception e) {
                                    Toast.makeText(NotificationActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                /*new android.app.BasicFeature.Builder(NotificationActivity.this)

                                        .setMessage("There might be an internet issue, please try again after some time.")
                                        .setPositiveButton("Okay",null)
                                        .show();*/
                                progress.dismiss();
                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() {
                                Log.d(TAG, "getParams: getParams()");
                                Map<String, String> params = new HashMap<>();
                                params.put("phone", "" + phone);
                                params.put("old_password", "" + CurrentPass);
                                params.put("new_password", "" + NewPass);
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

                        changepasswordQ.add(stringRequest);

                    } else {
                        Toast.makeText(NotificationActivity.this, "Please enter same password for new and confirm password field", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void start_request_loadingProcess() {

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();


    }

    private void notificationListeners() {

        sms_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    backgroundTask("emailsms", "" + isChecked);
                } else {
                    backgroundTask("emailsms", "" + isChecked);
                }
            }
        });
        classes_info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    backgroundTask("app_classes", "" + isChecked);
                } else {
                    backgroundTask("app_classes", "" + isChecked);
                }
            }
        });
        general_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    backgroundTask("app_messages", "" + isChecked);
                } else {
                    backgroundTask("app_messages", "" + isChecked);
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void backgroundTask(final String key, final String value) {

        Runnable runnable = new Runnable() {

            public void run() {
                Log.e(TAG, "onClick: " + key + " value" + value + "  " + Thread.currentThread());
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://api.gharpeshiksha.com" + "/Settings/setnotification";
                requestQueue.start();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: " + response);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*new android.app.BasicFeature.Builder(NotificationActivity.this)
                                .setMessage("There might be an internet issue, please try again after some time.")
                                .setPositiveButton("Okay",null)
                                .show();*/
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> param = new HashMap<>();
                        param.put("field", key);
                        param.put("value", value);
                        param.put("phone", "" + phone);
                        return param;
                    }
                };
                requestQueue.add(request);
            }
        };
        ExecutorService executor = Executors.newCachedThreadPool(); // if you need to spawn more than one thread
        executor.submit(runnable);

        //new Thread(runnable).start();

    }

    public void getNotificationStatus() {
        start_request_loadingProcess();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.gharpeshiksha.com" + "/Settings/getnotification";
        requestQueue.start();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "getNotificationStatus:onResponse: " + response);
                requestQueue.stop();
                progress.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    Log.e(TAG, "onResponse:emailsms " + object.getString("emailsms"));
                    Log.e(TAG, "onResponse: app_classes" + object.getString("app_classes"));
                    Log.e(TAG, "onResponse: app_messages" + object.getString("app_messages"));
                    sms_notification.setChecked(Boolean.parseBoolean(object.getString("emailsms")));
                    classes_info.setChecked(Boolean.parseBoolean(object.getString("app_classes")));
                    general_notification.setChecked(Boolean.parseBoolean(object.getString("app_messages")));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();

                /*new android.app.BasicFeature.Builder(NotificationActivity.this)
                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/
                Log.e(TAG, "getNotificationStatus:onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("phone", "" + phone);
                return param;
            }
        };
        requestQueue.add(request);


    }

}
