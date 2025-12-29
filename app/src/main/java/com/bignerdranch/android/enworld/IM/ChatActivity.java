package com.bignerdranch.android.enworld.IM;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bignerdranch.android.enworld.Basic.LoginActivity;
import com.bignerdranch.android.enworld.Basic.SingleFragmentActivity;
import com.bignerdranch.android.enworld.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * Created by Jeffrey on 2017/4/16.
 */

public class ChatActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        EaseChatFragment chatFragment = new EaseChatFragment();
        String userId = (String) getIntent().getSerializableExtra(EaseConstant.EXTRA_USER_ID);
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, userId);
        chatFragment.setArguments(args);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragment_container, chatFragment).commit();
    }
    /*
    @Override
    protected Fragment createFragment(){
        String userId = (String) getIntent().getSerializableExtra(EaseConstant.EXTRA_USER_ID);
        EaseChatFragment chatFragment = new EaseChatFragment();
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, userId);
        chatFragment.setArguments(args);
        return chatFragment;
    }
    */
}
