package com.fxc.myvideoplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

public class MovieActivity extends AppCompatActivity {

    private VideoView video;
    private TextView video_path;
    private TextView video_name;
    private TextView video_no;
    private String tpath;
    private Toolbar toolbar_video;
    private int filesum;
    private int fileno;
    private String videoname;
    private String videopath;
    private String tpathPrew;
    private String tpathNext;
    private ArrayList<String> moviepathlist;
    private MediaController mController= null;
    private boolean flag = false;
    private Handler handler=new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);
        video = (VideoView) findViewById(R.id.video);
        toolbar_video = findViewById(R.id.toolbar_video);
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
        toolbar_video.setVisibility(View.GONE);
        flag=false;
        mController = new MediaController(this,false);
        Intent intent = getIntent();
        tpath = intent.getStringExtra("moviename");
        filesum = intent.getIntExtra("filesum",0);
        fileno = intent.getIntExtra("fileno",0);
        Bundle bundle = intent.getBundleExtra("moviepathlist");
        moviepathlist =  (ArrayList<String>)bundle.getSerializable("Arraylist");
        setTopBarInfo(tpath,fileno,filesum);
        final File file = new File(tpath);
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

            playPrewNextVideo();
        }
         runnable= new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,3000);
                toolbar_video.setVisibility(View.GONE);
                flag =false ;
            }
        };

    }
    public void playPrewNextVideo(){
        mController.setPrevNextListeners(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(fileno<filesum)
                {

                tpathNext=moviepathlist.get(fileno);
                fileno =fileno+1;
                video.setVideoPath(tpathNext);
                video.setMediaController(mController);
                mController.setMediaPlayer(video);
                video.start();
                setTopBarInfo(tpathNext,fileno,filesum);
                Toast.makeText(MovieActivity.this, "下一個", Toast.LENGTH_SHORT).show();
                playPrewNextVideo();
                }
                else{
                    Toast.makeText(MovieActivity.this, "The last one", Toast.LENGTH_SHORT).show();
                }
            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (fileno>1){
                Toast.makeText(MovieActivity.this, "上一個", Toast.LENGTH_SHORT).show();
                fileno=fileno-1;
                tpathPrew=moviepathlist.get(fileno-1);
                video.setVideoPath(tpathPrew);
                video.setMediaController(mController);
                mController.setMediaPlayer(video);
                video.start();
                setTopBarInfo(tpathPrew,fileno,filesum);
                playPrewNextVideo();}
                else{
                    Toast.makeText(MovieActivity.this, "The first one", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public String getVideoName(String tpath){
        int start = tpath.lastIndexOf("/");
        int end = tpath.length();
        if(start!=-1&&end!=-1){
            return tpath.substring(start+1,end);
        }else{
            return null;
        }
    }
    public String getVideoPath(String tpath){
        int end = tpath.lastIndexOf("/");
        if(end!=-1){
            return tpath.substring(0,end+1);
        }else{
            return null;
        }
    }
    public void setTopBarInfo(String tpath,int fileno,int filesum){
        video_no.setText(fileno+"/"+filesum);
        //video_path.setText(tpath);
        videopath = getVideoPath(tpath);
        video_path.setText(videopath);
        videoname =getVideoName(tpath);
        video_name.setText(videoname);
        return;

    }

   @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (flag == true){
                toolbar_video.setVisibility(View.GONE);
                flag = false;
                  }else{
                toolbar_video.setVisibility(View.VISIBLE);
                flag = true;
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,3000);
                 }
                 break;
                default:
                    break;
        }
        return true;

    }
}
