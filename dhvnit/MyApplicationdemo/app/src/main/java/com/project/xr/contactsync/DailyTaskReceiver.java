package com.project.xr.contactsync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.project.xr.contactsync.Class.Utils;
import com.project.xr.contactsync.Job.GetTotalCountJob;

public class DailyTaskReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Utils.printLog("RECEIVER", "daily task");
        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
                Utils.setDailyTask(context);
                break;
            case Constants.RECEIVER_DAILY_TASK_ACTION_SYNC_DATA:
                //Auto Sync Data every 4 hour.
                DemoApplication.getInstance().getJobManager().addJobInBackground(new GetTotalCountJob());
                Log.e("Job called", "Data will refreshed");
                break;
            default:
                break;
        }
    }
}