package com.mca.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mca.Utils.Utils;
import com.mca.Utils.Constants;
import com.mca.R;
import com.mca.xmpp.GetXmppConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

public class OtpActivity extends AppCompatActivity {

    Button btn_next;
    EditText et_otp;
    TextView tv_otp;

    String email_id;
    String IMEI, MSISDN, MAC_id, country;
    String device_type, device_serial_no, device_os_type, device_os_version;
    int responseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        if (!Utils.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, NoInternetConnection.class);
            startActivityForResult(intent, 1);
            finish();
        }

        findViewId();

        Intent intent = getIntent();
        email_id = intent.getStringExtra("email_id");

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
                if (!Utils.isNetworkAvailable(OtpActivity.this)) {
                    Intent intent = new Intent(OtpActivity.this, NoInternetConnection.class);
                    startActivityForResult(intent, 1);
                }
                OtpActivity.AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
            }
        }
    }//onActivityResult

    private void findViewId() {
        btn_next = findViewById(R.id.otp_next);
        et_otp = findViewById(R.id.et_otp);
        tv_otp = findViewById(R.id.tv_otp_resolve);
    }

    @Override
    protected void onResume() {
        if (!Utils.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, NoInternetConnection.class);
            startActivityForResult(intent, 1);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @SuppressLint("WrongThread")
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
                json.put("otp", et_otp.getText().toString());

                JSONObject json_device_properties = new JSONObject();
                json_device_properties.put("msisdn", "911234567890");
                json_device_properties.put("imei", IMEI);
                json_device_properties.put("mac_id", MAC_id);
                json_device_properties.put("country", "India");
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

            String message = "", resolve = "";

            try {
                JSONObject job = new JSONObject(result);

                if (responseCode == 200) {
                    Utils.storeRequestPayloadData(OtpActivity.this, Utils.regid, job.getString("regid"));
                    Utils.storeRequestPayloadData(OtpActivity.this, Utils.crc, job.getString("crc"));
                    Utils.storeRequestPayloadData(OtpActivity.this, Utils.messageserver, job.getString("messageserver"));
                    Utils.storeRequestPayloadData(OtpActivity.this, Utils.messageserverport, job.getString("messageserverport"));

                    AsyncTaskRunner1 runner = new AsyncTaskRunner1();
                    runner.execute();

                } else {
                    message = job.getString("message");
                    resolve = job.getString("resolve");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (et_otp.length() != 0) {
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Utils.storeInPrefs(OtpActivity.this, Utils.Login, true);

                    Intent mainIntent = new Intent(OtpActivity.this, MainActivity.class);
                    mainIntent.putExtra("email_id", email_id);
                    Toast.makeText(OtpActivity.this, "Registration completed successfully", Toast.LENGTH_SHORT).show();
                    startActivity(mainIntent);
                    finish();
                } else {
                    tv_otp.setText(message + "\n" + resolve + "");
                }
            } else {
                Toast.makeText(OtpActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(OtpActivity.this,
                    "Please Wait...", "Hold on! We are Verifying your OTP");
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

    private class AsyncTaskRunner1 extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            XMPPConnection connection = GetXmppConnection.getConnection
                    (
                            Utils.getRequestPayloadData(OtpActivity.this, Utils.messageserver),
                            Integer.parseInt(Utils.getRequestPayloadData(OtpActivity.this, Utils.messageserverport))
                    );
            try {
                ((XMPPTCPConnection) connection).login
                        (
                                Utils.getRequestPayloadData(OtpActivity.this, Utils.regid),
                                Utils.getRequestPayloadData(OtpActivity.this, Utils.crc)
                        );
                Utils.printLog("XMPP connection", connection.isConnected() + "");

            } catch (XMPPException
                    | SmackException
                    | IOException
                    | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
