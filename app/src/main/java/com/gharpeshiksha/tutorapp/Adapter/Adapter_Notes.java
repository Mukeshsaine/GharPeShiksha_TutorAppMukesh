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
import com.gharpeshiksha.tutorapp.data_model.NotesModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Notes extends RecyclerView.Adapter<Adapter_Notes.ViewHolder> {

    private Context context;
    private List<NotesModel> notesModelList;

    public Adapter_Notes(Context context, List<NotesModel> notesModelList) {
        this.context = context;
        this.notesModelList = notesModelList;
    }

    @NonNull
    @Override
    public Adapter_Notes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_contact_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Notes.ViewHolder holder, int position) {

        NotesModel notesModel = notesModelList.get(position);
        holder.SubName.setText(notesModel.getSubject());
        String Url = notesModel.getImg();
        Picasso.get().load(Url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return notesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView SubName;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            SubName = itemView.findViewById(R.id.text_Physics);
            imageView = itemView.findViewById(R.id.note_imageView);
        }
    }
}
