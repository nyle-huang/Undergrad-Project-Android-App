package com.bignerdranch.android.enworld.Reading;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bignerdranch.android.enworld.Basic.CollectedArticle;
import com.bignerdranch.android.enworld.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Jeffrey on 2017/4/2.
 */

public class ArticleFragment extends Fragment {

    private static final String ARG_ADDRESS = "article_address";

    private Article mArticle;
    private Uri mAddress;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    public static ArticleFragment newInstance(Article article){
        Bundle args = new Bundle();
        args.putSerializable("article", article);

        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mArticle = (Article) getArguments().getSerializable("article");
        mAddress = Uri.parse(mArticle.getAddress());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_article, container, false);

        mProgressBar = (ProgressBar) v.findViewById(R.id.article_progress_bar);
        mProgressBar.setMax(100);

        mWebView = (WebView) v.findViewById(R.id.article_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView webView, int newProgress){
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }

            public void onReceivedTitle(WebView webView, String title) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mAddress.toString());

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_article, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_item_collect:
                CollectedArticle article = new CollectedArticle();
                article.setUsername(BmobUser.getCurrentUser().getUsername());
                article.setTitle(mArticle.getTitle());
                article.setCover(mArticle.getCover());
                article.setAuthor(mArticle.getAuthor());
                article.setAddress(mArticle.getAddress());
                article.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "收藏失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
