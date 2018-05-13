package com.example.wochat.singleClass;

import android.text.LoginFilter;
import android.util.Log;

import com.example.wochat.activity.LoginActivity;
import com.example.wochat.activity.RegisterActivity;
import com.example.wochat.tools.SmackConnectionListener;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;

/**
 * Created by 邹永鹏 on 2018/5/4.
 */

public class SmackManager {
    /*服务器IP地址以为，即本机的IP
    * 1、如果电脑连接锐捷客户端，地址为116.57.53.134
    * 2、如果电脑连接校园WIFI，地址为192.168.155.1
    * 3、因为服务器连接的是内网，真机调试时使用数据连接的真机不能连接服务器，需要通过电脑热点连接
    * */
    private static final String HOST="116.57.53.134";

    /*客户端到服务器端口5222
    * 用于客户端以标准方式连接到服务器。 此端口使用纯文本方式建立连接*/
    private static final int PORT=5222;

    /*服务器名称*/
    public static final String SERVER_NAME = "116.57.53.134";

    /*volatile关键字来保证可见性、有序性，但没办法保证对变量的操作的原子性
    * 1、它会强制修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值
    *    即新值对其他线程来说是立即可见的
    * 2、禁止进行指令重排序。*/
    private static volatile SmackManager sSmackManager;

    private XMPPTCPConnection mConnection;

    private SmackManager(){
        mConnection=getConnection();
    }

    /*获取sSmackManager实例
    * synchronized修饰一个代码块：
    * 当两个并发线程(thread1和thread2)访问同一个对象(syncThread)中的synchronized代码块时，在同一时刻只能有一个线程得到执行，
    * 另一个线程受阻塞，必须等待当前线程执行完这个代码块以后才能执行该代码块。
    * 参考资料：https://blog.csdn.net/luoweifu/article/details/46613015*/
//    public static SmackManager getSmackManager(){
///*        if (sSmackManager==null){
//            synchronized (SmackManager.class) {
//                if (sSmackManager == null) {
//                    sSmackManager = new SmackManager();
//                }
//            }
//        }
//        return sSmackManager;*/
//        synchronized (SmackManager.class){
//            if (sSmackManager == null) {
//                sSmackManager = new SmackManager();
//            }
//            return sSmackManager;
//        }
//    }
    public  synchronized static SmackManager getSmackManager(){
        if (sSmackManager == null) {
            sSmackManager = new SmackManager();
        }
        return sSmackManager;
    }

    /*连接服务器*/
    private XMPPTCPConnection getConnection(){
        try {
//            SmackConfiguration.setDefaultPacketReplyTimeout(10000);//作用未知
            XMPPTCPConnectionConfiguration configuration=XMPPTCPConnectionConfiguration.builder()
                    //不开启安全模式
                    .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
                    .setServiceName(SERVER_NAME)//服务器名称
                    .setHost(HOST)//服务器IP地址
                    .setPort(PORT)//服务器端口
                    .setCompressionEnabled(false)//不开启压缩
                    .setDebuggerEnabled(true)//开启调试模式
                    .build();

            XMPPTCPConnection connection=new XMPPTCPConnection(configuration);
            /*重连*/
            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
            reconnectionManager.enableAutomaticReconnection();//允许自动重连
            reconnectionManager.setFixedDelay(2);//重连间隔时间
            connection.addConnectionListener(new SmackConnectionListener());//连接监听
            connection.connect();
            return connection;
        }catch (Exception e) {
            Log.d("XMPPTCPConnection","getConnection error");
            return  null;
        }
    }

    /*判断是否连接服务器成功
    * 1、第一次连接为null则重新新建
    * 2、第二次连接为null就返回false
    * 3、第三次连接不为null但未连接则重新调用getConnection*/
    private boolean isConnected() {

        if (mConnection == null) {
            sSmackManager = new SmackManager();
        }
        if(mConnection == null) {
            return false;
        }
        if (!mConnection.isConnected()) {
            try {
                mConnection.connect();
                return true;
            } catch (SmackException | IOException | XMPPException e) {
                return false;
            }
        }
        return true;
    }

    /*登录操作
    * 1、先判断是否连接服务器，未连接则抛出错误
    * 2、如果连接则调用AbstractXMPPConnection的login(name,password)方法
    * 3、登录成功则返回1，失败则返回0*/
    public int login(String username,String userpass){
        Log.d("login","logining");
        try{
            if (!isConnected()){
//                throw new Exception("服务器出现异常");
                return LoginActivity.LOGIN_OFFLINE;
            }
            mConnection.login(username,userpass);
            return LoginActivity.LOGIN_SUCCESS;
        }catch (Exception e){
            Log.d("error","SmackManager login error!");
            return LoginActivity.LOGIN_FAILURE;
        }
    }

    public int register(String userName,String userPass){
        Log.d("register","register()");
        try{
            if (!isConnected()){
//                throw new Exception("服务器出现异常");
                return RegisterActivity.REGISTER_OFFLINE;
            }
            AccountManager.getInstance(mConnection).createAccount(userName, userPass);
            return RegisterActivity.REGISTER_SUCCESS;
        }catch (Exception e){
            Log.d("error","SmackManager login error!");
            return RegisterActivity.REGISTER_FAILURE;
        }
    }
}
