package com.bignerdranch.android.enworld.Reading;

import android.support.v4.app.Fragment;

import com.bignerdranch.android.enworld.Basic.SingleFragmentActivity;

/**
 * Created by Jeffrey on 2017/4/19.
 */

public class CollectionActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CollectionFragment();
    }
}
