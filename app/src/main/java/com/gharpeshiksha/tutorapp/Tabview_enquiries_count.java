package com.gharpeshiksha.tutorapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Tabview_enquiries_count extends AppCompatActivity {

    int numberofenqiries;
    TextView textView;

    @Override
    protected void onStart() {
        super.onStart();
        textView = findViewById(R.id.bjhj);
        textView.setText("45");
    }


// this is commented for error on sing apk biuld
/*    public Tabview_enquiries_count(int numberofenqiuries) {
        this.numberofenqiries = numberofenqiuries;
        // onStart();
    }*/

    public Tabview_enquiries_count() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiries__count);


    }
}
