package com.gharpeshiksha.tutorapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gharpeshiksha.tutorapp.Adapter.BottomNavViewPagerAdapter;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.activities.About;
import com.gharpeshiksha.tutorapp.activities.AllFeedbackActivity;
import com.gharpeshiksha.tutorapp.activities.AreaRangeSelection;
import com.gharpeshiksha.tutorapp.activities.Chatbot;
import com.gharpeshiksha.tutorapp.activities.ClassesActivity;
import com.gharpeshiksha.tutorapp.activities.EnquiryFilter;
import com.gharpeshiksha.tutorapp.activities.HelpAndSupport;
import com.gharpeshiksha.tutorapp.activities.MyAsynct;
import com.gharpeshiksha.tutorapp.activities.ParentsContacted;
import com.gharpeshiksha.tutorapp.activities.ReedemPoints;
import com.gharpeshiksha.tutorapp.activities.ReferAndEarn_Activity;
import com.gharpeshiksha.tutorapp.activities.SignUpAndIn;
import com.gharpeshiksha.tutorapp.activities.TeacherDetailsActivity;
import com.gharpeshiksha.tutorapp.activities.Testimonials;
import com.gharpeshiksha.tutorapp.activities.UsefulContent;
import com.gharpeshiksha.tutorapp.activities.UserFeedback;
import com.gharpeshiksha.tutorapp.fragments.AppliedLeadsFragmentNew;
import com.gharpeshiksha.tutorapp.fragments.ChatsFragment;
import com.gharpeshiksha.tutorapp.fragments.ClassesForMeFragment;
import com.gharpeshiksha.tutorapp.fragments.HomeFragment;
import com.gharpeshiksha.tutorapp.fragments.Search;
import com.gharpeshiksha.tutorapp.localdb.Contract;
import com.gharpeshiksha.tutorapp.localdb.LocalSQLiteDbHandler;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.gharpeshiksha.tutorapp.utils.BadgeCount;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected String Server_url;
    public String searchEnqId = "";
    protected Boolean doubleTapBack = false;
    public boolean mIsAllIndiaChecked = false, mIsRadiusChecked = false, mIsSearchByEnqId = false, isFavoriteOpened;
    private int filterBadgeCount = 0;
    protected String name;
    public SessionConfig sessionObj;
    private final String TAG = "Dashboard.java";
    private long phone;
    private String Token;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    PrefConfig prefConfig;
    ImageView password_loogle;
    MenuItem uploadDocument;
    TextView hTutorId, upgradeBarText, filter, remViews;

    static CircleImageView circleImageView;
    TextView HName;
    private NavigationView navigationView;
    private String navHeaderUrl;
    ViewPager viewPager;
    private Dialog dialog;
    private Button retry;
    private LocalSQLiteDbHandler dbHandler;
    private int flag = 0, length_words = 0;
    public RelativeLayout relativeLayout;
    private SharedPreferences sharedPreferences;
    static TextView enquire;
    static TextView classcount;
    static TextView appliedleadcount;
    static TextView favcount;
    static TextView badgecount_Classes;
    static TextView badgecount_Applied;
    static TextView badgeCountChats;
    static TextView badgecount_Community;
    static TextView badgecount_favourite;
    public static TextView badgecount_Free;
    private int countappliedlead;
    private String paid = "no";

    private String version = "";
    private BottomNavigationView bottomNavigationView;
    private String intialstate_my_classes = "";
    private RelativeLayout chatBotFloat;
    private RelativeLayout filterClass;
    private BasicFeatures basicFeatures;
    static TextView filterBadge;
    private RelativeLayout bottomNavLayout;
    private AppBarLayout appBarLayout;
    static String comeFrom = "Dashboard";
    static String filterLat = "";
    static String filterLong = "";
    static String filterRadius = "";
    static String filterClasses = "";
    // static String HomeScreen = "";
    private String statusUrl = "https://api.gharpeshiksha.com" + "/PaymentStatus/statusnew";

    private VolleyHelper volleyHelper;
    private BadgeCount badgeCount;
    private LinearLayout layoutfiltersvjhsjad;
    private TextView sortingtextview;
    private RelativeLayout onclickSort;
    public static TextView sortBadge;
    private boolean activityRunning = true;
    ImageView Start_icon;
    MyAsynct pl;

    // private Fragment HomeFragment;

//    private FirebaseMessaging fcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Start_icon = findViewById(R.id.start_icon);
        volleyHelper = new VolleyHelper();
        basicFeatures = new BasicFeatures();
        sessionObj = new SessionConfig(getApplicationContext());
        sharedPreferences = getApplicationContext().getSharedPreferences("com.gharpeshiksha.tutorapp", MODE_PRIVATE);
        prefConfig = new PrefConfig(getApplicationContext());
        dbHandler = new LocalSQLiteDbHandler(Dashboard.this);
        filterBadge = findViewById(R.id.filterBadge);
       /* pl = new MyAsynct(this);
        pl.execute();*/

