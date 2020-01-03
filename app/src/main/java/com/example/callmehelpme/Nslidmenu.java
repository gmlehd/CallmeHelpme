package com.example.callmehelpme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Nslidmenu extends AppCompatActivity implements SensorEventListener {

    private boolean FlashOn=false;
    private Camera camera = null;
    private CheckBox chboxman;
    private CheckBox chboxwoman;
    private ImageButton callbtn;
    private ImageButton siren;
    private ImageButton flash;
    private CheckBox callstate;
    private ImageButton dayver;
    private ImageButton user;
    private ImageButton staff;
    private SoundPool sound_pool;
    static boolean finduser;
    static int randomIndex;
    static String path;
    static int size;
    static boolean selected_man = false;
    static boolean selected_woman = false;

    String sex = "";

    // 가속도 센서
    private long lastTime;
    private float lastX;
    private float lastY;
    private float lastZ;
    private static final int SHAKE_THRESHOLD = 3700;
    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> fid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_slidmenu);

        databaseReference = firebaseDatabase.getInstance().getReference().child("users");

        chboxman = (CheckBox) findViewById(R.id.chboxman);
        chboxwoman = (CheckBox) findViewById(R.id.chboxwoman);
        callbtn = (ImageButton)findViewById(R.id.callbtn);
        siren = (ImageButton)findViewById(R.id.siren);
        flash = (ImageButton) findViewById(R.id.flash);
        callstate = (CheckBox)findViewById(R.id.callstate);
        dayver = (ImageButton)findViewById(R.id.dayver);
        user = (ImageButton) findViewById(R.id.user);
        staff = (ImageButton) findViewById(R.id.staff);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerormeterSensor, sensorManager.SENSOR_DELAY_NORMAL);

        ValueEventListener Listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(this.getClass().getName(),"리스너 입성!");
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while(child.hasNext()){
                    String key = child.next().getKey();
                    Log.i(this.getClass().getName(),"Key: "+key);
                    fid.add(key);
                }
                size = fid.size();
                Log.i(this.getClass().getName(),"size: "+size);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(this.getClass().getName(),"리스너 입성!");
            }
        };

        databaseReference.addListenerForSingleValueEvent(Listener);

        setcallst();



        callstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callst();
            }
        });

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean selected_man = false;
//                boolean selected_woman = false;
                finduser = false;

//                Log.i(this.getClass().getName(),"버튼 클릭");

                // 성별 선택 체크박스
                if(chboxman.isChecked()){
                    selected_man = true;
                }
                if(chboxwoman.isChecked()){
                    selected_woman=true;
                }
//                Log.i(this.getClass().getName(),"selected_man == "+selected_man+" selected_woman == "+selected_woman);
                if (selected_man==false && selected_woman == false){
                    Toast.makeText(getApplicationContext(),"전화할 성별을 선택해주세요!", Toast.LENGTH_LONG).show();
                }
                else{
                    FindUser();
                }
