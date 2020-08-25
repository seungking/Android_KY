package com.kakaoyeyak;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

public class SettingPreferenceFragmentMain extends PreferenceFragmentCompat {
    SharedPreferences prefs;
    SwitchPreference setNotification;
    Preference setSound;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.setting_main, s);

        setNotification = (SwitchPreference)findPreference("set_notification");
        setSound = (Preference)findPreference("set_sound");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);



    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("set_notification")){
                boolean set_noti = prefs.getBoolean("set_notification", false);
                //Toast.makeText(getContext(),"알림: "+set_noti, Toast.LENGTH_SHORT).show();

            }


        }
    };



}