package com.mca.Activity;

import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mca.Adapter.ViewPagerAdapter;
import com.mca.R;
import com.mca.Realm.RealmController;

public class MessagesEventsContactsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    RealmController realmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_events_contacts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView contacts = findViewById(R.id.tv_contacts);
        contacts.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getCurrentItem();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        realmController = RealmController.with(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}