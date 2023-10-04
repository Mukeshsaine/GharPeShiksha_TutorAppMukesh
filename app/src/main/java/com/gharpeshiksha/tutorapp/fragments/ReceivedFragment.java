package com.gharpeshiksha.tutorapp.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gharpeshiksha.tutorapp.Adapter.Adapter_Chats;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.Model_Chats;
import com.gharpeshiksha.tutorapp.databinding.FragmentChatCategoryBinding;
import com.gharpeshiksha.tutorapp.services.ClientServerService;

import java.util.ArrayList;

public class ReceivedFragment extends Fragment {


    private ArrayList<Model_Chats> list = new ArrayList<>();
    private String chatStatus = "";
    private Adapter_Chats adapter_chats;
    private RecyclerView recyclerView;
    private FragmentChatCategoryBinding binding;
    private String TAG = "ReceivedFragment.java";
    private ClientServerService clientServerService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment by passing LayoutInflater in its ViewBinding file
        //Contains its layout Java Object Hierarchy.
        binding = FragmentChatCategoryBinding.inflate(getLayoutInflater(), container, false);
        getActivity().bindService(new Intent(getActivity(), ClientServerService.class),
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        if(list.size() == 0) {
                            binding.upgradeLayout.setVisibility(View.VISIBLE);
                        }
                        clientServerService = ((ClientServerService.MyBinder) service).getBinder();
                        clientServerService.filterList(list, chatStatus, getActivity(), binding.RecycleViewChatsStudents);
                        Log.d(TAG, "onServiceConnected: size " + list);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, Context.BIND_AUTO_CREATE);

        Log.d(TAG, "onCreateView: " + list);

        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
//        binding.upgradeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ChatsFragment fr = ((ChatsFragment) ChatCategoryFragment.this.getParentFragment());
//                fr.getDataLocallyFromApi();
////                startActivity(new Intent(getActivity(), UpgradeActivity.class));
//            }
//        });
    }

    public ReceivedFragment(ArrayList<Model_Chats> list, String chatStatus) {
        Log.d(TAG, "ChatCategoryFragment: constructor " + list);
        this.list = list;
        this.chatStatus = chatStatus;
    }

    public ReceivedFragment() {
        // Required empty public constructor
    }
}