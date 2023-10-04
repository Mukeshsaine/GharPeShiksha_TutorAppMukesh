package com.gharpeshiksha.tutorapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private String TAG = "EditProfileActivity.java";

    private TextView edit_save, confirm, Teaching_Mode, name;
    int flag = 0;
    Spinner Teaching_Experience, Expected_Fees, TimeAbility;
    TextInputEditText email, phone, alternate_phone, dob, higher_Qualification, current_add, permanent_add, Sub_text_class, Teaching_Mode_class, Time_set;
    View alternate_phone_edit, higher_Qualification_edit, Teaching_Experience_edit, Expected_Fees_edit, Teaching_Mode_edit, current_add_edit, permanent_add_edit, editAbout, Sub_text_class_edit, Teaching_mode_edit, Time_Ability_Edit, Time_set_edit,
            Upload_Video_edit, Upload_test_edit, Upload_Notes_edit;

    Dialog mydialog;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    List<String> teaching_mode = new ArrayList<>();

    ImageView imageview_account_profile;

    private int length_words = 0;
    private String currentImagePath = null;
    Cache cache;
    Network network;
    RequestQueue requestQueue;
    String Server_url;
    ArrayAdapter<String> EF_adapter;
    ArrayAdapter<String> TE_adapter;
    //    ArrayAdapter<String> Time_adapter;
    long phone_no;
    String[] experience_list;
    String[] expectedfees_list;
    //    String[] Timeability_list;
    HashMap<String, String> index = new HashMap<>();
    private String selectedexpectedfees;
    int selectedExperience;
    Toolbar toolbar;
    ProgressDialog dialog;
    Button Set_Time, Upload_Notes, Upload_Video, Upload_test;;
    ImageView back_btn;
    private TextInputLayout abouttextInputLayout;


    LinearLayout linearLayout;
    TextView activeplan, date_exp, remain_view, subscriptiondetils;
    private EditText tutorAbout;


    @SuppressLint({"RestrictedApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        String tutorid = intent.getStringExtra("id");
//        Log.d("mukesh", "onCreate: id time = " + tutorid);
//        Toast.makeText(this, "id = " + tutorid, Toast.LENGTH_SHORT).show();

        SessionConfig sessionConfig = new SessionConfig(getApplicationContext());
        back_btn = findViewById(R.id.back);
        back_btn.setOnClickListener(view -> {
            finish();
//            Toast.makeText(this, "this is Edit Profile", Toast.LENGTH_SHORT).show();
        });
        Set_Time = findViewById(R.id.Set_time_btn);
//        Upload_Video = findViewById(R.id.Upload_Video);
//        Upload_test = findViewById(R.id.Upload_Test);
//        Upload_Notes = findViewById(R.id.Upload_Notes);
        Set_Time.setOnClickListener(view -> {
            Intent i = new Intent(EditProfileActivity.this, TimeSetActivity.class);
            i.putExtra("tutor_id", tutorid);
            startActivity(i);
        });

        phone_no = sessionConfig.getPhone();

        initialize();

        dialog.setMessage("Loading....");
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    finish();
                    return true;
                }

                return false;
            }
        });
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    finish();
                    return true;
                }

                return false;
            }
        });
      //  dialog.show();
        CreateRequest();


        EF_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, expectedfees_list);
        EF_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Expected_Fees.setEnabled(false);
        Expected_Fees.setAdapter(EF_adapter);

        TE_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, experience_list);
        TE_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Teaching_Experience.setEnabled(false);
        Teaching_Experience.setAdapter(TE_adapter);

      /*  Time_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, Timeability_list);
        Time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TimeAbility.setEnabled(false);
        TimeAbility.setAdapter(Time_adapter);*/


        listeners();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        tutorAbout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                length_words = charSequence.toString().trim().split(" ").length;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean checkAboutValidation() {
        if (length_words <= 30) {
            abouttextInputLayout.setError("Enter atleast 30 words.");
            Toast.makeText(EditProfileActivity.this, "Enter at least 30 words.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (length_words >= 200) {
            abouttextInputLayout.setError("Enter less than 200 words.");
            Toast.makeText(EditProfileActivity.this, "Enter less than 200 words.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public void initialize() {
        dialog = new ProgressDialog(EditProfileActivity.this);
        toolbar = findViewById(R.id.toolbar);


        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));


        edit_save = toolbar.findViewById(R.id.edit_save);
        abouttextInputLayout = findViewById(R.id.abouttextInputLayout);
        name = findViewById(R.id.name);
        tutorAbout = findViewById(R.id.tutorAbout);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        alternate_phone = findViewById(R.id.alternate_phone);
        dob = findViewById(R.id.dob);
        Sub_text_class = findViewById(R.id.Sub_text_class);
        Teaching_Mode_class = findViewById(R.id.Teaching_mode);
        higher_Qualification = findViewById(R.id.higher_Qualification);
        Teaching_Experience = findViewById(R.id.Teaching_Experience);
        Expected_Fees = findViewById(R.id.Expected_Fees);
        // TimeAbility = findViewById(R.id.Time_Ability);
        Teaching_Mode = findViewById(R.id.Teaching_Mode);
        current_add = findViewById(R.id.current_add);
        permanent_add = findViewById(R.id.permanent_add);
//        Time_set = findViewById(R.id.Time_set);
//        Upload_Notes = findViewById(R.id.Upload_Notes);
//        Upload_test = findViewById(R.id.Upload_Test);
//        Upload_Video = findViewById(R.id.Upload_Video);

        mydialog = new Dialog(this);
        imageview_account_profile = findViewById(R.id.imageview_account_profile);
        alternate_phone_edit = findViewById(R.id.alternate_phone_edit);
        higher_Qualification_edit = findViewById(R.id.higher_Qualification_edit);
        Teaching_Experience_edit = findViewById(R.id.Teaching_Experience_edit);
        Sub_text_class_edit = findViewById(R.id.Sub_text_class_edit);
        Expected_Fees_edit = findViewById(R.id.Expected_Fees_edit);
        //Time_Ability_Edit = findViewById(R.id.Time_Ability_edit);
        Teaching_Mode_edit = findViewById(R.id.Teaching_Mode_edit);
        current_add_edit = findViewById(R.id.current_add_edit);
        permanent_add_edit = findViewById(R.id.permanent_add_edit);
        activeplan = findViewById(R.id.activeplan);
        date_exp = findViewById(R.id.dateexp);
        //Time_set_edit = findViewById(R.id.Time_set_edit);
        remain_view = findViewById(R.id.viewremain);
        subscriptiondetils = findViewById(R.id.subscriptiondetails);
        linearLayout = findViewById(R.id.subscriptiondetailslayout);
        editAbout = findViewById(R.id.editAbout);
        Teaching_mode_edit = findViewById(R.id.Teaching_mode_edit);


        experience_list = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
                "22", "23", "24", "25", "26", "27", "28", "29", "30", "30+"};
        expectedfees_list = new String[]{"Rs 100 Above",
                "Rs 150 Above",
                "Rs 200 Above",
                "Rs 250 Above",
                "Rs 300 Above",
                "Rs 350 Above",
                "Rs 400 Above",
                "Rs 450 Above",
                "Rs 500 Above",
                "Rs 550 Above",
                "Rs 600 Above",
                "Rs 650 Above",
                "Rs 700 Above",
                "Rs 750 Above",
                "Rs 800 Above",
                "Rs 850 Above",
                "Rs 900 Above",
                "Rs 950 Above",
                "Rs 1000 Above"
        };

      /*  Timeability_list = new String[]{
                "choose day",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday",
        };*/

        index.put(100 + "", "" + 0);
        index.put(150 + "", "" + 1);
        index.put(200 + "", "" + 2);
        index.put(250 + "", "" + 3);
        index.put(300 + "", "" + 4);
        index.put(350 + "", "" + 5);
        index.put(400 + "", "" + 6);
        index.put(450 + "", "" + 7);
        index.put(500 + "", "" + 8);
        index.put(550 + "", "" + 9);
        index.put(600 + "", "" + 10);
        index.put(650 + "", "" + 11);
        index.put(700 + "", "" + 12);
        index.put(750 + "", "" + 13);
        index.put(800 + "", "" + 14);
        index.put(850 + "", "" + 15);
        index.put(900 + "", "" + 16);
        index.put(950 + "", "" + 17);
        index.put(1000 + "", "" + 18);


    }

    private void CreateRequest() {

        cache = new DiskBasedCache(getCacheDir(), 1024 * 512);
        network = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, network);

        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/GetProfile";
        requestQueue.start();

        StringRequest request = new StringRequest(Request.Method.POST, Server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String tutorabout = "";
                    if (jsonObject.has("tutorAbout")) {
                        tutorabout = jsonObject.getString("tutorAbout");
                    }

                    if (jsonObject.get("Status").equals("Success")) {
                        dialog.dismiss();
                        setParameters(
                                jsonObject.get("Pic_url").toString(),
                                jsonObject.get("name").toString(),
                                jsonObject.getString("email"),
                                "" + phone_no,
                                jsonObject.get("Alt_contact").toString(),
                                jsonObject.get("dob").toString(),
                                jsonObject.get("qualification").toString(),
                                jsonObject.get("Experience").toString(),
                                jsonObject.get("Expected_fees").toString(),
                                jsonObject.get("teaching_mode").toString(),
                                jsonObject.get("Current_add").toString(),
                                jsonObject.get("Permanent_add").toString(),
                                jsonObject.get("gender").toString(),
                                tutorabout


                        );

                        try {
                            if (!jsonObject.get("planDetail").toString().isEmpty()) {

                                linearLayout.setVisibility(View.VISIBLE);
                                if (jsonObject.get("planDetail").toString().equals("Free")) {
                                    activeplan.setText("Active Plan : Trial");
                                } else {
                                    activeplan.setText("Active Plan : " + jsonObject.get("planDetail").toString());
                                }
                                date_exp.setText("Expiry Date : " + jsonObject.get("expiry_date").toString());
                                remain_view.setText("Remaining View : " + jsonObject.get("contact_viewed").toString());

                            }
                        } catch (Exception e) {
                            Log.d(TAG, "onResponse: Non Paid member");
                        }


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                requestQueue.stop();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*new android.app.BasicFeature.Builder(EditProfileActivity.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/

                Log.e(TAG, "onErrorResponse: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("phone", "" + phone_no);

                return params;
            }
        };

        requestQueue.add(request);


    }

    private void createResponse(final long phone) {


        requestQueue = new RequestQueue(cache, network);
        Log.d(TAG, "createResponse expected fee: " + selectedexpectedfees);

        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/SetProfile";
        requestQueue.start();

        StringRequest request = new StringRequest(Request.Method.POST, Server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.get("Status").equals("Success")) {
                        Log.e(TAG, "onResponse: " + jsonObject.get("Status"));


                        //Toast.makeText(getApplicationContext(),"welcome to Dashboard..",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                requestQueue.stop();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*new android.app.BasicFeature.Builder(EditProfileActivity.this)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/
                Log.e(TAG, "onErrorResponse: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
               /* Log.e(TAG, "getParams: phone: " + phone + " currentImagePath: " + currentImagePath + " " +
                        "alternate_phone: " + alternate_phone.getText().toString() +
                        "higher_Qualification: " + higher_Qualification.getText().toString() +
                        "teaching_mode: " + teaching_mode.toString() +
                        "selectedexpectedfees: " + selectedexpectedfees +
                        "selectedExperience: " + selectedExperience +
                        "current_add: " + current_add.getText().toString() +
                        "permanent_add: " + permanent_add.getText().toString());*/
                params.put("phone", "" + phone);
                //   params.put("Pic_url",currentImagePath);
                params.put("Alt_contact", alternate_phone.getText().toString());
                params.put("qualification", higher_Qualification.getText().toString());
                params.put("Teaching_mode", teaching_mode.toString().replace("[", "").replace("]", ""));
//                params.put("Expected_fees", selectedexpectedfees.substring(3, selectedexpectedfees.length() - 6));
                params.put("Expected_fees", selectedexpectedfees);
                params.put("Experience", "" + selectedExperience);
                params.put("Current_add", current_add.getText().toString());
                params.put("Permanent_add", permanent_add.getText().toString());
                params.put("tutorAbout", tutorAbout.getText().toString());


                return params;
            }
        };

        requestQueue.add(request);


    }

    private void setParameters(String pic_url,
                               String Name,
                               String email_add,
                               String phone_no,
                               String alternate_phone_no,
                               String Dob,
                               String Higher_qualification,
                               String teaching_experience,
                               String expected_fees,
                               String teaching_mode1,
                               String Current_add,
                               String Permanent_add,
                               String gender,
                               String about) {


        imageview_account_profile.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).setDefaultRequestOptions(RequestOptions.centerCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .load(pic_url).placeholder(R.drawable.ic_nprofile).into(imageview_account_profile);


        if (gender.equals("male")) {
            Name = "Mr. " + Name;

        } else if (gender.equals("Male")) {
            Name = "Mr. " + Name;

        } else {
            Name = "Ms. " + Name;
        }


        selectedexpectedfees = expected_fees;

        if (teaching_experience.equals("30+")) {

            selectedExperience = 31;
        } else {

            selectedExperience = Integer.parseInt(teaching_experience);


        }
        if (about.matches("")) {
            about = "Please tell us about your Self";
            tutorAbout.setHint(about);
            tutorAbout.setTextColor(Color.GRAY);
        } else {
            tutorAbout.setText(about);
            tutorAbout.setTextColor(getResources().getColor(R.color.black));
        }

        name.setText(Name);
        email.setText(email_add);
        phone.setText(phone_no);

        if (alternate_phone_no.equals("1")) {

            alternate_phone.setText("");
        } else {
            alternate_phone.setText(alternate_phone_no);
        }

        dob.setText(Dob);
        if (Higher_qualification != null)
            higher_Qualification.setText(Higher_qualification);
        Teaching_Experience.setSelection(selectedExperience);

        Expected_Fees.setSelection(Integer.parseInt(index.get(expected_fees)));

        Teaching_Mode.setText(teaching_mode1.replace("[\"[", "").replace("]\"]", "").replace("\"", ""));

        teaching_mode.addAll(Arrays.asList(teaching_mode1.replace("[\"[", "").replace("]\"]", "").replace("\"", "").split(",")));


        current_add.setText(Current_add);

        if (Permanent_add.equals("No Address Found")) {

            permanent_add.setText("");

        } else {
            permanent_add.setText(Permanent_add);
        }


    }


    private void listeners() {


        toolbar.setNavigationOnClickListener(v -> finish());

        Expected_Fees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Log.d("EditProfile.java", "onItemSelected: " + "parent");
//                    selectedexpectedfees = ((TextView) view).getText().toString();
                } catch (Exception e) {
                    Log.e(TAG, "onItemSelected: ", e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Teaching_Experience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    if (((TextView) view).getText() != null) {
                        if (((TextView) view).getText().toString().equals("30+")) {
                            selectedExperience = 31;
                        } else {
                            selectedExperience = Integer.parseInt(((TextView) view).getText().toString());
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onItemSelected: " + e);
                    selectedExperience = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edit_save.setOnClickListener(v -> {
            if (flag == 0) {
                edit_save.setText("Save");
                flag = 1;

                alternate_phone.setEnabled(true);
                higher_Qualification.setEnabled(true);
                Teaching_Experience.setEnabled(true);
                Expected_Fees.setEnabled(true);
                Teaching_Mode.setEnabled(true);
                current_add.setEnabled(true);
                permanent_add.setEnabled(true);
                tutorAbout.setEnabled(true);
                Sub_text_class.setEnabled(true);
                Teaching_Mode_class.setEnabled(true);
                Set_Time.setVisibility(View.VISIBLE);
//                Upload_Notes.setVisibility(View.VISIBLE);
//                Upload_test.setVisibility(View.VISIBLE);
//                Upload_Video.setVisibility(View.VISIBLE);
                /*TimeAbility.setEnabled(true);
                Time_set.setEnabled(true);*/
               /* Upload_Video.setEnabled(true);
                Upload_test.setEnabled(true);
                Upload_Notes.setEnabled(true);*/

                alternate_phone_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                higher_Qualification_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                Teaching_Experience_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                Expected_Fees_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                // Time_Ability_Edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                Teaching_Mode_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                current_add_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                permanent_add_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                editAbout.setBackgroundColor(getResources().getColor(R.color.colorText));
                //Time_set_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                Sub_text_class_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                Teaching_mode_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
              /*  Upload_Video_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                Upload_test_edit.setBackgroundColor(getResources().getColor(R.color.colorText));
                Upload_Notes_edit.setBackgroundColor(getResources().getColor(R.color.colorText));*/


            } else {
                if (checkAboutValidation()) {
                    createResponse(phone_no);
                    if (getIntent().hasExtra("from")) {
                        if (getIntent().getStringExtra("from").equals("chatBot")) {
                            Toast.makeText(EditProfileActivity.this, "Going Back To ChatBot", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    edit_save.setText("Edit Profile");
                    flag = 0;
                    /*floatingActionButton.setVisibility(View.INVISIBLE);*/
                    // floatingActionButton.setEnabled(false);
                    alternate_phone.setEnabled(false);
                    higher_Qualification.setEnabled(false);
                    Teaching_Experience.setEnabled(false);
                    Expected_Fees.setEnabled(false);
                    Teaching_Mode.setEnabled(false);
                    current_add.setEnabled(false);
                    permanent_add.setEnabled(false);
                    tutorAbout.setEnabled(false);
                    Sub_text_class.setEnabled(false);
                    Teaching_Mode_class.setEnabled(false);
                    // TimeAbility.setEnabled(false);
//                    Time_set.setEnabled(false);
//                    Upload_Notes.setEnabled(false);
//                    Upload_test.setEnabled(false);
//                    Upload_Video.setEnabled(false);

                    alternate_phone_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    higher_Qualification_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    Teaching_Experience_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    Expected_Fees_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    Teaching_Mode_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    current_add_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                    Time_Ability_Edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    permanent_add_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    editAbout.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                    Time_set_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    Sub_text_class_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    Teaching_mode_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                    Upload_Notes_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                    Upload_test_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                    Upload_Video_edit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
            }


        });


        Teaching_Mode.setOnClickListener(v -> {


            mydialog.setContentView(R.layout.teaching_mode);
            checkBox1 = mydialog.findViewById(R.id.checkbox1);
            checkBox2 = mydialog.findViewById(R.id.checkbox2);
            checkBox3 = mydialog.findViewById(R.id.checkbox3);
            checkBox4 = mydialog.findViewById(R.id.checkbox4);
            confirm = mydialog.findViewById(R.id.confirm);

            if (teaching_mode.contains(checkBox1.getText().toString()))
                checkBox1.setChecked(true);

            if (teaching_mode.contains(checkBox2.getText().toString()))
                checkBox2.setChecked(true);

            if (teaching_mode.contains(checkBox3.getText().toString()))
                checkBox3.setChecked(true);

            if (teaching_mode.contains(checkBox4.getText().toString()))
                checkBox4.setChecked(true);

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

            confirm.setOnClickListener(view -> {

                teaching_mode.clear();

                if (checkBox1.isChecked())
                    teaching_mode.add(checkBox1.getText().toString());
                else
                    teaching_mode.remove(checkBox1.getText().toString());

                if (checkBox2.isChecked())
                    teaching_mode.add(checkBox2.getText().toString());
                else
                    teaching_mode.remove(checkBox2.getText().toString());

                if (checkBox3.isChecked())
                    teaching_mode.add(checkBox3.getText().toString());
                else
                    teaching_mode.remove(checkBox3.getText().toString());

                if (checkBox4.isChecked())
                    teaching_mode.add(checkBox4.getText().toString());
                else
                    teaching_mode.remove(checkBox4.getText().toString());


                mydialog.dismiss();
                Log.e(TAG, "onClick: teaching_mode " + teaching_mode);
                Teaching_Mode.setText(teaching_mode.toString().replace("[", "").replace("]", ""));
            });

        });

    }


}
