package com.gharpeshiksha.tutorapp.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gharpeshiksha.tutorapp.Adapter.Adapter_archived;
import com.gharpeshiksha.tutorapp.PrefConfig;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.Model_archived;
import com.gharpeshiksha.tutorapp.databinding.ActivityArchivedBinding;
import com.gharpeshiksha.tutorapp.localdb.Contract;
import com.gharpeshiksha.tutorapp.localdb.LocalSQLiteDbHandler;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.gharpeshiksha.tutorapp.utils.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Archived_Activity extends AppCompatActivity {

    private String TAG = "Archived_Activity.java", lastCursorStr;
    ActivityArchivedBinding binding;
    List<Model_archived> model_archivedList;
    LinearLayoutManager manager;
    Adapter_archived adapter_archived;
    Boolean isScrolling = false;
    Dialog dialog;
    int currentItems, totalItems, scrollOutItems;
    private boolean isScrolled = true;
    private ProgressBar progressBar;
    ProgressDialog progressDialog;
    SessionConfig sessionConfig;
    ImageView chechDis, checkRecArchived;
    PrefConfig prefConfig;
    private String intialstate_my_classes = "";
    private LocalSQLiteDbHandler db;
    private long softTL;
    TextView recency, sortDistanceArchived;
    private long itemId = 0;

    threadsWork threadsWork;
    MyAsynct pl;
    private boolean isLoading = false;
    private boolean hasMoreData = true; // Indicates if there is more data to load
    private int currentPage = 1; // Current page or offset
    private int pageSize = 10; // Number of items to load per page


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArchivedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefConfig = new PrefConfig(getApplicationContext());
        db = new LocalSQLiteDbHandler(Archived_Activity.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data API");
        progressDialog.setTitle("Ghar Pe Shiksha");
//        progressDialog.show();
        model_archivedList = new ArrayList<>();
        sessionConfig = new SessionConfig(getApplicationContext());
        manager = new LinearLayoutManager(getApplicationContext());
        adapter_archived = new Adapter_archived(Archived_Activity.this);
        binding.RecycleViewArchived.setAdapter(adapter_archived);
        binding.RecycleViewArchived.setLayoutManager(manager);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pl = new MyAsynct(this);
        pl.execute();
        getDataFromLocalAndApi();
        sortDialog();

        /* threadsWork = new threadsWork();
         Thread thr = new Thread(threadsWork);
         thr.start();*/
      //  Work work = new Work(0);

        // adding on scroll change listener method for our nested scroll view.

    }

    private void getDataFromLocalAndApi() {
        Cursor cursor = db.getArchived(db.getReadableDatabase(), itemId);
//        db.deleteArchived(db.getWritableDatabase());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String area = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.area));
                String studentUUId = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.studentUUID));
                String tutorUUUId = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.tutorUUID));
                String distance = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.distance));
                String sub = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.subjects));
                String enqId = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.enq_id));
                String hightCom1 = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.highComp1));
                String freeCls = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.freeCla));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.name));
                String paySta = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.paymentStatus));
                String tutorsCon = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.tutors_con));
                String favorite = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.favorite));
                String classfor = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.classfor));
                String budget = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.budget));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.status1));
                String enqView = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.enq_viewed));
                String curStr = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.cursor));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.timestamp));
                String opCount = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.op_count));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.time));
                softTL = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.sofTTL)));
                Model_archived m = new Model_archived(area, studentUUId, Double.parseDouble(distance), sub, tutorUUUId,
                        Long.parseLong(enqId), Double.parseDouble(opCount), hightCom1, freeCls, name,
                        paySta, Integer.parseInt(tutorsCon), time, favorite, classfor, budget, status, Boolean.parseBoolean(enqView),
                        curStr, Long.parseLong(timestamp));

                model_archivedList.add(m);
            } while (cursor.moveToNext());
            Log.d(TAG, "getDataFromLocalAndApi sqlite: " + cursor.getCount() + ", " + lastCursorStr);

            //SQLite data only valid for 10 minutes.
            long currTime = System.currentTimeMillis() / 1000;
            long futureSoftTL = (softTL / 1000) + 600;
