package com.col.commo.music_palyer_fuck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by commo on 2017/5/16.
 */
public class Item extends Activity  {

    private ListView list_mp3;
    private SimpleAdapter adapter;
    private List<HashMap<String, Object>> dataList;
    private ImageView music;
    private TextView title,atrist,url;
    String titles , artist,path;
    int playing = -1,id;

    final Mp3Info mp3info = new Mp3Info();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        initview();

        S_adapter();

        list_mp3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent getintent = getIntent();
                playing = getintent.getIntExtra("isplaying",0);

                getMp3info(position);

                IntentMain(position);

                isplayings(playing,position);

            }

        });

    }

    private void initview(){
        list_mp3 = (ListView) findViewById(R.id.list);
        music = (ImageView) findViewById(R.id.cover);
        title = (TextView) findViewById(R.id.title);
        atrist = (TextView) findViewById(R.id.artist);
        url = (TextView) findViewById(R.id.path);
    }

    private void  S_adapter(){
        dataList = mp3info.getMusicMaps(mp3info.getMp3(Item.this));

        adapter = new SimpleAdapter(this,dataList,R.layout.item,new String[]{"img","title","artist","url"},
                new int[]{R.id.cover,R.id.title,R.id.artist,R.id.path});
        list_mp3 = (ListView) findViewById(R.id.list);
        list_mp3.setAdapter(adapter);
    }

    private  void  IntentMain( int position){
        Intent intents = new Intent();
        intents.setClass(Item.this,MainActivity.class);
        intents.putExtra("listPosition",position);
        intents.putExtra("url", path);
        intents.putExtra("title",titles);
        intents.putExtra("artist",artist);
        intents.putExtra("album_id",id);
        setResult(10,intents);
        finish();
    }

    private void getMp3info(int position){
        Mp3 getmp3 = mp3info.getMp3(Item.this).get(position);
        titles = getmp3.getTitle();
        artist = getmp3.getArtist();
        path = getmp3.getUrl();
        id = getmp3.getAlbum_id();
    }

    private void isplayings(int playing,int position){
        if (playing == 2){
            Intent intent = new Intent();
            intent.putExtra("url", path);
            intent.putExtra("listPosition",position);
            intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
            intent.setClass(Item.this, PlayerService.class);
            startService(intent);       //启动服务
            Log.i("Log", "hello,activity");
        } else{
            Intent intent = new Intent();
            intent.putExtra("url", path);
            intent.putExtra("listPosition",position);
            intent.setClass(Item.this, PlayerService.class);
            startService(intent);       //启动服务
            Log.i("Log", "hello,activity");
        }
    }



}