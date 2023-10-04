package com.gharpeshiksha.tutorapp.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AreaRangeSelection extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static Boolean logCameFromPA = false;

    private Boolean camefromPA = false;
    Boolean camefromAreaRange = true;

    private FusedLocationProviderClient Client;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    Spinner radius;
    SupportMapFragment mapFragment;
    Circle circle;
    Button submit;
    String address = "", mode;
    long phone;

    int radiu_in_m;
    CircleOptions circleOptions;
    LatLng My_Location;
    FloatingActionButton FAB_Mylocation;
    private boolean mLocationPermissionGranted = false;

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final String TAG = AreaRangeSelection.this.toString();

    PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment_edit_location;
    private SearchView searchView;
    private double latitude, longitude;
    Cache cache;
    Network network;
    RequestQueue requestQueue;
    private String Server_url;
    private boolean status = false;
    private SessionConfig sessionObj;
    private Dialog dialog;
    private Button retry;
    private String mApiKey = "AIzaSyC6_pWGCV9LapjdF0tTPFHPTRdTETOJqGY";
    String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_range_selection);

        Intent intent = getIntent();

        sessionObj = new SessionConfig(this);
        phone = sessionObj.getPhone();
        mApiKey = sessionObj.getAPIKey();

        //user status is new or old comes from Intent
        userStatus = intent.getStringExtra("userStatus");

        variableInitialization();

        if (intent.hasExtra("latitude")) {
            latitude = intent.getDoubleExtra("latitude", 28.644800);
        } else {
            latitude = 28.644800;
        }

        if (intent.hasExtra("longitude")) {
            longitude = intent.getDoubleExtra("longitude", 77.216721);
        } else {
            longitude = 77.216721;
        }

        if (intent.hasExtra("status")) {
            status = intent.getBooleanExtra("status", false);
        } else {
            status = false;
        }

        if (intent.hasExtra("camefrompersonalactivity")) {
            camefromPA = intent.getBooleanExtra("camefrompersonalactivity", false);
        } else {
            camefromPA = false;
        }

        My_Location = new LatLng(latitude, longitude);

        Log.d(TAG, "onCreate() returned: " + camefromPA.toString());

        Log.e(TAG, "onCreate:latitude " + latitude);
        Log.e(TAG, "onCreate:longitude " + longitude);

        editSearchLocationInitilization();
        String[] radius_list = {"5 km", "6 km", "7 km", "8 km", "9 km", "10 km", "11 km", "12 km", "13 km", "14 km", "15 km", "16 km", "17 km", "18 km", "19 km", "20 km", "21 km", "22 km", "23 km", "24 km", "25 km"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, radius_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radius.setAdapter(adapter);

        if (isNetworkAvailable()) {
            if (isServiceOK()) {
                getLocationPermission();
            }
        } else {
            noNetworkDialog();
        }
        listeners();
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

                    if (isServiceOK() && !mLocationPermissionGranted) {
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

    private void getLocation(final long phone) {


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //requestQueue=new RequestQueue(cache,network);
        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/GetLocation";

        requestQueue.start();

        StringRequest request = new StringRequest(Request.Method.POST, Server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.get("Status").equals("Success")) {

                        latitude = Double.parseDouble(jsonObject.get("Latitude").toString());
                        longitude = Double.parseDouble(jsonObject.get("Longitude").toString());

                        Log.e(TAG, "onResponse: latitude " + latitude);
                        Log.e(TAG, "onResponse: longitude " + longitude);

                        int pos_int = Integer.parseInt(jsonObject.get("Radius").toString()) / 1000;
                        Log.e(TAG, "onResponse: pos_int  " + (pos_int - 5));
                        radius.setSelection(pos_int - 5);
                        radiu_in_m = Integer.parseInt(jsonObject.get("Radius").toString());
                        Log.d(TAG, "onResponse() returned: " + jsonObject.get("Radius").toString());
                        mode = "Edit Location";

                        UserEarlierLocation();
                        radiusSelectListener();
                        setOnMarkerDragListener();
                        editSearchLocationListener();
                        FAB_MylocationsetOnClickListener();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                requestQueue.stop();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* new android.app.BasicFeature.Builder(AreaRangeSelection.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/
               /* UserEarlierLocation();
                radiusSelectListener();
                setOnMarkerDragListener();
                editSearchLocationListener();
                FAB_MylocationsetOnClickListener();*/
                Log.e(TAG, "onErrorResponse: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + phone);
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void getLocationPermission() {

        String permissions[] = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS};
        } else {
            permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

        //ask for POST_NOTIFICATIONS permission by first check
//        String[] permissions2 = {Manifest.permission.POST_NOTIFICATIONS};
//        if(ContextCompat.checkSelfPermission(AreaRangeSelection.this, permissions2[0]) == PackageManager.PERMISSION_GRANTED ) {
//            Toast.makeText(AreaRangeSelection.this, "already granted", Toast.LENGTH_SHORT).show();
//        } else {
//            ActivityCompat.requestPermissions(AreaRangeSelection.this, permissions2, 221);
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mLocationPermissionGranted = false;
        switch (requestCode) {

            case LOCATION_PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0) {

                    for (int i = 0; i < grantResults.length; i++) {

                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();


                }

            }

//            case 221: {
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(AreaRangeSelection.this, "granted", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(AreaRangeSelection.this, "not granted", Toast.LENGTH_SHORT).show();
//                }
//            }
        }

    }

    public boolean isServiceOK() {

        Log.e("isServiceOK()", "checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplication());

        if (available == ConnectionResult.SUCCESS) {
            // every thing is fine and user can make map request
            Log.e("isServiceOK()", "google play service is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occured but can be solved
            Log.e("isServiceOK()", "an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(AreaRangeSelection.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Log.e("isServiceOK()", "you cant make map request");

        }
        return false;
    }

    private void initMap() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void UserEarlierLocation() {

        if (mLocationPermissionGranted) {

            mMap.clear();
            radiu_in_m = 5000;
            Log.e(TAG, "UserEarlierLocation: latitude" + latitude + " longitude" + longitude);
            int km = 0;
            My_Location = new LatLng(latitude, longitude);
            circleOptions = new CircleOptions();
            circleOptions.
                    center(My_Location)
                    .radius(radiu_in_m)
                    .strokeWidth(5f).strokeColor(getResources().getColor(R.color.colorBlack)).fillColor(getResources().getColor(R.color.colorCirclefill));


            circle = mMap.addCircle(circleOptions);
            mMap.addMarker(new MarkerOptions().position(My_Location).title("My location").draggable(true)).showInfoWindow();
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(My_Location, 11.2f);
            mMap.animateCamera(update);
            km = 2;
            double to_latitude = My_Location.latitude + km * 0.008993182000001099;
            double from_latitude = My_Location.latitude - km * 0.008993182000001099;

            double to_longitude = My_Location.longitude + km * 0.010258364990164637;
            double from_longitude = My_Location.longitude - km * 0.010258364990164637;
            Log.e(TAG, "onComplete: to_latitude" + to_latitude);

            LatLng southwestLatLng = new LatLng(from_latitude, from_longitude);
            LatLng northeastLatLng = new LatLng(to_latitude, to_longitude);
            autocompleteFragment_edit_location.setLocationBias(
                    RectangularBounds.newInstance(
                            southwestLatLng, northeastLatLng));
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses;
            String city = "Delhi";
            try {

                addresses = geocoder.getFromLocation(My_Location.latitude, My_Location.longitude, 1);

                try {
                    city = addresses.get(0).getLocality() + "";
                    Log.v("AreaRange.java", city);

                    sessionObj.setUserLocation(city);
                    Log.d(TAG, "UserEarlierLocation: User City : " + city);
                    address = addresses.get(0).getAddressLine(0);
                    autocompleteFragment_edit_location.setText(address);
                } catch (IndexOutOfBoundsException e) {

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {

                Task location = mFusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            Log.e(TAG, "onComplete: Found Location! ");
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                mMap.clear();
                                int km = 0;
                                My_Location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                circleOptions = new CircleOptions();
                                circleOptions.
                                        center(My_Location)
                                        .radius(radiu_in_m)
                                        .strokeWidth(5f).strokeColor(getResources().getColor(R.color.colorBlack)).fillColor(getResources().getColor(R.color.colorCirclefill));

                                circle = mMap.addCircle(circleOptions);
                                mMap.addMarker(new MarkerOptions().position(My_Location).title("My location").draggable(true)).showInfoWindow();
                                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(My_Location, 11.2f);
                                mMap.animateCamera(update);

                                km = 2;
                                double to_latitude = My_Location.latitude + km * 0.008993182000001099;
                                double from_latitude = My_Location.latitude - km * 0.008993182000001099;

                                double to_longitude = My_Location.longitude + km * 0.010258364990164637;
                                double from_longitude = My_Location.longitude - km * 0.010258364990164637;
                                Log.e(TAG, "onComplete: to_latitude" + to_latitude);

                                LatLng southwestLatLng = new LatLng(from_latitude, from_longitude);
                                LatLng northeastLatLng = new LatLng(to_latitude, to_longitude);
                                autocompleteFragment_edit_location.setLocationBias(
                                        RectangularBounds.newInstance(
                                                southwestLatLng, northeastLatLng));
                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                List<Address> addresses;
                                try {
                                    addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);

                                    address = addresses.get(0).getAddressLine(0);
                                    autocompleteFragment_edit_location.setText(address);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {

                            Log.e(TAG, "onComplete: Current Locaton is null! ");
                            Toast.makeText(getApplication(), "Unable to get Current Location", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }


    }

    // SEt the bound in auto complete textview

    private void editSearchLocationInitilization() {

        autocompleteFragment_edit_location = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.edit_location);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), mApiKey);
        }

        placesClient = Places.createClient(this);
        autocompleteFragment_edit_location.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
        autocompleteFragment_edit_location.setCountry("ind");
        autocompleteFragment_edit_location.getView().setBackgroundColor(getResources().getColor(R.color.colorWhite));
        autocompleteFragment_edit_location.setHint("Area / Current Location");
        autocompleteFragment_edit_location.setHint("Enter Your Location");

    }


    private void variableInitialization() {
        radius = findViewById(R.id.radius);
        submit = findViewById(R.id.submit);
        FAB_Mylocation = findViewById(R.id.fab_my_location);
        Client = LocationServices.getFusedLocationProviderClient(this);
    }


    public void listeners() {


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setVisibility(View.GONE);
                ProgressDialog dialog = new ProgressDialog(AreaRangeSelection.this);
                dialog.setTitle("please wait...");
                dialog.setMessage("We are doing some work.");
                dialog.show();
                if (isNetworkAvailable()) {

                    requestQueue = Volley.newRequestQueue(getApplicationContext());

                    Server_url = "https://api.gharpeshiksha.com" + "/Tutor/SetLocation";
                    requestQueue.start();
                    StringRequest request = new StringRequest(Request.Method.POST, Server_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            requestQueue.stop();
                            dialog.dismiss();
                            Log.e("AreaSelection.java", "onResponsee: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.get("Status").equals("Success")) {

                                    int i = 0;

//                                    sessionObj.LoginStatusWrite(true);

//                                    Log.e("AreaSelection.java", getIntent().getBooleanExtra("Dashboard", false) + "");
                                    if (camefromPA) {
                                        Log.d(TAG, "onResponse: came from Personal Activity");
                                        if (getIntent().getBooleanExtra("verifyotp", false)) {
                                            Log.v("AreaRangeSelection.java", "sign up user");
                                            Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class).putExtra("verifyotp", true).putExtra("userStatus", userStatus);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            sessionObj.setPhone(phone);
                                            startActivity(intent);
                                            finish();
                                            i++;
                                        } else {
                                            Log.d(TAG, "onResponse: came from Personal Activity 121312");
                                            Log.v("AreaRangeSelection.java", "verifyOtp is false");
                                            Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                                            intent.putExtra("contact", phone);
                                            intent.putExtra("camefromarearange", camefromAreaRange);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else if (getIntent().getBooleanExtra("Dashboard", false)) {
                                        Log.v("AreaRangeSelection.java", "when user already exist");
                                        Log.d(TAG, "onResponse: came from Personal Activity 1213dfsg12");
                                        Intent intent = new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                        finish();
                                    }
                                    if (i == 0) {
                                        Toast.makeText(getApplicationContext(), "welcome to Dashboard..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v("AreaSelection.java", e + "");
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                                /*new android.app.BasicFeature.Builder(AreaRangeSelection.this)

                                        .setMessage("There might be an internet issue, please try again after some time.")
                                        .setPositiveButton("Okay",null)
                                        .show();*/
                            dialog.dismiss();
                            Log.e("AreaSelection.java", "onResponsee: ");
                            Log.e(TAG, "onErrorResponse: " + error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {

                            Map<String, String> params = new HashMap<>();
                            params.put("current_add", address);
                            params.put("latitude", "" + My_Location.latitude);
                            params.put("longitude", "" + My_Location.longitude);
                            params.put("radius", "" + radiu_in_m);
                            params.put("phone", "" + phone);
                            return params;
                        }
                    };
                    requestQueue.add(request);

                } else {
                    dialog.dismiss();
                    noNetworkDialog();
                }

            }
        });

    }/*else {
            Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
            intent.putExtra("contact", Long.parseLong( SignUpActivity.contact_no.getText().toString() ));
            startActivity(intent);
        }*/


    private void radiusSelectListener() {

        radius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    TextView t = (TextView) view;
                    String str_radius = "no";
                    str_radius = t.getText().toString();

                    int int_radius = Integer.parseInt(str_radius.replace(" km", ""));

                    radiu_in_m = 1000 * int_radius;
                    float zoom_value = 0;
                    Log.e("radiu_in_m ", " " + radiu_in_m);


                    if (circle != null)
                        circle.remove();
                    if (radiu_in_m >= 13000) {

                        zoom_value = 10.4f;
                    } else {
                        zoom_value = 11.2f;
                    }


                    circle = mMap.addCircle(circleOptions.
                            center(My_Location)
                            .radius(radiu_in_m)
                            .strokeWidth(5f).strokeColor(getResources().getColor(R.color.colorBlack)).fillColor(getResources().getColor(R.color.colorCirclefill)));


                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(My_Location, zoom_value);

                    mMap.animateCamera(update);
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setOnMarkerDragListener() {

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.e("setOnMarkerDragListener", "onMarkerDragStart()");
                circle.remove();
                My_Location = marker.getPosition();
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDrag(Marker marker) {
                Log.e("setOnMarkerDragListener", "onMarkerDrag()");

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                circle.remove();
                My_Location = marker.getPosition();
                circle = mMap.addCircle(circleOptions.center(marker.getPosition()));

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 11.2f);

                mMap.animateCamera(update);

            }


            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.e("setOnMarkerDragListener", "onMarkerDragEnd()");

                circle.remove();
                mMap.clear();
                My_Location = marker.getPosition();

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(My_Location.latitude, My_Location.longitude, 1);

                    address = addresses.get(0).getAddressLine(0);
                    autocompleteFragment_edit_location.setText(address);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                circle = mMap.addCircle(circleOptions.center(marker.getPosition()));

                mMap.addMarker(new MarkerOptions().position(My_Location).title("My location").draggable(true)).showInfoWindow();

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(My_Location, 11.2f);
                mMap.animateCamera(update);
            }
        });
    }

    private void FAB_MylocationsetOnClickListener() {

        FAB_Mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        if (mLocationPermissionGranted) {
            if (status) {
                UserEarlierLocation();
                radiusSelectListener();
                setOnMarkerDragListener();
                editSearchLocationListener();
                FAB_MylocationsetOnClickListener();
            } else {
                getLocation(phone);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

    }

    private void editSearchLocationListener() {

        autocompleteFragment_edit_location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.e(TAG, "onPlaceSelected:Place: " + place.getName() + ", " + place.getId());
                mMap.clear();
                My_Location = place.getLatLng();

                address = place.getAddress();
                circleOptions = new CircleOptions();

                circleOptions.
                        center(My_Location)
                        .radius(radiu_in_m)
                        .strokeWidth(5f).strokeColor(getResources().getColor(R.color.colorBlack)).fillColor(getResources().getColor(R.color.colorCirclefill));

                circle = mMap.addCircle(circleOptions);

                mMap.addMarker(new MarkerOptions().position(My_Location).title("My location").draggable(true)).showInfoWindow();

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(My_Location, 11.2f);
                mMap.animateCamera(update);
                Log.e(" place.getLatLng(): ", "" + place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(" 1 : ", " onError ");
                Log.e(TAG, "An error occurred: " + status);
            }
        });
    }
}