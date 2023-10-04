package com.gharpeshiksha.tutorapp.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.activities.VideoPlayerActivity;
import com.gharpeshiksha.tutorapp.data_model.Model_Imonials;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Imonials extends RecyclerView.Adapter<Adapter_Imonials.viewHolder> {
    private Context context;
    private List<Model_Imonials> model_imonialsList;
   // SimpleExoPlayer simpleExoPlayer;
    String TAG = "Adapter_Imonials.java";



   // VideoView videoView;
    public Adapter_Imonials(Context context, List<Model_Imonials> model_imonialsList) {
        this.context = context;
        this.model_imonialsList = model_imonialsList;
    }



    @NonNull
    @Override
    public Adapter_Imonials.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_video_imonials,parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Model_Imonials model_imonials = model_imonialsList.get(position);
        holder.textView.setText(model_imonials.getDesc());
        holder.viewCard.setOnClickListener(view -> {
            Intent i = new Intent(context, VideoPlayerActivity.class);
            i.putExtra("videoId", model_imonials.getVideoId());
            context.startActivity(i);
        });

        Glide.with(context).load(model_imonials.getImgUrl()).into(holder.thumbnailImg);
    }


    @Override
    public int getItemCount() {
        return model_imonialsList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
       // WebView youtube_player_view;
        TextView textView;
        CardView viewCard;
        ImageView thumbnailImg;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_discrition);
            viewCard = itemView.findViewById(R.id.video);
            thumbnailImg = itemView.findViewById(R.id.thumbnail);
        }

    }
}
