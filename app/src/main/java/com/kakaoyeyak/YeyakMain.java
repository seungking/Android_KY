package com.kakaoyeyak;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

//예약 메시지 전송 액티비티
public class YeyakMain extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pendingIntent;

    ArrayList<String> Hour = new ArrayList<String>();
    ArrayList<String> Minute = new ArrayList<String>();
    ArrayList<String> Id = new ArrayList<String>();
    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<String> Message = new ArrayList<String>();

    ManagePref managePref = new ManagePref();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeyak_main);

        this.context = this;

        //시작 시 정보 받아옴
        //저장된 배열에 추가로 저장을 하고 다시 저장할거여서
        Hour = managePref.getStringArrayPref(this,"hour");
        Minute = managePref.getStringArrayPref(this,"minute");
        Id = managePref.getStringArrayPref(this,"id");
        Name = managePref.getStringArrayPref(this,"name");
        Message = managePref.getStringArrayPref(this,"message");

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = findViewById(R.id.time_picker);

        final Calendar calendar = Calendar.getInstance();
        final Intent my_intent = new Intent(this.context, Alarm_Reciver.class);

        // 알람 시작 버튼
        Button alarm_on = findViewById(R.id.btn_start);
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                // calendar에 시간 셋팅
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                // 시간 가져옴
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();
                Toast.makeText(YeyakMain.this,"Alarm 예정 " + hour + "시 " + minute + "분",Toast.LENGTH_SHORT).show();

                //사용자에게 값 받은
                //따로 레이아웃 추가하고 값 설정 필요
                Hour.add(String.valueOf(hour));
                Minute.add(String.valueOf(minute));
                Id.add("1");
                Name.add("안승기");
                Message.add("test");

                //로컬에 업데이트
                managePref.setStringArrayPref(YeyakMain.this,"hour",Hour);
                managePref.setStringArrayPref(YeyakMain.this,"minute",Minute);
                managePref.setStringArrayPref(YeyakMain.this,"id",Id);
                managePref.setStringArrayPref(YeyakMain.this,"name",Name);
                managePref.setStringArrayPref(YeyakMain.this,"message",Message);

                //시작 및 서비스 등록
                pendingIntent = PendingIntent.getBroadcast(YeyakMain.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                startActivity(new Intent(YeyakMain.this,HorizontalNtbActivity.class));
                finish();
            }
        });
    }
}