package com.gharpeshiksha.tutorapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.TeacherDetailsProfile;

import java.util.List;

public class Adapter_timeTeachterdetials extends RecyclerView.Adapter<Adapter_timeTeachterdetials.Viewholder> {
    private Context context;
    private List<TeacherDetailsProfile> teacherDet;

    public Adapter_timeTeachterdetials(Context context, List<TeacherDetailsProfile> teacherDet) {
        this.context = context;
        this.teacherDet = teacherDet;
    }

    @NonNull
    @Override
    public Adapter_timeTeachterdetials.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_time_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_timeTeachterdetials.Viewholder holder, int position) {
        TeacherDetailsProfile model = teacherDet.get(position);
        holder.day.setText(model.getDay());
        holder.closetime.setText(model.getClosingTime());
        holder.Opentime.setText(model.getOpeningTime());

    }

    @Override
    public int getItemCount() {
        return teacherDet.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView day,closetime,Opentime;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day);
            Opentime = itemView.findViewById(R.id.openTime);
            closetime = itemView.findViewById(R.id.closeTime);
        }
    }
}
