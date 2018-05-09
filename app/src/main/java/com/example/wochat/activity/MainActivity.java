package com.example.wochat.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.wochat.R;
import com.example.wochat.tools.ToastTool;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.main_layout)
    protected DrawerLayout mainLayout;

    @BindView(R.id.toolbar)
    protected Toolbar mainToolbar;

    @BindView(R.id.toolbarTitle)
    protected TextView mainToolbarTitle;

    @BindView(R.id.nav_view)
    protected NavigationView mainNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*设置标题栏*/
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示ToolBar的标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示图标
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);//设置图标
        mainToolbarTitle.setText(R.string.toolbar_title);//设置标题

        mainNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            /**/
            case android.R.id.home:
                mainLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.add_user:
                ToastTool.showToast(this,"addUser()");
                break;
            case R.id.create_group:
                ToastTool.showToast(this,"createGroup()");
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.personal_data:
                Log.d("main","mainNavView");
                ToastTool.showToast(MainActivity.this,"个人资料");
                break;
            case R.id.setting:
                Log.d("main","mainNavView");
                ToastTool.showToast(MainActivity.this,"个人资料");
                break;
            case R.id.about:
                Log.d("main","mainNavView");
                ToastTool.showToast(MainActivity.this,"个人资料");
                break;
            case R.id.help:
                Log.d("main","mainNavView");
                ToastTool.showToast(MainActivity.this,"个人资料");
                break;
            case R.id.exit:
                Log.d("main","mainNavView");
                ToastTool.showToast(MainActivity.this,"个人资料");
                break;
        }
        return false;
    }
}
