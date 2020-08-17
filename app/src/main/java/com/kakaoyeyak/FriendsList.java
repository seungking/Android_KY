package com.kakaoyeyak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.kakaoyeyak.sortedlist.ClearEditText;
import com.kakaoyeyak.sortedlist.PinyinComparator;
import com.kakaoyeyak.sortedlist.PinyinUtils;
import com.kakaoyeyak.sortedlist.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//친구목록 출력
public class FriendsList extends AppCompatActivity {

    //친구 목록 항목
    private ArrayList<Item_Friend> items = new ArrayList<>();
    ArrayList<String> uuids = new ArrayList<String>();
    ArrayList<String> userids = new ArrayList<String>();
    ArrayList<String> nicknames = new ArrayList<String>();
    ArrayList<String> profileimages = new ArrayList<String>();

    private SideBar sideBar;
    private TextView dialog;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter_Friend adapter;
    private ClearEditText mClearEditText;
    LinearLayoutManager manager;

    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        //데이터 로컬DB에서 받아옴
        initDataset();
        initViews();

//        //연결
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_friend);
//        recyclerView.setHasFixedSize(true);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//
//        RecyclerViewAdapter_Friend adapter = new RecyclerViewAdapter_Friend(this, items);
//        recyclerView.setAdapter(adapter);
    }

    private void initViews(){
        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sideBar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧SideBar触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_friend);

        //RecyclerView社置manager
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new RecyclerViewAdapter_Friend(this, items);
        mRecyclerView.setAdapter(adapter);
        //item点击事件
        /*adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, ((SortModel)adapter.getItem(position)).getName(),Toast.LENGTH_SHORT).show();
            }
        });*/
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

//    private List<Item_Friend> filledData(String[] date) {
//        List<Item_Friend> mSortList = new ArrayList<>();
//
//        for (int i = 0; i < date.length; i++) {
//            Item_Friend sortModel = new Item_Friend();
//            sortModel.setName(date[i]);
//            //汉字转换成拼音
//            String pinyin = PinyinUtils.getPingYin(date[i]);
//            String sortString = pinyin.substring(0, 1).toUpperCase();
//
//            // 正则表达式，判断首字母是否是英文字母
////            if (sortString.matches("[A-Z]")) {
//            if (sortString.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") || sortString.matches("[A-Z]")) {
//                sortModel.setLetters(sortString.toUpperCase());
//            } else {
//                sortModel.setLetters("#");
//            }
//
//            mSortList.add(sortModel);
//        }
//        return mSortList;
//
//    }

    //데이터 삽입
    private void initDataset() {
        items.clear();

        ManagePref managePref = new ManagePref();
        uuids = managePref.getStringArrayPref(this,"uuids");
        userids = managePref.getStringArrayPref(this,"userids");
        nicknames = managePref.getStringArrayPref(this,"nicknames");
        profileimages = managePref.getStringArrayPref(this,"profileimages");

        for(int i=0; i<uuids.size(); i++) {
            Item_Friend temp = new Item_Friend(uuids.get(i),userids.get(i),nicknames.get(i),profileimages.get(i));
            String pinyin = PinyinUtils.getPingYin(nicknames.get(i));
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") || sortString.matches("[A-Z]")) {
                temp.setLetters(sortString.toUpperCase());
            } else {
                temp.setLetters("#");
            }
            items.add(temp);
            Log.d("friend list2","uuid : " + uuids.get(i) + "  userids : " + userids.get(i) + "   nickname : " + nicknames.get(i) + "   profileimage : " + profileimages.get(i));
        }
        Log.d("friend list","friend list dataset(), item size : " + String.valueOf(items.size()));
    }

    private void filterData(String filterStr) {
        ArrayList<Item_Friend> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = items;
        } else {
            filterDateList.clear();
            for (Item_Friend sortModel : items) {
                String name = sortModel.getNickname();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateList(filterDateList);
    }

}