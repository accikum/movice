package com.lcw.movice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by luchangwei on 2015/7/21.
 */
public class FullVideoPlayerActivity extends Activity {
    MyVideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.full_video_player_activity);
        videoView = (MyVideoView) findViewById(R.id.full_video);
        String path = "http://115.231.72.149/fcs132.56.com/flvdownload/21/14/sc_139289302113hd_super.flv.mp4";//网络访问的视频资源地址
        videoView.setVideoPath(path);
        int curr = getIntent().getIntExtra("curr",0);
        videoView.seekTo(curr);
        videoView.start();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("long",videoView.getCurrentPosition());
        setResult(200, intent);
        finish();
    }
}
