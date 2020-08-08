package com.kakaoyeyak;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingPreferenceFragmentMain extends PreferenceFragmentCompat {
    SharedPreferences prefs;

    Preference soundPreference;
    //    Preference keywordSoundPreference;
    Preference keywordScreen;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.setting_main, s);

        if (s == null) {
            soundPreference = findPreference("sound_list");
//            keywordSoundPreference = findPreference("keyword_sound_list");
            keywordScreen = findPreference("keyword_screen");

            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            if (!prefs.getString("sound_list", "").equals("")) {
                soundPreference.setSummary(prefs.getString("sound_list", "카톡"));
            }

            if (prefs.getBoolean("keyword", false)) {
                keywordScreen.setSummary("사용");
            }else {
                keywordScreen.setSummary("사용안함");
            }

            prefs.registerOnSharedPreferenceChangeListener(prefListener);
        }

    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("sound_list")) {
                soundPreference.setSummary(prefs.getString("sound_list", "카톡"));
            }
            if (key.equals("keyword")) {
                if (prefs.getBoolean("keyword", false)) {
                    keywordScreen.setSummary("사용");
                }else {
                    keywordScreen.setSummary("사용안함");
                }
            }

        }
    };



}