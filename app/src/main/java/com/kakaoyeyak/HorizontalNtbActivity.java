package com.kakaoyeyak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
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

    View view;
    View view1;
    View view2;
    Context view1context;

    NavigationTabBar navigationTabBar;
    ViewPager viewPager;
    LinearLayout topbar;

    private Boolean mode;

    SharedPreferences pref;

    BackPressCloseHandler backPressCloseHandler;

    public static void removeItem(int position){
        adapter.removeItem(position);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_ntb);

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/imcresoojin.ttf");

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mode = pref.getBoolean("screen_mode",false);

        backPressCloseHandler = new BackPressCloseHandler(this);
//        //친구목록으로
        findViewById(R.id.addmessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HorizontalNtbActivity.this, FriendsList.class));
                finish();

                /*
                if(view!=null){
                    Log.d("log1", "view not null!");
                    ViewGroup parent = (ViewGroup)view.getParent();
                    if(parent!=null){
                        parent.removeView(view);
                    }
                }

                 */

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

        navigationTabBar = (NavigationTabBar)findViewById(R.id.ntb_horizontal);
        viewPager = (ViewPager)findViewById(R.id.vp_horizontal_ntb);
        topbar = (LinearLayout)findViewById(R.id.topbar);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

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
                    Log.d("log1", "position 0");
                    view1 = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.item_vp_list, null, false);

                    TypefaceUtil.overrideFont(view1.getContext(), "SERIF", "fonts/imcresoojin.ttf");

                    view = view1;

                    view1context = view1.getContext();
                    recyclerView = (RecyclerView) view1.findViewById(R.id.rv_msg);
                    recyclerView.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(view1context);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);

                    connectView();

                }
                else {
                    //세팅
                    Log.d("log1", "position 1");
                    LayoutInflater layoutInflater = getLayoutInflater();
                    view2 = layoutInflater.inflate(R.layout.activity_setting, null);

                    view = view2;

                    /*
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.setting, null, false);


                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.activity_setting, null, false);

                     */

                }

                //화면에 뷰 추가
                container.addView(view);
                //스크린 모드에 따른 색 설정
                setColor(mode);
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

    public void connectView(){

        adapter.setMode(pref.getBoolean("screen_mode",false));
        adapter = new RecyclerViewAdapter_Msg(view1context, items);
        //뷰 연결
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
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

        for(int i=0; i<Time.size(); i++){
            items.add(new Item_Msg(Time.get(i)  ,Name.get(i),  Message.get(i),Profile.get(i)));
        }
    }


   public void setColor(Boolean mode){

        Log.d("log1", "setcolor  " + mode.toString());
        if(mode){
            navigationTabBar.setBackgroundColor(Color.parseColor("#ffffff"));
            navigationTabBar.setActiveColor(Color.parseColor("#727272"));
            navigationTabBar.setInactiveColor(Color.parseColor("#727272"));
            navigationTabBar.setBgColor(Color.parseColor("#ffffff"));
            viewPager.setBackgroundColor(Color.parseColor("#ffffff"));
            topbar.setBackgroundColor(Color.parseColor("#ffffff"));
            view1.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else{
            navigationTabBar.setBackgroundColor(Color.parseColor("#727272"));
            navigationTabBar.setActiveColor(Color.parseColor("#ffffff"));
            navigationTabBar.setInactiveColor(Color.parseColor("#ffffff"));
            navigationTabBar.setBgColor(Color.parseColor("#727272"));
            viewPager.setBackgroundColor(Color.parseColor("#535353"));
            topbar.setBackgroundColor(Color.parseColor("#232323"));
            view1.setBackgroundColor(Color.parseColor("#cd5252"));
        }
        connectView();
    }
}
