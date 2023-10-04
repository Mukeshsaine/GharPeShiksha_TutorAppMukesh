package com.gharpeshiksha.tutorapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserFeedback extends AppCompatActivity {


    private Toolbar toolbar;
    private Spinner spinner;
    private EditText message;
    private SessionConfig config;
    private Long phone;
    private String title, msg;
    private CardView submit;
    private LinearLayout linearLayout;
    private ProgressDialog progressDialog;
    private VolleyHelper volleyHelper;

    private static final String TAG = "UserFeedback";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);

        toolbar = findViewById(R.id.feedbacktoolbar);
        spinner = findViewById(R.id.spinner);
        message = findViewById(R.id.messageedittext);
        linearLayout = findViewById(R.id.Lineardbdsjhgds);

        volleyHelper = new VolleyHelper();

        progressDialog = new ProgressDialog(UserFeedback.this);
        progressDialog.setMessage("Submitting Feedback...");
        progressDialog.setCancelable(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                    progressDialog.dismiss();
                    finish();
                    return true;
                }

                return false;
            }
        });


        config = new SessionConfig(UserFeedback.this);

        phone = config.getPhone();

        toolbar.setTitle("Feedback");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) UserFeedback.this.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void sendFeedback() {


        String alurl = "https://api.gharpeshiksha.com" + "/Tutor/TutorsFeedback";
        Map<String, String> params = new HashMap<>();
        params.put("Phone", "" + phone);
        params.put("Feedbacktype", title);
        params.put("Message", "" + msg);


        volleyHelper.VolleyPostRequest(UserFeedback.this, alurl, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {

                try {
                    JSONObject json = new JSONObject(response);
                    String result = json.getString("Result");

                    if (TextUtils.equals(result, "Success")) {
                        progressDialog.dismiss();
                        message.setText("");
                        spinner.setSelection(0);
                        new AlertDialog.Builder(UserFeedback.this)
                                .setMessage("Thank you for your valuable feedback")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                //.setCancelable(false)
                                .show();
                        Toast.makeText(UserFeedback.this, "Thank you for your feedback !", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        message.setText("");
                        spinner.setSelection(0);
                        new AlertDialog.Builder(UserFeedback.this)
                                .setMessage("Network error, Please try again.")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                //.setCancelable(false)
                                .show();
                        Toast.makeText(UserFeedback.this, "Network error, PLease try again.", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    progressDialog.dismiss();
                    message.setText("");
                    spinner.setSelection(0);
                    new AlertDialog.Builder(UserFeedback.this)
                            .setMessage("Network error, Please try again.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            //.setCancelable(false)
                            .show();
                    Toast.makeText(UserFeedback.this, "Network error, PLease try again.", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse Error: " + e.getMessage());
                }

            }

            @Override
            public void onError(String error) {
                noNetworkDialog();
                progressDialog.dismiss();
                message.setText("");
                spinner.setSelection(0);
            }
        });
    }


    public void submit(View view) {


        // Toast.makeText(this, "hello", Toast.LENGTH_LONG).show();
        Log.d(TAG, "submit: Feedback submit");
        msg = message.getText().toString();
        title = spinner.getSelectedItem().toString();

        if (title.equals("Select an option") || title.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please select an item in feedback type!", Toast.LENGTH_SHORT).show();
        } else if (msg.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill out message field!", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            sendFeedback();
        }
    }

    protected void noNetworkDialog() {

        final Dialog dialog = new Dialog(UserFeedback.this);

        dialog.setContentView(R.layout.no_network_dialog);
        Button retry = dialog.findViewById(R.id.retry);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
