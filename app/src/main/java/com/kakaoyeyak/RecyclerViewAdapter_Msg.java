package com.kakaoyeyak;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.item_text1.setText("Time");
        holder.item_text2.setText("NAME");
        holder.item_text3.setText(R.string.temp_string);
        holder.circleImageView.setImageResource(R.drawable.kakao_default_profile_image);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewer = new Intent(v.getContext(), Item_Msg_Detail.class);
                v.getContext().startActivity(viewer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }


    //ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item_text1;
        public TextView item_text2;
        public TextView item_text3;
        public ImageView imageView;
        public TextView summary;
        ImageView search;
        RelativeLayout layout;
        CircleImageView circleImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            item_text1 = (TextView) itemView.findViewById(R.id.item_text1);
            item_text2 = (TextView) itemView.findViewById(R.id.item_text2);
            item_text3 = (TextView) itemView.findViewById(R.id.item_text3);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.item_msg_profile);
//            name = (TextView) itemView.findViewById(R.id.tv_name_msg);
//            summary = (TextView) itemView.findViewById(R.id.tv_summary_msg);
//            search = (ImageView) itemView.findViewById(R.id.bt_search);
//            search.setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
            layout = (RelativeLayout) itemView.findViewById(R.id.item_msg_layout);
        }
    }
}