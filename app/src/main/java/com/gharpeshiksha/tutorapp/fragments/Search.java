package com.gharpeshiksha.tutorapp.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Adapter.ClassesForMeRecyclerViewAdaptor;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.FragmentLifecycle;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.ClassesForMe;
import com.gharpeshiksha.tutorapp.localdb.Contract;
import com.gharpeshiksha.tutorapp.localdb.LocalSQLiteDbHandler;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search extends Fragment implements FragmentLifecycle {
    View view;
    RecyclerView favouriteRecycler;
    String url = "https://api.gharpeshiksha.com" + "/GetClasses/favorite_enqTest";
    SwipeRefreshLayout swipeRefreshLayout;
    static Context context;
    ArrayList<ClassesForMe> arrayList;
    ClassesForMe classesForMe;
    ClassesForMeRecyclerViewAdaptor adaptor;
    SessionConfig sessionConfig;
    TextView nodata;
    Dashboard dashboard;
    ImageView back;
    private LocalSQLiteDbHandler dbHandler;
    private long softTl = 0;
    private String TAG = "Search.java";


    public Search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        back = view.findViewById(R.id.back);

        favouriteRecycler = view.findViewById(R.id.recycle_favourite);
        if (getActivity() != null)
            dbHandler = new LocalSQLiteDbHandler(getActivity());
        nodata = view.findViewById(R.id.nodata);
        nodata.setVisibility(View.VISIBLE);
        favouriteRecycler.setHasFixedSize(true);
        sessionConfig = new SessionConfig(view.getContext());
        final long phone = sessionConfig.getPhone();
        dashboard = new Dashboard();
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        favouriteRecycler.setLayoutManager(manager);
        arrayList = new ArrayList<>();
        swipeRefreshLayout = view.findViewById(R.id.swipe_favourite);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        if (getLocalData()) {
            //db
            long currTime = System.currentTimeMillis() / 1000;
            long futureSoftTL = softTl / 1000 + 10;
            if(currTime > futureSoftTL) {
                Log.d(TAG, "onCreateView: valid");
                arrayList.add(classesForMe);
                adaptor = new ClassesForMeRecyclerViewAdaptor(view.getContext(), arrayList);
                favouriteRecycler.setAdapter(adaptor);
                adaptor.notifyDataSetChanged();
            } else {
                Log.d(TAG, "onCreateView: not valid");
                FetchFavourite(sessionConfig.getPhone());
            }
        } else {
            //api
            Log.d(TAG, "onCreateView: api");
            FetchFavourite(phone);
        }

        back.setOnClickListener(view1 -> {
            getActivity().onBackPressed();
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("setOnRefreshListener", "run: setOnRefreshListener ");
//                        arrayList.clear();
                        FetchFavourite(phone);
                        Log.d("setOnRefreshListener", "run: setOnRefreshListener 1");
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, 3000);
            }
        });

        return view;
    }

    private boolean getLocalData() {
        Cursor cursor = dbHandler.getFav(dbHandler.getReadableDatabase());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String enq_id = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.ENQ_ID));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.TIME));
                String tutors_contacted = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.TUTOR_CON));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.NAME));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.TITLE));
                String budget = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.BUDGET));
                String utf8String = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.UTF_8STR));
                String favorite = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.FAVORITE));
                String distance = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.DISTANCE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.STATUS));
                String checkstatus = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.CHECK_STATUS));
                String enq_viewed = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.ENQ_VIEWED));
                String feedback = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.FEEDBACK));
                String highComp = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.HIGH_COMP));
                String freeClass = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.FREE_CLASS));
                String studentUUId = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.STUDENT_UUID));
                String tutorUUId = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.TUTOR_UUID));
                String picUrl = cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.PIC_URL));
                classesForMe = new ClassesForMe(
                        Long.parseLong(enq_id),
                        time,
                        (tutors_contacted + " Tutors contacted"),
                        name,
                        title,
                        budget,
                        utf8String,
                        favorite,
                        Double.parseDouble(distance), status, checkstatus, Boolean.parseBoolean(enq_viewed), Boolean.parseBoolean(feedback),
                        highComp, freeClass
                        , studentUUId, tutorUUId, picUrl);
                arrayList.add(classesForMe);
            } while (cursor.moveToNext());

            cursor.moveToPrevious();

            //Below is in milliseconds
            softTl = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(Contract.FavoriteTable.SOFT_TTL)));
            return true;
        }
        return false;
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        this.context = getActivity();

    }

    public void FetchFavourite(final long phone) {
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Success").matches("No Favorite Enquiries")) {
                        nodata.setVisibility(View.VISIBLE);
                        favouriteRecycler.setVisibility(View.GONE);
                        /*dashboard.favCount(0);*/
                        /*dashboard.setBadgeFavourite(2);*/
                    }

                } catch (Exception e) {

                }

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("bhjfdagjhfbdvmnmfdvj", "onResponse: ");
                    if (jsonArray.length() == 0) {

                        Log.d("bhjfdagjhfbdvmnmfdvj", "onResponse: ");
                        nodata.setVisibility(View.VISIBLE);
                        favouriteRecycler.setVisibility(View.GONE);
                        dashboard.setBadgeFavourite(0);

                    } else {
                        dbHandler.deleteFav(dbHandler.getWritableDatabase());
                        nodata.setVisibility(View.INVISIBLE);
                        favouriteRecycler.setVisibility(View.VISIBLE);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.d("bhjfdagjhfbdvmnmfdvj", "onResponse:  99   " + jsonArray.length());

                            String s = jsonArray.getString(i); // changes in every loop

                            JSONObject jsonObject = new JSONObject(s);


                            String name = jsonObject.getString("name");
                            String area = jsonObject.getString("area");

                            String utf8String = new String(area.getBytes("ISO-8859-1"), "UTF-8");

                            long distance = jsonObject.getLong("distance");
                            String subjects = jsonObject.getString("subjects");
                            String tutors_contacted = jsonObject.getString("tutors_contacted");
                            String time = jsonObject.getString("time");
                            String clas = jsonObject.getString("class");
                            long enq_id = jsonObject.getLong("enq_id");
                            String budget = jsonObject.getString("budget");
                            String favorite = jsonObject.getString("favorite");
                            String status = jsonObject.getString("status");

                            String checkstatus = jsonObject.getString("paymentstatus");

                            Boolean enq_viewed = jsonObject.getBoolean("enq_viewed");


                            Boolean feedback;

                            if (!jsonObject.has("feedback")) {

                                feedback = true;

                            } else {
                                feedback = jsonObject.getBoolean("feedback");
                            }

                            String highComp = "non";
                            if (jsonObject.has("highComp"))
                                highComp = jsonObject.getString("highComp");

                            String freeClass = "non";
                            if (jsonObject.has("freeClass"))
                                freeClass = jsonObject.getString("freeClass");
                            String picUrl = "NA";
                            String studentUUId = "NA";
                            String tutorUUId = "NA";
                            if (jsonObject.has("default_profile_pic")) {
                                picUrl = jsonObject.getString("default_profile_pic");
                            }

                            if (jsonObject.has("studentUUID")) {
                                studentUUId = jsonObject.getString("studentUUID");
                            }

                            if (jsonObject.has("tutorUUID")) {
                                tutorUUId = jsonObject.getString("tutorUUID");
                            }

                            classesForMe = new ClassesForMe(
                                    enq_id,
                                    time,
                                    (tutors_contacted + " Tutors contacted"),
                                    name,
                                    ("Tutor Requirement for " + subjects + " for " + clas),
                                    budget,
                                    utf8String,
                                    favorite,
                                    (distance), status, checkstatus, enq_viewed, feedback, highComp, freeClass
                                    , studentUUId, tutorUUId, picUrl);

                            dbHandler.addFav(dbHandler.getWritableDatabase(), classesForMe, subjects, clas);
                            arrayList.add(classesForMe);
                            adaptor = new ClassesForMeRecyclerViewAdaptor(view.getContext(), arrayList);
                            favouriteRecycler.setAdapter(adaptor);
                            adaptor.notifyDataSetChanged();


                        }

                        /* Dashboard dashboard = new Dashboard();*/
                        /*dashboard.favCount(jsonArray.length());*/
                        dashboard.setBadgeFavourite(jsonArray.length());


                    }


                } catch (Exception e) {
                    Log.e("exception favourites", e.getMessage());
                    //Toast.makeText(view.getContext(), "Exception : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noNetworkDialog();
            }
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

    protected void noNetworkDialog() {

        try {
            final Dialog dialog = new Dialog(view.getContext());

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
        } catch (Exception e) {
            Log.v("Favourite.java", e + "");
        }

    }

}