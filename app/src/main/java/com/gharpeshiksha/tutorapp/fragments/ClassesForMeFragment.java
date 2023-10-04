package com.gharpeshiksha.tutorapp.fragments;


import static com.gharpeshiksha.tutorapp.VolleyService.classesForMes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Adapter.ClassesForMeRecyclerViewAdaptor;
import com.gharpeshiksha.tutorapp.Adapter.ImagePageAdapter;
import com.gharpeshiksha.tutorapp.Adapter.InfinitePageAdapter;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.FragmentLifecycle;
import com.gharpeshiksha.tutorapp.PrefConfig;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.VolleyService;
import com.gharpeshiksha.tutorapp.activities.Archived_Activity;
import com.gharpeshiksha.tutorapp.activities.AreaRangeSelection;
import com.gharpeshiksha.tutorapp.activities.ClassesActivity;
import com.gharpeshiksha.tutorapp.activities.MyAsynct;
import com.gharpeshiksha.tutorapp.activities.Work;
import com.gharpeshiksha.tutorapp.data_model.AllIndiaModel;
import com.gharpeshiksha.tutorapp.data_model.ClassesEnquiryModel;
import com.gharpeshiksha.tutorapp.data_model.ClassesForMe;
import com.gharpeshiksha.tutorapp.data_model.Model_archived;
import com.gharpeshiksha.tutorapp.localdb.Contract;
import com.gharpeshiksha.tutorapp.localdb.LocalSQLiteDbHandler;
import com.gharpeshiksha.tutorapp.retrofit.ApiInterface;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.gharpeshiksha.tutorapp.utils.BadgeCount;
import com.glide.slider.library.Tricks.InfiniteViewPager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;

@SuppressLint("ValidFragment")
public class ClassesForMeFragment extends Fragment implements FragmentLifecycle {

    private static String TAG = "ClassesForMeFragment.java";
    private ApiInterface apiInterface;
    private String lastCursorStr = "", searchEnqId;
    private String cls = "";
    static View view;
    static RecyclerView recyclerView;
    private ArrayList<ClassesForMe> arrayList;
    private ProgressBar progressBar;
    private static ProgressBar endlessBar;
    private boolean mFlag = true;
    private boolean isPagination = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager manager;
    //below are testing apis for integrating chat system
    private String classes_url = "https://api.gharpeshiksha.com" + "/GetClasses/classesformeTest";
    private String filteredClasses = "https://api.gharpeshiksha.com" + "/GetClasses/classesformeFilteredChat";
    //Below are current 5.3.2 version apis
//    private String classes_url = "https://api.gharpeshiksha.com" + "/GetClasses/classesforme";
//    private String filteredClasses = "https://api.gharpeshiksha.com" + "/GetClasses/classesformeFiltered";
    private VolleyService volleyService;
    private View no_data;
    private SessionConfig sessionConfig;
    private CardView ChangeSubject, ChangeLocation;
    private TextView oops;
    LinearLayout Archived;
    private TextView aclass, counter;
    static Context context;
    private Dialog dialog;
    long phone;
    private Button retry;
    private TextView clascount;
    private Handler handler = new Handler();
    private VolleyHelper volleyHelper;
    private ClassesForMeRecyclerViewAdaptor adapterss;
    private Dashboard dashboard;
    private String comeFromFilter = "";
    private String radius = "";
    private String lat = "";
    private String longt = "";
    private String subj = "";
    private String classes = "";
    public static boolean prevRequestOfClassesForMe = false;
    LocalSQLiteDbHandler dbHandler;
    private long softTTL = 0;

    private PrefConfig prefConfig;
    long currentTimestamp;
    boolean isDataSQLite = false, isAllIndiaChecked = false, isSearchByEnqId;
    private NestedScrollView mNestedScrollView, nestScoll;
    private ProgressBar bar;
    private ExecutorService mExecutor;
    String result = "";
    MyAsynct pl;
    int page = 0, limit = 2;


