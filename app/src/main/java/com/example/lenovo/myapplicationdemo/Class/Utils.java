package com.example.lenovo.myapplicationdemo.Class;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.nio.charset.StandardCharsets;

public class Utils {

    public static int counter;
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static byte[] getEncryptKey(){
        String keyName = "1234567890abcd1234567890abcd1234567890abcd1234567890abcd12345678";
        return keyName.getBytes(StandardCharsets.UTF_8);
    }
}