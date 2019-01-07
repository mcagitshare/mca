package com.project.xr.contactsync.Activity;

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
import com.project.xr.contactsync.Class.Utils;
import com.project.xr.contactsync.DemoApplication;
import com.project.xr.contactsync.Job.GetTotalCountJob;
import com.project.xr.contactsync.R;
import com.project.xr.contactsync.Realm.RealmController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_contact, btn_demo_contact;

    String url = "http://www.meclubapp.com/public/gw/contact/read.php";
    RealmController realmController;
    JobManager jobManager;

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
                            for (int i = 1; i <= Utils.counter; i++) {
//                                Item item = new Item(i + i + i, "name" + i, "phone" + i, "1");
//                                RealmClass.Insertdata(realm,item);
//                                jobManager.addJobInBackground(new ContactJobInsert(
//                                        String.valueOf()R.mipmap.icon,
//                                        "Name" + i,
//                                        "" + i));
                            }
                            Intent contactIntent = new Intent(MainActivity.this, ContactListActivity.class);
                            startActivity(contactIntent);
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter at least one number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.btn_contact:
                jobManager.addJobInBackground(new GetTotalCountJob());

                Intent intent = new Intent(this, ContactListActivity.class);
                startActivity(intent);
                break;
        }
    }
}