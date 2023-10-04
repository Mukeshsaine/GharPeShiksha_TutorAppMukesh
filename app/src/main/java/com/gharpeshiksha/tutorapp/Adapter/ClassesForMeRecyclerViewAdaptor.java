package com.gharpeshiksha.tutorapp.Adapter;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.BasicFeatures;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.UpgradeActivity;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.activities.Chats_all_StudentsActivity;
import com.gharpeshiksha.tutorapp.activities.ClassesForMeActivity;
import com.gharpeshiksha.tutorapp.data_model.ClassesForMe;
import com.gharpeshiksha.tutorapp.fragments.ChatsFragment;
import com.gharpeshiksha.tutorapp.fragments.ClassesForMeFragment;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassesForMeRecyclerViewAdaptor extends RecyclerView.Adapter<ClassesForMeRecyclerViewAdaptor.EnquiriesViewHolder> {
    private static String TAG = "ClassesForMeListAdapter";
    private Context context;
    private List<ClassesForMe> classesForMeList;
    SessionConfig sessionConfig;
    static long phone;
    private Dialog dialog;
    private Button retry;
    String favourite_enq_URL = "https://api.gharpeshiksha.com" + "/GetClasses/favorite_enq";
    String favourite_action_URL = "https://api.gharpeshiksha.com" + "/GetClasses/favorite";
    Map<String, String> PostFavDataMap = new HashMap<>();
    private BasicFeatures basicFeatures = new BasicFeatures();
    String tempId = "";
    private VolleyHelper volleyHelper;
    boolean isShimmer = false;
    int shimmerNumber = 3;

    public ClassesForMeRecyclerViewAdaptor(Context context, List<ClassesForMe> classesForMeList) {
        this.context = context;
        this.classesForMeList = classesForMeList;
        sessionConfig = new SessionConfig(context);
        phone = sessionConfig.getPhone();
        Log.d(TAG, "ClassesForMeRecyclerViewAdaptor:  " + "bhjgj   " + classesForMeList.size());
    }

    public ClassesForMeRecyclerViewAdaptor(List<ClassesForMe> classesForMeList) {
        this.classesForMeList = classesForMeList;
    }

    public ClassesForMeRecyclerViewAdaptor(Context context1, List<ClassesForMe> classesForMeList, boolean flag) {
        this.context = context1;
        this.classesForMeList = classesForMeList;
    }

    @NonNull
    @Override
    public EnquiriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_classes_for_me, parent, false);
        return new EnquiriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EnquiriesViewHolder holder, @SuppressLint("RecyclerViewnumberofclasses") final int position) {
        final ClassesForMe classesForMe = classesForMeList.get(position);
        holder.Chats_btn.setOnClickListener(view -> {
               Intent i = new Intent(context, Chats_all_StudentsActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               i.putExtra("tutorname", classesForMe.getTextName());
               i.putExtra("UrlPic", classesForMe.getPicUrl());
               i.putExtra("studentsUUID1",classesForMe.getStudentUUId());
               i.putExtra("tutorUUID1",classesForMe.getTutorUUId());
               i.putExtra("enqId", classesForMe.getTextEnq_id() + "");
            Log.d(TAG, "onBindViewHolder enqId: " + classesForMe.getTextEnq_id());
               context.startActivity(i);
        });

//        Log.d(TAG, "onBindViewHolder: ClassesForMess : " + position + "  " + classesForMe.getTextName());
        holder.textMins.setText(classesForMe.getTextMins());

        if (classesForMe.getFreeClass().equals("non")) {
            holder.freeClass.setVisibility(View.GONE);
        } else if (classesForMe.getFreeClass().equals("false")) {
            holder.freeClass.setVisibility(View.GONE);
        } else {
            holder.freeClass.setVisibility(View.VISIBLE);
        }

        if (classesForMe.getHighComp().equals("non")) {
            holder.highComp.setVisibility(View.GONE);
        } else if (classesForMe.getFreeClass().equals("false")) {
            holder.highComp.setVisibility(View.GONE);
        } else {
            holder.highComp.setVisibility(View.VISIBLE);
        }

        if (classesForMe.getPaymentstatus().matches("active") || classesForMe.getPaymentstatus().matches("expired")) {

            int r = 162;
            int g = 162;
            int b = 162;

            if (TextUtils.equals(classesForMeList.get(position).getStatus(), "Cancelled")) {
                if (classesForMe.getEnq_viewed()) {
                    holder.textCon.setVisibility(View.VISIBLE);
                    holder.textclose.setVisibility(View.GONE);
                    holder.cardView.setCardBackgroundColor(Color.WHITE);
                    holder.textTutorReq.setTextColor(Color.BLACK);
                    holder.textName.setTextColor(Color.BLACK);
                    holder.textBudget.setTextColor(Color.BLACK);
                    holder.textTutorReq.setTextColor(Color.BLACK);
                    holder.textLoc.setTextColor(Color.BLACK);
                    holder.textDis.setTextColor(Color.BLACK);
                    holder.textMins.setTextColor(Color.BLACK);
                    holder.textViews.setTextColor(Color.BLACK);
                    holder.textFeedback.setVisibility(View.VISIBLE);


                    holder.textcall.setVisibility(View.VISIBLE);
                    holder.textCon.setVisibility(View.GONE);

                    if (classesForMe.getFeedback()) {
                        holder.textFeedback.setVisibility(View.GONE);
                    } else {
                        holder.textFeedback.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.Chats_btn.setVisibility(View.GONE);
                    holder.highComp.setVisibility(View.GONE);
                    holder.freeClass.setVisibility(View.GONE);


                    holder.textCon.setVisibility(View.GONE);
                    holder.textclose.setVisibility(View.VISIBLE);
                    holder.cardView.setCardBackgroundColor(Color.rgb(245, 245, 245));
                    holder.textTutorReq.setTextColor(Color.rgb(r, g, b));
                    holder.textName.setTextColor(Color.rgb(r, g, b));
                    holder.textBudget.setTextColor(Color.rgb(r, g, b));
                    holder.textTutorReq.setTextColor(Color.rgb(r, g, b));
                    holder.textLoc.setTextColor(Color.rgb(r, g, b));
                    holder.textDis.setTextColor(Color.rgb(r, g, b));
                    holder.textMins.setTextColor(Color.rgb(r, g, b));
                    holder.textViews.setTextColor(Color.rgb(r, g, b));
                    holder.textcall.setVisibility(View.GONE);
                    holder.textFeedback.setVisibility(View.GONE);
                }
            } else {

                holder.textCon.setVisibility(View.VISIBLE);
                holder.textclose.setVisibility(View.GONE);
                holder.cardView.setCardBackgroundColor(Color.WHITE);
                holder.textTutorReq.setTextColor(Color.BLACK);
                holder.textName.setTextColor(Color.BLACK);
                holder.textBudget.setTextColor(Color.BLACK);
                holder.textTutorReq.setTextColor(Color.BLACK);
                holder.textLoc.setTextColor(Color.BLACK);
                holder.textDis.setTextColor(Color.BLACK);
                holder.textMins.setTextColor(Color.BLACK);
                holder.textViews.setTextColor(Color.BLACK);
                holder.textFeedback.setVisibility(View.VISIBLE);


                if (classesForMe.getEnq_viewed()) {

                    holder.textcall.setVisibility(View.VISIBLE);
                    holder.textCon.setVisibility(View.GONE);

                    if (classesForMe.getFeedback()) {
                        holder.textFeedback.setVisibility(View.GONE);
                    } else {
                        holder.textFeedback.setVisibility(View.VISIBLE);
                    }


                } else {
                    holder.textcall.setVisibility(View.GONE);
                    holder.textCon.setVisibility(View.VISIBLE);
                    holder.textFeedback.setVisibility(View.GONE);

                }

            }


        } else if (classesForMe.getPaymentstatus().matches("nonpaid")) {

            if (classesForMe.getEnq_viewed()) {
                holder.textcall.setVisibility(View.GONE);
                holder.textCon.setVisibility(View.VISIBLE);
                holder.textFeedback.setVisibility(View.GONE);

            } else {
                holder.textcall.setVisibility(View.GONE);
                holder.textCon.setVisibility(View.VISIBLE);
                holder.textFeedback.setVisibility(View.GONE);

            }

        }


        if (TextUtils.equals(classesForMe.getTextViews(), "0 Tutors contacted")) {
            holder.textViews.setText("Be the first one to contact");
        } else {
            holder.textViews.setText(classesForMe.getTextViews());
        }


        if (TextUtils.equals(classesForMe.getFavorite(), "true")) {
            holder.favourite.setImageResource(R.drawable.favourite_true);
        } else {
            holder.favourite.setImageResource(R.drawable.favourite_false);
        }

        holder.textName.setText(classesForMe.getTextName());
        holder.textTutorReq.setText(classesForMe.getTextTutorReq());
        holder.textBudget.setText("Budget : " + classesForMe.getTextBudget());
        holder.textLoc.setText(classesForMe.getTextLoc());
        holder.textDis.setText("" + classesForMe.getTextDis() + " Km");

        holder.textFeedback.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                feedBackDialog(String.valueOf(classesForMe.getTextEnq_id()), holder.textFeedback);
            } else {
                noNetworkDialog();
            }
        });


        holder.share.setOnClickListener(v -> {
            String shareBody =   // STRING TO BE SHARED
                    "\uD83D\uDCDA Requirement of subjects : "
                            + classesForMe.getTextTutorReq() // SUBJECTS
                            + "\n\n\u20B9 Amount offered : "
                            + classesForMe.getTextBudget()  // BUDGET
                            + "\n\n\uD83D\uDCCD Location of teaching : "
                            + classesForMe.getTextLoc()  // LOCATION
                            + "\n\n\uD83D\uDCF1 Find more classes at " + "https://play.google.com/store/apps/details?id=com.gharpeshiksha.tutorapp"
                            + "\n\nOR"
                            + "\n\n\uD83D\uDCF1 Explore our website : https://gharpeshiksha.com/";
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            context.startActivity(Intent.createChooser(sharingIntent, "Share via..."));
        });

        holder.favourite.setOnClickListener(v -> {
            Toast.makeText(context, "" + classesForMe.getTextViews(), Toast.LENGTH_SHORT).show();
            if (TextUtils.equals(classesForMe.getFavorite(), "true")) {
                holder.favourite.setImageResource(R.drawable.favourite_false);
                PostFavDataMap.put("phone", "" + phone);
                PostFavDataMap.put("enq_id", "" + classesForMe.getTextEnq_id());
                PostFavDataMap.put("action", "remove");
                String Text4Toast = "Removed from favourite";
                VolleyForFavourite(PostFavDataMap, Text4Toast);
            } else {
                holder.favourite.setImageResource(R.drawable.favourite_true);
                PostFavDataMap.put("phone", "" + phone);
                PostFavDataMap.put("enq_id", "" + classesForMe.getTextEnq_id());
                PostFavDataMap.put("action", "add");
                String Text4Toast = "Added to favourite";
                VolleyForFavourite(PostFavDataMap, Text4Toast);
            }
        });

        holder.cardView.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                Log.d(TAG, "onClick: on dakbhjgdjgfkjhd" + classesForMe.getTextEnq_id());
                intent(ClassesForMeActivity.class, classesForMe, "noView");
            } else {
                noNetworkDialog();
            }
        });

        holder.textcall.setOnClickListener(view -> {

            if (isNetworkAvailable()) {
                tempId = classesForMe.getTextEnq_id() + "";
                checkStatus("textcall", classesForMe);
            } else {
                noNetworkDialog();
            }

        });

        holder.textCon.setOnClickListener(v -> {

            if (isNetworkAvailable()) {
                tempId = classesForMe.getTextEnq_id() + "";
                checkStatus("textcon", classesForMe);
            } else {
                noNetworkDialog();
            }
        });
    }

    private void checkStatus(final String type, final ClassesForMe classesForMe) {

//        Log.v("Classes---", "status");
        sessionConfig = new SessionConfig(context);

        basicFeatures.CheckPaymentStatus(context, sessionConfig.getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {

                if (type.matches("textcall")) {
                    if (status.matches("active") || status.matches("expired")){
                        Log.v("Classes---", "active");
                        intent(ClassesForMeActivity.class, classesForMe, "enq_viewed");
                    }
                    else if (status.matches("nonpaid"))
                        context.startActivity(new Intent(context, UpgradeActivity.class));
                    else if (status.matches("free"))
                        basicFeatures.showFreePaidDialog(context);
                    else if (status.matches("freeactivation"))
                        intent(ClassesForMeActivity.class, classesForMe, "calldirect");
                    /*requestfornumber(classesForMe);*/

                } else if (type.matches("textcon")) {
                    if (status.matches("active")) {
                        Log.v("Classes---", "active2");
                        showMsgBeforecall(classesForMe);
                    }
                    else if (status.matches("nonpaid") || status.matches("expired"))
                        context.startActivity(new Intent(context, UpgradeActivity.class));
                    else if (status.matches("freeactivation"))
                        requestfornumber(classesForMe);
                    else if (status.matches("free"))
                        basicFeatures.showFreePaidDialog(context);

                }

            }

            @Override
            public void onError() {

            }
        });


    }


    private void viewContactFun(Context context, String id) {
        String RealnumberURL = "https://api.gharpeshiksha.com" + "/GetClasses/getContact";
        Map<String, String> params = new HashMap<>();
        params.put("phone", sessionConfig.getPhone() + "");
        params.put("enq_id", id);
        Log.v("Classes---", params + "");
        if (volleyHelper == null) {
            volleyHelper = new VolleyHelper();
        }
        volleyHelper.VolleyPostRequest(context, RealnumberURL, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    final JSONObject part = new JSONObject(response);

                    String result = part.getString("result");
                    if (result.equals("success")) {
                        Log.v("Classes---", "student contacted" + part.getString("phone"));
                        String num = part.getString("phone");
                        Toast.makeText(context, "1 contact viewed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_DIAL); // opens up all available dialers
                        intent.setData(Uri.parse("tel:" + num)); // tel:  is required or else app will crash
                        context.startActivity(intent); // startActivity


                    } else {
                        if (result.matches("error")) {
                            if (part.has("time_left")) {
                                basicFeatures.AlertDialogFreeViews(context, part.getLong("time_left"));
                            } else {
                                try {
                                    new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                                            .setMessage(part.getString("message"))
                                            .setPositiveButton("Okay", null)
                                            .show();
                                } catch (Exception e) {
                                    Toast.makeText(context, "Unable to view this class", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    private void showMsgBeforecall(ClassesForMe classesForMe) {
        final ProgressDialog progressDialog1 = new ProgressDialog(context);
        progressDialog1.setCancelable(false);
        progressDialog1.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                progressDialog1.dismiss();
                return true;
            }

            return false;
        });
        progressDialog1.setMessage("Fetching data...");
        progressDialog1.show();
        ActiveDialog(classesForMe, progressDialog1);
    }

    private void requestfornumber(ClassesForMe classesForMe) {
        showMsgBeforecall(classesForMe);
    }


    private void feedBackDialog(final String enq_id, final TextView feedB) {

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
                    if (feedback.equals("Select an option")) {
                        Toast.makeText(context, "Please select an option", Toast.LENGTH_LONG).show();
                        feedB.setVisibility(View.VISIBLE);
                    } else {
                        feedB.setVisibility(View.GONE);
                        long phone = 0L;
                        try {
                            phone = sessionConfig.getPhone();
                        } catch(Exception e) {
                            Log.v("crash.java", e + "");
                        }


                        if (genRel.getVisibility() == View.VISIBLE) {

                            if (!(feedback_details.getText().toString()).equals("")) {

                                postdata("" + phone, enq_id, feedback, feedback_details.getText().toString(), "");
                                Toast.makeText(context, feedback_details.getText().toString(), Toast.LENGTH_LONG).show();
                            }
                            if (feedback_details.getText().toString().equals("")) {
                                Toast.makeText(context, "Field can't be empty!", Toast.LENGTH_LONG).show();
                            }
                        } else if (relativeLayout.getVisibility() == View.VISIBLE) {

                            String incorrect_info = info_spin.getSelectedItem().toString();
                            postdata("" + phone, enq_id, feedback, "", incorrect_info);


                        } else {

                            postdata("" + phone, enq_id, feedback, "", "");

                        }
                        // Toast.makeText(context, feedback, Toast.LENGTH_LONG).show();
                    }
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
                          final String incorrect_info) {


        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "https://api.gharpeshiksha.com" + "/GetClasses/setfeedback";

        requestQueue.start();

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

        }, error -> {

            Log.e(TAG, "onErrorResponse: " + error.getMessage());
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


    private void VolleyForFavourite(final Map<String, String> postFavDataMap, final String Text4Toast) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest favouriteRequest = new StringRequest(Request.Method.POST, favourite_action_URL, response -> Toast.makeText(context, Text4Toast, Toast.LENGTH_SHORT).show(), error -> Log.d("Server Error : ", "error : " + error.getMessage())) {

            @Override
            protected Map<String, String> getParams() {
                Log.e("checkPassedJSON", postFavDataMap.toString());
                return postFavDataMap;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        favouriteRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(favouriteRequest);
    }

    private void noNetworkDialog() {

        dialog = new Dialog(context);

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
        // return classesForMeList.size();
        return isShimmer ? shimmerNumber : classesForMeList.size();
    }

    static class EnquiriesViewHolder extends RecyclerView.ViewHolder {

        private TextView textMins, textViews, textName, textTutorReq, textBudget, textLoc, textDis, textCon, textclose, textcall, textFeedback,Chats_btn, Archived;
        private CardView cardView;
        private ImageView share, favourite, freeClass;
        private RelativeLayout highComp;
      //  private FrameLayout frameLayoutll;


        EnquiriesViewHolder(final View itemView) {
            super(itemView);

            //frameLayoutll = itemView.findViewById(R.id.fragment_2_holder);
            cardView = itemView.findViewById(R.id.enquiry_card);
            share = itemView.findViewById(R.id.share);
            favourite = itemView.findViewById(R.id.favourite);
            highComp = itemView.findViewById(R.id.highComp);
            textMins = itemView.findViewById(R.id.textMins);
            textViews = itemView.findViewById(R.id.textViews);
            textName = itemView.findViewById(R.id.textClaName);
            textTutorReq = itemView.findViewById(R.id.textClaTutorReq);
            textBudget = itemView.findViewById(R.id.textBudget);
            textLoc = itemView.findViewById(R.id.textLoc);
            textDis = itemView.findViewById(R.id.textDis);
            textCon = itemView.findViewById(R.id.textCon);
            textclose = itemView.findViewById(R.id.textConqqq);
            textcall = itemView.findViewById(R.id.textcall);
            textFeedback = itemView.findViewById(R.id.textFeed);
            freeClass = itemView.findViewById(R.id.freeClass);
            Chats_btn = itemView.findViewById(R.id.Chats_btn);
            //Archived = itemView.findViewById(R.id.Archived);
        }
    }

    private void intent(Class intentClass, ClassesForMe classesForMe, String view) {
        try {
            Intent intent = new Intent(context, intentClass);
            intent.putExtra("minDis", classesForMe.getTextDis());
            intent.putExtra("enqId", classesForMe.getTextEnq_id());
            intent.putExtra("viewContact", view);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.v("ClassesFor.java", e + "");
        }
    }

    private void ActiveDialog(final ClassesForMe classesForMe, final ProgressDialog prg) {
        Log.v("Classes---", tempId + ", " + sessionConfig.getPhone());
        String url = "https://api.gharpeshiksha.com" + "/Tutor/viewContactMsg";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {

                prg.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                if (TextUtils.equals(jsonObject.getString("status"), "success")) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                    alertDialog.setTitle("View Contact");

//                    alertDialog.setMessage(jsonObject.getString("message"));
                    String htmlMessage = null;
                    if(jsonObject.has("message")) {
                        htmlMessage = jsonObject.getString("message");
                    }
                    //Below Html.fromHtml(String stringDoc, flag) is work only for api lever 24(N) or above else use fromHtml() method without flag
                    //and if you want to render img tag image from url then you need to Picasso library to load image from url and convert it in
                    //drawable by extend ImageGetter subclass of Html class then you have to pass ImageGetter and TagHandler additional to fromHtml() params
                    //and if you want functioning anchor text fromHtml to TextView use TagHandler class instance and pass it in fromHtml() method params.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        alertDialog.setMessage(Html.fromHtml(htmlMessage, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        alertDialog.setMessage(htmlMessage);
                    }
                    alertDialog.setIcon(R.drawable.icon_phone);

                    alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                        viewContactFun(context, tempId);
                    });
                    alertDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());


                    alertDialog.show();


                } else if (TextUtils.equals(jsonObject.getString("status"), "error")) {

                    if (jsonObject.has("time_left")) {
                        basicFeatures.AlertDialogFreeViews(context, jsonObject.getLong("time_left"));
                    }
                }

            } catch (JSONException e) {

                prg.dismiss();
            }


        }, error -> {

            prg.dismiss();
            noNetworkDialog();

        }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("phone", "" + phone);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(postRequest);

    }
    public void navigateToFragment() {
        Dashboard dashboard = (Dashboard) context;
        dashboard.changeFrag(3, "NA", false, null);
    }
    public void Archived_api_Calling()
    {
        Toast.makeText(context, "this si Archived", Toast.LENGTH_SHORT).show();
    }

}