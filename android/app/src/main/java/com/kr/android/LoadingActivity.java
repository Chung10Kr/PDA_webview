package com.kr.android;


import android.os.Bundle;
import android.os.Handler;

import com.kr.android.com.defaultActivity;
/**
 * 앱 실행시 로딩 화면
 * @author LCY
 * @version 1.0.0
 * @since 2020-07-07 오전 11:21
 **/
public class LoadingActivity extends defaultActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 700);
    }


};