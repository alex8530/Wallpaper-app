package com.example.alex.wallpaperapp.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.alex.wallpaperapp.R;
import com.example.alex.wallpaperapp.interfaces.MyItemClickListener;
import com.example.alex.wallpaperapp.model.WallPaperItem;
import com.example.alex.wallpaperapp.utils.Common;
import com.example.alex.wallpaperapp.viewHolder.WallPaperViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ListWallPaperActivity extends AppCompatActivity {
    private static final String TAG = "ListWallPaperActivity";
    private static final String SCROLL_POSITION_KEY = "SCROLL_POSITION_KEY";
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    GridLayoutManager  mLayoutManager;
    Query query;
    FirebaseRecyclerAdapter<WallPaperItem,WallPaperViewHolder> adapter;
    FirebaseRecyclerOptions<WallPaperItem> options;
     int positin;
    RecyclerView mRecyclerView;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wall_paper);

        Toolbar toolbar = (Toolbar) findViewById(R.id.wallPapertoolbar);
        toolbar.setTitle(Common.CATEGORY_SELECTED);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView= findViewById(R.id.wallpaperRecyleView);
        mRecyclerView.setHasFixedSize(true);
        int numberOfGridColumn=getResources().getInteger(R.integer.grid_number_column);
        mLayoutManager= new GridLayoutManager(this,numberOfGridColumn);
        mRecyclerView.setLayoutManager(mLayoutManager);


        loadBackgroundImage();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();

                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
//                    mRecyclerView.scrollToPosition(positionStart);
                    mRecyclerView.scrollToPosition(positin);

                }
            }
        });

    }

    private void loadBackgroundImage() {

        //get that object from previus activity ... i can use intent to get that id instaed of CATEGORY_SELECTED_ID

        query=FirebaseDatabase.getInstance().getReference(Common.REF_WALLPAPER )
                .orderByChild("categoryId").equalTo(Common.CATEGORY_SELECTED_ID);


        options = new FirebaseRecyclerOptions.Builder<WallPaperItem>()
                .setQuery(query, WallPaperItem.class)
                .build();

        adapter= new FirebaseRecyclerAdapter<WallPaperItem, WallPaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final WallPaperViewHolder holder, int position, @NonNull final WallPaperItem model) {

                Picasso.with(getApplicationContext()).load(model.getImageUrl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.imgbackground, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                //try again online if the first time faild to lod from cash !!
                                Picasso.with(getApplicationContext()).load(model.getImageUrl())
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
                        startActivity(new Intent(getApplicationContext(),ViewWallPaperActivity.class));

                        //this one for view activity and get details from it
                        Common.wallPaperItem = model;


                        /**
                         *   and this key we need it for increse number of view from view activity ..
                         *     so  we need the key of that child for referance it
                         *     we need to set this value from  all places that we are go from it ..so from this class  and  RecentsFragment .(inside its adapter)
                         */
                        Common.WALLPAPERITEM_KEY=adapter.getRef(position).getKey();
                    }
                });

            }

            @NonNull
            @Override
            public WallPaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.layout_wallpaper_item,parent,false);

                int hight=parent.getMeasuredHeight()/2; //todo

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
    protected void onDestroy() {
        super.onDestroy();
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
//
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

         positin = ((GridLayoutManager) (mRecyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();
        outState.putInt(SCROLL_POSITION_KEY, positin);

    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
         if (state != null) {
            positin = state.getInt(SCROLL_POSITION_KEY, 0);
             mRecyclerView.scrollToPosition(positin);

        }

    }



}
