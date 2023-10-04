package com.gharpeshiksha.tutorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.Adapter.AdapterSpinnerTimeSet;
import com.gharpeshiksha.tutorapp.data_model.SpinnerModel;
import com.gharpeshiksha.tutorapp.data_model.Time_Set_Models;
import com.gharpeshiksha.tutorapp.databinding.ActivityTimeSetBinding;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TimeSetActivity extends AppCompatActivity {

    ActivityTimeSetBinding binding;
    Dialog dialogtime;

    public static ArrayAdapter<String> firsdayspinnerAdapter;
    public ArrayAdapter<String> openTimeSpinnerAdapter;
    private ArrayAdapter<String> closeTimeSpinnerAdapter;
    public static Spinner daySpinner, openSpinner, closeSpinner;
    SessionConfig sessionConfig;
    String day = "", openTime = "", closeTime = "";
    int pos=0;

    String TAG = "TimeSetActivity.java";
    Button Save_Item;
    ImageView deleteIcon1;
    List<SpinnerModel> spinnerModelList = new ArrayList<>();
    //List<Time_Set_Models> time_set_modelsList = new ArrayList<>();
    AdapterSpinnerTimeSet listAdapter;
    public static List<String> list;
    public static  List<String> list1;
    public static List<String> list2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimeSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeView);

        Intent intent = getIntent();
        String tu_id = intent.getStringExtra("tutor_id");
        Toast.makeText(this, "tutor = "+tu_id, Toast.LENGTH_SHORT).show();
        sessionConfig = new SessionConfig(TimeSetActivity.this);

        swipeRefreshLayout.setColorSchemeResources(R.color.pDarkGreen, R.color.pDarkGreen, R.color.pLightGreen, R.color.pFullLightGreen);
        // Refresh  the layout
        getAllotment(tu_id);


        list = new ArrayList<>();
        list.add("Choose Day");
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");
        list.add("Sunday");

        binding.back1.setOnClickListener(view -> {
            finish();

            // overridePendingTransition(R.anim.right_to_letf,R.anim.left_to_right);
        });

        binding.addItemdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TimeSetActivity.this, "This is Toast", Toast.LENGTH_SHORT).show();

                dialogtime = new Dialog(TimeSetActivity.this);
                dialogtime.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = LayoutInflater.from(TimeSetActivity.this).inflate(R.layout.time_schedule, null);
                view.findViewById(R.id.Save_Item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!day.isEmpty() && !openTime.isEmpty() && !closeTime.isEmpty()) {
                            spinnerModelList.add(new SpinnerModel(day, openTime, closeTime));
                            //Log.d(TAG, "onClick: "+day, openTime, closeTime);
                            Call_API_setTime();
                            openTime = "";
                            closeTime = "";
                            list.remove(pos);
                            dialogtime.dismiss();
//                            listAdapter = new AdapterSpinner(spinnerModelList, TimeSetActivity.this);
//                            binding.recyclerViewtime.setAdapter(listAdapter);
                            getAllotment(tu_id);
//                            listAdapter.notifyDataSetChanged();


                        } else {
                            Toast.makeText(TimeSetActivity.this, "Please Select All Values", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                daySpinner = view.findViewById(R.id.daySpinner);
                openSpinner = view.findViewById(R.id.openSpinner);
                closeSpinner = view.findViewById(R.id.closeSpinner);

                firsdayspinnerAdapter = new ArrayAdapter<>(TimeSetActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
                daySpinner.setAdapter(firsdayspinnerAdapter);
                daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (position > 0) {
                            Log.d(TAG, "onItemSelected: " + list);
                            day = adapterView.getItemAtPosition(position).toString();
                            pos=position;

                            Log.d(TAG, "onItemSelected: " + day);

                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                list2 = new ArrayList<>();
                list1 = new ArrayList<>();
                list1.add("Choose Open Time");
                list1.add("01:00 PM");
                list1.add("02:00 PM");
                list1.add("03:00 PM");
                list1.add("04:00 PM");
                list1.add("05:00 PM");
                list1.add("06:00 PM");
                list1.add("07:00 PM");
                list1.add("08:00 PM");
                list1.add("09:00 PM");
                list1.add("10:00 PM");
                list1.add("11:00 PM");
                list1.add("05:00 AM");
                list1.add("06:00 AM");
                list1.add("07:00 AM");
                list1.add("08:00 AM");
                list1.add("09:00 AM");
                list1.add("10:00 AM");
                list1.add("11:00 AM");
                list1.add("12:00 AM");
                list1.add("01:00 AM");
                list1.add("02:00 AM");
                list1.add("03:00 AM");
                list1.add("04:00 AM");
                list1.add("05:00 AM");

                openTimeSpinnerAdapter = new ArrayAdapter<>(TimeSetActivity.this, android.R.layout.simple_spinner_dropdown_item, list1);
                openSpinner.setAdapter(openTimeSpinnerAdapter);
                openSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (position > 0) {
                            openTime = adapterView.getItemAtPosition(position).toString();
                            deleteItem(position, list2, openTime);
                            Log.d(TAG, "onItemSelected: " + openTime);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                list2.add("Choose Close Time");
                list2.add("01:00 PM");
                list2.add("02:00 PM");
                list2.add("03:00 PM");
                list2.add("04:00 PM");
                list2.add("05:00 PM");
                list2.add("06:00 PM");
                list2.add("07:00 PM");
                list2.add("08:00 PM");
                list2.add("09:00 PM");
                list2.add("10:00 PM");
                list2.add("11:00 PM");
                list2.add("12:00 PM");
                list2.add("05:00 AM");
                list2.add("06:00 AM");
                list2.add("07:00 AM");
                list2.add("08:00 AM");
                list2.add("09:00 AM");
                list2.add("10:00 AM");
                list2.add("11:00 AM");
                list2.add("12:00 AM");
                list2.add("01:00 AM");
                list2.add("02:00 AM");
                list2.add("03:00 AM");
                list2.add("04:00 AM");
                list2.add("05:00 AM");

                closeTimeSpinnerAdapter = new ArrayAdapter<>(TimeSetActivity.this, android.R.layout.simple_spinner_dropdown_item, list2);
                closeSpinner.setAdapter(closeTimeSpinnerAdapter);
                closeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (position > 0) {
                            closeTime = adapterView.getItemAtPosition(position).toString();
                            Log.d(TAG, "onItemSelected: " + closeTime);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                dialogtime.setContentView(view);
                dialogtime.show();
            }


        });
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Call_API_setTime();

                        getAllotment(tu_id);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    private void getAllotment(String tu_id) {
        //show time data api
        ApiServies servies = RetrofitClient.getClient().create(ApiServies.class);
        servies.ViewAllotment(sessionConfig.getPhone()).enqueue(new Callback<List<SpinnerModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerModel>> call, Response<List<SpinnerModel>> response) {
                Log.d(TAG, "onResponse: response -== "+response.body());
                try {
                    if (response.isSuccessful())
                    {
                        spinnerModelList = response.body();
                        Log.d(TAG, "onResponse: time schedules = = "+spinnerModelList);
                        // spinnerModelList.add((SpinnerModel) time_schedule_modelList);
                        Log.d(TAG, "onResponse: spinner models liset  === "+spinnerModelList);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(TimeSetActivity.this);
                        listAdapter = new AdapterSpinnerTimeSet(spinnerModelList, TimeSetActivity.this);
                        binding.recyclerViewtime.setAdapter(listAdapter);
                        binding.recyclerViewtime.setLayoutManager(layoutManager);
                    }else {

                        Log.d(TAG, "onResponse: else  == ");
                    }

                }catch (Exception e) {
                    Log.e(TAG, "onResponse: Exception  =  " +e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public void deleteItem(int position, List<String> spinnerList, String spinnerSelectedItem) {
        for (int i = 0; i < spinnerList.size(); i++) {
            if (spinnerList.get(i).matches(openTime)) {
                //here is we have find out which position previous spinner item in spinnerList
                List<String> spinnerUpdatedList = new ArrayList<>();
                for (int j = i; j < spinnerList.size(); j++) {
                    spinnerUpdatedList.add(spinnerList.get(j));
                }
                closeTimeSpinnerAdapter = new ArrayAdapter<>(TimeSetActivity.this, android.R.layout.simple_spinner_dropdown_item,
                        spinnerUpdatedList);
                closeSpinner.setAdapter(closeTimeSpinnerAdapter);

            }
        }
    }
    public void Call_API_setTime()
    {
        List<Time_Set_Models> time_set_modelsList = new ArrayList<>();
        Date d = new java.sql.Date(System.currentTimeMillis());
        SpinnerModel date = spinnerModelList.get(spinnerModelList.size() - 1);
        Log.d(TAG, "Call_API_setTime: " + date);
        String day = date.getDay();
        String open = date.getOpeningTime();
        String close = date.getClosingTime();

        Log.d(TAG, "Call_API_setTime:  day = "+date.getDay()+"  open = "+date.getOpeningTime()+" close = "+date.getOpeningTime()+" timestamp = ");
        ApiServies servies = RetrofitClient.getClient().create(ApiServies.class);
        servies.addAllotmentTime(sessionConfig.getPhone() + "",day,open,close).enqueue(new Callback<Time_Set_Models>() {
            @Override
            public void onResponse(Call<Time_Set_Models> call, Response<Time_Set_Models> response) {
                Log.d(TAG, "onResponse: ==== "+response.body());

            }

            @Override
            public void onFailure(Call<Time_Set_Models> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());

            }
        });


    }
}