    ClassesForMeRecyclerViewAdaptor adapter;
    private boolean isLoading = false;
    private boolean hasMoreData = true; // Indicates if there is more data to load
    private int currentPage = 1; // Current page or offset
    private int pageSize = 10; // Number of items to load per page


    @SuppressLint("ValidFragment")
    public ClassesForMeFragment(String comeFromFilter, String radius, String lat, String longt, String subj, String classes) {
        Log.v("Classes---", comeFromFilter + "");
        this.comeFromFilter = comeFromFilter;
        this.subj = subj;
        this.longt = longt;
        this.lat = lat;
        this.radius = radius;
        this.classes = classes;
    }

    public ClassesForMeFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_classes_for_me, container, false);
        try {
            Dashboard dashboard = (Dashboard) getActivity();
            comeFromFilter = dashboard.getfromCome();
            radius = dashboard.getRadius();
            lat = dashboard.getLat();
            longt = dashboard.getLong();
            subj = dashboard.getBadgeCount();
            isAllIndiaChecked = dashboard.isAllIndiaChecked();
            searchEnqId = dashboard.searchEnqId;
            isSearchByEnqId = dashboard.mIsSearchByEnqId;
            Log.d(TAG, "onCreateView: search enqId" + searchEnqId);
        } catch (Exception ignored) {
        }

        pl = new MyAsynct(getContext());
        pl.execute();
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        Archived = view.findViewById(R.id.Archived);
        Archived.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), Archived_Activity.class));
        });

        prefConfig = new PrefConfig(getActivity());
        volleyHelper = new VolleyHelper();
        recyclerView = view.findViewById(R.id.enquiries_list);
        mNestedScrollView = view.findViewById(R.id.nestedScrollView);
        recyclerView.setHasFixedSize(true);

      //  nestScoll = view.findViewById(R.id.nesteScro);
        sessionConfig = new SessionConfig(view.getContext());
        phone = sessionConfig.getPhone();
        dashboard = new Dashboard();
        endlessBar = view.findViewById(R.id.endlessBar);
        no_data = view.findViewById(R.id.no_data);
        oops = view.findViewById(R.id.oops);
        ChangeLocation = no_data.findViewById(R.id.change_location);
        ChangeSubject = no_data.findViewById(R.id.change_subject);
        bar = view.findViewById(R.id.bar);

        arrayList = new ArrayList<>();

        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);


       // mExecutor = Executors.newFixedThreadPool(5);
      //  new ExampleAsync().execute();


        Log.e(TAG, "onCreateView: url class " + classes_url);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_clcontainer);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        dbHandler = new LocalSQLiteDbHandler(getActivity());
