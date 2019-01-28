package com.project.xr.contactsync.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.xr.contactsync.Class.Utils;
import com.project.xr.contactsync.Constants;
import com.project.xr.contactsync.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class SignInActivity extends AppCompatActivity {

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_WIFI_STATE
    };

    private static final String TODO = "TODO";
    String IMEI, MSISDN, MAC_id, country;
    String device_type, device_serial_no, device_os_type, device_os_version;
    String message = "failed", resolve;

    Intent intent = null;
    int responseCode;

    Button btn_next;
    EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if (Utils.getInPrefs(this, Utils.Login)){
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        findViewId();

        IMEI = Utils.getRequestPayloadData(this, Utils.IMEI);
        MSISDN = Utils.getRequestPayloadData(this, Utils.MSISDN);
        MAC_id = Utils.getRequestPayloadData(this, Utils.MAC_id);
        country = Utils.getRequestPayloadData(this, Utils.country);
        device_type = Utils.getRequestPayloadData(this, Utils.device_type);
        device_serial_no = Utils.getRequestPayloadData(this, Utils.device_serial_no);
        device_os_type = Utils.getRequestPayloadData(this, Utils.device_os_type);
        device_os_version = Utils.getRequestPayloadData(this, Utils.device_os_version);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isValidEmail(EmailID)) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
//                } else
//                    Toast.makeText(SignInActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findViewId() {
        btn_next = findViewById(R.id.sign_in_next);
        et_email = findViewById(R.id.et_email);
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(Constants.urlRegister);

                httpPost.setHeader("Authorization", " Basic YWRtaW46MTIzNDU=");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Accept", "application/json");

                JSONObject json = new JSONObject();
                json.put("email", et_email.getText().toString());
                json.put("name", "hiren");

                JSONObject json_device_properties = new JSONObject();
                json_device_properties.put("msisdn", "ADH0068");
                json_device_properties.put("imei", IMEI);
                json_device_properties.put("mac_id", MAC_id);
                json_device_properties.put("country", country);
                json_device_properties.put("devicetype", device_type);
                json_device_properties.put("deviceserialno", device_serial_no);
                json_device_properties.put("deviceostype", device_os_type);
                json_device_properties.put("deviceosversion", device_os_version);

                json.put("device_properties", json_device_properties);
                Log.e("json", json.toString());
                httpPost.setEntity(new StringEntity(json.toString(), "UTF-8"));

                HttpResponse response = httpclient.execute(httpPost);
                responseCode = response.getStatusLine().getStatusCode();

                result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();

            try {
                JSONObject job = new JSONObject(result);
                message = job.getString("message");
                resolve = job.getString("resolve");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (et_email.length() == 0) {
                Toast.makeText(SignInActivity.this, "Please enter Email Address", Toast.LENGTH_SHORT).show();
            } else {
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    intent = new Intent(SignInActivity.this, OtpActivity.class);
                    intent.putExtra("email_id", et_email.getText().toString());
                    startActivity(intent);
                    finish();
                } else if (responseCode == HttpsURLConnection.HTTP_PAYMENT_REQUIRED) {
                    intent = new Intent(SignInActivity.this, AlreadySigninActivity.class);
                    intent.putExtra("email_id", et_email.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }/* else {
                Toast.makeText(SignInActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show();
            }*/
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(
                    SignInActivity.this,
                    "ProgressDialog", "Hold on! We are Verifying your Email Address");
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}