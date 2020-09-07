package com.kakaoyeyak;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//예약 메시지 목록 리사이클러 뷰 어뎁터
public class RecyclerViewAdapter_Msg extends RecyclerView.Adapter<RecyclerViewAdapter_Msg.MyViewHolder> {

    private ArrayList<Item_Msg> mPersons;
    private LayoutInflater mInflate;
    private Context mContext;

    public RecyclerViewAdapter_Msg(Context context, ArrayList<Item_Msg> persons) {
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mPersons = persons;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.list_item_msg, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
//        holder.summary.setText(mPersons.get(position).summary);
        holder.item_text1.setText(mPersons.get(position).getTime());
        holder.item_text2.setText(mPersons.get(position).getName());
        holder.item_text3.setText(mPersons.get(position).getSummary());

        Glide.with(mContext)
                .load(mPersons.get(position).profileimage)
                .centerCrop()
                .into(holder.circleImageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewer = new Intent(v.getContext(), Item_Msg_Detail.class);
                viewer.putExtra("position",position);
                viewer.putExtra("time",mPersons.get(position).getTime());
                viewer.putExtra("name",mPersons.get(position).getName());
                viewer.putExtra("message",mPersons.get(position).getSummary());
                viewer.putExtra("profile",mPersons.get(position).getProfileimage());
                v.getContext().startActivity(viewer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }


    public void removeItem(int position){

        Log.d("LOG1","어뎁터 removeItem" + position);

        ManagePref managePref = new ManagePref();

        ArrayList<String> B_id = managePref.getStringArrayPref(mContext,"BroadCastID");
        ArrayList<String> Time = managePref.getStringArrayPref(mContext,"time");
        ArrayList<String> Id = managePref.getStringArrayPref(mContext,"id");
        ArrayList<String> Name = managePref.getStringArrayPref(mContext,"name");
        ArrayList<String> Message = managePref.getStringArrayPref(mContext,"message");
        ArrayList<String> Profile = managePref.getStringArrayPref(mContext,"profile");


        B_id.remove(position);
        Time.remove(position);
        Id.remove(position);
        Name.remove(position);
        Message.remove(position);
        Profile.remove(position);


        managePref.setStringArrayPref(mContext,"BroadCastID",B_id);
        managePref.setStringArrayPref(mContext,"time",Time);
        managePref.setStringArrayPref(mContext,"id",Id);
        managePref.setStringArrayPref(mContext,"name",Name);
        managePref.setStringArrayPref(mContext,"message",Message);
        managePref.setStringArrayPref(mContext,"profile",Profile);

        Log.d("LOG1","어뎁터 삭제완료");
        mPersons.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, mPersons.size());
        Log.d("LOG1","어뎁터 업데이트");
    }

    //ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item_text1;
        public TextView item_text2;
        public TextView item_text3;
        ConstraintLayout layout;
        ConstraintLayout layout_inside;
        CircleImageView circleImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            item_text1 = (TextView) itemView.findViewById(R.id.item_text1);
            item_text2 = (TextView) itemView.findViewById(R.id.item_text2);
            item_text3 = (TextView) itemView.findViewById(R.id.item_text3);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.item_msg_profile);
            layout = (ConstraintLayout) itemView.findViewById(R.id.item_msg_layout);
        }
    }
}