package com.bignerdranch.android.enworld.Reading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.bignerdranch.android.enworld.Basic.SingleFragmentActivity;


/**
 * Created by Jeffrey on 2017/4/2.
 */

public class ArticleActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, Article article){
        Intent i = new Intent(context, ArticleActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("article", article);
        i.putExtras(args);
        return i;
    }

    @Override
    protected Fragment createFragment(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle args = intent.getExtras();

        Article article = (Article)args.getSerializable("article");
        return ArticleFragment.newInstance(article);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
