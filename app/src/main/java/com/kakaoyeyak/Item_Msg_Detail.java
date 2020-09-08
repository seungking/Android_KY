package com.kakaoyeyak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.kakaoyeyak.HorizontalNtbActivity.removeItem;

public class Item_Msg_Detail extends AppCompatActivity {

    Toolbar toolbar;

    ManagePref managePref = new ManagePref();

    private SlidrConfig mConfig;
    int position = 0;

    public String Time;
    public String Message;
    public String NName;
    public String ProfileUri;

    @BindView(R.id.item_msg_detail_nickanme)
    TextView name;

    @BindView(R.id.item_msg_detail_date)
    TextView date;

    @BindView(R.id.item_msg_detail_time)
    TextView time;

    @BindView(R.id.item_msg_detail_msg)
    TextView msg;

    @BindView(R.id.item_msg_detail_profile)
    CircleImageView profile;

    @BindView(R.id.item_msg_detail_delete)
    CircleImageView delete;

    @BindView(R.id.item_msg_detail_edit)
    CircleImageView edit;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__msg__detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);

        Time = intent.getExtras().getString("time");
        Message = intent.getExtras().getString("message");
        NName = intent.getExtras().getString("name");
        ProfileUri = intent.getExtras().getString("profile");

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        name.setText(NName);
        time.setText(Time);
        msg.setText(Message);
        Glide.with(this)
                .load(ProfileUri)
                .centerCrop()
                .into(profile);

        mConfig = new SlidrConfig.Builder()
                .primaryColor(getResources().getColor(R.color.primaryDark))
                .secondaryColor(Color.WHITE)
                .position(SlidrPosition.HORIZONTAL)
                .velocityThreshold(2400)
//                .distanceThreshold(.25f)
//                .edge(true)
                .touchSize(32)
                .build();

        Slidr.attach(this, mConfig);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Item_Msg_Detail.this, YeyakMain.class));
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity.removeItem(position, getApplicationContext());
                finish();
            }
        });

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