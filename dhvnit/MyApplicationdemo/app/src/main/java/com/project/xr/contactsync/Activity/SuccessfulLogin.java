package com.project.xr.contactsync.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.xr.contactsync.R;

public class SuccessfulLogin extends AppCompatActivity {

    Button btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_login);

        findViewId();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otpIntent = new Intent(SuccessfulLogin.this, MainActivity.class);
                startActivity(otpIntent);
                finish();
            }
        });
    }

    private void findViewId() {
        btn_close = findViewById(R.id.success_close);
    }
}
