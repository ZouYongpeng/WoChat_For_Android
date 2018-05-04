package com.example.wochat.singleClass;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager.NameNotFoundException;

import com.example.wochat.tools.MyApplication;

/**
 * Created by 邹永鹏 on 2018/5/1.
 * WoIM的一个单例类
 */

public class WoIMContext extends ContextWrapper {

    /*静态私有成员变量*/
    private static WoIMContext sWoIMInstance;

    /*私有构造函数*/
    private WoIMContext(Context context){
        super(context);
    }

    /*静态公有工厂方法，返回唯一实例*/
    public static WoIMContext getWoIMInstance(){
        if (sWoIMInstance==null){
            try {
                //通过MyApplication.getWoIMContext()获取全局Context
                sWoIMInstance = new WoIMContext(MyApplication.getWoIMContext());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sWoIMInstance;
    }

    /*context.createPackageContext:
    * 主要作用是:创建其它程序的Context,
    *           通过创建的这个Context，就可以访问该软件包的资源，甚至可以执行其它软件包的代码。
    * 参考资料：https://blog.csdn.net/wangbole/article/details/22876179
    * */
    public static void init(Context context){
        if (sWoIMInstance==null){
            try {
                sWoIMInstance = new WoIMContext(context.createPackageContext(context.getPackageName(),
                        Context.CONTEXT_INCLUDE_CODE));
            } catch (NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
