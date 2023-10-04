package com.gharpeshiksha.tutorapp.activities;

import android.util.Log;

public class Work implements Runnable{

    private static final String TAG = "Mytag";
    private int mThreadNo;
    public Work(int mThreadNo){
        this.mThreadNo = mThreadNo;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: "+Thread.currentThread().getName()+" start thread no =="+mThreadNo);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
