package com.gharpeshiksha.tutorapp.fragments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gharpeshiksha.tutorapp.Adapter.Adapter_Chats;
import com.gharpeshiksha.tutorapp.Adapter.ChatViewPagerAdapter;
import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.Model_Chats;
import com.gharpeshiksha.tutorapp.databinding.FragmentChatsBinding;
import com.gharpeshiksha.tutorapp.localdb.Contract;
import com.gharpeshiksha.tutorapp.localdb.LocalSQLiteDbHandler;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import java.security.AlgorithmParameterGenerator;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsFragment extends Fragment {

    Boolean isAllFabsVisible;
    private FragmentChatsBinding binding;
    SessionConfig sessionConfig;
    String TAG = "ChatsFragment.java";
    List<Model_Chats> model_chatsList;
    private AlgorithmParameterGenerator ViewAnimation;
    private LocalSQLiteDbHandler dbHandler;
    private Adapter_Chats adapter_chats;
    private String lastChatTimestamp = "0";
    private Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        sessionConfig = new SessionConfig(getActivity());
        model_chatsList = new ArrayList<>();
        dbHandler = new LocalSQLiteDbHandler(getActivity());
//        binding.RecycleViewChatsStudents.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.swipeRef.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorRed,
                R.color.colorGreen,
                R.color.grey);
        binding.swipeRef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastChatTimestamp = "0";
                getDataLocallyFromApi();
                binding.swipeRef.setRefreshing(false);
            }
        });

        getDataLocallyFromApi();

        return binding.getRoot();
    }

    public void getDataLocallyFromApi() {
        getDisplayChats(false);
//        Cursor cursor = dbHandler.getDisplayChats(dbHandler.getReadableDatabase());
//        if(cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            lastChatTimestamp = cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.TIMESTAMP));
//            Log.d(TAG, "onCreateView: valid " + lastChatTimestamp);
//            //valid
//            do {
//                model_chatsList.add(new Model_Chats(
//                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.PIC_URL)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.STUDENT_UUID)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.STUDENT_NAME)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.TUTOR_UUId)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.MESSAGE)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.TIMESTAMP)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.TIMESTAMP_IN_MILLIS)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.ENQ_ID)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.IS_APPROVED)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.DisplayChats.SEND_BY))
//                ));
//            } while(cursor.moveToNext());
//
//            getDisplayChats(false);
//        } else {
//            dbHandler.deleteDisplayChats(dbHandler.getWritableDatabase());
//            getDisplayChats(false);
//        }
    }

    private void getDisplayChats(boolean refreshed) {
        lastChatTimestamp = "0";
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        Log.d(TAG, "getDisplayChats: time === "+lastChatTimestamp);
        apiServies.DidplayChat(sessionConfig.getPhone() + "", lastChatTimestamp).enqueue(new Callback<List<Model_Chats>>() {
            @Override
            public void onResponse(Call<List<Model_Chats>> call, Response<List<Model_Chats>> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        model_chatsList.clear();
                        List<Model_Chats> list = response.body();
                        for(int i=0; i<list.size(); i++) {
                            Model_Chats m = list.get(i);
//                            dbHandler.addDisplayChats(dbHandler.getWritableDatabase(), m);
                            model_chatsList.add(m);
                            if(i == list.size() - 1) {
                                lastChatTimestamp = m.getDay();
                            }
                        }

                        ((Dashboard) getActivity()).setBadgeChat(model_chatsList.size());
                        Log.d(TAG, "onResponse: size of list: " + list);
                        setupViewPager(list);
                    } else {
                        setupViewPager(model_chatsList);
                        Log.d(TAG, "onResponse: Else ==== " + response.errorBody());
                    }
                } catch (Exception e) {
                    setupViewPager(model_chatsList);
                    Log.d(TAG, "onResponse: catch == "+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Model_Chats>> call, Throwable t) {
//                adapter_chats = new Adapter_Chats(model_chatsList, getContext());
//                binding.RecycleViewChatsStudents.setAdapter(adapter_chats);
                setupViewPager(model_chatsList);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void setupViewPager(List<Model_Chats> list) {
        ChatViewPagerAdapter pagerAdapter = new ChatViewPagerAdapter(getActivity().getSupportFragmentManager());
        Fragment fragment = new ChatCategoryFragment((ArrayList<Model_Chats>) list, "accepted");
        pagerAdapter.addFragment(fragment, "Accepted");
        Fragment fragment1 = new SentFragment((ArrayList<Model_Chats>) list, "sent");
        pagerAdapter.addFragment(fragment1, "Sent");
        Fragment fragment2 = new ReceivedFragment((ArrayList<Model_Chats>) list, "received");
        pagerAdapter.addFragment(fragment2, "Received");
        Fragment fragment3 = new AppliedLeadsFragmentNew();
        pagerAdapter.addFragment(fragment3, "Contacted");
        Log.d(TAG, "setupViewPager: chats list " + list);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.tabLay.setupWithViewPager(binding.viewPager);
    }

}