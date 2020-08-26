package com.kakaoyeyak;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import androidx.preference.SwitchPreference;

import java.util.ArrayList;

import androidx.preference.PreferenceScreen;


public class SettingPreferenceFragmentMain extends PreferenceFragmentCompat {
    SharedPreferences prefs;
    SwitchPreference setNotification;
    SwitchPreference setSound;
    SwitchPreference setVibrate;

    ArrayList<String> arrSetNoti = new ArrayList<String>();
    ArrayList<String> arrSetSound = new ArrayList<String>();
    ArrayList<String> arrSetVibrate = new ArrayList<String>();


    ManagePref managePref = new ManagePref();

    private Boolean mode = false;

    Preference soundPreference;
    //    Preference keywordSoundPreference;
    Preference keywordScreen;

    private View view;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.setting_main, s);


        TypefaceUtil.overrideFont(getContext(), "SERIF", "fonts/imcresoojin.ttf");

        setNotification = (SwitchPreference)findPreference("set_notification");
        setSound = (SwitchPreference)findPreference("set_sound");
        setVibrate = (SwitchPreference)findPreference("set_vibrate");

        Log.d("log1","preference screen");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

        boolean set_noti = prefs.getBoolean("set_notification", false);
        boolean set_sound = prefs.getBoolean("set_sound", false);
        boolean set_vibrate = prefs.getBoolean("set_vibrate", false);

        Log.e("PUSH","초기 환경 설정을 파악합니다 [알림,소리,진동]: ["+set_noti+set_sound+set_vibrate+"]");
        arrSetNoti = managePref.getStringArrayPref(getContext(),"isPush");
        arrSetSound = managePref.getStringArrayPref(getContext(),"isSound");
        arrSetVibrate = managePref.getStringArrayPref(getContext(),"isVibrate");

        arrSetNoti.clear();
        arrSetSound.clear();
        arrSetVibrate.clear();

        arrSetNoti.add(String.valueOf(set_noti));
        arrSetSound.add(String.valueOf(set_sound));
        arrSetVibrate.add(String.valueOf(set_vibrate));

        managePref.setStringArrayPref(getContext(),"isPush",arrSetNoti);
        managePref.setStringArrayPref(getContext(),"isSound",arrSetSound);
        managePref.setStringArrayPref(getContext(),"isVibrate",arrSetVibrate);

        Log.e("PUSH","저장된 환경 설정 DB를 확인합니다 [알림,소리,진동]: " +
                "["+arrSetNoti.toString()+arrSetSound.toString()+arrSetVibrate.toString()+"]");


    }

    @Override
    public void onStart() {
        super.onStart();
        mode = prefs.getBoolean("screen_mode", false);
        ((HorizontalNtbActivity)getActivity()).setColor(mode);
        setScreenMode(mode);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // push 알림에 따라 달라짐.
            boolean set_noti;

            if(key.equals("set_notification")) {
                set_noti = prefs.getBoolean("set_notification", false);
                //Toast.makeText(getContext(),"알림: "+set_noti, Toast.LENGTH_SHORT).show();
                Log.e("push 알림을 변경합니다.", "" + set_noti);
                arrSetNoti = managePref.getStringArrayPref(getContext(), "isPush");
                arrSetNoti.clear();
                arrSetNoti.add(String.valueOf(set_noti));
                managePref.setStringArrayPref(getContext(), "isPush", arrSetNoti);
                Log.e("push 알림: ", arrSetNoti.toString());


                // push 알림을 끈다면, 진동과 소리 모두 false
                if(set_noti == false){
                    boolean set_sound = false;
                    boolean set_vibrate = false;
                    arrSetSound = managePref.getStringArrayPref(getContext(),"isSound");
                    arrSetVibrate = managePref.getStringArrayPref(getContext(),"isVibrate");
                    arrSetSound.clear();
                    arrSetVibrate.clear();
                    arrSetSound.add(String.valueOf(set_sound));
                    arrSetVibrate.add(String.valueOf(set_vibrate));
                    managePref.setStringArrayPref(getContext(),"isSound",arrSetSound);
                    managePref.setStringArrayPref(getContext(),"isVibrate",arrSetVibrate);
                    Log.e("push 알림 off","소리, 진동 off"+arrSetSound.toString()+arrSetVibrate.toString());
                }
                // push 알람을 킨다면, 진동과 소리 복원
                else{
                    boolean set_sound = prefs.getBoolean("set_sound", false);
                    boolean set_vibrate = prefs.getBoolean("set_vibrate", false);
                    arrSetSound = managePref.getStringArrayPref(getContext(),"isSound");
                    arrSetVibrate = managePref.getStringArrayPref(getContext(),"isVibrate");
                    arrSetSound.clear();
                    arrSetVibrate.clear();
                    arrSetSound.add(String.valueOf(set_sound));
                    arrSetVibrate.add(String.valueOf(set_vibrate));
                    managePref.setStringArrayPref(getContext(),"isSound",arrSetSound);
                    managePref.setStringArrayPref(getContext(),"isVibrate",arrSetVibrate);
                    Log.e("push 알림 on","소리, 진동 on"+arrSetSound.toString()+arrSetVibrate.toString());
                }
            }
            // push 알림이 켜져 있는 상태라면, 진동과 소리 설정 가능
            if(key.equals("set_sound")){
                // 소리 설정
                boolean set_sound = prefs.getBoolean("set_sound", false);
                Log.e("알림 소리 설정을 변경합니다.",""+set_sound);
                arrSetSound = managePref.getStringArrayPref(getContext(),"isSound");
                arrSetSound.clear();
                arrSetSound.add(String.valueOf(set_sound));
                managePref.setStringArrayPref(getContext(),"isSound",arrSetSound);
                Log.e("알림 소리: ",arrSetSound.toString());
            }
            if(key.equals("set_vibrate")){
                // 진동 설정
                boolean set_vibrate = prefs.getBoolean("set_vibrate", false);
                Log.e("알림 진동 설정을 변경합니다.",""+set_vibrate);
                arrSetVibrate = managePref.getStringArrayPref(getContext(),"isVibrate");
                arrSetVibrate.clear();
                arrSetVibrate.add(String.valueOf(set_vibrate));
                managePref.setStringArrayPref(getContext(),"isVibrate",arrSetVibrate);
                Log.e("알림 소리: ",arrSetVibrate.toString());
            }

            if (key.equals("screen_mode")) {
                mode = sharedPreferences.getBoolean("screen_mode", false);
                ((HorizontalNtbActivity)getActivity()).setColor(mode);
                setScreenMode(mode);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(prefListener);
    }


    public void setScreenMode(Boolean mode){
        if (mode) {
            Log.d("log1", "screen mode : black");
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            Log.d("log1", "screen mode : black");
            view.setBackgroundColor(Color.parseColor("#CFCECE"));
        }


    }
}