package com.kr.android.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;

import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kr.android.LoadingActivity;
import com.kr.android.MainActivity;
import com.kr.android.R;
import com.kr.android.com.defaultActivity;

import com.kr.android.utils.StringUtils;
import com.m3.sdk.scannerlib.Barcode;
import com.m3.sdk.scannerlib.BarcodeListener;
import com.m3.sdk.scannerlib.BarcodeManager;
import com.m3.sdk.scannerlib.Barcode.Symbology;
/**
* 웹뷰 액티비티
* @author LCY
* @version 1.0.0
* @since 2021-02-24
**/
public class webViewActivity extends defaultActivity {

    Vibrator vibrator = null; // 진동
    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅

    private Barcode mBarcode = null;
    private BarcodeListener mListener = null;
    private BarcodeManager mManager = null;
    private Symbology mSymbology = null;

    SoundPool soundPool;
    AudioManager mAudioManager;
    private int idOK, idNG;
    private Context mContext ;

    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        vibrator =  (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // 진동

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        String p_user_id = StringUtils.getUserId();
        String p_user_nm = StringUtils.getUserName();

        String web_path = SERVER_IP + "/m/main?USER_ID="+p_user_id+"&USER_NAME="+p_user_nm;
        mContext = getApplicationContext();

        // 웹뷰 시작
        mWebView = (WebView) findViewById(R.id.webView);

        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스크립트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부

        mWebView.addJavascriptInterface(new webViewInterface(this),"Android");
        mWebView.setWebViewClient(new WebViewClient_sub());
        mWebView.setWebChromeClient(new WebChromeClient_sub());

        mWebView.loadUrl( web_path ); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작


        mBarcode = new Barcode(this);
        mManager = new BarcodeManager(this);
        mSymbology = mBarcode.getSymbologyInstance();
        mBarcode.setScanner(true);
        mListener = new BarcodeListener() {

            @Override
            public void onBarcode(String strBarcode) {
                Log.i("ScannerTest","result="+strBarcode);
            };
            @Override
            public void onBarcode(String barcode, String codeType) {
                vibrator.vibrate(500); // 0.5초간 진동

                //String script= "javascript:document.getElementById('lbl_id').innerHTML ='" + barcode + "'; appScan();";
                String script= "javascript:appScan('\"+barcode+\"');";


                mWebView.evaluateJavascript(script, new ValueCallback<String>()
                {
                    @Override
                    public void onReceiveValue(String value)
                    {
                        if( value.equals("true") ){
                            soundPool.play(idOK, 1f, 1f, 1, 0, 1f);
                        }else{
                            soundPool.play(idNG, 1f, 1f, 1, 0, 1f);
                        }

                    }
                });
            };

            @Override
            public void onGetSymbology(int i, int i1) {

            }
        };
        mManager.addListener(mListener);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
            mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


        }
        else {
            soundPool = new SoundPool(5, AudioManager.STREAM_NOTIFICATION, 0);
            mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }

        idNG = soundPool.load(mContext, R.raw.ng, 1);
        idOK = soundPool.load(mContext, R.raw.ok, 1);



        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                    @Override
                                                    public void onRefresh() {
                                                        mWebView.loadUrl( web_path );
                                                        swipeRefreshLayout.setRefreshing(false);
                                                    }
                                                }
        );
    };
    public class webViewInterface{
        private Context mContext;
        public webViewInterface(Context mContext) {
            this.mContext = mContext;
        };
        @JavascriptInterface
        public void showToast(String msg){
            Toast.makeText(webViewActivity.this, msg, Toast.LENGTH_SHORT).show();
        };
    };

    private class WebViewClient_sub extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    public class WebChromeClient_sub extends WebChromeClient {
        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
        {
            new AlertDialog.Builder(webViewActivity.this)
                    .setTitle("")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    result.confirm();
                                }
                            })
                    .setCancelable(false)
                    .create()
                    .show();

            return true;
        }
        //웹뷰에 Confirm창에 url을 제거한다.
        @Override
        public boolean onJsConfirm(WebView view, String url,
                                   String message, final JsResult result) {
            new AlertDialog.Builder(webViewActivity.this)
                    .setTitle("")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    result.cancel();
                                }
                            })
                    .create()
                    .show();
            return true;
        }
    }


    @Override
    protected void onDestroy() {
        mManager.removeListener(mListener);
        mManager.dismiss();
        super.onDestroy();
    };


    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // 웹뷰 History상 이전 페이지가 있을 경우
            if(mWebView.canGoBack()){
                mWebView.goBack(); // 뒤로가기
                return true;
            }
            // 없을 경우 앱 종료 전 Toast로 물어보기
            else{
                // 토스트메세지 출력
                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    backKeyPressedTime = System.currentTimeMillis();
                    toast = Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                // 토스트메세지가 있는 상태에서 뒤로가기를 한번 더 누르면 앱 종료
                else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                    finish();
                    toast.cancel();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

};

