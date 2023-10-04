package com.gharpeshiksha.tutorapp.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

public class MyAsynct extends AsyncTask<Void, Void, Void> {

    Context ctx;
    String TAG = "MyAsyncTask.java";
    ProgressDialog pd;

    public MyAsynct(Context context) {
        ctx = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(ctx);
        pd.setTitle("Loading");
        pd.setMessage("Please Wait...");
        pd.show();

        // Schedule a task to dismiss the progress dialog after a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }, 2000); // Delay in milliseconds (adjust as needed)
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Simulate some background work
        try {
            Thread.sleep(5000); // Simulate 5 seconds of work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // No need to dismiss the progress dialog here, it will be dismissed automatically
    }
}
