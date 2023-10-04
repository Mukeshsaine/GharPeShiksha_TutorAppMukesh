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
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QualificationActivity extends AppCompatActivity implements View.OnClickListener {


    private final String TAG = QualificationActivity.this.toString();
    ;
    Dialog mydialog;
    Spinner teaching_experience, expectedfees;
    EditText qualification;
    TextView teachingmode, confirm;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    List<String> teaching_mode = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    SessionConfig sessionConfig;
    private Button next1;
    Toolbar toolbar;
    RadioGroup vehical_owned;
    String selectedexperiences, selectedexpectedfees;
    RadioButton radioSexButton;
    RequestQueue requestQueue;
    String Server_url;
    Network network;
    DiskBasedCache cache;
    long phone;
    private ProgressDialog progress;
    private int flage = 0;
    private Dialog dialog;
    private Button retry;
    String userStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification);

        final Intent intent = getIntent();

        //user status is new or old comes from Intent
        userStatus = intent.getStringExtra("userStatus");
        intialize();

        listeners();

        if (!isNetworkAvailable()) {
            noNetworkDialog();
        }

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

    private void listeners() {

        teaching_mode.clear();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        expectedfees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    TextView t = (TextView) view;
                    selectedexpectedfees = t.getText().toString();
                }catch (Exception e) {
                    Log.v("Qualification.java", e + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = vehical_owned.getCheckedRadioButtonId();
                radioSexButton = findViewById(selectedId);

                Log.e(TAG, "qualification: " + qualification.getText().toString() + "selectedexperiences: " + selectedexperiences + "selectedexpectedfees: " + selectedexpectedfees + "teaching_mode: " + teaching_mode.toString());
                if (!qualification.getText().toString().equals("") && !selectedexperiences.equals("Teaching Experience ( In years)") && !selectedexpectedfees.equals("Expected Fees") && !(radioSexButton == null) && !teaching_mode.toString().equals("[]")) {
                    if (isNetworkAvailable()) {
                        createRequest(phone);
                    } else {
                        noNetworkDialog();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields first", Toast.LENGTH_LONG).show();
                }
            }
        });

        teachingmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mydialog.setContentView(R.layout.teaching_mode);
                checkBox1 = mydialog.findViewById(R.id.checkbox1);
                checkBox2 = mydialog.findViewById(R.id.checkbox2);
                checkBox3 = mydialog.findViewById(R.id.checkbox3);
                checkBox4 = mydialog.findViewById(R.id.checkbox4);
                confirm = mydialog.findViewById(R.id.confirm);

                if (teaching_mode.contains(checkBox1.getText().toString())) {
                    checkBox1.setChecked(true);
                }

                if (teaching_mode.contains(checkBox2.getText().toString())) {
                    checkBox2.setChecked(true);
                }
                if (teaching_mode.contains(checkBox3.getText().toString())) {
                    checkBox3.setChecked(true);
                }
                if (teaching_mode.contains(checkBox4.getText().toString())) {
                    checkBox4.setChecked(true);
                }

                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mydialog.show();
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        teaching_mode.clear();
                        if (checkBox1.isChecked()) {
                            teaching_mode.add(checkBox1.getText().toString());
                        } else {
                            teaching_mode.remove(checkBox1.getText().toString());
                        }
                        if (checkBox2.isChecked()) {
                            teaching_mode.add(checkBox2.getText().toString());
                        } else {
                            teaching_mode.remove(checkBox2.getText().toString());
                        }
                        if (checkBox3.isChecked()) {
                            teaching_mode.add(checkBox3.getText().toString());
                        } else {
                            teaching_mode.remove(checkBox3.getText().toString());
                        }
                        if (checkBox4.isChecked()) {
                            teaching_mode.add(checkBox4.getText().toString());
                        } else {
                            teaching_mode.remove(checkBox4.getText().toString());
                        }
                        mydialog.dismiss();
                    }
                });


            }
        });
        teaching_experience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    teaching_experience.setSelection(i);
                    TextView t = (TextView) view;
                    selectedexperiences = t.getText().toString();
                }catch (Exception e) {
                    Log.v("Qualification.java", e + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void intialize() {

        sessionConfig = new SessionConfig(getApplicationContext());
        next1 = findViewById(R.id.next1);

        qualification = findViewById(R.id.qualification);
        teaching_experience = findViewById(R.id.spinner_teaching_experience);
        expectedfees = findViewById(R.id.expected_fees);
        vehical_owned = findViewById(R.id.radio_yes_no);
        teachingmode = findViewById(R.id.teachingmode);
        phone = sessionConfig.getPhone();


        qualification.requestFocus();
        mydialog = new Dialog(this);


        String[] experience_list = {"Teaching Experience ( In years)", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        final String[] expectedfees_list = {"Expected Fees", "Rs 100 Above", "Rs 200 Above", "Rs 300 Above", "Rs 400 Above", "Rs 500 Above", "Rs 600 Above"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, experience_list);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, expectedfees_list);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);

        teaching_experience.setAdapter(adapter);
        expectedfees.setAdapter(adapter1);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("GharPeShiksha");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_nevigation_icon));
    }

    @Override
    public void onClick(View view) {

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

    void createRequest(final long phone) {

        start_request_loadingProcess();

        cache = new DiskBasedCache(getCacheDir(), 1024 * 2000);
        network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/Qualification";
        StringRequest postRequest = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e(TAG, "CreateResponse: onResponse" + response);
                        progress.dismiss();
                        try {
                            JSONObject jsResponse = new JSONObject(response);

                            if (jsResponse.get("Status").equals("Success")) {

                                if (getIntent().getBooleanExtra("verifyotp", false)) {
                                    startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class)
                                            .putExtra("verifyotp", true).putExtra("userStatus", userStatus));
                                } else {
                                    startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class).putExtra("userStatus", userStatus));
                                }

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
                        /*new android.app.BasicFeature.Builder(QualificationActivity.this)

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
                params.put("higherqualification", qualification.getText().toString());
                params.put("teachingexperience", selectedexperiences.toString());
                params.put("expectedfees", selectedexpectedfees.substring(3, selectedexpectedfees.length() - 6));
                params.put("vehicalowned", radioSexButton.getText().toString());
                params.put("teachingmode", teaching_mode.toString());
                params.put("phone", "" + phone);
                return params;

            }
        };
        requestQueue.add(postRequest);
    }

// Double tab to exit......

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
