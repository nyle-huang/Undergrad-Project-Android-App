package com.bignerdranch.android.enworld.Pager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bignerdranch.android.enworld.EWApplication;
import com.bignerdranch.android.enworld.IM.ChatActivity;
import com.bignerdranch.android.enworld.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * Created by Jeffrey on 2017/4/7.
 */

public class PagerFragment extends Fragment implements View.OnClickListener{

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager viewPager;
    private LinearLayout llFeeds, llChats, llContacts, llMe;
    private ImageView ivFeeds, ivChats, ivContacts, ivMe, ivCurrent;

    private Map<String, EaseUser> contacts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        initView(view);

        return view;
    }

    private void initView(View v) {
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);

        llFeeds = (LinearLayout) v.findViewById(R.id.llFeeds);
        llChats = (LinearLayout) v.findViewById(R.id.llChats);
        llContacts = (LinearLayout) v.findViewById(R.id.llContacts);
        llMe = (LinearLayout) v.findViewById(R.id.llMe);

        llFeeds.setOnClickListener(this);
        llChats.setOnClickListener(this);
        llContacts.setOnClickListener(this);
        llMe.setOnClickListener(this);

        ivFeeds = (ImageView) v.findViewById(R.id.ivFeeds);
        ivChats = (ImageView) v.findViewById(R.id.ivChats);
        ivContacts = (ImageView) v.findViewById(R.id.ivContacts);
        ivMe = (ImageView) v.findViewById(R.id.ivMe);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        viewPager.setOffscreenPageLimit(3); //设置向左和向右都缓存limit个页面

        Fragment feedsFragment = FeedsFragment.newFeedsFragment();

        EaseConversationListFragment chatsFragment = new EaseConversationListFragment();
        chatsFragment.setConversationListItemClickListener(
                new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                startActivity(intent);
            }
        });

        EaseContactListFragment contactsFragment = new EaseContactListFragment();
        contactsFragment.setContactsMap(getContacts());
        contactsFragment.setContactListItemClickListener(
                new EaseContactListFragment.EaseContactListItemClickListener() {

            @Override
            public void onListItemClicked(EaseUser user) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());
                startActivity(intent);
            }
        });

        Fragment meFragment = new MeFragment();

        fragments.add(feedsFragment);
        fragments.add(chatsFragment);
        fragments.add(contactsFragment);
        fragments.add(meFragment);

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        ivCurrent = ivFeeds;
        changeTab(R.id.llFeeds); //模拟一次点击tab feeds
    }

    @Override
    public void onClick(View v) {
        changeTab(v.getId());
    }

    private void changeTab(int id) {
        ivCurrent.setSelected(false);
        switch (ivCurrent.getId()){
            case R.id.ivFeeds:
                ivFeeds.setImageResource(R.drawable.ic_tab_feeds);
                break;
            case R.id.ivChats:
                ivChats.setImageResource(R.drawable.ic_tab_chats);
                break;
            case R.id.ivContacts:
                ivContacts.setImageResource(R.drawable.ic_tab_contacts);
                break;
            case R.id.ivMe:
                ivMe.setImageResource(R.drawable.ic_tab_me);
                break;
            default:
                break;
        }

        switch (id) {
            case R.id.llFeeds:
                viewPager.setCurrentItem(0);
            case 0:  //此处case服务于viewpager内置函数
                ivFeeds.setSelected(true);
                ivCurrent = ivFeeds;
                break;
            case R.id.llChats:
                viewPager.setCurrentItem(1);
            case 1:
                ivChats.setSelected(true);
                ivCurrent = ivChats;
                break;
            case R.id.llContacts:
                viewPager.setCurrentItem(2);
            case 2:
                ivContacts.setSelected(true);
                ivCurrent = ivContacts;
                break;
            case R.id.llMe:
                viewPager.setCurrentItem(3);
            case 3:
                ivMe.setSelected(true);
                ivCurrent = ivMe;
                break;
            default:
                break;
        }

        switch (viewPager.getCurrentItem()){
            case 0:
                ivFeeds.setImageResource(R.drawable.ic_tab_feeds_selected);
                break;
            case 1:
                ivChats.setImageResource(R.drawable.ic_tab_chats_selected);
                break;
            case 2:
                ivContacts.setImageResource(R.drawable.ic_tab_contacts_selected);
                break;
            case 3:
                ivMe.setImageResource(R.drawable.ic_tab_me_selected);
                break;
            default:
                break;
        }
    }

    private Map<String, EaseUser> getContacts() {

        contacts = new HashMap<String, EaseUser>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    for (String username : usernames) {
                        contacts.put(username, new EaseUser(username));
                    }
                } catch (HyphenateException e) {
                    Log.e("contacts", "获取好友列表失败: " + e);
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
        }

        return contacts;
    }
}
