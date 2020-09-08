package com.kakaoyeyak;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.AppFriendOrder;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SessionCallback implements ISessionCallback {

    ArrayList<String> uuids = new ArrayList<String>();
    ArrayList<String> userids = new ArrayList<String>();
    ArrayList<String> nicknames = new ArrayList<String>();
    ArrayList<String> profileimages = new ArrayList<String>();

    AppFriendContext context_friend =
            new AppFriendContext(AppFriendOrder.NICKNAME, 0, 100, "asc");

    ManagePref managePref = new ManagePref();

    private Context context;


    public SessionCallback(Context context){
        this.context = context;
    }
    // 로그인에 성공한 상태
    @Override
    public void onSessionOpened() {
        Log.d("friend list", "session callback array size : " + uuids.size());
        requestMe();
        Log.d("KAKAO API","Session Opend");

        KakaoTalkService.getInstance()
                .requestAppFriends(context_friend, new TalkResponseCallback<AppFriendsResponse>() {
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
                        Log.i("KAKAO_API", "친구 조회 성공 session callback");

                        for (AppFriendInfo friend : result.getFriends()) {
                            Log.d("KAKAO_API", friend.toString());

                            uuids.add(friend.getUUID());        // 친구 UUID를 uuids배열 리스트에 저장.
                            userids.add(String.valueOf(friend.getId()));
                            nicknames.add(friend.getProfileNickname());
                            if(friend.getProfileThumbnailImage().length()<5)
                                profileimages.add(friend.getProfileThumbnailImage());
                            else
                                profileimages.add(Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.kakao_default_profile_image).toString());
                        }

                        Log.d("friend list", "session callback array size : " + uuids.size());
                        managePref.setStringArrayPref(context,"uuids",uuids);
                        managePref.setStringArrayPref(context,"userids",userids);
                        managePref.setStringArrayPref(context,"nicknames",nicknames);
                        managePref.setStringArrayPref(context,"profileimages",profileimages);
                    }
                });



        Intent intent = new Intent(context, OnBoardActivity.class); // 다음 넘어갈 클래스 지정
        context.startActivity(intent); // 다음 화면으로 넘어간다
    }


    // 로그인에 실패한 상태
    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
    }

    // 사용자 정보 요청
    public void requestMe() {
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
                            Log.d("friend list", "session callback array size : " + uuids.size());

                            managePref.setStringArrayPref(context,"uuids",uuids);
                            managePref.setStringArrayPref(context,"userids",userids);
                            managePref.setStringArrayPref(context,"nicknames",nicknames);
                            managePref.setStringArrayPref(context,"profileimages",profileimages);

                        }
                    }
                });
    }
}