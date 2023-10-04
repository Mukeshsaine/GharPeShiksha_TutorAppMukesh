package com.gharpeshiksha.tutorapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Person;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.POST_NOTIFICATIONS;

public class PersonalInfoActivity extends AppCompatActivity {

    Button next;
    Boolean forwardedPersonalActivity = true;

    //    TextView forwardpersonalactivity;
    TextView dob;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    Toolbar toolbar;
    EditText full_name, alternate_contact;
    RadioGroup gender;
    RadioButton male_female;

    private AutocompleteSupportFragment autocompleteFragment_user_current_add;
    PlacesClient placesClient;
    final String TAG = PersonalInfoActivity.this.toString();
    private boolean doubleBackToExitPressedOnce = false;
    SessionConfig sessionConfig;
    private String mApiKey = "AIzaSyC6_pWGCV9LapjdF0tTPFHPTRdTETOJqGY";

    Network network;
    Cache cache;
    RequestQueue requestQueue;
    private String Server_url;
    double latitude, longitude;
    long phone;
    LatLng my_location;
    private static final String FINE_LOCATION = ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String READ_SMS = Manifest.permission.READ_SMS;
    private static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    private static final int PERMISSION_REQUEST_CODE = 1234;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002; //0x1
    private Boolean locationGranted = false;


    String address = "";
    LatLng latLng;
    private ProgressDialog progress;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Dialog dialog;
    private Button retry;
    private int flag;
    String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        sessionConfig = new SessionConfig(getApplicationContext());
        phone = sessionConfig.getPhone();
        toolbar = findViewById(R.id.toolbar);
        full_name = findViewById(R.id.full_name);
        gender = findViewById(R.id.gender);
        alternate_contact = findViewById(R.id.alternate_contact);
        dob = findViewById(R.id.dob);
        next = findViewById(R.id.next);
        //Api key from sharedPreferences
        mApiKey = sessionConfig.getAPIKey();

        final Intent intent = getIntent();


        //user status is new or old comes from Intent
        userStatus = intent.getStringExtra("userStatus");

        editSearchLocationInitilization();

        if (isNetworkAvailable()) {

            if (isServicesOK()) {

                Log.d("okokok", "fddfdf");
                getLocationPermission();
            }
        } else {

            noNetworkDialog();
        }


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    Log.e(TAG, "onLocationResult: " + location.getLatitude());
                    // Update UI with location data
                    // ...
                }
            }

            ;
        };

        editSearchLocationListener();

        toolbar.setTitle("GharPeShiksha");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_nevigation_icon));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        setSupportActionBar(toolbar);

        listeners();

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isNetworkAvailable()) {

            noNetworkDialog();
        }
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

                    if (isServicesOK() && !locationGranted) {
                        getLocationPermission();
                    }

                } else {
                    dialog.dismiss();
                    noNetworkDialog();
                }
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void buildAlertMessageNoGps() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {

        });

        task.addOnFailureListener(this, e -> {

            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(PersonalInfoActivity.this,
                            PERMISSIONS_REQUEST_ENABLE_GPS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });

    }

    public boolean isMapsEnabled() {

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    AlertDialog d;

    private void getLocationPermission() {
        Log.d("okokok", "enert per");
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationGranted = true;
            Log.d("okokok", "fddfdf22222");
            if (isMapsEnabled()) {
                Log.d("okokok", "fddfdf33333333");
                Toast.makeText(this, "okok2", Toast.LENGTH_SHORT).show();
                getDeviceLocation();
            }

        } else {
            //Every time user deny the location permission show a AlertDialog and when click on Positive button request again permission.
//            Log.d("okokok", "fddfdf2e else");
//            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInfoActivity.this);
//            builder.setMessage("Allow permission to continue");
//            builder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    ActivityCompat.requestPermissions(PersonalInfoActivity.this, new String[]{FINE_LOCATION, READ_SMS, RECEIVE_SMS}, PERMISSION_REQUEST_CODE);
//                    getLocationPermission();
//                    Log.v("PersonalActivity.java", "ask granted");
//                    d.dismiss();
//                }
//            });
//            d = builder.create();
//            d.show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(PersonalInfoActivity.this, new String[]{FINE_LOCATION, READ_SMS, RECEIVE_SMS, POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            } else {

                ActivityCompat.requestPermissions(PersonalInfoActivity.this, new String[]{FINE_LOCATION, READ_SMS, RECEIVE_SMS}, PERMISSION_REQUEST_CODE);
            }
//            getLocationPermission();
            Log.v("PersonalActivity.java", "not granted");
        }
    }

    public boolean isServicesOK() {

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PersonalInfoActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(PersonalInfoActivity.this,
                    available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    int i1 = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        locationGranted = false;
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result(gran Results array is > 0) arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("PersonalActivity.java", "requestCode cancelled" + ", " + grantResults[0]);
                    if (isMapsEnabled()) {
                        getDeviceLocation();
                    }

                    locationGranted = true;
                } else {
                    //Re-request for Location permission. it display an AlertDialog first then requestPermission for location.
                    AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInfoActivity.this);
                    builder.setMessage("Allow location to continue");
                    builder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                //run a loop for all the denied permission request code check if checked with never ask again or not.
                                Log.v("PersonalActivity.java", permissions.length + "");
//                            for (int i = 0, len = permissions.length; i < len; i++) {
                                int i = permissions.length - 1;//user recent denied location permission not before one.
                                String permission = permissions[i];
                                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                                    // user rejected the permission
                                    boolean showRationale = false;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                        showRationale = shouldShowRequestPermissionRationale(permission);
                                    }
                                    if (!showRationale) {
                                        // user also CHECKED "never ask again" when denied location permission.
                                        // Open settings of app to allow location from setting.
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 101);
                                        Log.v("PersonalActivity.java", "denied1");
