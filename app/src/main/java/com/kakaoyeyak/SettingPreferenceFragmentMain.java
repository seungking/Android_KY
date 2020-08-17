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

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        }
    };



}