//        dbHandler.onUpgrade(dbHandler.getWritableDatabase(), Contract.DB_VERSION, Contract.DB_VERSION + 1);
        if (getDataFromSQLite()) {
            //10 minutes.
            softTTL += 600;
            Calendar cal = Calendar.getInstance();
            currentTimestamp = cal.getTimeInMillis() / 1000;
            if (currentTimestamp < softTTL && !comeFromFilter.matches("EnquiryFilter") && !isAllIndiaChecked && !isSearchByEnqId) {
                Log.v(TAG, "get data from localSQLite");
               /* new Thread(new Runnable() {
                    @Override
                    public void run() {
                        changeUserInterface();
                    }
                }).start();*/

               changeUserInterface();
                ImageSliderVolley();
                isDataSQLite = true;
            } else {
                //local db data is expired by ttl
                Log.v(TAG, "get data from api:" + lat + ", " + longt);
                if (isNetworkAvailable()) {
                    dbHandler.deleteClassesForMeResponse();
                    myQuiries(comeFromFilter, subj, radius, lat, longt);
                    ImageSliderVolley();
                } else {
                    noNetworkDialog();
                }
            }

        } else {
            //if there is no data in local db.
            Log.v(TAG, "get data from api2:" + lat + ", " + longt);
            if (isNetworkAvailable()) {
                dbHandler.deleteClassesForMeResponse();
                myQuiries(comeFromFilter, subj, radius, lat, longt);
                ImageSliderVolley();
            } else {
                noNetworkDialog();
            }
        }


       /* nestScoll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                  //  loadingPB.setVisibility(View.VISIBLE);
                   // getDataFromAPI(page, limit);
                    changeUserInterface();

                }
            }
        });*/
   /*     recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!adapter.isLoading()) {
                    int visibleItemCount = manager.getChildCount();
                    int totalItemCount = manager.getItemCount();
                    int firstVisibleItem = manager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItem) >= totalItemCount
                            && firstVisibleItem >= 0
                            && adapter.getCurrentPage() < adapter.getTotalPages()) {
                        // Load more data (e.g., make a network request for the next page)
                        adapter.setLoading(true);
                        loadMoreData(adapter.getCurrentPage() + 1);
                    }
                }
            }
        });
*/

       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    loadNextPage();
                }
            }
        });*/
        //loadFirstPage();
        listeners();
        return view;


    }

    public void setAllIndiaChecked(boolean isChecked) {
        isAllIndiaChecked = isChecked;
    }

    public void setSearchByEnqId(boolean isChecked) {
        isSearchByEnqId = isChecked;
    }

   private void changeUserInterface() {
       // Show the refreshing bar and hide the no_data View
       mSwipeRefreshLayout.setRefreshing(true);
       no_data.setVisibility(View.GONE);
       mSwipeRefreshLayout.setVisibility(View.VISIBLE);

       //prefConfig to distance and sortBadge D
       prefConfig.writeSort("distance");
       Dashboard.sortBadge.setText("D");

       // Execute AsyncTask to perform background work
       new ChangeUserInterfaceTask().execute();
   }
    private class ChangeUserInterfaceTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Simulate some background work (replace with your actual work)
                Thread.sleep(2000); // Sleep for 2 seconds as an example
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Update the UI on the main thread after background work is done
            mSwipeRefreshLayout.setRefreshing(false);

            // Update other UI elements here
            if (arrayList != null) {
                dashboard.setBadgeClases(arrayList.size());
            }

            Log.v("ClassesFor.java", "a size: " + arrayList.size());

            adapter = new ClassesForMeRecyclerViewAdaptor(getActivity(), arrayList);
            recyclerView.setAdapter(adapter);

            final Map<String, String> empty = new HashMap<>();
            mSwipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
                mSwipeRefreshLayout.setRefreshing(false);
                volleyService = new VolleyService(view.getContext(), classes_url, arrayList, recyclerView,
                        mSwipeRefreshLayout, no_data, 0, empty, "classesForMe");
            }, 3000));
        }
    }

