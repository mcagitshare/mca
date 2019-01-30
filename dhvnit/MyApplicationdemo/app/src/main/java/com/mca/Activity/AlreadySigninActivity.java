package com.mca.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mca.R;
import com.mca.Utils.Utils;
import com.mca.Utils.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class AlreadySigninActivity extends AppCompatActivity {

    TextView tv_alredy;
    Button ok, cancel;

    int responseCode;

    String email_id;
    String IMEI, MSISDN, MAC_id, country;
    String device_type, device_serial_no, device_os_type, device_os_version;
    String message = "failed", resolve;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_signin);

        IMEI = Utils.getRequestPayloadData(this, Utils.IMEI);
        MSISDN = Utils.getRequestPayloadData(this, Utils.MSISDN);
        MAC_id = Utils.getRequestPayloadData(this, Utils.MAC_id);
        country = Utils.getRequestPayloadData(this, Utils.country);
        device_type = Utils.getRequestPayloadData(this, Utils.device_type);
        device_serial_no = Utils.getRequestPayloadData(this, Utils.device_serial_no);
        device_os_type = Utils.getRequestPayloadData(this, Utils.device_os_type);
        device_os_version = Utils.getRequestPayloadData(this, Utils.device_os_version);

        Intent intent = getIntent();
        email_id = intent.getStringExtra("email_id");

        ok = findViewById(R.id.btn_ok);
        cancel = findViewById(R.id.btn_cancel);

        tv_alredy = findViewById(R.id.tv_already);
        tv_alredy.setText(intent.getStringExtra("email_id") + "already registered with other device.  Proceed to re-register?");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlreadySigninActivity.AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                json.put("email", email_id);
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

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Intent otpIntent = new Intent(AlreadySigninActivity.this, OtpActivity.class);
                startActivity(otpIntent);
                finish();
            } else if (responseCode == HttpsURLConnection.HTTP_PAYMENT_REQUIRED) {
                Intent alreadySigninINtent = new Intent(AlreadySigninActivity.this, AlreadySigninActivity.class);
                alreadySigninINtent.putExtra("email_id", email_id);
                startActivity(alreadySigninINtent);
                finish();
            } else {
                Toast.makeText(AlreadySigninActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AlreadySigninActivity.this, "ProgressDialog", "Hold on! We are re-registering your Email Address");
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }
}