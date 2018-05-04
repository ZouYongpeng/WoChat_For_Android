package com.example.wochat.tools;

import android.util.Log;

import com.example.wochat.activity.LoginActivity;

/**
 * Created by 邹永鹏 on 2018/5/1.
 * 1、判断是否记住密码
 * 2、如果记住密码就通过SharedPreferences获取用户名和密码
 */

public class LoginTool {

    private static final String TAG="login";

    public static final String FORMER_LOGIN="key_former_login";
    public static final String REMEMBER_PASS="key_remember_pass";
    public static final String AUTO_LOGIN="key_auto_login";

    /*记住上一个登录用户的账号*/
    public static void setFormerLogin(String userName){
        Log.d(TAG,"setFormerLogin()"+userName);
        SharedPreferencesTool.putString(FORMER_LOGIN,userName);
    }

    /*获取上一个登录用户的账号*/
    public static String getFormerLogin(){
        return SharedPreferencesTool.getString(FORMER_LOGIN);
    }

    /*判断某一个用户是否记住密码*/
    public static boolean isRememberPass(String userName){
        return SharedPreferencesTool.getBoolean(REMEMBER_PASS+userName,false);
    }

    /*为当前用户记住密码*/
    public static void setRememberPass(String userName,boolean isRemember){
        Log.d(TAG,"setRememberPass > "+userName+" - remember? "+isRemember);
        SharedPreferencesTool.putBoolean(REMEMBER_PASS+userName,isRemember);
    }

    /*判断是否自动登录*/
    public static boolean isAutoLogin(String userName){
        return SharedPreferencesTool.getBoolean(AUTO_LOGIN+userName,false);
    }

    /*存储是否自动登录*/
    public static void setAutoLogin(String userName,boolean isAuto){
        Log.d(TAG,"setAutoLogin > "+userName+" - auto? "+isAuto);
        SharedPreferencesTool.putBoolean(AUTO_LOGIN+userName,isAuto);
    }



}
