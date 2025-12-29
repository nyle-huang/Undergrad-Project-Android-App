package com.bignerdranch.android.enworld.Reading;

import android.os.Bundle;
import android.util.Log;

import com.bignerdranch.android.enworld.Basic.CollectedArticle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Jeffrey on 2017/4/19.
 */

public class CollectionFragment extends ArticleListFragment {

    private String mUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = BmobUser.getCurrentUser().getUsername();
    }

    @Override
    protected void updateUI() {

        BmobQuery<CollectedArticle> query = new BmobQuery<CollectedArticle>();
        query.addWhereEqualTo("username", mUsername);
        query.setLimit(20);
        query.findObjects(new FindListener<CollectedArticle>() {
            @Override
            public void done(List<CollectedArticle> list, BmobException e) {
                if (e == null) {
                    List<Article> articleList = new ArrayList<Article>();
                    for (CollectedArticle collectedArticle : list){
                        articleList.add((Article)collectedArticle);
                    }
                    Collections.reverse(articleList);
                    if (mAdapter == null){
                        mAdapter = new ArticleAdapter(articleList);
                        mArticleRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("Bmob", "List<CollectedArticle>载入出错");
                }
            }
        });
    }
}
