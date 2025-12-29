package com.bignerdranch.android.enworld.Reading;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.enworld.R;

import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Jeffrey on 2017/4/2.
 */

public class ArticleListFragment extends Fragment {

    protected static final String ARG_ARTICLE_TYPE = "article_type";

    protected RecyclerView mArticleRecyclerView;
    protected ArticleAdapter mAdapter;
    protected String mArticleType;

    public static ArticleListFragment newInstance(String articleType){
        Bundle args = new Bundle();
        args.putSerializable(ARG_ARTICLE_TYPE, articleType);

        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        mArticleRecyclerView = (RecyclerView) view.findViewById(R.id.article_recycler_view);
        mArticleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    protected void updateUI(){
        mArticleType = (String) getArguments().getSerializable(ARG_ARTICLE_TYPE);

        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereEqualTo("type", mArticleType);
        query.setLimit(20);
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null){
                    if (mAdapter == null){
                        Collections.reverse(list);
                        mAdapter = new ArticleAdapter(list);
                        mArticleRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("bmob", "Failed to retrieve article data: "
                            + e.getMessage() + "," + e.getErrorCode());
                    Toast.makeText(getActivity(), R.string.refresh_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mAuthorTextView;
        private ImageView mCoverImageView;
        private Article mArticle;

        public ArticleHolder(View itemView){
            super(itemView);

            mTitleTextView = (TextView) itemView
                    .findViewById(R.id.list_item_article_title_text_view);
            mAuthorTextView = (TextView) itemView
                    .findViewById(R.id.list_item_article_author_text_view);
            mCoverImageView = (ImageView) itemView
                    .findViewById(R.id.list_item_article_cover_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindArticle(Article article){
            mArticle = article;
            mTitleTextView.setText(mArticle.getTitle());
            mAuthorTextView.setText(mArticle.getAuthor());
            new LoadPictrue().getPicture(mArticle.getCover(),mCoverImageView);
        }

        @Override
        public void onClick(View v){
            Intent intent = ArticleActivity.newIntent(getActivity(), mArticle);
            startActivity(intent);
        }
    }

    protected class ArticleAdapter extends RecyclerView.Adapter<ArticleHolder>{

        private List<Article> mArticles;

        public ArticleAdapter(List<Article> articles){
            mArticles = articles;
        }

        @Override
        public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_article,parent,false);
            return new ArticleHolder(view);
        }

        @Override
        public void onBindViewHolder(ArticleHolder holder, int position){
            Article article = mArticles.get(position);
            holder.bindArticle(article);
        }

        @Override
        public int getItemCount(){
            return mArticles.size();
        }
    }
}
