package com.gharpeshiksha.tutorapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Adapter.Students_chat;
import com.gharpeshiksha.tutorapp.BasicFeatures;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.UpgradeActivity;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.data_model.ClassesEnquiryModel;
import com.gharpeshiksha.tutorapp.data_model.SendMessage_Model;
import com.gharpeshiksha.tutorapp.data_model.Students_Chat_Models;
import com.gharpeshiksha.tutorapp.databinding.ActivityChatsAllStudentsBinding;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.services.ClientServerService;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.gharpeshiksha.tutorapp.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chats_all_StudentsActivity extends AppCompatActivity {

    List<Students_Chat_Models> students_chat_modelsList;
    private boolean isFirstChat = false, chatAccepted = false;
    SendMessage_Model sendMessage_modelArrayList;
    ActivityChatsAllStudentsBinding binding;
    Students_chat students_chatAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String TAG = "Chats_all_StudentsActivity.java";
    private String studentsUUID1;
    private String tutorName, enqId;
    private List<Students_Chat_Models> list;
    private String tutorUUID1, paymentStatus;
    SessionConfig sessionConfig;
    private BasicFeatures basicFeatures = new BasicFeatures();
    private ProgressDialog progressDialog;
    private ClientServerService clientServerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsAllStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            //swipeRefreshLayout = findViewById(R.id.)
            sessionConfig = new SessionConfig(Chats_all_StudentsActivity.this);
            bindService(new Intent(Chats_all_StudentsActivity.this, ClientServerService.class), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    clientServerService = ((ClientServerService.MyBinder) service).getBinder();
//                    clientServerService.print();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    //disconnected
                }
            }, Service.BIND_AUTO_CREATE);

            students_chat_modelsList = new ArrayList();
            binding.name12.setText(tutorName);
            //Getting Extras.
            String url1 = getIntent().getStringExtra("UrlPic");
            studentsUUID1 = getIntent().getStringExtra("studentsUUID1");
            tutorUUID1 = getIntent().getStringExtra("tutorUUID1");
            String tutorName = getIntent().getStringExtra("tutorname");
            enqId = getIntent().getStringExtra("enqId");

            Picasso.get().load(url1).placeholder(R.drawable.loogoo).placeholder(R.drawable.profile_placeholder).into(binding.image12);

            binding.name12.setText(tutorName);
            Log.d(TAG, "onCreate: params: " + studentsUUID1 + "\n" + tutorUUID1 + "\n" + tutorName + "\n" + enqId);
            binding.profileLV.setOnClickListener(view -> {
                startActivity(new Intent(getApplicationContext(), Chat_ViewProfile_Activity.class)
                        .putExtra("studentUUId", studentsUUID1)
                        .putExtra("tutorUUId", tutorUUID1)
                        .putExtra("enqId", enqId));
            });

            Log.d(TAG, "onCreate enqId: " + enqId);
            binding.SwrefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                    R.color.colorRed,
                    R.color.colorGreen,
                    R.color.grey);

            binding.SwrefreshLayout.setRefreshing(true);

