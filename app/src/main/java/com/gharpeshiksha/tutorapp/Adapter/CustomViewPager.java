package com.gharpeshiksha.tutorapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.gharpeshiksha.tutorapp.R;

public class CustomViewPager extends PagerAdapter {

    private final LayoutInflater Inflater;
    Context context;
    int[] images_id;
    ImageView imageView;
    TextView textView;
    String[] texts = {"Providing Teaching Opportunities", "Find Your Student Nearby you", "Earn Extra Income By Sharing Your Knowledge"};


    public CustomViewPager(Context context, int[] images_id) {

        this.context = context;
        this.images_id = images_id;
        Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        View itemview = Inflater.inflate(R.layout.view_pager_layout, container, false);

        imageView = itemview.findViewById(R.id.imageview);
        textView = itemview.findViewById(R.id.textview);

        imageView.setImageResource(images_id[position]);
        textView.setText(texts[position]);
        container.addView(itemview);

        return itemview;
    }

    @Override
    public int getCount() {
        return images_id.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);

    }
}

