package com.gharpeshiksha.tutorapp.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BadgeCount {

    private ArrayList<String> selectedclasses = new ArrayList<>();
    private ArrayList<String> datab = new ArrayList<>();
    private Context context;
    private VolleyHelper volleyHelper;
    private final String TAG = "BadgeCount.java";

    public BadgeCount(Context context) {
        this.context = context;
        volleyHelper = new VolleyHelper();
    }


    public void selectedClasses(Long phone, final SelectedClassInterface classInterface) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String baseurl = "https://api.gharpeshiksha.com/Tutor/Classes?phone=";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseurl + phone, response -> {
            try {
                selectedclasses.clear();
                datab.clear();
                JSONArray jsonArray = new JSONArray(response);

                JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
                String cls[] = jsonObject.getString("selectedcourse").split("@");
                for (String c : cls) {
                    String ff[] = c.split("<");
                    selectedclasses.add(ff[0]);

                }
                classInterface.SelectedCourses(selectedclasses);
                Log.d(TAG, "selectedClasses: " + selectedclasses.size());

                for (int i = 1; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                    datab.add(jsonObject1.getString("Course"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
        });
        requestQueue.add(stringRequest);

    }

    public void appliedcount(Long phone, final BCount count) {

        String alurl = "https://api.gharpeshiksha.com" + "/GetClasses/appliedleads";
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", "" + phone);

        volleyHelper.VolleyPostRequest(context, alurl, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    count.Count(jsonArray.length());

                } catch (Exception e) {

                }

            }

            @Override
            public void onError(String error) {

            }
        });

    }


    public void favCountVolley(Long phone, final BCount favCounts) {

        String url = "https://api.gharpeshiksha.com" + "/GetClasses/favorite_enq";
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", "" + phone);

        volleyHelper.VolleyPostRequest(context, url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("Success").matches("No Favorite Enquiries")) {
                        favCounts.Count(0);
                    }

                } catch (Exception e) {

                }

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    favCounts.Count(jsonArray.length());

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    public interface BCount {
        void Count(int count);
    }

    public interface SelectedClassInterface {

        void SelectedCourses(ArrayList<String> selectedCourses);

    }


}
