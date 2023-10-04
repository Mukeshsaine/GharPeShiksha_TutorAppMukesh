package com.gharpeshiksha.tutorapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.gharpeshiksha.tutorapp.FragmentLifecycle;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.VolleyService;
import com.gharpeshiksha.tutorapp.data_model.ClassesForMe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EnquiriesFragment extends Fragment implements FragmentLifecycle {

    private static String TAG = "EnquiriesFragment";
    static View view;
    TextView textView;
    static RecyclerView recyclerView;
    ArrayList<ClassesForMe> arrayList;
    RequestQueue requestQueue;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Network network;
    final Map<String, String> empty = new HashMap<>();
    DiskBasedCache cache;
    ClassesForMe classesForMe;
    Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    ProgressBar progressBar, endlessBar;
    String classes_url;
    VolleyService volleyService;
    static Context context;
    //FloatingActionButton filterButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_enquiries, container, false);

        recyclerView = view.findViewById(R.id.enquiries_list);
        recyclerView.setHasFixedSize(true);

        // filterButton = view.findViewById(R.id.filter_classes);


        arrayList = new ArrayList<>();

        classes_url = "https://api.gharpeshiksha.com" + "/GetClasses/allenquiries";

        endlessBar = view.findViewById(R.id.endlessBar);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
       /* final LinearLayout linearLayout;

        Log.d(TAG, "onCreateView: classesUrl" + classes_url);
        volleyService = new VolleyService(getActivity(), classes_url, arrayList, recyclerView, mSwipeRefreshLayout, view, 1, empty,linearLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        volleyService = new VolleyService(view.getContext(), classes_url, arrayList, recyclerView, mSwipeRefreshLayout, view, 1, empty,linearLayout);
                    }
                }, 3000);
            }
        });*/

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
/*
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);*/

        /*filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EnquiryFilter.class));
            }
        });*/

        return view;
    }

    public void shorting(Context context, String shortby, int activity_no) {

        if (shortby.equals("recency")) {
            new VolleyService(context, shortby, activity_no, recyclerView);
            Toast.makeText(context, shortby, Toast.LENGTH_SHORT).show();
        } else {
            new VolleyService(context, shortby, activity_no, recyclerView);
            Toast.makeText(context, shortby, Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        this.context = getActivity();
        recyclerView = view.findViewById(R.id.enquiries_list);
        Log.e(TAG, "onResume: recyclerView" + recyclerView);

    }
}