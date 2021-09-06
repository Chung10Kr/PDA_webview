package com.kr.android.com;

/**
 * android
 * Class: member
 * Created by LEE on 2020-08-11.
 * <p>
 * Description:
 */
public class MemberVo {
    //Instance
    private static MemberVo instance = null;

    private String USER_ID;
    private String USER_NAME;

    //private construct
    private MemberVo() {
    };
    public static MemberVo getInstance() {
        return LazyHolder.INSTANCE;
    };
    private static class LazyHolder {
        public static final MemberVo INSTANCE = new MemberVo();
    };
    public String getUSER_ID() {
        return USER_ID;
    };
    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    };
    public String getUSER_NAME() {
        return USER_NAME;
    };
    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
;    };
}