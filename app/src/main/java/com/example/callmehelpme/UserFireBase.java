package com.example.callmehelpme;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFireBase {
    public DatabaseReference reference;

    public String password;
    public String sex;
    public boolean call;

    public UserFireBase(String password,String sex,boolean call){
        this.password=password;
        this.sex=sex;
        this.call=call;
    }

    public void insertData(String phonenumber){
        UserFireBase userData=new UserFireBase(password,sex,call);
        reference= FirebaseDatabase.getInstance().getReference();
//        String key=reference.child("users").push().getKey();
        reference.child("users").child(phonenumber).setValue(userData);
    }
}
