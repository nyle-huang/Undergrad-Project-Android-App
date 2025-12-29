package com.bignerdranch.android.enworld.Reading;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.enworld.Basic.SingleFragmentActivity;

/**
 * Created by Jeffrey on 2017/4/2.
 */

public class ArticleListActivity extends SingleFragmentActivity {
    private static final String EXTRA_ARTICLE_TYPE = "com.bignerdranch.android.enworld.article_type";

    public static Intent newIntent(Context context, String articleType){
        Intent i = new Intent(context, ArticleListActivity.class);
        i.putExtra(EXTRA_ARTICLE_TYPE, articleType);
        return i;
    }

    @Override
    protected Fragment createFragment(){
        String articleType = (String) getIntent()
                .getSerializableExtra(EXTRA_ARTICLE_TYPE);
        return ArticleListFragment.newInstance(articleType);
    }
}
