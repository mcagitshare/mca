package com.example.lenovo.myapplicationdemo.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.birbit.android.jobqueue.JobManager;
import com.example.lenovo.myapplicationdemo.Class.Utils;
import com.example.lenovo.myapplicationdemo.Interface.ApiInterface;
import com.example.lenovo.myapplicationdemo.Interface.Block;
import com.example.lenovo.myapplicationdemo.Interface.JsonObjectBody;
import com.example.lenovo.myapplicationdemo.Job.ContactJobInsert;
import com.example.lenovo.myapplicationdemo.Job.JsonJobInsert;
import com.example.lenovo.myapplicationdemo.Model.Item;
import com.example.lenovo.myapplicationdemo.R;
import com.example.lenovo.myapplicationdemo.Realm.DemoApplication;
import com.example.lenovo.myapplicationdemo.Realm.RealmController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String JSON_URL = "http://www.meclubapp.com/public/gw/contact/";
    Button btn_contact, btn_demo_contact, btn_clear_list;
    RealmController realmController;
    JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        btn_contact.setOnClickListener(this);
        btn_demo_contact.setOnClickListener(this);
        btn_clear_list.setOnClickListener(this);

    }

    private void getJSONdata() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(4000, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                okhttp3.Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = null;

        retrofit = new Retrofit.Builder().baseUrl(JSON_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ApiInterface apiService = retrofit.create(ApiInterface.class);
        retrofit2.Call<List<Item>> call;

        call = apiService.getSearchCategory(new Block());

        call.enqueue(new retrofit2.Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<List<Item>> call,
                                   @NonNull retrofit2.Response<List<Item>> response) {
               List<Item> items = response.body();

//                jobManager.addJobInBackground(new JsonJobInsert(
//                        id,
//                        name,
//                        image,
//                        phone));
            }

            @Override
            public void onFailure(retrofit2.Call<List<Item>> call, Throwable t) {
            }
        });


//        final String jsonbody = "{  \"Block\": {    \"block_size\": 50,    \"block_id\": 1 }}";
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                //getting the json object of the particular index inside the array
//                                JSONObject job = jsonArray.getJSONObject(i);
//
//                                String id = job.getString("id");
//                                String name = job.getString("name");
//                                String image = job.getString("image");
//                                String phone = job.getString("phone");
//
//                                jobManager.addJobInBackground(new JsonJobInsert(
//                                        id,
//                                        name,
//                                        image,
//                                        phone));
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //Failure Callback
//                    }
//                })
//        {
//            /** Passing some request headers* */
//            @Override
//            public Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("content-type", "application/json");
//                headers.put("cache-control", "no-cache");
//                headers.put("postman-token", "79faaf92-8749-35c7-5b5c-8618d9231d51");
//                return headers;
//            }
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return jsonbody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                return  null;
//            }
//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }
//        };

        //add request to queue
//        queue.add(stringRequest);
    }

    private void initView() {

        jobManager = DemoApplication.getInstance().getJobManager();

        btn_demo_contact = findViewById(R.id.btn_demo_contact);
        btn_contact = findViewById(R.id.btn_contact);
        btn_clear_list = findViewById(R.id.btn_clear_contact);

        realmController = RealmController.with(this);
    }

    public void deleteItem(final String itemId) {
        realmController.deleteItem(itemId);
    }

    public void editItem(final String itemId, final String name, String number, String grade) {
        realmController.editItem(itemId, name, number);
        Utils.hideKeyboard(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmController.destroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_demo_contact:
                final Dialog dialog = new Dialog(MainActivity.this);
                //setting custom layout to dialog
                dialog.setContentView(R.layout.dialog);

                //adding text dynamically
                final EditText txt = (EditText) dialog.findViewById(R.id.et_data);

                //adding button click event
                Button ok = (Button) dialog.findViewById(R.id.btn_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(txt.getText().toString())) {
                            Utils.counter = Integer.parseInt(txt.getText().toString());
                            dialog.dismiss();
                            for (int i = 1; i <= Utils.counter; i++) {
                                jobManager.addJobInBackground(new ContactJobInsert(
                                        "Name" + i,
                                        "" + i));
                            }
                            Intent contactIntent = new Intent(MainActivity.this, ContactListActivity.class);
                            startActivity(contactIntent);
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter at least one number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.btn_contact:
                getJSONdata();
                Intent contactIntent = new Intent(MainActivity.this, ContactListActivity.class);
                startActivity(contactIntent);
                break;

            case R.id.btn_clear_contact:
                Realm realm = null;
                try {
                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.deleteAll();
                        }
                    });
                } catch (Exception ex) {
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
                break;
        }
    }
}