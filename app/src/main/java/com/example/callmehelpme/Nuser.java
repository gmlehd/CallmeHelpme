package com.example.callmehelpme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Nuser extends AppCompatActivity {

    ImageButton goback;
    EditText secession_edittxt;
    ImageButton secessionbtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_user);

        goback = (ImageButton)findViewById(R.id.goback);
        secession_edittxt = (EditText)findViewById(R.id.secession_edittxt);
        secessionbtn = (ImageButton)findViewById(R.id.secessionbtn);

        secessionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secession();
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Nuser.this, Nservice.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    public void secession(){
        final DatabaseReference databaseReference = firebaseDatabase.getInstance().getReference().child("users");
        Log.i(this.getClass().getName(),"setcallst 입성!");
        ValueEventListener Listener0 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String pwd =  dataSnapshot.child(DLoginActivity.loginID).child("password").getValue().toString();
                if (!pwd.equals(secession_edittxt.getText().toString())){
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();

                }
                else{
                    dataSnapshot.child(DLoginActivity.loginID).getRef().removeValue();
                    Toast.makeText(getApplicationContext(),"회원탈퇴 되었습니다.",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Nuser.this, DLoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(this.getClass().getName(),"onCancelled");
            }
        };
        databaseReference.addListenerForSingleValueEvent(Listener0);
    }
}
