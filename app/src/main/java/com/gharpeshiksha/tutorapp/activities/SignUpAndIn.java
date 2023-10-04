package com.gharpeshiksha.tutorapp.activities;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpAndIn extends AppCompatActivity {

    private Boolean doubleTapBack = false;
    Credential credential;
    private static final int CREDENTIAL_PICKER_REQUEST = 120;
    private Button byOtp, byPassword;
    private TextInputLayout phoneNumberInputLayout;
    private LinearLayout newUser, oldUser;
    private EditText newPassword, newCnfPassword, referalCode, emailId, phoneNumber;

    private SessionConfig sessionConfig;
    private String Token, userStatus = "", tutorName = "", tutorImage = "", source = "";
    private TextView letsBegin, errorMsg;
    private ProgressBar progressBar;
    private VolleyHelper volleyHelper;
    int count;
    private RelativeLayout newUserEmail;
    private EditText email;
    private Button newUserBtn;
    String uploaddocument;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_and_in);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = SignUpAndIn.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(SignUpAndIn.this, R.color.white));
        }
        oldUser = findViewById(R.id.oldUserLayout);
        newUser = findViewById(R.id.newuserLayout);

        //if user is new below Views are Visible
        newUserEmail = findViewById(R.id.email);
        email = findViewById(R.id.editTextEmail);
        newUserBtn = findViewById(R.id.next);

        oldUser.setVisibility(View.GONE);
        newUser.setVisibility(View.GONE);

        sessionConfig = new SessionConfig(SignUpAndIn.this);
        Token = sessionConfig.getToken();
        source = sessionConfig.getSource();

        progressBar = findViewById(R.id.progressBar);
        newPassword = findViewById(R.id.password_new_edit);
        newCnfPassword = findViewById(R.id.password_new_cnf_edit);
        referalCode = findViewById(R.id.referral_new_edit);
        emailId = findViewById(R.id.email_new_edit);
        phoneNumber = findViewById(R.id.edittextnumber);
        letsBegin = findViewById(R.id.loginbutton);
        phoneNumberInputLayout = findViewById(R.id.textinputLayout);

        volleyHelper = new VolleyHelper();
        byOtp = findViewById(R.id.withOtp);
        byPassword = findViewById(R.id.withPassword);

        errorMsg = findViewById(R.id.errormsg);
        errorMsg.setVisibility(View.GONE);

        try {
            uploaddocument = getIntent().getStringExtra("upload");
        } catch (Exception e) {

        }

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);

        String verified = sharedPreferences.getString("VerifiedContact", sharedPreferences.getString("VerifiedContact", "failed"));

        Log.d("TAG", "onCreate: SharedPreference VerifiedContact : " + verified);

        sessionConfig = new SessionConfig(getApplicationContext());

        if (verified.matches("Success")) {
            if (sessionConfig.LoginStatusRead()) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("uploaddoc", uploaddocument).
                        putExtra("sortDis", "firsttime"));
                finish();
