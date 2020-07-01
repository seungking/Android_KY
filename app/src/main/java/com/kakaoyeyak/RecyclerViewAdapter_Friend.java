package com.kakaoyeyak;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//친구 목록 용 리사이클러뷰 어뎁터
public class RecyclerViewAdapter_Friend extends RecyclerView.Adapter<RecyclerViewAdapter_Friend.MyViewHolder>{

    private ArrayList<Item_Friend> mFriend;
    private LayoutInflater mInflate;
    private Context mContext;

    public RecyclerViewAdapter_Friend(Context context, ArrayList<Item_Friend> friends) {
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mFriend = friends;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_Friend.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.list_item_friend, parent, false);
        RecyclerViewAdapter_Friend.MyViewHolder viewHolder = new RecyclerViewAdapter_Friend.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_Friend.MyViewHolder holder, final int position) {
        holder.nickname.setText("" + mFriend.get(position).nickname);
        holder.uuid.setText("" + mFriend.get(position).uuid);
        holder.userid.setText("" + mFriend.get(position).userid);

        Glide.with(mContext)
                .load(mFriend.get(position).profileimage)
                .centerCrop()
                .into(holder.profileimage);

        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), YeyakMain.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFriend.size();
    }

    //ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView uuid;
        public TextView userid;
        public CircleImageView profileimage;
        public ImageView select;

        public MyViewHolder(View itemView) {
            super(itemView);
            nickname = (TextView) itemView.findViewById(R.id.tv_nickname_friend);
            uuid = (TextView) itemView.findViewById(R.id.tv_uuid_friend);
            userid = (TextView) itemView.findViewById(R.id.tv_userid_friend);
            profileimage = (CircleImageView) itemView.findViewById(R.id.profile_friend);
            select = (ImageView) itemView.findViewById(R.id.bt_select_friend);
        }
    }

    class TCPTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