/*
  private void changeUserInterface() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            //visible refreshing bar and gone no_data View
            no_data.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(true);
            //prefConfig to distance and sortBadge D
            prefConfig.writeSort("distance");
            Dashboard.sortBadge.setText("D");
            if (arrayList != null) {
                dashboard.setBadgeClases(arrayList.size());
            }

            Log.v("ClassesFor.java", "a size: " + arrayList.size());
            adapter = new ClassesForMeRecyclerViewAdaptor(getActivity(), arrayList);

//            adapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
            recyclerView.setAdapter(adapter);
            final Map<String, String> empty = new HashMap<>();
            mSwipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
                mSwipeRefreshLayout.setRefreshing(false);
                volleyService = new VolleyService(view.getContext(), classes_url, arrayList, recyclerView,
                        mSwipeRefreshLayout, no_data, 0, empty, "classesForMe");
            }, 3000));
        } catch (Exception e) {
            Log.d(TAG, "catch: swipe refresh" + e);
            mSwipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
        }
    }*/

    private boolean getDataFromSQLite() {
        Cursor tableRowData = dbHandler.getClassesForMeResponse();
        if (tableRowData.getCount() > 0) {
            arrayList.clear();
            if (tableRowData.moveToFirst()) {
//                AsyncTask<Integer, Integer, String> task = new AsyncTask<Integer, Integer, String>() {
//                    @Override
//                    protected String doInBackground(Integer... integers) {
                        do {
                            String enq_id = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.ENQ_ID));
                            String time = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.TEXT_MIN));
                            String tutors_contacted = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.TEXT_VIEWS));
                            String name = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.TEXT_NAME));
                            String tutorsReq = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.TEXT_TUTOR_REQUIREMENT));
                            String budget = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.TEXT_BUDGET));
                            String utf8String = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.TEXT_LOC));
                            String favorite = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.FAVORITE));
                            double distance = tableRowData.getDouble(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.TEXT_DIS));
                            String status = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.STATUS));
                            String paymentStatus = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.PAYMENT_STATUS));
                            String enq_viewed = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.ENQ_VIEWED));
                            String feedback = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.FEEDBACK));
                            String highComp = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.HIGH_COMP));
                            String freeClass = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.FREE_CLASS));
                            String studentUUId = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.STUDENT_UUID));
                            String tutorUUId = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.TUTOR_UUID));
                            String picUrl = tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.PIC_URL));
                            long enqId = Long.parseLong(enq_id);
                            boolean enqViewed = false, feedbacks = false;
                            if (enq_viewed.matches("1")) {
                                enqViewed = true;
                            }
                            if (feedback.matches("1")) {
                                feedbacks = true;
                            }
                            ClassesForMe classesForMe = new ClassesForMe(enqId, time, tutors_contacted, name, tutorsReq, budget, utf8String, favorite,
                                    distance, status, paymentStatus, enqViewed, feedbacks, highComp, freeClass, studentUUId, tutorUUId, picUrl);
                            arrayList.add(classesForMe);
                            softTTL = Long.parseLong(tableRowData.getString(tableRowData.getColumnIndexOrThrow(Contract.ClassesForMeAPI.SOFT_TTL)));
                            classesForMes.add(classesForMe);
                        } while (tableRowData.moveToNext());
