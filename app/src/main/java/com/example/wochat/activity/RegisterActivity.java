package com.example.wochat.activity;

import com.example.wochat.R;
import com.example.wochat.singleClass.SmackManager;
import com.example.wochat.tools.ToastTool;
import com.example.wochat.ui.ClearEditText;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_toolbar)
    protected Toolbar mRegisterToolbar;

    @BindView(R.id.toolbarTitle)
    protected TextView mRegisterToolbarTitle;


    @BindView(R.id.register_user_name)
    protected ClearEditText mRegisterName;

    @BindView(R.id.register_user_password)
    protected ClearEditText mRegisterPass;

    @BindView(R.id.register_user_password_again)
    protected ClearEditText mRegisterPassAgain;

    @BindView(R.id.register_button)
    protected Button mRegisterButton;

    @BindView(R.id.goto_login)
    protected TextView mGotoLogin;

    public static final int REGISTER_SUCCESS=1;//注册成功
    public static final int REGISTER_FAILURE=0;//注册错误
    public static final int REGISTER_OFFLINE=2;//服务器连接失败

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setSupportActionBar(mRegisterToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示ToolBar的标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRegisterToolbarTitle.setText(R.string.register);
        mRegisterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private Handler mHandler=new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case REGISTER_SUCCESS://登录成功
                    Log.d("register","success");
                    ToastTool.showToast(RegisterActivity.this,getString(R.string.register_success));
                    registerSuccess();
                    break;
                case REGISTER_FAILURE://失败
                    Log.d("register","failure");
                    ToastTool.showToast(RegisterActivity.this,getString(R.string.register_failure));
                    openInput();
                    break;
                case REGISTER_OFFLINE://离线状态
                    Log.d("register","offline");
                    ToastTool.showToast(RegisterActivity.this,getString(R.string.register_offline));
                    openInput();
                    break;
            }
        }
    };

    /*注册按钮点击事件
    * 1、检测用户名是否符合规范：3-16的字母+数字
    * 2、检查密码是否符合规范：6-18的字母和数字
    * 3、检查两次输入的密码是否一致
    * 4、用户名及密码符合规范时执行注册事件*/
    @OnClick(R.id.register_button)
    public void ClickRegister(){
        closeInput();
        String registerName=mRegisterName.getText().toString();
        String registerPass=mRegisterPass.getText().toString();
        String registerPassAgain=mRegisterPassAgain.getText().toString();

        if (registerName.equals("")||registerPass.equals("")||registerPassAgain.equals("")){
            ToastTool.showToast(this,getString(R.string.error_register_input));
            openInput();
            return;
        }

        /*使用Pattern类，编译正则表达式后创建一个匹配模式.
        * name:^[a-zA-Z0-9]{3,16}$   长度为3-16的字母+数字
        * pass:^[a-zA-Z0-9]{6,18}$   长度为6-16的字母+数字*/
        Pattern namePattern = Pattern.compile(getString(R.string.pattern_register_name));
        Pattern passPattern = Pattern.compile(getString(R.string.pattern_register_password));

        /*matches() 最常用方法:尝试对整个目标字符展开匹配检测,也就是只有整个目标字符串完全匹配时才返回真值*/
        if(!namePattern.matcher(registerName).matches()){
            ToastTool.showToast(this,getString(R.string.error_register_name));
            mRegisterName.setText("");
            openInput();
            return;
        }
        if(!passPattern.matcher(registerPass).matches()){
            ToastTool.showToast(this,getString(R.string.error_register_password));
            mRegisterPass.setText("");
            mRegisterPassAgain.setText("");
            openInput();
            return;
        }
        if (!registerPass.equals(registerPassAgain)){
            ToastTool.showToast(this,getString(R.string.error_register_password_again));
            mRegisterPassAgain.setText("");
            openInput();
            return;
        }

        register(registerName,registerPass);
    }

    public void register(final String name, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=new Message();
                message.what= SmackManager.getSmackManager().register(name, password);//1
                mHandler.sendMessage(message);
            }
        }).start();
    }

    /*跳转至登录界面*/
    public void registerSuccess(){
        Intent intent=new Intent();
        intent.putExtra("registerName",mRegisterName.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }

    /*关闭输入*/
    public void closeInput(){
        mRegisterName.setEnabled(false);
        mRegisterPass.setEnabled(false);
        mRegisterPassAgain.setEnabled(false);
        mRegisterButton.setEnabled(false);
        mRegisterButton.setText(getString(R.string.registering));
    }

    /*开启输入*/
    public void openInput(){
        mRegisterName.setEnabled(true);
        mRegisterPass.setEnabled(true);
        mRegisterPassAgain.setEnabled(true);
        mRegisterButton.setEnabled(true);
        mRegisterButton.setText(getString(R.string.register));
    }
}
