package com.gharpeshiksha.tutorapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.MyFeedbackActivity;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.activities.Chats_all_StudentsActivity;
import com.gharpeshiksha.tutorapp.activities.DemoConfirm;
import com.gharpeshiksha.tutorapp.data_model.AppliedLeads;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppliedLeadsAdapter extends RecyclerView.Adapter<AppliedLeadsAdapter.AppliedViewHolder> {
    private static String TAG = "AppliedLeadsAdapter";
    String feedback = "";
    Context context;
    SessionConfig sessionConfig;
    private ArrayList<AppliedLeads> appliedLeadsList;
    RequestQueue requestQueue;
    private Dialog dialog;
    private Button retry;
    String textDemofill = "";


    public AppliedLeadsAdapter(Context context, ArrayList<AppliedLeads> appliedLeads) {
        this.context = context;
        this.appliedLeadsList = appliedLeads;
        sessionConfig = new SessionConfig(context);
    }

    @NonNull
    @Override
    public AppliedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_applied_leads, parent, false);

        return new AppliedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedViewHolder holder, int position) {
        Log.v("applied---", position + "");
        AppliedLeads appliedLeads = appliedLeadsList.get(position);
        Log.d(TAG, "onBindViewHolder: leads obj " + appliedLeads);
        final AppliedViewHolder appliedViewHolder = holder;

        holder.textAppName.setText(appliedLeads.getTextAppName());
        holder.textAppPosted.setText(appliedLeads.getTextAppPosted());
        holder.textAppViewed.setText(appliedLeads.getTextAppViewed());
        holder.textAppBudget.setText(appliedLeads.getTextAppBudget());
        holder.textAppLoc.setText(appliedLeads.getTextAppLoc());
        holder.textAppDis.setText(appliedLeads.getTextAppDis());
        holder.textAppTutorReq.setText(appliedLeads.getTextAppTutorReq());
        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Chats_all_StudentsActivity.class);
                i.putExtra("UrlPic", appliedLeads.getPicUrl());
                i.putExtra("studentsUUID1", appliedLeads.getStudentUUID());
                i.putExtra("tutorUUID1", appliedLeads.getTutorUUID());
                i.putExtra("tutorname", appliedLeads.getTextAppName());
                i.putExtra("enqId", appliedLeads.getTextEnq_id());
                context.startActivity(i);
            }
        });


        Log.e(TAG, "onBindViewHolder: appliedLeads.getFeedback_given()" + appliedLeads.getFeedback_given());
        try {
            if (appliedLeads.getFeedback_given().equals("false")) {
                holder.feedRel.setVisibility(View.GONE);
                holder.gpsRel.setVisibility(View.GONE);
                holder.textFeed.setVisibility(View.VISIBLE);
            } else if (appliedLeads.getFeedback_given().equals("true")) {

                holder.our_msg.setText(appliedLeads.getTextAppGPSMes());
                holder.feedback.setText(appliedLeads.getTextAppMyFeed());
                holder.feedRel.setVisibility(View.VISIBLE);
                holder.gpsRel.setVisibility(View.VISIBLE);
                holder.textFeed.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: " + e.getMessage());
        }

        if (appliedLeads.getOtp_given().equals("non")) {
            holder.demoOtp.setVisibility(View.GONE);
        } else if (appliedLeads.getOtp_given().equals("false")) {
            holder.demoOtp.setVisibility(View.GONE);
        } else {
            holder.demoOtp.setVisibility(View.VISIBLE);
        }

        holder.applied_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    context.startActivity(new Intent(context, MyFeedbackActivity.class)
                            .putExtra("enqId", appliedLeads.getTextEnq_id())
                            .putExtra("contact", appliedLeads.getContact())
                            .putExtra("posted_on", appliedLeads.getTextAppPosted())
                            .putExtra("viewed_on", appliedLeads.getTextAppViewed())
                            .putExtra("my_feedback", appliedLeads.getTextAppMyFeed())
                            .putExtra("our_feedback", appliedLeads.getTextAppGPSMes())
                            .putExtra("feedback_given", appliedLeads.getFeedback_given()));
                } else {
                    noNetworkDialog();
                }

            }
        });

        holder.textFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {
                    feedBackDialog(appliedLeads, appliedViewHolder);
                } else {
                    noNetworkDialog();
                }

            }
        });

        TextForDemoAPI();

        holder.demoOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {
                    //SendDemoOTP(appliedLeads.getTextEnq_id());
                    showDialog1(appliedLeads.getTextEnq_id());
                } else {
                    noNetworkDialog();
                }
            }
        });
    }

    private void showDialog1(String enqId) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_demo_process);

        TextView processText = (TextView) dialog.findViewById(R.id.processText);

        TextView sentOtp_click = (TextView) dialog.findViewById(R.id.sentOtp_click);
        TextView cancelclick = (TextView) dialog.findViewById(R.id.cancelclick);

        processText.setText(textDemofill);

        sentOtp_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                builder1.setMessage("Are You Sure Send OTP to Student ? ");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int id) {
                                context.startActivity(new Intent(context, DemoConfirm.class)
                                        .putExtra("enqIdForOTP", enqId));
                                dialog1.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int id) {
                                dialog1.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        cancelclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void SendDemoOTP(String textEnq_id) {
        requestQueue = Volley.newRequestQueue(context);
        String url = "https://api.gharpeshiksha.com" + "/GetClasses/setfeedback";
        requestQueue.start();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                params.put("enq_id", textEnq_id);
                return params;
            }
        };
        requestQueue.add(request);
    }

    protected void noNetworkDialog() {

        dialog = new Dialog((context));


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
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public int getItemCount() {

        return appliedLeadsList.size();
    }

    class AppliedViewHolder extends RecyclerView.ViewHolder {

        private TextView textAppName, textAppPosted, textAppBudget, textAppViewed, textAppLoc,
                textAppDis, textAppTutorReq, textFeed, our_msg, feedback, demoOtp, chatBtn;
        private CardView applied_card;
        private RelativeLayout feedRel, gpsRel;

        AppliedViewHolder(View itemView) {
            super(itemView);

            textAppName = itemView.findViewById(R.id.textAppName);
            textAppPosted = itemView.findViewById(R.id.textAppPosted);
            textAppBudget = itemView.findViewById(R.id.textAppBudget);
            textAppViewed = itemView.findViewById(R.id.textAppViewed);
            textAppLoc = itemView.findViewById(R.id.textAppLoc);
            textAppDis = itemView.findViewById(R.id.textAppDis);
            textAppTutorReq = itemView.findViewById(R.id.textAppTutorReq);

            applied_card = itemView.findViewById(R.id.applied_card);
            textFeed = itemView.findViewById(R.id.textFeed);
            demoOtp = itemView.findViewById(R.id.demoOtp);

            feedRel = itemView.findViewById(R.id.feedRel);
            gpsRel = itemView.findViewById(R.id.gpsRel);
            our_msg = itemView.findViewById(R.id.our_msg);
            feedback = itemView.findViewById(R.id.feedback);
            chatBtn = itemView.findViewById(R.id.chatBtn);

        }
    }

    private void feedBackDialog(final AppliedLeads appliedLeads, final AppliedViewHolder holder) {

        try {
            final AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView;
            final Spinner spinner, info_spin;
            final RelativeLayout relativeLayout, genRel;
            final EditText feedback_details;

            dialogView = inflater.inflate(R.layout.layout_feedback, null);
            alert.setView(dialogView);

            alert.setCancelable(true);

            spinner = dialogView.findViewById(R.id.feedback_spin);
            info_spin = dialogView.findViewById(R.id.info_spin);
            relativeLayout = dialogView.findViewById(R.id.infoRel);
            genRel = dialogView.findViewById(R.id.genRel);
            feedback_details = dialogView.findViewById(R.id.feedback_details);

            spinnerAdapter(spinner, R.array.feedback_arrays);
            spinnerAdapter(info_spin, R.array.info_arrays);

            spinner.setPrompt("Select your Feedback Option!");

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (String.valueOf(spinner.getSelectedItem())) {
                        case "Information mentioned in lead is incorrect":
                            relativeLayout.setVisibility(View.VISIBLE);
                            genRel.setVisibility(View.GONE);
                            break;
                        case "Parents are not genuine (They do not want any teacher)":
                            relativeLayout.setVisibility(View.GONE);
                            genRel.setVisibility(View.VISIBLE);
                            break;
                        default:
                            genRel.setVisibility(View.GONE);
                            relativeLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            alert.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String feedback = String.valueOf(spinner.getSelectedItem());
                    long phone = sessionConfig.getPhone();

                    if (feedback.equals("Select an option")) {
                        Toast.makeText(context, "Please select an option", Toast.LENGTH_LONG).show();
                    }
                    if (genRel.getVisibility() == View.VISIBLE) {

                        if (!(feedback_details.getText().toString()).equals("")) {

                            String eng_id = appliedLeads.getTextEnq_id();
                            postdata("" + phone, eng_id, feedback, feedback_details.getText().toString(), "", holder, appliedLeads);
                            Toast.makeText(context, feedback_details.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                        if (feedback_details.getText().toString().equals("")) {
                            Toast.makeText(context, "Field can't be empty!", Toast.LENGTH_LONG).show();
                        }
                    } else if (relativeLayout.getVisibility() == View.VISIBLE) {

                        String eng_id = appliedLeads.getTextEnq_id();
                        String incorrect_info = info_spin.getSelectedItem().toString();
                        postdata("" + phone, eng_id, feedback, "", incorrect_info, holder, appliedLeads);


                    } else {
                        String eng_id = appliedLeads.getTextEnq_id();

                        postdata("" + phone, eng_id, feedback, "", "", holder, appliedLeads);

                    }
                    // Toast.makeText(context, feedback, Toast.LENGTH_LONG).show();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "feedBackDialog: " + e.getMessage());
        }
    }

    private void spinnerAdapter(Spinner spinner, int array) {
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    R.layout.spinner_layout, context.getResources().getStringArray(array));
            // adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
        } catch (Exception e) {
            Log.d(TAG, "spinnerAdapter: " + e.getMessage());
        }
    }

    private void postdata(final String phone,
                          final String enq_id,
                          final String current_status,
                          final String message,
                          final String incorrect_info, final AppliedViewHolder holder, final AppliedLeads appliedLeads) {


        requestQueue = Volley.newRequestQueue(context);
        String url = "https://api.gharpeshiksha.com" + "/GetClasses/setfeedback";

        requestQueue.start();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                appliedLeads.setFeedback_given("true");

                appliedLeads.setTextAppMyFeed(current_status);
                appliedLeads.setTextAppGPSMes("status is pending");
                Log.e(TAG, "onResponse: " + response);
                requestQueue.stop();
                holder.our_msg.setText("status is pending");
                holder.feedback.setText(current_status);
                holder.feedRel.setVisibility(View.VISIBLE);
                holder.gpsRel.setVisibility(View.VISIBLE);
                holder.textFeed.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                /*new android.app.BasicFeature.Builder(context)

                        .setMessage("There might be an internet issue, please try again after some time.")
                        .setPositiveButton("Okay",null)
                        .show();*/
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("enq_id", enq_id);
                params.put("current_status", current_status);
                params.put("message", message);
                params.put("incorrect_info", incorrect_info);
                return params;

            }
        };
        requestQueue.add(request);


    }

    private void TextForDemoAPI() {
        requestQueue = Volley.newRequestQueue(context);
        String url = "https://api.gharpeshiksha.com/Tutor/earn_by_demo";
        requestQueue.start();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    textDemofill = response;
                } catch (Exception e) {
                    Log.d("error", "" + e);
                }
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
                params.put("phone", sessionConfig.getPhone() + "");
                return params;
            }
        };
        requestQueue.add(request);
    }

}
