package com.col.commo.music_palyer_fuck;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public ImageView hello,stop,play,previous,next,item;
    public static SeekBar sb = null;
    private TextView title,artist;
    int playing = 1;
    String Strtitle , Strartist ,Strutl;
    int listPosition ,id ;
    public int duration;
    public static final String MUSIC_DURATION = "MUSIC_DURATION";

    final Mp3Info mp3info = new Mp3Info();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();

        initmusic(mp3info);

        item.setOnClickListener(this);

        play.setOnClickListener(this);

        stop.setOnClickListener(this);

        previous.setOnClickListener(this);

        next.setOnClickListener(this);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // fromUser判断是用户改变的滑块的值
                if(fromUser==true){
                    duration = progress;
                    Intent intent = new Intent();
                    intent.putExtra("MSG", AppConstant.PlayerMsg.PROGRESS_CHANGE);
                    intent.putExtra("seek",progress);
//                    Toast.makeText(MainActivity.this,"playing=2",Toast.LENGTH_LONG).show();
                    intent.setClass(MainActivity.this, PlayerService.class);
                    startService(intent);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

    }


    public void initmusic(Mp3Info mp3info){
        Strtitle = mp3info.getMp3(MainActivity.this).get(0).getTitle();
        Strartist = mp3info.getMp3(MainActivity.this).get(0).getArtist();
        Strutl = mp3info.getMp3(MainActivity.this).get(0).getUrl();
        id = mp3info.getMp3(MainActivity.this).get(0).getAlbum_id();
        sb.setMax((int) mp3info.getMp3(MainActivity.this).get(0).getDuration());
        getImage(id);
        listPosition = 0;
        title.setText(Strtitle);
        artist.setText(Strartist);
    }

    private void initview(){
        hello = (ImageView) findViewById(R.id.imageView);
        play = (ImageView) findViewById(R.id.play_view);
        stop = (ImageView) findViewById(R.id.stop_view);
        previous = (ImageView) findViewById(R.id.pervious_view);
        next = (ImageView) findViewById(R.id.next_view);
        item = (ImageView) findViewById(R.id.item_view);
        title = (TextView) findViewById(R.id.music_title);
        artist = (TextView) findViewById(R.id.music_artist);
        sb = (SeekBar) findViewById(R.id.seekBar);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.item_view: {
                if(playing == 2){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,Item.class);
                    intent.putExtra("isplaying",playing);
                    startActivityForResult(intent,1);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,Item.class);
                    startActivityForResult(intent,1);
                }
                break;
            }
            case R.id.play_view:{
                if (playing == 1) {
                    play.setImageResource(R.mipmap.pause);
//                    DrawSurfaceView surfaceView = new DrawSurfaceView(MainActivity.this);

                    Intent intent = new Intent();
                    intent.putExtra("url", Strutl);
                    intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
//                    Toast.makeText(MainActivity.this,"playing=1",Toast.LENGTH_LONG).show();
                    intent.setClass(MainActivity.this, PlayerService.class);
                    startService(intent);
                    playing = 2;
                }else if(playing == 2){
                    play.setImageResource(R.mipmap.play);

                    Intent intent = new Intent();
                    intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
//                    Toast.makeText(MainActivity.this,"playing=2",Toast.LENGTH_LONG).show();
                    intent.setClass(MainActivity.this, PlayerService.class);
                    startService(intent);
                    playing = 3;
                }else if (playing == 3){
                    play.setImageResource(R.mipmap.pause);

                    Intent intent = new Intent();
                    intent.putExtra("url", Strutl);
                    intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
//                    Toast.makeText(MainActivity.this,"playing=3",Toast.LENGTH_LONG).show();
                    intent.setClass(MainActivity.this, PlayerService.class);
                    startService(intent);
                    playing =2;
                }
                break;
            }
            case R.id.stop_view:{
                play.setImageResource(R.mipmap.play);
                sb.setProgress(0);

                Intent intent = new Intent();
                intent.putExtra("MSG", AppConstant.PlayerMsg.STOP_MSG);
                intent.setClass(MainActivity.this, PlayerService.class);
                startService(intent);
                playing = 1;
                break;
            }
            case R.id.pervious_view:{
                play.setImageResource(R.mipmap.pause);
                sb.setProgress(0);
                listPosition = listPosition - 1;

                if (listPosition < 0 ){
                    Toast.makeText(MainActivity.this,"列表循环",Toast.LENGTH_SHORT).show();
                    listPosition = mp3info.getMp3(MainActivity.this).size() - 1;
                }

                upmusic(listPosition);

                Intent intent = new Intent();
                intent.putExtra("url", Strutl);
                intent.putExtra("MSG", AppConstant.PlayerMsg.PREVIOUS_MSG);
                intent.setClass(MainActivity.this, PlayerService.class);
                startService(intent);
                break;
            }
            case R.id.next_view:{
                play.setImageResource(R.mipmap.pause);
                sb.setProgress(0);
                listPosition = listPosition + 1;

                if (listPosition > mp3info.getMp3(MainActivity.this).size() - 1){
                    Toast.makeText(MainActivity.this,"列表循环",Toast.LENGTH_SHORT).show();
                    listPosition = 0;
                }

                upmusic(listPosition);

                Intent intent = new Intent();
                intent.putExtra("url", Strutl);
                intent.putExtra("MSG", AppConstant.PlayerMsg.NEXT_MSG);
                intent.setClass(MainActivity.this, PlayerService.class);
                startService(intent);
                break;
            }
        }

    }

    private void upmusic(int listPosition){
        Mp3 getmp3 = mp3info.getMp3(MainActivity.this).get(listPosition);

        Strtitle = getmp3.getTitle();
        Strartist = getmp3.getArtist();
        Strutl = getmp3.getUrl();
        id = getmp3.getAlbum_id();
        sb.setMax((int) getmp3.getDuration());
        getImage(id);

        title.setText(Strtitle);
        artist.setText(Strartist);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 10){

            Strtitle = data.getStringExtra("title");
            Strartist = data.getStringExtra("artist");
            Strutl = data.getStringExtra("url");
            listPosition = data.getIntExtra("listPosition",0);
            id = data.getIntExtra("album_id",0);
            getImage(id);
            sb.setProgress(0);

            title.setText(Strtitle);
            artist.setText(Strartist);
        }
    }

    private  void getImage(int id){
        int album_id = id ;
        String albumArt = mp3info.getAlbumAtr(MainActivity.this,album_id);
        Bitmap bm = null;
        if (albumArt == null){
            hello.setBackgroundResource(R.mipmap.music_player11);
        } else {
            bm = BitmapFactory.decodeFile(albumArt);
            BitmapDrawable bmpDraw = new BitmapDrawable(bm);
            hello.setImageDrawable(bmpDraw);
        }
    }


}
