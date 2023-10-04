package com.gharpeshiksha.tutorapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.UpgradeActivity;
import com.gharpeshiksha.tutorapp.Adapter.AdapterClasses;
import com.gharpeshiksha.tutorapp.Adapter.AdapterSubject;
import com.gharpeshiksha.tutorapp.Adapter.AdapterViewPager;
import com.gharpeshiksha.tutorapp.Adapter.Adapter_testimonial;
import com.gharpeshiksha.tutorapp.Adapter.Adapter_Imonials;
import com.gharpeshiksha.tutorapp.data_model.Classes_Model;
import com.gharpeshiksha.tutorapp.data_model.Model_Imonials;
import com.gharpeshiksha.tutorapp.data_model.Written_Testimonial;
import com.gharpeshiksha.tutorapp.databinding.FragmentHomeBinding;
import com.gharpeshiksha.tutorapp.localdb.Contract;
import com.gharpeshiksha.tutorapp.localdb.LocalSQLiteDbHandler;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.data_model.Subject_Class_Model;
import com.google.android.gms.maps.model.Dash;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    List<Written_Testimonial> google_fbList = new ArrayList<>();
    private boolean isPressed = false;
    private long softTTL = 0;

    List<String> viewPagerModelList = new ArrayList<>();
    List<Subject_Class_Model> subject_class_modelList = new ArrayList<>();
    private Handler sliderHandler = new Handler();
    private boolean button1IsVisible = true;
    List<Classes_Model> classes_modelList;
    boolean temp = true;
    String TAG = "HomeFragment.java";
    FragmentHomeBinding binding;
    private Dashboard dashboard;
    private LocalSQLiteDbHandler dbHandler;
    List<Model_Imonials> model_imonialsList;

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        dbHandler = new LocalSQLiteDbHandler(getActivity());
        dashboard = (Dashboard) getActivity();
        classes_modelList = new ArrayList<>();
        model_imonialsList = new ArrayList<>();
        long currTime = System.currentTimeMillis() / 1000;
        // Classes_Visible_Methods();
        View_Pager();
        //google facebook reviews
        Cursor cursor = dbHandler.getWritten(dbHandler.getReadableDatabase());
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                google_fbList.add(new Written_Testimonial(
                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.WrittenTestimonial.PIC_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.WrittenTestimonial.DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.WrittenTestimonial.TUTOR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.WrittenTestimonial.TUTOR_NAME))
                ));
            } while(cursor.moveToNext());
            cursor.moveToPrevious();
            long futureSoftTL = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(Contract.WrittenTestimonial.SOFT_TTL))) / 1000;
            futureSoftTL += 86400;
            if(futureSoftTL > currTime) {
                Log.d(TAG, "onCreateView: valid");
                //valid
                Adapter_testimonial adapter_google_fb = new Adapter_testimonial(google_fbList, getContext());
                binding.RecycleViewGoogle.setAdapter(adapter_google_fb);
                binding.RecycleViewGoogle.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            } else {
                Log.d(TAG, "onCreateView: not valid");
                google_fbList.clear();
                Google_Gb_Methods();
            }
        } else {
            Log.d(TAG, "onCreateView: not data");
            google_fbList.clear();
            Google_Gb_Methods();
        }

        //Testimonial cache management
        Cursor cursor1 = dbHandler.getTestimonials(dbHandler.getReadableDatabase());
        if(cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            long futureSoftTTL = Long.parseLong(cursor1.getString(cursor1.getColumnIndexOrThrow(Contract.VideoTestimonial.SOFT_TTL))) / 1000;
            futureSoftTTL += 86400;
            if(futureSoftTTL > currTime) {
                do {
                    model_imonialsList.add(new Model_Imonials(
                            cursor1.getString(cursor1.getColumnIndexOrThrow(Contract.VideoTestimonial.YOUTUBE_URL)),
                            cursor1.getString(cursor1.getColumnIndexOrThrow(Contract.VideoTestimonial.DESC)),
                            cursor1.getString(cursor1.getColumnIndexOrThrow(Contract.VideoTestimonial.VIDEO_ID)),
                            cursor1.getString(cursor1.getColumnIndexOrThrow(Contract.VideoTestimonial.IMG_URL))
                    ));
                } while (cursor1.moveToNext());

                Adapter_Imonials adapter_imonials = new Adapter_Imonials(getContext(), model_imonialsList);
                binding.RecycleViewTestImonials.setAdapter(adapter_imonials);
                binding.RecycleViewTestImonials.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                Log.d(TAG, "onCreateView: video valid");
            } else {
                //videos reviews
                Testing_Imonials_Video_Methods();
                Log.d(TAG, "onCreateView: video not valid");
            }
        } else {
            //videos reviews
            Testing_Imonials_Video_Methods();
        }

        binding.Upgrade.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), UpgradeActivity.class));
        });
        binding.RelativeExa.setOnClickListener(view -> {

            startActivity(new Intent(getContext(), Dashboard.class));
        });

        binding.ClassesVit.setOnClickListener(view -> {
            binding.RecycleViewSubject.setVisibility(View.GONE);
            binding.RecycleViewClasses.setVisibility(View.VISIBLE);
          /*  binding.ClassesVit.setBackgroundResource(R.color.colorWhite);
            binding.Subject.setBackgroundColor(R.color.white);*/

        });
        binding.Subject.setOnClickListener(view -> {
            binding.RecycleViewClasses.setVisibility(View.GONE);
            binding.RecycleViewSubject.setVisibility(View.VISIBLE);
          /*  binding.Subject.setBackgroundColor(R.color.colorWhite);
            binding.ClassesVit.setBackgroundColor(R.color.white);*/
        });


        binding.daskboardBtn.setOnClickListener(view -> {
            assert getActivity() != null;
            Dashboard dash = (Dashboard) getActivity();
            dash.changeFrag(2, "All", false, "");
        });

        //classes for grid recycler
