package com.example.callmehelpme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class DLoginActivity extends AppCompatActivity {

    ImageButton Join,Login;
    public static EditText login_phonenumber,login_password;
    public DatabaseReference reference;
    private static final String TAG = "DLoginActivity";
    public static String loginID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_login);

        Join = (ImageButton)findViewById(R.id.join);
        Login = (ImageButton)findViewById(R.id.login);
        login_phonenumber = (EditText)findViewById(R.id.login_phonenumber);
        login_password = (EditText)findViewById(R.id.login_password);
        reference = FirebaseDatabase.getInstance().getReference("users");

        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // -activity_d_join으로 이동-
                Intent myintent = new Intent(DLoginActivity.this, DJoinActivity.class);
                startActivity(myintent);
//                finish();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // -DB 데이터와 비교하여 로그인-
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // flag는 해당 번호를 찾았을때 회원가입절차를 무시하기 위해 사용
                        boolean flag = true;
                        Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                        while(child.hasNext()){
                            DataSnapshot data = child.next();
                            String pn = data.getKey();
                            Log.d(TAG,"로그pn"+pn + " " + login_phonenumber.getText().toString());
                            Log.d(TAG,"로그pw"+ data.child("password").getValue().toString() + " " + login_password.getText().toString());

                            // -DB의 phonenumber와 password를 EditText로 받은 phonenumber와 password를 비교
                            if(pn.equals(login_phonenumber.getText().toString()) && data.child("password").getValue().toString().equals(login_password.getText().toString())){
                                Toast.makeText(getApplicationContext(),"로그인이 되었습니다",Toast.LENGTH_LONG).show();
                                loginID = login_phonenumber.getText().toString();
                                //로그인이 되면 activity_d_sildmenu로 이동
                                Intent intent = new Intent(DLoginActivity.this, Dslidmenu.class);
                                startActivity(intent);
                                flag = false;
                                break;
                            }
                        }
                        // 번호를 찾지 못한 경우 Toast메시지를 띄우고 회원가입 페이지로 이동
                        if(flag){
                            Toast.makeText(getApplicationContext(),"존재하지 않는 번호입니다. 회원가입이 필요합니다",Toast.LENGTH_LONG).show();
                            Intent intent_i = new Intent(DLoginActivity.this, DJoinActivity.class);
                            startActivity(intent_i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}
