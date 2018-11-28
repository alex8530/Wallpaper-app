package com.example.alex.wallpaperapp;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.alex.wallpaperapp.R;
import com.example.alex.wallpaperapp.utils.Common;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewWallPaperActivity extends AppCompatActivity {

    @BindView(R.id.viewpaperCollapse)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.viewpaperFab)
    FloatingActionButton mFab;
    @BindView(R.id.viewwallpaper_image)
    ImageView mImageViewBackground;
    @BindView(R.id.viewpaperCoordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;


    private Target target= new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
            try {
                manager.setBitmap(bitmap);
                Snackbar.make(mCoordinatorLayout,"WallPaper was Successfully Set !!",Snackbar.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wall_paper);
        ButterKnife.bind(this);


        Toolbar toolbar =   findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        //init
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsAppbar);
         mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        mCollapsingToolbarLayout.setTitle(Common.CATEGORY_SELECTED);
        Picasso.with(this).load(Common.wallPaperItem.getImageUrl()).into(mImageViewBackground);


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when click on fab ,,we load image into targent ,, and when the load is comlpeted.. it will triger the listener ..
                // and the code in that target will excute..
                Picasso.with(getApplicationContext()).load(Common.wallPaperItem.getImageUrl()).into(target);
            }
        });




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