//        dbHandler.onUpgrade(dbHandler.getWritableDatabase(), Contract.DB_VERSION, Contract.DB_VERSION + 1);
        if (getLocalData()) {
            long futureSoftTL = (softTTL / 1000) + 86400;
            if(futureSoftTL > currTime) {
                Log.d(TAG, "onCreateView: valid");
                AdapterClasses adapterClasses = new AdapterClasses(getContext(), classes_modelList);
                binding.RecycleViewClasses.setAdapter(adapterClasses);
                binding.RecycleViewClasses.setLayoutManager(new GridLayoutManager(getContext(), 3));
            } else {
//                dbHandler.deleteClasses(dbHandler.getWritableDatabase());
                Log.d(TAG, "onCreateView: not valid");
                Classes_Class_Methods();
            }
        } else {
//            dbHandler.deleteClasses(dbHandler.getWritableDatabase());
            Log.d(TAG, "onCreateView: api");
            Classes_Class_Methods();
        }

        binding.swipeRef.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorRed,
                R.color.colorGreen,
                R.color.grey);
        binding.swipeRef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                dbHandler.deleteTestimonials(dbHandler.getWritableDatabase());
//                dbHandler.deleteWritten(dbHandler.getWritableDatabase());
//                dbHandler.deleteClasses(dbHandler.getWritableDatabase());
                Google_Gb_Methods();
                Testing_Imonials_Video_Methods();
                Classes_Class_Methods();
                binding.swipeRef.setRefreshing(false);
            }
        });
        //classes for grid recycler
