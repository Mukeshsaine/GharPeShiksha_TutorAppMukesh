package com.gharpeshiksha.tutorapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.BuildConfig;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.localdb.Contract;
import com.gharpeshiksha.tutorapp.localdb.LocalSQLiteDbHandler;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class Splash extends AppCompatActivity {

    private static final String TAG = "SplashActivity.java";
    private SessionConfig sessionConfig;
    private String uploaddocument;
    private Long phone;
    private int flag = 0;

    private Boolean sart = false;
    private Button retry;
    private Dialog dialog;
    private ProgressBar progressBar;
    private String appupdateType = "no_update";
    private String verified;
    int MY_REQUEST_CODE = 6000;
    private String mAPIKey;
    private LocalSQLiteDbHandler dbHandler;
    AppUpdateManager appUpdateManager;
    com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Log.d(TAG, "onCreate: DataChori " + System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));

        try {
            Window window = Splash.this.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(Splash.this, R.color.white));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        if(isNetworkAvailable()) {
            inits();
        } else {
            noInternetDialog();
        }
    }

    private void inits() {
        //getGoogleMapApiKey() method get Google Map API key from the server by making POST Request on url and save response key-value in variable
        getGoogleMapApiKey();
        //final ProgressBar progressBar = new ProgressBar(Splash.this);
        dbHandler = new LocalSQLiteDbHandler(Splash.this);
        progressBar = findViewById(R.id.progressbarsplash);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);
        verified = sharedPreferences.getString("VerifiedContact", sharedPreferences.getString("VerifiedContact", "failed"));

        sessionConfig = new SessionConfig(Splash.this);

        sessionConfig.setTempAddress("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // Initialize Google Play Availability API
            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
            int status = googleApiAvailability.isGooglePlayServicesAvailable(Splash.this);
            // Show Error if not available

            Log.d(TAG, "hcbcdjdhcdhconResponse 1: " + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP));


            if (status != ConnectionResult.SUCCESS) {
                Log.d(TAG, "hcbcdjdhcdhconResponse 2: " + ConnectionResult.SUCCESS);
                CheckVersionAndAfterCheckUpdate();
            } else {
                checkforupdate();
            }
        } else {
            CheckVersionAndAfterCheckUpdate();
        }


        try {
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(Splash.this, new com.google.android.gms.tasks.OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            Uri deepLink = null;

                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                                Log.e(TAG, "linkReceived " + deepLink);

                                if (deepLink.getQueryParameter("from") != null) {
                                    Log.e(TAG, "getQueryParamenter :  " + deepLink.getQueryParameter("from"));
                                    new SessionConfig(Splash.this).setSource(deepLink.getQueryParameter("from"));
                                }
                            }
                        }
                    }).addOnFailureListener(Splash.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "getDynamicLink:onFailure", e);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "DynamicLink: " + e.getMessage());
        }

        sessionConfig.setShowAboutDialog(true);
    }

    private void noInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
        View view = LayoutInflater.from(this).inflate(R.layout.no_network_dialog, null);
        builder.setView(view);
        AlertDialog al = builder.create();
        view.findViewById(R.id.retry).setOnClickListener(v -> {
           if(isNetworkAvailable()) {
               inits();
               al.dismiss();
           } else {
               noNetworkDialog();
           }
        });
        al.show();
    }

    private void CheckVersionAndAfterCheckUpdate() {
        VersionChecker versionChecker = new VersionChecker();

        try {
            String latestVersion = versionChecker.execute().get();
            Log.e("Splash.java", "onCreate: latestversion  " + latestVersion + "\n App version  " + BuildConfig.VERSION_NAME);
            if (!latestVersion.equals(BuildConfig.VERSION_NAME)) {
                sessionConfig.setupdateAvailable(true);
            } else {
                sessionConfig.setupdateAvailable(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        afterupadtecheck();
    }


    public class VersionChecker extends AsyncTask<String, String, String> {

        private String newVersion;

        @Override
        protected String doInBackground(String... params) {

            try {
                try {
                    newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.gharpeshiksha.tutorapp&hl=en")
                            .timeout(45000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            .select(".IxB2fe .hAyfc:nth-child(4) .htlgb span")
                            .get(0)
                            .ownText();
                } catch (IndexOutOfBoundsException e) {
                    Log.v("Splash.java", e + "");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return newVersion;
        }
    }


    public void afterupadtecheck() {

        if (verified.matches("Success")) {

            try {
                phone = sessionConfig.getPhone();
                Log.d(TAG, "onCreate: phone splash " + phone);
                if (isNetworkAvailable()) {
                    checkstatus();
                    // progressBar.setVisibility(View.GONE);
                } else {
                    noNetworkDialog();
                }


            } catch (Exception e) {

                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Intent intent = new Intent(Splash.this, SignUpAndIn.class);
                    startActivity(intent);
                    // progressBar.setVisibility(View.GONE);
                    finish();
                }, 200);

            }

        } else {

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent intent = new Intent(Splash.this, SignUpAndIn.class);
                startActivity(intent);
                // progressBar.setVisibility(View.GONE);
                finish();
            }, 2000);
        }


    }


    protected void noNetworkDialog() {

        try {

            dialog = new Dialog(getApplicationContext());

            dialog.setContentView(R.layout.no_network_dialog);
            retry = dialog.findViewById(R.id.retry);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.show();

            retry.setOnClickListener(v -> {
                dialog.dismiss();
                if (isNetworkAvailable()) {
                    dialog.dismiss();
                    if (flag == 0) {
                        checkstatus();
                    }


                } else {
                    dialog.dismiss();
                    noNetworkDialog();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "noNetworkDialog: " + e);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void checkstatus() {
        Cursor cursor = dbHandler.getBasicInfo(dbHandler.getReadableDatabase());
        Log.d(TAG, "checkstatus: size: " + cursor.getCount());
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            long currTime = System.currentTimeMillis() / 1000;
            long futureSoftTL = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(Contract.BasicInfo.SOFT_TL))) / 1000;
            futureSoftTL += 86400;
            Log.d(TAG, "checkstatus: " + futureSoftTL + ", " + currTime);
            if(futureSoftTL > currTime) {
                //valid
                try {
                    sart = true;
                    uploaddocument = cursor.getString(cursor.getColumnIndexOrThrow(Contract.BasicInfo.STATUS));
                    Intent intent = new Intent(Splash.this, SignUpAndIn.class);
                    intent.putExtra("upload", uploaddocument);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "onResponse: on slideactivity check status " + uploaddocument);
                } catch (Exception e) {
                    Log.d(TAG, "onResponse Error: " + e.getMessage());
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(Splash.this, SignUpAndIn.class);
                        startActivity(intent);
                        //progressBar.setVisibility(View.GONE);
                        finish();
                    }, 200);
                }
                return;
            } else {

            }
        }
        Log.d(TAG, "checkstatus: not valid");
        String alurl = "https://api.gharpeshiksha.com" + "/Tutor/basicinfo";
        RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, alurl,
                response -> {
                    try {
                        sart = true;
                        JSONObject jsonObject = new JSONObject(response);
                        dbHandler.deleteBasicInfo(dbHandler.getWritableDatabase());
                        dbHandler.addBasicInfo(dbHandler.getWritableDatabase(), jsonObject);
                        uploaddocument = jsonObject.getString("status");
                        Intent intent = new Intent(Splash.this, SignUpAndIn.class);
                        intent.putExtra("upload", uploaddocument);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                        Log.d(TAG, "onResponse: on slideactivity check status " + uploaddocument);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse Error: " + e.getMessage());
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            Intent intent = new Intent(Splash.this, SignUpAndIn.class);
                            startActivity(intent);
                            //progressBar.setVisibility(View.GONE);
                            finish();
                        }, 200);
                    }
                },
                error -> {

                    Log.d(TAG, "onErrorResponse: " + error);
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(Splash.this, SignUpAndIn.class);
                        startActivity(intent);
                        //progressBar.setVisibility(View.GONE);
                        finish();
                    }, 200);
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

    public void checkforupdate() {
        Log.d(TAG, "checkforupdate: " + sessionConfig.getToken());
        String UPDATEAPPAPI = "https://api.gharpeshiksha.com" + "/Tutor/appupdateNew";
        RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, UPDATEAPPAPI,
                response -> {

                    try {
                        JSONObject json = new JSONObject(response);
                        appupdateType = json.getString("update");
                        if(json.has("loginAllowed")) {
                            boolean isLoginAllowed = json.getBoolean("loginAllowed");
                            if(!isLoginAllowed) {
                                sessionConfig.clearSessionConfig();
                                Intent i = new Intent(Splash.this, SignUpAndIn.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                                return;
                            }
                        }
                        Log.d(TAG, "hcbcdjdhcdhconResponse Error: " + appupdateType);

                        if (appupdateType.matches("no_update")) {
                            afterupadtecheck();
                        } else if (appupdateType.matches("flexible")) {
                            FlexibleUpdate();
                        } else if (appupdateType.matches("critical")) {
                            ImmediateUpdate();
                        }


                    } catch (Exception e) {
                        Log.d(TAG, "onResponse Error: " + e.getMessage());
                        afterupadtecheck();
                    }

                },
                error -> {

                    try {
                        //noNetworkDialog();
                        new android.app.AlertDialog.Builder(Splash.this)
                                .setMessage("There is a server error, please try again after sometime.")
                                .setPositiveButton("Okay", null)
                                .show();
                    } catch (Exception e) {
                        Log.v("Splash.java", e + "aaa");
                    }

                    Log.d(TAG, "onErrorResponse: " + error);
                    afterupadtecheck();
                }
        ) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("phone", sessionConfig.getPhone() + "");
                map.put("accessToken", sessionConfig.getAccessToken());
                return map;
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

    private void FlexibleUpdate() {
        // IN APP UPDATE

        // Creates instance of the manager.

        appUpdateManager = AppUpdateManagerFactory.create(Splash.this);

        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(FLEXIBLE)) {
                // Request the update.

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            FLEXIBLE,
                            // The current activity making the update request.
                            Splash.this,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);

                } catch (IntentSender.SendIntentException e) {
                    /*Log.e("Update App", e.getMessage());*/

                }
            } else {

                afterupadtecheck();
            }

        });

        appUpdateInfoTask.addOnFailureListener(e -> {

            afterupadtecheck();
        });


    }

    private void ImmediateUpdate() {


        // IN APP UPDATE

        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(Splash.this);

        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {


                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    // Request the update.
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(Splash.this, "Critical Update Available \nFetching...", Toast.LENGTH_LONG).show();

                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                appUpdateInfo,
                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                IMMEDIATE,
                                // The current activity making the update request.
                                Splash.this,
                                // Include a request code to later monitor this update request.
                                MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("Update App", e.getMessage());
                    }
                } else {
                    afterupadtecheck();
                }

            }
        });
        appUpdateInfoTask.addOnFailureListener(e -> {

            afterupadtecheck();
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //progressBar.setVisibility(View.GONE);
        super.onActivityResult(requestCode, resultCode, data);
        if (appupdateType.equals("critical")) {

            if (requestCode == MY_REQUEST_CODE) {

                if (resultCode != RESULT_OK) {
                    Log.e("onActivityResult", "update failed");

                    startActivity(new Intent(getApplicationContext(), Splash.class));
                    finish();
                    //afterupadtecheck();
                    // If the update is cancelled or fails,
                    // you can request to start the update again.

                } else if (resultCode == ActivityResult.RESULT_IN_APP_UPDATE_FAILED) {

                    startActivity(new Intent(getApplicationContext(), Splash.class));
                    finish();
                    //afterupadtecheck();

                    Log.e("OnActivityResult", "update Failed due to server error");
                } else if (resultCode == RESULT_OK) {
                    Toast.makeText(Splash.this, "Updating...", Toast.LENGTH_LONG).show();
                }
            }

        } else if (appupdateType.equals("flexible")) {

            if (requestCode == MY_REQUEST_CODE) {

                if (resultCode != RESULT_OK) {
                    Log.e("onActivityResult", "update cancelled");
                    afterupadtecheck();

                } else if (resultCode == ActivityResult.RESULT_IN_APP_UPDATE_FAILED) {

                    afterupadtecheck();

                    Log.e("OnActivityResult", "update Failed due to server error");
                } else if (resultCode == RESULT_OK) {

                    afterupadtecheck();
                    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
                        @Override
                        public void onStateUpdate(InstallState installState) {
                            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                                popupSnackbarForCompleteUpdate();
                                appUpdateManager.completeUpdate();
                            }

                            if (installState.installStatus() == InstallStatus.DOWNLOADING) {
                                afterupadtecheck();
                            }

                        }
                    };
                    appUpdateManager.registerListener(installStateUpdatedListener);
                }
            }
        }
    }

    public void popupSnackbarForCompleteUpdate() {
        Toast.makeText(getApplicationContext(), "Update Downloaded \nPlease Restart App.", Toast.LENGTH_LONG).show();
    }

    //Making network request to fetch the Google Map API Key using Volley Library POST type request.
    public void getGoogleMapApiKey() {
        //RequestQueue class object is manage to make request on network and manage processing of response in cache
        //It run request in background and return response when it completely executed
        RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);
        String url = "https://api.gharpeshiksha.com" + "/Tutor/common_config";
        StringRequest mapApiRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject data = new JSONObject(response);
                    mAPIKey = data.getString("map_key");
