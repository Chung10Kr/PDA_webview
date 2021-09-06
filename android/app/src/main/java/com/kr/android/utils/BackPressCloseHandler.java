package com.kr.android.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;
/**
 * android
 * Class: BackPressCloseHandler
 * Created by LEE on 2020-08-12.
 * <p>
 * Description:
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            //activity.moveTaskToBack(true);						// 태스크를 백그라운드로 이동
            activity.finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
            //android.os.Process.killProcess(android.os.Process.myPid());	// 앱 프로세스 종료


        }
    }
    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT); toast.show();
    };

};
