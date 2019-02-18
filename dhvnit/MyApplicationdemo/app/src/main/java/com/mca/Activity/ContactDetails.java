package com.mca.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mca.Application.DemoApplication;
import com.mca.Job.EventsJob;
import com.mca.Job.GroupJob;
import com.mca.Job.MessagesJob;
import com.mca.Model.Event;
import com.mca.Model.Group;
import com.mca.Model.Message;
import com.mca.R;
import com.mca.Realm.RealmController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetails extends AppCompatActivity {

    CircleImageView image;
    LinearLayout ll_acc_rej;
    Button accept, reject;
    TextView name, phone, id;
    TextView tv_name;
    Toolbar toolbar;

    Intent intent;
    Boolean acRej = true;

    RealmController realmController;
    JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        intent = getIntent();

        jobManager = DemoApplication.getInstance().getJobManager();
        realmController = RealmController.with(this);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle(intent.getStringExtra("name"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        findId();

        tv_name.setText(intent.getStringExtra("Name"));
        name.setText(intent.getStringExtra("name"));
        phone.setText(intent.getStringExtra("phone"));
        id.setText(intent.getStringExtra("id"));

        if (intent.getIntExtra("type", 0) == 100) {
            try {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("jsonMessage"));
                int type = jsonObject.getInt("type");
                if (type == 101) {
                    Event event = realmController.getEventId(intent.getStringExtra("id"));
                    if (event != null) {
                        acRej = event.getAccRej();
                    }
                }
                if (type == 100) {
                    Message message = realmController.getMessageId(intent.getStringExtra("id"));
                    if (message != null) {
                        acRej = message.getAccRej();
                        Toast.makeText(this, message.getUnReadCount() + "", Toast.LENGTH_SHORT).show();
                    }
                }
                if (type == 103) {
                    Group group = realmController.getGroupId(intent.getStringExtra("id"));
                    if (group != null) {
                        acRej = group.getAccRej();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Glide.with(this)
                .load(intent.getStringExtra("image"))
                .apply(new RequestOptions().placeholder(R.mipmap.contact).error(R.mipmap.contact))
                .into(image);

        if (!acRej) {
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject jsonObject = new JSONObject(intent.getStringExtra("jsonMessage"));
                        if (jsonObject.getInt("type") == 101) {

                            //  add the event to Event List
                            Boolean readStatus = false;
                            Boolean accRej = false;
                            String eventName = jsonObject.getString("eventname");
                            String icon = jsonObject.getString("icon");
                            String dateFrom = jsonObject.getString("datefrom");
                            String dateTo = jsonObject.getString("dateto");

                            String option = null;
                            JSONArray optionsArray = jsonObject.getJSONArray("options");
                            for (int i = 0; i < optionsArray.length(); i++) {
                                JSONObject jobOptions = optionsArray.getJSONObject(i);
                                if (jobOptions.getInt("responecode") == 200) {
                                    option = jobOptions.getString("option");
                                }
                            }
                            jobManager.addJobInBackground(new EventsJob(intent.getStringExtra("id"), eventName, icon, dateFrom, dateTo, option, readStatus, accRej));
                            deleteMessageData(intent.getStringExtra("id"));
                        }

                        if (jsonObject.getInt("type") == 103) {
                            // add the contact to Group List
                            Boolean readStatus = false;
                            Boolean accRej = false;
                            String groupId = jsonObject.getString("groupid");
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            String image = jsonObject.getString("image");
                            String phone = jsonObject.getString("phone");

                            jobManager.addJobInBackground(new GroupJob(intent.getStringExtra("id"),
                                    groupId, id, name, image, phone, readStatus, accRej));
                            deleteMessageData(intent.getStringExtra("id"));
                        }

                        if (jsonObject.getInt("type") == 100) {

                            // add the message to Message List
                            Boolean readStatus = false;
                            Boolean accRej = false;
                            String displayMessage = jsonObject.getString("displaymessage");
                            String icon = jsonObject.getString("icon");

                            String option = null;
                            JSONArray optionsArray = jsonObject.getJSONArray("options");
                            for (int i = 0; i < optionsArray.length(); i++) {
                                JSONObject jobOptions = optionsArray.getJSONObject(i);
                                if (jobOptions.getInt("responecode") == 200) {
                                    option = jobOptions.getString("option");
                                }
                            }
                            deleteMessageData(intent.getStringExtra("id"));
                            jobManager.addJobInBackground(new MessagesJob(intent.getStringExtra("id"),
                                    displayMessage, icon, option, readStatus, accRej, intent.getStringExtra("jsonMessage"), 0));

                        }
                    } catch (JSONException e) {
                        e.getLocalizedMessage();
                    }
                    ll_acc_rej.setVisibility(View.GONE);
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMessageData(intent.getStringExtra("id"));
                    ll_acc_rej.setVisibility(View.GONE);
                }
            });
        } else
            ll_acc_rej.setVisibility(View.GONE);
    }

    public void deleteMessageData(String itemId) {
        realmController.deleteMessageData(itemId);
    }

    private void findId() {
        tv_name = findViewById(R.id.tv_name);
        image = findViewById(R.id.contact);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        id = findViewById(R.id.id);

        ll_acc_rej = findViewById(R.id.ll_acc_rej);
        accept = findViewById(R.id.btn_accept);
        reject = findViewById(R.id.btn_reject);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}