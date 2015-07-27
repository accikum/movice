package com.lcw.movice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainMyActivity extends Activity {

    private VideoListView video_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new MySurfaceView(this));

        setContentView(R.layout.activity_main_my);
        video_list = (VideoListView) findViewById(R.id.video_list);
//        video.setVideoPath("/storage/sdcard1/DCIM/Camera/VID_20150717_122503.mp4");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (VideoListView.visibleItemCount == 2) {
            video_list.getChildAt(0).findViewById(R.id.video_prepare).setVisibility(View.GONE);
            ((MyVideoView) video_list.getChildAt(0).findViewById(R.id.video)).setVisibility(View.VISIBLE);
            ((MyVideoView) video_list.getChildAt(0).findViewById(R.id.video)).start();
            Log.e("------","sdgsagasgag");
        }
        if (VideoListView.visibleItemCount == 3) {
            video_list.getChildAt(1).findViewById(R.id.video_prepare).setVisibility(View.GONE);
            ((MyVideoView) video_list.getChildAt(1).findViewById(R.id.video)).start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(),"result "+ data.getIntExtra("long",300),Toast.LENGTH_SHORT).show();
    }
}