//        binding.callingBtn.setOnClickListener(view -> {
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            String temp = "tel:";
//            intent.setData(Uri.parse(temp));
//            startActivity(intent);
//        });

            binding.back.setOnClickListener(view -> {
                super.onBackPressed();
            });

            binding.upgradeBtn.setOnClickListener(view -> {
                startActivity(new Intent(Chats_all_StudentsActivity.this, UpgradeActivity.class));
            });

            binding.chatsLive.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return false;
                }
            });

            progressDialog = new ProgressDialog(Chats_all_StudentsActivity.this);
            progressDialog.setMessage("Please wait...");

            checkStatus();

            /**Below is SpannableString to span particular part of string by change Foreground color
             * and make that clickable. */
            SpannableString span = new SpannableString(binding.upgradeTV.getText().toString());
            int start = binding.upgradeTV.getText().toString().indexOf("U");
            int end = binding.upgradeTV.getText().toString().length();
            span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            binding.upgradeTV.setText(span);
            binding.upgradeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Explicit intent message object give android to open current application component.
                    startActivity(new Intent(Chats_all_StudentsActivity.this, UpgradeActivity.class));
                }
            });
            Utility.hideKeyboard(Chats_all_StudentsActivity.this, Chats_all_StudentsActivity.this);
        } catch (Exception e) {
            Log.d(TAG, "onCreate: " + e);
        }
    }

    private void Send_Chates_Students() {
        //button use Send message
        binding.SendChatsBtn.setOnClickListener(view -> {
            String question = binding.chatsLive.getText().toString().trim();
            if (!question.isEmpty()) {
                //addToChat(question,Students_Chat_Models.SENT_BY_ME);
                Date d = new Date(System.currentTimeMillis());
                SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                String dateAndTime;
                dateAndTime = spf.format(d);
                if (isFirstChat) {
                    Log.d(TAG, "Send_Chates_Students: initiated " + isFirstChat);
                    initiateChat(question, dateAndTime, true);
                } else {
                    Log.d(TAG, "Send_Chates_Students: initiated " + isFirstChat);
                    addToChat(question, Students_Chat_Models.SENT_BY_ME, dateAndTime);
                    Send_Chats_Call_api(question);
                }
            } else {
                Log.d(TAG, "Send_Chates_Students: ");
            }
            binding.chatsLive.setText("");
        });
    }

    /**
     * Below method check status of user plan and if it is active then display a message in AlertDialog
     * and call api to deduct user View-Contact.
     */
    private void viewContact(String question, String dateAndTime) {
        basicFeatures.CheckPaymentStatus(Chats_all_StudentsActivity.this, new SessionConfig(this).getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {
                Log.d(TAG, "Result: " + status);
                ClassesEnquiryModel classesForMe = new ClassesEnquiryModel();
                if (Utility.isNetworkAvailable(Chats_all_StudentsActivity.this)) {
                    BasicFeatures basicFeatures = new BasicFeatures();
                    if (status.matches("active")) {
                        Log.v("Classes---", "active2");
                        showMsgBeforecall(classesForMe, question, dateAndTime);
                    } else if (status.matches("nonpaid") || status.matches("expired")) {
                        startActivity(new Intent(Chats_all_StudentsActivity.this, UpgradeActivity.class));
                        Log.v("Classes---", "expired");
                    } else if (status.matches("freeactivation"))
                        showMsgBeforecall(classesForMe, question, dateAndTime);
                    else if (status.matches("free"))
                        basicFeatures.showFreePaidDialog(Chats_all_StudentsActivity.this);
                } else {
                    noNetworkDialog(status, classesForMe, question, dateAndTime);
                }
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError: error");
            }
        });
    }

    protected void noNetworkDialog(String status, ClassesEnquiryModel classesForMe, String question, String dateAndTime) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.no_network_dialog);
        View retry = dialog.findViewById(R.id.retry);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        retry.setOnClickListener(v -> {
            dialog.dismiss();
            if (Utility.isNetworkAvailable(Chats_all_StudentsActivity.this)) {
                dialog.dismiss();
                if (!status.isEmpty()) {
                    BasicFeatures basicFeatures = new BasicFeatures();
                    if (status.matches("active")) {
                        Log.v("Classes---", "active2");
                        showMsgBeforecall(classesForMe, question, dateAndTime);
                    } else if (status.matches("nonpaid") || status.matches("expired")) {
                        startActivity(new Intent(Chats_all_StudentsActivity.this, UpgradeActivity.class));
                        Log.v("Classes---", "expired");
                    } else if (status.matches("freeactivation"))
                        showMsgBeforecall(classesForMe, question, dateAndTime);
                    else if (status.matches("free"))
                        basicFeatures.showFreePaidDialog(Chats_all_StudentsActivity.this);
                }
            } else {
                dialog.dismiss();
                noNetworkDialog(status, classesForMe, question, dateAndTime);
            }
        });

    }

    private void showMsgBeforecall(ClassesEnquiryModel classesForMe, String question, String dateAndTime) {
        final ProgressDialog progressDialog1 = new ProgressDialog(Chats_all_StudentsActivity.this);
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
        ActiveDialog(classesForMe, progressDialog1, question, dateAndTime);
    }


    private void ActiveDialog(final ClassesEnquiryModel classesForMe, final ProgressDialog prg, String question, String dateAndTime) {
//        Log.v("Classes---", tempId + ", " + sessionConfig.getPhone());
        String url = "https://api.gharpeshiksha.com" + "/Tutor/viewContactMsg";

        RequestQueue requestQueue = Volley.newRequestQueue(Chats_all_StudentsActivity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {

                prg.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                if (TextUtils.equals(jsonObject.getString("status"), "success")) {

                    androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Chats_all_StudentsActivity.this, R.style.AppCompatAlertDialogStyle);
                    alertDialog.setTitle("View Contact");

//                    alertDialog.setMessage(jsonObject.getString("message"));
                    String htmlMessage = null;
                    if (jsonObject.has("message")) {
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
                        viewContactFun(Chats_all_StudentsActivity.this, question, dateAndTime, enqId);
                        progressDialog.show();
                    });
                    alertDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());


                    alertDialog.show();


                } else if (TextUtils.equals(jsonObject.getString("status"), "error")) {

                    if (jsonObject.has("time_left")) {
                        new BasicFeatures().AlertDialogFreeViews(Chats_all_StudentsActivity.this, jsonObject.getLong("time_left"));
                    }
                }

            } catch (JSONException e) {

                prg.dismiss();
            }


        }, error -> {

            prg.dismiss();
//            noNetworkDialog();

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


    private void viewContactFun(Context context, String question, String dateAndTime, String id) {
        String RealnumberURL = "https://api.gharpeshiksha.com" + "/GetClasses/getContact";
        Map<String, String> params = new HashMap<>();
        params.put("phone", sessionConfig.getPhone() + "");
        params.put("enq_id", id);
        Log.v("Classes---", params + "");
        VolleyHelper volleyHelper = new VolleyHelper();
        volleyHelper.VolleyPostRequest(context, RealnumberURL, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    final JSONObject part = new JSONObject(response);
                    String result = part.getString("result");
                    if (result.equals("success")) {
                        if (chatAccepted) {
                            //This api only update isApproved when it is true and status also true
                            RetrofitClient.getClient().create(ApiServies.class).updateChatApproval(studentsUUID1, tutorUUID1, "accepted")
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            progressDialog.dismiss();
                                            binding.chatApprovalLL.setVisibility(View.GONE);
                                            if (response.isSuccessful() && response.body() != null) {
                                                try {
                                                    checkStatus();
                                                } catch (Exception e) {
                                                    Log.d(TAG, "onResponse: " + e);
                                                }
                                            } else {
                                                Log.d(TAG, "onResponse: " + response.errorBody().toString());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Log.d(TAG, "onFailure: " + t);
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            addToChat(question, Students_Chat_Models.SENT_BY_ME, dateAndTime);
                            Send_Chats_Call_api(question);
                            checkStatus();
                        }
                    } else {
                        progressDialog.dismiss();
                        if (result.matches("error")) {
                            if (part.has("time_left")) {
                                new BasicFeatures().AlertDialogFreeViews(context, part.getLong("time_left"));
                            } else {
                                try {
                                    new androidx.appcompat.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
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
                Log.d(TAG, "onError: " + error);
            }
        });
    }


    private void Send_Chats_Call_api(String question) {
        // students_chat_modelsList = new ArrayList<Students_Chat_Models>();
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.SendMessages("Tutor", question, studentsUUID1, tutorUUID1, enqId).enqueue(new Callback<SendMessage_Model>() {
            @Override
            public void onResponse(Call<SendMessage_Model> call, Response<SendMessage_Model> response) {
                Log.e(TAG, "onResponse: Send Message in === " + response.body());
                sendMessage_modelArrayList = response.body();
                Date d1 = new Date(System.currentTimeMillis());
                list.get(list.size() - 1).setSended(true);
                students_chatAdapter.notifyDataSetChanged();
                binding.recycleView.scrollToPosition(list.size() - 1);
            }

            @Override
            public void onFailure(Call<SendMessage_Model> call, Throwable t) {
                Log.d(TAG, "onFailure: send" + t.getMessage());
            }
        });

    }

    void addToChat(String message, String sentBy, String timestamp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    list.add(new Students_Chat_Models(message, timestamp, sentBy, false, "accepted"));
                    students_chatAdapter.notifyDataSetChanged();
                    binding.recycleView.smoothScrollToPosition(students_chatAdapter.getItemCount());
                } catch (Exception e) {
                    Log.e(TAG, "run: " + e);
                }

            }
        });
    }

    private void Open_Chats_Call_api() {
        students_chat_modelsList = new ArrayList<>();
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        Log.d(TAG, "Open_Chats_Call_api: " + studentsUUID1 + ", " + tutorUUID1);
        apiServies.ChatOpen(studentsUUID1, tutorUUID1).enqueue(new Callback<List<Students_Chat_Models>>() {
            @Override
            public void onResponse(Call<List<Students_Chat_Models>> call, Response<List<Students_Chat_Models>> response) {
                Log.e(TAG, "onResponse: Open Chate == " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        students_chat_modelsList = response.body();
                        binding.SwrefreshLayout.setRefreshing(false);
                        binding.chatsLive.setEnabled(false);
                        list = new ArrayList<>();
                        if (students_chat_modelsList.isEmpty()) {
                            binding.chatsLive.setEnabled(true);
                            binding.pendingCard.setVisibility(View.GONE);
                            binding.chatLL.setVisibility(View.VISIBLE);
                            isFirstChat = true;
                            Send_Chates_Students();
                            students_chatAdapter = new Students_chat(getApplicationContext(), list);
                            binding.recycleView.setAdapter(students_chatAdapter);
                            LinearLayoutManager llm = new LinearLayoutManager(Chats_all_StudentsActivity.this);
                            llm.setStackFromEnd(true);
                            binding.recycleView.setLayoutManager(llm);
                            binding.recycleView.setVisibility(View.VISIBLE);
                            binding.upgradeLayout.setVisibility(View.GONE);
                            binding.welcome.setVisibility(View.GONE);
                        } else {
                            String status = students_chat_modelsList.get(students_chat_modelsList.size() - 1).getIsApproved();
                            Log.d(TAG, "onResponse: status " + status);
                            if (status.matches("chatInitiated")) {
                                binding.upgradeLayout.setVisibility(View.VISIBLE);
                                binding.recycleView.setVisibility(View.GONE);
                                binding.unreadRL.setVisibility(View.GONE);
                                binding.upgradeCard.setVisibility(View.GONE);
                                binding.pendingCard.setVisibility(View.VISIBLE);
                                binding.chatLL.setVisibility(View.GONE);
                                if (students_chat_modelsList.get(students_chat_modelsList.size() - 1)
                                        .getSendBy().toLowerCase().matches("student")) {
                                    binding.chatApprovalLL.setVisibility(View.VISIBLE);
                                }
                            } else if (status.matches("declined")) {
                                Log.d(TAG, "onResponse: declined" + students_chat_modelsList.get(0).getSendBy());
                                if (students_chat_modelsList.get(0)
                                        .getSendBy().toLowerCase().matches("tutor")) {
                                    binding.statusTV.setText("Chat declined by student.");
                                    binding.pendingCard.setVisibility(View.GONE);
                                    binding.chatLL.setVisibility(View.GONE);
                                    binding.chatApprovalLL.setVisibility(View.GONE);
                                } else {
                                    binding.pendingCard.setVisibility(View.VISIBLE);
                                    binding.chatLL.setVisibility(View.GONE);
                                    binding.chatApprovalLL.setVisibility(View.VISIBLE);
                                }
                                binding.upgradeLayout.setVisibility(View.VISIBLE);
                                binding.recycleView.setVisibility(View.GONE);
                                binding.unreadRL.setVisibility(View.GONE);
                                binding.upgradeCard.setVisibility(View.GONE);
                                binding.declineBtn.setVisibility(View.GONE);
                                binding.viewDevider.setVisibility(View.GONE);
                            } else {
                                binding.chatsLive.setEnabled(true);
                                binding.pendingCard.setVisibility(View.GONE);
                                binding.chatLL.setVisibility(View.VISIBLE);
                                Send_Chates_Students();
                                Log.d(TAG, "onResponse: " + students_chat_modelsList);
                                for (int i = students_chat_modelsList.size() - 1; i >= 0; i--) {
                                    students_chat_modelsList.get(i).setSended(true);
                                    list.add(students_chat_modelsList.get(i));
                                    Log.d(TAG, "onResponse == " + students_chat_modelsList.get(0).getMessage() + "\n " +
                                            "second = " + students_chat_modelsList.get(0).getSendBy());
                                }

                                students_chatAdapter = new Students_chat(getApplicationContext(), list);
                                binding.recycleView.setAdapter(students_chatAdapter);
                                LinearLayoutManager llm = new LinearLayoutManager(Chats_all_StudentsActivity.this);
                                llm.setStackFromEnd(true);
                                binding.recycleView.setLayoutManager(llm);
                                binding.recycleView.setVisibility(View.VISIBLE);
                                binding.upgradeLayout.setVisibility(View.GONE);
                                binding.welcome.setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onCatch: " + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Students_Chat_Models>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        Swipi_Refresh_api();
    }

    public void Swipi_Refresh_api() {
        binding.SwrefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("Fav ", "onRefresh called from SwipeRefreshLayout");
                        checkStatus();
//                        Open_Chats_Call_api();
//                        Send_Chates_Students();
                        binding.SwrefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    /**
     * Below method check user paid or non-paid status.
     */
    private void checkStatus() {
        sessionConfig = new SessionConfig(Chats_all_StudentsActivity.this);
        basicFeatures.CheckPaymentStatus(Chats_all_StudentsActivity.this, sessionConfig.getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {
                paymentStatus = status;
                binding.acceptCard.setVisibility(View.GONE);
                binding.chatApprovalLL.setVisibility(View.GONE);
                if (status.matches("active")) {
                    //Fetch data Method
                    binding.acceptCard.setVisibility(View.VISIBLE);
                    binding.acceptBtn.setOnClickListener(view -> {
                        progressDialog.show();
                        initiateChat("", "", false);
                    });

                    binding.declineBtn.setOnClickListener(view -> {
                        RetrofitClient.getClient().create(ApiServies.class)
                                .updateChatApproval(studentsUUID1, tutorUUID1, "declined")
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast.makeText(Chats_all_StudentsActivity.this, "Chat Declined Successfully!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.d(TAG, "onFailure: " + t);
                                    }
                                });
                        binding.chatApprovalLL.setVisibility(View.GONE);
                    });

                    Open_Chats_Call_api();
                    Log.v("Classes---", "active");
                } else if (status.matches("nonpaid") || status.matches("expired")) {
                    binding.SwrefreshLayout.setRefreshing(false);
                    binding.upgradeLayout.setVisibility(View.VISIBLE);
                    binding.recycleView.setVisibility(View.GONE);
                    Utility.hideKeyboard(Chats_all_StudentsActivity.this, Chats_all_StudentsActivity.this);
                } else if (status.matches("free")) {
                    binding.SwrefreshLayout.setRefreshing(false);
                    binding.upgradeLayout.setVisibility(View.VISIBLE);
                    binding.recycleView.setVisibility(View.GONE);
                } else if (status.matches("freeactivation")) {
                    binding.SwrefreshLayout.setRefreshing(false);
                    binding.upgradeLayout.setVisibility(View.VISIBLE);
                    binding.recycleView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError: api error");
            }
        });
    }

    private void initiateChat(String question, String dateAndTime, boolean isSend) {
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(Chats_all_StudentsActivity.this);
        builder.setMessage("To Initiate this chat one contact will be viewed");
        builder.setPositiveButton("View Contact", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichType) {
                if (!isSend) {
                    Log.d(TAG, "onClick initiate: " + isSend);
                    chatAccepted = true;
                    viewContact("", "");
                } else {
                    Log.d(TAG, "onClick initiate: " + isSend);
                    chatAccepted = false;
                    viewContact(question, dateAndTime);
                }
            }
        });

        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        binding.chatsLive.setEnabled(false);
        super.onBackPressed();
    }
}
