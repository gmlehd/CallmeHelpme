package com.example.callmehelpme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Collection;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DJoinActivity extends AppCompatActivity {

    ImageButton joinok;
    EditText phonenumber,password,passwordcheck;
    RadioGroup rg_sex;
    RadioButton radioButton;
    ArrayList<Integer> fid = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_join);

        joinok=(ImageButton) findViewById(R.id.joinok);
        phonenumber=(EditText)findViewById(R.id.signup_phonenumber);
        password=(EditText)findViewById(R.id.signup_password);
        passwordcheck=(EditText)findViewById(R.id.signup_password_2);
        rg_sex=(RadioGroup)findViewById(R.id.rg_sex);

//        // -DB 데이터에 순차적으로 ID값 부여-
//        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
////                    Log.i(this.getClass().getName(),"포문 입성!");
//                    String uid = snapshot.child("ID").getValue().toString();
//                    fid.add(Integer.parseInt(uid));
//                }
//                int size = fid.size();
//                if(size>0){
//                    int max = fid.get(0);
//
//                    for (int i = 0; i < size; i++){
//                        if(max<fid.get(i)){
//                            max = fid.get(i);
//                        }
//                    }
//                    ID = max+1;
////                    Log.i(this.getClass().getName(), "ID: "+ID);
//                }
//                else{
//                    ID = 1;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        // -성별 라디오 버튼그룹-
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                radioButton=(RadioButton)radioGroup.findViewById(checked);
                if(null!=radioButton){
                }
            }
        });

        // -가입하기 버튼 클릭 시 회원가입완료-
        joinok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // -비밀번호 확인란과 일치하면 가입완료-
                if(password.getText().toString().equals(passwordcheck.getText().toString())) {

                    String sex=null;
                    int rb = ((RadioGroup) rg_sex.findViewById(R.id.rg_sex)).getCheckedRadioButtonId();
                    switch (rb){
                        case R.id.radioman:{
                            sex="Man";
                            break;
                        }
                        case R.id.radiowoman:{
                            sex="Woman";
                            break;
                        }
                    }

                    // -firebase에 데이터 저장(회원가입)-
                    UserFireBase userData=new UserFireBase(password.getText().toString(),sex,true);
                    userData.insertData(phonenumber.getText().toString());

                    //회원가입 완료 토스트 메시지
                    Toast.makeText(getApplicationContext(),"회원가입 완료",Toast.LENGTH_LONG).show();

                    // -activity_d_login으로 이동
                    Intent myintent = new Intent(DJoinActivity.this, DLoginActivity.class);
                    startActivity(myintent);
                } else {
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다",Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}
