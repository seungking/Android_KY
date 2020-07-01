package com.kakaoyeyak;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

//메시지 보내는 서비스
public class Send_Msg extends Service {

    String id;
    String name;
    String message;

    ArrayList<String> Hour = new ArrayList<String>();
    ArrayList<String> Minute = new ArrayList<String>();
    ArrayList<String> Id = new ArrayList<String>();
    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<String> Message = new ArrayList<String>();

    ManagePref managePref = new ManagePref();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //실행 시 값 받아옴
        Log.d("LOG1","Message - Working!");

        Hour = managePref.getStringArrayPref(this,"hour");
        Minute = managePref.getStringArrayPref(this,"minute");
        Id = managePref.getStringArrayPref(this,"id");
        Name = managePref.getStringArrayPref(this,"name");
        Message = managePref.getStringArrayPref(this,"message");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //여기서 설정한 시간에 맞는 값을 DB에서 찾아서
        //해당 인덱스에 해당하는 정보로 메시지 보내야함
        //현제는 로그 출력 부분
        Log.d("LOG1",Hour.get(0) + "  " + Minute.get(0) + "  " + Id.get(0) + "  " + Name.get(0) + "  " + Message.get(0) + "  ");

        //메시지 보냈으므로 해당 인덱스 제거
        Hour.remove(0);
        Minute.remove(0);
        Id.remove(0);
        Name.remove(0);
        Message.remove(0);

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //종료시에 DB 업데이트
        managePref.setStringArrayPref(this,"hour",Hour);
        managePref.setStringArrayPref(this,"minute",Minute);
        managePref.setStringArrayPref(this,"id",Id);
        managePref.setStringArrayPref(this,"name",Name);
        managePref.setStringArrayPref(this,"message",Message);
    }
}
