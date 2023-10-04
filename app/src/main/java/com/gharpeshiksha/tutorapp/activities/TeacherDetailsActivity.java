package com.gharpeshiksha.tutorapp.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.gharpeshiksha.tutorapp.Adapter.AdapterSpinnerTimeSet;
import com.gharpeshiksha.tutorapp.Adapter.Adapter_timeTeachterdetials;
import com.gharpeshiksha.tutorapp.BasicFeatures;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.Plan_Models;
import com.gharpeshiksha.tutorapp.data_model.SpinnerModel;
import com.gharpeshiksha.tutorapp.data_model.TeacherDetailsProfile;
import com.gharpeshiksha.tutorapp.databinding.ActivityTeacherDetailsBinding;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.gharpeshiksha.tutorapp.utils.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityTeacherDetailsBinding binding;
    Fragment Map;
    GoogleMap googleMap;
    LatLng latLng;
    SessionConfig sessionConfig;
    ArrayList<TeacherDetailsProfile> teacherlist;
    double lat, lng;
    String TAG = "TeacherDetailsActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionConfig = new SessionConfig(getApplication());
        if(Utility.isNetworkAvailable(TeacherDetailsActivity.this)) {
            Teachter_details_Call_API();
            Other_Click_Calling();
            Swipi_Refresh_Methods();
            getPlanDetails();
        } else {
            Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPlanDetails() {
        new BasicFeatures().CheckPaymentStatus(TeacherDetailsActivity.this, sessionConfig.getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {
                if(status.equals("active")) {
                    ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
                    apiServies.getPlan(sessionConfig.getPhone() + "").enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.d(TAG, "onResponse: Call Api Play ====  " + response.body());
                            Plan_Models planModel = new Plan_Models();
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response.body().toString());
                                binding.activeplan.setText("Active Plan : " + planModel.getPlanDetail());
                                Log.d(TAG, "onResponse: Active ====== = =" + planModel.getPlanDetail());

                                Log.d(TAG, "onResponse: josjfjdsfkj  ===  " + jsonObject);
                                // binding.viewremain.setText(""+planModel.expiry_date);
                                if (jsonObject.get("Status").equals("Success")) {
                                    if (!jsonObject.get("planDetail").toString().isEmpty()) {
                                        binding.subscriptiondetailslayout.setVisibility(View.VISIBLE);
                                        if (jsonObject.get("planDetail").toString().equals("Free")) {
                                            binding.activeplan.setText("" + planModel.getPlanDetail());
                                        } else {
                                            binding.activeplan.setText("Active Plan : " + jsonObject.get("planDetail").toString());
                                        }
                                        binding.dateexp.setText("Expiry Date : " + jsonObject.get("expiry_date").toString());
                                        binding.viewremain.setText("Remaining View : " + jsonObject.get("contact_viewed").toString());
                                    }
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError: ");
            }
        });
    }

    private void Other_Click_Calling() {
        binding.back1.setOnClickListener(v -> {
            finish();
        });

        binding.SubjectChange.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ClassesActivity.class));
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Maping);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Maping);
        mapFragment.getMapAsync(TeacherDetailsActivity.this);
    }

    private void Teachter_details_Call_API() {
        List<String> mode = new ArrayList<>();
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.TeacherDetails(sessionConfig.getPhone() + "").enqueue(new Callback<List<TeacherDetailsProfile>>() {
            @Override
            public void onResponse(Call<List<TeacherDetailsProfile>> call, Response<List<TeacherDetailsProfile>> response) {
                if (response.isSuccessful()) {
                    try {

                        teacherlist = (ArrayList<TeacherDetailsProfile>) response.body();
                        Log.d(TAG, "onResponse: === " + teacherlist.toString());

                        //String pos1 = pos+1;
                        TeacherDetailsProfile teacherDet = teacherlist.get(teacherlist.size() - 1);

                        binding.EditProfile.setOnClickListener(view -> {
                            Intent intent = new Intent(TeacherDetailsActivity.this, EditProfileActivity.class);
                            String id = String.valueOf(teacherDet.getTutId());
                            intent.putExtra("id", id);
                            Log.d(TAG, "Other_Click_Calling: == dgdfsg = = = " + id);
                            startActivity(intent);
                        });
                        Log.d(TAG, "onResponse: " + teacherDet.getLon() + ", " + teacherDet.getLat());
                        lat = teacherDet.getLat();
                        lng = teacherDet.getLon();
                        //create adapter
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        Adapter_timeTeachterdetials adapter = new Adapter_timeTeachterdetials(getApplicationContext(), teacherlist);
                        binding.Recycleview.setAdapter(adapter);
                        binding.Recycleview.setLayoutManager(layoutManager);

                        String mode2 = "";
                        for (int i = 0; i < teacherDet.getMode().size(); i++) {
                            String mode1 = String.valueOf(teacherDet.getMode().get(i));
                            mode2 = mode2 + mode1 + "\n";
                            Log.d(TAG, "onResponse: ===== mmj == " + mode1);
                            binding.modeltea.setText(mode2);

                        }
                        String subject = "";
                        for (int j = 0; j < teacherDet.getSubject().size(); j++) {
                            String sub = teacherDet.getSubject().get(j);
                            subject = subject + sub + "";
                            binding.studentsSubject.setText(subject);
                        }


                        String url = teacherDet.getProfilepic();
                        Picasso.get().load(url).into(binding.imagetea);
                        String Age = String.valueOf(teacherDet.getAge());
                        binding.age.setText(Age);
//                        binding.RatingBar.setRating((float) teacherDet.getRating());
                        String nametea = teacherDet.getTutorName();
                        binding.name.setText(nametea);
                        String Exp = teacherDet.getExperience();
                        binding.Experiene.setText(Exp);
                        String Qualification = teacherDet.getQualification();
                        binding.Qualification.setText(Qualification);
                        String about = teacherDet.getAbout();
                        binding.abouttea.setText(about);
                        showLocation();

                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<TeacherDetailsProfile>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void showLocation() {
        Log.d(TAG, "showLocation: " + lat + "," + lng);
        latLng = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    private void Teachter_details_Call_API1() {

        List<String> mode = new ArrayList<>();
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.TeacherDetails(sessionConfig.getPhone() + "").enqueue(new Callback<List<TeacherDetailsProfile>>() {
            @Override
            public void onResponse(Call<List<TeacherDetailsProfile>> call, Response<List<TeacherDetailsProfile>> response) {
//                Log.d(TAG, "onResponse: Teacher = ="+response.body().get(0).getDate());
                if (response.isSuccessful()) {
                    try {
                        ArrayList<TeacherDetailsProfile> teacherlist = (ArrayList<TeacherDetailsProfile>) response.body();
                        Log.d(TAG, "onResponse: === " + teacherlist.toString());

                        TeacherDetailsProfile teacherDet = teacherlist.get(teacherlist.size() - 1);

                        Log.d(TAG, "onResponse: " + teacherDet.getLon() + ", " + teacherDet.getLat());
                        lat = teacherDet.getLat();
                        lng = teacherDet.getLon();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        Adapter_timeTeachterdetials adapter = new Adapter_timeTeachterdetials(getApplicationContext(), teacherlist);
                        binding.Recycleview.setAdapter(adapter);
                        binding.Recycleview.setLayoutManager(layoutManager);


                        String mode2 = "";
                        for (int i = 0; i < teacherDet.getMode().size(); i++) {
                            String mode1 = String.valueOf(teacherDet.getMode().get(i));
                            mode2 = mode2 + mode1 + "\n";
                            Log.d(TAG, "onResponse: ===== mmj == " + mode1);
                            binding.modeltea.setText(mode2);

                        }
                        String subject = "";
                        for (int j = 0; j < teacherDet.getSubject().size(); j++) {
                            String sub = teacherDet.getSubject().get(j);
                            subject = subject + sub + "";
                            binding.studentsSubject.setText(subject);
                        }


                        String url = teacherDet.getProfilepic();
                        Picasso.get().load(url).into(binding.imagetea);
                        String Age = String.valueOf(teacherDet.getAge());
                        binding.age.setText(Age);
                        /* binding.RatingBar.setRating((float) teacherDet.getRating());*/
                        String nametea = teacherDet.getTutorName();
                        binding.name.setText(nametea);
               /* String class_session = teacherlist.get(0).getFeeSession();
                binding.studentSession.setText(class_session);*/
                        String Exp = teacherDet.getExperience();
                        binding.Experiene.setText(Exp);
                        String Qualification = teacherDet.getQualification();
                        binding.Qualification.setText(Qualification);
              /*  String students = teacherlist.get(0).getClasses();
                binding.studentsclass1.setText(students);*/
                        String about = teacherDet.getAbout();
                        binding.abouttea.setText(about);
                        showLocation();

                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e.getMessage());
                    }
                } else {

                }

            }

            @Override
            public void onFailure(Call<List<TeacherDetailsProfile>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    public void Swipi_Refresh_Methods() {
        binding.swiperfresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Teachter_details_Call_API1();
                Other_Click_Calling();

                binding.swiperfresh.setRefreshing(false);
            }
        });
    }
}