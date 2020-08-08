package com.kakaoyeyak;

import android.app.Service;
import android.content.Intent;
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

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;

import static com.kakaoyeyak.HorizontalNtbActivity.adapter;


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

    ManagePref managePref = new ManagePref();


    private boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("LOG1","처음으로 알람 서비스가 실행되었습니다. 로컬 DB 연결 시작");

        B_id = managePref.getStringArrayPref(this,"BroadCastID");
        Time = managePref.getStringArrayPref(this,"time");
        Id = managePref.getStringArrayPref(this,"id");
        Name = managePref.getStringArrayPref(this,"name");
        Message = managePref.getStringArrayPref(this,"message");
        Profile = managePref.getStringArrayPref(this,"profile");

        Log.d("LOG1","로컬 DB 연결 완료");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Foreground 에서 실행되면 Notification 을 보여줘야 됨
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Oreo(26) 버전 이후 버전부터는 channel 이 필요함
            String channelId =  createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("YK_MSG")
                    .setContentText("전송 완료")
                    .build();

            startForeground(1, notification);
        }

        String state = intent.getStringExtra("state");

        if (state.equals("on")) {
            // 알람음 재생 OFF, 알람음 시작 상태

            this.isRunning = true;

            Log.d("AlarmService", "Alarm Start");
            Log.d("LOG1","카톡 메세지를 전송합니다.");
            // To Do
            ///// 임시로 get(0)으로만 보내게 해봄. /////
            // 텍스트 템플릿 만들기

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

//            TemplateParams params = TextTemplate.newBuilder(Message.get(idx), LinkObject.newBuilder()
//                    .setWebUrl("https://developers.kakao.com")
//                    .setMobileWebUrl("https://developers.kakao.com").build()).build();
//            // 선택한 카카오 친구에게 보내기
//            KakaoTalkService.getInstance()
//                    .sendMessageToFriends(Collections.singletonList(Id.get(idx)), params, new TalkResponseCallback<MessageSendResponse>() {
//                        @Override
//                        public void onNotKakaoTalkUser() {
//                            Log.e("KAKAO_API", "카카오톡 사용자가 아님");
//                        }
//
//                        @Override
//                        public void onSessionClosed(ErrorResult errorResult) {
//                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
//                        }
//
//                        @Override
//                        public void onFailure(ErrorResult errorResult) {
//                            Log.e("KAKAO_API", "친구에게 보내기 실패: " + errorResult);
//                        }
//
//                        @Override
//                        public void onSuccess(MessageSendResponse result) {
//                            if (result.successfulReceiverUuids() != null) {
//                                Log.i("KAKAO_API", "친구에게 보내기 성공");
//                                Log.d("KAKAO_API", "전송에 성공한 대상: " + result.successfulReceiverUuids());
//                            }
//                            if (result.failureInfo() != null) {
//                                Log.e("KAKAO_API", "일부 사용자에게 메시 보내기 실패");
//                                for (MessageFailureInfo failureInfo : result.failureInfo()) {
//                                    Log.d("KAKAO_API", "code: " + failureInfo.code());
//                                    Log.d("KAKAO_API", "msg: " + failureInfo.msg());
//                                    Log.d("KAKAO_API", "failure_uuids: " + failureInfo.receiverUuids());
//                                }
//                            }
//                        }
//                    });


            if(idx!=-1){
                adapter.removeItem(idx);
                Log.d("알람 번호 LOG", B_id.get(idx));
                Log.d("보낸 시간 LOG", Time.get(idx));
                Log.d("받는이 / 메세지 내용", Name.get(idx) + "  " + Message.get(idx) + "  ");
                Log.d("LOG1","카톡 메세지 전송 끝. 해당 정보를 삭제합니다." + idx);
            }
            else  Log.d("LOG1","전송 실패");

        } else if (state.equals("off")) {
            // 알람음 재생 ON, 알람음 중지 상태

            this.isRunning = false;

            Log.d("AlarmService", "Alarm Stop");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopForeground(true);
            }
        }

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


/*
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

        ///// 임시로 get(0)으로만 보내게 해봄. /////
        // 텍스트 템플릿 만들기
        TemplateParams params = TextTemplate.newBuilder(Message.get(0), LinkObject.newBuilder()
                .setWebUrl("https://developers.kakao.com")
                .setMobileWebUrl("https://developers.kakao.com").build()).build();
        // 선택한 카카오 친구에게 보내기
        KakaoTalkService.getInstance()
                .sendMessageToFriends(Collections.singletonList(Id.get(0)), params, new TalkResponseCallback<MessageSendResponse>() {
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

        //현재는 로그 출력 부분
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

 */
