package com.example.callmehelpme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Nservice extends AppCompatActivity {

    ImageButton withdrawal;
    ImageButton inquiry;
    ImageButton goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_service);

        withdrawal = (ImageButton)findViewById(R.id.withdrawal);
        inquiry = (ImageButton)findViewById(R.id.inquiry);
        goback = (ImageButton)findViewById(R.id.goback);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Nservice.this, Nslidmenu.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Nservice.this, Nuser.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/sa3HsJPb"));
                startActivity(intent);
            }
        });
    }
}
