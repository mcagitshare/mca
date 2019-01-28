package com.project.xr.contactsync.Class;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    public static int counter;
    public static String email;
    public static String  Login = "login", DeviceData = "device_data";
    public static String IMEI = "IMEI", MSISDN = "MSISDN", MAC_id = "MAC_id", country = "country";
    public static String device_type = "device_type", device_serial_no = "device_serial_no", device_os_type = "device_os_type", device_os_version = "device_os_version";

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void printLog(String receiver, String daily_task) {
        Log.e(receiver, daily_task);
    }

    public static void setDailyTask(Context context) {
    }

    public static void storeRequestPayloadData(Context context, String key, String  value) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("messages", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static String getRequestPayloadData(Context context, String key) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("messages", Context.MODE_PRIVATE);
            if (sharedPreferences.contains(key)) {
                return sharedPreferences.getString(key, null);
            }
        }
        return null;
    }

    public static void storeInPrefs(Context context, String key, boolean  value) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("messages", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public static boolean getInPrefs(Context context, String key) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("messages", Context.MODE_PRIVATE);
            if (sharedPreferences.contains(key)) {
                return sharedPreferences.getBoolean(key, false);
            }
        }
        return false;
    }
}