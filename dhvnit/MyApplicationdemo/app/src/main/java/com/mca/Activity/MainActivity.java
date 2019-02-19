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

    Button btn_contact, btn_demo_contact, btn_model;

    RealmController realmController;
    JobManager jobManager;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        Utils.printLog("Login Data",
                "regid" + Utils.getRequestPayloadData(MainActivity.this, Utils.regid) + "/" +
                        "crc" + Utils.getRequestPayloadData(MainActivity.this, Utils.crc) + "/" +
                        "messageserver" + Utils.getRequestPayloadData(MainActivity.this, Utils.messageserver) + "/" +
                        "mesageserverport" + Utils.getRequestPayloadData(MainActivity.this, Utils.messageserverport));

        btn_contact.setOnClickListener(this);
        btn_model.setOnClickListener(this);
        btn_demo_contact.setOnClickListener(this);
    }

    private void initView() {

        jobManager = DemoApplication.getInstance().getJobManager();

        btn_demo_contact = findViewById(R.id.btn_demo_contact);
        btn_contact = findViewById(R.id.btn_contact);
        btn_model = findViewById(R.id.btn_model);

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
                intent.putExtra("groupId", "1");
                intent.putExtra("name", "Contacts");
                startActivity(intent);
                break;

            case R.id.btn_model:
                intent = new Intent(this, MessagesEventsContactsActivity.class);
                startActivity(intent);
        }
    }
}