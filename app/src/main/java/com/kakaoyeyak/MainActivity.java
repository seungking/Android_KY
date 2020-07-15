package com.kakaoyeyak;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.AppFriendOrder;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.MessageSendResponse;
import com.kakao.kakaotalk.response.model.MessageFailureInfo;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//이거 안 건들였음
public class MainActivity extends AppCompatActivity {

    private Button btn_custom_login;
    private Button btn_custom_login_out;
    private Button btn_custom_msg_me; // 나에게 보내기
    private Button btn_custom_list; // 친구 목록 조회하기
    private Button btn_custom_msg_you; // 친구에게 보내기

    //private Button kakaoLinkBtn; // 카카오 링크

    private SessionCallback sessionCallback = new SessionCallback(this);
    Session session;

    // 컨텍스트 생성
    //   - 닉네임,  처음(index 0)부터, 100명까지, 오름차순 예시
    AppFriendContext context =
            new AppFriendContext(AppFriendOrder.NICKNAME, 0, 100, "asc");
    List<String> uuids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btn_custom_login = (Button) findViewById(R.id.btn_custom_login);
        btn_custom_login_out = (Button) findViewById(R.id.btn_custom_login_out);
        btn_custom_msg_me = (Button) findViewById(R.id.btn_custom_msg_me);
        btn_custom_list = (Button) findViewById(R.id.btn_custom_list);
        btn_custom_msg_you = (Button) findViewById(R.id.btn_custom_msg_you);
        //kakaoLinkBtn = (Button) findViewById(R.id.kakaoLinkBtn);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        btn_custom_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.open(AuthType.KAKAO_LOGIN_ALL, MainActivity.this);

            }
        });

        btn_custom_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance()
                        .requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btn_custom_msg_me.setOnClickListener(new View.OnClickListener() {

            // 텍스트 템플릿 만들기
            LinkObject link = LinkObject.newBuilder()
                    .build();
            TemplateParams params = TextTemplate.newBuilder("안녕하세요 앱에서 보냅니다.",link)
                    .build();

            @Override
            public void onClick(View v) {
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
        });

        btn_custom_list.setOnClickListener(new View.OnClickListener() {
            // 친구 목록 받기
            @Override
            public void onClick(final View v) {
                // 조회 요청 -> 친구 UUID 얻음
                KakaoTalkService.getInstance()
                        .requestAppFriends(context, new TalkResponseCallback<AppFriendsResponse>() {
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
                                Log.e("KAKAO_API", "친구 조회 실패: " + errorResult);
                            }

                            @Override
                            public void onSuccess(AppFriendsResponse result) {
                                Log.i("KAKAO_API", "친구 조회 성공");

                                for (AppFriendInfo friend : result.getFriends()) {
                                    Log.d("KAKAO_API", friend.toString());

                                    String uuid = friend.getUUID();     // 메시지 전송 시 사용
                                    uuids = Arrays.asList(uuid);        // 친구 UUID를 uuids배열 리스트에 저장.
                                    Log.i("KAKAO_API", uuid);
                                }

                                // 사용자 리스트 로컬 디비로 저장하고
                                // 다음 화면으로 이동
                                // Yeyak.startwithIntent(v.getContext(), uuids);
                            }
                        });
            }
        });

        // 친구 UUID로 메세지 보내기
        btn_custom_msg_you.setOnClickListener(new View.OnClickListener() {

            // 텍스트 템플릿 만들기
            LinkObject link = LinkObject.newBuilder()
                    .setWebUrl("https://developers.kakao.com")
                    .setMobileWebUrl("https://developers.kakao.com")
                    .build();
            TemplateParams params = TextTemplate.newBuilder("Hello!", link)
                    .setButtonTitle("Hello, world!")
                    .build();


            @Override
            public void onClick(View v) {
                // 기본 템플릿으로 친구에게 보내기
                KakaoTalkService.getInstance()
                        .sendMessageToFriends(uuids, params, new TalkResponseCallback<MessageSendResponse>() {
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
        });
    }

    public void btnClick(View view){
        // 보낼 메세지 입력창 만들기
        final AlertDialog.Builder MsgBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        // 보낼 메세지 입력창
        MsgBuilder.setTitle("보낼 메세지를 입력하세요.");
        MsgBuilder.setView(input);
        MsgBuilder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    // 입력창 오픈
                    public void onClick( DialogInterface dialog, int which) {
                        // 텍스트 템플릿 만들기
                        TemplateParams params = TextTemplate.newBuilder(input.getText().toString(), LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build()).build();

                        // 링크 콜백에서 함께 수신할 커스텀 파라미터를 설정합니다.
                        Map<String, String> serverCallbackArgs = new HashMap<>();
                        serverCallbackArgs.put("user_id", "${current_user_id}");
                        serverCallbackArgs.put("product_id", "${shared_product_id}");

                        // 카카오 링크 보내기
                        KakaoLinkService.getInstance()
                                .sendDefault(MainActivity.this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Log.e("KAKAO_API", "카카오링크 공유 실패: " + errorResult);
                                    }

                                    @Override
                                    public void onSuccess(KakaoLinkResponse result) {
                                        Log.i("KAKAO_API", "카카오링크 공유 성공");

                                        // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                                        Log.w("KAKAO_API", "warning messages: " + result.getWarningMsg());
                                        Log.w("KAKAO_API", "argument messages: " + result.getArgumentMsg());

                                    }
                                });
                    }
                });
        MsgBuilder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        MsgBuilder.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}