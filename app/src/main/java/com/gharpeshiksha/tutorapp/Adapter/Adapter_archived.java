package com.gharpeshiksha.tutorapp.Adapter;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.BasicFeatures;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.UpgradeActivity;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.activities.Chats_all_StudentsActivity;
import com.gharpeshiksha.tutorapp.activities.ClassesForMeActivity;
import com.gharpeshiksha.tutorapp.data_model.ClassesForMe;
import com.gharpeshiksha.tutorapp.data_model.Model_archived;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Adapter_archived extends ListAdapter<Model_archived, Adapter_archived.Viewholder> {

    private Context context;
    private List<Model_archived> adapter_archivedList;
    private SessionConfig sessionConfig;
    String tempId;
    private BasicFeatures basicFeatures = new BasicFeatures();
    String favourite_action_URL = "https://api.gharpeshiksha.com" + "/GetClasses/favorite";
    String studentsUUId;
    String tutorUUId;
    String tutorName;
    String payment;
    String TAG = "Adapter_archived.java";
    String imgUrl;

    public Adapter_archived(Context context) {
        super( new DiffUtil.ItemCallback<Model_archived>() {
            @Override
            public boolean areItemsTheSame(Model_archived oldItem, Model_archived newItem) {
                return oldItem.getEnq_id() == newItem.getEnq_id();
            }

            @Override
            public boolean areContentsTheSame( Model_archived oldItem, Model_archived newItem) {
                return oldItem.toString().equals(newItem.toString());
            }
        });
        this.context = context;
        this.adapter_archivedList = getCurrentList();
        sessionConfig = new SessionConfig(context);
    }

    @NonNull
    @Override
    public Adapter_archived.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.archived_layout_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_archived.Viewholder holder, int position) {
        Model_archived model = getItem(position);
        studentsUUId = String.valueOf(model.getStudentUUID());
        tutorUUId = String.valueOf(model.getTutorUUID());
        tutorName = model.getName();

        holder.time.setText(model.getTime());
        holder.distance.setText(model.getDistance() + " Km");
        holder.textCon1.setText(model.getTutors_contacted() + " Tutors Contacted");
//        holder.viewContactedTv.setText(model.getTutors_contacted() + "");
        holder.area.setText(model.getArea());
        holder.budget.setText(model.getBudget());
        holder.name.setText(model.getName());
        String title = "Tutor Requirement For " + model.getSubjects() +" For " + model.getClassfor();
        holder.clase.setText(title);

        holder.cardView.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                intent(ClassesForMeActivity.class, model, "noView");
            } else {
                Toast.makeText(context, "Turn on Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        if(TextUtils.equals(model.getFavorite(), "true")) {
            holder.favoriteImg.setImageResource(R.drawable.favourite_true);
        }

        Map<String, String> postFavDataMap = new HashMap<>();
        holder.favoriteImg.setOnClickListener(v -> {
            if (TextUtils.equals(model.getFavorite(), "true")) {
                holder.favoriteImg.setImageResource(R.drawable.favourite_false);
                postFavDataMap.put("phone", "" +sessionConfig.getPhone());
                postFavDataMap.put("enq_id", "" + model.getEnq_id());
                postFavDataMap.put("action", "remove");
                String Text4Toast = "Removed from favourite";
                VolleyForFavourite(postFavDataMap, Text4Toast);
            } else {
                holder.favoriteImg.setImageResource(R.drawable.favourite_true);
                postFavDataMap.put("phone", "" + sessionConfig.getPhone());
                postFavDataMap.put("enq_id", "" + model.getEnq_id());
                postFavDataMap.put("action", "add");
                String Text4Toast = "Added to favourite";
                VolleyForFavourite(postFavDataMap, Text4Toast);
            }
        });
        holder.viewContactedTv.setOnClickListener(view -> {
            if (isNetworkAvailable()) {
                tempId = model.getEnq_id() + "";
                checkStatus("textcon", model);
            } else {
                noNetworkDialog();
            }

        });

        if(model.getEnq_viewed()) {
            holder.textCon1.setVisibility(View.GONE);
            holder.textCallBtn.setVisibility(View.VISIBLE);
            holder.textCallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RetrofitClient.getClient().create(ApiServies.class)
                            .getContactNum(sessionConfig.getPhone() + "", model.getEnq_id() + "")
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.isSuccessful() && response.body() != null) {
                                        try {
                                            Intent i = new Intent(Intent.ACTION_DIAL);
                                            i.setData(Uri.parse(new JSONObject(response.body().string()).getString("contact")));
                                            context.startActivity(i);
                                        } catch (Exception e) {
                                            Log.d(TAG, "onResponse: " + e);
                                        }}
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d(TAG, "onFailure: " + t);
                                }
                            });
                }
            });
        }

        clickButtonsMethods(holder, model);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView share, freeclass, favoriteImg;
        CardView cardView;
        RelativeLayout highcomp;
        TextView area, distance, subject, enq_id, op_count, name, payment, tutors_contacts, clase,
                budget, status, time, chate_archived, textCon1, viewContactedTv, textCallBtn;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            clase = itemView.findViewById(R.id.textClaTutorReq1);
            name = itemView.findViewById(R.id.textClaName1);
            budget = itemView.findViewById(R.id.textBudget1);
            area = itemView.findViewById(R.id.textLoc1);
            viewContactedTv = itemView.findViewById(R.id.textCon1);
            cardView = itemView.findViewById(R.id.enquiry_card1);
            distance = itemView.findViewById(R.id.textDis1);
            time = itemView.findViewById(R.id.textMins1);
            subject = itemView.findViewById(R.id.Subject);
            enq_id = itemView.findViewById(R.id.enquiryid);
            share = itemView.findViewById(R.id.share1);
            chate_archived = itemView.findViewById(R.id.Chats_btn12);
            textCon1 = itemView.findViewById(R.id.textViews1);
            favoriteImg = itemView.findViewById(R.id.favourite1);
            textCallBtn = itemView.findViewById(R.id.textcall1);
        }
    }

    public void clickButtonsMethods(Viewholder holder, Model_archived model) {
        holder.share.setOnClickListener(view -> {
            Toast.makeText(context, "this is ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "I suggest this app for you : https://play.google.com/store/apps/details?id=com.android.chrome");
            intent.setType("text/plain");
            context.startActivity(intent);

        });
        holder.chate_archived.setOnClickListener(view -> {
            Intent intent = new Intent(context, Chats_all_StudentsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("studentsUUID1", model.getStudentUUID());
            intent.putExtra("tutorUUID1", model.getTutorUUID());
            intent.putExtra("tutorname", model.getName());
            intent.putExtra("enqId", model.getEnq_id() + "");
            Log.d(TAG, "clickButtonsMethods: enqId" + model.getEnq_id());
            context.startActivity(intent);
        });
        holder.textCon1.setOnClickListener(view -> {

        });
    }


    private void checkStatus(String type, Model_archived model) {

//        Log.v("Classes---", "status");
        sessionConfig = new SessionConfig(context);

        basicFeatures.CheckPaymentStatus(context, sessionConfig.getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {

                if (type.matches("textcall")) {
                    if (status.matches("active") || status.matches("expired")){
                        Log.v("Classes---", "active");
                        intent(ClassesForMeActivity.class, model, "enq_viewed");
                    }
                    else if (status.matches("nonpaid"))
                        context.startActivity(new Intent(context, UpgradeActivity.class));
                    else if (status.matches("free"))
                        basicFeatures.showFreePaidDialog(context);
                    else if (status.matches("freeactivation"))
                        intent(ClassesForMeActivity.class, model, "calldirect");
                    /*requestfornumber(classesForMe);*/

                } else if (type.matches("textcon")) {
                    if (status.matches("active")) {
                        Log.v("Classes---", "active2");
                        showMsgBeforecall(model);
                    }
                    else if (status.matches("nonpaid") || status.matches("expired"))
                        context.startActivity(new Intent(context, UpgradeActivity.class));
                    else if (status.matches("freeactivation"))
                        requestfornumber(model);
                    else if (status.matches("free"))
                        basicFeatures.showFreePaidDialog(context);

                }

            }

            @Override
            public void onError() {

            }
        });


    }

    private void requestfornumber(Model_archived classesForMe) {
        showMsgBeforecall(classesForMe);
    }

    private void showMsgBeforecall(Model_archived classesForMe) {
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


    private void ActiveDialog(final Model_archived classesForMe, final ProgressDialog prg) {
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

                    String htmlMessage = null;
                    if(jsonObject.has("message")) {
                        htmlMessage = jsonObject.getString("message");
                    }
                    //Below Html.fromHtml(String stringDoc, flag) is work only for api level 24(N) or above else use fromHtml() method without flag
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

                params.put("phone", "" + sessionConfig.getPhone());

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

    VolleyHelper volleyHelper;
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

    private void intent(Class intentClass, Model_archived archivedCls, String view) {
        try {
            Intent intent = new Intent(context, intentClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("minDis", archivedCls.getDistance());
            intent.putExtra("enqId", archivedCls.getEnq_id());
            intent.putExtra("viewContact", view);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.v("ClassesFor.java", e + "");
        }
    }

    private void noNetworkDialog() {
        AlertDialog dialog;
        View view = LayoutInflater.from(context).inflate(R.layout.no_network_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        dialog = builder.create();
        Button retry = view.findViewById(R.id.retry);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    dialog.dismiss();
                } else {
                    noNetworkDialog();
                }
            }
        });
        dialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