//            Log.d(TAG, "getDataFromLocalAndApi: softtl: " + currTime + ", " + futureSoftTL);
            if (currTime > futureSoftTL) {
                Log.d(TAG, "getDataFromLocalAndApi: not valid");
                lastCursorStr = "";
                model_archivedList.clear();
                db.deleteArchived(db.getWritableDatabase());
            } else {
                Log.d(TAG, "getDataFromLocalAndApi: valid");
                cursor.moveToPrevious();
                itemId = cursor.getLong(cursor.getColumnIndexOrThrow(Contract.ArchivedTable._ID));
                lastCursorStr = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ArchivedTable.cursor));
                adapter_archived.submitList(model_archivedList);
                adapter_archived.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
                binding.RecycleViewArchived.setVisibility(View.VISIBLE);
                isScrolled = true;

                //Add pagination on RecyclerView by following below algorithm
                /** 1. Add scroll event listener on NestedScrollView
                 *  2. Override onScrollChanged(NestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY)
                 *  3. check if user scrollY == nestedScrollView height - its 0th child height
                 *  4. inside condition block check boolean flag is true then call method to get data and make that flag false there
                 *  5. so if user scrolled for 2 or more times to bottom then not call method to get data multiple times.*/

                /*binding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                        (nSV, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                            if (scrollY == nSV.getChildAt(0).getMeasuredHeight() - nSV.getMeasuredHeight()) {
                                //User scrolled at bottom of RecyclerView List.
                                if (isScrolled) {
                                    isScrolled = false;
                                    Log.d(TAG, "onScrollChange: scrolled sqlite");

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //getDataFromLocalAndApi();;
                                        }
                                    }).start();

                                    binding.progressBar.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                return;*/

                binding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                        (nSV, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                            int totalHeight = nSV.getChildAt(0).getHeight();
                            int visibleHeight = nSV.getHeight();
                            int scrolledHeight = scrollY;

                            // Check if the user has scrolled to the bottom of the NestedScrollView
                            if (totalHeight - visibleHeight <= scrolledHeight) {
                                // User scrolled to the bottom

                                // Ensure that data loading is not already in progress and there is more data to load
                                if (!isLoading && hasMoreData) {
                                    isLoading = true;
                                    Log.d(TAG, "onScrollChange: scrolled to the bottom");

                                    // Start a background thread to load more data
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Load more data from your data source
                                            List<Model_archived> newData = loadNextPageData(currentPage, pageSize);

                                            // Update UI on the main thread
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Handle the loaded data
                                                    if (newData != null && !newData.isEmpty()) {
                                                        // Add the loaded data to your list
                                                        model_archivedList.addAll(newData);

                                                        // Notify your adapter of the data change
                                                        adapter_archived.notifyDataSetChanged();

                                                        // Increment the current page
                                                        getDataFromLocalAndApi();

                                                        currentPage++;

                                                        // Data loading is complete
                                                        isLoading = false;

                                                        // Check if there is more data to load
                                                        if (newData.size() < pageSize) {
                                                            hasMoreData = false;
                                                        }
                                                        // Hide the progress bar
                                                        binding.progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                        }

                                        private List<Model_archived> loadNextPageData(int currentPage, int pageSize) {
                                            return null;
                                        }
                                    }).start();
                                    // Show a progress bar while loading data
                                    binding.progressBar.setVisibility(View.VISIBLE);
                                }
                            }
                        });

            }

            if (Utility.isNetworkAvailable(this)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getDataFromAPI();
                    }
                }).start();

               // getDataFromAPI();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getDataFromAPI();
                    }
                }).start();
            }
        } else {
            if (Utility.isNetworkAvailable(this)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getDataFromAPI();
                    }
                }).start();
            } else {
                noNetworkDialog();
            }
        }
    }

    private void noNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Archived_Activity.this);
        builder.setTitle("Turn on Internet");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!Utility.isNetworkAvailable(Archived_Activity.this)) {
                    noNetworkDialog();
                } else {
                    getDataFromAPI();
                    Toast.makeText(Archived_Activity.this, "Internet connected successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.create().show();
    }

    private void sortDialog() {
        binding.onclickSortArchived.setOnClickListener(view -> {
            dialog = new Dialog(Archived_Activity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogbox_archived);
            dialog.show();

            recency = dialog.findViewById(R.id.sortRecencyArchived);
            sortDistanceArchived = dialog.findViewById(R.id.sortDistanceArchived);
            chechDis = dialog.findViewById(R.id.checkDis);
            checkRecArchived = dialog.findViewById(R.id.checkRecArchived);
            if (binding.sortBadge.getText().equals("R")) {
                checkRecArchived.setVisibility(View.VISIBLE);
                chechDis.setVisibility(View.GONE);
            }

            recency.setOnClickListener(view1 -> {
                dialog.dismiss();
                progressDialog.show();
                binding.sortBadge.setText("R");
                intialstate_my_classes = "bhjadgjdaf";
                sortList(recency.getText());
                checkRecArchived.setVisibility(View.VISIBLE);
                chechDis.setVisibility(View.GONE);
                Log.v(TAG, "recency");
            });

            sortDistanceArchived.setOnClickListener(view1 -> {
                dialog.dismiss();
                binding.sortBadge.setText("D");
                checkRecArchived.setVisibility(View.GONE);
                chechDis.setVisibility(View.VISIBLE);
                progressDialog.show();
                sortList(sortDistanceArchived.getText());
                checkRecArchived.setVisibility(View.GONE);
                chechDis.setVisibility(View.VISIBLE);
                Log.v(TAG, "distance");
            });
        });
    }

    /**
     * Below method sort RecyclerView datasource list by distance or by recency. submit list to ListAdapter
     * after sort list.
     */
    private void sortList(CharSequence text) {
        if (text.equals(sortDistanceArchived.getText())) {
            //sort by distance
            ArrayList<Model_archived> sortList = Utility.distance((ArrayList<Model_archived>) model_archivedList);
//            Log.d(TAG, "sortList: " + sortList.size());
            adapter_archived.submitList(sortList);
            adapter_archived.notifyDataSetChanged();
            progressDialog.dismiss();
        } else if (text.equals(recency.getText())) {
            //sort by recency.
            ArrayList<Model_archived> sortList = Utility.recency((ArrayList<Model_archived>) model_archivedList);
//            Log.d(TAG, "sortList: " + model_archivedList.size());
            adapter_archived.submitList(sortList);
            adapter_archived.notifyDataSetChanged();
            progressDialog.dismiss();
        }
        Log.d(TAG, "sortList: " + adapter_archived.getCurrentList().size());
    }

   /* getDataFromAPI() {

        Log.d(TAG, "getDataFromAPI: archived=== "+lastCursorStr);
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.classesformeArchived(sessionConfig.getPhone(), "recency", lastCursorStr).enqueue(new Callback<List<Model_archived>>() {
            @Override
            public void onResponse(Call<List<Model_archived>> call, Response<List<Model_archived>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Update the UI on the main thread
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.body().isEmpty()) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    return;
                                }
                                for (int i = 0; i < response.body().size(); i++) {
                                    model_archivedList.add(response.body().get(i));
                                    db.addArchived(db.getWritableDatabase(), response.body().get(i));
                                }
//                                  model_archivedList.addAll(response.body());
                                ArrayList<Model_archived> list = Utility.recency((ArrayList<Model_archived>) model_archivedList);
                                adapter_archived.submitList(list);
                                adapter_archived.notifyDataSetChanged();
                                binding.progressBar.setVisibility(View.GONE);
                                binding.RecycleViewArchived.setVisibility(View.VISIBLE);

                                Log.d(TAG, "onResponse: Archived == " + response.body());
                                Log.d(TAG, "onResponse: " + model_archivedList.size());
                                if (model_archivedList.size() > 0) {
                                    lastCursorStr = response.body().get(response.body().size() - 1).getCursor();
                                    Log.d(TAG, "onResponse: " + lastCursorStr);
                                }
                                isScrolled = true;

                                //Add pagination on RecyclerView by following below algorithm
                                *//** 1. Add scroll event listener on NestedScrollView
                                 *  2. Override onScrollChanged(NestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY)
                                 *  3. check if user scrollY == nestedScrollView height - its 0th child height
                                 *  4. inside condition block check boolean flag is true then call method to get data and make that flag false there
                                 *  5. so if user scrolled for 2 or more times to bottom then not call method to get data multiple times.*//*

                                threadsWork = new threadsWork();

                                binding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                                        (nSV, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                                            if (scrollY == nSV.getChildAt(0).getMeasuredHeight() - nSV.getMeasuredHeight()) {
                                                //User scrolled at bottom of RecyclerView List.
                                                if (isScrolled) {
                                                    Log.d(TAG, "onScrollChange: scrolled");
                                                    getDataFromAPI();
                                                    binding.progressBar.setVisibility(View.VISIBLE);
                                                    isScrolled = false;
                                                    //threadsWork.start();
                                                }
                                            }
                                        });

                            }
                        });

                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: Eexo " + e.getMessage());
                    }
                } else {
                    Log.d(TAG, "onResponse: else == ");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Model_archived>> call, Throwable t) {
                Log.d(TAG, "onFailure: error:  " + t);
                progressDialog.dismiss();
            }
        });

    }*/

 /*   public void getDataFromAPI() {
        Log.d(TAG, "getDataFromAPI: archived=== " + lastCursorStr);
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);

        // Use a background thread to perform the network request
        new Thread(new Runnable() {
            @Override
            public void run() {
                apiServies.classesformeArchived(sessionConfig.getPhone(), "recency", lastCursorStr).enqueue(new Callback<List<Model_archived>>() {
                    @Override
                    public void onResponse(Call<List<Model_archived>> call, Response<List<Model_archived>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                List<Model_archived> responseData = response.body();

                                // Process the API response and update UI on the main thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (responseData.isEmpty()) {
                                            binding.progressBar.setVisibility(View.GONE);
                                        } else {
                                            for (int i = 0; i < responseData.size(); i++) {
                                                model_archivedList.add(responseData.get(i));
                                                db.addArchived(db.getWritableDatabase(), responseData.get(i));
                                            }
                                            ArrayList<Model_archived> list = Utility.recency((ArrayList<Model_archived>) model_archivedList);
                                            adapter_archived.submitList(list);
                                            adapter_archived.notifyDataSetChanged();
                                            binding.progressBar.setVisibility(View.GONE);
                                            binding.RecycleViewArchived.setVisibility(View.VISIBLE);

                                            Log.d(TAG, "onResponse: Archived == " + response.body());
                                            Log.d(TAG, "onResponse: " + model_archivedList.size());
                                            if (!responseData.isEmpty()) {
                                                lastCursorStr = responseData.get(responseData.size() - 1).getCursor();
                                                Log.d(TAG, "onResponse: " + lastCursorStr);
                                            }
                                            isScrolled = true;
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                Log.d(TAG, "onResponse: Exception " + e.getMessage());
                            }
                        } else {
                            Log.d(TAG, "onResponse: else == ");
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<List<Model_archived>> call, Throwable t) {
                        Log.d(TAG, "onFailure: error:  " + t);
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }*/
    public void getDataFromAPI() {
        Log.d(TAG, "getDataFromAPI: archived=== " + lastCursorStr);

        // Show progress bar
        binding.progressBar.setVisibility(View.VISIBLE);
        new ApiCallAsyncTask().execute();
    }

    private class ApiCallAsyncTask extends AsyncTask<Void, Void, List<Model_archived>> {

        @Override
        protected List<Model_archived> doInBackground(Void... params) {
            ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
            try {
                Response<List<Model_archived>> response = apiServies.classesformeArchived(sessionConfig.getPhone(), "recency", lastCursorStr).execute();
                if (response.isSuccessful() && response.body() != null) {
                    return response.body();
                } else {
                    Log.e(TAG, "API call failed with code: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "API call failed with exception: " + e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Model_archived> responseData) {
            if (responseData != null) {
                try {
                    if (responseData.isEmpty()) {
                        binding.progressBar.setVisibility(View.GONE);
                        return;
                    }
                    for (int i = 0; i < responseData.size(); i++) {
                        model_archivedList.add(responseData.get(i));
                        db.addArchived(db.getWritableDatabase(), responseData.get(i));
                    }
                    ArrayList<Model_archived> list = Utility.recency((ArrayList<Model_archived>) model_archivedList);
                    adapter_archived.submitList(list);
                    adapter_archived.notifyDataSetChanged();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.RecycleViewArchived.setVisibility(View.VISIBLE);

                    Log.d(TAG, "onResponse: Archived == " + responseData);
                    Log.d(TAG, "onResponse: " + model_archivedList.size());
                    if (!responseData.isEmpty()) {
                        lastCursorStr = responseData.get(responseData.size() - 1).getCursor();
                        Log.d(TAG, "onResponse: " + lastCursorStr);
                    }
                    isScrolled = true;
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: Exception " + e.getMessage());
                }
            } else {
                Log.d(TAG, "API call failed.");
            }
            progressDialog.dismiss();
        }
    }


}