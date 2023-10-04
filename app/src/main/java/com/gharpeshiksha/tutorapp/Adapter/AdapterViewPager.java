package com.gharpeshiksha.tutorapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.gharpeshiksha.tutorapp.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterViewPager extends RecyclerView.Adapter<AdapterViewPager.ViewHolder> {

    private ViewPager2 viewPager2;
    private Context context;
    private List<String> viewPagerModelList;

    public AdapterViewPager(ViewPager2 viewPager2, List<String> viewPagerModelList) {
        this.viewPager2 = viewPager2;
        this.context = context;
        this.viewPagerModelList = viewPagerModelList;
        this.runnable = runnable;
    }


    @NonNull
    @Override
    public AdapterViewPager.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewPager.ViewHolder holder, int position) {

        String url = viewPagerModelList.get(position);
        Picasso.get().load(url).into(holder.imageView);

        if (position == viewPagerModelList.size() - 2) {
            viewPager2.post(runnable);

        }
    }

    @Override
    public int getItemCount() {
        return viewPagerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPagerModelList.addAll(viewPagerModelList);
            notifyDataSetChanged();
        }
    };
}