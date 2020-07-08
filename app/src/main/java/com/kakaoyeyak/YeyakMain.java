package com.kakaoyeyak;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.MessageSendResponse;
import com.kakao.kakaotalk.response.model.MessageFailureInfo;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

//예약 메시지 전송 액티비티
public class YeyakMain extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pendingIntent;
    EditText katok_msg; // 에딧 텍스트 창에 메시지 입력

    ArrayList<String> Hour = new ArrayList<String>();
    ArrayList<String> Minute = new ArrayList<String>();
    ArrayList<String> Id = new ArrayList<String>(); // User UID
    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<String> Message = new ArrayList<String>();

    ManagePref managePref = new ManagePref();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeyak_main);

        // 전 페이지에서 선택한 유저들 정보 intent에 담아 보낸걸 여기서 받음.
        // 어뎁터프렌드-putExtra -> 예약메인-getExtra
        Intent intent = getIntent(); /*데이터 수신*/
        final String UID = intent.getExtras().getString("UserUID");
        final String ID = intent.getExtras().getString("UserID");
        final String NName = intent.getExtras().getString("UserName");

        // 로그로 선택된 사람 확인해보라고 남겨놨음.
        // I/보낼 사람 UID,ID,Name:: 4dPm0ObT49ThzfrD8MDzy_rK5tLn1uXS4JI/1395494438/안승기
        Log.i("보낼 사람 UID,ID,Name: ",UID+"/"+ID+"/"+NName);

        this.context = this;

        //시작 시 정보 받아옴
        //저장된 배열에 추가로 저장을 하고 다시 저장할거여서
        Hour = managePref.getStringArrayPref(this,"hour");
        Minute = managePref.getStringArrayPref(this,"minute");
        Id = managePref.getStringArrayPref(this,"id");
        Name = managePref.getStringArrayPref(this,"name");
        Message = managePref.getStringArrayPref(this,"message");

        // 자꾸 데이터 쌓여서 클리어시킴 여기서

        Hour.clear();
        Minute.clear();
        Id.clear();
        Name.clear();
        Message.clear();



        // 현재까지 저장된 넘들 확인해봄.
        Log.e("Hour",Hour.toString());
        Log.e("Minute",Minute.toString());
        Log.e("UUID",Id.toString());
        Log.e("Name",Name.toString());
        Log.e("Msg",Message.toString());

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = findViewById(R.id.time_picker);

        final Calendar calendar = Calendar.getInstance();
        final Intent my_intent = new Intent(this.context, Alarm_Reciver.class);
        katok_msg = (EditText) findViewById(R.id.katok_msg);

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
                Id.add(UID); // 선택한 유저 UID 저장함
                Name.add(NName); // 선택한 유저 닉네임 저장함
                Message.add(katok_msg.getText().toString()); // 보낼 메세지 저장함

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

                // 서비스가 끝나니까 종료되는 현상이 있음. 오류안뜨고 그냥 꺼짐.
                /* I/com.kakaoyeyak: Thread[3,tid=8506,WaitingInMainSignalCatcherLoop,Thread*=0x71c1616400,peer=0x12fc0380,"Signal Catcher"]: reacting to signal 3
                    I/com.kakaoyeyak: WaitForGcToComplete blocked NativeAlloc on ClassLinker for 6.169ms
                    I/com.kakaoyeyak: Wrote stack traces to '[tombstoned]'
                */
            }
        });
    }
}