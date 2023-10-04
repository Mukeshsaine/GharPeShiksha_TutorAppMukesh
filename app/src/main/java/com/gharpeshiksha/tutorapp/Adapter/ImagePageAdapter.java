package com.gharpeshiksha.tutorapp.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gharpeshiksha.tutorapp.R;
import com.glide.slider.library.Tricks.InfiniteViewPager;

public class ImagePageAdapter extends PagerAdapter {


    Context context;
    String imageArray[];


    public ImagePageAdapter(Context act, String[] imgArra) {
        imageArray = imgArra;
        context = act;

    }

    public int getCount() {
        return imageArray.length;
    }

    public Object instantiateItem(View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext
                ().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.flipper_offers, null);

        ImageView im = (ImageView) layout.findViewById(R.id.myimage);

        //  im.setImageResource(imageArray[position]);
   /*     Glide.with(context).setDefaultRequestOptions(RequestOptions.centerCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .load("https://cdn.vox-cdn.com/thumbor/th5YNVqlkHqkz03Va5RPOXZQRhA=/0x0:2040x1360/1200x800/filters:focal(857x517:1183x843)/cdn.vox-cdn.com/uploads/chorus_image/image/57358643/jbareham_170504_1691_0020.0.0.jpg").into(im);

*/

        try{
            Glide.with(context).setDefaultRequestOptions(RequestOptions.centerCropTransform()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .load(imageArray[position])
                    .placeholder(R.drawable.gps_web_logo)
                    .into(im);
        } catch(Exception e) {
            Log.v("Image.java", e + "");
        }


        LinearLayout txt = (LinearLayout) layout.findViewById(R.id.dotsLayout);


        ((InfiniteViewPager) collection).addView(layout, 0);
        return layout;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((InfiniteViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
