package com.kr.android.utils;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.kr.android.com.commonConstants;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
*  이미지 읽기
* @author LCY
* @version 1.0.0
* @since 2020-07-07 오전 11:18
**/

public class ImgReadTask extends AsyncTask<Void, Void, String>  implements commonConstants {

    private String filePath;
    private ContentValues values;
    Bitmap bmImg = null;
    public ImgReadTask(String filePath, ContentValues values) {
        this.filePath = filePath;
        this.values = values;
    };


    /**
     *  백그라운드 작업을 실행하기 전에 실행
     * @author LCY
     * @version 1.0.0
     * @since 2020-07-07 오전 11:18
     **/
    @Override
    public void onPreExecute() {
        super.onPreExecute();
        preExt();
    };

    /**
     * 동작 구현
     * @author LCY
     * @version 1.0.0
     * @since 2020-07-07 오전 11:18
     **/
    @Override
    public String doInBackground(Void... params) {

       try{
           URL myFileUrl = new URL(SERVER_IP + "/files/upload" + filePath  );
           HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
           conn.setDoInput(true);
           conn.connect();
           InputStream is = new BufferedInputStream(conn.getInputStream());
           is.mark(is.available());
           is.reset();
           bmImg = BitmapFactory.decodeStream(is);
       }catch (Exception e){
           Log.d("File Read" , e.getMessage());
       };
        return "SUC";
    };

    /**
     *  doInBackground() 메소드 작업이 끝나면 그 결과값을 onPostExecute()로 리턴하면서 실행
     * @author LCY
     * @version 1.0.0
     * @since 2020-07-07 오전 11:18
     **/
    @Override
    public void onPostExecute(String str) {
        postExt(bmImg);
    };



    /**
     *  onPostExecute 동작
     * @author LCY
     * @version 1.0.0
     * @since 2020-07-07 오전 11:18
     **/
    public void postExt(Bitmap bmImg) {}
    /**
     *  onPreExecute 동작
     * @author LCY
     * @version 1.0.0
     * @since 2020-07-07 오전 11:18
     **/
    public void preExt() {}


}
