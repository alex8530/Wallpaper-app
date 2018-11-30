package com.example.alex.wallpaperapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alex.wallpaperapp.R;
import com.example.alex.wallpaperapp.database.AppDatabase;
import com.example.alex.wallpaperapp.imageProcessing.SaveImageHelper;
import com.example.alex.wallpaperapp.model.RecentItem;
import com.example.alex.wallpaperapp.model.WallPaperItem;
import com.example.alex.wallpaperapp.utils.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;


public class ViewWallPaperActivity extends AppCompatActivity {
    private static final String TAG = "ViewWallPaperActivity";
    @BindView(R.id.viewpaperCollapse)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.viewpaperFab)
    FloatingActionButton mFab;
    @BindView(R.id.viewwallpaper_image)
    ImageView mImageViewBackground;
    @BindView(R.id.viewpaperCoordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.viewpaperFabDownload)
    FloatingActionButton mFabDownlaod;

    private Target target= new Target() {


 //    Callback when an image has been successfully loaded.
//    Note: You must not recycle the bitmap.

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

        mFabDownlaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //request runtime permission external storage
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        !=PackageManager.PERMISSION_GRANTED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},Common.PERMISSION_REQUEST_CODE);
                    }
                }
                else
                {

                  shoeDailogAndDownloadImage();
                }
            }
        });


        /**
         *
         * Now, after i click on image wall paper .. i need to add this image to recent fragemnt .. so
         * i will insert in database with new object
         *
         */

        addToRecent();
        increseNubmerViews();
  }

    private void increseNubmerViews() {


        //this Common.WALLPAPERITEM_KEY must be set from everywhere coming from to this class !!
        final DatabaseReference reference =FirebaseDatabase.getInstance()
                .getReference(Common.REF_WALLPAPER).child(Common.WALLPAPERITEM_KEY);



                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("numberViews"))
                        {

                            WallPaperItem wallPaperItem= dataSnapshot.getValue(WallPaperItem.class);
                            assert wallPaperItem != null;
                            long numberViews= wallPaperItem.getNumberViews()+1;

                            //ubdate now
                            Map<String,Object> map= new HashMap<>();
                            map.put("numberViews",numberViews);

                            reference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ViewWallPaperActivity.this, "Faild to updated View Number", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                        else //if numberVIews not init .. set to 1

                       {

                           Map<String,Object> map= new HashMap<>();
                           map.put("numberViews",1);

                           reference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {

                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(ViewWallPaperActivity.this, "Faild to set View Number", Toast.LENGTH_SHORT).show();
                               }
                           });


                       }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void addToRecent() {


        final RecentItem recent = new RecentItem();
        recent.setCategoryId(Common.wallPaperItem.getCategoryId());
        recent.setImageUrl(Common.wallPaperItem.getImageUrl());

        String lastTimeVisit =String.valueOf(System.currentTimeMillis());
        recent.setLastTimeVisited(lastTimeVisit);
        recent.setKey(Common.WALLPAPERITEM_KEY);


            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {


                    AppDatabase.getInstance(getApplicationContext()).recentItemDao().insertRecent(recent);
                    Log.d(TAG, "run: insert!!!!!!!!");

                }
            });



    }

    private void shoeDailogAndDownloadImage() {
        //this dailog is bettar than normal dialog..

        AlertDialog dialog =  new SpotsDialog.Builder().setContext(ViewWallPaperActivity.this)
                .setMessage("Downloading..")
                .build();

        dialog.show();

        String fileNAme= UUID.randomUUID().toString()+".png";
        Picasso.with(getApplicationContext()).load(Common.wallPaperItem.getImageUrl())
                .into(new SaveImageHelper(
                        getApplicationContext(),
                        dialog,
                        getContentResolver(),
                        fileNAme,
                        "Alex Image"));

    }

    @Override
    protected void onDestroy() {
        Picasso.with(getApplicationContext()).cancelRequest(target);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==Common.PERMISSION_REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show();

                shoeDailogAndDownloadImage();

            }else {
                Toast.makeText(this, "You must accept this permission to be able to download images", Toast.LENGTH_LONG).show();
            }

        }
    }




}
