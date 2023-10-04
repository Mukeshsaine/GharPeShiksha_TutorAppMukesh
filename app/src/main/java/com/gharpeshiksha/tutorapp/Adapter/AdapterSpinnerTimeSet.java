package com.gharpeshiksha.tutorapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.SpinnerModel;
import com.gharpeshiksha.tutorapp.data_model.Time_Set_Models;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSpinnerTimeSet extends RecyclerView.Adapter<AdapterSpinnerTimeSet.Viewholder> {
    private List<SpinnerModel> spinnerModelList;
    private Context context;
    Dialog dialog;
    Dialog dialog1;
    RecyclerView recyclerView;
    int pos;
    SessionConfig sessionConfig;
    AdapterSpinnerTimeSet listAdapter1;
    String openTime1, closeTime1, daytime;
    View view1;
    public static List<String> list;
    public static List<String> list1;
    public static List<String> list2;
    String tutoruuid;
    List<Time_Set_Models> time_set_modelsList = new ArrayList<>();
    public static ArrayAdapter<String> firsdayspinnerAdapter;
    public ArrayAdapter<String> openTimeSpinnerAdapter;
    private ArrayAdapter<String> closeTimeSpinnerAdapter;
    private String TAG = "AdapterSpinner.java";
    // public static Spinner daySpinner, openSpinner, closeSpinner;
    String day = "", openTime = "", closeTime = "";

    //public ArrayAdapter<String> firsdayspinnerAdapter;


    public AdapterSpinnerTimeSet(List<SpinnerModel> spinnerModelList, Context context) {
        this.spinnerModelList = spinnerModelList;
        this.context = context;
    }


    @NonNull
    @Override
    public AdapterSpinnerTimeSet.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_cantact_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        SpinnerModel spinnerModel = spinnerModelList.get(position);
        holder.day.setText(spinnerModel.getDay());
        holder.time1.setText(spinnerModel.getOpeningTime());
        holder.time2.setText(spinnerModel.getClosingTime());

        //delete time item
        holder.DeleteIcon.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Image icon delete", Toast.LENGTH_SHORT).show();
            dialog = new Dialog(view.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.time_set_delete, null);
            view1.findViewById(R.id.delete1).setOnClickListener(view2 -> {
                Toast.makeText(context, "delete item", Toast.LENGTH_SHORT).show();/*
                spinnerModelList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, spinnerModelList.size());*/

                Delete_Time_APi(position);
                dialog.dismiss();
            });

            view1.findViewById(R.id.Edit_TimeBox);
            view1.setOnClickListener(view2 -> {
                dialog1 = new Dialog(view.getContext());
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.time_schedule, null);
                // Toast.makeText(context, "this is Edit Time", Toast.LENGTH_SHORT).show();

                list = new ArrayList<>();
                list.add("Choose Day");
                list.add("Monday");
                list.add("Tuesday");
                list.add("Wednesday");
                list.add("Thursday");
                list.add("Friday");
                list.add("Saturday");
                list.add("Sunday");

                //   daySpinner1 = view.findViewById(R.id.daySpinner);
                view1.findViewById(R.id.Save_Item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(context, "Time Edited Successfully", Toast.LENGTH_SHORT).show();
                        if (!daytime.isEmpty() && !openTime1.isEmpty() && !closeTime1.isEmpty()) {
                            //call api to save edit details
                            Edit_time_api(daytime, openTime1, closeTime1, position, spinnerModel.getTimestamp());
//                            spinnerModelList.add(new SpinnerModel(day, openTime, closeTime));
                            //  latestDaySelected = day;
                            Log.d(TAG, "onClick: " + daytime + "\n" + openTime1 + "\n" + closeTime1);
                           /* spinnerModel.setDay(daytime);
                            spinnerModel.setOpeningTime(openTime1);
                            spinnerModel.setClosingTime(closeTime1);
                            holder.day.setText(spinnerModel.getDay());
                            holder.time1.setText(spinnerModel.getOpeningTime());
                            holder.time2.setText(spinnerModel.getClosingTime());*/

                            dialog1.dismiss();
                        } else {
                            Toast.makeText(view.getContext(), "Please Select All Values", Toast.LENGTH_SHORT).show();
                        }
                        dialog1.dismiss();
                    }
                });


                timeSet_Methods(view1, spinnerModel);
                isFirstTime = false;
                dialog1.setContentView(view1);
                dialog1.show();
                dialog.dismiss();
            });


            dialog.setContentView(view1);
            dialog.show();
        });


    }

    @Override
    public int getItemCount() {
        return spinnerModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {


        TextView day, time1, time2;
        Spinner Opentime, closeTime, daySpinner1;
        ImageView DeleteIcon;
        Button Save_Item1;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            time1 = itemView.findViewById(R.id.openTime);
            time2 = itemView.findViewById(R.id.closeTime);
            DeleteIcon = itemView.findViewById(R.id.DeleteItem);

        }
    }
    private boolean isFirstTime = false;

    public void timeSet_Methods(View view, SpinnerModel itemDataObj) {

        firsdayspinnerAdapter = new ArrayAdapter<>(view1.getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        Spinner daySpinner = view.findViewById(R.id.daySpinner);
        daySpinner.setAdapter(firsdayspinnerAdapter);
        int dayPos = list.indexOf(itemDataObj.getDay());
        daySpinner.setSelection(dayPos);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    daytime = adapterView.getItemAtPosition(position).toString();
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

        openTimeSpinnerAdapter = new ArrayAdapter<>(view1.getContext(), android.R.layout.simple_spinner_dropdown_item, list1);
        Spinner openSpinner = view1.findViewById(R.id.openSpinner);
        openSpinner.setAdapter(openTimeSpinnerAdapter);
        int openPos = list1.indexOf(itemDataObj.getOpeningTime());
        openSpinner.setSelection(openPos);
        openSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && isFirstTime) {
                    openTime1 = adapterView.getItemAtPosition(position).toString();
                    deleteItem(position, list2, openTime1);
                    Log.d(TAG, "onItemSelected: open spinner " + openTime1);
                } else {
                    isFirstTime = true;
                    openTime1 = adapterView.getItemAtPosition(position).toString();
                    Log.d(TAG, "onItemSelected: open spinner " + openTime1);
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

        closeTimeSpinnerAdapter = new ArrayAdapter<>(view1.getContext(), android.R.layout.simple_spinner_dropdown_item, list2);
        Spinner closeSpinner = view1.findViewById(R.id.closeSpinner);
        closeSpinner.setAdapter(closeTimeSpinnerAdapter);
        int closePos = list2.indexOf(itemDataObj.getClosingTime());
        closeSpinner.setSelection(closePos);
        closeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    closeTime1 = adapterView.getItemAtPosition(position).toString();
                    Log.d(TAG, "onItemSelected: close spinner " + openTime1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Log.d(TAG, "timeSet_Methods: positions: " + dayPos + ", " + openPos + ", " + closePos);
    }

    public void deleteItem(int postion, List<String> SpinnerList, String openTime1) {

        for (int i = 0; i < SpinnerList.size(); i++) {
            if (SpinnerList.get(i).matches(openTime1)) {
                List<String> listview = new ArrayList<>();
                for (int j = i; j < SpinnerList.size(); j++) {
                    listview.add(SpinnerList.get(j));
                }
                closeTimeSpinnerAdapter = new ArrayAdapter<>(view1.getContext(), android.R.layout.simple_spinner_dropdown_item, listview);
                Spinner closeSpinner = view1.findViewById(R.id.closeSpinner);
                closeSpinner.setAdapter(closeTimeSpinnerAdapter);
            }
        }
    }

    public void Delete_Time_APi(int position) {
        SpinnerModel date = spinnerModelList.get(position);
        //  SpinnerModel date1 = spinnerModelList.add(time_set_modelsList.size());
        String timestamp = String.valueOf(date.getTimestamp());
        Log.d(TAG, "Delete_Time_APi: " + timestamp);
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.deleteAllotmentTime(timestamp).enqueue(new Callback<Time_Set_Models>() {
            @Override
            public void onResponse(Call<Time_Set_Models> call, Response<Time_Set_Models> response) {

                Log.d(TAG, "onResponse: delete  === " + response.body());
                spinnerModelList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, spinnerModelList.size());
            }

            @Override
            public void onFailure(Call<Time_Set_Models> call, Throwable t) {
                Log.d(TAG, "onFailure: delele == " + t.getMessage());
            }
        });
    }

    public void Edit_time_api(String day, String open, String close, int position, String timestamp) {
        time_set_modelsList = new ArrayList<>();
        sessionConfig = new SessionConfig(context);
        Log.d(TAG, "Edit_time_api edit: " + "\n" + day + "\n" + open + "\n" + close + "\n" + timestamp);

        ApiServies apiService = RetrofitClient.getClient().create(ApiServies.class);
        apiService.EditAllotmentTime(timestamp, day, open, close, sessionConfig.getPhone() + "").enqueue(new Callback<Time_Set_Models>() {
            @Override
            public void onResponse(Call<Time_Set_Models> call, Response<Time_Set_Models> response) {
                Log.d(TAG, "onResponse: === Edit   == " + response.body());

                if (response.isSuccessful()) {
                    Time_Set_Models res = response.body();
                    if(res != null) {
                        SpinnerModel listItem = spinnerModelList.get(position);
                        listItem.setDay(res.getDay());
                        listItem.setOpeningTime(res.getOpening());
                        listItem.setClosingTime(res.getClosing());
                        Log.d(TAG, "onResponse: spinner == " + listItem);
                        notifyItemChanged(position);
                    }
                } else {
                    Log.d(TAG, "onResponse: else == ");
                }
            }

            @Override
            public void onFailure(Call<Time_Set_Models> call, Throwable t) {

            }
        });
    }
}
