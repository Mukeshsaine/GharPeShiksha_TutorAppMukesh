package com.gharpeshiksha.tutorapp.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.Adapter.HowToEarnAdapter;
import com.gharpeshiksha.tutorapp.data_model.HowtoEarnModel;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HowToEarn extends Fragment {

    View view;
    RecyclerView recyclerView;
    TextView noData;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String url = "https://api.gharpeshiksha.com" + "/Tutor/howToEarnDetails";
    SessionConfig sessionConfig;
    List<HowtoEarnModel> list = new ArrayList<>();
    HowtoEarnModel model;
    HowToEarnAdapter adapter;
    Dashboard dashboard;


    public HowToEarn() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_how_to_earn, container, false);

        recyclerView = view.findViewById(R.id.earn_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        sessionConfig = new SessionConfig(getActivity());
        adapter = new HowToEarnAdapter(view.getContext(), list);
        requestQueue = Volley.newRequestQueue(view.getContext());
        noData = view.findViewById(R.id.noData_earn);
        dashboard = new Dashboard();
        startVolleyForData();

        return view;
    }

    private void startVolleyForData() {
        if (list != null) {
            list.clear();
        }
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("HowToEarnVolley", "onResponse: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String title = jsonObject.getString("title");
                        String description = jsonObject.getString("description");
                        String subTitle = jsonObject.getString("subTitle");
                        String buttonName = jsonObject.getString("buttonName");
                        String buttonAction = jsonObject.getString("buttonAction");
                        String status = jsonObject.getString("status");

                        model = new HowtoEarnModel(title, subTitle, description, buttonName, buttonAction, status);
                        list.add(model);
                        adapter.notifyDataSetChanged();
                    }
                    dashboard = (Dashboard) getActivity();
                    dashboard  .sessionObj.setBadgeFree(list.size());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e("HowToEarnVolley", "onResponseException: ", e);
                    noData.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HowToEarnVolley", "onResponseException: ", error);
                noData.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", "" + sessionConfig.getPhone());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
