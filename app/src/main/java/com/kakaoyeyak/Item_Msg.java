package com.kakaoyeyak;

public class Item_Msg {

    String name;
    String summary;

    public Item_Msg(String name, String summary) {
        this.name = name;
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }
}