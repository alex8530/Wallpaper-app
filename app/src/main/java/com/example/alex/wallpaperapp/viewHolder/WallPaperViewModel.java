package com.example.alex.wallpaperapp.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.wallpaperapp.R;
import com.example.alex.wallpaperapp.interfaces.MyItemClickListener;

public class WallPaperViewModel extends RecyclerView.ViewHolder implements View.OnClickListener  {


    public ImageView imgbackground;


    MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public WallPaperViewModel(View itemView) {
        super(itemView);

        imgbackground= itemView.findViewById(R.id.wallpaper_item_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        myItemClickListener.onClick(v,getAdapterPosition());

    }
}
