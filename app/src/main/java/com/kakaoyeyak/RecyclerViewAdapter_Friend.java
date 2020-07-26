package com.kakaoyeyak;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//친구 목록 용 리사이클러뷰 어뎁터
public class RecyclerViewAdapter_Friend extends RecyclerView.Adapter<RecyclerViewAdapter_Friend.MyViewHolder>{

    private ArrayList<Item_Friend> mFriend;
    private LayoutInflater mInflate;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

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
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter_Friend.MyViewHolder holder, final int position) {
        holder.nickname.setText("" + mFriend.get(position).nickname);
        holder.uuid.setText("" + mFriend.get(position).uuid);
        holder.userid.setText("" + mFriend.get(position).userid);

        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mFriend.get(position).getLetters());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }

        holder.holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, mFriend.get(position).getNickname(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), YeyakMain.class);

                // 알람 동적 설정을 위해 YeyakMain의 broadcastCode를 증가시킴.
                YeyakMain.broadcastCode++;

                // 해당 유저 uid, id, 닉네임 다음 창으로 가져감
                intent.putExtra("UserUID",mFriend.get(position).uuid);
                intent.putExtra("UserID",mFriend.get(position).userid);
                intent.putExtra("UserName",mFriend.get(position).nickname);
                intent.putExtra("ProfileUri",mFriend.get(position).profileimage);
                view.getContext().startActivity(intent);
            }
        });

        Glide.with(mContext)
                .load(mFriend.get(position).profileimage)
                .centerCrop()
                .into(holder.profileimage);

        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), YeyakMain.class);

        // 알람 동적 설정을 위해 YeyakMain의 broadcastCode를 증가시킴.
        YeyakMain.broadcastCode++;

        // 해당 유저 uid, id, 닉네임 다음 창으로 가져감
        intent.putExtra("UserUID",mFriend.get(position).uuid);
        intent.putExtra("UserID",mFriend.get(position).userid);
        intent.putExtra("UserName",mFriend.get(position).nickname);
        intent.putExtra("ProfileUri",mFriend.get(position).profileimage);
        view.getContext().startActivity(intent);

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
        public TextView tvTag;
        public LinearLayout holder;

        public MyViewHolder(View itemView) {
            super(itemView);
            nickname = (TextView) itemView.findViewById(R.id.tv_nickname_friend);
            uuid = (TextView) itemView.findViewById(R.id.tv_uuid_friend);
            userid = (TextView) itemView.findViewById(R.id.tv_userid_friend);
            profileimage = (CircleImageView) itemView.findViewById(R.id.profile_friend);
            select = (ImageView) itemView.findViewById(R.id.bt_select_friend);
            tvTag = (TextView) itemView.findViewById(R.id.tag);
            holder = (LinearLayout) itemView.findViewById(R.id.item_friend_holder);
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

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //**************************************************************

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvName;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 提供给Activity刷新数据
     * @param list
     */
    public void updateList(ArrayList<Item_Friend> list){
        this.mFriend = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mFriend.get(position);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mFriend.get(position).getLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mFriend.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
