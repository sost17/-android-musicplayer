package com.col.commo.music_palyer_fuck;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by commo on 2017/5/21.
 */

public final class Mp3Info  {

    public static List<Mp3> getMp3(Context context){

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        List<Mp3> mp3s = new ArrayList<Mp3>();

        for( int i = 0 ; i < cursor.getCount() ; i ++ ){
            cursor.moveToNext();
            Mp3 mp3 = new Mp3();
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            int album_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));


            if ( isMusic != 0){
                mp3.setTitle(title);
                mp3.setArtist(artist);
                mp3.setUrl(url);
                mp3.setAlbum_id(album_id);
                mp3.setDuration(duration);

                mp3s.add(mp3);

            }
        }

        return mp3s;

    }


    public static List<HashMap<String,Object>> getMusicMaps(List<Mp3> mp3s){
        List<HashMap<String,Object>> mp3list = new ArrayList<HashMap<String, Object>>();

        for (Iterator iterator = mp3s.iterator(); iterator.hasNext();){
            Mp3 mp3 = (Mp3) iterator.next();
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("img",R.drawable.music_128px);
            map.put("title",mp3.getTitle());
            map.put("artist",mp3.getArtist());
            map.put("url",mp3.getUrl());
            map.put("duration",formatTime(mp3.getDuration()));
            mp3list.add(map);
        }

        return mp3list;
    }

    public String getAlbumAtr(Context context,int album_id){
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums+ "/" + Integer.toString(album_id)),projection,null,null,null);
        String ablum_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0){
            cur.moveToNext();
            ablum_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return ablum_art;
    }

    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }
}
