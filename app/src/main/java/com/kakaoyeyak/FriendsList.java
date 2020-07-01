package com.kakaoyeyak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

//친구목록 출력
public class FriendsList extends AppCompatActivity {

    //친구 목록 항목
    private ArrayList<Item_Friend> items = new ArrayList<>();
    ArrayList<String> uuids = new ArrayList<String>();
    ArrayList<String> userids = new ArrayList<String>();
    ArrayList<String> nicknames = new ArrayList<String>();
    ArrayList<String> profileimages = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        //데이터 로컬DB에서 받아옴
        initDataset();

        //연결
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_friend);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewAdapter_Friend adapter = new RecyclerViewAdapter_Friend(this, items);
        recyclerView.setAdapter(adapter);
    }

    //데이터 삽입
    private void initDataset() {
        items.clear();

        ManagePref managePref = new ManagePref();
        uuids = managePref.getStringArrayPref(this,"uuids");
        userids = managePref.getStringArrayPref(this,"userids");
        nicknames = managePref.getStringArrayPref(this,"nicknames");
        profileimages = managePref.getStringArrayPref(this,"profileimages");

        for(int i=0; i<uuids.size(); i++) {
            items.add(new Item_Friend(uuids.get(i),userids.get(i),nicknames.get(i),profileimages.get(i)));
            Log.d("friend list2","uuid : " + uuids.get(i) + "  userids : " + userids.get(i) + "   nickname : " + nicknames.get(i) + "   profileimage : " + profileimages.get(i));
        }
        Log.d("friend list","friend list dataset(), item size : " + String.valueOf(items.size()));
    }
}