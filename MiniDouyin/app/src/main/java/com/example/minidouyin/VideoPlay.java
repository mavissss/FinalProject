package com.example.minidouyin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import retrofit2.http.Url;

public class VideoPlay extends AppCompatActivity {
    private Uri uri;
    private VideoView videoView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags
                    (
                            WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN
                    );
        }
        setContentView(R.layout.activity_video_play);
        videoView=findViewById(R.id.videoView);
        Intent intent=getIntent();
        String videoUrl=intent.getStringExtra("videoUrl");


        uri= Uri.parse(videoUrl);
        videoView.setVideoURI(uri);
        videoView.setVisibility(View.VISIBLE);
        videoView.start();

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(videoView.isPlaying()){
                    videoView.pause();
                }
                else{
                    videoView.start();
                }
                return false;
            }

    });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                findViewById(R.id.textView).setVisibility(View.GONE);
            }
        });
    }
}
