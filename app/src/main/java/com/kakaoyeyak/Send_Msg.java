package com.kakaoyeyak;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.MessageSendResponse;
import com.kakao.kakaotalk.response.model.MessageFailureInfo;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;
import static com.kakaoyeyak.MainActivity.adapter;


public class Send_Msg extends Service {

    public String id;
    public String name;
    public String message;

    ArrayList<String> B_id = new ArrayList<String>(); // broadcastCode 저장.
    ArrayList<String> Time = new ArrayList<String>();
    ArrayList<String> Id = new ArrayList<String>();
    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<String> Message = new ArrayList<String>();
    ArrayList<String> Profile = new ArrayList<String>();
    // push 알림 get
    ArrayList<String> arrSetNoti = new ArrayList<String>();
    ArrayList<String> arrSetSound = new ArrayList<String>();
    ArrayList<String> arrSetVibrate = new ArrayList<String>();

    ManagePref managePref = new ManagePref();

    // 상단바 푸시 알람.
    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    private boolean isRunning;
    private boolean isPushOn = true; // 푸시 알림 off: false / on: true
    private boolean isSoundOn = true; // 소리
    private boolean isVibrateOn = true; // 진동

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // android.app.RemoteServiceException: Context.startForegroundService() 오류
        // 서비스가 stop되고 재실행되면 5초 이내로 startforeground를 해주어야 함.
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_service";
            String CHANNEL_NAME = "My Background Service";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,NotificationManager.IMPORTANCE_NONE);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setCategory(Notification.CATEGORY_SERVICE).setPriority(PRIORITY_MIN).build();

            startForeground(101, notification);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);

        // 데이터 복구
        Log.d("LOG1","처음으로 알람 서비스가 실행되었습니다. 로컬 DB 연결 시작");

        B_id = managePref.getStringArrayPref(this,"BroadCastID");
        Time = managePref.getStringArrayPref(this,"time");
        Id = managePref.getStringArrayPref(this,"id");
        Name = managePref.getStringArrayPref(this,"name");
        Message = managePref.getStringArrayPref(this,"message");
        Profile = managePref.getStringArrayPref(this,"profile");
        // push 알림 get
        arrSetNoti = managePref.getStringArrayPref(this,"isPush");
        arrSetSound = managePref.getStringArrayPref(this,"isSound");
        arrSetVibrate = managePref.getStringArrayPref(this,"isVibrate");

        Log.d("LOG1","로컬 DB 연결 완료");

        String state = intent.getStringExtra("state");

        if(arrSetNoti == null || arrSetNoti.get(0) == null){
            Log.e("push 알람 오류","push 알람 데이터가 없습니다!. 푸시 알람이 자동 해제 됩니다.");
            isPushOn = false;
        }
        else{
            Log.e("push 알람","push 알람 데이터가 있습니다!. - "+arrSetNoti.get(0));
            if(Boolean.valueOf(arrSetNoti.get(0)) == false){
                isPushOn = false;
            }
            else{
                isPushOn = true;
            }
        }


        // 상단바 푸시 알람 체크 했다면 푸시 알람 나옴.
        if(isPushOn == true){
            builder = null; manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //버전 오레오 이상일 경우
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                manager.createNotificationChannel( new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT) );
                builder = new NotificationCompat.Builder(this,CHANNEL_ID);
            }
            //하위 버전일 경우
            else{
                builder = new NotificationCompat.Builder(this);
            }


            // 알람창 누를 시 앱으로 이동하는 인텐트
            /*
            Intent noti_intent = new Intent(this, HorizontalNtbActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, noti_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            */

            Intent noti_intent = new Intent(this, HorizontalNtbActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, noti_intent, 0);

            //알림창 제목
            builder.setContentTitle("예약카톡 전송완료");
            //알림창 메시지
            builder.setContentText("전송이 완료되었습니다");
            //알림창 아이콘
            builder.setSmallIcon(R.drawable.kakaotalk_icon);
            //알람창 터치시 자동 삭제
            builder.setAutoCancel(true);
            // pendingIntent를 builder에 설정 해줍니다.
            // 알림창 터치시 인텐트가 전달할 수 있도록 해줍니다.
            builder.setContentIntent(pendingIntent);
            builder.setSound(null);
            Notification notification = builder.build();

            //알림창 실행
            Random notification_id = new Random();
            manager.notify(notification_id.nextInt(100), notification);
        }

        if (state.equals("on")) {
            // 알람음 재생 OFF, 알람음 시작 상태

            this.isRunning = true;

            Log.d("AlarmService", "Alarm Start");
            Log.d("LOG1","카톡 메세지를 전송합니다.");
            // ---  To Do --- //

            // 현재 시간 -> 분 저장
            long now = System.currentTimeMillis();
            Date mDate = new Date(now);
            SimpleDateFormat simpleDate = new SimpleDateFormat("MMddHHmm");
            String getTime = simpleDate.format(mDate);

            Log.e("현재 시간과 비교합니다. 현재 시간",getTime);

            int idx = -1;
            for(int i=0;i < Time.size();i++){
                Log.d("log1","Time.get() : " + Time.get(i));
                Log.d("log1","gettime : " + getTime);
                if(getTime.equals(Time.get(i))) {
                    Log.e("현재 시간과 동일한 인덱스를 찾았습니다. ", "동일 시간: "+Time.get(i)+"인덱스: "+i);
                    idx = i;
                    break;
                }
            }

            // 메세지 템플릿 만들기
            TemplateParams params = TextTemplate.newBuilder(Message.get(idx), LinkObject.newBuilder()
                    .setWebUrl("https://developers.kakao.com")
                    .setMobileWebUrl("https://developers.kakao.com").build()).build();

            if(Id.get(idx).contains("@")){
                Log.e("YEYAK SEND", "나에게 보내는 메세지");
                // 기본 템플릿으로 나에게 보내기
                KakaoTalkService.getInstance()
                        .requestSendMemo(new TalkResponseCallback<Boolean>() {
                            @Override
                            public void onNotKakaoTalkUser() {
                                Log.e("KAKAO_API", "카카오톡 사용자가 아님");
                            }

                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {
                                Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                            }

                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                Log.e("KAKAO_API", "나에게 보내기 실패: " + errorResult);
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                Log.i("KAKAO_API", "나에게 보내기 성공");
                            }
                        }, params);
            }
            else{
              Log.e("YEYAK SEND", "친구에게 보내는 메세지");
            // 선택한 카카오 친구에게 보내기
            KakaoTalkService.getInstance()
                    .sendMessageToFriends(Collections.singletonList(Id.get(idx)), params, new TalkResponseCallback<MessageSendResponse>() {
                        @Override
                        public void onNotKakaoTalkUser() {
                            Log.e("KAKAO_API", "카카오톡 사용자가 아님");
                        }

                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "친구에게 보내기 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MessageSendResponse result) {
                            if (result.successfulReceiverUuids() != null) {
                                Log.i("KAKAO_API", "친구에게 보내기 성공");
                                Log.d("KAKAO_API", "전송에 성공한 대상: " + result.successfulReceiverUuids());
                            }
                            if (result.failureInfo() != null) {
                                Log.e("KAKAO_API", "일부 사용자에게 메시 보내기 실패");
                                for (MessageFailureInfo failureInfo : result.failureInfo()) {
                                    Log.d("KAKAO_API", "code: " + failureInfo.code());
                                    Log.d("KAKAO_API", "msg: " + failureInfo.msg());
                                    Log.d("KAKAO_API", "failure_uuids: " + failureInfo.receiverUuids());
                                }
                            }
                        }
                    });
            }


            if(idx!=-1){
                adapter.removeItem(idx);
                Log.d("알람 번호 LOG", B_id.get(idx));
                Log.d("보낸 시간 LOG", Time.get(idx));
                Log.d("받는이 / 메세지 내용", Name.get(idx) + "  " + Message.get(idx) + "  ");
                Log.d("LOG1","카톡 메세지 전송 끝. 해당 정보를 삭제합니다." + idx);
            }
            else  Log.d("LOG1","전송 실패");

        }
        // 알람음 재생 ON, 알람음 중지 상태
        /*
            this.isRunning = false;
            Log.d("AlarmService", "Alarm Stop");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopForeground(true);
            }
            else{
                stopSelf();
            }

         */

        return START_NOT_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "Alarm";
        String channelName = getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        //channel.setDescription(channelName);
        channel.setSound(null, null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        return channelId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("LOG1","알람 서비스를 종료합니다. 로컬 DB 연결 해제");

//        managePref.setStringArrayPref(this,"BroadCastID",B_id);
//        managePref.setStringArrayPref(this,"hour",Time);
//        managePref.setStringArrayPref(this,"id",Id);
//        managePref.setStringArrayPref(this,"name",Name);
//        managePref.setStringArrayPref(this,"message",Message);
//        managePref.setStringArrayPref(this,"profile",Profile);

        Log.d("LOG1","로컬 DB 연결 해제 완료");
    }
}
