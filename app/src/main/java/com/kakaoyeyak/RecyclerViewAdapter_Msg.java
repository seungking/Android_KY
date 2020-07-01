package com.kakaoyeyak;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        holder.summary.setText(mPersons.get(position).summary);

        holder.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }


    //ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView imageView;
        public TextView summary;
        ImageView search;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_name_msg);
            summary = (TextView) itemView.findViewById(R.id.tv_summary_msg);
            search = (ImageView) itemView.findViewById(R.id.bt_search);
            search.setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        }
    }
}