//        Subject_Class_Methode();
        return binding.getRoot();
    }

    private boolean getLocalData() {
        Cursor cursor = dbHandler.getClasses(dbHandler.getReadableDatabase());
        int size = cursor.getCount();
        if (size > 0) {
            cursor.moveToFirst();
            do {
                classes_modelList.add(new Classes_Model(
                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.ClassesTable.CLASS_PIC_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.ClassesTable.CLASSES_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Contract.ClassesTable.FILTER_CLS))
                ));
            } while (cursor.moveToNext());
            cursor.moveToPrevious();
            softTTL = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ClassesTable.SOFT_TTL)));
            return true;
        } else {
            return false;
        }
    }

    private void Testing_Imonials_Video_Methods() {
        binding.RecycleViewTestImonials.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.getVideoTestimonial().enqueue(new Callback<List<Model_Imonials>>() {
            @Override
            public void onResponse(Call<List<Model_Imonials>> call, Response<List<Model_Imonials>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: Video == " + response.body());
                    dbHandler.deleteTestimonials(dbHandler.getWritableDatabase());
                    List<Model_Imonials> list = response.body();
                    for(Model_Imonials m: list) {
                        model_imonialsList.add(m);
                        dbHandler.addTestimonials(dbHandler.getWritableDatabase(), m);
                    }
                    Adapter_Imonials adapter_imonials = new Adapter_Imonials(getContext(), model_imonialsList);
                    binding.RecycleViewTestImonials.setAdapter(adapter_imonials);
                }
            }

            @Override
            public void onFailure(Call<List<Model_Imonials>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    private void Google_Gb_Methods() {
        binding.RecycleViewGoogle.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.getTestimo().enqueue(new Callback<List<Written_Testimonial>>() {
            @Override
            public void onResponse(Call<List<Written_Testimonial>> call, Response<List<Written_Testimonial>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    dbHandler.deleteWritten(dbHandler.getWritableDatabase());
                    for(int i=0; i<response.body().size(); i++) {
                        dbHandler.addWritten(dbHandler.getWritableDatabase(), response.body().get(i));
                        google_fbList.add(response.body().get(i));
                    }

                    Adapter_testimonial adapter_google_fb = new Adapter_testimonial(google_fbList, getContext());
                    binding.RecycleViewGoogle.setAdapter(adapter_google_fb);
                }
            }

            @Override
            public void onFailure(Call<List<Written_Testimonial>> call, Throwable t) {

            }
        });

    }

    public void View_Pager() {

        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.getSlider().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                Log.d(TAG, "View Pager2 == " + response.body());
                viewPagerModelList = response.body();

                AdapterViewPager adapterViewPager = new AdapterViewPager(binding.viewPager2, viewPagerModelList);
                binding.viewPager2.setAdapter(adapterViewPager);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

                Log.d(TAG, "onFailure: View Pager 2 == " + t.getMessage());
            }
        });


        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setClipToPadding(false);

        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {

                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.14f);
            }
        });
        binding.viewPager2.setPageTransformer(transformer);
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2500);
            }
        });

    }


    private void Subject_Class_Methode() {

        binding.RecycleViewSubject.setLayoutManager(new GridLayoutManager(getContext(), 3));
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.StudentsSubject().enqueue(new Callback<List<Subject_Class_Model>>() {
            @Override
            public void onResponse(Call<List<Subject_Class_Model>> call, Response<List<Subject_Class_Model>> response) {
                Log.d(TAG, "onResponse: Subject === " + response.body().toString());
                subject_class_modelList = response.body();
                Log.d(TAG, "onResponse: Image " + subject_class_modelList.get(0).getImg());
                Toast.makeText(getContext(), "Images = " + subject_class_modelList.get(0).getImg(), Toast.LENGTH_SHORT).show();

                AdapterSubject adapter_notes = new AdapterSubject(getContext(), subject_class_modelList);
                binding.RecycleViewSubject.setAdapter(adapter_notes);
            }

            @Override
            public void onFailure(Call<List<Subject_Class_Model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void Classes_Class_Methods() {
        classes_modelList = new ArrayList<>();
        // binding.shimmerViewContainer.startShimmer();
        binding.RecycleViewClasses.setLayoutManager(new GridLayoutManager(getContext(), 3));
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.StudentsClass().enqueue(new Callback<List<Classes_Model>>() {
            @Override
            public void onResponse(Call<List<Classes_Model>> call, Response<List<Classes_Model>> response) {
                try {
                    classes_modelList = response.body();
                    dbHandler.deleteClasses(dbHandler.getWritableDatabase());
                    for (int i = 0; i < classes_modelList.size(); i++) {
                        dbHandler.addClasses(dbHandler.getWritableDatabase(), classes_modelList.get(i));
                    }
                    Log.d(TAG, "onResponse: Classes == " + classes_modelList);
                    AdapterClasses adapterClasses = new AdapterClasses(getContext(), classes_modelList);
                    binding.RecycleViewClasses.setAdapter(adapterClasses);
                /*binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.shimmerViewContainer.stopShimmer();*/
                } catch (Exception e) {
                    Log.d(TAG, "onResponse:  " + e);
                }
            }

            @Override
            public void onFailure(Call<List<Classes_Model>> call, Throwable t) {
                Log.d(TAG, "onFailure:   " + t.getMessage());
            }
        });

    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);

        binding.getRoot();
    }
}