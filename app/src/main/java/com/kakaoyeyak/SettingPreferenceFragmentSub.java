package com.kakaoyeyak;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingPreferenceFragmentSub extends PreferenceFragmentCompat {
    SharedPreferences prefs;

    Preference keywordSoundPreference;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.setting_sub, s);

        if (s == null) {
            keywordSoundPreference = findPreference("keyword_sound_list");

            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            if (!prefs.getString("keyword_sound_list", "").equals("")) {
                keywordSoundPreference.setSummary(prefs.getString("keyword_sound_list", "카톡"));
            }

            prefs.registerOnSharedPreferenceChangeListener(prefListener);
        }

    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals("keyword_sound_list")) {
                Log.d("kkang", "keyword_sound_list : "+prefs.getString("keyword_sound_list", "카톡"));
                keywordSoundPreference.setSummary(prefs.getString("keyword_sound_list", "카톡"));
            }
        }
    };

}