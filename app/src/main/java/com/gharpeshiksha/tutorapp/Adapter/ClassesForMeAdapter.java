package com.gharpeshiksha.tutorapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.activities.Chats_all_StudentsActivity;
import com.gharpeshiksha.tutorapp.data_model.ClassesForMe;
import com.gharpeshiksha.tutorapp.fragments.ClassesForMeFragment;

import java.util.List;

public class ClassesForMeAdapter extends RecyclerView.Adapter<ClassesForMeAdapter.ClassesForMeViewHolder> {


    Context context;
    private List<ClassesForMe> classesForMeList;

    public ClassesForMeAdapter(Context context, List<ClassesForMe> classesForMeList) {
        this.context = context;
        this.classesForMeList = classesForMeList;
    }

    @NonNull
    @Override
    public ClassesForMeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.layout_classes_for_me, viewGroup, false);
        return new ClassesForMeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesForMeViewHolder classesForMeViewHolder, int i) {
        ClassesForMe classesForMe = classesForMeList.get(i);

       /* classesForMeViewHolder.Chats_btn.setOnClickListener(view -> {
            Toast.makeText(context, "this is the contact view", Toast.LENGTH_SHORT).show();
        });*/

    }

    @Override
    public int getItemCount() {
        return classesForMeList.size();
    }

    public class ClassesForMeViewHolder extends RecyclerView.ViewHolder {


        public ClassesForMeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
