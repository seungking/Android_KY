package com.kakaoyeyak;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.MessageSendResponse;
import com.kakao.kakaotalk.response.model.MessageFailureInfo;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakaoyeyak.ui.DatePicker;
import com.kakaoyeyak.ui.TimePicker;

public class YeyakMain extends AppCompatActivity {

    public static int broadcastCode = 0; // 전역변수로 만들어서 다중 메세지 알람 가능케함.

    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private PendingIntent pendingIntent;

    public EditText katok_msg; // 에딧 텍스트 창에 메시지 입력

    ArrayList<String> B_id = new ArrayList<String>(); // broadcastCode 저장.
    ArrayList<String> Time = new ArrayList<String>();
    ArrayList<String> Id = new ArrayList<String>();
    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<String> Message = new ArrayList<String>();
    ArrayList<String> Profile = new ArrayList<String>();

    ManagePref managePref = new ManagePref();

    //
    public Intent intent;
    public String UID;
    public String ID;
    public String NName;
    public String ProfileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeyak_main);

        // 전 페이지에서 선택한 유저들 정보 intent에 담아 보낸걸 여기서 받음.
        // 어뎁터프렌드-putExtra -> 예약메인-getExtra
        intent = getIntent(); // 데이터 수신
//        UID = intent.getExtras().getString("UserUID");
//        ID = intent.getExtras().getString("UserID");
//        NName = intent.getExtras().getString("UserName");
//        ProfileUri = intent.getExtras().getString("ProfileUri");

        // 로그로 선택된 사람 확인해보라고 남겨놨음.
        // I/보낼 사람 UID,ID,Name:: 4dPm0ObT49ThzfrD8MDzy_rK5tLn1uXS4JI/1395494438/안승기
        Log.i("보낼 사람 UID,ID,Name: ",UID+"/"+ID+"/"+NName+"/"+ProfileUri);

        this.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        this.datePicker = findViewById(R.id.date_picker);
        this.timePicker = findViewById(R.id.time_picker);

        this.katok_msg = (EditText) findViewById(R.id.katok_msg);

        findViewById(R.id.btn_start).setOnClickListener(mClickListener);
        findViewById(R.id.btn_finish).setOnClickListener(mClickListener);
