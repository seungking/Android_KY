package com.kakaoyeyak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class HomeFragment extends Fragment {

    private View view;

    ArrayList<String> B_id = new ArrayList<String>(); // broadcastCode 저장.
    static ArrayList<String> Time = new ArrayList<String>();
    static ArrayList<String> Id = new ArrayList<String>();
    static ArrayList<String> Name = new ArrayList<String>();
    static ArrayList<String> Message = new ArrayList<String>();
    static ArrayList<String> Profile = new ArrayList<String>();

    ManagePref managePref = new ManagePref();

    //설정 메시지 항목들
    private ArrayList<Item_Msg> items = new ArrayList<>();

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    static RecyclerViewAdapter_Msg adapter;

    public static void removeItem(int position){
        adapter.removeItem(position);
    }

    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_vp_list,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_msg);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initDataset();
    }

    public void initDataset() {
        items.clear();

        Time = managePref.getStringArrayPref(view.getContext(),"time");
        Id = managePref.getStringArrayPref(view.getContext(),"id");
        Name = managePref.getStringArrayPref(view.getContext(),"name");
        Message = managePref.getStringArrayPref(view.getContext(),"message");
        Profile = managePref.getStringArrayPref(view.getContext(),"profile");


        for(int i=0; i<Time.size(); i++)
            items.add(new Item_Msg(Time.get(i)  ,Name.get(i),  Message.get(i),Profile.get(i)));


        adapter = new RecyclerViewAdapter_Msg(view.getContext(), items);
        recyclerView.setAdapter(adapter);
    }

}
