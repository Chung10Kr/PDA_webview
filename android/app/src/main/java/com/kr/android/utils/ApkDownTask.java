package com.kr.android.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.kr.android.com.commonConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
*  APK 다운로드
* @author LCY
* @version 1.0.0
* @since 2020-07-07 오전 11:18
**/

public class ApkDownTask extends AsyncTask<Void, Void, String>  implements commonConstants {

    String  filePath = null ;
    String localPath = null;
    HashMap<String, Object> param;
    String savePath = Environment.getExternalStorageDirectory() + File.separator + "temp";


    public ApkDownTask(String filePath, HashMap<String, Object> param) {
        this.filePath = filePath;
        this.param = param;
    };

    /**
    * 백그라운드 작업을 실행하기 전에 실행
    * @author LCY
    * @version 1.0.0
    * @since 2020-07-07 오전 11:17
    **/

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        preExt();
    };

    /**
    * 실행할 동작 구현
    * @author LCY
    * @version 1.0.0
    * @since 2020-07-07 오전 11:16
    **/
    @Override
    public String doInBackground(Void... params) {
        File dir = new File(savePath);
        //상위 디렉토리가 존재하지 않을 경우 생성
        if (!dir.exists()) {
            dir.mkdirs();
        };
        localPath = savePath + "/" + "app-debug.apk";
            try{
                URL imgUrl = new URL(filePath);
                //서버와 접속하는 클라이언트 객체 생성
                HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
                int response = conn.getResponseCode();
                File file = new File(localPath);
                InputStream is = conn.getInputStream();
                OutputStream outStream = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf)) > 0) {
                    outStream.write(buf, 0, len);
                };
                outStream.close();
                is.close();
                conn.disconnect();

            }catch (Exception e){
                Log.d("APK DOWN ERR" , e.getMessage());
            }
        return "SUC";
    };

    /**
    * 메소드 작업이 끝나면 그 결과값을 onPostExecute()로 리턴하면서 실행
    * @author LCY
    * @version 1.0.0
    * @since 2020-07-07 오전 11:15
    **/
    @Override
    public void onPostExecute(String str) {
        postExt( str , localPath );
    };


    /**
    * onPostExecute 실행 클래스
    * @author LCY
    * @version 1.0.0
    * @since 2020-07-07 오전 11:16
    **/
    public void postExt(String str , String localPath) {}
    /**
    * onPreExecute 실행 클래스
    * @author LCY
    * @version 1.0.0
    * @since 2020-07-07 오전 11:16
    **/

    public void preExt() {}


}
