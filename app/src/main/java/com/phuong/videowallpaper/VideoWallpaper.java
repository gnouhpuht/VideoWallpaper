package com.phuong.videowallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import java.io.IOException;

public class VideoWallpaper extends WallpaperService {
    public String videoPath;
    private MediaPlayer mMediaPlayer;
    public static final String VIDEO_PARAMS_CONTROL_ACTION = "com.play.zv";
    public static final String KEY_ACTION = "action";
    public static final int ACTION_VOICE_SILENCE = 110;
    public static final int ACTION_VOICE_NORMAL = 111;



    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }

    public static void voiceSilence(Context context){
        Intent intent = new Intent(VideoWallpaper.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(VideoWallpaper.KEY_ACTION, VideoWallpaper.ACTION_VOICE_SILENCE);
        context.sendBroadcast(intent);
    }

    public static void voiceNormal(Context context){
        Intent intent = new Intent(VideoWallpaper.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(VideoWallpaper.KEY_ACTION, VideoWallpaper.ACTION_VOICE_NORMAL);
        context.sendBroadcast(intent);
    }

    public void setToWallPaper(Context context, String path){
        videoPath = path;
        System.out.println("video path！"+videoPath);
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context, VideoWallpaper.class));
        context.startActivity(intent);
    }

     class VideoEngine extends Engine{
         private BroadcastReceiver mvideoParamsControlReceiver;

         @Override
         public void onCreate(SurfaceHolder surfaceHolder) {
             super.onCreate(surfaceHolder);
             IntentFilter intentFilter = new IntentFilter(VIDEO_PARAMS_CONTROL_ACTION);
             registerReceiver(mvideoParamsControlReceiver = new BroadcastReceiver() {
                 @Override
                 public void onReceive(Context context, Intent intent) {
                     //L.d("onReceive");
                     int action = intent.getIntExtra(KEY_ACTION, -1);

                     switch (action) {
                         case ACTION_VOICE_NORMAL:
                             mMediaPlayer.setVolume(1.0f, 1.0f);
                             break;
                         case ACTION_VOICE_SILENCE:
                             mMediaPlayer.setVolume(0, 0);
                             break;

                     }
                 }
             }, intentFilter);
         }

         @Override
         public void onDestroy() {
             unregisterReceiver(mvideoParamsControlReceiver);
             super.onDestroy();
         }

         @Override
         public void onVisibilityChanged(boolean visible) {
             super.onVisibilityChanged(visible);
             if (visible) {
                 mMediaPlayer.start();
             } else {
                 mMediaPlayer.pause();
             }
         }

         @Override
         public void onSurfaceCreated(SurfaceHolder holder) {
             System.out.println("onSurfaceCreated");
             super.onSurfaceCreated(holder);
             mMediaPlayer = new MediaPlayer();

             try {
                 System.out.println("Video path" + videoPath);
                 mMediaPlayer.setDataSource("/storage/emulated/0/DCIM/Camera/VID_20170521_150414.mp4");
                 mMediaPlayer.setLooping(true);
                 mMediaPlayer.setVolume(0, 0);
                 mMediaPlayer.prepare();
                 mMediaPlayer.start();
                 mMediaPlayer.setSurface(holder.getSurface());

             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

         @Override
         public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
             super.onSurfaceChanged(holder, format, width, height);
         }

         @Override
         public void onSurfaceDestroyed(SurfaceHolder holder) {
             super.onSurfaceDestroyed(holder);
             mMediaPlayer.release();
             mMediaPlayer = null;
         }
     }
}
