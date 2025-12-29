package com.bignerdranch.android.enworld.Basic;

import com.bignerdranch.android.enworld.Reading.Article;

/**
 * Created by Jeffrey on 2017/4/19.
 */

public class CollectedArticle extends Article {

    private String username;

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}
