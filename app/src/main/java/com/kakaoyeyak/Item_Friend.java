package com.kakaoyeyak;

public class Item_Friend {

    String nickname;
    String userid;
    String uuid;
    String profileimage;

    public Item_Friend(String uuid, String userid, String nickname, String profileimage) {
        this.uuid = uuid;
        this.userid = userid;
        this.nickname = nickname;
        this.profileimage = profileimage;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUserid() {
        return userid;
    }

    public String getUuid() { return uuid; }

    public String getProfileimage() { return profileimage; }
}
