package com.example.wochat.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wochat.R;
import com.example.wochat.bean.User;
import com.example.wochat.tools.LoginTool;
import com.example.wochat.tools.ToastTool;
import com.example.wochat.ui.ClearEditText;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    /*用户名*/
    @BindView(R.id.login_user_name)
    protected ClearEditText mEditUserName;

    /*用户密码*/
    @BindView(R.id.login_user_password)
    protected ClearEditText mEditUserPass;

    /*是否记住密码*/
    @BindView(R.id.remember_password)
    protected AppCompatCheckBox mRememberPass;

    /*是否自动登录*/
    @BindView(R.id.auto_login)
    protected AppCompatCheckBox mAutoLogin;

    /*登录按钮*/
    @BindView(R.id.login_button)
    protected Button mLoginButton;

    /*注册按钮*/
    @BindView(R.id.goto_register)
    protected TextView mToRegister;

    public static final int LOGIN_SUCCESS=1;//登录成功
    public static final int LOGIN_FAILURE=0;//密码错误
    public static final int LOGIN_OFFLINE=2;//服务器连接失败

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        /*获取上次登录的用户名*/
        String formerLogin=LoginTool.getFormerLogin();
        mEditUserName.setText(formerLogin);

        initUserInfo(formerLogin);
        changeEditText();//输入框监听事件
    }

    private Handler mHandler=new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case LOGIN_SUCCESS://登录成功
                    Log.d("login","success");
//                    ToastTool.showToast(LoginActivity.this,getString(R.string.login_success));
                    loginSuccess();
                    break;
                case LOGIN_FAILURE://密码错误
                    Log.d("login","failure");
                    ToastTool.showToast(LoginActivity.this,getString(R.string.login_failure));
                    mLoginButton.setEnabled(true);//启用登录按钮
                    mLoginButton.setText(getString(R.string.login));
                    mEditUserPass.setText("");
                    break;
                case LOGIN_OFFLINE://离线状态
                    Log.d("login","OFFLINE");
                    ToastTool.showToast(LoginActivity.this,getString(R.string.login_offline));
                    //改变登录按钮外观
                    mLoginButton.setEnabled(true);//启用登录按钮
                    mLoginButton.setText(getString(R.string.login));
                    break;
            }
        }
    };

    /*初始化界面时，先在用户名输入栏显示上次登录成功的用户名
    * 如果用户名不为空，则通过SharedPreferencesTool查看上次登录是否记住密码
    * 如果记住密码，那么就通过tool获取上次登录的用户（包括name和password）
    * 将“记住密码”状态设置为上次记住密码状态*/
    private void initUserInfo(String userName){
        if (!userName.isEmpty()){//formerLogin!=null && !formerLogin.equals("")
            Boolean isRember=LoginTool.isRememberPass(userName);
            if (isRember){
                ToastTool.showToast(this,"上个用户记住密码啦");
                mRememberPass.setChecked(true);
                mAutoLogin.setChecked(LoginTool.isAutoLogin(userName));
                //获取数据库中的密码
                List<User> users= DataSupport
                        .select("password")
                        .where("name = ?",userName)
                        .limit(1)
                        .find(User.class);
                if (users!=null){
                    mEditUserPass.setText(users.get(0).getPassword());
                    /**/
                    if (LoginTool.isAutoLogin(userName) &&!mEditUserPass.getText().toString().isEmpty()){
                        ToastTool.showToast(this,"自动登录");
                        mLoginButton.performClick();
                    }
                }
            }
        }
    }

    /*用户名输入框监听器，当用户名改变时，密码框内密码清空*/
    private void changeEditText(){
        mEditUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEditUserPass.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 登陆响应
     * 1、判断用户名和密码是否为空
     * 2、改变登录按钮外观
     * 3、使用RxJava进行异步操作
     *      1、创建Observable时，用just原样发射用户名和密码
     *      2、设置在io线程激活或产生该提交登录事件
     *      3、设置在主线程运行事件
     *      4、使用flatMap进行变换，在内部调用login()实现登录操作，并返回loginResult登录结果
     *
     */
    @OnClick(R.id.login_button)
    public void userLogin(View view){
        //判断用户和密码是否为空
        final String userName=mEditUserName.getText().toString();
        final String userPass=mEditUserPass.getText().toString();
        if (userName.isEmpty() || userName.length()==0){
            ToastTool.showToast(this,getString(R.string.login_null_name));
            return;
        }
        if (userPass.isEmpty() || userPass.length()==0){
            ToastTool.showToast(this,getString(R.string.login_null_pass));
            return;
        }
        //改变登录按钮外观
        mLoginButton.setEnabled(false);//禁用登录按钮
        mLoginButton.setText(getString(R.string.logining));

        //使用handle-message
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("login","LoginThread--"+userName+":"+userPass);
                Message message=new Message();
                message.what=1;//SmackManager.getSmackManager().login(userName,userPass)
                Log.d("login","message.what:"+message.what);
                mHandler.sendMessage(message);
            }
        }).start();
    }

    /*登录成功
    * 一、如果选择记住密码
    *     1、那么将利用litepal存储用户名和密码
    *     2、利用sharedpreferences的boolean存储记住密码状态
    *     3、利用sharedpreferences的int存储自动登录状态*/
    private void loginSuccess(){
        Log.d("login","loginSuccess()");
        final String userName=mEditUserName.getText().toString();
        final String userPass=mEditUserPass.getText().toString();
        Log.d("login",userName);
        LoginTool.setFormerLogin(userName);
        LoginTool.setRememberPass(userName,mRememberPass.isChecked());//保存记住密码状态
        if (mRememberPass.isChecked()){
            LoginTool.setAutoLogin(userName,mAutoLogin.isChecked());
            //保存用户密码
            User loginUser=new User();
            loginUser.setName(userName);
            loginUser.setPassword(userPass);
            loginUser.save();
        }
        //跳转至主界面

    }
}
