package com.example.alex.wallpaperapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.wallpaperapp.R;
import com.example.alex.wallpaperapp.ViewWallPaperActivity;
import com.example.alex.wallpaperapp.interfaces.MyItemClickListener;
import com.example.alex.wallpaperapp.model.RecentItem;
import com.example.alex.wallpaperapp.model.WallPaperItem;
import com.example.alex.wallpaperapp.utils.Common;
import com.example.alex.wallpaperapp.viewHolder.WallPaperViewHolder;
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



    public List<RecentItem> getRecentItemsList() {
        return recentItemsList;
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
            }
        });
    }



    @Override
    public int getItemCount() {
        if (recentItemsList ==null) return 0;
        return recentItemsList.size();
    }



}