//        fcm = FirebaseMessaging.getInstance();
//        fcm.getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if (!task.isSuccessful()) {
//                    Log.w("tag", "Fetching FCM registration token failed", task.getException());
//                    return;
//                }
//
//                // Get new FCM registration token
//                String token = task.getResult();
//
//                // Log and toast
////                        String msg = getString(R.string.msg_token_fmt, token);
//                Log.d("tag.java", token);
//                Toast.makeText(Dashboard.this, token, Toast.LENGTH_SHORT).show();
//            }
//        });

        bottomNavigationView = findViewById(R.id.bottomNav);
        chatBotFloat = findViewById(R.id.chatFloat);
        filterClass = findViewById(R.id.filterfloat);
        layoutfiltersvjhsjad = findViewById(R.id.layoutfiltersvjhsjad);
        sortingtextview = findViewById(R.id.sortingtextview);
        onclickSort = findViewById(R.id.onclickSort);
        sortBadge = findViewById(R.id.sortBadge);

        relativeLayout = findViewById(R.id.linearLay);
        appBarLayout = findViewById(R.id.appbar1);
        viewPager = findViewById(R.id.container);

        viewPager.setOffscreenPageLimit(4);

        if (badgeCount == null) {
            badgeCount = new BadgeCount(Dashboard.this);
        }

        getAllIntentValues();
        setLayoutofViewPager();

        onclickSort.setOnClickListener(view -> sortList());

        chatBotFloat.setOnClickListener(v -> startActivity(new Intent(Dashboard.this, Chatbot.class)));

        filterClass.setOnClickListener(v -> startActivity(new Intent(Dashboard.this, EnquiryFilter.class)));


        phone = sessionObj.getPhone();
        Token = sessionObj.getToken();

        if (isNetworkAvailable()) {
            updateToken();
            modulesEnable();
            checkStatus();
        } else {
            noNetworkDialog();
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.Home:
                    Log.v(TAG, "adfasdf " + viewPager.getCurrentItem());
                    viewPager.setCurrentItem(0); //home fragment
                    layoutfiltersvjhsjad.setVisibility(View.GONE);
                    break;
                case R.id.appliedClass_BN: //applied fragment

                    viewPager.setCurrentItem(1);
                    break;

                case R.id.classes_BN:

                    viewPager.setCurrentItem(2); // Classeforme fragment
                    break;
              /*  case R.id.freeZone_BN:
                    viewPager.setCurrentItem(3); //search fragment
                    break;*/
                case R.id.Chats_BN:

                    viewPager.setCurrentItem(3); // Chat fragment
                    break;
                default:
                    Toast.makeText(this, "this is chats", Toast.LENGTH_SHORT).show();

            }


            return false;
        });


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

                Log.d("OnPageScroll", " hhjoiji  i" + i + " i1" + i1);
                bottomNavLayout.animate().translationY(0).setDuration(200);
                /*bottomNavLayout.setVisibility(View.GONE);*/
                //floatingButtonLayout.animate().translationY(0).setDuration(200);
                appBarLayout.setExpanded(true, true);
            }

            @Override
            public void onPageSelected(int i) {

                Log.d("onPageScrolled", "RUN : " + i);
                if (i == 2) {
                    layoutfiltersvjhsjad.setVisibility(View.VISIBLE);
                    filterClass.setVisibility(View.VISIBLE);
                    if (getIntent().hasExtra("filterCount")) {
                        setFilterBadge(getIntent().getIntExtra("filterCount", 0));
                    } else {
                        Log.d(TAG, "onPageScrolled: fyjfhyffjgfj     seledhbjkb");
                        FilterCount();
                    }
                } else if (i == 0 || i == 1 || i == 3 || i == 4) {
                    Log.d("HideFilterCount", "Hide true");
                    layoutfiltersvjhsjad.setVisibility(View.GONE);
                    filterClass.setVisibility(View.GONE);
                    filterBadge.setVisibility(View.GONE);
                    Log.d("HideFilterCount", "Hide ho gaya");
                }
                Log.v(TAG, "bottom selected: " + i);
                bottomNavigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.d("OnPageScroll", "onPageScrollStateChanged hhjoiji  i" + i);
            }
        });
        bottomNavLayout = findViewById(R.id.bottomNavLayout);
        /*//floatingButtonLayout = findViewById(R.id.//floatingButtonLayout);*/

        viewPager(viewPager);

        final BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0); // GETS COMPLETE MENU

        View v1 = bottomNavigationMenuView.getChildAt(2); // ITEM POSITION CLASSES
        View v = bottomNavigationMenuView.getChildAt(0); // ITEM POSITION HOME
        // View v3 = bottomNavigationMenuView.getChildAt(3); // ITEM POSITION SEARCH
        View v4 = bottomNavigationMenuView.getChildAt(1); // ITEM POSITION APPLIED
        View v5 = bottomNavigationMenuView.getChildAt(3); // ITEM POSITION CHATE

        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        //BottomNavigationItemView itemView1search = (BottomNavigationItemView) v3;
        BottomNavigationItemView itemView3 = (BottomNavigationItemView) v1;
        BottomNavigationItemView itemView4 = (BottomNavigationItemView) v4;
        BottomNavigationItemView itemView5 = (BottomNavigationItemView) v5;

        View badge = LayoutInflater.from(Dashboard.this).inflate(R.layout.classes_badge, itemView3, true);// INFLATE THE LAYOUT
        Log.d(TAG, "onCreate: Badge = " + badge);
      /*  View badge1search = LayoutInflater.from(Dashboard.this).inflate(R.layout.classes_badge,  itemView1search, true); // INFLATE THE LAYOUT
        Log.d(TAG, "onCreate: Badge1 = "+badge1search);*/
        View badge3 = LayoutInflater.from(Dashboard.this).inflate(R.layout.classes_badge, itemView4, true); // INFLATE THE LAYOUT
        Log.d(TAG, "onCreate: Badge3 = " + badge3);
        View badge4 = LayoutInflater.from(Dashboard.this).inflate(R.layout.classes_badge, itemView, true); // INFLATE THE LAYOUT
        Log.d(TAG, "onCreate: Badge4 = " + badge4);
        View badge5 = LayoutInflater.from(Dashboard.this).inflate(R.layout.classes_badge, itemView5, true);
        Log.d(TAG, "onCreate: Badge5 = " + badge5);


        badgecount_Classes = badge.findViewById(R.id.notificationsBadge);
        badgecount_Applied = badge3.findViewById(R.id.notificationsBadge);
        badgecount_favourite = badge4.findViewById(R.id.notificationsBadge);
        // badgecount_Free = badge4.findViewById(R.id.notificationsBadge);
        badgeCountChats = badge5.findViewById(R.id.notificationsBadge);

        Log.d("hcbcdjdhcdhc", sessionObj.getupdateAvailable() + "");
        if (sessionObj.getupdateAvailable()) {
            new AlertDialog.Builder(Dashboard.this, R.style.AppCompatAlertDialogStyle)
                    .setTitle("Update Available")
                    .setMessage("It looks like you are missing out some new features, kindly Update app to get a better experience")
                    .setPositiveButton("Update now", (dialog, which) -> {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    })
                    .setNegativeButton("Later", null)
                    .show();
        }

        if (sessionObj.getfirstTimeUser()) {

            new GuideView.Builder(Dashboard.this)
                    .setTitle("Home")
                    .setContentText("Explore our classes and subjects and get to know us more.")
                    .setGravity(Gravity.auto) //optional
                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                    .setTargetView(bottomNavigationMenuView.getChildAt(0))
                    .setGuideListener(view1 -> new GuideView.Builder(Dashboard.this)
                            .setTitle("Applied Leads")
                            .setContentText("here you can find the classes you have already contacted and give feedbacks for the same.")
                            .setGravity(Gravity.auto) //optional
                            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                            .setTargetView(bottomNavigationMenuView.getChildAt(1))
                            .setGuideListener(view112 -> new GuideView.Builder(Dashboard.this)
                                    .setTitle("Classes") //Community
                                    .setContentText("Get the classes according to your preference as well as all the latest classes that are posted.") //Here you can answer the questions asked by students and earn points.
                                    .setGravity(Gravity.auto) //optional
                                    .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                    .setTargetView(bottomNavigationMenuView.getChildAt(2))
                                    .setGuideListener(view11 -> new GuideView.Builder(Dashboard.this)
                                            .setTitle("Chats")
                                            .setContentText("Chat with all the Tutors near by you.")
                                            .setGravity(Gravity.auto) //optional
                                            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                            .setTargetView(bottomNavigationMenuView.getChildAt(3))
                                            .setGuideListener(view111 -> {
                                                /*new GuideView.Builder(Dashboard.this)
                                                        .setTitle("")
                                                        .setContentText("")
                                                        .setGravity(Gravity.auto) //optional
                                                        .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                                        .setTargetView(bottomNavigationMenuView.getChildAt(4))
                                                        .setGuideListener(new GuideListener() {
                                                            @Override
                                                            public void onDismiss(View view1) {
                                                                sessionObj.setfirstTimeUser(false);
                                                            }
                                                        })
                                                        .setContentTextSize(12)//optional
                                                        .setTitleTextSize(14)//optional
                                                        .build()
                                                        .show();*/
                                            })
                                            .setContentTextSize(12)//optional
                                            .setTitleTextSize(14)//optional
                                            .build()
                                            .show())
                                    .setContentTextSize(12)//optional
                                    .setTitleTextSize(14)//optional
                                    .build()
                                    .show())
                            .setContentTextSize(12)//optional
                            .setTitleTextSize(14)//optional
                            .build()
                            .show())
                    .setContentTextSize(12)//optional
                    .setTitleTextSize(14)//optional
                    .build()
                    .show();
            sessionObj.setfirstTimeUser(false);
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            if (!notificationManager.isNotificationPolicyAccessGranted()) {
//                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//                startActivity(intent);
//            }
//        } else {
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            if (!notificationManager.isNotificationPolicyAccessGranted()) {
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
//                startActivityForResult(intent, 101);
//                startActivity(intent);
//            }
//        }

        if (ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            //permission already granted
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                //permission not granted
                ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
            } else {
//                ActivityCompat.requestPermissions(Dashboard.this, new String[] {Manifest.permission.CAMERA}, 0);
            }
        }

        if (getIntent().hasExtra("filterCount")) {
            viewPager.setCurrentItem(2);
        }

        Log.d(TAG, "onCreate: " + filterClasses);
    }

    public void setBadgeChat(int value) {
        if(value == 0) {
            badgeCountChats.setVisibility(View.GONE);
        } else {
            badgeCountChats.setVisibility(View.VISIBLE);
            badgeCountChats.setText(value + "");
        }
    }
    public boolean isAllIndiaChecked() {
        return mIsAllIndiaChecked;
    }

    public void setIsAllIndiaChecked(boolean isChecked) {
        ClassesForMeFragment clsForMe = (ClassesForMeFragment) bottomNavAdapter.getItem(2);
        clsForMe.setAllIndiaChecked(false);
        mIsAllIndiaChecked = isChecked;
    }

    public void setMIsSearchByEnqId(boolean isChecked) {
        ClassesForMeFragment clsForMe = (ClassesForMeFragment) bottomNavAdapter.getItem(2);
        clsForMe.setSearchByEnqId(false);
        mIsAllIndiaChecked = isChecked;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(this, "not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setLayoutofViewPager() {

        if (intialstate_my_classes.matches("firsttime")) {

            if (prefConfig != null)
                prefConfig.writeSort("distance");
            sortBadge.setText("D");

        }
    }

    private void getAllIntentValues() {
        Intent intent = getIntent();
        if (intent.hasExtra("filterBy")) {
            filterClasses = intent.getStringExtra("selectClaesss");
            filterRadius = intent.getStringExtra("radius");
            filterLat = intent.getStringExtra("lat");
            filterLong = intent.getStringExtra("long");
            String filterBy = intent.getStringExtra("filterBy");
            if (filterBy.matches("allIndia")) {
                mIsAllIndiaChecked = true;
            } else if (filterBy.matches("searchByEnquiryId")) {
                searchEnqId = intent.getStringExtra("enqId");
                mIsSearchByEnqId = true;
                Log.d(TAG, "getAllIntentValues: " + searchEnqId);
            } else {
                mIsRadiusChecked = true;
            }
//            Log.d(TAG, "getAllIntentValues: " + intent.getStringExtra("filterBy"));
        } else {
            if (intent.hasExtra("EnquiryFilter")) {
                comeFrom = "EnquiryFilter";
                filterClasses = intent.getStringExtra("selectClaesss");
                filterRadius = intent.getStringExtra("radius");
                filterLat = intent.getStringExtra("lat");
                filterLong = intent.getStringExtra("long");
            }

            if (intent.hasExtra("filterCount")) {
                Log.d(TAG, "getAllIntentValues: " + intent.getStringExtra("filterCount"));
                setFilterBadge(intent.getIntExtra("filterCount", 0));
            } else {
                FilterCount();
            }

            if (intent.hasExtra("uploaddoc")) {
                paid = intent.getStringExtra("uploaddoc");
            }

            if (intent.hasExtra("sortDis")) {
                intialstate_my_classes = intent.getStringExtra("sortDis");
            }
        }
    }


    public void setBadgeClases(int value) {
        if (value == 0) {
            badgecount_Classes.setVisibility(View.GONE);
            sortingtextview.setTextColor(getResources().getColor(R.color.grey));
            sortingtextview.setEnabled(false);
        } else {
            badgecount_Classes.setVisibility(View.VISIBLE);
            badgecount_Classes.setText(String.valueOf(value));
        }
    }

    public String getfromCome() {
        return comeFrom;
    }

    public String getBadgeCount() {
        return filterClasses;
    }

    public String getRadius() {
        return filterRadius;
    }

    public String getLat() {
        return filterLat;
    }

    public String getLong() {
        return filterLong;
    }

    public void setBadgeApplied(int value) {
        if (value == 0) {
            badgecount_Applied.setVisibility(View.GONE);
        } else {
            badgecount_Applied.setVisibility(View.VISIBLE);
            badgecount_Applied.setText(String.valueOf(value));
        }
    }

    public void setFilterBadge(int value) {

        if (filterBadge != null) {
            if (value == 0) {
                filterBadge.setVisibility(View.GONE);
            } else {
                filterBadge.setText(String.valueOf(value));
                filterBadge.setVisibility(View.VISIBLE);
                Log.d(TAG, "setFilterBadge: run " + value);
            }
        }

    }


    public void setBadgeFavourite(int value) {
//        if (value == 0) {
//            badgecount_favourite.setVisibility(View.GONE);
//        } else {
//            badgecount_favourite.setVisibility(View.VISIBLE);
//            badgecount_favourite.setText(String.valueOf(value));
//        }
    }


    private void modulesEnable() {
        Cursor cursor = dbHandler.getBasicInfo(dbHandler.getReadableDatabase());
        Log.d(TAG, "checkstatus: size: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            long currTime = System.currentTimeMillis() / 1000;
            long futureSoftTL = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(Contract.BasicInfo.SOFT_TL))) / 1000;
            futureSoftTL += 600;
            Log.d(TAG, "checkstatus: " + futureSoftTL + ", " + currTime);
            if (futureSoftTL > currTime) {
                //valid
                try {
                    paid = cursor.getString(cursor.getColumnIndexOrThrow(Contract.BasicInfo.STATUS));
                    toolbar = findViewById(R.id.toolbar);

                    drawerLayout = findViewById(R.id.drawer_layout);
                    toolbar.setTitle(R.string.app_name);
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
                    setSupportActionBar(toolbar);

                    ActionBar actionBar = getSupportActionBar();

                    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View actionBarView = null;
                    if (inflater != null) {
                        actionBarView = inflater.inflate(R.layout.upgrade_bar, toolbar, false);
                    }

                    if (actionBar != null) {
                        actionBar.setTitle("GharPeShiksha");
                        actionBar.setCustomView(actionBarView);
                        actionBar.setDisplayShowCustomEnabled(true);
                    }

                    upgradeBarText = findViewById(R.id.upgradeBarText);
                    upgradeBarText.setOnClickListener(v -> {
                        Intent intent = new Intent(getApplicationContext(), UpgradeActivity.class);
                        startActivity(intent);
                    });

                    Start_icon = findViewById(R.id.start_icon);
                    Start_icon.setOnClickListener(view -> {
                        isFavoriteOpened = true;
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().addToBackStack("fragSearch").replace(R.id.fragment_container, new Search()).commit();
                    });


                    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open_drawer,
                            R.string.nav_close_drawer);
                    drawerLayout.addDrawerListener(toggle);
                    toggle.syncState();

                    navigationView.setItemIconTintList(null);
                    navigationView.setNavigationItemSelectedListener(this);

                    try {
                        version = "\t\t\tVersion " + getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    Menu menuNav = navigationView.getMenu();
                    MenuItem versionName = menuNav.findItem(R.id.nav_version);
                    uploadDocument = menuNav.findItem(R.id.nav_uploadDocuments);
                    versionName.setTitle(version);
                    if (paid != null) {
                        if (paid.matches("false")) {
                            uploadDocument.setVisible(true);
                        } else if (paid.matches("true") || paid.matches("Non Paid Member")) {
                            uploadDocument.setVisible(false);
                        }
                    }
                    setNavigationHeader();
                    Log.d(TAG, "onResponse: on slideactivity check status " + paid);
                    return;
                } catch (Exception e) {
                    Log.d(TAG, "onResponse Error: " + e.getMessage());
                }
            }
        }
        Log.d(TAG, "checkstatus: not valid");
//        Log.v("Dashboard.java", paid + "");
        navHeaderUrl = "https://api.gharpeshiksha.com" + "/Tutor/basicinfo";
        //get status of user using Volley Http library
        RequestQueue que = Volley.newRequestQueue(Dashboard.this);
        StringRequest sR = new StringRequest(Request.Method.POST, navHeaderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    paid = jsonObject.getString("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", phone + "");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        que.add(sR);
        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = null;
        if (inflater != null) {
            actionBarView = inflater.inflate(R.layout.upgrade_bar, toolbar, false);
        }

        if (actionBar != null) {
            actionBar.setTitle("GharPeShiksha");
            actionBar.setCustomView(actionBarView);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        upgradeBarText = findViewById(R.id.upgradeBarText);
        upgradeBarText.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UpgradeActivity.class);
            startActivity(intent);
        });

        Start_icon = findViewById(R.id.start_icon);
        Start_icon.setOnClickListener(view -> {
            isFavoriteOpened = true;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack("fragSearch").replace(R.id.fragment_container, new Search()).commit();
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setNavigationHeader();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);


        try {
            version = "\t\t\tVersion " + getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Menu menuNav = navigationView.getMenu();
        MenuItem versionName = menuNav.findItem(R.id.nav_version);
        uploadDocument = menuNav.findItem(R.id.nav_uploadDocuments);
        versionName.setTitle(version);
//        Log.v("Dashboard.java", paid + "");
        if (paid != null) {
            if (paid.matches("false")) {
                uploadDocument.setVisible(true);
            } else if (paid.matches("true") || paid.matches("Non Paid Member")) {
                uploadDocument.setVisible(false);
            }
        }
    }


    public void favCountVolley() {
        badgeCount.favCountVolley(sessionObj.getPhone(), count -> setBadgeFavourite(count));
    }

    public void changePositionOfViewPager(int pos) {
        viewPager.setCurrentItem(pos);
        bottomNavigationView.setSelectedItemId(pos);
    }

    BottomNavViewPagerAdapter bottomNavAdapter = new BottomNavViewPagerAdapter(getSupportFragmentManager());

    private void viewPager(ViewPager viewPager) {
        bottomNavAdapter = new BottomNavViewPagerAdapter(getSupportFragmentManager());
        HomeFragment homeFragment = new HomeFragment();
        AppliedLeadsFragmentNew appliedLeadsFragment = new AppliedLeadsFragmentNew();
        ClassesForMeFragment classesForMeFragment = new ClassesForMeFragment(Dashboard.comeFrom, filterRadius, filterLat, filterLong, filterClasses, filterClasses);
        ChatsFragment chatsFragment1 = new ChatsFragment();
        //SearchViewFragment searchViewFragment = new SearchViewFragment();

        bottomNavAdapter.addFragment(homeFragment);// Home Fragment
        bottomNavAdapter.addFragment(appliedLeadsFragment);//Applied fragment
        bottomNavAdapter.addFragment(classesForMeFragment);// Classes fragment

        // bottomNavAdapter.addFragment(searchViewFragment); //old Fragment calling Favourite Fragment
        bottomNavAdapter.addFragment(chatsFragment1);// Chate framgment
        viewPager.setAdapter(bottomNavAdapter);
    }

    private void FilterCount() {
        if (!comeFrom.matches("filtered")) {
            final int[] count = {0};
            badgeCount.selectedClasses(sessionObj.getPhone(), selectedCourses -> {
                Log.d(TAG, "SelectedCourses: seledhbjkb " + selectedCourses.size());
                count[0] = selectedCourses.size();
                setFilterBadge(count[0]);
            });
        } else {
            setFilterBadge(filterBadgeCount);
        }
    }

    public void filterCout() {
        int[] countclase = {2};
        badgeCount.selectedClasses(sessionObj.getPhone(), selectedCourses -> {
            Log.d(TAG, "SelectedCourses: seledhbjkb " + selectedCourses.size());
            countclase[2] = selectedCourses.size();
            setFilterBadge(countclase[2]);

        });
    }

    protected void noNetworkDialog() {

        dialog = new Dialog(this);

        dialog.setContentView(R.layout.no_network_dialog);
        retry = dialog.findViewById(R.id.retry);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        retry.setOnClickListener(v -> {
            dialog.dismiss();
            if (isNetworkAvailable()) {
                dialog.dismiss();
                if (flag == 0) {
                    updateToken();
                    modulesEnable();
                }
            } else {
                dialog.dismiss();
                noNetworkDialog();
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void navHeaderResponse(String url, final TextView hTutorId, final CircleImageView imageView,
                                   final TextView upgradeBarText, final TextView remViews) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone + "");
        volleyHelper.VolleyPostRequest(Dashboard.this, url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    final JSONObject part = new JSONObject(response);
                    name = part.getString("name");
                    if (!part.getString("planDetail").equals("nonpaid")) {
                        String plandetail = part.getString("planDetail").toUpperCase();
                        if (plandetail.equals("FREE")) {
                            upgradeBarText.setText("TRIAL");
                        } else {
                            upgradeBarText.setText(plandetail);
                        }
                    }

                    if (part.has("remainingViews")) {
                        remViews.setVisibility(View.VISIBLE);
                        remViews.setText(part.getString("remainingViews"));
                    } else {
                        remViews.setVisibility(View.GONE);
                    }

                    if (!part.isNull("profile_pic_url")) {

                        String image = part.getString("profile_pic_url");
                        Log.e(TAG, "onResponse: image" + image);

                        if (!image.equals("No Image")) {
                            Glide.with(getApplicationContext()).setDefaultRequestOptions(RequestOptions.centerCropTransform()
                                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                                    .load(image).into(imageView);
                        }
                    }

                    if (part.has("about")) {
                        if (!part.getBoolean("about")) {
                            if (sessionObj.getShowAboutDialog()) {
                                if (activityRunning) {
                                    new Handler().postDelayed(() -> showAboutDialog(), 5000);
                                    sessionObj.setShowAboutDialog(false);
                                }
                            }

                        }
                    }

                    String tutorId = part.getString("tut_id");
                    HName.setText(name);
                    hTutorId.setText("Tutor Id: " + tutorId);
                    Log.d(TAG, "onSuccess: tut " + tutorId);
                } catch (Exception e) {
                    Log.d(TAG, "onResponse Error: " + e.getMessage());
                }

            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "onError asdfsadf: " + error);
                noNetworkDialog();
            }
        });

    }


    private void showAboutDialog() {
        try {
            LayoutInflater inflater = LayoutInflater.from(Dashboard.this);
            View view = inflater.inflate(R.layout.enter_about_dialog, null);
            final AlertDialog.Builder alert = new AlertDialog.Builder(Dashboard.this, R.style.AppCompatAlertDialogStyle);
            alert.setView(view);
            final AlertDialog dialog = alert.show();
            final TextInputLayout inputLayout = view.findViewById(R.id.textinputLayout);
            final EditText edit = view.findViewById(R.id.edittextbio);
            final Button confirm = view.findViewById(R.id.confirmB);
            final TextView skip = view.findViewById(R.id.skipB);
            final Spinner spinner = view.findViewById(R.id.spinnerEnglish);

            if (edit.isEnabled()) {
                inputLayout.setHint("");
            }


            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    length_words = charSequence.toString().trim().split(" ").length;

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            confirm.setOnClickListener(view1 -> {
                if (length_words <= 30) {
                    Toast.makeText(Dashboard.this, "Enter atleast 30 words.", Toast.LENGTH_SHORT).show();
                    inputLayout.setError("");
                    inputLayout.setErrorEnabled(true);
                } else if (length_words >= 200) {
                    Toast.makeText(Dashboard.this, "Enter less than 200 words.", Toast.LENGTH_SHORT).show();
                    inputLayout.setError("");
                    inputLayout.setErrorEnabled(true);
                } else if (spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(Dashboard.this, "Select your english speaking skill.", Toast.LENGTH_LONG).show();
                } else {
                    sendAboutVolley(edit.getText().toString(), spinner.getSelectedItem().toString());
                    dialog.dismiss();
                }
            });


            skip.setOnClickListener(view12 -> dialog.dismiss());
        } catch (Exception e) {
            Log.e(TAG, "showAboutDialog: ", e);
        }
    }

    private void sendAboutVolley(String bio, String english_skill) {
        String aboutURL = "https://api.gharpeshiksha.com/Tutor/addAbout";
        Map<String, String> params = new HashMap<>();
        params.put("phone", String.valueOf(phone));
        params.put("about", bio);
        params.put("skill", english_skill);
        volleyHelper.VolleyPostRequest(Dashboard.this, aboutURL, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                if (response.equals("success")) {
                    Toast.makeText(Dashboard.this, "Successfully Updated.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    private void setNavigationHeader() {
        navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        HName = header.findViewById(R.id.hName);
        HName.setText(name);

        circleImageView = header.findViewById(R.id.navImage);
        hTutorId = header.findViewById(R.id.hTutorId);
        upgradeBarText = header.findViewById(R.id.upgradeBarText);
        remViews = header.findViewById(R.id.remViews);
        password_loogle = header.findViewById(R.id.toggle_Eye_Icon_click);

        navHeaderResponse(navHeaderUrl, hTutorId, circleImageView, upgradeBarText, remViews);
        circleImageView.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), TeacherDetailsActivity.class)));
        upgradeBarText.setOnClickListener(v -> intentAct(UpgradeActivity.class));

        password_loogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Dashboard.this, "this is details", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), TeacherDetailsActivity.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        if (isNetworkAvailable()) {
            switch (item.getItemId()) {
                case R.id.nav_profile:
//                    intentAct(EditProfileActivity.class);
                    Intent i = new Intent(getApplicationContext(), TeacherDetailsActivity.class);
                    //i.putExtra("id", id);
                    startActivity(i);
                    break;
                case R.id.nav_changeSubjects:
                    intentAct(ClassesActivity.class);
                    break;
                case R.id.nav_changeLocation:
                    Intent intent = new Intent(getApplicationContext(), AreaRangeSelection.class);
                    intent.putExtra("Dashboard", true);
                    startActivity(intent);
                    break;
                case R.id.nav_classesFeedback:
                    intentAct(AllFeedbackActivity.class);
                    break;
                case R.id.nav_billingHistory:
                    intentAct(BillingHistoryActivity.class);
                    break;
                case R.id.nav_referEarn:
                    intentAct(ReferAndEarn_Activity.class);
                    break;
                case R.id.nav_wallet:
                    intentAct(ReedemPoints.class);
                    break;
                case R.id.nav_testimonial:
                    intentAct(Testimonials.class);
                    break;
                case R.id.nav_feedbackSuggestions:
                    intentAct(UserFeedback.class);
                    break;
                case R.id.nav_usefulContent:
                    intentAct(UsefulContent.class);
                    break;
                case R.id.nav_settings:
                    intentAct(NotificationActivity.class);
                    break;
                case R.id.nav_helpSupport:
                    intentAct(HelpAndSupport.class);
                    break;
                case R.id.nav_uploadDocuments:
                    intentAct(UploadDocuments.class);
                    break;
                case R.id.nav_contactUs:
                    intentAct(Chatbot.class);
                    break;
                /*case R.id.nav_TeacherDetails:
                    intentAct(TeacherDetailsActivity.class);
                    break;
                case R.id.nav_Upgrade:
                    intentAct(UpgradeActivity.class);
                    break;*/

                case R.id.nav_about:
                    intentAct(About.class);
                    break;
                case R.id.parentsContacted:
                    checkStatus2();
//                    Intent myIntent = new Intent(getApplicationContext(), Upi.class);
//                    //ADD the data into bundle and send
//
//                    Bundle bundle = new Bundle();  //create a bundle and send it to activity called upi class.
//                    bundle.putString("stuffs", Double.toString(100.00));
//                    myIntent.putExtras(bundle);
//                    startActivity(myIntent);
                    break;
                case R.id.nav_logout:
                    wantToLogout();
                    break;

            }
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            noNetworkDialog();
        }
        return false;
    }

    private void gotoStart() {


        Intent intent = new Intent(Dashboard.this, SignUpAndIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        try {
            if (isFavoriteOpened) {
                isFavoriteOpened = false;
                super.onBackPressed();
                return;
            }
            if (drawerLayout != null) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {

                    if (doubleTapBack) {
                        super.onBackPressed();
                    } else {
                        changeFrag(0, "NA", false, null);
                        Toast.makeText(Dashboard.this, "Press again to exit", Toast.LENGTH_SHORT).show();
                        this.doubleTapBack = true;
                        new Handler().postDelayed(() -> doubleTapBack = false, 2000);
                    }

                }
            } else {
                if (doubleTapBack) {
                    super.onBackPressed();
                } else {
                    changeFrag(0, "NA", false, null);
                    Toast.makeText(Dashboard.this, "Press again to exit", Toast.LENGTH_SHORT).show();
                    this.doubleTapBack = true;
                    new Handler().postDelayed(() -> doubleTapBack = false, 2000);
                }

            }
        } catch (Exception e) {
            Log.v("Dashboard.java", e + "");
        }
    }

    public void sortList() {

        if (isNetworkAvailable()) {
            try {
                final Dialog dialog = new Dialog(Dashboard.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setCancelable(true);

                TextView recency, distance;
                final ImageView checkRec, checkDis;

                recency = dialog.findViewById(R.id.sortRecency);
                distance = dialog.findViewById(R.id.sortDistance);
                checkRec = dialog.findViewById(R.id.checkRec);
                checkDis = dialog.findViewById(R.id.checkDis);


                dialog.show();
                recency.setOnClickListener(v -> {

                    prefConfig.writeSort("recency");
                    sortBadge.setText("R");
                    intialstate_my_classes = "bhjadgjdaf";

                    if (viewPager.getCurrentItem() == 2) {
                        ClassesForMeFragment classesForMeFragment = new ClassesForMeFragment();
                        Log.v("Dashboard.java", "recency");
                        classesForMeFragment.recency(Dashboard.this, 0);
                    }
                    dialog.dismiss();
                });

                distance.setOnClickListener(v -> {

                    prefConfig.writeSort("distance");
                    sortBadge.setText("D");
                    intialstate_my_classes = "bhjadgjdaf";
                    if (viewPager.getCurrentItem() == 2) {
                        ClassesForMeFragment classesForMeFragment = new ClassesForMeFragment();
                        Log.v("Dashboard.java", "distance");
                        classesForMeFragment.distance(Dashboard.this, 0);
                    }
                    dialog.dismiss();
                });


                if (prefConfig.readSort().equals("recency")) {
                    checkRec.setVisibility(View.VISIBLE);
                    checkDis.setVisibility(View.GONE);
                }
                if (prefConfig.readSort().equals("distance")) {
                    checkRec.setVisibility(View.GONE);
                    checkDis.setVisibility(View.VISIBLE);
                }

                Log.d(TAG, "sortList: " + prefConfig.readSort());
            } catch (Exception e) {
                Log.d(TAG, "sortList: " + e.getMessage());
            }
        } else {
            noNetworkDialog();
        }

    }

    private void wantToLogout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Dashboard.this, R.style.AppCompatAlertDialogStyle);
        alertDialog.setTitle("Log Out");
        alertDialog.setMessage("Are you sure you want to log out");
        alertDialog.setIcon(R.drawable.ic_out);

        alertDialog.setPositiveButton("Yes", (dialog, which) -> {

            removetoken();
            prefConfig.writeLoginStatus(false);
            sessionObj.LoginStatusWrite(false);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("VerifiedContact");
            editor.clear();
            editor.apply();
            //Delete All table data from SQLite.
//            db.execSQL(createClassesForMeTable);
//            db.execSQL(createClassesTable);
//            db.execSQL(createArchivedTable);
//            db.execSQL(createFavTable);
//            db.execSQL(createWrittenTable);
//            db.execSQL(createVideoTestimonialTable);
//            db.execSQL(createBasicInfo);
//            db.execSQL(createDisplayChatsTable);
            LocalSQLiteDbHandler dbHandler = new LocalSQLiteDbHandler(Dashboard.this);
            dbHandler.deleteClassesForMeResponse();
            dbHandler.deleteClasses(dbHandler.getWritableDatabase());
            dbHandler.deleteArchived(dbHandler.getWritableDatabase());
            dbHandler.deleteFav(dbHandler.getWritableDatabase());
            dbHandler.deleteWritten(dbHandler.getWritableDatabase());
            dbHandler.deleteTestimonials(dbHandler.getWritableDatabase());
            dbHandler.deleteBasicInfo(dbHandler.getWritableDatabase());
            dbHandler.deleteDisplayChats(dbHandler.getWritableDatabase());
            finish();
            Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_SHORT).show();
            gotoStart();
        });

        alertDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        //alertDialog.setNegativeButton(BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlack));
        alertDialog.show();
    }

    private void removetoken() {

        String alurl = "https://api.gharpeshiksha.com" + "/Tutor/signout";
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone + "");
        params.put("firebasetoken", Token);


        volleyHelper.VolleyPostRequest(Dashboard.this, alurl, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                Log.d(TAG, "onSucess: REMOVE TOKEN " + response);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "onError: REMOVE TOKEN " + error);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (flag == 1) {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }


        if (isNetworkAvailable()) {
            checkStatus();
            if (!getIntent().hasExtra("filterCount")) {
                FilterCount();
            }

        } else {
            noNetworkDialog();
        }


    }

    private void intentAct(Class mClass) {
        Intent intent = new Intent(getApplicationContext(), mClass);
        startActivity(intent);
    }

    private void intentAct2(Class mClass) {
        Intent intent = new Intent(getApplicationContext(), mClass);
        intent.putExtra("phone", String.valueOf(phone));
        startActivity(intent);
    }

    private void updateToken() {

        Server_url = "https://api.gharpeshiksha.com" + "/Tutor/setToken";
        Map<String, String> params = new HashMap<>();
        params.put("phone", "" + phone);
        params.put("firebaseToken", Token);

        volleyHelper.VolleyPostRequest(Dashboard.this, Server_url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                flag = 1;
                Log.e(TAG, "CreateResponse: onResponse" + response);

                try {
                    JSONObject jsResponse = new JSONObject(response);
                    Log.e(TAG, "sResponse.get()" + jsResponse.get("Status"));
                    if (jsResponse.get("Status").equals("Success")) {

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }


    public void appliedcount() {

        badgeCount.appliedcount(sessionObj.getPhone(), count -> {
            countappliedlead = count;
            setBadgeApplied(count);
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        activityRunning = true;
        enquire = findViewById(R.id.countenqiries);
        classcount = findViewById(R.id.countclass);
        // favcount = findViewById(R.id.favouritescount);
        appliedcount();
        //appliedleadcount = findViewById(R.id.appliedleadcount);
        favCountVolley();

    }

    @Override
    protected void onStop() {
        super.onStop();
        activityRunning = false;
    }

    private void checkStatus2() {
        basicFeatures.CheckPaymentStatus(Dashboard.this, sessionObj.getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {
                if (status.matches("active")) {
                    intentAct2(ParentsContacted.class);
                } else if (status.matches("expired")) {
                    Intent i = new Intent(Dashboard.this, UpgradeActivity.class);
                    startActivity(i);
                } else {
                    basicFeatures.showFreePaidDialog(Dashboard.this);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void checkStatus() {
        basicFeatures.CheckPaymentStatus(Dashboard.this, sessionObj.getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {
                if (status.matches("expired")) {
                    try {
                        basicFeatures.showPlanExpiredSnackbar(viewPager, Dashboard.this);
                    } catch (Exception e) {
                        Log.d(TAG, "Result: " + e);
                    }
                }
            }

            @Override
            public void onError() {

            }
        });

    }

    public void changeFrag(int pos, String filterClass, boolean isClassSub, String strLatLongRad) {
        if (isClassSub && strLatLongRad != null) {
            comeFrom = "filtered";
            Log.d(TAG, "changeFrag: " + filterClass + ", " + strLatLongRad);
            String[] str = strLatLongRad.split(",");
            viewPager.setCurrentItem(pos);
            ClassesForMeFragment obj = (ClassesForMeFragment) bottomNavAdapter.getItem(2);
            filterClasses = filterClass;
            obj.myQuiries("EnquiryFilter", filterClasses, str[0], str[1], str[2]);
            String[] clsArr = filterClass.split(",");
            setFilterBadge(clsArr.length);
            filterBadgeCount = clsArr.length;
        } else {
            viewPager.setCurrentItem(pos);
            Log.d(TAG, "changeFrag: " + filterClass);
        }


    }

}
