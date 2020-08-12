package com.obiangetfils.homefood.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.obiangetfils.homefood.fragments.order.OngoingOrderFragment;
import com.obiangetfils.homefood.fragments.order.PassOrderFragment;

public class MyAdapter extends FragmentPagerAdapter {



    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OngoingOrderFragment ongoingOrderFragment = new OngoingOrderFragment();
                return ongoingOrderFragment;
            case 1:
                PassOrderFragment passOrderFragment = new PassOrderFragment();
                return passOrderFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
