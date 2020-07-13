package com.kakaoyeyak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

//메인 with 네비게이션 텝 바
public class HorizontalNtbActivity extends AppCompatActivity {

    //설정 메시지 항목들
    private ArrayList<Item_Msg> items = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_horizontal_ntb);

        //설정
        initUI();

        //친구목록으로
        findViewById(R.id.addmessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HorizontalNtbActivity.this, FriendsList.class));
            }
        });

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
                final View view;
                //탭 바 위치에 따라서 레이아웃
                if(position==0) {
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.item_vp_list, null, false);

                    //데이터 설정
                    initDataset();

                    //뷰 연결
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_msg);
                    recyclerView.setHasFixedSize(true);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);

                    RecyclerViewAdapter_Msg adapter = new RecyclerViewAdapter_Msg(context, items);
                    recyclerView.setAdapter(adapter);

                }
                else {
                    //세팅
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.setting, null, false);
                    /*
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.activity_setting, null, false);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.settings_fragment, new SettingPreferenceFragmentMain())
                            .addToBackStack(null)
                            .commit();
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
        items.add(new Item_Msg("Message1", "몇시 몇분 누구에게 보냄"));
        items.add(new Item_Msg("Message2", "몇시 몇분 누구에게 보냄"));
        items.add(new Item_Msg("Message3",  "몇시 몇분 누구에게 보냄"));
        items.add(new Item_Msg("Message4",  "몇시 몇분 누구에게 보냄"));
    }

    public void go_settings(View view) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }
}
