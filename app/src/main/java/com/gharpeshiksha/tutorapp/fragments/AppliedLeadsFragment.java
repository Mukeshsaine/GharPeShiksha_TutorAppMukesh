package com.gharpeshiksha.tutorapp.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

public class AppliedLeadsFragment extends Fragment implements FragmentLifecycle {
    private static String TAG = "AppliedLeadsFragment";

    View view, view1;
    private TextView textView, upgradeAppButton;
    private RecyclerView recyclerView;


    private ArrayList<AppliedLeads> arrayList;
    private long phone;
    private CardView cardView;
    SessionConfig sessionConfig;
    private Dialog dialog;
    private Button retry;
    private int flag = 0;
    int classesLoaded = 0;
    private String statusUrl, url;
    static Boolean paidMember;
    private Dashboard dashboard;
    private boolean dataAvailable = true;
    private boolean isSrolling = false;
    private boolean isLastItemFetched;
    ProgressDialog progressDialog;
    private String cursor = "none";
    private AppliedLeads appliedLeads;
    private LinearLayoutManager manager;
    private AppliedLeadsAdapter adapter;
    private VolleyHelper volleyHelper;
    private BasicFeatures basicFeatures;

    private ProgressBar progressGetLeads;

    public AppliedLeadsFragment() {
    }


/*
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }*/


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_applied_leads, container, false);
        cursor = "none";
        volleyHelper = new VolleyHelper();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");

        progressGetLeads = view.findViewById(R.id.progressGetLeads);
        basicFeatures = new BasicFeatures();
        dashboard = new Dashboard();
        manager = new LinearLayoutManager(getContext());
        sessionConfig = new SessionConfig(getActivity());
        phone = sessionConfig.getPhone();
        url = "https://api.gharpeshiksha.com" + "/GetClasses/appliedleadsnew";
        statusUrl = "https://api.gharpeshiksha.com" + "/PaymentStatus/statusnew";

        upgradeAppButton = view.findViewById(R.id.upgradeAppButton);
        recyclerView = view.findViewById(R.id.applied_leads_list);
        cardView = view.findViewById(R.id.appCard);


        arrayList = new ArrayList<>();
        adapter = new AppliedLeadsAdapter(view.getContext(), arrayList);
        if (isNetworkAvailable()) {
            checkStatus(statusUrl);
            getLeads(url);
            upgradeAppButton();
        } else {
            noNetworkDialog();
        }

/*

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isSrolling = true;
                }

                manager.requestLayout();
                long firstVisibleItem = manager.findFirstVisibleItemPosition();
                long visibleItemCount = recyclerView.getChildCount();
                long totalItemCount = manager.getItemCount();

                Log.d("onScrollStateChanged", "onScrolled: bsdfhb : \n" + "isSrolling " + isSrolling + "\nfirstVisibleItem " +firstVisibleItem + "\nvisibleItemCount " + visibleItemCount + "\ntotalItemCount " +totalItemCount );

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                    long firstVisibleItem = manager.findFirstVisibleItemPosition();
                    long visibleItemCount = manager.getChildCount();
                    long totalItemCount = manager.getItemCount();

                    Log.d("onScrollStateChanged", "onScrolled: bsdfhb : \n" + "isSrolling " + isSrolling + "\nfirstVisibleItem " +firstVisibleItem + "\nvisibleItemCount " + visibleItemCount + "\ntotalItemCount " +totalItemCount  );
                    if (isSrolling && (firstVisibleItem + visibleItemCount == totalItemCount) && dataAvailable) {
                        isSrolling = false;
                        progressGetLeads.setVisibility(View.VISIBLE);
                        getLeads(url);
                    }


            }
        });
*/

        recyclerView.setLayoutManager(manager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: " + newState + "  " + dataAvailable + "  " + arrayList.size());
                long firstVisibleItem = manager.findFirstVisibleItemPosition();
                long visibleItemCount = manager.getChildCount();
                long totalItemCount = manager.getItemCount();

                Log.d("onScrollStateChanged", "onScrolled: bsdfhb : \n" + "isSrolling " + isSrolling + "\nfirstVisibleItem " + firstVisibleItem + "\nvisibleItemCount " + visibleItemCount + "\ntotalItemCount " + totalItemCount);

                if (dataAvailable && newState == 0) {
                    progressDialog.show();
                    progressGetLeads.setVisibility(View.VISIBLE);
                    getLeads(url);

                }
                if (!recyclerView.getLayoutManager().canScrollVertically()) {
                    Toast.makeText(view.getContext(), "Last", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onScrollStateChanged: LAST");

                }
            }

        });
        return view;
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
                    checkStatus(statusUrl);
                    getLeads(url);
                    upgradeAppButton();
                }


            } else {
                dialog.dismiss();
                noNetworkDialog();
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkStatus(String url) {
        Log.d(TAG, "Result: BasicFeatures PaymentStatus : " + "hjhjds");
        basicFeatures.CheckPaymentStatus(view.getContext(), phone, new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {
                flag = 1;
                Log.d(TAG, "Result: BasicFeatures PaymentStatus : " + status);
                if (status.matches("nonpaid") || status.matches("free")) {
                    cardView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    paidMember = false;
                } else if (status.matches("expired") || status.matches("freeactivation") || status.matches("active")) {
                    cardView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    paidMember = true;
                }

            }

            @Override
            public void onError() {
                flag = 0;
            }

        });
    }

    private void getLeads(String url) {

        Map<String, String> params = new HashMap<>();
        params.put("phone", "" + phone);
        if (!cursor.equals("none")) {
            params.put("cursor", cursor);
        }

        volleyHelper.VolleyPostRequest(view.getContext(), url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("cursor")) {
                        cursor = jsonObject.getString("cursor");
                        if (cursor.equals("") || cursor.isEmpty()) {
                            cursor = "DON'T_FETCH_DATA";
                            dataAvailable = false;
                            progressGetLeads.setVisibility(View.GONE);

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
                            if (part.has("otp_given"))
                                otp_given = part.getString("otp_given");

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                areaDecoded = Html.fromHtml(area, Html.FROM_HTML_MODE_COMPACT).toString();
                            }
                            appliedLeads = new AppliedLeads(name, posted_on, budget, viewed_on, areaDecoded, (distance + " KM"),
                                    ("Tutor Requirement for " + subjects + " for " + classes),
                                    parents_msg, feedback, our_action, enq_id, feedback_given, contact, otp_given/*, gender*/);

                            arrayList.add(appliedLeads);

                        }
                        adapter.notifyDataSetChanged();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (progressGetLeads.isShown()) {
                            progressGetLeads.setVisibility(View.GONE);
                        }
                    }
                    classesLoaded = arrayList.size();


                } catch (Exception e) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

    }


    public void upgradeAppButton() {
        upgradeAppButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UpgradeActivity.class);
            startActivity(intent);
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
