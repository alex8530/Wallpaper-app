package com.alex.wallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.wallpaper.R;
import com.alex.wallpaper.ui.ViewWallPaperActivity;
import com.alex.wallpaper.interfaces.MyItemClickListener;
import com.alex.wallpaper.model.RecentItem;
import com.alex.wallpaper.model.WallPaperItem;
import com.alex.wallpaper.utils.Common;
import com.alex.wallpaper.viewHolder.WallPaperViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class RecentAdapter extends RecyclerView.Adapter<WallPaperViewHolder> {


    Context mContext;
    List<RecentItem> recentItemsList;

    public void setRecentItemsList(List<RecentItem> recentItemsList) {
        this.recentItemsList = recentItemsList;
    }

    public RecentAdapter(Context mContext) {
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public WallPaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_wallpaper_item,parent,false);

        int hight=parent.getMeasuredHeight()/2;

        view.setMinimumHeight(hight);

        return new WallPaperViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final WallPaperViewHolder holder, final int position) {

        Picasso.with(mContext).load(recentItemsList.get(position).getImageUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.imgbackground, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        //try again online if the first time faild to lod from cash !!
                        Picasso.with(mContext).load(recentItemsList.get(position).getImageUrl())
                                .error(R.drawable.ic_terrain_black_24dp)
                                .into(holder.imgbackground, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, "onError:  not able to load image"  );

                                    }


                                });

                    }


                });

        holder.setMyItemClickListener(new MyItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //for details
                mContext.startActivity(new Intent(mContext,ViewWallPaperActivity.class));
                WallPaperItem wallPaperItem= new WallPaperItem();
                wallPaperItem.setCategoryId(recentItemsList.get(position).getCategoryId());
                wallPaperItem.setImageUrl(recentItemsList.get(position).getImageUrl());
                Common.wallPaperItem = wallPaperItem;

                //we add this key .. because  if we are in ViewWallPAper activity and write
                // method there to increase number of views..
                // so we need referance to
                // get that child ..and if we are here in this class ..
                // and go to View activity .. we must set the key value to use it for referance child
                //so we must store the key in  RecentItem to be able to retrive key
                 Common.WALLPAPERITEM_KEY=recentItemsList.get(position).getKey();
            }
        });
    }



    @Override
    public int getItemCount() {
        if (recentItemsList ==null) return 0;
        return recentItemsList.size();
    }



}
