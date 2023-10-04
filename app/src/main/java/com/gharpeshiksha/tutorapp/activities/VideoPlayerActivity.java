package com.gharpeshiksha.tutorapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gharpeshiksha.tutorapp.databinding.ActivityVideoPlayerBinding;
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
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayerActivity extends AppCompatActivity {
    private ActivityVideoPlayerBinding binding;
    private String TAG = "VideoPlayerActivity.java";
    private SimpleExoPlayer exoPlayer;
    private ApiServies apiInterface;
    Call<ResponseBody> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent receivedIntent = getIntent();
        String videoId = "";
        apiInterface = RetrofitClient.getClient().create(ApiServies.class);
        if (receivedIntent.hasExtra("videoId")) {
            videoId = receivedIntent.getStringExtra("videoId");
            playVideo(videoId);
        }
    }

    private void playVideo(String videoId) {
        call = apiInterface.getVideoTestimonials(videoId);
        Log.d(TAG, "playVideo: this is  ==== "+videoId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        binding.bar.setVisibility(View.GONE);
                        binding.exoPlayerView.setVisibility(View.VISIBLE);
                        String url = new JSONObject(response.body().string()).getString("url");
                        //Below is default bandWidth meter counter
                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                        //Below is TractSelector for default seek bar
                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                        //Below is we are creating instance of SimpleExoPlayer using its factory method and pass context and TrackSelector interface instance
                        exoPlayer = ExoPlayerFactory.newSimpleInstance(VideoPlayerActivity.this, trackSelector);
                        //Create Uniform Resource Identifier uri which exoplayer understands any file
                        Uri uri = Uri.parse(url);
                        //DefaultHttpResourceFactory class instance to create user agent with any string
                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_view");
                        //Extractor to extract media file into bytes to understand by system.
                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                        //setup MediaSource instance which add extractor and seek bar and control on it and leave
                        //event handler and event listener null
                        MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
                        //set SimpleExoPlayer object on ExoPlayerView layout element
                        binding.exoPlayerView.setPlayer(exoPlayer);
                        //set mediaSource on SimpleExoPlayer object to set seekbar and other control and data extractors
                        exoPlayer.prepare(mediaSource);
                        //play when exoPlayer is prepare
                        exoPlayer.setPlayWhenReady(true);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            //cancel api call
            call.cancel();
            //stop video
            exoPlayer.stop();
            //release all resources by exoplayer used.
            exoPlayer.release();
        }
    }
}
