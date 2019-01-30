package com.mca.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;
import com.mca.Utils.Utils;
import com.mca.Application.DemoApplication;
import com.mca.Job.GetTotalCountJob;
import com.mca.R;
import com.mca.Realm.RealmController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_contact, btn_demo_contact;

    RealmController realmController;
    JobManager jobManager;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        btn_contact.setOnClickListener(this);
        btn_demo_contact.setOnClickListener(this);
    }

    private void initView() {

        jobManager = DemoApplication.getInstance().getJobManager();

        btn_demo_contact = findViewById(R.id.btn_demo_contact);
        btn_contact = findViewById(R.id.btn_contact);

        realmController = RealmController.with(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmController.destroy();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.btn_demo_contact:
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog);

                final EditText txt = dialog.findViewById(R.id.et_data);

                Button ok = dialog.findViewById(R.id.btn_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(txt.getText().toString())) {
                            Utils.counter = Integer.parseInt(txt.getText().toString());
                            dialog.dismiss();
                            intent = new Intent(MainActivity.this, ContactListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter at least one number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.btn_contact:
                jobManager.addJobInBackground(new GetTotalCountJob());

                intent = new Intent(this, ContactListActivity.class);
                startActivity(intent);
                break;
        }
    }
}