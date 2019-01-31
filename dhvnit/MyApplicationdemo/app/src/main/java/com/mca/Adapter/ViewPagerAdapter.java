package com.mca.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mca.Fragment.ContactsFragment;
import com.mca.Fragment.EventsFragment;
import com.mca.Fragment.MessagesFragment;
import com.mca.R;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;
    Context context;

    //Constructor to the class
    public ViewPagerAdapter(Context context, FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.context = context;
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MessagesFragment();
            case 1:
                return new EventsFragment();
            case 2:
                return new ContactsFragment();
            default:
                return new MessagesFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.messages);
            case 1:
                return context.getString(R.string.events);
            case 2:
                return context.getString(R.string.contacts);
            default:
                return null;
        }
    }
}
