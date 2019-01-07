package com.project.xr.contactsync.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.xr.contactsync.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetials extends AppCompatActivity {

    CircleImageView image;
    TextView name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        image = findViewById(R.id.contact);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        phone.setText(intent.getStringExtra("phone"));
        Glide.with(this)
                .load(intent.getStringExtra("image"))
                .apply(new RequestOptions().placeholder(R.mipmap.contact).error(R.mipmap.contact)).into(image);

    }
}
