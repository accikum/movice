package com.lcw.movice;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义的ListView
 * 实现了OnItemClickListener 和 OnScrollListener
 * Created by luchangwei on 2015/7/17.
 */
public class VideoListView extends ListView implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private static final String TAG = "VideoListView";
    private View itemView;
    public static int visibleItemCount;//屏幕上ListView中可见的Item 数
    int[] su = new int[2];//每个Item的位置
    private MediaAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public VideoListView(Context context) {
        this(context, null);

    }

    public VideoListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnItemClickListener(this);
        setOnScrollListener(this);
        adapter = new MediaAdapter(getContext());
        setAdapter(adapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE ) {
            this.getLocationInWindow(su);
            Log.e("supermlist_", su[0] + " " + su[1]);
            /*
            以下代码只实现一个视频播放
             */
            itemView = visibleItemCount == 2 ? this.getChildAt(0) : this.getChildAt(1);
            MyVideoView video = (MyVideoView) itemView.findViewById(R.id.video);
            for (int i=0;i<visibleItemCount;i++){
                if (view.getChildAt(i).findViewById(R.id.video)==video) {
                    itemView.findViewById(R.id.video_prepare).setVisibility(GONE);
                    video.setVisibility(VISIBLE);
                    if (video!=null)
                        video.start();
                }else {
                    MyVideoView v = (MyVideoView) this.getChildAt(i).findViewById(R.id.video);
                    v.setVisibility(GONE);
                    this.getChildAt(i).findViewById(R.id.video_prepare).setVisibility(VISIBLE);
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.visibleItemCount = visibleItemCount;
//        Log.e("positon-", view.getTop() + " " + view.getBottom() + " firstVisibleItem " + firstVisibleItem
//                + " visibleItemCount " + visibleItemCount);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        int b = view.findViewById(R.id.video_player_control).getVisibility();

        if (b == View.VISIBLE) {
            view.findViewById(R.id.video_player_control).setVisibility(GONE);
//            ((VideoView)view.findViewById(R.id.video)).pause();
//            Toast.makeText(getContext(), "video_pause_on", Toast.LENGTH_SHORT).show();
            Log.e("full", "full");
        } else {
            view.findViewById(R.id.video_player_control).setVisibility(VISIBLE);
//            ((VideoView)view.findViewById(R.id.video)).start();
//            Toast.makeText(getContext(), "video_pause_Off", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.findViewById(R.id.video_player_control).setVisibility(GONE);
                }
            }, 2500);
        }
        for (int i = 0; i < visibleItemCount; i++) {
            if (view == getChildAt(i)) {
                for (int j = 0; j < visibleItemCount; j++) {
                    if (j == i) continue;
                    this.getChildAt(j).findViewById(R.id.video_prepare).setVisibility(VISIBLE);
                    MyVideoView v = (MyVideoView) this.getChildAt(j).findViewById(R.id.video);
                    v.pause();
                    v.setVisibility(GONE);
                }
            }
        }
    }
}

class MediaAdapter extends BaseAdapter {

    public static final String path2 = "http://115.231.72.149/fcs132.56.com/flvdownload/21/14/sc_139289302113hd_super.flv.mp4";//网络访问的视频资源地址


    private Context context;

    public MediaAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        Log.e("get--Id", position + "");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.video_item, null);
            holder = new ViewHolder(convertView);
            holder.video.setVideoPath(path2);
            //TODO
            //holder.video.setHandler(handler);
            holder.video.setSeekBar(holder.video_seekbar);
            holder.video_prepare.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    holder.video_prepare.setVisibility(View.GONE);
                    holder.video.setVisibility(View.VISIBLE);
                    holder.video.start();
                    return false;
                }
            });
            holder.full_screen_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("curr", holder.video.getCurrentPosition());
                    intent.setClass(context, FullVideoPlayerActivity.class);
                    ((MainMyActivity)context).startActivityForResult(intent,200);
                }
            });

            holder.video_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.video.isPlaying()) {
                        holder.video.pause();
                        Toast.makeText(context, "video_pause_on", Toast.LENGTH_SHORT).show();
                    } else {
                        holder.video.start();
                        Toast.makeText(context, "video_pause_Off", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.video.setVisibility(View.GONE);
            holder.video.setVideoPath(path2);
        }
        return convertView;
    }

    private class ViewHolder {
        public ViewHolder(View convertView){
            video_title = (RelativeLayout) convertView.findViewById(R.id.video_title);
            full_screen_play = (TextView) convertView.findViewById(R.id.full_screen_play);
            video_prepare = (ImageView) convertView.findViewById(R.id.video_prepare);

            video_seekbar = (SeekBar) convertView.findViewById(R.id.video_seekbar);
            video = (MyVideoView) convertView.findViewById(R.id.video);
            video_player_control = (RelativeLayout) convertView.findViewById(R.id.video_player_control);
            video_pause = (TextView) convertView.findViewById(R.id.video_pause);
            video_fav = (ImageView) convertView.findViewById(R.id.video_fav);
            video_share = (ImageView) convertView.findViewById(R.id.video_share);

        };
        MyVideoView video;
        RelativeLayout video_title;
        RelativeLayout video_player_control;
        TextView full_screen_play;
        ImageView video_prepare;
        TextView video_pause;
        ImageView video_fav;
        ImageView video_share;

        SeekBar video_seekbar;
    }
}