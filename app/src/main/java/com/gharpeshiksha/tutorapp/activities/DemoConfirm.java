package com.gharpeshiksha.tutorapp.activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
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

public class DemoConfirm extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private GoogleMap mMap;

    private Boolean camefromPA = false;
    Boolean camefromAreaRange = true;

    private FusedLocationProviderClient Client;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    SupportMapFragment mapFragment;
    Circle circle;

    String address = "", mode, sessionId;
    long phone;

    int radiu_in_m;
    CircleOptions circleOptions;
    LatLng My_Location;
    FloatingActionButton FAB_Mylocation;
    private boolean mLocationPermissionGranted = false;

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final String TAG = DemoConfirm.this.toString();

    PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment_edit_location;
    private SearchView searchView;
    private double latitude, longitude;
    Cache cache;
    Network network;

    CardView resend;
    TextView contactno;
    TextView resend_count;
    ProgressDialog progress;
    TextWatcher textWatcher;
    PinView pinView;
    Thread t;

    RequestQueue requestQueue;
    private String Server_url;
    private boolean status = false;
    private SessionConfig sessionObj;
    private Dialog dialog;
    private Button retry;

    private String enqID;
    private String winMessage;
    private String mApiKey = "AIzaSyC6_pWGCV9LapjdF0tTPFHPTRdTETOJqGY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_confirm);

        sessionObj = new SessionConfig(this);
        phone = sessionObj.getPhone();
        //google map api key
        mApiKey = sessionObj.getAPIKey();

        Re_RequestOtp();

        progress = new ProgressDialog(DemoConfirm.this);
        progress.setMessage("Please wait...");
        progress.setCancelable(true);

        Intent intent = getIntent();
        if (intent.hasExtra("enqIdForOTP")) {
            enqID = intent.getStringExtra("enqIdForOTP");
        }

        pinView = findViewById(R.id.pinview);
        contactno = findViewById(R.id.contact);
        resend_count = findViewById(R.id.resend_count);

        resend = findViewById(R.id.resend);
        resend.setEnabled(false);
        resend.setBackgroundColor(getResources().getColor(R.color.colorText));
        resendcountFunction();
        textwatcher();
        variableInitialization();

        My_Location = new LatLng(latitude, longitude);

        Log.d(TAG, "onCreate() returned: " + camefromPA.toString());
        Log.e(TAG, "onCreate:latitude " + latitude);
        Log.e(TAG, "onCreate:longitude " + longitude);

        editSearchLocationInitilization();

        if (isNetworkAvailable()) {
            if (isServiceOK()) {
                getLocationPermission();
            }
        } else {
            noNetworkDialog();
        }

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend_count.setVisibility(View.VISIBLE);
                resend.setEnabled(false);
                resend.setBackgroundColor(getResources().getColor(R.color.colorText));
                resend.setEnabled(false);
                resend.setBackgroundColor(getResources().getColor(R.color.colorText));
                progress.show();
                resendcountFunction();
                Re_RequestOtp();
            }
        });
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

                        radiu_in_m = Integer.parseInt(jsonObject.get("Radius").toString());
                        Log.d(TAG, "onResponse() returned: " + jsonObject.get("Radius").toString());
                        mode = "Edit Location";

                        UserEarlierLocation();
                        setOnMarkerDragListener();
                        editSearchLocationListener();
                        getDeviceLocation();
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
            permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, POST_NOTIFICATIONS};
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
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DemoConfirm.this, available, ERROR_DIALOG_REQUEST);
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

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses;
            String city = "Delhi";
            try {

                addresses = geocoder.getFromLocation(My_Location.latitude, My_Location.longitude, 1);

                city = addresses.get(0).getLocality().toString();

                sessionObj.setUserLocation(city);
                Log.d(TAG, "UserEarlierLocation: User City : " + city);
                address = addresses.get(0).getAddressLine(0);
                autocompleteFragment_edit_location.setText(address);

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
        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), mApiKey);
        }

        placesClient = com.google.android.libraries.places.api.Places.createClient(this);

        autocompleteFragment_edit_location.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG,
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS));

        Log.d(TAG, "editSearchLocationInitilization() returned: " + Place.Field.ADDRESS + " " + Place.Field.LAT_LNG);
        autocompleteFragment_edit_location.setCountry("ind");

        autocompleteFragment_edit_location.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment_edit_location.setTypeFilter(TypeFilter.REGIONS);

        autocompleteFragment_edit_location.setAllowEnterTransitionOverlap(true);
        //   autocompleteFragment_edit_location.setTypeFilter(TypeFilter.CITIES);
        autocompleteFragment_edit_location.setHint("Enter Your Location");

        // Area Boundary
        RectangularBounds rectangularBounds = RectangularBounds.newInstance(new LatLngBounds(new LatLng(19.0825223, 72.7411018), new LatLng(19.1251279, 72.865491)));

        autocompleteFragment_edit_location.setLocationBias(rectangularBounds);

    }

    private void variableInitialization() {
        FAB_Mylocation = findViewById(R.id.fab_my_location);
        Client = LocationServices.getFusedLocationProviderClient(this);
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
                setOnMarkerDragListener();
                editSearchLocationListener();
                FAB_MylocationsetOnClickListener();
                getDeviceLocation();
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

    private void resendcountFunction() {
        t = new Thread() {

            public void run() {
                for (int i = 0; i < 30; i++) {
                    try {
                        final int a = i;
                        runOnUiThread(new Runnable() {
                            public void run() {

                                if (a == 29) {

                                    resend.setEnabled(true);
                                    resend.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                                    resend_count.setVisibility(View.INVISIBLE);
                                    t.interrupt();
                                }
                                resend_count.setText("Resend Code in " + (30 - (a + 1)));
                            }
                        });
                        //   System.out.println("Value of i= " + i);
                        sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }

    private void Re_RequestOtp() {
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.gharpeshiksha.com/Tutor/studentsendotp";
        requestQueue.start();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("hello", jsonObject + ""+enqID);
                    Long student_phone = jsonObject.getLong("studentContact");
                    sessionId = jsonObject.getString("sessionId");


                    contactno.setText("+91 " + student_phone);
                    // String feedback = jsonObject.getString("Success");
                } catch (Exception e) {
                    Log.d("error", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("enquiryId", enqID);
                return params;
            }
        };
        requestQueue.add(request);
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
                    verifyOtp(pinView.getText().toString());
                    progress.show();
                    getParentActivityIntent();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        pinView.addTextChangedListener(textWatcher);
    }

    private void verifyOtp(String otp) {
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.gharpeshiksha.com/Tutor/studentverifyotp";
        requestQueue.start();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("heloooooo", "" + jsonObject);
                    String status = jsonObject.getString("status");

                    if (status.equals("Matched")) {
                        winMessage = jsonObject.getString("message");
                        showDialog1();

                    } else {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Wrong OTP, please enter correct OTP", Toast.LENGTH_SHORT).show();
                    }
                    requestQueue.stop();
                } catch (Exception e) {
                    Log.d("error", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("otp", otp);
                params.put("sessionId", String.valueOf(sessionId));
                params.put("latitude", "" + latitude);
                params.put("longitude", "" + longitude);
                params.put("contact", "" + phone);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void showDialog1() {
        final Dialog dialog = new Dialog(DemoConfirm.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.after_otp_confirm_dialog);


        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        ImageView imageView = dialog.findViewById(R.id.corect_img);
        TextView win_coin = dialog.findViewById(R.id.win_coin);
        TextView cancelclick = dialog.findViewById(R.id.cancelclick);

        win_coin.setText(winMessage);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }
}