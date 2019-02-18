package com.mca.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.birbit.android.jobqueue.JobManager;
import com.mca.Adapter.MessageChatRecyclerAdapter;
import com.mca.Adapter.MessageRecyclerAdapter;
import com.mca.Adapter.SimpleDividerItemDecoration;
import com.mca.Application.DemoApplication;
import com.mca.R;
import com.mca.Realm.RealmController;
import com.mca.Utils.Utils;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class MessageChatActivity extends AppCompatActivity {

    RealmController realmController;
    RecyclerView recyclerView;
    MessageChatRecyclerAdapter adapter;
    JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("name");

        jobManager = DemoApplication.getInstance().getJobManager();
        recyclerView = findViewById(R.id.recyclerView);
        realmController = RealmController.with(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        Utils.printLog("messageBody", realmController.getMessageDetails() + "");

        adapter = new MessageChatRecyclerAdapter(this, realmController.getMessageDetails());
        realmController.getRealm().addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                if (realmController.getMessages() != null && realmController.getMessages().size() > 0) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(realmController.getMessageDetails().size() - 1);

        RealmController.clearUnreadCountMessage(realmController.getRealm(),intent.getStringExtra("id"));
    }
}