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
import com.gharpeshiksha.tutorapp.data_model.Subject_Class_Model;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSubject extends RecyclerView.Adapter<AdapterSubject.ViewHolder> {
    private Context context;
    private List<Subject_Class_Model> subject_class_modelList;

    public AdapterSubject(Context context, List<Subject_Class_Model> subject_class_modelList) {
        this.context = context;
        this.subject_class_modelList = subject_class_modelList;
    }

    @NonNull
    @Override
    public AdapterSubject.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_contact_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSubject.ViewHolder holder, int position) {

        Subject_Class_Model subject_class_model = subject_class_modelList.get(position);
        holder.Name.setText(subject_class_model.getSub());
        String url = subject_class_model.getImg();
        Picasso.get()
                .load(url)
                .noFade()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return subject_class_modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView Name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Text_name_subject);
            imageView = itemView.findViewById(R.id.image_subject);

        }
    }
}
