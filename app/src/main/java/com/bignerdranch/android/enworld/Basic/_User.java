package com.bignerdranch.android.enworld.Basic;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jeffrey on 2017/3/5.
 */

public class _User extends BmobObject {

    private String username;
    private String password;

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }
}
