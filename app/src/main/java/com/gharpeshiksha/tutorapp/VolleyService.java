package com.gharpeshiksha.tutorapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Adapter.ClassesForMeRecyclerViewAdaptor;
import com.gharpeshiksha.tutorapp.data_model.ClassesForMe;
import com.gharpeshiksha.tutorapp.localdb.LocalSQLiteDbHandler;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyService {
    private static String TAG = "VolleyService.java";
    private long phone;
    private static RecyclerView RecyclerView;
    private SwipeRefreshLayout SwipeRefreshLayout;
    public static List<ClassesForMe> classesForMes = new ArrayList<>();
    public static List<ClassesForMe> allQuiries = new ArrayList<>();
    private Context context;
    private static int activity_no;
    private PrefConfig prefConfig;
    private Dashboard dashboard = new Dashboard();


    public VolleyService(final Context context, final String url, final List<ClassesForMe> arrayList,
                         final RecyclerView recyclerView,
                         final SwipeRefreshLayout mSwipeRefreshLayout, final View layout_nodata,
                         final int activity_no, final Map<String, String> filterParams, String apiFrom) {

        prefConfig = new PrefConfig(context);
        SessionConfig sessionConfig = new SessionConfig(context);
        phone = sessionConfig.getPhone();
        arrayList.clear();
        if (activity_no == 0) {

            classesForMes.clear();


        } else if (activity_no == 1) {

            allQuiries.clear();

        }

        this.context = context;
        VolleyService.activity_no = activity_no;
        RecyclerView = recyclerView;
        SwipeRefreshLayout = mSwipeRefreshLayout;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        mSwipeRefreshLayout.setRefreshing(true);

        AsyncTask<Integer, Integer, String> task = new AsyncTask<Integer, Integer, String>() {

            @Override
            protected String doInBackground(Integer... integers) {
                StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {
                    Log.d(TAG, "VolleyService:onResponse: volley " + response);

                    try {

                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() == 0) {

                            layout_nodata.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            //loadingLayout.setVisibility(View.GONE);
                            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();


                        } else {
                            layout_nodata.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setRefreshing(true);
                    /*if(getLoadingLayout()!=null){
                        loadingLayout.setVisibility(View.GONE);
                    }else {
                        loadingLayout.setVisibility(View.VISIBLE);
                    }*/

                            LocalSQLiteDbHandler dbHandler = new LocalSQLiteDbHandler(context);
                            dbHandler.deleteClassesForMeResponse();
                            Log.v(TAG, "length: " + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {

                                final JSONObject part = jsonArray.getJSONObject(i);

                                String time = part.getString("time");
                                String tutors_contacted = part.getString("tutors_contacted");
                                String name = part.getString("name");
                                String classes = part.getString("class");
                                String subjects = part.getString("subjects");
                                String budget = "";
                                if(part.has("budget")) {
                                    budget = part.getString("budget");
                                }
//                        String budget = part.getString("budget");
                                long enq_id = part.getLong("enq_id");
                                String area = part.getString("area");
                                String utf8String = new String(area.getBytes("ISO-8859-1"), "UTF-8");

                                String favorite = part.getString("favorite");

                                Log.d(TAG, "onResponses: favorites true " + favorite + "  " + activity_no);

                                String status = part.getString("status");
                                String checkstatus = part.getString("paymentstatus");
                                Boolean enq_viewed = part.getBoolean("enq_viewed");

                                boolean feedback;
                                if (!part.has("feedback")) {
                                    feedback = true;
                                } else {
                                    feedback = part.getBoolean("feedback");
                                }
                                long distance = part.getLong("distance");

                                String highComp = "non";
                                if (part.has("highComp"))
                                    highComp = part.getString("highComp");

                                String freeClass = "non";
                                if (part.has("freeClass"))
                                    freeClass = part.getString("freeClass");

                                String studentUUId = "NA";
                                String tutorUUId = "NA";
                                if (part.has("studentUUID")) {
                                    studentUUId = part.getString("studentUUID");
                                }

                                if(part.has("tutorUUID")) {
                                    tutorUUId = part.getString("tutorUUID");
                                }

                                ClassesForMe classesForMe = new ClassesForMe(enq_id, time, (tutors_contacted + " Tutors contacted"),
                                        name, ("Tutor Requirement for " + subjects + " for " + classes),
                                        budget, utf8String, favorite, (distance), status, checkstatus, enq_viewed, feedback, highComp, freeClass
                                        , studentUUId, tutorUUId, "https://api.gharpeshiksha.com/imageTutor/default_profile_img.jpg");
                                if(apiFrom.matches("classesForMe")) {
                                    dbHandler.addClassesForMeResponse(classesForMe);
                                }
                                arrayList.add(classesForMe);
                                prefConfig.writeSort("distance");
                                Dashboard.sortBadge.setText("D");
                                if (activity_no == 0) {

                                    classesForMes.add(classesForMe);


                                } else if (activity_no == 1) {

                                    allQuiries.add(classesForMe);

                                }

                            }


                        }


                        if (activity_no == 0) {

                            if (arrayList != null) {
                                dashboard.setBadgeClases(arrayList.size());
                            }


                        } else if (activity_no == 1) {

                    /*if (arrayList.size() == 0) {
                        Dashboard.enquire.setVisibility(View.GONE);

                    } else {
                        Dashboard.enquire.setText(String.valueOf(arrayList.size()));
                        Dashboard.enquire.setVisibility(View.VISIBLE);

                        Log.d(TAG, "onResponse: classcount" + arrayList.size());


                    }*/

                        }


                    } catch (Exception e) {
                        Log.d(TAG, "onResponse Error: " + e.getMessage());
                    }


//            Log.d(TAG, "onResponse: adapter created : jkhskhkjafshkjhksff" + arrayList.size());
                    final ClassesForMeRecyclerViewAdaptor adapter = new ClassesForMeRecyclerViewAdaptor(context, arrayList);

                    adapter.notifyDataSetChanged();
                    /*new ClassesForMeFragment().showHideLoading(0);*/
                    recyclerView.setAdapter(adapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                    // loadingLayout.setVisibility(View.GONE);
                },
                        error -> {

                            mSwipeRefreshLayout.setRefreshing(false);
                            Log.d(TAG, "onErrorResponse: " + error);
                        }
                ) {

                    @Override
                    protected Map<String, String> getParams() {
                        Log.d(TAG, "getParams: getParams()");
                        Map<String, String> params = new HashMap<>();
                        Log.v(TAG, filterParams + "");
                        if (filterParams.containsKey("type")) {
                            return filterParams;
                        } else {
                            params.put("phone", "" + phone);
                            params.put("sort_by", "recency");
                            return params;
                        }
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
                return "finished" +
                        "";
            }
        };

        task.execute(10);
    }


    public VolleyService(Context context1, String shortby, int activity_no, RecyclerView recyclerView) {
        if (0 == activity_no) {
            //Toast.makeText(context, "activity_no: " + activity_no + " shortby: " + shortby, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "VolleyService: length:classesForMes" + classesForMes.size());
            Log.e(TAG, "activity_no: " + activity_no + " shortby: " + shortby);
            if (shortby.equals("recency")) {
                Comparator<ClassesForMe> com = new Comparator<ClassesForMe>() {
                    @Override
                    public int compare(ClassesForMe o1, ClassesForMe o2) {
                        if (o1.getTextEnq_id() < o2.getTextEnq_id()) {
                            return 1;
                        } else if (o1.getTextEnq_id() == o2.getTextEnq_id()) {
                            return 0;
                        } else return -1;
                    }
                };

                Collections.sort(classesForMes, com);
                final ClassesForMeRecyclerViewAdaptor adapter = new ClassesForMeRecyclerViewAdaptor(context1, classesForMes, true);

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            } else {
                Log.e(TAG, "VolleyService: length: classesForMes" + classesForMes.size());
                Log.e(TAG, "activity_no: " + activity_no + " shortby: " + shortby);
                //Toast.makeText(context, "activity_no: " + activity_no + " shortby: " + shortby, Toast.LENGTH_SHORT).show();
                Comparator<ClassesForMe> com1 = new Comparator<ClassesForMe>() {
                    @Override
                    public int compare(ClassesForMe o1, ClassesForMe o2) {
                        if (o1.getTextDis() > o2.getTextDis()) {
                            return 1;
                        } else if (o1.getTextDis() == o2.getTextDis()) {
                            return 0;
                        } else return -1;
                    }
                };
                Collections.sort(classesForMes, com1);
             /*   for(ClassesForMe classesForMe:classesForMes){

                    Log.e(TAG, "VolleyService: classesForMes"+classesForMe.getTextEnq_id() );
                }*/
                final ClassesForMeRecyclerViewAdaptor adapter = new ClassesForMeRecyclerViewAdaptor(context1, classesForMes, true);

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        } else {

            if (shortby.equals("recency")) {

                //Toast.makeText(context, "activity_no: " + activity_no + " shortby: " + shortby, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "activity_no: " + activity_no + " shortby: " + shortby);
                Log.e(TAG, "VolleyService: length: allQuiries" + allQuiries.size());
                Comparator<ClassesForMe> com = new Comparator<ClassesForMe>() {
                    @Override
                    public int compare(ClassesForMe o1, ClassesForMe o2) {
                        if (o1.getTextEnq_id() < o2.getTextEnq_id()) {
                            return 1;
                        } else if (o1.getTextEnq_id() == o2.getTextEnq_id()) {
                            return 0;
                        } else return -1;
                    }
                };
                Collections.sort(allQuiries, com);
                final ClassesForMeRecyclerViewAdaptor adapter = new ClassesForMeRecyclerViewAdaptor(context1, allQuiries, true);

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            } else {

                //Toast.makeText(context, "activity_no: " + activity_no + " shortby: " + shortby, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "activity_no: " + activity_no + " shortby: " + shortby);
                Log.e(TAG, "VolleyService: length: allQuiries" + allQuiries.size());
                Comparator<ClassesForMe> com1 = new Comparator<ClassesForMe>() {
                    @Override
                    public int compare(ClassesForMe o1, ClassesForMe o2) {
                        if (o1.getTextDis() > o2.getTextDis()) {
                            return 1;
                        } else if (o1.getTextDis() == o2.getTextDis()) {
                            return 0;
                        } else return -1;
                    }
                };
                Collections.sort(allQuiries, com1);
               /* for(ClassesForMe classesForMe:classesForMes){

                    Log.e(TAG, "VolleyService: classesForMes"+classesForMe.getTextEnq_id() );
                }*/
                final ClassesForMeRecyclerViewAdaptor adapter = new ClassesForMeRecyclerViewAdaptor(context1, allQuiries, true);

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        }
    }


}


