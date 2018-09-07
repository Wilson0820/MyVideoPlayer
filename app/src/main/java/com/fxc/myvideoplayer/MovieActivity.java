package com.fxc.myvideoplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;

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
    private MyMediaController mController= null;
    private Handler handler=new Handler();
    private Runnable runnable;
    private RelativeLayout video_group;
    private String vno;
    private String fileindex;
    private String videono;
    private int vfileno ;
    private VerticalSeekBar volbar;
    public  AudioManager audioManager;
    private int maxVol,currentVol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);
        video_group=(RelativeLayout)findViewById(R.id.videoactivity);
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
        mController = new MyMediaController(this,false);
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
            Log.i("fileno", "fileindexonClick:222 "+fileindex);
            playPrewNextVideo();
        }

         runnable= new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,3000);
                toolbar_video.setVisibility(View.GONE);

            }
        };

       /*mController.getViewTreeObserver().dispatchOnGlobalLayout();
       mController.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
           @Override
           public void onGlobalLayout() {
               if (!mController.isShowing()){
                   hideTitleMenu();
                   Log.i("onlayoutchange", "onGobalLayoutChange:--hide ");

               }else{
                   showTitleMenu();
                   Log.i("onlayoutchange", "onGobalLayoutChange:--show ");

               }
           }
       });*/
      mController.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
           @Override
           public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
               if (!mController.isShowing()){
                   hideTitleMenu();
                   Log.i("onlayoutchange", "onLayoutChange:--hide ");

               }else{
                   showTitleMenu();
                   Log.i("onlayoutchange", "onLayoutChange:--show ");

               }
           }
       });
      volbar = (VerticalSeekBar) findViewById(R.id.volbar);
      audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
      maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
      currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
      volbar.setMax(maxVol);
      volbar.setProgress(currentVol);
      volbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
              currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
              volbar.setProgress(currentVol);
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {

          }
      });
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        int sec = (int)savedInstanceState.getLong("time");
        boolean isplay = savedInstanceState.getBoolean("play");
        String currentpath =savedInstanceState.getString("currentpath");
        fileindex = savedInstanceState.getString("fileindex");
        Log.i("path", "onRestoreInstanceState:222 "+currentpath);
        videono=getFileNo(fileindex);
        fileno=Integer.parseInt(videono);
        video.setVideoPath(currentpath);
        setTopBarInfo(currentpath,fileindex);
        video.seekTo(sec);
        if (!isplay)
        {video.pause();}
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int sec = video.getCurrentPosition();
        boolean isplay =video.isPlaying();
        String vpath=video_path.getText().toString();
        String vname=video_name.getText().toString();
        vno=video_no.getText().toString();
        String currentpath= vpath+vname;
        outState.putString("fileindex",vno);
        outState.putString("currentpath",currentpath);
        Log.i("path", "onRestoreInstanceState: 111"+currentpath);
        outState.putBoolean("play",isplay);
        outState.putLong("time",sec);
        //Toast.makeText(this,sec+"s",Toast.LENGTH_SHORT).show();
        Log.i("onSave", "onSaveInstanceState: 1111");
        super.onSaveInstanceState(outState);
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
                    Log.i("fileno", "onClick:next "+fileno);
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
                    Log.i("fileno", "onClick:pre "+vfileno);
                playPrewNextVideo();
                }
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
    public String getFileNo(String s){
        int end = s.lastIndexOf("/");
        Log.i("fileno", "getfileno:111 "+s);
        if(end!=-1){
            return s.substring(0,end);
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
    public void setTopBarInfo(String tpath,String fileindex){
        video_no.setText(fileindex);
        //video_path.setText(tpath);
        videopath = getVideoPath(tpath);
        video_path.setText(videopath);
        videoname =getVideoName(tpath);
        video_name.setText(videoname);
        return;

    }

  /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){

            case MotionEvent.ACTION_UP:
                showTopMenu();

                break;
                default:
                    break;
        }
        //if (mController.isShowing()){showTopMenu();}
        return false;

    }*/
    public void showTopMenu(){
        if (toolbar_video.getVisibility()== View.VISIBLE){
            toolbar_video.setVisibility(View.GONE);

        }else{
            toolbar_video.setVisibility(View.VISIBLE);
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable,3000);

        }
    }
    public void hideTitleMenu()
    {
            toolbar_video.setVisibility(View.GONE);
    }
    public void showTitleMenu()
    {
        toolbar_video.setVisibility(View.VISIBLE);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,3000);
    }
    public class VolumeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
                currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                volbar.setProgress(currentVol);
            }
        }
    }
}
