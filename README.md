# WoChat For Android
  基于基于Openfire+Smack+Spark的搭建和调试，可在这篇博客查看
  https://blog.csdn.net/sbsujjbcy/article/details/48734539
# 登录界面具有以下内容：
      1、自定义的带清除功能的输入框
      2、利用SharedPreferences存储登录界面的数据
      3、利用LitePal存储登录用户的用户名和密码
      缺陷有：无法更新不同用户的头像
# 注册界面具有以下内容：
      1、自定义的带清除功能的输入框
      2、使用正则表达式检查、匹配输入的用户名和密码
      3、注册成功将会把用户名返回显示在登录界面
      缺陷：注册时不能编辑用户的头像、注册内容单一，日后改进
# 主界面具有以下内容
      1、具有NavigationView滑动菜单，内含CircleImageView图片圆形化控件和menu菜单，
         图片加载使用glide库。
      2、底部栏由Fragment+FragmentTabHost++ViewPager实现
         Fragment：存放不同选项的页面内容
         FragmentTabHost：点击切换选项卡
         ViewPager：实现页面的左右滑动效果
         