//                startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
//                finish();
            }
        }

        phoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (count == 0) {

                    requestHint();
                    count++;
                }
                return false;
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 10) {
                    Log.d("PhoneTextWatcher", "onTextChanged: " + s.toString());
                    if (s.toString().charAt(0) == '6' || s.toString().charAt(0) == '7' || s.toString().charAt(0) == '8' ||
                            s.toString().charAt(0) == '9') {
                        phoneNumberInputLayout.setErrorEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        errorMsg.setVisibility(View.GONE);
                        checkIfUserNewVolley(s.toString());

                    } else {
                        phoneNumberInputLayout.setError("Enter valid Number");
                        phoneNumberInputLayout.setErrorEnabled(true);
                        errorMsg.setVisibility(View.VISIBLE);
                    }
                } else {
                    oldUser.setVisibility(View.GONE);
                    newUser.setVisibility(View.GONE);
                    letsBegin.setVisibility(View.GONE);
                    newUserEmail.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //by this letsBegin(LoginButton) View onClick() method user get signup on the api or server
        letsBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ValueCheck", "onClick: " + "phone = " + phoneNumber.getText().toString()
                        + "\nemail = " + emailId.getText().toString()
                        + "\npassword = " + newPassword.getText().toString()
                        + "\ncnfpassword = " + newCnfPassword.getText().toString()
                        + "\nrefereal = " + referalCode.getText().toString());
                if (userStatus.equals("new")) {
                    if (emailId.getText().toString().isEmpty() || newPassword.getText().toString().isEmpty() || newCnfPassword.getText().toString().isEmpty()) {
                        showAlertDialog("Please Complete All Fields");
                    } else {
                        if (!newPassword.getText().toString().equals(newCnfPassword.getText().toString())) {
                            showAlertDialog("Password and confirm password does not match");
                        } else {
                            startSignUpProcessVolley(phoneNumber.getText().toString(), emailId.getText().toString(), newPassword.getText().toString(), referalCode.getText().toString());
                        }
                    }
                }
            }
        });

        byOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionConfig.setPhone(Long.parseLong(phoneNumber.getText().toString()));
                Intent intent = new Intent(SignUpAndIn.this, OTPActivity.class);
                Log.d("TAG", "onClick: byOtp : " + phoneNumber.getText().toString());
                intent.putExtra("contact", Long.parseLong(phoneNumber.getText().toString()));
                intent.putExtra("userStatus", "" + userStatus);
                startActivity(intent);
                Log.v("Sign.java", userStatus);
            }
        });
        byPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionConfig.setPhone(Long.parseLong(phoneNumber.getText().toString()));
                startActivity(new Intent(SignUpAndIn.this, PasswordActivity.class)
                        .putExtra("tutor", tutorName)
                        .putExtra("contact", Long.parseLong(phoneNumber.getText().toString()))
                        .putExtra("Image_url", tutorImage)
                        .putExtra("userStatus", userStatus));
                Log.v("Sign.java", userStatus);
            }
        });
    }

    private void startSignUpProcessVolley(final String phone, final String email, final String password, final String referral) {
        Log.e("NewUser", "startSignUpProcessVolley: " + "phone = " + phone
                + "\nemail = " + email
                + "\npassword = " + password
                + "\nrefereal = " + referral);

        progressBar.setVisibility(View.VISIBLE);

        String Server_url = "https://api.gharpeshiksha.com" + "/Tutor/SignUp";
        Map<String, String> params = new HashMap<>();

        params.put("phone", phone.trim());
        params.put("password", password);
        params.put("email", email);
        params.put("referralId", referral);
        params.put("firebaseToken", Token);
        params.put("source", source);

        volleyHelper.VolleyPostRequest(SignUpAndIn.this, Server_url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    JSONObject jsResponse = new JSONObject(response);
                    if (jsResponse.get("Status").equals("Success")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);
                        sharedPreferences.edit().clear();
                        sessionConfig.setPhone(Long.parseLong(phone.trim()));
                        startActivity(new Intent(getApplicationContext(), ClassesActivity.class));
                        finish();
                    } else if (jsResponse.get("Status").equals("Error") && jsResponse.get("Message").equals("Invalid Contact")) {
                        showAlertDialog("Invalid Number");
                    } else {
                        showAlertDialog("You already have an account. Please Sign In");
                        RegistrationStatus(Long.parseLong(phone.trim()));
                        Log.v("Sign.java", params.toString());
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

    public void RegistrationStatus(final long phone) {

        String Server_url = "https://api.gharpeshiksha.com" + "/Tutor/checkactivity";
        Map<String, String> params = new HashMap<>();
        params.put("phone", "" + phone);

        volleyHelper.VolleyPostRequest(SignUpAndIn.this, Server_url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
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
//                                finish();
                                break;
                        }
                    } else {
                        Log.v("SignUpAndIn.java", "" + Status);
                        sessionConfig.setPhone(phone);
                        sessionConfig.LoginStatusWrite(true);
                        startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
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

    private void checkIfUserNewVolley(final String phone) {
        String Server_url = "https://api.gharpeshiksha.com" + "/Tutor/SignInPassword";
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone.trim());
        params.put("firebaseToken", Token);

        volleyHelper.VolleyPostRequest(SignUpAndIn.this, Server_url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {

                try {
                    JSONObject jsResponse = new JSONObject(response);

                    if (jsResponse.get("Status").equals("Success")) {
                        // USER EXISTS
                        userStatus = "old";
                        Log.v("SignUpAndIn.java", jsResponse.get("password_created") + "");
                        if (jsResponse.get("password_created").equals(true)) {
                            tutorName = jsResponse.get("name").toString();
                            tutorImage = jsResponse.getString("Pic_url");
                            oldUser.setVisibility(View.VISIBLE);
                            byPassword.setVisibility(View.VISIBLE);
                            newUser.setVisibility(View.GONE);
                            letsBegin.setVisibility(View.GONE);
                            progressBar.setVisibility(View.INVISIBLE);
                        } else if (jsResponse.get("password_created").equals(false)) {
                            tutorName = jsResponse.get("name").toString();
                            tutorImage = jsResponse.getString("Pic_url");
                            oldUser.setVisibility(View.VISIBLE);
                            byPassword.setVisibility(View.GONE);
                            newUser.setVisibility(View.GONE);
                            letsBegin.setVisibility(View.GONE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        // USER DOES NOT EXISTS
                        userStatus = "new";
                        newUser.setVisibility(View.GONE);
                        oldUser.setVisibility(View.GONE);
                        byPassword.setVisibility(View.GONE);
                        letsBegin.setVisibility(View.GONE);
                        progressBar.setVisibility(View.INVISIBLE);
                        sessionConfig.setPhone(Long.parseLong(phoneNumber.getText().toString()));
                        //Display RelativeLayout contain View for enter Email and next button to go on OTPActivity
                        newUserEmail.setVisibility(View.VISIBLE);
                        newUserBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setupEmailInput();
                            }
                        });
//                        Intent intent = new Intent(SignUpAndIn.this, OTPActivity.class);
//                        Log.d("TAG", "onClick: byOtp : " + phoneNumber.getText().toString());
//                        intent.putExtra("contact", Long.parseLong(phoneNumber.getText().toString()));
//                        intent.putExtra("userStatus", "" + userStatus);
//                        startActivity(intent);
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

    private void setupEmailInput() {
        String emailId = email.getText().toString();
        //check format of email id
        if (!emailId.equals("")) {
            //formatExp is the format of EmailId validation
            String formatExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            //Pattern class object is check if expression is valid or not with flag for CASE_INSENSITIVE;
            Pattern pattern = Pattern.compile(formatExp, Pattern.CASE_INSENSITIVE);
            //Matcher class instance is returned by matcher(emailId) method if pattern is valid
            Matcher matcher = pattern.matcher(emailId);
            try {
                if (matcher.matches()) {
                    sessionConfig.setEmailId(emailId);
                    Log.v("Signup.java", sessionConfig.getEmailId() + ", " + Long.parseLong(phoneNumber.getText().toString()));
                    //Explicit Intent object to tell android to open component of out app (Activity, Service, Broadcast Receiver)
                    Intent i = new Intent(SignUpAndIn.this, OTPActivity.class);
                    i.putExtra("contact", Long.parseLong(phoneNumber.getText().toString()));
                    i.putExtra("userStatus", userStatus);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.v("Signup.java", e + "");
            }
        } else {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {


        if (doubleTapBack) {
            super.onBackPressed();
        } else {
            Toast.makeText(SignUpAndIn.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            this.doubleTapBack = true;

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleTapBack = false;
                }
            }, 2000);
        }
    }

    public void requestHint() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        Toast.makeText(SignUpAndIn.this, "this is show list", Toast.LENGTH_SHORT).show();
        PendingIntent intent = Credentials.getClient(SignUpAndIn.this).getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }


    // Obtain the phone number from the result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                Log.v("SignUpAndIn.java", "" + credential.getId().substring(3));
                phoneNumber.setText(credential.getId().substring(3));
                Log.d("data===", Credential.EXTRA_KEY);
            }
        }
    }

    public void showAlertDialog(String title) {
        new AlertDialog.Builder(SignUpAndIn.this, R.style.AppCompatAlertDialogStyle)
                .setMessage(title)
                .setPositiveButton("Okay", null).show();
    }

    //Show details forms after otp verification
    public void showForms() {
        ImageView logo = findViewById(R.id.logo);
        TextView title = findViewById(R.id.title);
        TextView subTitle = findViewById(R.id.subtitle);
        TextInputLayout input = findViewById(R.id.textinputLayout);
        newUser.setVisibility(View.VISIBLE);
        letsBegin.setVisibility(View.VISIBLE);
        oldUser.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        subTitle.setVisibility(View.GONE);
        input.setVisibility(View.GONE);
    }
}
