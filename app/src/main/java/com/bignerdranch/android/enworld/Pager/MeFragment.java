package com.bignerdranch.android.enworld.Pager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.enworld.Basic.LoginActivity;
import com.bignerdranch.android.enworld.R;
import com.bignerdranch.android.enworld.Reading.CollectionActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import cn.bmob.v3.BmobUser;

/**
 * Created by Jeffrey on 2017/4/14.
 */

public class MeFragment extends Fragment {

    private TextView mCollection, mSignOut;
    private BmobUser bu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        initView(view);

        return view;
    }

    private void initView(View view){

        mCollection = (TextView)view.findViewById(R.id.collection);
        mCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
            }
        });

        mSignOut = (TextView)view.findViewById(R.id.signout);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注销Bmob连接
                BmobUser.logOut();
                bu=BmobUser.getCurrentUser();
                //注销Easemob连接
                // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);
                        Log.i("Easemob", "Logout - success");
                        getActivity().finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i("Easemob", "Logout - error " + i + " - " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                    }
                });
            }
        });
    }
}
