package com.gharpeshiksha.tutorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.gharpeshiksha.tutorapp.Adapter.Adapter_Notes;
import com.gharpeshiksha.tutorapp.data_model.NotesModel;
import com.gharpeshiksha.tutorapp.databinding.ActivityVideoBinding;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity {

    ActivityVideoBinding binding;
    List<NotesModel> VideoModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back1.setOnClickListener(view -> {
            finish();
        });

        binding.RecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        String url1 = "https://www.saga.co.uk/contentlibrary/saga/publishing/verticals/technology/internet/communications/youtube-1.png";
        String url2 = "https://www.saga.co.uk/contentlibrary/saga/publishing/verticals/technology/internet/communications/youtube-1.png";
        String url3 = "https://www.saga.co.uk/contentlibrary/saga/publishing/verticals/technology/internet/communications/youtube-1.png";
        String url4 = "https://www.saga.co.uk/contentlibrary/saga/publishing/verticals/technology/internet/communications/youtube-1.png";
        String url5 = "https://www.saga.co.uk/contentlibrary/saga/publishing/verticals/technology/internet/communications/youtube-1.png";

        VideoModelList.add(new NotesModel(url1,"Physics"));
        VideoModelList.add(new NotesModel(url2,"Physics"));
        VideoModelList.add(new NotesModel(url3,"Physics"));
        VideoModelList.add(new NotesModel(url4,"Physics"));
        VideoModelList.add(new NotesModel(url5,"Physics"));

        Adapter_Notes adapter_notes = new Adapter_Notes(getApplicationContext(),VideoModelList);
        binding.RecycleView.setAdapter(adapter_notes);
    }
}