package com.example.alex.wallpaperapp.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.wallpaperapp.R;
import com.example.alex.wallpaperapp.interfaces.MyItemClickListener;

import butterknife.BindView;

public class CategoryViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imgbackground;
      public TextView tvName;

      MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public CategoryViewHolder(View itemView) {
        super(itemView);

        imgbackground= itemView.findViewById(R.id.category_item_image);
        tvName= itemView.findViewById(R.id.category_item_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        myItemClickListener.onClick(v,getAdapterPosition());

    }
}
