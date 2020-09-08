package com.kakaoyeyak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.AppFriendOrder;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private SessionCallback sessionCallback = new SessionCallback(this);
    Session session;

    LoginButton loginButton;
    TextView textView;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ArrayList<String> uuids = new ArrayList<String>();
    ArrayList<String> userids = new ArrayList<String>();
    ArrayList<String> nicknames = new ArrayList<String>();
    ArrayList<String> profileimages = new ArrayList<String>();
    AppFriendContext context =
            new AppFriendContext(AppFriendOrder.NICKNAME, 0, 100, "asc");

    ManagePref managePref = new ManagePref();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        loginButton = (LoginButton)findViewById(R.id.btn_kakao_login);
        textView = (TextView)findViewById(R.id.need_login_text);

        //Lottie Animation
        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.splash_logo);
        animationView.setAnimation("loading.json");
        animationView.loop(true);
        //Lottie Animation start
        animationView.playAnimation();

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                finish();
                loginButton.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.INVISIBLE);
                new splashhandler();
            }
        },1800);
    }

    private class splashhandler implements Runnable{
        public void run(){
            //로그인 상태
            if(session.isOpened()){
                Log.d("KAKAO API","Opened in LoginActivity");
                UserManagement.getInstance()
                        .me(new MeV2ResponseCallback() {
                                @Override
                                public void onSessionClosed(ErrorResult errorResult) {
                                    Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                                }

                                @Override
                                public void onFailure(ErrorResult errorResult) {
                                    Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                                }

                                @Override
                                public void onSuccess(MeV2Response result) {
                                    Log.i("KAKAO_API", "사용자 아이디: " + result.getId());

                                    UserAccount kakaoAccount = result.getKakaoAccount();

                                    if (kakaoAccount != null) {

                                        // 이메일
                                        String email = kakaoAccount.getEmail();

                                        if (email != null) {
                                            Log.i("KAKAO_API", "email: " + email);

                                        } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                            // 동의 요청 후 이메일 획득 가능
                                            // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.

                                        } else {
                                            // 이메일 획득 불가
                                        }

                                        // 프로필
                                        Profile profile = kakaoAccount.getProfile();

                                        if (profile != null) {
                                            Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                            Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                            Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());

                                        } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                            // 동의 요청 후 프로필 정보 획득 가능

                                        } else {
                                            // 프로필 획득 불가
                                        }

                                        // 소스 저장 후 DB 연결
                                        uuids.add(email);
                                        userids.add(String.valueOf(result.getId()));
                                        nicknames.add(profile.getNickname());
                                        profileimages.add(profile.getProfileImageUrl());


                                        managePref.setStringArrayPref(LoginActivity.this,"uuids",uuids);
                                        managePref.setStringArrayPref(LoginActivity.this,"userids",userids);
                                        managePref.setStringArrayPref(LoginActivity.this,"nicknames",nicknames);
                                        managePref.setStringArrayPref(LoginActivity.this,"profileimages",profileimages);



                                    }
                                }
                        });

                //친구리스트 받아서 로컬 DB 저장
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
                                Log.i("KAKAO_API", "친구 조회 성공 logn activty");

                                for (AppFriendInfo friend : result.getFriends()) {
                                    Log.d("KAKAO_API", friend.toString());

                                    uuids.add(friend.getUUID());        // 친구 UUID를 uuids배열 리스트에 저장.
                                    userids.add(String.valueOf(friend.getId()));
                                    nicknames.add(friend.getProfileNickname());
                                    if(friend.getProfileThumbnailImage().length()<5)
                                        profileimages.add(friend.getProfileThumbnailImage());
                                    else
                                        profileimages.add(Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.kakao_default_profile_image).toString());

                                    Log.d("KAKAO API", friend.getUUID() + friend.getId() + friend.getProfileNickname() + friend.getProfileThumbnailImage());
                                }

                                Log.d("KAKAO API","ARRAY LIST : " + uuids.size());
                                managePref.setStringArrayPref(LoginActivity.this,"uuids",uuids);
                                managePref.setStringArrayPref(LoginActivity.this,"userids",userids);
                                managePref.setStringArrayPref(LoginActivity.this,"nicknames",nicknames);
                                managePref.setStringArrayPref(LoginActivity.this,"profileimages",profileimages);


                            }
                        });

                if(pref.getInt("isfirst",0)==0){
                    editor.putInt("isfirst",1);
                    editor.commit();
                    startActivity(new Intent(LoginActivity.this,OnBoardActivity.class));
                }
                else{
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                finish();
            }
            //로그인 안될때
            //시간 지나면 버튼 등장하고 클릭하면 로그인
            else if(session.isClosed()) {
                Log.d("KAKAO API","Closed in LoginActivity");
                loginButton.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

}
