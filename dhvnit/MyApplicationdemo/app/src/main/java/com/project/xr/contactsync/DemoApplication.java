package com.project.xr.contactsync;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.project.xr.contactsync.Activity.MainActivity;
import com.project.xr.contactsync.Activity.SignInActivity;
import com.project.xr.contactsync.Class.Utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DemoApplication extends Application {

    private static DemoApplication instance;
    JobManager jobManager;

    public DemoApplication() {
        instance = this;
    }

    public static DemoApplication getInstance() {
        return instance;
    }

    public static void setDailyTask(Context context) {

        //Every 4 hour data refreshed from the urlReadData
        Intent intentTempFile = new Intent(context, DailyTaskReceiver.class);
        intentTempFile.setAction(Constants.RECEIVER_DAILY_TASK_ACTION_SYNC_DATA);
        boolean isTempFileWorking = (PendingIntent.getBroadcast(context, Constants.RECEIVER_DAILY_TASK, intentTempFile, PendingIntent.FLAG_NO_CREATE) != null);//just changed the flag
        if (!isTempFileWorking) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 4);
            calendar.set(Calendar.MINUTE, 0);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.RECEIVER_DAILY_TASK, intentTempFile, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            assert manager != null;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY / 4, pendingIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();

        setDailyTask(getApplicationContext());

        if (!Utils.getInPrefs(this, Utils.DeviceData)) {
            String mac_address = getMac();
            if (mac_address != null) {
                Utils.storeRequestPayloadData(this, Utils.MAC_id, mac_address);
            }

            String imei = getIMEI();
            if (imei != null) {
                Utils.storeRequestPayloadData(this, Utils.IMEI, imei);
            }

            String msdisdn = getMSISDN();
            if (msdisdn != null) {
                Utils.storeRequestPayloadData(this, Utils.MSISDN, msdisdn);
            }

            String country = getCountry();
            if (country != null) {
                Utils.storeRequestPayloadData(this, Utils.country, country);
            }

            String device_os_type = getDevice_os_type();
            if (device_os_type != null) {
                Utils.storeRequestPayloadData(this, Utils.device_os_type, device_os_type);
            }

            Utils.storeRequestPayloadData(this, Utils.device_type, Build.TYPE);
            Utils.storeRequestPayloadData(this, Utils.device_serial_no, Build.SERIAL);
            Utils.storeRequestPayloadData(this, Utils.device_os_version, Build.VERSION.RELEASE);

            Utils.storeInPrefs(this, Utils.DeviceData, true);
        }

        Realm.init(this);
        setupUncaughtException();
        final RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("myContactdemo.realm")
                .schemaVersion(10)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        Configuration configuration = new Configuration.Builder(this)
                .build();
        jobManager = new JobManager(configuration);
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    private void setupUncaughtException() {

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable e) {

                Intent intent = new Intent(getInstance(), ExceptionHandlerActivity.class);
                intent.putExtra("Exception", e + "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
    }

    private String getMac() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert manager != null;
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    private String getMSISDN() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return mTelephonyMgr.getSubscriberId();
    }

    private String getCountry() {
        String country_name = null;
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        for (String provider : lm.getAllProviders()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        country_name = addresses.get(0).getCountryName();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return country_name;
    }

    private String getIMEI() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        assert mTelephonyMgr != null;
        return mTelephonyMgr.getDeviceId();
    }

    private String getDevice_os_type() {

        switch (Build.VERSION.SDK_INT) {

            case 11:
                return "Honeycomb";

            case 12:
                return "Honeycomb";

            case 13:
                return "Honeycomb";

            case 14:
                return "Ice Cream Sandwich";

            case 15:
                return "Ice Cream Sandwich";

            case 16:
                return "Jelly Bean";

            case 17:
                return "Jelly Bean";

            case 18:
                return "Jelly Bean";

            case 19:
                return "KitKat";

            case 21:
                return "Lollipop";

            case 22:
                return "Lollipop";

            case 23:
                return "Marshmallow";

            case 24:
                return "Nougat";

            case 25:
                return "Nougat";

            case 26:
                return "Oreo";

            case 27:
                return "Oreo";

            case 28:
                return "Pie";

            default:
                return null;
        }
    }
}