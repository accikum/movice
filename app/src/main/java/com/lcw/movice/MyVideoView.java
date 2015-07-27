package com.lcw.movice;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.VideoView;

/**
 * 自定义VideoView 实现可调节播放区域大小。
 * 可自定义添加seekBar 进行调节视频的播放进度以及查看
 * Created by luchangwei on 2015/7/20.
 */
public class MyVideoView extends VideoView implements MediaPlayer.OnPreparedListener {

    private static final String TAG = "MyVideoView";
    private int mVideoWidth;
    private int mVideoHeight;


    private Handler handler;
    private SeekBar seekBar;//添加的进度显示seekBar
    private int duration;// 视频总长度

    public MyVideoView(Context context) {
        this(context, null);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
//        setZOrderMediaOverlay(false);
        setOnPreparedListener(this);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
        initView();
    }

    private void initView() {
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int curr = seekBar.getProgress();
                seekTo((int) (duration * (curr / 100.0)));//跳转到指定地方播放
                start();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) {
        super.setOnCompletionListener(l);
        Log.e(TAG, "setOnCompletionListener");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        duration = mp.getDuration();
        mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            int curr;

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                //查看进度

                if (seekBar != null) {
                    curr = mp.getCurrentPosition();
                    seekBar.setProgress((int) (100.0 * (curr / (duration * 1.0))));
                    seekBar.setSecondaryProgress(percent);
                }
            }
        });
    }
}
