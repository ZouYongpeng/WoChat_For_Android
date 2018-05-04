package com.example.wochat.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 邹永鹏 on 2018/5/4.
 */

public class User extends DataSupport{
/**
 * Returns an unmodifiable collection of the names of the required account attributes.
 * All attributes must be set when creating new accounts. The standard set of possible
 * attributes are as follows: <ul>
 *      <li>name -- the user's name.
 *      <li>first -- the user's first name.
 *      <li>last -- the user's last name.
 *      <li>email -- the user's email address.
 *      <li>city -- the user's city.
 *      <li>state -- the user's state.
 *      <li>zip -- the user's ZIP code.
 *      <li>phone -- the user's phone number.
 *      <li>url -- the user's website.
 *      <li>date -- the date the registration took place.
 *      <li>misc -- other miscellaneous information to associate with the account.
 *      <li>text -- textual information to associate with the account.
 *      <li>remove -- empty flag to remove account.
 * </ul><p>
 * */

    private String name;//用户名
    private String password;//用户密码
    private int state;//用户状态

    private String head;//用户头像
    private int age;//年龄
    private boolean sex;//性别

    private int phoneNum;//电话
    private String email;//邮箱
    private String text;//个性签名

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getState() {
        return state;
    }

    public String getHead() {
        return head;
    }

    public int getAge() {
        return age;
    }

    public boolean isSex() {
        return sex;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getText() {
        return text;
    }
}
