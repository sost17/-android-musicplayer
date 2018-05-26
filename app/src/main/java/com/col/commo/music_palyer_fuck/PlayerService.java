package com.col.commo.music_palyer_fuck;

/**
 * Created by commo on 2017/5/18.
 */

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;

@SuppressLint("NewApi")
public class PlayerService extends Service {
    public static MediaPlayer mediaPlayer =  new MediaPlayer();       //媒体播放器对象
    private String path;                        //音乐文件路径
    boolean isPause = false;
    private int current = 0;
    int pos;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        path = intent.getStringExtra("url");
        current = intent.getIntExtra("seek", -1);
        int msg = intent.getIntExtra("MSG", 0);
        if(msg == AppConstant.PlayerMsg.PLAY_MSG) {
            play(0);
        } else if(msg == AppConstant.PlayerMsg.PAUSE_MSG) {
            pause();
        } else if(msg == AppConstant.PlayerMsg.STOP_MSG) {
            stop();
        } else if (msg == AppConstant.PlayerMsg.CONTINUE_MSG) {
            resume();
        } else if (msg == AppConstant.PlayerMsg.NEXT_MSG) {
            next();
        } else if (msg == AppConstant.PlayerMsg.PREVIOUS_MSG) {
            previous();
        } else if (msg == AppConstant.PlayerMsg.PROGRESS_CHANGE){
            sb(current);
        }
        return super.onStartCommand(intent, flags, startId);
    }



    /**
     * 播放音乐
     * @param position
     */
    private void play(final int position) {
        try {
            mediaPlayer.reset();//把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();  //进行缓冲
            mediaPlayer.setOnPreparedListener(new PreparedListener(position));//注册一个监听器
            MainActivity.sb.setMax(PlayerService.mediaPlayer.getDuration());
            new Thread(){
                public void run() {
                    int CurrentPosition = 0;
                    int total = mediaPlayer.getDuration();
                    while (mediaPlayer != null && CurrentPosition < total) {
                        try {
                            Thread.sleep(1000);
                            if (mediaPlayer != null) {
                                CurrentPosition = mediaPlayer.getCurrentPosition();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        MainActivity.sb.setProgress(CurrentPosition);
                    }

                }
            }.start();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    /**
     * 停止音乐
     */
    private void stop(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            MainActivity.sb.setMax(0);
        }
    }

    private void resume() {
        if (isPause) {
            mediaPlayer.start();

            isPause = false;
        }
    }

    private void next() {
        play(0);
    }

    private void previous() {
        play(0);
    }


    @Override
    public void onDestroy() {
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    public void sb(int current){
        mediaPlayer.seekTo(current);
    }

    /**
     *
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     *
     */
    private final class PreparedListener implements OnPreparedListener {
        private int positon;

        public PreparedListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();    //开始播放
            if(positon > 0) {    //如果音乐不是从头播放
                mediaPlayer.seekTo(positon);
            }

        }
    }
}
