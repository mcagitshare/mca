package com.project.xr.contactsync.Job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.project.xr.contactsync.Constants;
import com.project.xr.contactsync.Model.Item;
import com.project.xr.contactsync.Model.ReqResp;
import com.project.xr.contactsync.RealmClass;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

import io.realm.Realm;

public class ContactJobInsert extends Job {
    int batchid;
    String id = UUID.randomUUID() + "";
    boolean processed;
    String response = "";
    private long requestTime = 0, responseTime = 0;

    public ContactJobInsert(int batchid) {
        super(new Params(1).requireNetwork());
        this.batchid = batchid;
    }

    @Override
    public void onAdded() {
        //realm insert
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();

            ReqResp reqResp = new ReqResp(id, batchid, processed, response, requestTime, responseTime);
            RealmClass.InsertReqResp(realm, reqResp);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

    }

    @Override
    public void onRun() throws Throwable {

        ReqResp reqResp = RealmClass.getBatchdata(batchid);
        processed = reqResp.isProcessed();
        int bId = reqResp.getBatchId();

        if (!processed) {

            String response = getContacts(bId);

            Realm realm = null;
            try {

                realm = Realm.getDefaultInstance();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject job = jsonArray.getJSONObject(i);

                        String id = job.getString("id");
                        String name = job.getString("name");
                        String image = job.getString("image");
                        String phone = job.getString("phone");

                        Item item = new Item(id, name, phone, image);
                        RealmClass.Insertdata(realm, item);
                    } catch (Exception ex) {
                        Log.e("Exception", response);
                        ex.printStackTrace();
                    }
                }
                processed = true;
                Log.e("BatchData", "ID:" + id + "bId" + bId + "reeponse" + response);
                ReqResp data = new ReqResp(id, bId, processed, response, requestTime, responseTime);
                RealmClass.InsertReqResp(realm, data);

            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }
    }

    private String getContacts(int batchid) {
        String result = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();

            JSONObject jsonObject = new JSONObject();
            JSONObject block = new JSONObject();
            block.put("block_size", 50);
            block.put("block_id", batchid);
            jsonObject.put("block", block);

            HttpPost httpPost = new HttpPost(Constants.url);
            httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httpPost);

            result = EntityUtils.toString(response.getEntity());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        // when job is canceled then goes here
        // in cancelReason given reason for why job cancel @ THIS CLASS WILL GIVEN ERROR RESULT = (JobHolder.java)
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        // if onRun method getting error then call this method

        return null;
    }

}