//                                        }
                                    } else {
                                        //User did not check never ask again when deny location.
                                        Log.v("PersonalActivity.java", "denied2");
                                        getLocationPermission();
//                                        showRationale(permission, R.string.permission_denied_contacts);
                                    }
                                }
                                d.dismiss();
                            } catch (Exception e) {

                            }
                        }
                    });
                    d = builder.create();
                    d.show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        if (requestCode == PERMISSIONS_REQUEST_ENABLE_GPS) {
            if (locationGranted) {
                getDeviceLocation();

            }
        } else if (requestCode == 101) {
            //Intent object request returns result then decrement i1 by 1.
            i1--;
            getLocationPermission();
            Log.v("PersonalActivity.java", requestCode + "");
        }
    }


    public void listeners() {

        dob.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            year = year - 18;
            DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalInfoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
            datePickerDialog.setTitle("Date Of Birth");

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            datePickerDialog.show();

        });


        mDateSetListener = (datePicker, year, month, day) -> {
            String strmonth = "";
/*

            if (month == 0)
                strmonth = "Jan";
            else if (month == 1)
                strmonth = "Feb";
            else if (month == 2)
                strmonth = "Mar";
            else if (month == 3)
                strmonth = "Apr";
            else if (month == 4)
                strmonth = "May";
            else if (month == 5)
                strmonth = "Jun";
            else if (month == 6)
                strmonth = "Jul";
            else if (month == 7)
                strmonth = "Aug";
            else if (month == 8)
                strmonth = "Sep";
            else if (month == 9)
                strmonth = "Oct";
            else if (month == 10)
                strmonth = "Nov";
            else if (month == 11)
                strmonth = "Dec";
*/


            String date = year + "-" + (month + 1) + "-" + day;
            dob.setText(date);
        };


        next.setOnClickListener(view -> {


            gender = findViewById(R.id.gender);

            alternate_contact = findViewById(R.id.alternate_contact);
            dob = findViewById(R.id.dob);

            int selectedId = gender.getCheckedRadioButtonId();


            male_female = findViewById(selectedId);

            if (!(full_name.getText().toString().equals(""))
                    && !(male_female == null)
                    && !(dob.getText().toString().equals(""))
                    && !(address.equals(""))) {

                if (alternate_contact.getText().toString().equals(""))
                    alternate_contact.setText("1");
                if (!isNetworkAvailable()) {
                    noNetworkDialog();
                } else {
                    createRequest(phone);
                }

                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getApplicationContext(), "Please Fill All the Fields First", Toast.LENGTH_LONG).show();
            }

//            if (!(full_name.getText().toString().equals(""))
//                    && !(male_female == null)
//                    && !(dob.getText().toString().equals(""))) {
//
//                if (alternate_contact.getText().toString().equals(""))
//                    alternate_contact.setText("1");
//                if (!isNetworkAvailable()) {
//                    noNetworkDialog();
//                } else {
//                    createRequest(phone);
//                }
//
//                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
//
//            } else {
//
//                Toast.makeText(getApplicationContext(), "Please Fill All the Fields First", Toast.LENGTH_LONG).show();
//            }


        });
    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (locationGranted) {

                Task currentLocation = mFusedLocationProviderClient.getLastLocation();

                currentLocation.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Location currentLocation1 = (Location) task.getResult();

                        if (currentLocation1 != null) {

                            latLng = new LatLng(currentLocation1.getLatitude(), currentLocation1.getLongitude());
                            Log.d(TAG, "onLocation: " + latLng);
                            int km = 0;
                            my_location = new LatLng(currentLocation1.getLatitude(), currentLocation1.getLongitude());
                            latitude = currentLocation1.getLatitude();
                            longitude = currentLocation1.getLongitude();
                            km = 2;
                            double to_latitude = my_location.latitude + km * 0.008993182000001099;
                            double from_latitude = my_location.latitude - km * 0.008993182000001099;

                            double to_longitude = my_location.longitude + km * 0.010258364990164637;
                            double from_longitude = my_location.longitude - km * 0.010258364990164637;
                            Log.e(TAG, "onComplete: to_latitude" + to_latitude);

                            LatLng southwestLatLng = new LatLng(from_latitude, from_longitude);
                            LatLng northeastLatLng = new LatLng(to_latitude, to_longitude);

                            autocompleteFragment_user_current_add.setLocationBias(
                                    RectangularBounds.newInstance(
                                            southwestLatLng, northeastLatLng));


                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(currentLocation1.getLatitude(), currentLocation1.getLongitude(), 1);

                                address = addresses.get(0).getAddressLine(0);
                                Log.e(TAG, "onLocationChanged: address " + address);
                                autocompleteFragment_user_current_add.setText(address);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.e(TAG, "onComplete: Not Found Location! ");
                            getDeviceLocation();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    public void start_request_loadingProcess() {

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                progress.dismiss();
                return true;
            }

            return false;
        });// disable dismiss by tapping outside of the dialog
        progress.show();


    }

    private void createRequest(final long phone) {

        start_request_loadingProcess();

        cache = new DiskBasedCache(getCacheDir(), 1024 * 400);
        network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        //    Log.e(TAG, "createRequest: "+phone );9031
        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/PersonalInfo";
        requestQueue.start();
        StringRequest request = new StringRequest(Request.Method.POST, Server_url, response -> {

            Log.e(TAG, "onResponse: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.get("Status").equals("Success")) {
                    progress.dismiss();

                    if (getIntent().getBooleanExtra("verifyotp", false)) {
                        startActivity(new Intent(getApplicationContext(),
                                AreaRangeSelection.class)
                                .putExtra("camefrompersonalactivity", forwardedPersonalActivity)
                                .putExtra("latitude", latitude)
                                .putExtra("longitude", longitude)
                                .putExtra("status", true)
                                .putExtra("verifyotp", true)
                                .putExtra("userStatus", userStatus));
                    } else {
                        startActivity(new Intent(getApplicationContext(),
                                AreaRangeSelection.class)
                                .putExtra("camefrompersonalactivity", forwardedPersonalActivity)
                                .putExtra("latitude", latitude)
                                .putExtra("longitude", longitude)
                                .putExtra("status", true)
                                .putExtra("verifyotp", false));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            requestQueue.stop();

        }, error -> {

            progress.dismiss();
            Log.e(TAG, "onErrorResponse: " + error.getMessage());

        }) {
            @Override
            protected Map<String, String> getParams() {
                //     Log.e(TAG, "getParams: ");
                String gend = "";
                if (male_female.getText().toString().matches("Male")) {
                    gend = "male";
                } else if (male_female.getText().toString().matches("Female")) {
                    gend = "female";
                } else {
                    gend = "any";
                }

                Map<String, String> params = new HashMap<>();
                params.put("full_name", full_name.getText().toString());
                params.put("gender", gend);
                params.put("alternate_contact", alternate_contact.getText().toString());
                params.put("dob", (dob.getText().toString()));
                params.put("phone", "" + phone);

                return params;
            }
        };
        requestQueue.add(request);
    }

    private void editSearchLocationInitilization() {

        autocompleteFragment_user_current_add = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.user_current_add);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), mApiKey);
        }

        placesClient = Places.createClient(this);
        autocompleteFragment_user_current_add.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
        autocompleteFragment_user_current_add.setCountry("ind");
        autocompleteFragment_user_current_add.getView().setBackgroundColor(getResources().getColor(R.color.colorWhite));
        autocompleteFragment_user_current_add.setHint("Area / Current Location");

    }

    private void editSearchLocationListener() {

        autocompleteFragment_user_current_add.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                address = place.getAddress();
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: " + status);
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            return;
        }

        Toast.makeText(getApplicationContext(), "Press again to EXIT!", Toast.LENGTH_LONG).show();

        this.doubleBackToExitPressedOnce = true;

        Log.d(TAG, "onBackPressed: " + "Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
