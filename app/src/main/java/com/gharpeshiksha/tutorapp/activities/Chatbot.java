package com.gharpeshiksha.tutorapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.MyFeedbackActivity;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.UploadDocuments;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Chatbot extends AppCompatActivity {

    LinearLayout chatLayout;
    LinearLayout suggestionLayout;
    RelativeLayout chatLayoutRel;
    SessionConfig sessionConfig;
    String phone;
    NestedScrollView scrollview, suggestionScrollView;
    String accessToken = "ya29.GltiBxj0NshjGnOgQlVWfLwSvTHxbvXEHIhiNae-bQw1CMGpftrM-rbP5GVCEBoR4wPHvSGLlH_3fwunMTjx6q-xHgiUhVDKkhVbMwoRH4S30wYsC26HU3Y2WG5q";
    String baseURL = "https://dialogflow.googleapis.com/v2beta1/projects/gharpeshiksha-first/agent/sessions/123456789:detectIntent";
    String dataToSend;
    /*"https://13-dot-gpschat.appspot.com/DialogFlowRequestApi/Welcome"*/
    String ApiUrl = "https://api.gharpeshiksha.com/Chatbot/Welcome";
    /*"https://13-dot-gpschat.appspot.com/DialogFlowRequestApi/ChatBotPreviousConversation"*/
    String previousChatURL = "https://api.gharpeshiksha.com/Chatbot/ChatBotPreviousConversation";
    String sendQueryURL = "https://api.gharpeshiksha.com/Chatbot/tutorComplaint";
    String startResponse = "none";
    String previousValue = "none";
    String returnFormat = "none";
    String QueryType = "Complaint";
    TextView NA;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    StringRequest postRequest;
    Button typingSarthi;
    /*HttpsURLConnection connection;*/
    /*int responseCode = 0;*/
    JSONObject mainGlobal;
    int page = 0;
    String currDate = "Today";
    boolean oldDataAvailable = true;
    ProgressBar loadPreviousChat;
    TextView loadPrevious, goToBottom;
    View view;
    int diff = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        chatLayout = findViewById(R.id.chatLayout);
        chatLayoutRel = findViewById(R.id.chatLayoutRel);
        suggestionLayout = findViewById(R.id.suggestionLayout);
        scrollview = findViewById(R.id.scrollview_chat);
        suggestionScrollView = findViewById(R.id.scrollforsugeestion);
        sessionConfig = new SessionConfig(Chatbot.this);
        phone = String.valueOf(sessionConfig.getPhone());
        dataToSend = "Phone Number is " + phone;
        NA = findViewById(R.id.notAvailableChatbot);
        NA.setVisibility(View.VISIBLE);
        typingSarthi = findViewById(R.id.typing);
        requestQueue = Volley.newRequestQueue(Chatbot.this);
        progressDialog = new ProgressDialog(Chatbot.this);
        progressDialog.setMessage("Booting ChatBot...");
        progressDialog.show();
        loadPreviousChat = findViewById(R.id.loadPreviousChat);
        loadPrevious = findViewById(R.id.loadChats);
        loadPrevious.setVisibility(View.GONE);
        goToBottom = findViewById(R.id.bottomChats);
        goToBottom.setVisibility(View.GONE);

        scrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                Log.i("ChatScrollPosition", "onScrollChange: X = " + i + "\nY = " + i1 + "\nOld X = " + i2 + "\nOld Y = " + i3);
                if (oldDataAvailable) {
                    if (i1 == 0) {
                        Log.d("PreviousChat", "Start Previous Chat  ");
                        loadPrevious.setVisibility(View.VISIBLE);
                    } else {
                        loadPrevious.setVisibility(View.GONE);
                    }
                }
                view = (View) scrollview.getChildAt(scrollview.getChildCount() - 1);
                diff = (view.getBottom() - (scrollview.getHeight() + scrollview.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    // do stuff
                    Log.d("ScrollToBed", "Reached Bottom");
                    goToBottom.setVisibility(View.GONE);
                } else {
                    goToBottom.setVisibility(View.VISIBLE);
                }
            }
        });

        loadPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPrevious.setVisibility(View.GONE);
                getPreviousChat();
            }
        });
        goToBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        StartVolley();
    }

    private void getPreviousChat() {
        loadPreviousChat.setVisibility(View.VISIBLE);
        postRequest = new StringRequest(Request.Method.POST, previousChatURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PreviousChat", "Response: " + response);
                try {
                    JSONArray main = new JSONArray(response);
                    if (main.length() == 0) {
                        oldDataAvailable = false;
                        setDateforChat(currDate);
                    }
                    for (int i = 0; i < main.length(); i++) {
                        JSONObject part = new JSONObject(main.get(i).toString());
                        currDate = part.getString("Date");
                        if (part.getString("Messenger").equals("User")) {
                            setUserDataAtZero(part.getString("Message"));
                        } else if (part.getString("Messenger").equals("ChatBot")) {
                            setBotDataAtZero(part.getString("Message"));
                        }
                        if (i <= main.length() - 2) {
                            if (!currDate.equals(new JSONObject(main.get(i + 1).toString()).getString("Date"))) {
                                setDateforChat(currDate);
                                currDate = new JSONObject(main.get(i + 1).toString()).getString("Date");
                            }
                        }
                    }
                    loadPreviousChat.setVisibility(View.GONE);

                    page++;

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NA.setVisibility(View.VISIBLE);
                Log.d("PreviousChat", "VolleyError: " + error);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phoneNumber", phone);
                params.put("page", String.valueOf(page));
                Log.d("PreviousChat", "Params: " + params);
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

    private void setDateforChat(String text) {
        TextView button = new TextView(this);
        button.setBackground(getResources().getDrawable(R.drawable.chatbot_date_background));
        try {
            button.setText(Html.fromHtml(text));
        } catch (Exception e) {
            button.setText(text);
        }
        button.setAllCaps(false);
        button.setTextSize(15);
        button.setPadding(10, 10, 10, 10);
        button.setTextColor(getResources().getColor(R.color.black));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 20, 0, 20);
        params.gravity = Gravity.CENTER;

        chatLayout.addView(button, 0, params);
        //chatLayout.setGravity(Gravity.START);
    }

    private void setBotDataAtZero(String text) {
        Button button = new Button(this);
        button.setBackground(getResources().getDrawable(R.drawable.bot_response_background));
        try {
            button.setText(Html.fromHtml(text));
        } catch (Exception e) {
            button.setText(text);
        }
        button.setAllCaps(false);
        button.setTextSize(15);
        button.setPadding(15, 5, 15, 5);
        button.setTextColor(getResources().getColor(R.color.black));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 20, 200, 20);
        params.gravity = Gravity.START;
        chatLayout.addView(button, 0, params);
        //chatLayout.setGravity(Gravity.START);
    }

    private void setUserDataAtZero(String text) {
        Button button = new Button(this);
        button.setBackground(getResources().getDrawable(R.drawable.user_response_background));
        try {
            button.setText(Html.fromHtml(text));
        } catch (Exception e) {
            button.setText(text);
        }
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setPadding(15, 5, 15, 5);
        button.setTextColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(200, 20, 2, 20);
        params.gravity = Gravity.END;
        chatLayout.addView(button, 0, params);
        //chatLayout.setGravity(Gravity.START);
    }

    public void setUserdata(String text) {
        Button button = new Button(this);
        button.setBackground(getResources().getDrawable(R.drawable.user_response_background));
        try {
            button.setText(Html.fromHtml(text));
        } catch (Exception e) {
            button.setText(text);
        }
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setPadding(15, 5, 15, 5);
        button.setTextColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(200, 20, 2, 20);
        params.gravity = Gravity.END;
        chatLayout.addView(button, params);
        //chatLayout.setGravity(Gravity.START);

        typingSarthi.setVisibility(View.VISIBLE);
        // chatLayoutRel.scrollTo(0, 0);
        scrollview.fullScroll(ScrollView.FOCUS_DOWN);

    }

    public void setBotdata(String text) {
        //String textJ = "<p align=\"justify\">" + text + "</p>";
        Button button = new Button(this);
        button.setMovementMethod(LinkMovementMethod.getInstance());
        //Linkify.addLinks(button,Linkify.WEB_URLS);
        //button.setLinksClickable(true);

        button.setBackground(getResources().getDrawable(R.drawable.bot_response_background));
        try {
            button.setText(Html.fromHtml(text));
        } catch (Exception e) {
            button.setText(text);
        }
        button.setAllCaps(false);
        button.setTextSize(15);
        button.setPadding(15, 5, 15, 5);
        button.setTextColor(getResources().getColor(R.color.black));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 20, 200, 20);
        params.gravity = Gravity.START;
        chatLayout.addView(button, params);
        //chatLayout.setGravity(Gravity.START);
        //chatLayoutRel.scrollTo(0, 0);
        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void setSuggestiondata(String text, final int size) {
        final Button button = new Button(this);
        button.setBackground(getResources().getDrawable(R.drawable.suggestion_background_bubble));
        try {
            button.setText(Html.fromHtml(text));
        } catch (Exception e) {
            button.setText(text);
        }
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setPadding(10, 10, 10, 10);
        button.setTextColor(getResources().getColor(R.color.colorPrimaryDarker));

        /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(50, 20, 50, 20);*/

       /* suggestionLayout.addView(button, params);
        suggestionLayout.setGravity(Gravity.START);*/

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 5, 200, 5);
        params.gravity = Gravity.START;

        chatLayout.addView(button, params);
        scrollview.fullScroll(ScrollView.FOCUS_DOWN);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("RemoveSuggestions", "Total Count: " + chatLayout.getChildCount() + " Suggestion Size : " + size);

                int childCount = chatLayout.getChildCount();
                for (int i = childCount - 1; i > childCount - 1 - size; i--) {
                    Log.d("RemoveSuggestions", "Total Count: " + childCount + "\nRemoving At : " + (i) + "\nRemoving Till : " + (childCount - 1 - size));
                    chatLayout.removeViewAt(i);
                }
                if (button.getText().toString().equals("Back to Previous Menu")) {
                    dataToSend = String.format("Previous Value is %s %s", previousValue, phone);
                } else {

                    if (returnFormat.equals("Basic")) {
                        dataToSend = button.getText().toString() + "" + phone;
                    } else if (returnFormat.equals("Normal")) {
                        dataToSend = startResponse + " " + button.getText().toString() + " " + phone;
                    } else if (returnFormat.equals("Enquiry")) {
                        String[] helper = button.getText().toString().split(" ");
                        dataToSend = helper[0] + " " + startResponse + " " + phone;
                    } else if (returnFormat.equals("none")) {
                        dataToSend = button.getText().toString() + " " + phone;
                    }
                }

                StartVolley();
                setUserdata(button.getText().toString());
                scrollview.smoothScrollTo(0, chatLayout.getBottom());
            }
        });
    }

    private void sendQueryToVolley(final String query, final String queryType) {
        try {
            postRequest = new StringRequest(Request.Method.POST, sendQueryURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("QueryChatBot", "onResponse: " + response );
                    try {
                        if (response.equals("Success")) {
                            new AlertDialog.Builder(Chatbot.this, R.style.AppCompatAlertDialogStyle)
                                    .setMessage("Your query has been successfully submitted, we will resolve the issue as soon as possible.")
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onBackPressed();
                                        }
                                    })
                                    .show();
                            Toast.makeText(Chatbot.this, "Your Query has been successfully submitted.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.d("Query", "Exception Caused " + e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Contact", phone);
                    params.put("Message", query);
                    params.put("MessageType", queryType);
                    Log.d("QueryChatBot", "Params: " + params);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            requestQueue.add(postRequest);
        } catch (Exception e){
            Log.e("QueryChatBotError", "sendQueryToVolley: ",e );
        }
    }

    public void StartVolley() {

        postRequest = new StringRequest(Request.Method.POST, ApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println(response.length());
                Log.d("ChatBot", "VolleyResponse: " + response);
                try {
                    if (response.length() != 0) {
                        Log.e("ChatBot", "onResponse: ");
                        NA.setVisibility(View.GONE);

                       /* if (responseCode != 0) {
                            connection.disconnect();
                        }*/

                    } else {
                        /*URL url = new URL("https://14-dot-gpschat.appspot.com/access");
                        connection = (HttpsURLConnection) url.openConnection();
                        connection.connect();
                        responseCode = connection.getResponseCode();*/
                    }
                } catch (Exception e) {

                }

                try {

                    typingSarthi.setVisibility(View.GONE);
                    JSONObject main = new JSONObject(response);

                    mainGlobal = main;

                    /*if (main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").has("Status")){
                        if (main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("Status").equals("End")){
                            new BasicFeature.Builder(Chatbot.this, R.style.AppCompatAlertDialogStyle)
                                    .setMessage("Chat Has Ended, Sathi has left.")
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                    }*/

                    if (main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").has("Intent")) {
                        String type = main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("Intent");
                        Log.e("ChatBot", "onResponse: " + type);
                        if (type.equals("Class")) {
                            showAlertDialog("Please Select Classes of Your Choice", ClassesActivity.class);
                            //startActivity(new Intent(Chatbot.this, ClassesActivity.class));
                        } else if (type.equals("Location")) {
                            showAlertDialog("Please Select Location of Your Choice", AreaRangeSelection.class);
                            //startActivity(new Intent(Chatbot.this, AreaRangeSelection.class));
                        } else if (type.equals("Feedback")) {
                            new AlertDialog.Builder(Chatbot.this, R.style.AppCompatAlertDialogStyle)
                                    .setMessage("Please Give Feedback For The Selected Class")
                                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                startActivity(new Intent(Chatbot.this, MyFeedbackActivity.class)
                                                        .putExtra("enqId", mainGlobal.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("EnquiryId"))
                                                        .putExtra("contact", mainGlobal.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("Contact"))
                                                        .putExtra("feedback_given", "false")
                                                        .putExtra("our_feedback", "")
                                                        .putExtra("from", "chatBot")
                                                        .putExtra("my_feedback", "")
                                                        .putExtra("viewed_on", mainGlobal.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("Viewed On"))
                                                        .putExtra("posted_on", mainGlobal.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("Posted On")));

                                            } catch (JSONException e) {
                                                Log.e("IntentChatBot", "onClick: " + e);
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();

                            Log.e("ChatBot", "onIntent: " + main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("EnquiryId"));
                            Log.e("ChatBot", "onIntent: " + main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("Contact"));
                            Log.e("ChatBot", "onIntent: " + main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("Viewed On"));
                            Log.e("ChatBot", "onIntent: " + main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("Posted On"));
                        } else if (type.equals("Document")) {
                            //showAlertDialog("Please Upload Your Documents", UploadDocuments.class);
                            //startActivity(new Intent(Chatbot.this, UploadDocuments.class));
                            new AlertDialog.Builder(Chatbot.this, R.style.AppCompatAlertDialogStyle)
                                    .setMessage("Please Upload Your Documents")
                                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Chatbot.this, UploadDocuments.class)
                                                    .putExtra("type", "paid")
                                                    .putExtra("from", "chatBot"));
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        } else if (type.equals("End")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        new AlertDialog.Builder(getApplicationContext(), R.style.AppCompatAlertDialogStyle)
                                                .setMessage("Chat Has Ended, Saathi has left.")
                                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();
                                    } catch (Exception e) {
                                        Log.e("ChatBotExitError", "run: ", e);
                                    }
                                }
                            }, 5000);
                        } else if (type.equals("Call")) {
                            new AlertDialog.Builder(Chatbot.this, R.style.AppCompatAlertDialogStyle)
                                    .setMessage("Contact our customer service agent")
                                    .setPositiveButton("Call now", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:7065806565")));
                                            //finish();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //finish();
                                        }
                                    })
                                    //.setCancelable(false)
                                    .show();
                        } else if (type.equals("Profile")) {
                            showAlertDialog("Do you want to Proceed to make change in your profile ?", EditProfileActivity.class);
                            //startActivity(new Intent(Chatbot.this, AreaRangeSelection.class));
                        } else if (type.equals("Query")) {
                            Log.v("Chatbot.java", "query1");
                            if (main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").has("QueryType")) {
                                QueryType = main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("QueryType");
                            }
                            LayoutInflater inflater = LayoutInflater.from(Chatbot.this);
                            View view = inflater.inflate(R.layout.edittext_alertdialog, null);
                            final EditText editText = view.findViewById(R.id.edittext1);
                            try {
                                new AlertDialog.Builder(Chatbot.this, R.style.AppCompatAlertDialogStyle)
                                        .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                sendQueryToVolley(editText.getText().toString(), QueryType);
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                onBackPressed();
                                            }
                                        })
                                        .setView(view)
                                        .show();
                            } catch (Exception e) {
                                Log.v("Chatbot.java", e + "");
                            }

                        }
                    }

                    Log.d("ChatBot", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");

                    if (main.getJSONObject("queryResult").has("fulfillmentText")) {
                        String stuff = main.getJSONObject("queryResult").getString("fulfillmentText").toString();
                        setBotdata(stuff);

                        Log.d("ChatBot", "Response:+++++ " + stuff);
                    }

                    Log.d("ChatBot", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");

                    if (main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getJSONObject("richResponse").has("suggestions")) {
                        JSONArray options = main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getJSONObject("richResponse").getJSONArray("suggestions");
                        suggestionLayout.removeAllViews();
                        if (options.length() == 0) {
                            suggestionLayout.setVisibility(View.GONE);
                            suggestionScrollView.setVisibility(View.GONE);
                        } else {
                            suggestionLayout.setVisibility(View.VISIBLE);
                            suggestionScrollView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < options.length(); i++) {
                                setSuggestiondata(options.getJSONObject(i).getString("title"), options.length());
                                Log.d("ChatBot", "Response:+++++ " + options.getJSONObject(i).getString("title"));
                            }
                        }
                    }

                    if (main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").has("ReturnFormat")) {
                        returnFormat = main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("ReturnFormat");
                    } else {
                        returnFormat = "none";
                    }

                    if (main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").has("buttonValue")) {
                        startResponse = main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("buttonValue");
                    } else {
                        startResponse = "none";
                    }

                    if (main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").has("PreviousValue")) {
                        previousValue = main.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getString("PreviousValue");
                    } else {
                        previousValue = "none";
                    }

                    try {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NA.setVisibility(View.VISIBLE);
                        Log.d("ChatBot", "VolleyError: " + error);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Value", dataToSend);
                Log.d("ChatBot", "Params: " + params);
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

    private void showAlertDialog(String message, final Class intentToClass) {
        new AlertDialog.Builder(Chatbot.this, R.style.AppCompatAlertDialogStyle)
                .setMessage(message)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Chatbot.this, intentToClass).putExtra("from", "chatBot"));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    /*public void StartVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = baseUrl  *//*+ "query?v=20180101"*//*;

        JSONObject text = new JSONObject();
        JSONObject queryInput = new JSONObject();
        JSONObject textforQ = new JSONObject();
        try {
            text.put("text", dataToSend *//*+ sessionConfig.getPhone()*//*);
            text.put("languageCode", "en-US");

            textforQ.put("text", text);

            queryInput.put("queryInput", textforQ);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("ChatBotSent", "dataVolley: " + queryInput);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, queryInput, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                NA.setVisibility(View.GONE);

                Log.d("ChatBot", "OnResponse: " + response);

                try {

                    Log.e("ChatBot", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");

                    String stuff = response.getJSONObject("queryResult").getString("fulfillmentText").toString();
                    setBotdata(stuff);

                    Log.e("ChatBot", "Response:+++++ " + stuff);
                    Log.e("ChatBot", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");

                    JSONArray options = response.getJSONObject("queryResult").getJSONObject("webhookPayload").getJSONObject("google").getJSONObject("richResponse").getJSONArray("suggestions");
                    suggestionLayout.removeAllViews();
                    for (int i = 0; i < options.length(); i++) {
                        setSuggestiondata(options.getJSONObject(i).getString("title"));
                        Log.e("ChatBot", "Response:+++++ " + options.getJSONObject(i).getString("title"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NA.setVisibility(View.VISIBLE);
                Log.e("ChatBot", "OnError: " + error.toString());
                if (error.toString().equals("com.android.volley.AuthFailureError")) {
                    getNewToken();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };
        requestQueue.add(objectRequest);
    }*/

    /*private void getNewToken() {
        RequestQueue requestQueue = Volley.newRequestQueue(Chatbot.this);
        String url = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ChatBot", "NewTokenOnResponse: " + response);
                try {
                    accessToken = new JSONObject(response).getString("access_token");
                } catch (JSONException e) {
                    Log.e("ChatBot", "NewTokenOnResponse: " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ChatBot", "NewTokenOnError: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }*/
    /*if (startResponse.equals("none")) {
                        dataToSend = button.getText().toString()+ "" + phone;
                    } else if (startResponse.equals("i have given feedback but no reversal coming button")) {
                        String[] helper = button.getText().toString().split(" ");
                        dataToSend = helper[0] + " " + phone + " " + startResponse;
                    } else if (startResponse.equals("My leads are getting wasted giving feedback")) {
                        String[] helper = button.getText().toString().split(" ");
                        dataToSend = helper[0] + " " + startResponse + " " + phone;
                    } else {
                        dataToSend = startResponse + " " + button.getText().toString() + " " + phone;
                    }*/
}