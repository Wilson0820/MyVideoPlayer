package com.fxc.myvideoplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MovieActivity extends AppCompatActivity {

    private VideoView video;
    private TextView video_path;
    private TextView video_name;
    private TextView video_no;
    private String tpath;
    private Toolbar toolbar_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);
        video = (VideoView) findViewById(R.id.video);
        toolbar_video = findViewById(R.id.toolbar_video);
        //toolbar_video.setNavigationIcon(R.mipmap.back);
        video_path=findViewById(R.id.top_vdieopath);
        video_name=findViewById(R.id.top_vdieoname);
        video_no=findViewById(R.id.top_vdieonum);
        toolbar_video.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2list =new Intent();
                intent2list.setClass(MovieActivity.this, MainActivity.class);
                startActivity(intent2list);
                finish();
            }
        });
        MediaController mController = new MediaController(this,false);
        Intent intent = getIntent();
        tpath = intent.getStringExtra("moviename");
        //video_path.setText(tpath);
        File file = new File(tpath);
        if (file.exists()) {
            video.setVideoPath(file.getAbsolutePath());
            video.setMediaController(mController);
            mController.setMediaPlayer(video);
            video.start();
            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    Intent intent2list =new Intent();
                    intent2list.setClass(MovieActivity.this, MainActivity.class);
                    startActivity(intent2list);
                    finish();

                }
            });
            mController.setPrevNextListeners(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(MovieActivity.this, "下一個", Toast.LENGTH_SHORT).show();

                }
            }, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(MovieActivity.this, "上一個", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

}
