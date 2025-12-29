package com.bignerdranch.android.enworld.Reading;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jeffrey on 2017/4/2.
 */

public class Article extends BmobObject implements Serializable{

    protected static final long serialVersionUID = 1L;

    protected String type, title, cover, author, address;

    public void setType(String type){
        this.type = type;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setCover(String cover){
        this.cover = cover;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getType(){
        return type;
    }

    public String getTitle(){
        return title;
    }

    public String getCover(){
        return cover;
    }

    public String getAuthor(){
        return author;
    }

    public String getAddress(){
        return address;
    }
}
