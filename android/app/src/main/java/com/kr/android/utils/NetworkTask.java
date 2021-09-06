package com.kr.android.utils;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.kr.android.com.commonConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



/**
*  http 통신
* @author LCY
* @version 1.0.0
* @since 2020-07-07 오전 11:21
**/
public class NetworkTask extends AsyncTask<Void, Void, String>  implements commonConstants {

    private String url;
    private ContentValues values;
    public ArrayList<Map<String,Object>> result;

    public NetworkTask(String url, ContentValues values) {
        if( StringUtils.noNull(url).equals("") ){
            url = "/comRouter";
        };

        this.url = url;
        this.values = values;
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
        String result = null; // 요청 결과를 저장할 변수.

        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

        result = requestHttpURLConnection.request(SERVER_IP + url, StringUtils.setUserInfo( values ) ); // 해당 URL로 부터 결과물을 얻어온다.
        return result;
    };
    /*
     * Method : onPostExecute
     * doInBackground() 메소드 작업이 끝나면 그 결과값을 onPostExecute()로 리턴하면서 실행
     * */
    @Override
    public void onPostExecute(String str) {

        // 결과 JSON 파싱
        super.onPostExecute(str);
        JSONArray jarray = null;
        JSONObject jObject = null;

        ArrayList<HashMap<String,Object>> resultsList = new ArrayList<HashMap<String,Object>>();
        if(str != null){
            try {
                jarray = new JSONArray(str);

                if (jarray != null) {
                    for (int i = 0; i < jarray.length(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();

                        try {
                            JSONObject c = (JSONObject) jarray.get(i);
                            //Fill map
                            Iterator iter = c.keys();
                            while(iter.hasNext())   {
                                String currentKey = (String) iter.next();
                                map.put(currentKey, c.getString(currentKey));
                            }
                            resultsList.add(map);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        };
                    };
                };
            } catch (JSONException e) {
                e.printStackTrace();
            };
        };

        postExt( resultsList );
    };

    /* 호출 후 실행*/
    public void postExt( ArrayList<HashMap<String,Object>> resultsList  ){
    };
    /* 호출 전 실행*/
    public void preExt(){
    };
}