//        findViewById(R.id.btn_clear).setOnClickListener(mClickListener);

        //시작 시 정보 받아옴
        //저장된 배열에 추가로 저장을 하고 다시 저장할거여서
        B_id = managePref.getStringArrayPref(this,"BroadCastID");
        Time = managePref.getStringArrayPref(this,"time");
        Id = managePref.getStringArrayPref(this,"id");
        Name = managePref.getStringArrayPref(this,"name");
        Message = managePref.getStringArrayPref(this,"message");
        Profile = managePref.getStringArrayPref(this,"profile");

        // 현재까지 저장된 넘들 확인해봄.
        Log.e("BroadCastID",B_id.toString());
        Log.e("Time", Time.toString());
        Log.e("UUID",Id.toString());
        Log.e("Name",Name.toString());
        Log.e("Msg",Message.toString());
        Log.e("Profile",Profile.toString());

    }

    /* 알람 시작 */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void start() {

        // 시간 설정

        Calendar calendar = Calendar.getInstance();
        calendar = this.datePicker.getCalendar();
        //calendar.set(Calendar.MONTH, this.datePicker.getMonth());
        //calendar.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, this.timePicker.getHour());
        calendar.set(Calendar.MINUTE, this.timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);

        // 현재시간보다 이전이면
        if (calendar.before(Calendar.getInstance())) {
            // 다음날로 설정
            calendar.add(Calendar.DATE, 1);
        }

        String strTime = "";
        strTime = (this.datePicker.getCalendar().toString());
        //strTime = (this.datePicker.getMonth()+1 > 10) ? strTime + String.valueOf(this.datePicker.getMonth()+1): strTime + "0" + String.valueOf(this.datePicker.getMonth()+1);
        //strTime = (this.datePicker.getDayOfMonth() > 10) ? strTime + String.valueOf(this.datePicker.getDayOfMonth()): strTime + "0" + String.valueOf(this.datePicker.getDayOfMonth());
        strTime = (this.timePicker.getHour() > 10) ? strTime + String.valueOf(this.timePicker.getHour()): strTime + "0" + String.valueOf(this.timePicker.getHour());
        strTime = (this.timePicker.getMinute() > 10) ? strTime + String.valueOf(this.timePicker.getMinute()): strTime + "0" + String.valueOf(this.timePicker.getMinute());
        Log.d("log1","strTime : " + strTime);

        //사용자에게 값 받은
        //따로 레이아웃 추가하고 값 설정 필요
        B_id.add(String.valueOf(broadcastCode));
        Time.add(strTime);
        Id.add(UID); // 선택한 유저 UID 저장함
        Name.add(NName); // 선택한 유저 닉네임 저장함
        Message.add(katok_msg.getText().toString()); // 보낼 메세지 저장함
        Profile.add(ProfileUri);

        //로컬에 업데이트
        managePref.setStringArrayPref(YeyakMain.this,"BroadCastID",B_id);
        managePref.setStringArrayPref(YeyakMain.this,"time",Time);
        managePref.setStringArrayPref(YeyakMain.this,"id",Id);
        managePref.setStringArrayPref(YeyakMain.this,"name",Name);
        managePref.setStringArrayPref(YeyakMain.this,"message",Message);
        managePref.setStringArrayPref(YeyakMain.this,"profile",Profile);

        // Receiver 설정
        Intent intent = new Intent(this, Alarm_Receiver.class);
        // state 값이 on 이면 알람시작, off 이면 중지
        intent.putExtra("state", "on");

        this.pendingIntent = PendingIntent.getBroadcast(this, broadcastCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 알람 설정
//        this.alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),0, pendingIntent);
        }

        // Toast 보여주기 (알람 시간 표시)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Toast.makeText(this, "Alarm : " + format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();

        startActivity(new Intent(YeyakMain.this,MainActivity.class));
        finish();
    }

    /* 알람 중지 */
    private void stop() {
        if (this.pendingIntent == null) {
            return;
        }

        // 알람 취소
        this.alarmManager.cancel(this.pendingIntent);

        // 알람 중지 Broadcast
        Intent intent = new Intent(this, Alarm_Receiver.class);
        intent.putExtra("state","off");

        sendBroadcast(intent);

        this.pendingIntent = null;

        startActivity(new Intent(YeyakMain.this,MainActivity.class));
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start:
                    // 메세지 알람 시작
                    start();

                    // 여기다가 wait() 같은걸로 1초 뒤에 머 꺼지도록 따로 종료버튼 안눌러도 되게 해야할듯.
                    // 아니면 알아서 꺼지게 한다던가.
                    // stop();

                    break;
                case R.id.btn_finish:
                    // 알람 중지
                    stop();

                    break;

//                case R.id.btn_clear:
//
//                    //clearing();
//                    // 데이터 쌓이면 클리어.
//                    B_id.clear();
//                    Time.clear();
//                    Id.clear();
//                    Name.clear();
//                    Message.clear();
//                    //로컬에 업데이트
//                    managePref.setStringArrayPref(YeyakMain.this,"BroadCastID",B_id);
//                    managePref.setStringArrayPref(YeyakMain.this,"time",Time);
//                    managePref.setStringArrayPref(YeyakMain.this,"id",Id);
//                    managePref.setStringArrayPref(YeyakMain.this,"name",Name);
//                    managePref.setStringArrayPref(YeyakMain.this,"message",Message);
//                    managePref.setStringArrayPref(YeyakMain.this,"profile",Profile);
//                    // 현재까지 저장된 넘들 확인해봄.
//                    Log.e("BroadCastID",B_id.toString());
//                    Log.e("Time", Time.toString());
//                    Log.e("UUID",Id.toString());
//                    Log.e("Name",Name.toString());
//                    Log.e("Msg",Message.toString());
//                    Log.e("Profile",Profile.toString());
//
//                    break;
            }
        }
    };
}