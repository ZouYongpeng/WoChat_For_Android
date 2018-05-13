package com.example.wochat.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wochat.R;
import com.example.wochat.fragment.mainFriendsFragment;
import com.example.wochat.fragment.mainMessageFragment;
import com.example.wochat.tools.FragmentAdapter;
import com.example.wochat.tools.ToastTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        ,ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    @BindView(R.id.main_layout)
    protected DrawerLayout mainLayout;

    @BindView(R.id.toolbar)
    protected Toolbar mainToolbar;

    @BindView(R.id.toolbarTitle)
    protected TextView mainToolbarTitle;

    @BindView(R.id.nav_view)
    protected NavigationView mainNavView;

    @BindView(R.id.main_viewPage)
    protected ViewPager mainViewPage;

    @BindView(R.id.main_fragmentTabHost)
    protected FragmentTabHost tabHost;

    /*NavigationView滑动菜单内控件*/
    private View headerLayout;
    private CircleImageView navUserHead;
    private TextView navUserName;

    /*底部菜单栏——Fragment+FragmentTabHost++ViewPager
    * 参考资料：https://www.jianshu.com/p/a663803b2a44*/
    private LayoutInflater mLayoutInflater;
    private Class fragmentArray[]={mainMessageFragment.class, mainFriendsFragment.class};
    private int tabImageViewArray[]={R.drawable.ic_main_message,R.drawable.ic_main_friends};
    private int tabSelectedImageViewArray[]={R.drawable.ic_main_message_selected,R.drawable.ic_main_friends_selected};
    private int tabTextViewArray[]={R.string.fragment_message_tab,R.string.fragment_friends_tab};
    private List<Fragment> mFragmentList=new ArrayList<Fragment>();

    /*用包名修饰extra数据信息，可避免来自不同应用的extra间发生命名冲突*/
    private static final String CURRENT_USER_NAME="com.example.wochat.current_user_name";
    private static String currentUserHeadUrl;
    private String currentUserName;

    private static final String TAG="MainActivity";

    public static Intent logonToMain (Context packageContext,String uaerName){
        Intent intent=new Intent(packageContext,MainActivity.class);
        intent.putExtra(CURRENT_USER_NAME,uaerName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        currentUserName=getIntent().getStringExtra(CURRENT_USER_NAME);
        initNav();//初始化NavigationView滑动菜单
        initToolBar();//初始化标题栏
        initViewPage();//初始化ViewPage
        initFragment();//初始化碎片

    }
    /*初始化NavigationView滑动菜单*/
    private void initNav(){
        /*NavigationView中获取headerLayout的方法:
        * 1、获取headerLayout
        * 2、获取其中的组件
        * https://blog.csdn.net/qq_15907463/article/details/52352561*/
        //获取headerLayout
        headerLayout = mainNavView.inflateHeaderView(R.layout.nav_header);
        //获取其中的组件
        navUserHead=(CircleImageView)headerLayout.findViewById(R.id.user_head);
        navUserName=(TextView)headerLayout.findViewById(R.id.user_name);

        /*使用Tomcat和glide加载电脑服务器的文件
        * 1、手机与电脑在同一局域网，如手机可以连接电脑WiFi或连接相同WiFi，且电脑关闭防火墙
        * 2、此时电脑IP为192.168.155.1，手机IP为192.168.155.3
        * 2、电脑打开Tomcat，可以访问 IP地址：端口/文件名，如http://192.168.155.1:8080/new_head.jpg
        * 4、那么手机也可以访问
        * 参考资料：https://blog.csdn.net/u011439289/article/details/17529215*/

        /*加载当前用户头像
        * 1、获取url*/
        currentUserHeadUrl="http://192.168.155.1:8080/new_head.jpg";
        Glide.with(this)
                .load(currentUserHeadUrl)
                .error(R.drawable.login_user_head)
                .into(navUserHead);

        mainNavView.setNavigationItemSelectedListener(this);
    }

    /*初始化标题栏*/
    private void initToolBar(){
        /*设置标题栏*/
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示ToolBar的标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示图标
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);//设置图标
        mainToolbarTitle.setText(R.string.toolbar_title);//设置标题
        navUserName.setText(currentUserName);
    }

    /*初始化ViewPage*/
    private void initViewPage(){

        /*实现OnPageChangeListener接口,目的是监听Tab选项卡的变化，然后通知ViewPager适配器切换界面
        * 简单来说,是为了让ViewPager滑动的时候能够带着底部菜单联动*/
        mainViewPage.addOnPageChangeListener(this);//设置页面切换时的监听器
        mLayoutInflater=LayoutInflater.from(this);//加载布局管理器

        /*绑定viewpager*/
        tabHost.setup(this,getSupportFragmentManager(),R.id.main_viewPage);
        /*实现setOnTabChangedListener接口,目的是为监听界面切换），然后实现TabHost里面图片文字的选中状态切换
        * 简单来说,是为了当点击下面菜单时,上面的ViewPager能滑动到对应的Fragment*/
        tabHost.setOnTabChangedListener(this);

        /*新建Tabspec选项卡并设置Tab菜单栏的内容和绑定对应的Fragment*/
        int tabCount=tabTextViewArray.length;
        for (int i=0;i<tabCount;i++){
            //给每个Tab按钮设置标签、图标和文字
            TabHost.TabSpec tabSpec=tabHost.newTabSpec(getString(tabTextViewArray[i]))
                    .setIndicator(getTabItemView(i));
            /*将Tab按钮添加进Tab选项卡中，并绑定Fragment*/
            tabHost.addTab(tabSpec,fragmentArray[i],null);
            tabHost.setTag(i);
        }
    }

    /*将xml布局转换为View对象*/
    private View getTabItemView(int i){
        View view =mLayoutInflater.inflate(R.layout.main_tab_content,null);
        /*利用view对象，找到布局中的组件,并设置内容，然后返回视图*/
        ImageView tabImageView=(ImageView)view.findViewById(R.id.main_tab_image);
        /*第一次打开页面时，默认打开第一个底部标签，所以设计点击图标*/
        if (i==0)
            tabImageView.setBackgroundResource(tabSelectedImageViewArray[i]);
        else
            tabImageView.setBackgroundResource(tabImageViewArray[i]);
        TextView tabTextView=(TextView)view.findViewById(R.id.main_tab_textView);
        tabTextView.setText(tabTextViewArray[i]);
        return view;
    }

    private void initFragment(){
        Fragment messageFragment=new mainMessageFragment();
        Fragment friendsFragment=new mainFriendsFragment();

        mFragmentList.add(messageFragment);
        mFragmentList.add(friendsFragment);

        /*绑定适配器*/
        mainViewPage.setAdapter(new FragmentAdapter(getSupportFragmentManager(),mFragmentList));
        tabHost.getTabWidget().setDividerDrawable(null);
    }

    /*arg0 ==1的时候表示正在滑动
    * arg0==2的时候表示滑动完毕了
    * arg0==0的时候表示什么都没做，就是停在那。*/
    @Override
    public void onPageScrollStateChanged(int arg0) {
        switch (arg0){
            case 0:
                Log.d(TAG, "onPageScrollStateChanged: arg==0");
                break;
            case 1:
                Log.d(TAG, "onPageScrollStateChanged: arg==1");
                break;
            case 2:
                Log.d(TAG, "onPageScrollStateChanged: arg==2");
                break;
        }
    }

    /*表示在前一个页面滑动到后一个页面的时候，在前一个页面滑动前调用的方法*/
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /*arg0是表示你当前选中的页面位置Postion，页面跳转完毕的时候调用。*/
    @Override
    public void onPageSelected(int arg0) {
        TabWidget widget = tabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);//设置View覆盖子类控件而直接获得焦点
        tabHost.setCurrentTab(arg0);//根据位置Postion设置当前的Tab
        widget.setDescendantFocusability(oldFocusability);//设置取消分割线
        /*改变底部标签图片*/
        ImageView nowView=(ImageView) widget.getChildAt(arg0).findViewById(R.id.main_tab_image);
        ImageView oldView=(ImageView) widget.getChildAt(1-arg0).findViewById(R.id.main_tab_image);
        nowView.setBackgroundResource(tabSelectedImageViewArray[arg0]);
        oldView.setBackgroundResource(tabImageViewArray[1-arg0]);
    }

    /*Tab改变的时候调用*/
    @Override
    public void onTabChanged(String tabId) {
        int position = tabHost.getCurrentTab();
        mainViewPage.setCurrentItem(position);//把选中的Tab的位置赋给适配器，让它控制页面切换
    }

    /*创建主界面右上角的菜单*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu,menu);
        return true;
    }

    /*菜单点击事件*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            /*toolbar菜单项*/
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

    /*滑动菜单点击事件*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.personal_data:
                Log.d("main","mainNavView");
                ToastTool.showToast(MainActivity.this,"个人资料");
                break;
            case R.id.setting:
                Log.d("main","mainNavView");
                ToastTool.showToast(MainActivity.this,"设置");
                break;
            case R.id.about:
                Log.d("main","mainNavView");
                ToastTool.showToast(MainActivity.this,"关于WoChat");
                break;
            case R.id.help:
                Log.d("main","mainNavView");
                ToastTool.showToast(MainActivity.this,"帮助");
                break;
            case R.id.exit:
                Log.d("main","mainNavView");
                ToastTool.showToast(MainActivity.this,"退出");
                break;
        }
        return false;
    }

}
