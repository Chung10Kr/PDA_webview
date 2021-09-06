package com.kr.android;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kr.android.activity.webViewActivity;
import com.kr.android.com.MemberVo;
import com.kr.android.utils.ApkDownTask;
import com.kr.android.utils.BackPressCloseHandler;
import com.kr.android.utils.NetworkTask;
import com.kr.android.com.defaultActivity;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kr.android.utils.StringUtils;

/**
 *  로그인시
 * @author LCY
 * @version 1.0.0
 * @since 2020-07-07 오전 11:21
 **/
public class MainActivity extends defaultActivity{

    private static final String TAG = "MainActivity";
    private EditText userId;
    private EditText userPwd;
    private TextView versionV;

    private SharedPreferences appData;
    private BackPressCloseHandler backPressCloseHandler;
    private static final int MY_PERMISSION_STORAGE = 1111;
    private static boolean apkCheckYn = false;
    Vibrator vibrator = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        checkPermission();
        //상태바 없애기

        backPressCloseHandler = new BackPressCloseHandler(this);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        versionV = (TextView) findViewById(R.id.versionv);
        versionV.setText( "V."+APK_VERSION);
        // SharedPreferences 수정을 위한 Editor 객체를 얻어옵니다.
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        remove();

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setVisibility(View.INVISIBLE);
        String url = "/chkApk";
        String version = APK_VERSION;
        ContentValues values = new ContentValues();
        values.put("APK_V" , version );
        NetworkTask apkTask = new NetworkTask(url , values){
            @Override
            public void postExt(ArrayList<HashMap<String, Object>> resultsList) {
                String update_chk = resultsList.get(0).get("result").toString();
                String new_version = resultsList.get(0).get("new_version").toString();
                String apkPath = SERVER_IP + "/files/apk/" + new_version+"/app-debug.apk";

                if(update_chk.equals("need")){
                    ApkDownTask apkTask = new ApkDownTask(apkPath , null){
                        @Override
                        public void postExt(String str , String localPath) {
                            /*
                            try{
                                File apkFile = new File(localPath);
                                Uri apkUri = Uri.fromFile(apkFile);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction(android.content.Intent.ACTION_VIEW);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setDataAndType( Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                                startActivity(intent);
                            }catch (Exception e){
                                Log.d("fileDown Error" , e.getMessage());
                            };
                            */

                            Toast.makeText(MainActivity.this, "앱을 업데이트 해 주세요.", Toast.LENGTH_SHORT).show();
                            vibrator.vibrate(500);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( SERVER_IP + "/apkQrDown"));
                            startActivity(intent);
                        };
                    };
                    apkTask.execute();
                }else{
                    apkCheckYn = true;
                    loginButton.setVisibility(View.VISIBLE);
                }

            }
        };
        apkTask.execute();


        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( apkCheckYn ){
                  onLogin(view);
                }else{
                    Toast.makeText(getApplicationContext(),"업데이트 진행중 입니다.", Toast.LENGTH_SHORT).show();
                };
            };
        });
    };

    // login button click event
    public void onLogin(View v){
        userId =  (EditText) findViewById(R.id.userId);
        userPwd = (EditText) findViewById(R.id.userPwd);

        String url =  "/m/login";

        ContentValues params = new ContentValues();
        params.put("USER_ID" , userId.getText().toString());
        params.put("USER_PWD",userPwd.getText().toString());
        params.put("TOKEN" ,  FirebaseInstanceId.getInstance().getToken() );

        NetworkTask netTask = new NetworkTask(url , params ){
            @Override
            public void postExt(ArrayList<HashMap<String, Object>> resultsList) {
                LoginAction(resultsList);
            }
        };
        netTask.execute();
    };
    //FCM 푸시 알림 셋팅
    private void sendRegistrationToServer(String token, String id) {

        String url =  "/fcmMsg";
        ContentValues params = new ContentValues();
        params.put("TOKEN" , token);
        params.put("USER_ID",id);

        NetworkTask fcmTask = new NetworkTask(url , params ){
            @Override
            public void postExt(ArrayList<HashMap<String, Object>> resultsList) {
                super.postExt(resultsList);
            }
        };
        fcmTask.execute();
    };
    //
    /**
     * 로그인 액션
     * @author kwonym
     * @version 1.0.0
     * @since 2020-07-30 오후 2:45
     **/
    public void LoginAction(ArrayList<HashMap<String, Object>> resultsList){
        String USER_ID = "";
        String USER_NM = "";

        // 로그인 에러 수정 By kwonym 07.30
        if( resultsList.size() != 0 && (StringUtils.noNull( resultsList.get(0).get("CHECK_PWD"))).equals("1") ){
            USER_ID = StringUtils.noNull(  resultsList.get(0).get("USER_ID") );
            USER_NM = StringUtils.noNull(  resultsList.get(0).get("USER_NAME") );
            save(USER_ID , USER_NM);

            // FCM 메시지 셋팅
            String token = FirebaseInstanceId.getInstance().getToken();
            sendRegistrationToServer(token , USER_ID);

            MemberVo memberVo = MemberVo.getInstance();
            memberVo.setUSER_ID(USER_ID);
            memberVo.setUSER_NAME(USER_NM);

            Intent intent = new Intent(this, webViewActivity.class);

            this.startActivity(intent);
        }else{
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            vibrator.vibrate(500); // 0.5초간 진동
            imm.hideSoftInputFromWindow(userPwd.getWindowToken(), 0); // 로그인 에러시 키보드 때문에 메시지 안보여서 내려줌
            Toast.makeText(getApplicationContext(),"Login Fail",Toast.LENGTH_LONG).show();
        };
    };
    // 설정값을 저장
    private void save(String USER_ID , String USER_NM ) {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putString("USER_ID",USER_ID);
        editor.putString("USER_NM",USER_NM);

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 삭제
    private void remove(){
        SharedPreferences.Editor editor = appData.edit();
        //기존의 ID , NM Remove
        editor.remove("USER_ID");
        editor.remove("USER_NM");
        editor.apply();
        /*
        불러올때
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        commandMap.put("gUserId", appData.getString("USER_ID", ""));//아이디
        commandMap.put("gUserNm", appData.getString("USER_NM", ""));//이름
        * */
    };




    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
            // ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);

            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            };
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(MainActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    };

    //뒤로가기 이벤트
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle( APP_NAME + " 종료 확인")
                .setMessage("종료 하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }})
                .show();
    };

};