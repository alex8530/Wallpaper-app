package com.wallpaper.alex.wallpaperapp.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.wallpaper.alex.wallpaperapp.R;
import com.wallpaper.alex.wallpaperapp.interfaces.MyItemClickListener;

public class WallPaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {


    public ImageView imgbackground;

    MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public WallPaperViewHolder(View itemView) {
        super(itemView);

        imgbackground= itemView.findViewById(R.id.wallpaper_item_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (myItemClickListener!=null){
            myItemClickListener.onClick(v,getAdapterPosition());
        }


    }
}
