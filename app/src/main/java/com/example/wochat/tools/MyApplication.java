package com.example.wochat.tools;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by 邹永鹏 on 2018/5/3.
 * 全局获取Context
 */

public class MyApplication extends Application {

    private static Context WoIMContext;

    /*重写父类onCreate()
    * 调用getApplicationContext()得到应用级别的Context*/
    @Override
    public void onCreate() {
        WoIMContext=getApplicationContext();
        LitePalApplication.initialize(WoIMContext);//配置LitePal
    }

    public static Context getWoIMContext(){
        return WoIMContext;
    }
}
