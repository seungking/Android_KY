package com.kakaoyeyak;

public class Item_Msg {

    String time;
    String name;
    String summary;
    String profileimage;

    public Item_Msg(String time, String name, String summary, String profileimage) {
        this.time = time;
        this.name = name;
        this.summary = summary;
        this.profileimage = profileimage;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getTime() {
        return time;
    }

    public String getProfileimage() {
        return profileimage;
    }
}