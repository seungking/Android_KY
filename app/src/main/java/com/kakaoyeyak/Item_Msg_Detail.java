package com.kakaoyeyak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

public class Item_Msg_Detail extends AppCompatActivity {

    public static final String EXTRA_OS = "extra_os_version";

    Toolbar toolbar;

//    @BindView(R.id.title)
//    TextView mTitle;

    private SlidrConfig mConfig;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__msg__detail);
//        ButterKnife.bind(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        // Get the status bar colors to interpolate between
        int primary = getResources().getColor(R.color.primaryDark);
        int secondary = Color.WHITE;

        mConfig = new SlidrConfig.Builder()
                .primaryColor(primary)
                .secondaryColor(secondary)
                .position(SlidrPosition.HORIZONTAL)
                .velocityThreshold(2400)
                .distanceThreshold(.25f)
                .edge(true)
//                .touchSize(SizeUtils.dpToPx(this, 32))
                .build();

        // Attach the Slidr Mechanism to this activity
        Slidr.attach(this, mConfig);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}