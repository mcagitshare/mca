package com.mca.Job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.mca.Utils.Constants;
import com.mca.Application.DemoApplication;
import com.mca.Model.Item;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.Realm;

public class GetTotalCountJob extends Job {

    JobManager jobManager;
    String count;

    public GetTotalCountJob() {
        super(new Params(500).requireNetwork());
    }

    @Override
    public void onAdded() {
        jobManager = DemoApplication.getInstance().getJobManager();
    }

    @Override
    public void onRun() throws Throwable {

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();

            HttpClient httpclient = new DefaultHttpClient();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result_type", "rec_count");

            HttpPost httpPost = new HttpPost(Constants.urlReadData);
            httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httpPost);

            String result = EntityUtils.toString(response.getEntity());

            JSONArray job = new JSONArray(result);
            for (int i = 0; i < job.length(); i++) {
                JSONObject jsonObject1 = job.getJSONObject(i);
                count = jsonObject1.getString("contact_count").trim();
                Log.e("count", count + "");
            }

            if (Integer.valueOf(count) != realm.where(Item.class).count()) {
                try {
                    int last = 0;
                    for (int i = 0; i <= Integer.valueOf(count) / 50; i++) {
                        Log.e("value", i + " -> " + count);
                        last++;
                        jobManager.addJobInBackground(new
                                ContactJobInsert(last + "", "0", "0", "0", "0"));
                    }
                    Log.e("result", result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}