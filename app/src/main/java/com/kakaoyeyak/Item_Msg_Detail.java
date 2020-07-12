package com.kakaoyeyak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class Item_Msg_Detail extends AppCompatActivity {

    Toolbar toolbar;

    ArrayList<String> Hour = new ArrayList<String>();
    ArrayList<String> Minute = new ArrayList<String>();
    ArrayList<String> Id = new ArrayList<String>();
    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<String> Message = new ArrayList<String>();
    ArrayList<String> profileimages = new ArrayList<String>();

    ManagePref managePref = new ManagePref();

    private SlidrConfig mConfig;
    int position = 0;

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
        initData();

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        name.setText(Name.get(position));
        time.setText(Hour.get(position) + "  :  " +  Minute.get(position));
        msg.setText(Message.get(position));
        Glide.with(this)
                .load(profileimages.get(position))
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

                Hour.remove(position);
                Minute.remove(position);
                Id.remove(position);
                Name.remove(position);
                Message.remove(position);
                profileimages.remove(position);

                managePref.setStringArrayPref(Item_Msg_Detail.this,"hour",Hour);
                managePref.setStringArrayPref(Item_Msg_Detail.this,"minute",Minute);
                managePref.setStringArrayPref(Item_Msg_Detail.this,"id",Id);
                managePref.setStringArrayPref(Item_Msg_Detail.this,"name",Name);
                managePref.setStringArrayPref(Item_Msg_Detail.this,"message",Message);
                managePref.setStringArrayPref(Item_Msg_Detail.this,"profileimage",profileimages);

                HorizontalNtbActivity.removeItem(position);
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

    private void initData() {

        Hour = managePref.getStringArrayPref(this,"hour");
        Minute = managePref.getStringArrayPref(this,"minute");
        Id = managePref.getStringArrayPref(this,"id");
        Name = managePref.getStringArrayPref(this,"name");
        Message = managePref.getStringArrayPref(this,"message");
        profileimages = managePref.getStringArrayPref(this,"profileimage");

    }
}