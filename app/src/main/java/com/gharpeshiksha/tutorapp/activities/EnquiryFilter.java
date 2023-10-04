package com.gharpeshiksha.tutorapp.activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.gharpeshiksha.tutorapp.utils.Utility;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EnquiryFilter extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "EnquiryFilter.java";
    CheckBox check_box1, check_box2;
    private ArrayList<CheckBox> checkBoxes;
    private TextView /*location_text_view,*/ classes_text_view, radius_text_view, seekbar_initial_numebr_text_view, seekbar_final_numebr_text_view, seekbar_selected_numebr_text_view, changeLocation;
    private LinearLayout checkBoxLayout,/* locationlinearlayout,*/
            radiuslinearlayout, layout_search;
    private Button sumbit_button, Sumbit_button1;
    private SeekBar radius_seekbar;
    int stepsize = 1;
    int min = 5;
    int max = 30;
    private String selectClaesss = "";
    private String selectedRadius = "";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1, REQUEST_CHECK_SETTINGS = 2, LAYOUT_LOCATION = 3;
    static String LatitudeS = "", LongitudeS = "";
    private TextView locText;
    private ArrayList<String> datab = new ArrayList<>();
    private ArrayList<String> selectedclasses = new ArrayList<>();
    private EditText locationEdiText, edit_Search;
    private TextView getCurrentLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted = false;
    private LatLng My_Location;
    private SessionConfig sessionConfig;
    private VolleyHelper volleyHelper;
    private String filterdata = "https://api.gharpeshiksha.com/Tutor/getInitialFilterData";
    private String tempaddress = "";
    private TextView examplelocation, textSearch;
    private Boolean changeClasses = false, changeArea = false;
    private int CountClasses = 0;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private String mApiKey = "AIzaSyC6_pWGCV9LapjdF0tTPFHPTRdTETOJqGY", searchEnquiryId;
    private boolean isSearchByEnqId = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_filter);

        Dashboard dashboard = new Dashboard();

        check_box1 = findViewById(R.id.check_box1);
        check_box2 = findViewById(R.id.check_box2);
        edit_Search = findViewById(R.id.Edit_Search1);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchEnquiryId = s.toString();
                if (!searchEnquiryId.isEmpty() && !searchEnquiryId.contains(" ") && searchEnquiryId.length() > 3) {
                    isSearchByEnqId = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        edit_Search.addTextChangedListener(textWatcher);


        //edit_Search.setBackgroundColor(Color.TRANSPARENT);
        textSearch = findViewById(R.id.search_textview);
        progressDialog = new ProgressDialog(EnquiryFilter.this);
        sessionConfig = new SessionConfig(EnquiryFilter.this);
        volleyHelper = new VolleyHelper();
        checkBoxes = new ArrayList<>();

        selectClaesss = dashboard.getBadgeCount();
        LatitudeS = dashboard.getLat();
        LongitudeS = dashboard.getLong();
        selectedRadius = dashboard.getRadius();
        tempaddress = sessionConfig.getTempAddress();
        //Google Api key
        mApiKey = sessionConfig.getAPIKey();
        getGoogleMapApiKey();

        locText = findViewById(R.id.locText);
        seekbar_initial_numebr_text_view = findViewById(R.id.seekbar_initial_numebr_text_view);
        seekbar_final_numebr_text_view = findViewById(R.id.seekbar_final_numebr_text_view);
        seekbar_selected_numebr_text_view = findViewById(R.id.seekbar_selected_numebr_text_view);
        locationEdiText = findViewById(R.id.locationEdiText);
        getCurrentLocation = findViewById(R.id.getCurrentLocation);
        checkBoxLayout = findViewById(R.id.checkBoxLinearLayout);

        radiuslinearlayout = findViewById(R.id.radiusLinearLayout);
        layout_search = findViewById(R.id.layout_search);
        classes_text_view = findViewById(R.id.classes_text_view);
        changeLocation = findViewById(R.id.changeLocation);
        radius_text_view = findViewById(R.id.radius_text_view);
        sumbit_button = findViewById(R.id.submit_button);

        radius_seekbar = findViewById(R.id.radius_seekbar);
        examplelocation = findViewById(R.id.examplelocation);
        toolbar = findViewById(R.id.toolbarfilter);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> finish());


        defaultValueOfArea(selectedRadius, tempaddress);
        checkboxvolley();
        getCurrentselectedFilters();
        getCurrentselectedFilters_All_India();
        Log.v("EnquiryFilter.java", mApiKey + "");

        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            try {
                com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), mApiKey);
            } catch (Exception e) {
                Log.v("EnquiryFilter.java", e + "");
            }
        }

        locationEdiText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "loc2");
                getLocation();
            }
        });

        textSearch.setOnClickListener(view -> {
            layout_search_Methods();
        });


        getCurrentLocation.setOnClickListener(view -> {

            if (checkLocationServiceOn()) {
                getLocationPermission();
                if (mLocationPermissionGranted) {
                    Log.v(TAG, "loc1");
                    getDeviceLocation();
                } else {
                    Toast.makeText(EnquiryFilter.this, "Unable Device Location", Toast.LENGTH_SHORT).show();
                }
            } else {
                getHighPriorityLocation();
            }

        });

        check_box1.setOnClickListener(view -> {
            check_box1.setChecked(true);
            check_box2.setChecked(false);
            isSearchByEnqId = false;
            searchEnquiryId = "";
            edit_Search.setText("");
        });

        check_box2.setOnClickListener(view -> {
            check_box1.setChecked(false);
            check_box2.setChecked(true);
            isSearchByEnqId = false;
            searchEnquiryId = "";
            edit_Search.setText("");
        });

        sumbit_button.setOnClickListener(v -> {

            if(Utility.isNetworkAvailable(EnquiryFilter.this)) {
                if (isSearchByEnqId) {
                    if (searchEnquiryId.isEmpty() || searchEnquiryId.length() <= 3) {
                        Toast.makeText(this, "Enquiry id must be greater than 3 or not empty", Toast.LENGTH_SHORT).show();
                    } else {
                        actionSend2("searchByEnquiryId");
                    }
                } else {
                    if (check_box1.isChecked()) {
                        check_box1.toggle();
//                check_box2.setChecked(false);
                        Log.d(TAG, "onCreate: radius checkbox");
                        getSelectedClasses();
                        actionSend();
                    }
                    if (check_box2.isChecked()) {
                        check_box2.toggle();
                        Log.d(TAG, "onCreate: all india checkbox");
                        actionSend2("allIndia");
//                check_box1.setChecked(false);
                    }
                }
            } else {
                Toast.makeText(EnquiryFilter.this, "No connection", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }


        });

        setradiusseekbar();


        radius_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double value = min + (progress * stepsize);

                String km = "kms";
                if (progress == 1) {
                    km = "km";
                }
                selectedRadius = String.valueOf(progress);
                seekbar_selected_numebr_text_view.setText("Radius (" + progress + " " + km + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        checkBoxLayout.setVisibility(View.VISIBLE);
        radiuslinearlayout.setVisibility(View.GONE);

        classes_text_view.setOnClickListener(v -> setLayout());

        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "loc1");
                getLocation();
            }
        });


        radius_text_view.setOnClickListener(v -> setRadiusLayout());
    }

    private void actionSend2(String filterBy) {
        Intent intent = new Intent(EnquiryFilter.this, Dashboard.class)
                .putExtra("filterBy", filterBy)
                .putExtra("enqId", searchEnquiryId)
                .putExtra("selectClaesss", selectClaesss)
                .putExtra("lat", LatitudeS)
                .putExtra("long", LongitudeS)
                .putExtra("radius", selectedRadius)
                .putExtra("filterCount", CountClasses);

        Log.v("EnquiryFilter.java", selectClaesss);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    //Making network request to fetch the Google Map API Key using Volley Library POST type request.
    public void getGoogleMapApiKey() {
        //RequestQueue class object is manage to make request on network and manage processing of response in cache
        //It run request in background and return response when it completely executed
        RequestQueue requestQueue = Volley.newRequestQueue(EnquiryFilter.this);
        String url = "https://api.gharpeshiksha.com" + "/Tutor/common_config";
        StringRequest mapApiRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse:  location" + response);
                try {
                    JSONObject data = new JSONObject(response);
                    mApiKey = data.getString("map_key");
                    Log.v("Splash.java", mApiKey + "");

                    sessionConfig.setAPIKey(mApiKey);

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

    private void actionSend() {

        Intent intent = new Intent(EnquiryFilter.this, Dashboard.class);
        intent.putExtra("EnquiryFilter", "EnquiryFilter")
                .putExtra("selectClaesss", selectClaesss)
                .putExtra("lat", LatitudeS)
                .putExtra("long", LongitudeS)
                .putExtra("radius", selectedRadius)
                .putExtra("filterCount", CountClasses);

        Log.v("EnquiryFilter.java", selectClaesss);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void defaultValueOfArea(String selectedRadius, String tempaddress) {

        if (!selectedRadius.matches("")) {
            seekbar_selected_numebr_text_view.setText("Radius (" + selectedRadius + " Km" + ")");

            radius_seekbar.setProgress(0);
            radius_seekbar.setMax(25);
            radius_seekbar.setProgress(Integer.parseInt(selectedRadius));


        }
        if (!tempaddress.matches("")) {
            locationEdiText.setText(tempaddress);
        }

        if (!selectedRadius.isEmpty() && !tempaddress.isEmpty()) {
            String exmlocation = getExampleLocation(selectedRadius, tempaddress);
            examplelocation.setText(Html.fromHtml(exmlocation));
        }
    }

    private String getExampleLocation(String selectedRadius, String tempaddress) {

        String exmlocation = "";
        exmlocation = "<font color=\"#006e96\"> " + selectedRadius + "</font>" + " Kms  From " + "<font color=\"#006e96\"> " + tempaddress + "</font>";

        return exmlocation;
    }

    private void setRadiusLayout() {

        radiuslinearlayout.setVisibility(View.VISIBLE);
        checkBoxLayout.setVisibility(View.GONE);
        layout_search.setVisibility(View.GONE);
        radius_text_view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        radius_text_view.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        classes_text_view.setBackgroundColor(getResources().getColor(R.color.grey));
        classes_text_view.setTextColor(getResources().getColor(R.color.colorBlack));
        textSearch.setBackgroundColor(getResources().getColor(R.color.grey));
        textSearch.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    private void layout_search_Methods() {
        radiuslinearlayout.setVisibility(View.GONE);
        checkBoxLayout.setVisibility(View.GONE);
        layout_search.setVisibility(View.VISIBLE);
        textSearch.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        textSearch.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        classes_text_view.setBackgroundColor(getResources().getColor(R.color.grey));
        classes_text_view.setTextColor(getResources().getColor(R.color.colorBlack));
        radius_text_view.setBackgroundColor(getResources().getColor(R.color.grey));
        radius_text_view.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    private void setLayout() {
        radiuslinearlayout.setVisibility(View.GONE);
        checkBoxLayout.setVisibility(View.VISIBLE);
        layout_search.setVisibility(View.GONE);
        classes_text_view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        classes_text_view.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        radius_text_view.setBackgroundColor(getResources().getColor(R.color.grey));
        radius_text_view.setTextColor(getResources().getColor(R.color.colorBlack));
        textSearch.setBackgroundColor(getResources().getColor(R.color.grey));
        textSearch.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    private void setradiusseekbar() {
        radius_seekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        radius_seekbar.setMax((max - min) / stepsize);
        // radius_seekbar.setProgress(1);
    }

    private void getCurrentselectedFilters() {
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", sessionConfig.getPhone() + "");

        volleyHelper.VolleyPostRequest(EnquiryFilter.this, filterdata, params, new VolleyResponse() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSucess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (LatitudeS.matches("")) {
                        LatitudeS = jsonObject.getString("latitude");
                        Log.d(TAG, "onSucess: lati  =  " + LatitudeS);
                    }
                    if (LongitudeS.matches("")) {
                        LongitudeS = jsonObject.getString("longitude");
                        Log.d(TAG, "onSucess: long ==  " + LongitudeS);
                    }
                    if (selectedRadius.matches("")) {
                        selectedRadius = jsonObject.getString("radius");
                        radius_seekbar.setProgress(0);
                        radius_seekbar.setMax(25);
                        radius_seekbar.setProgress(Integer.parseInt(selectedRadius));
                        Log.d(TAG, "onSucess:  == radius = " + radius_seekbar);
                    }

                    String add = jsonObject.getString("currentLocation");
                    if (tempaddress.matches("")) {
                        locationEdiText.setText(add);
                        Log.d(TAG, "onSucess:currlo  === " + locationEdiText);
                    }

                    if (!selectedRadius.matches("") && tempaddress.isEmpty())
                        examplelocation.setText(Html.fromHtml(getExampleLocation(selectedRadius, add)));

                    if (!selectedRadius.matches("") && !tempaddress.isEmpty()) {
                        examplelocation.setText(Html.fromHtml(getExampleLocation(selectedRadius, tempaddress)));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void getCurrentselectedFilters_All_India() {


    }


    private void getSelectedClasses() {

        selectClaesss = "";
        CountClasses = 0;
        for (int i = 0; i < checkBoxes.size(); i++) {

            CheckBox checkBox = checkBoxes.get(i);
            if (checkBox.isChecked()) {
                if (selectClaesss.matches("")) {
                    selectClaesss = checkBox.getText().toString();
                    CountClasses = 1;
                } else {
                    selectClaesss += "," + checkBox.getText().toString();
                    CountClasses++;
                }
            }

        }

    }

    public void setClasses() {


        // checkBoxes.clear();
        for (int i = 0; i < datab.size(); i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(datab.get(i));


            for (int j = 0; j < selectedclasses.size(); j++) {
                if (datab.get(i).matches(selectedclasses.get(j))) {
                    checkBox.setChecked(true);
                }
            }
            checkBoxLayout.addView(checkBox);
            checkBoxes.add(checkBox);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkBox.setButtonTintList(getColorStateList(R.color.colorPrimary));
            }

        }
//        progressDialog.dismiss();


    }


    public void getLocation() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("in")
                .build(EnquiryFilter.this);

        startActivityForResult(intent, LAYOUT_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == LAYOUT_LOCATION) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    Log.i("testLoc", "Place: " + place.getName() + ", " + place.getId());
                    sessionConfig.setTempAddress(tempaddress);
                    /*tempaddress = place.getName();*/
                    locationEdiText.setText(place.getName());
                    Log.v(TAG, place.getLatLng().latitude + ", " + place.getLatLng().longitude);
                    LatitudeS = String.valueOf(place.getLatLng().latitude);
                    LongitudeS = String.valueOf(place.getLatLng().longitude);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkboxvolley() {

        String url = "https://api.gharpeshiksha.com/Tutor/Classes?phone=" + sessionConfig.getPhone();


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);

                selectedclasses.clear();
                if (selectClaesss.matches("")) {

                    changeClasses = false;

                    JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
                    String cls[] = jsonObject.getString("selectedcourse").split("@");
                    for (String c : cls) {
                        String ff[] = c.split("<");
                        selectedclasses.add(ff[0]);

                        Log.d(TAG, "onResponse: 123456 :" + ff[0]);
                    }
                    CountClasses = selectedclasses.size();

                } else {

                    String cls[] = selectClaesss.split(",");
                    for (String c : cls) {
                        selectedclasses.add(c);
                    }
                    CountClasses = selectedclasses.size();

                }


                Log.d(TAG, "onResponse: ChangeSubjectFilter : " + changeClasses);


                for (int i = 1; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                        datab.add(jsonObject1.getString("Course"));
                    } catch (IndexOutOfBoundsException e) {
                        Log.v("Enquiry.java", e + "");
                    }
                }

                setClasses();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        requestQueue.add(stringRequest);
    }


    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {

                Task location = mFusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Log.e(TAG, "onComplete: Found Location! ");
                        Location currentLocation = (Location) task.getResult();
                        if (currentLocation != null) {
                            Log.e(TAG, "onComplete: Found Location! ");
                            My_Location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                                Log.d(TAG, "onComplete: location found :" + addresses.get(0).getLocality());
                                String subloca = addresses.get(0).getSubLocality();
                                String locality = addresses.get(0).getLocality();
                                String SubAdminArea = addresses.get(0).getSubAdminArea();
                                String add = getcurrentlocationtextaddress(subloca, SubAdminArea, locality);
                                sessionConfig.setTempAddress(add);
                                locationEdiText.setText(add);
                                examplelocation.setText(Html.fromHtml(getExampleLocation(selectedRadius, add)));
                                LatitudeS = String.valueOf(currentLocation.getLatitude());
                                LongitudeS = String.valueOf(currentLocation.getLongitude());
                                Log.v(TAG, LatitudeS + ", " + LongitudeS);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {

                        Log.e(TAG, "onComplete: Current Locaton is null! ");
                        Toast.makeText(getApplication(), "Unable to get Current Location", Toast.LENGTH_LONG).show();
                    }

                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }


    }

    private String getcurrentlocationtextaddress(String subloca, String subadmin, String locality) {

        String address = "";

        if (subloca != null && locality != null && subadmin != null) {
            address = subloca + " , " + subadmin + " , " + locality;

        } else if (subloca == null && locality != null && subadmin != null) {
            address = subadmin + " , " + locality;
        } else if (subloca != null && locality == null && subadmin != null) {
            address = subloca + " , " + subadmin;
        } else if (subloca == null && locality == null && subadmin != null) {
            address = subadmin;
        } else if (subloca == null && subadmin == null && locality != null) {
            address = locality;
        } else if (subloca != null && subadmin == null && locality == null) {
            address = subloca;
        }

        return address;
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


                getDeviceLocation();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    public void getHighPriorityLocation() {


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            getDeviceLocation();
        });

        task.addOnFailureListener(this, e -> {

            Toast.makeText(EnquiryFilter.this, "Turn on your device location.", Toast.LENGTH_LONG).show();

            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(EnquiryFilter.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }


    private boolean checkLocationServiceOn() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }


    private void showProgressDialog() {

        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {

            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                progressDialog.dismiss();
                finish();
                return true;
            }

            return false;
        });
        progressDialog.show();
    }

}