//                Log.i(this.getClass().getName(),"키힝 finduser: "+finduser);
//                Log.i(this.getClass().getName(),"메소드 탈출");

            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Nslidmenu.this, Nservice.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        dayver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Nslidmenu.this, Dslidmenu.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        //후레시맨~!~!~!~!
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FlashOn) {
                    camera.release();
                    FlashOn = false;
                } else {
                    camera = Camera.open();
                    Camera.Parameters mCameraParameters = camera.getParameters();
                    mCameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
                    camera.setParameters(mCameraParameters);
                    camera.startPreview();
                    FlashOn = true;
                }
            }
        });

        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/sa3HsJPb"));
                startActivity(intent);
            }
        });

        sound_pool = new SoundPool(5, AudioManager.STREAM_ALARM, 0);
        final int sound_beep_alert    = sound_pool.load(this, R.raw.siren, 1);

        siren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sound_pool.play(sound_beep_alert, 1f, 1f, 0, 0, 1f);
            }
        });
    }

    public void setcallst(){
        final DatabaseReference databaseReference = firebaseDatabase.getInstance().getReference().child("users");
        Log.i(this.getClass().getName(),"setcallst 입성!");
        ValueEventListener Listener0 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean callst = (boolean)dataSnapshot.child(DLoginActivity.loginID).child("call").getValue();
                Log.i(this.getClass().getName(), "통화 상태: "+callst);

                if (!callst){
                    callstate.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(this.getClass().getName(),"onCancelled");
            }
        };
        databaseReference.addListenerForSingleValueEvent(Listener0);
    }

    public void callst(){
        final DatabaseReference databaseReference = firebaseDatabase.getInstance().getReference().child("users");
        Log.i(this.getClass().getName(),"callst 입성!");
        ValueEventListener Listener0 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean callst = (boolean)dataSnapshot.child(DLoginActivity.loginID).child("call").getValue();
                Log.i(this.getClass().getName(), "통화 상태: "+callst);

                if (callstate.isChecked()){
                    Map<String, Object> taskMap = new HashMap<String, Object>();

                    taskMap.put(DLoginActivity.loginID+"/call",false);
                    databaseReference.updateChildren(taskMap);
                }
                else {
                    Map<String, Object> taskMap = new HashMap<String, Object>();

                    taskMap.put(DLoginActivity.loginID+"/call",true);
                    databaseReference.updateChildren(taskMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(this.getClass().getName(),"onCancelled");
            }
        };
        databaseReference.addListenerForSingleValueEvent(Listener0);
    }

    public void FindUser(){
        final DatabaseReference databaseReference = firebaseDatabase.getInstance().getReference().child("users");
//        Log.i(this.getClass().getName(),"FindUser 입성!");
        ValueEventListener Listener0 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                finduser = false;
                while(finduser==false){
                    randomIndex = (int) (Math.random() * size);
//                    Log.i(this.getClass().getName(),"키힝 randomIndex: "+randomIndex);
                    path = fid.get(randomIndex).toString();
//                    Log.i(this.getClass().getName(),"키힝 path: "+path);
//                Log.i(this.getClass().getName(), "오잉 path : " + path);
                    String str = dataSnapshot.child(path).child("sex").getValue().toString();
                    Boolean callstate = (boolean) dataSnapshot.child(path).child("call").getValue();
//                    Log.i(this.getClass().getName(), "오잉 str: " + str);
//                    Log.i(this.getClass().getName(), "callstate: "+callstate);
//                    Log.i(this.getClass().getName(),"오잉 selected_man: "+selected_man+" selected_woman: "+selected_woman);

                    if (callstate==true){
//                        Log.i(this.getClass().getName(),"if문 입성!");
                        if (str.equals("Man") && selected_man ){
//                            Log.i(this.getClass().getName(),"오잉 str == man and selected_man==ture");
                            finduser=true;
                        }
                        else if (str.equals("Woman") && selected_woman){
//                            Log.i(this.getClass().getName(),"오잉 str == woman and selected_woman==ture");
                            finduser=true;
                        }
                    }
                }

//                Log.i(this.getClass().getName(),"오잉 finduser: " + finduser);
                if (finduser==true){
//                    Log.i(this.getClass().getName(),"전화할 if문 입성!");
                    String tel = "tel:"+path;
//                    Log.i(this.getClass().getName(),tel);
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.i(this.getClass().getName(),"onCancelled");
            }
        };
        databaseReference.addListenerForSingleValueEvent(Listener0);
    }

    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis(); // 현재시간
            if((curTime - lastTime) > 100) {
                long diffTime = (curTime - lastTime);
                lastTime = curTime;

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    //지정된 수치이상 흔들림이 있으면 실행
                    Toast.makeText(getApplicationContext(),"흔들림흔들림흔들림",Toast.LENGTH_LONG).show();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("신고하시겠습니까?").setMessage("선택하세요!");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
                            Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"));
                            startActivity(tt);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else if (speed < 10) {
//                    Toast.makeText(getApplicationContext(),"노흔들",Toast.LENGTH_LONG).show();
                }

                //갱신
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerormeterSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
