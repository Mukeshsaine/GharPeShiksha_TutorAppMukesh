package com.gharpeshiksha.tutorapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.Written_Testimonial;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_testimonial extends RecyclerView.Adapter<Adapter_testimonial.ViewHolder> {

    private List<Written_Testimonial> google_fbList;
    private Context context;

    public Adapter_testimonial(List<Written_Testimonial> google_fbList, Context context) {
        this.google_fbList = google_fbList;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_testimonial.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contect_wirtten_testimonial,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_testimonial.ViewHolder holder, int position) {
        Written_Testimonial google_fb = google_fbList.get(position);
        holder.textView.setText(google_fb.getDesc());
        holder.id.setText(google_fb.getTutorID());
        holder.name.setText(google_fb.getTutorName());
        String url = google_fb.getPicurl();
        Picasso.get().load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return google_fbList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView,id,name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.google_fb_images);
            imageView.isInEditMode();
            textView = itemView.findViewById(R.id.google_text);
            id = itemView.findViewById(R.id.idtutor);
            name = itemView.findViewById(R.id.name);
        }
    }
}
