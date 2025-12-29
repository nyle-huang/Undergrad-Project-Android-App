package com.bignerdranch.android.enworld.Pager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bignerdranch.android.enworld.R;
import com.bignerdranch.android.enworld.Reading.ArticleListActivity;
import com.bignerdranch.android.enworld.Reading.ArticleListFragment;

/**
 * Created by Jeffrey on 2017/4/14.
 */

public class FeedsFragment extends ArticleListFragment implements View.OnClickListener {

    private LinearLayout llCet4, llCet6, llToefl, llIelts, llSat, llGre, llGmat, llBec;

    public static FeedsFragment newFeedsFragment(){
        Bundle args = new Bundle();
        args.putSerializable(ARG_ARTICLE_TYPE, "news");

        FeedsFragment fragment = new FeedsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        initView(view);

        updateUI();

        return view;
    }

    private void initView(View v){
        mArticleRecyclerView = (RecyclerView) v.findViewById(R.id.news_recycler_view);
        mArticleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        llCet4=(LinearLayout) v.findViewById(R.id.llCet4);
        llCet4.setOnClickListener(this);
        llCet6=(LinearLayout) v.findViewById(R.id.llCet6);
        llCet6.setOnClickListener(this);
        llToefl=(LinearLayout) v.findViewById(R.id.llToefl);
        llToefl.setOnClickListener(this);
        llIelts=(LinearLayout) v.findViewById(R.id.llIelts);
        llIelts.setOnClickListener(this);
        llSat=(LinearLayout) v.findViewById(R.id.llSat);
        llSat.setOnClickListener(this);
        llGre=(LinearLayout) v.findViewById(R.id.llGre);
        llGre.setOnClickListener(this);
        llGmat=(LinearLayout) v.findViewById(R.id.llGmat);
        llGmat.setOnClickListener(this);
        llBec=(LinearLayout) v.findViewById(R.id.llBec);
        llBec.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

        Intent intent;
        switch (v.getId()){
            case R.id.llCet4:
                intent = ArticleListActivity.newIntent(getActivity(), "cet4");
                startActivity(intent);
                break;
            case R.id.llCet6:
                intent = ArticleListActivity.newIntent(getActivity(), "cet6");
                startActivity(intent);
                break;
            case R.id.llToefl:
                intent = ArticleListActivity.newIntent(getActivity(), "toefl");
                startActivity(intent);
                break;
            case R.id.llIelts:
                intent = ArticleListActivity.newIntent(getActivity(), "ielts");
                startActivity(intent);
                break;
            case R.id.llSat:
                intent = ArticleListActivity.newIntent(getActivity(), "sat");
                startActivity(intent);
                break;
            case R.id.llGre:
                intent = ArticleListActivity.newIntent(getActivity(), "gre");
                startActivity(intent);
                break;
            case R.id.llGmat:
                intent = ArticleListActivity.newIntent(getActivity(), "gmat");
                startActivity(intent);
                break;
            case R.id.llBec:
                intent = ArticleListActivity.newIntent(getActivity(), "bec");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
