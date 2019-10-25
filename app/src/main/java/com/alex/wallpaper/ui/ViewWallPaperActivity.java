package com.alex.wallpaper.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alex.wallpaper.executer.AppExecutors;
import com.alex.wallpaper.R;
import com.alex.wallpaper.database.AppDatabase;
import com.alex.wallpaper.imageProcessing.SaveImageHelper;
import com.alex.wallpaper.model.RecentItem;
import com.alex.wallpaper.model.WallPaperItem;
import com.alex.wallpaper.utils.Common;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

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
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;


public class ViewWallPaperActivity extends AppCompatActivity {


    RelativeLayout userRootRelativeLayout;

    @BindView(R.id.viewpaperFab)
    com.github.clans.fab.FloatingActionButton mFab;


    @BindView(R.id.viewpaperFabDownload)
    com.github.clans.fab.FloatingActionButton mFabDownlaod;



    @BindView(R.id.viewwallpaper_image)
    ImageView mImageViewBackground;



    @BindView(R.id.fabFacebook)
    com.github.clans.fab.FloatingActionButton fabFacebook;
    @BindView(R.id.fabInstgram)
    com.github.clans.fab.FloatingActionButton fabInstgram;





    //Facebook callback
    CallbackManager callbackManager;
    ShareDialog shareDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wall_paper);
        ButterKnife.bind(this);
        userRootRelativeLayout =findViewById(R.id.rootRelativeLayout);

        Toolbar toolbar =   findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        //init faacebook
        callbackManager= CallbackManager.Factory.create();
        shareDialog= new ShareDialog(this);


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


        fabFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //callback create
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ViewWallPaperActivity.this, "Succesfuly Share !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ViewWallPaperActivity.this, "Share Cancel !", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {

                        Toast.makeText(ViewWallPaperActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


                //i need get image from link and convert to bitmap !!
                Picasso.with(getApplicationContext()).load(Common.wallPaperItem.getImageUrl())
                        .into(faceboockBitmapConverted);




            }
        });


        fabInstgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Picasso.with(getApplicationContext()).load(Common.wallPaperItem.getImageUrl())
                        .into(InstgramBitmapConverted);


            }
        });




  }

    private void createInstagramIntent(Uri uri){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);
        //to show only instgram in the Chooser
        share.setPackage("com.instagram.android");

        share.setType("image/*");

        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
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


    private Target InstgramBitmapConverted = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            //when bitmap ready.. store it in midea to be able to share instgram
            String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"imageIn", null);
            Uri bitmapUri = Uri.parse(bitmapPath);

            createInstagramIntent(bitmapUri);


        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    private Target faceboockBitmapConverted = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            SharePhoto sharePhoto= new SharePhoto.Builder()
                    .setBitmap(bitmap).build();
            if (ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent sharePhotoContent=  new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                shareDialog.show(sharePhotoContent);
            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private Target target= new Target() {


        //    Callback when an image has been successfully loaded.
//    Note: You must not recycle the bitmap.

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
            try {
                manager.setBitmap(bitmap);
                Snackbar.make(userRootRelativeLayout,"WallPaper was Successfully Set !!",Snackbar.LENGTH_LONG).show();
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


}
