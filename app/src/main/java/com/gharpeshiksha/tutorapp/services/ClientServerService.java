package com.gharpeshiksha.tutorapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.Adapter.Adapter_Chats;
import com.gharpeshiksha.tutorapp.data_model.Model_Chats;
import com.gharpeshiksha.tutorapp.data_model.Model_archived;
import com.gharpeshiksha.tutorapp.utils.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Below is an Service application component to perform long background operation.
 * That follows a Bounded lifecycle and runs till application component is not destroyed.
 */
public class ClientServerService extends Service {
    public IBinder iBinder = new MyBinder();
    private String TAG = "ClientServerService.java";
   // private List<CountDownTimer> timerList = new ArrayList<>();

    @Override
    public int onStartCommand(Intent i, int flag, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return Service.START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return iBinder;
    }
     @Override
    public void onCreate() {
        //onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        //destroy
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //unbind
        Log.d(TAG, "onUnbind: ");
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        //on rebind
        Log.d(TAG, "onRebind: ");
    }


    /**
     * Below method sort RecyclerView datasource list by distance or by recency. submit list to ListAdapter
     * after sort list.
     */
    public ArrayList<Model_archived> sortList(CharSequence text, ArrayList<Model_archived> model_archivedList) {
        if (text.equals("distance")) {
            //sort by distance
            Comparator<Model_archived> comparator = (Comparator<Model_archived>) (o1, o2) -> {
                if (o1.getDistance() > o2.getDistance()) {
                    return 1;
                } else if (o1.getDistance() == o2.getDistance()) {
                    return 0;
                } else {
                    return -1;
                }
            };
            Collections.sort(model_archivedList, comparator);
            Log.d(TAG, "sortList: distance sorted");
            return model_archivedList;

        } else if (text.equals("recency")) {
            //sort by recency.
            Comparator<Model_archived> comparator = (Comparator<Model_archived>) (o1, o2) -> {
                if(o1.getTimestamp() < o2.getTimestamp()) {
                    return 1;
                } else if(o1.getTimestamp() == o2.getTimestamp()) {
                    return 0;
                } else {
                    return -1;
                }
            };
            Collections.sort(model_archivedList, comparator);
            Log.d(TAG, "sortList: recency sorted");
            return model_archivedList;
        }
        return model_archivedList;
    }

    public ArrayList<Model_Chats> filterList(ArrayList<Model_Chats> allList, String chatStatus, Context ctx, RecyclerView recyclerView) {
        ArrayList<Model_Chats> list = new ArrayList<>();
        try {
//            Log.d(TAG, "filterList: before " + chatStatus);
            for(Model_Chats m: allList) {
                if(chatStatus.matches("accepted")) {
                    if(m.getIsApproved().matches("accepted")) {
                        m.setChatStatus("Accepted");
                        list.add(m);
                    }
                } else if(chatStatus.matches("sent")) {
                    if(m.getIsApproved().matches("chatInitiated") && m.getSendBy().toLowerCase().matches("tutor")) {
                        m.setChatStatus("Sent");
                        list.add(m);
                    } else if(m.getIsApproved().matches("declined") && m.getSendBy().toLowerCase().matches("tutor")) {
                        m.setChatStatus("Declined by student");
                        list.add(m);
                    }
                } else if(chatStatus.matches("received")) {
                    if(m.getIsApproved().matches("chatInitiated") && m.getSendBy().toLowerCase().matches("student")) {
                        m.setChatStatus("Received");
                        list.add(m);
                    } else if(m.getIsApproved().matches("declined") && m.getSendBy().toLowerCase().matches("student")) {
                        m.setChatStatus("Declined by you");
                        list.add(m);
                    }
                }
            }

            Adapter_Chats adapter_chats = new Adapter_Chats(list, ctx);
            recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
            recyclerView.setAdapter(adapter_chats);
            Log.d(TAG, "filterList: after " + allList);
        } catch (Exception e) {
            Log.d(TAG, "filterList error: " + e);
        }
        return list;
    }
    public void unBindService() {
        unBindService();
        Log.d("ClientServer.java", "print: service");
    }

    /**
     * Below is Binder method to communicate between a service and a Application component.
     */
    public class MyBinder extends Binder {
        public ClientServerService getBinder() {
            return ClientServerService.this;
        }
    }

}
