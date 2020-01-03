package com.example.callmehelpme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // -앱 첫 시작화면 띄우기-
        Handler hand = new Handler();

        hand.postDelayed(new Runnable() {

            //            2초후 화면 전환
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(MainActivity.this, DLoginActivity.class);
                startActivity(i);
                finish();

            }
        }, 2000);
    }
}
