package com.mca.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mca.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetials extends AppCompatActivity {

    CircleImageView image;
    TextView name, phone, id;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        Intent intent = getIntent();

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle(intent.getStringExtra("name"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        image = findViewById(R.id.contact);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        id = findViewById(R.id.id);

        name.setText(intent.getStringExtra("name"));
        phone.setText(intent.getStringExtra("phone"));
        id.setText(intent.getStringExtra("id"));
        Glide.with(this)
                .load(intent.getStringExtra("image"))
                .apply(new RequestOptions().placeholder(R.mipmap.contact).error(R.mipmap.contact)).into(image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}