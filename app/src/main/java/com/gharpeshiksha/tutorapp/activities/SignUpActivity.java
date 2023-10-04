package com.gharpeshiksha.tutorapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private VolleyHelper volleyHelper;
    private LinearLayout SignIn;
    private CardView Sign_Up;
    private TextInputEditText email, confirm_password, password, referalcode;
    private TextInputLayout refershow;
    static TextInputEditText contact_no;

    private String Server_url;
    private SessionConfig sessionObj;
    private ProgressDialog progress;
    private Boolean doubleTapBack = false;
    private Context context;
    private String Token = "", source = "";
    private Button retry;
    private Dialog dialog;
    private FrameLayout frameLayout;
    private TextView contact_validation, email_validation, password_validation, confirm_password_validation;
    final String TAG = SignUpActivity.this.toString();

    private TextWatcher mTextWatcher;

    @Override
    public void onBackPressed() {
        if (doubleTapBack) {
            super.onBackPressed();
        } else {
            Toast.makeText(SignUpActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            this.doubleTapBack = true;

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleTapBack = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        volleyHelper = new VolleyHelper();
        sessionObj = new SessionConfig(getApplicationContext());
        Token = sessionObj.getToken();
        source = sessionObj.getSource();
        Log.e(TAG, "onCreate: Token" + Token);

        setContentView(R.layout.activity_sign_up);


        frameLayout = findViewById(R.id.signupframelayout);
        variableInitializations();

        Sign_Up.setOnClickListener(this);
        SignIn.setOnClickListener(this);

        textWatcher();


        if (isNetworkAvailable()) {


        } else {

            noNetworkDialog();
        }

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) SignUpActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

        });


    }


    private void textWatcher() {

        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (contact_no.getText().hashCode() == s.hashCode())
                    validationOnContact();

                if (email.getText().hashCode() == s.hashCode())
                    validationOnEmail();

                if (password.getText().hashCode() == s.hashCode())
                    validationOnPassword();

                if (confirm_password.getText().hashCode() == s.hashCode())
                    validationOnConfirmPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        contact_no.addTextChangedListener(mTextWatcher);
        email.addTextChangedListener(mTextWatcher);
        password.addTextChangedListener(mTextWatcher);
        confirm_password.addTextChangedListener(mTextWatcher);
        try {
            if (referalcode.getText() != null) {
                referalcode.addTextChangedListener(mTextWatcher);
            }
        } catch (Exception e) {
            Log.e(TAG, "textWatcher: " + e.getMessage());
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void noNetworkDialog() {

        dialog = new Dialog(this);

        dialog.setContentView(R.layout.no_network_dialog);
        retry = dialog.findViewById(R.id.retry);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isNetworkAvailable()) {

                    dialog.dismiss();

                } else {

                    noNetworkDialog();
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }

    private void variableInitializations() {


        contact_no = findViewById(R.id.contact_no);

        refershow = findViewById(R.id.refershow);

        referalcode = findViewById(R.id.refercodes);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        SignIn = findViewById(R.id.sign_in);
        Sign_Up = findViewById(R.id.sign_up);

        contact_validation = findViewById(R.id.contact_validation);
        email_validation = findViewById(R.id.email_validation);
        password_validation = findViewById(R.id.password_validation);
        confirm_password_validation = findViewById(R.id.confirm_password_validation);

    }

    private void validationOnConfirmPassword() {

        if (confirm_password.getText().toString().equalsIgnoreCase("")) {
            confirm_password_validation.setVisibility(View.VISIBLE);
            confirm_password_validation.setText("Please Fill");
        } else {
            confirm_password_validation.setVisibility(View.INVISIBLE);
        }

    }

    private void validationOnPassword() {
        if (password.getText().toString().equalsIgnoreCase("")) {
            password_validation.setVisibility(View.VISIBLE);
        } else {
            password_validation.setVisibility(View.INVISIBLE);

        }


    }

    private void validationOnEmail() {
        if (email.getText().toString().equalsIgnoreCase("")) {

            email_validation.setVisibility(View.VISIBLE);
            email_validation.setText("Please Fill");
        } else {
            email_validation.setVisibility(View.INVISIBLE);
        }
    }

    private void validationOnContact() {

        if (contact_no.getText().toString().equalsIgnoreCase("")) {
            contact_validation.setVisibility(View.VISIBLE);
            contact_validation.setText("Please Fill");
        } else {
            contact_validation.setVisibility(View.INVISIBLE);
        }

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


    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sign_up:

                if (isNetworkAvailable()) {

                    signupValidation();
                } else {

                    noNetworkDialog();
                }

                break;

            case R.id.sign_in:
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
                break;

        }

    }


    private void signupValidation() {


        if (contact_no.getText().toString().equalsIgnoreCase("") || email.getText().toString().equalsIgnoreCase("") || password.getText().toString().equalsIgnoreCase("") || confirm_password.getText().toString().equalsIgnoreCase("")) {
            validationOnContact();
            validationOnEmail();
            validationOnPassword();
            validationOnConfirmPassword();
        } else {
            if (contact_no.getText().toString().length() == 10) {
                if (password.getText().toString().equalsIgnoreCase(confirm_password.getText().toString())) {
                    if (isValidEmail(email.getText().toString())) {

                        CreateRequest();


                    } else {

                        email_validation.setText("Enter valid email address");
                        email_validation.setVisibility(View.VISIBLE);
                    }

                } else {
                    confirm_password_validation.setText("Password Does Not Match");
                    confirm_password_validation.setVisibility(View.VISIBLE);

                }

            } else {
                contact_validation.setText("Invalid Contact");
                contact_validation.setVisibility(View.VISIBLE);
            }
        }
    }


    private void CreateRequest() {
        start_request_loadingProcess();

        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/SignUp"; //?phone="+contact_no+"&password="+password+"&email="+email;
        Map<String, String> params = new HashMap<>();

        params.put("phone", contact_no.getText().toString().trim());
        params.put("password", password.getText().toString());
        params.put("email", email.getText().toString());
        params.put("referralId", referalcode.getText().toString());
        params.put("firebaseToken", Token);
        params.put("source", source);

        volleyHelper.VolleyPostRequest(SignUpActivity.this, Server_url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    progress.dismiss();
                    JSONObject jsResponse = new JSONObject(response);
                    if (jsResponse.get("Status").equals("Success")) {

                        // create session here
                        SharedPreferences sharedPreferences = getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);
                        sharedPreferences.edit().clear();
                        sessionObj.setPhone(Long.parseLong(contact_no.getText().toString().trim()));
                        startActivity(new Intent(getApplicationContext(), ClassesActivity.class));
                        finish();
                    } else if (jsResponse.get("Status").equals("Error") && jsResponse.get("Message").equals("Invalid Contact")) {

                        Toast.makeText(getApplicationContext(), jsResponse.getString("Message") + ":" + contact_no.getText().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You have already a account with: " + contact_no.getText().toString() + " \nPlease Sign In..", Toast.LENGTH_SHORT).show();
                        RegistrationStatus(Long.parseLong(contact_no.getText().toString().trim()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                progress.dismiss();
            }
        });

    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public void RegistrationStatus(final long phone) {


        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/checkactivity";
        Map<String, String> params = new HashMap<>();
        params.put("phone", "" + phone);

        volleyHelper.VolleyPostRequest(SignUpActivity.this, Server_url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                //   progress.dismiss();

                Log.e(TAG, "RegistrationStatus: onResponse" + response);
                try {
                    JSONObject jsResponse = new JSONObject(response);

                    String Status = jsResponse.getString("Status");

                    if (Status.equals("Success")) {
                        String ActivityToFill = jsResponse.getString("ActivityToFill");

                        switch (ActivityToFill) {

                            case "Classes":
                                startActivity(new Intent(getApplicationContext(), ClassesActivity.class)
                                        .putExtra("contact", phone));
                                finish();
                                break;

                            case "Qualification":
                                startActivity(new Intent(getApplicationContext(), QualificationActivity.class));
                                finish();
                                break;
                            case "PersonalInfo":
                                startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
                                finish();
                                break;

                            case "AreaRangeSelection":
                                startActivity(new Intent(getApplicationContext(), AreaRangeSelection.class)
                                        .putExtra("status", true));
                                finish();
                                break;

                            case "Otp_verification":
                                startActivity(new Intent(getApplicationContext(), OTPActivity.class).putExtra("contact", phone)
                                        .putExtra("camefromarearange", true));

                                finish();
                                break;


                        }
                    } else {
                        progress.dismiss();
                        sessionObj.setPhone(phone);
                        sessionObj.LoginStatusWrite(true);

                        startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                progress.dismiss();
            }
        });

    }


}
