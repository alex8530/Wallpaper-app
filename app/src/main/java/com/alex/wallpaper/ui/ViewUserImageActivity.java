package com.alex.wallpaper.ui;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alex.wallpaper.R;
import com.alex.wallpaper.utils.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewUserImageActivity extends AppCompatActivity {


    @BindView(R.id.fabUserDelete)
    com.github.clans.fab.FloatingActionButton fabUserDelete;

    FirebaseUser currentUser;
    @BindView(R.id.user_image_background)
    ImageView imageView;


    @BindView(R.id.fabUserSetWallpaper)
    com.github.clans.fab.FloatingActionButton  mFab;

    @BindView(R.id.userRootRelativeLayout)
    RelativeLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_image);
        ButterKnife.bind(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar =   findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        loadImage();

        fabUserDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
                finish();
            }
        });


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Picasso.with(getApplicationContext()).load(Common.userImage.getImageURL()).into(target);
            }
        });



    }

    private void loadImage() {
        Picasso.with(this).load(Common.userImage.getImageURL()).into(imageView);
    }

    private void deleteImage() {
        FirebaseDatabase.getInstance().getReference("UsersImages").child(currentUser.getUid())
                .child(Common.DELETE_KEY_IMAGE_USER).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //now delete also from storage

                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(Common.userImage.getImageURL());
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        Toast.makeText(ViewUserImageActivity.this, "The image was deleted ", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                         Toast.makeText(ViewUserImageActivity.this, "Can not deleted ..", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });



    }

    private Target target= new Target() {


        //    Callback when an image has been successfully loaded.


        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
            try {
                manager.setBitmap(bitmap);
                Snackbar.make(mRootView,"WallPaper was Successfully Set !!",Snackbar.LENGTH_LONG).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