//                        return "finished";
//                    }
//                };
            }
            tableRowData.close();
            return true;
        }
        return false;
    }

    private void ImageSliderVolley() {


        String url = "https://api.gharpeshiksha.com" + "/Tutor/flipperImage";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        mSwipeRefreshLayout.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        final InfiniteViewPager myPager = view.findViewById(R.id.myimagepager);
                        myPager.setVisibility(View.VISIBLE);

                        String[] imageArra = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            imageArra[i] = jsonArray.get(i).toString();

                        }

                        Log.d(TAG, "onResponse: imagea rrakjfah ");

                        PagerAdapter adapter = new InfinitePageAdapter(new ImagePageAdapter(getContext(), imageArra));

                        myPager.setAdapter(adapter);
                        myPager.setCurrentItem(0);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                myPager.setCurrentItem(myPager.getCurrentItem() + 1);

                                handler.postDelayed(this, 5000);

                            }
                        }, 5000);
                        mSwipeRefreshLayout.setRefreshing(false);

                    }


                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }

            }
        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", phone + "");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //Classes to click filter data ClassesForMe
    public void myQuiries(String type, final String classes, final String radius, final String latitude, final String longitude) {
        if (isAllIndiaChecked) {
            Log.d(TAG, "allIndia: params: " + classes + ", " + lastCursorStr);
            if (classes.isEmpty()) {
                Log.d(TAG, "getAllIndiaData: empty all india classes");
                BadgeCount badgeCount = new BadgeCount(getActivity());
                badgeCount.selectedClasses(sessionConfig.getPhone(), new BadgeCount.SelectedClassInterface() {
                    @Override
                    public void SelectedCourses(ArrayList<String> selectedCourses) {
                        for (String s : selectedCourses) {
                            cls += s + ",";
                        }
                        getAllIndiaData(cls.substring(0, cls.length() - 1));
                        Log.d(TAG, "SelectedCourses: " + cls);
                    }
                });
            } else {
                getAllIndiaData(classes);
                Log.d(TAG, "getAllIndiaData: not empty all india classes");
            }
        } else if (isSearchByEnqId) {
            arrayList.clear();
            apiInterface.getClassByEnqId(sessionConfig.getPhone(), searchEnqId).enqueue(
                    new Callback<ClassesEnquiryModel>() {
                        @Override
                        public void onResponse(@NonNull Call<ClassesEnquiryModel> call, @NonNull retrofit2.Response<ClassesEnquiryModel> response) {
                            Log.d(TAG, "onResponse: " + response.body());
                            if (response.body() != null) {
                                ClassesEnquiryModel m = response.body();
                                arrayList.add(new ClassesForMe(m.getEnqId(), m.getTime(), m.getTutorsContacted() + " Tutors Contacted", m.getName()
                                        , "Tutor Requirement for " + m.getSubjects() + ", " + m.getClass_(), m.getBudget(), m.getArea(), m.getFavorite(),
                                        m.getDistance(), m.getStatus(), m.getPaymentstatus(), m.getEnqViewed(), false,
                                        m.getHighComp().toString(), m.getFreeClass().toString(), m.getStudentUUID(), m.getTutorUUID(),
                                        m.getDefaultProfilePic()));

                                changeUserInterface();
                              /*  new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        changeUserInterface();
                                    }
                                }).start();*/

                                ImageSliderVolley();
                            } else {
                                final Map<String, String> empty = new HashMap<>();
                                mSwipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    volleyService = new VolleyService(view.getContext(), classes_url, arrayList, recyclerView,
                                            mSwipeRefreshLayout, no_data, 0, empty, "classesForMe");
                                }, 3000));
                                Toast.makeText(getActivity(), "no enquiry found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ClassesEnquiryModel> call, @NonNull Throwable t) {
                            Log.d(TAG, "onFailure: search enq " + t);
                        }
                    }
            );
            Log.d(TAG, "myQuiries: search by enquiryId");
        } else {
            final Map<String, String> empty = new HashMap<>();
            if (!type.equals("EnquiryFilter")) {
                Log.v(TAG, classes_url);
                volleyService = new VolleyService(getActivity(), classes_url, arrayList, recyclerView, mSwipeRefreshLayout, no_data,
                        0, empty, "classesForMe");

                isDataSQLite = false;
                mSwipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    volleyService = new VolleyService(view.getContext(), classes_url, arrayList, recyclerView, mSwipeRefreshLayout, no_data, 0, empty, "classesForMe");
                }, 3000));
            } else {
                final Map<String, String> params = new HashMap<>();
                params.put("type", "filtered");
                params.put("phone", "" + phone);
                params.put("radius", "" + radius);
                params.put("classes", classes);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                Log.d(TAG, "myQuiries: FilterRun : " + params.toString());

                isDataSQLite = false;
                arrayList.clear();//so won't display when filtered
                volleyService = new VolleyService(view.getContext(), filteredClasses, arrayList, recyclerView, mSwipeRefreshLayout, no_data, 0, params, "classesForMe");

                mSwipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    volleyService = new VolleyService(view.getContext(), filteredClasses, arrayList, recyclerView, mSwipeRefreshLayout, no_data, 0, params, "classesForMe");
                }, 3000));
            }
        }
    }

    private void getAllIndiaData(String classes) {
        Log.d(TAG, "getAllIndiaData: this is allIndian == = class == "+classes+", lastcur==="+lastCursorStr);
        Log.d(TAG, "getAllIndiaData: cls of all india: " + classes);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RetrofitClient.getClient().create(ApiInterface.class).getAllIndiaData(
                                sessionConfig.getPhone() + "", classes, lastCursorStr)
                        .enqueue(new Callback<List<AllIndiaModel>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<AllIndiaModel>> call, @NonNull
                            retrofit2.Response<List<AllIndiaModel>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    try {
                                        bar.setVisibility(View.GONE);
                                        if (!isPagination) {
                                            arrayList.clear();
                                        } else {
                                            if (response.body().size() == 0)
                                                return;
//                                            bar.setVisibility(View.GONE);
                                        }

                                        List<AllIndiaModel> list = response.body();
                                        Log.d(TAG, "onResponse: " + list);
                                        for (int i = 0; i < list.size(); i++) {
                                            AllIndiaModel m = list.get(i);
                                            if (i == list.size() - 1) {
                                                lastCursorStr = m.getCursor();
                                            }

                                            arrayList.add(new ClassesForMe(m.getEnqId(), m.getTime(), m.getTutorsContacted()
                                                    + " Tutors Contacted", m.getName()
                                                    , "Tutor Requirement for " + m.getSubjects() + ", " + m.getClass_(),
                                                    m.getBudget(), m.getArea(), m.getFavorite(),
                                                    m.getDistance(), m.getStatus(), m.getPaymentstatus(), m.getEnqViewed(),
                                                    false,
                                                    m.getHighComp().toString(), m.getFreeClass().toString(), m.getStudentUUID(),
                                                    m.getTutorUUID(),
                                                    m.getDefaultProfilePic()));
                                        }

                                        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                            @Override
                                            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                                                                       int oldScrollY) {
                                                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                                                    Log.d(TAG, "onScrollChange: scrolled allIndia");
                                                    if (mFlag) {
                                                        getAllIndiaData(classes);
                                                        Log.d(TAG, "onScrollChange: scrolled bottom");
                                                        isPagination = true;
                                                        mFlag = false;
                                                        bar.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            }
                                        });

                                        mFlag = true;

                                        changeUserInterface();
                                        ImageSliderVolley();
                                    } catch (Exception e) {
                                        Log.d(TAG, "catch: " + e);
                                        e.printStackTrace();
                                    }
                                } else {
                                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                        @Override
                                        public void onRefresh() {
                                            isAllIndiaChecked = false;
                                            dashboard.mIsAllIndiaChecked = false;
                                            mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                                @Override
                                                public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY,
                                                                           int oldScrollX, int oldScrollY) {
//                                            Log.d(TAG, "onScrollChange: " + scrollX);
                                                }
                                            });
                                            HashMap<String, String> empty = new HashMap<>();
                                            volleyService = new VolleyService(view.getContext(), classes_url, arrayList, recyclerView,
                                                    mSwipeRefreshLayout, no_data, 0,
                                                    empty, "classesForMe");
                                        }
                                    });
                                    ImageSliderVolley();
                                }
                            }

                            @Override
                            public void onFailure
                                    (@NonNull Call<List<AllIndiaModel>> call, @NonNull Throwable t) {
                                Log.d(TAG, "onFailure: " + t);
                            }
                        });
            }
        }, 0);
    }

    protected void noNetworkDialog() {

        dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.no_network_dialog);
        retry = dialog.findViewById(R.id.retry);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        retry.setOnClickListener(v -> {
            dialog.dismiss();
            if (isNetworkAvailable()) {
                dialog.dismiss();
                dbHandler.deleteClassesForMeResponse();
                ImageSliderVolley();
                Log.d(TAG, "onCreateView: My Queries: " + comeFromFilter + subj + radius +
                        " " + "\n" + " lat" + lat + " long" + longt);
                myQuiries(comeFromFilter, subj, radius, lat, longt);
            } else {
                dialog.dismiss();
                noNetworkDialog();
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onResume() {
        super.onResume();
        context = getActivity();
        recyclerView = view.findViewById(R.id.enquiries_list);
        Log.e(TAG, "onResume: recyclerView" + recyclerView.getId());
    }

    public void listeners() {
        ChangeLocation.setOnClickListener(v -> getActivity().startActivity(new Intent(getActivity(), AreaRangeSelection.class)
                .putExtra("Dashboard", true)));
        ChangeSubject.setOnClickListener(v -> getActivity().startActivity(new Intent(getActivity(), ClassesActivity.class)));
    }

    //sort by recency
    public void recency(Context context, int activity_no) {
        Log.d(TAG, "recency: recency filter " + isAllIndiaChecked + ", " + isSearchByEnqId);
        new VolleyService(context, "recency", activity_no, recyclerView);
    }

    //sort by distance
    public void distance(Context context, int activity_no) {
        Log.d(TAG, "recency: distance filter " + isAllIndiaChecked + ", " + isSearchByEnqId);
        new VolleyService(context, "distance", activity_no, recyclerView);
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
        Log.v("ClassesFor.java", "l;asjdf");
        recyclerView = view.findViewById(R.id.enquiries_list);
    }

}

