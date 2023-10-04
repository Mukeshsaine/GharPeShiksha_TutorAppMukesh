package com.gharpeshiksha.tutorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.gharpeshiksha.tutorapp.Adapter.Adapter_Notes;
import com.gharpeshiksha.tutorapp.data_model.NotesModel;
import com.gharpeshiksha.tutorapp.databinding.ActivityNotesBinding;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    ActivityNotesBinding binding;
    List<NotesModel> notesModelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back1.setOnClickListener(view -> {
            finish();
        });

        binding.RecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        String url1 = "https://www.wikihow.com/images/thumb/8/88/Take-Notes-Step-4-Version-3.jpg/aid319398-v4-728px-Take-Notes-Step-4-Version-3.jpg";
        String url2 = "https://www.wikihow.com/images/thumb/8/88/Take-Notes-Step-4-Version-3.jpg/aid319398-v4-728px-Take-Notes-Step-4-Version-3.jpg";
        String url3 = "https://www.wikihow.com/images/thumb/8/88/Take-Notes-Step-4-Version-3.jpg/aid319398-v4-728px-Take-Notes-Step-4-Version-3.jpg";
        String url4 = "https://www.wikihow.com/images/thumb/8/88/Take-Notes-Step-4-Version-3.jpg/aid319398-v4-728px-Take-Notes-Step-4-Version-3.jpg";
        String url5 = "https://www.wikihow.com/images/thumb/8/88/Take-Notes-Step-4-Version-3.jpg/aid319398-v4-728px-Take-Notes-Step-4-Version-3.jpg";
        notesModelList.add(new NotesModel(url1,"Physics"));
        notesModelList.add(new NotesModel(url2,"Physics"));
        notesModelList.add(new NotesModel(url3,"Physics"));
        notesModelList.add(new NotesModel(url4,"Physics"));
        notesModelList.add(new NotesModel(url5,"Physics"));
        Adapter_Notes adapter_notes = new Adapter_Notes(getApplicationContext(),notesModelList);
        binding.RecycleView.setAdapter(adapter_notes);
    }

}