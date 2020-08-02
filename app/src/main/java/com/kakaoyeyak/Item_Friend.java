package com.kakaoyeyak;

import android.content.ClipData;

public class Item_Friend {

    String nickname;
    String userid;
    String uuid;
    String profileimage;
    String letters;

    public Item_Friend(){}

    public Item_Friend(String uuid, String userid, String nickname, String profileimage) {
        this.uuid = uuid;
        this.userid = userid;
        this.nickname = nickname;
        this.profileimage = profileimage;
    }

    public void setLetters(String letter) {
        this.letters = letter;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUserid() {
        return userid;
    }

    public String getUuid() { return uuid; }

    public String getProfileimage() { return profileimage; }

    public String getLetters() {
        return letters;
    }
}
