package com.kakaoyeyak;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

public class SettingPreferenceFragmentMain extends PreferenceFragmentCompat {
    SharedPreferences prefs;

    private Boolean mode = false;

    Preference soundPreference;
    //    Preference keywordSoundPreference;
    Preference keywordScreen;

    private View view;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.setting_main, s);

        Log.d("log1","preference screen");
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

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

            if (key.equals("screen_mode")) {
                mode = sharedPreferences.getBoolean("screen_mode", false);
                ((HorizontalNtbActivity)getActivity()).setColor(mode);
                setScreenMode(mode);
            }
        }
    };

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