package com.kr.android.com;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import com.kr.android.MainActivity;
import com.kr.android.activity.webViewActivity;
import com.kr.android.utils.StringUtils;


public class defaultActivity extends AppCompatActivity  implements commonConstants {

    private static MemberVo memberVo = MemberVo.getInstance();

    /**
    * 화면 터치시 키보드 내리기
    * @author LCY
    * @version 1.0.0
    * @since 2020-08-26 오전 11:02
    **/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try{
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            Log.d("ttt" , "TTT");
        };
        return true;
    };


    /**
    * 뒤로가기 버튼 누를시 메뉴화면으로
    * @author LCY
    * @version 1.0.0
    * @since 2020-08-26 오전 11:03
    **/
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String activityNm = null;
        if( StringUtils.noNull( memberVo.getUSER_ID() ).equals("")){
            activityNm =  "com.kr.android.MainActivity";
        }else{
            activityNm =  "com.kr.android.activity.webViewActivity";
        };
        Class<?> classNm = MainActivity.class;
        try {
            classNm = Class.forName( activityNm );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



        Intent intent = new Intent(this, classNm);
        startActivity(intent);

/*
        Intent intent2 = new Intent(this, webViewActivity.class);
        this.startActivity(intent2);

 */
    };





};

