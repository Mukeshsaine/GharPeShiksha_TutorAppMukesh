package com.gharpeshiksha.tutorapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.BasicFeatures;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.activities.Chat_ViewProfile_Activity;
import com.gharpeshiksha.tutorapp.data_model.ClassesEnquiryModel;
import com.gharpeshiksha.tutorapp.data_model.Model_archived;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Utility {
    public static String TAG = "Utility.java";

    public static ArrayList<Model_archived> recency(ArrayList<Model_archived> list) {
        //sort by recency.
        Comparator<Model_archived> comparator = (Comparator<Model_archived>) (o1, o2) -> {
                if(o1.getTimestamp() < o2.getTimestamp()) {
                    return 1;
                } else if(o1.getTimestamp() == o2.getTimestamp()) {
                    return 0;
                } else {
                    return -1;
                }
        };
        Collections.sort(list, comparator);
        return list;
    }

    public static ArrayList<Model_archived> distance(ArrayList<Model_archived> list) {
        //sort by distance.
        Comparator<Model_archived> comparator = (Comparator<Model_archived>) (o1, o2) -> {
                if(o1.getDistance() > o2.getDistance()) {
                    return 1;
                } else if(o1.getDistance() == o2.getDistance()) {
                    return 0;
                } else {
                    return -1;
                }
        };
        Collections.sort(list, comparator);
        return list;
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
                if(connectivityManager.getActiveNetwork() != null) {
                    Network network = connectivityManager.getActiveNetwork();
                    NetworkCapabilities netCap = connectivityManager.getNetworkCapabilities(network);
                    return netCap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            netCap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                } else {
                    return false;
                }
            } else {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
            }
        } catch (Exception e) {
            Log.d(TAG, "isNetworkAvailable: " + e);
            return true;
        }
    }

    public static String getRandomStr() {
        try {
            // choose a Character random from this String
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz";

            // create StringBuffer size of AlphaNumericString
            StringBuilder sb = new StringBuilder(10);

            for (int i = 0; i < 10; i++) {
                // generate a random number between
                // 0 to AlphaNumericString variable length
                int index = (int) (AlphaNumericString.length() * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString.charAt(index));
            }
            return sb.toString();
        } catch (Exception e) {
            Log.d(TAG, "hideKeyboard: " + e);
        }
        return "";
    }

    public static void hideKeyboard(Context context, Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if(view == null) {
                //if no view currently have focus which we need to hide soft keyboard
                //create new View
            }

            /**Below method is use to hide keyboard from Activity only so for fragment get View
             * in params and then get its root then window token simply to get current focus. */
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.d(TAG, "hideKeyboard: " + e);
        }
    }

    /**Below method generate input params number of stars and return that. */
    public static String getStars(int length) {
        String stars = "";
        for(int i=0; i<length; i++) {
            stars += "*";
        }

        return stars;
    }

    public static void VolleyForFavourite(Context ctx, final Map<String, String> postFavDataMap, final String Text4Toast) {
        String favourite_action_URL = "https://api.gharpeshiksha.com" + "/GetClasses/favorite";

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        StringRequest favouriteRequest = new StringRequest(Request.Method.POST, favourite_action_URL, response -> Toast.makeText(ctx, Text4Toast, Toast.LENGTH_SHORT).show(), error -> Log.d("Server Error : ", "error : " + error.getMessage())) {

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

    public static void showMsgBeforecall(Context ctx, ClassesEnquiryModel classesForMe, String enqId) {
        final ProgressDialog progressDialog1 = new ProgressDialog(ctx);
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
        ActiveDialog(ctx, classesForMe, progressDialog1, enqId);
    }

    public static void ActiveDialog(Context ctx, final ClassesEnquiryModel classesForMe, final ProgressDialog prg, String enqId) {
//        Log.v("Classes---", tempId + ", " + sessionConfig.getPhone());
        String url = "https://api.gharpeshiksha.com" + "/Tutor/viewContactMsg";

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {

                prg.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                if (TextUtils.equals(jsonObject.getString("status"), "success")) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx, R.style.AppCompatAlertDialogStyle);
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
                        viewContactFun(ctx, enqId);
                    });
                    alertDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());


                    alertDialog.show();


                } else if (TextUtils.equals(jsonObject.getString("status"), "error")) {

                    if (jsonObject.has("time_left")) {
                        new BasicFeatures().AlertDialogFreeViews(ctx, jsonObject.getLong("time_left"));
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

                params.put("phone", "" + new SessionConfig(ctx).getPhone());

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

    public static void viewContactFun(Context ctx, String id) {
        String RealnumberURL = "https://api.gharpeshiksha.com" + "/GetClasses/getContact";
        Map<String, String> params = new HashMap<>();
        params.put("phone", new SessionConfig(ctx).getPhone() + "");
        params.put("enq_id", id);
        Log.v("Classes---", params + "");
        VolleyHelper volleyHelper = new VolleyHelper();
        volleyHelper.VolleyPostRequest(ctx, RealnumberURL, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    final JSONObject part = new JSONObject(response);

                    String result = part.getString("result");
                    if (result.equals("success")) {
                        Log.v("Classes---", "student contacted" + part.getString("phone"));
                        String num = part.getString("phone");
                        Toast.makeText(ctx, "1 contact viewed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_DIAL); // opens up all available dialers
                        intent.setData(Uri.parse("tel:" + num)); // tel:  is required or else app will crash
                        ctx.startActivity(intent); // startActivity


                    } else {
                        if (result.matches("error")) {
                            if (part.has("time_left")) {
                                new BasicFeatures().AlertDialogFreeViews(ctx, part.getLong("time_left"));
                            } else {
                                try {
                                    new AlertDialog.Builder(ctx, R.style.AppCompatAlertDialogStyle)
                                            .setMessage(part.getString("message"))
                                            .setPositiveButton("Okay", null)
                                            .show();
                                } catch (Exception e) {
                                    Toast.makeText(ctx, "Unable to view this class", Toast.LENGTH_SHORT).show();
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


}
