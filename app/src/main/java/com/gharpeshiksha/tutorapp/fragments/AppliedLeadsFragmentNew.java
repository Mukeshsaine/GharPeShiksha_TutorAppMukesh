package com.gharpeshiksha.tutorapp.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.BasicFeatures;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.FragmentLifecycle;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.UpgradeActivity;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.Adapter.AppliedLeadsAdapter;
import com.gharpeshiksha.tutorapp.data_model.AppliedLeads;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AppliedLeadsFragmentNew extends Fragment implements FragmentLifecycle {


    private String url = "https://api.gharpeshiksha.com" + "/GetClasses/appliedleadsnew";
    private String cursor = "none";
    private int flag = 0;

    private VolleyHelper volleyHelper;
    private ArrayList<AppliedLeads> arrayList;
    private SessionConfig sessionConfig;
    private AppliedLeadsAdapter appliedLeadsAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressGetLeads;
    private LinearLayoutManager linearLayoutManager;
    private BasicFeatures basicFeatures;
    private Dialog dialog;

    private Boolean dataAvailable = true;
    private Boolean isSrolling = false;
    private Dashboard dashboard;

    private AppliedLeads appliedLeads;

    private TextView upgradeAppButton;
    private NestedScrollView nonpaidLayout;
    private Button retry;
    private View v;
    private String TAG = "AppliedLeads.java";

    public AppliedLeadsFragmentNew() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.applied_lead_new_fragment, container, false);

        Log.d("AppliedLeadFragment", "onCreateView:  New");

        cursor = "none";
        progressGetLeads = v.findViewById(R.id.progressBar);
        recyclerView = v.findViewById(R.id.leadRecyclerView);
        upgradeAppButton = v.findViewById(R.id.upgradeAppButton);
        nonpaidLayout = v.findViewById(R.id.relativelayoutnonpaid);


        linearLayoutManager = new LinearLayoutManager(getActivity());
        arrayList = new ArrayList<>();
        appliedLeadsAdapter = new AppliedLeadsAdapter(getActivity(), arrayList);
        sessionConfig = new SessionConfig(v.getContext());
        volleyHelper = new VolleyHelper();
        dashboard = new Dashboard();
        recyclerView.setAdapter(appliedLeadsAdapter);
        basicFeatures = new BasicFeatures();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isSrolling = true;
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                long firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                long visibleItemCount = linearLayoutManager.getChildCount();
                long totalItemCount = linearLayoutManager.getItemCount();

                Log.d("onScrollStateChanged", "onScrolled: bsdfhb : \n" + "isSrolling " + isSrolling + "\nfirstVisibleItem " + firstVisibleItem + "\nvisibleItemCount " + visibleItemCount + "\ntotalItemCount " + totalItemCount);
                if (isSrolling && (firstVisibleItem + visibleItemCount == totalItemCount) && dataAvailable) {
                    isSrolling = false;
                    progressGetLeads.setVisibility(View.VISIBLE);
                    getLeads(url);
                }

            }
        });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(8);
        upgradeAppButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), UpgradeActivity.class)));

        if (isNetworkAvailable()) {

            checkStatus();

        } else {
            noNetworkDialog();
        }

        return v;
    }


    private void checkStatus() {

        basicFeatures.CheckPaymentStatus(getContext(), sessionConfig.getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {
                flag = 1;
                Log.d("BasicFeaturesApplied", "Result: BasicFeatures PaymentStatus : " + status);
                if (status.matches("nonpaid") || status.matches("free")) {
                    nonpaidLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    progressGetLeads.setVisibility(View.GONE);


                } else if (status.matches("expired") || status.matches("freeactivation") || status.matches("active")) {
                    nonpaidLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    getLeads(url);
                }

            }

            @Override
            public void onError() {
                flag = 0;
            }

        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void noNetworkDialog() {

        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.no_network_dialog);
        retry = dialog.findViewById(R.id.retry);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        retry.setOnClickListener(v -> {
            dialog.dismiss();
            if (isNetworkAvailable()) {
                dialog.dismiss();
                if (flag == 0) {
                    checkStatus();
                    /*getLeads(url);*/

                }


            } else {
                dialog.dismiss();
                noNetworkDialog();
            }
        });

    }

    private void getLeads(String url) {

        progressGetLeads.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("phone", "" + sessionConfig.getPhone());
        if (!cursor.equals("none")) {
            params.put("cursor", cursor);
        }
        volleyHelper.VolleyPostRequest(v.getContext(), url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG, "onSuccess: response: " + response);
                    if (jsonObject.has("cursor")) {
                        cursor = jsonObject.getString("cursor");
                        if (cursor.equals("") || cursor.isEmpty()) {
                            cursor = "DON'T_FETCH_DATA";
                            dataAvailable = false;
                        }
                    } else {
                        cursor = "none";
                    }

                    if (jsonObject.has("classescount")) {
                        dashboard.setBadgeApplied(Integer.parseInt(jsonObject.getString("classescount")));
                    }
                    if (jsonObject.has("classes")) {


                        JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("classes").toString());
                        if (jsonArray.length() == 0) {
                            dataAvailable = false;
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject part = jsonArray.getJSONObject(i);
                            Log.d(TAG, "part: " + part + "\n");

                            String feedback_given = part.getString("feedback_given");
                            String feedback = part.getString("feedback");
                            String our_action = part.getString("our_action");
                            String parents_msg = part.getString("parents_msg");
                            String name = part.getString("name");
                            String classes = part.getString("class");
                            String subjects = part.getString("subjects");
                            String budget = part.getString("budget");
                            String enq_id = part.getString("enq_id");
                            String distance = part.getString("distance");
                            String posted_on = part.getString("posted_on");
                            String viewed_on = part.getString("viewed_on");
                            String contact = part.getString("contact");
                            String area = part.getString("area");
                            String areaDecoded = area;

                            String otp_given = "non";
                            if (part.has("demo_otp_given"))
                                otp_given = part.getString("demo_otp_given");

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                areaDecoded = Html.fromHtml(area, Html.FROM_HTML_MODE_COMPACT).toString();
                            }
                            appliedLeads = new AppliedLeads(name, posted_on, budget, viewed_on, areaDecoded, (distance + " KM"),
                                    ("Tutor Requirement for " + subjects + " for " + classes),
                                    parents_msg, feedback, our_action, enq_id, feedback_given, contact,otp_given/*, gender*/,
                                    part.getString("studentUUID"), part.getString("tutorUUID"),
                                    part.getString("default_profile_pic")
                                    );

                            arrayList.add(appliedLeads);
                        }
                        appliedLeadsAdapter.notifyDataSetChanged();

                        if (progressGetLeads.isShown()) {
                            progressGetLeads.setVisibility(View.GONE);
                        }
                    }


                } catch (Exception e) {
                    Log.d(TAG, "onCatch: " + e);
                }

                appliedLeadsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "onError: " + error);
            }
        });

    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        cursor = "none";
    }
}
