package com.alex.wallpaper.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alex.wallpaper.R;
import com.alex.wallpaper.interfaces.MyItemClickListener;
import com.alex.wallpaper.model.UserImage;
import com.alex.wallpaper.utils.Common;
import com.alex.wallpaper.viewHolder.WallPaperViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ListUserImagesActivity extends AppCompatActivity {

    Query query;
    FirebaseRecyclerAdapter<UserImage, WallPaperViewHolder> adapter;
    FirebaseRecyclerOptions<UserImage> options;
    private static final String TAG = "ListUserImagesActivity";

    RecyclerView mRecyclerView;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_images);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setUpToolbar();
        setUpRecyleview();
         loadUserImages();


        MobileAds.initialize(this, Common.AD_ID);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    private void setUpRecyleview() {
        mRecyclerView = findViewById(R.id.userImagesRecyleView);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.userImagestoolbar);
        toolbar.setTitle("Your Images");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void loadUserImages() {
        query = FirebaseDatabase.getInstance().getReference("UsersImages").child(currentUser.getUid());


        options = new FirebaseRecyclerOptions.Builder<UserImage>()
                .setQuery(query, UserImage.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<UserImage, WallPaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final WallPaperViewHolder holder, int position, @NonNull final UserImage model) {

                Picasso.with(getApplicationContext()).load(model.getImageURL())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.imgbackground, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                //try again online if the first time faild to lod from cash !!
                                Picasso.with(getApplicationContext()).load(model.getImageURL())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.imgbackground, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {

                                                Toast.makeText(ListUserImagesActivity.this, "not able to load image", Toast.LENGTH_SHORT).show();

                                            }


                                        });
                            }


                        });

                holder.setMyItemClickListener(new MyItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //for details
                         Common.userImage=model;
                        startActivity(new Intent(getApplicationContext(), ViewUserImageActivity.class));
                        String key = adapter.getRef(position).getKey();//i need this to delete
                        Common.DELETE_KEY_IMAGE_USER=key;


                    }
                });


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
        };

        adapter.startListening();
        mRecyclerView.setAdapter(adapter);

    }



    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null){

            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.startListening();
        }
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