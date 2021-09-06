package com.kr.android.utils;

import android.os.AsyncTask;
import android.util.Log;
import com.kr.android.com.commonConstants;
import org.json.JSONObject;
import java.util.HashMap;

/**
 *  업로드 태스트
 * @author LCY
 * @version 1.0.0
 * @since 2020-07-07 오전 11:21
 **/
public class UploadTask extends AsyncTask<Void, Void, String>  implements commonConstants {

    String uploadUrl = "/uploadImg";
    HashMap<String, Object> param;
    HashMap<String, String> files;

    public UploadTask( HashMap<String, Object> param ,HashMap<String, String> files) {
        this.param = param;
        this.files = files;
    };

    /*
     * Method : onPreExecute
     * 백그라운드 작업을 실행하기 전에 실행
     * 로딩중 이미지 띄어 놓기 등 스레더 작업 이전에 수행할 동작 구현
     */
    @Override
    public void onPreExecute() {
        super.onPreExecute();
        preExt();
    };
    /*
     * Method : doInBackground
     * 실행할 동작 구현
     * */
    @Override
    public String doInBackground(Void... params) {

        JSONObject json = null;
        String returnStr = null;
        try {
            MultipartUpload multipartUpload = new MultipartUpload(SERVER_IP+uploadUrl, "UTF-8");
            json = multipartUpload.upload(param, files);
            returnStr = StringUtils.noNull(json.get("filepath"));
        } catch (Exception e) {
            Log.d("[multil error]" , e.getMessage());
        }

        return returnStr;
    };
    /*
     * Method : onPostExecute
     * doInBackground() 메소드 작업이 끝나면 그 결과값을 onPostExecute()로 리턴하면서 실행
     * */
    @Override
    public void onPostExecute(String str) {
        postExt( str );
    };




    public void postExt(String returnPath) {}
    public void preExt() {}


}
