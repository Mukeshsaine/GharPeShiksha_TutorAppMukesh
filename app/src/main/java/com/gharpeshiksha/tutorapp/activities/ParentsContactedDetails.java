package com.gharpeshiksha.tutorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gharpeshiksha.tutorapp.R;

public class ParentsContactedDetails extends AppCompatActivity {

    String name, date, fees, address, altContact, contact, userClass, teachingMode;
    int enqId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_contacted_details);
        //get all the values from intent extras of Activity
        Intent i = getIntent();
        date = i.getStringExtra("date");
        fees = i.getStringExtra("fees");
        address = i.getStringExtra("address");
        altContact = i.getStringExtra("alt_contact");
        contact = i.getStringExtra("contact");
        name = i.getStringExtra("name");
        userClass = i.getStringExtra("cls");
        enqId = i.getIntExtra("enq_id", 0);
        teachingMode = i.getStringExtra("teaching_mode");

        Log.v("contactedDetails---", date + fees + address + altContact + contact + name + userClass + enqId + teachingMode);
    }
}