//                    Log.v("Splash.java", mAPIKey + "");
                    sessionConfig.setAPIKey(mAPIKey);
                } catch (Exception e) {
                    Log.v("Splash.java", e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Splash.java", "" + error.getMessage());
                sessionConfig.setAPIKey("AIzaSyC6_pWGCV9LapjdF0tTPFHPTRdTETOJqGY");
            }
        });

        requestQueue.add(mapApiRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appupdateType.equals("critical")) {
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {

                    if ((appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS)) {
                        // If an in-app update is already running, resume the update.
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    IMMEDIATE,
                                    Splash.this,
                                    MY_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("error on resume", e.getMessage());
                        }

                    }
                    if ((appUpdateInfo.updateAvailability() == InstallStatus.DOWNLOADED)) {
                        popupSnackbarForCompleteUpdate();
                        appUpdateManager.completeUpdate();
                    }
                }
            });
        } else if (appupdateType.equals("flexible")) {

            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if ((appUpdateInfo.updateAvailability() == InstallStatus.DOWNLOADED)) {
                        popupSnackbarForCompleteUpdate();
                        appUpdateManager.completeUpdate();
                    }
                }
            });

        }
    }

}

/**
 * String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
 * <p>
 * String randomString = "";
 * <p>
 * int length = 10;
 * <p>
 * Random rand = new Random();
 * char[] text = new char[length];
 * <p>
 * <p>
 * for(int i = 0 ; i<length  ;i++){
 * <p>
 * text[i] = characters.charAt(rand.nextInt(characters.length()));
 * }
 * <p>
 * for(int i =0 ;i< text.length;i++){
 * randomString += text[i];
 * }
 * <p>
 * Log.d(TAG, "onCreate: random String "+sessionConfig.getPhone() +randomString);
 **/