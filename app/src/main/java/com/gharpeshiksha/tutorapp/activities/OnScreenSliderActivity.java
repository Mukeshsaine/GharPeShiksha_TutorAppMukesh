package com.gharpeshiksha.tutorapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.Adapter.CustomViewPager;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;


public class OnScreenSliderActivity extends AppCompatActivity {

    ViewPager viewPager;
    int[] images_id = {R.drawable.slider2_3, R.drawable.slider1_3, R.drawable.slider3_1};
    // int MY_REQUEST_CODE = 6000;
    LinearLayout Dots_Layout;
    ImageView[] Dots;
    CardView SignUp, SignIn;
    Boolean doubleTapBack = false;
    SessionConfig sessionObj;
    Long phone;
    String uploaddocument = "true";


    public final String TAG = OnScreenSliderActivity.this.toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_screen_slider);

        try {
            uploaddocument = getIntent().getStringExtra("upload");
        } catch (Exception e) {

        }

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);

        String verified = sharedPreferences.getString("VerifiedContact", sharedPreferences.getString("VerifiedContact", "failed"));

        Log.d(TAG, "onCreate: SharedPreference VerifiedContact : " + verified);

        sessionObj = new SessionConfig(getApplicationContext());

        if (verified.matches("Success")) {
            if (sessionObj.LoginStatusRead()) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("uploaddoc", uploaddocument).
                        putExtra("sortDis", "firsttime"));
                finish();
            }
        }

        variablsInitializes();

        createdots(0);
        CustomViewPager customViewPager = new CustomViewPager(this, images_id);

        viewPager.setAdapter(customViewPager);

        transparantstausbar();
        listeners();

        try {
            FirebaseMessaging.getInstance().subscribeToTopic("general")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.e(TAG, "onComplete: 1" + task.getResult());

                        }
                    });
        } catch (Exception e) {

        }

        try {
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(OnScreenSliderActivity.this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            Uri deepLink = null;

                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                                Log.e(TAG, "linkReceived " + deepLink);

                                String[] a = deepLink.toString().split("\\?");

                                Log.e("DynamicLinksSlider", a[a.length - 1]);

                                if (a[a.length - 1].equals("androidapp")) {
                                    Log.e("Links", "Downloaded via Link From PlayStore");
                                }


                                if (deepLink.getQueryParameter("from") != null) {
                                    Log.e(TAG, "getQueryParamenter :  " + deepLink.getQueryParameter("from"));
                                    new SessionConfig(OnScreenSliderActivity.this).setSource(deepLink.getQueryParameter("from"));
                                }

                            }
                        }
                    }).addOnFailureListener(OnScreenSliderActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "getDynamicLink:onFailure", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "DynamicLink: " + e.getMessage());
        }
    }


    private void listeners() {


        /*SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(getApplicationContext(), SignUpAndIn.class));
                finish();


            }
        });*/

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpAndIn.class));
                finish();
            }
        });

        viewpagerlistener();
    }

    private void variablsInitializes() {


        SignUp = findViewById(R.id.sign_up);
        SignIn = findViewById(R.id.sign_in);
        Dots_Layout = findViewById(R.id.dotsLayout);
        viewPager = findViewById(R.id.view_pager);
    }

    private void transparantstausbar() {

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void viewpagerlistener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                createdots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleTapBack) {
            super.onBackPressed();
        } else {

            Toast.makeText(OnScreenSliderActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            this.doubleTapBack = true;

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleTapBack = false;
                }
            }, 2000);
        }
    }

    private void createdots(int currant_position) {

        try {
            if (Dots_Layout != null) {

                Dots_Layout.removeAllViews();

            }
            Dots = new ImageView[3];

            for (int i = 0; i < 3; i++) {

                Dots[i] = new ImageView(this);
                if (i == currant_position) {

                    Dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));

                } else {
                    Dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dots));
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);
                Dots_Layout.addView(Dots[i], params);
            }

        } catch (Exception e) {
            Log.e(TAG, "createdots: ", e);
        }
    }

}
