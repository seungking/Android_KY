package com.kakaoyeyak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

//메인 with 네비게이션 텝 바
public class HorizontalNtbActivity extends AppCompatActivity {

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

    static View view;

    public static void removeItem(int position){
        adapter.removeItem(position);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_ntb);

//        //친구목록으로
        findViewById(R.id.addmessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HorizontalNtbActivity.this, FriendsList.class));
//                finish();
                if(view!=null){
                    Log.d("log1", "view not null!");
                    ViewGroup parent = (ViewGroup)view.getParent();
                    if(parent!=null){
                        parent.removeView(view);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //데이터 설정
        initDataset();
        //설정
        initUI();
    }

    //설정
    private void initUI() {
        Log.d("LOG1", "INIT UI");
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @SuppressLint("ResourceType")
            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                //탭 바 위치에 따라서 레이아웃
                if(position==0) {
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.item_vp_list, null, false);

//                    //뷰 연결
                    Context context = view.getContext();
                    recyclerView = (RecyclerView) view.findViewById(R.id.rv_msg);
                    recyclerView.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);

                    adapter = new RecyclerViewAdapter_Msg(context, items);
                    recyclerView.setAdapter(adapter);

                }
                else {
                    //세팅

                    LayoutInflater layoutInflater = getLayoutInflater();
                    view = layoutInflater.inflate(R.layout.activity_setting, null);

                    /*
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.setting, null, false);


                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.activity_setting, null, false);

                     */

                }

                //화면에 뷰 추가
                container.addView(view);
                return view;
            }
        });

        //텝 바 메뉴
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        navigationTabBar.setModelIndex(1);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_person_unselected),
                        Color.parseColor("#F1F1F1"))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_person_selected))
                        .title("LIST")
                        .badgeTitle("")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_settings_unselected),
                        Color.parseColor("#F1F1F1"))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_settings_selected))
                        .title("SETTING")
                        .badgeTitle("")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        // 텝 바 움직이는거
        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }



    private void initDataset() {
        //초기화
        //로컬 DB로 나중에 받기
        items.clear();

        Time = managePref.getStringArrayPref(this,"time");
        Id = managePref.getStringArrayPref(this,"id");
        Name = managePref.getStringArrayPref(this,"name");
        Message = managePref.getStringArrayPref(this,"message");
        Profile = managePref.getStringArrayPref(this,"profile");

        Log.d("LOG1","time size : " + String.valueOf(Time.size()));
        Log.d("LOG1","NAME size : " + String.valueOf(Name.size()));
        Log.d("LOG1","MESSAGE size : " + String.valueOf(Message.size()));
        Log.d("LOG1","PROFILE size : " + String.valueOf(Profile.size()));

//        if(Time.size()==0){
//            Time.add("11");
//            Time.add("12");
//            Time.add("13");
//            Time.add("14");
//            Id.add("seungki1");
//            Id.add("seungki2");
//            Id.add("seungki3");
//            Id.add("seungki4");
//            Name.add("안승기1");
//            Name.add("안승기2");
//            Name.add("안승기3");
//            Name.add("안승기4");
//            Message.add("안녕하세요 저는 광운대학교 4학년 컴퓨터정보공학부 안승기라고 합니다. 뽑아주시면 열심히 하겠습니다.1");
//            Message.add("안녕하세요 저는 광운대학교 4학년 컴퓨터정보공학부 안승기라고 합니다. 뽑아주시면 열심히 하겠습니다.2");
//            Message.add("안녕하세요 저는 광운대학교 4학년 컴퓨터정보공학부 안승기라고 합니다. 뽑아주시면 열심히 하겠습니다.3");
//            Message.add("안녕하세요 저는 광운대학교 4학년 컴퓨터정보공학부 안승기라고 합니다. 뽑아주시면 열심히 하겠습니다.4");
//            Profile.add("https://movie-phinf.pstatic.net/20190417_250/1555465284425i6WQE_JPEG/movie_image.jpg?type=m665_443_2");
//            Profile.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRfTVhCq6mfz9BxSAwQlrJ1DzkzauA-1leMBA&usqp=CAU");
//            Profile.add("data:image/jpeg;base64,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");
//            Profile.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTG1-TlHSMBHlUsOlWRIzl4yniVy6ijVaAiSA&usqp=CAU");
//
//            managePref.setStringArrayPref(this,"time",Time);
//            managePref.setStringArrayPref(this,"id",Id);
//            managePref.setStringArrayPref(this,"name",Name);
//            managePref.setStringArrayPref(this,"message",Message);
//            managePref.setStringArrayPref(this,"profile",Profile);
//
//        }

        for(int i=0; i<Time.size(); i++){
            items.add(new Item_Msg(Time.get(i)  ,Name.get(i),  Message.get(i),Profile.get(i)));
        }
    }

    public void go_settings(View view) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }
}
