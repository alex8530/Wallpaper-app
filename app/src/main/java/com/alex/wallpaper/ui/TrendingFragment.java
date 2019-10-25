package com.alex.wallpaper.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alex.wallpaper.R;
import com.alex.wallpaper.interfaces.MyItemClickListener;
import com.alex.wallpaper.model.WallPaperItem;
import com.alex.wallpaper.utils.Common;
import com.alex.wallpaper.viewHolder.WallPaperViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {
    private static final String TAG = "TrendingFragment";
    FirebaseDatabase database;
    DatabaseReference wallpaperBakeground;

    LinearLayoutManager layoutManager;
    //firebaseUi adapter
    FirebaseRecyclerOptions<WallPaperItem> options;
    FirebaseRecyclerAdapter<WallPaperItem,WallPaperViewHolder> adapter;
    int numberOfItem;

    @BindView(R.id.fragment_trending_rv)
    RecyclerView recyclerViewTrending;


    private static TrendingFragment Instance ;

    public TrendingFragment() {
        // Required empty public constructor
        database=FirebaseDatabase.getInstance();
        wallpaperBakeground=database.getReference(Common.REF_WALLPAPER);



        Query query = wallpaperBakeground.orderByChild("numberViews").limitToLast(10);
        //NOte the limitToLast order assending... and i need the largest views number so i get from last !!


        options = new FirebaseRecyclerOptions.Builder<WallPaperItem>()
                .setQuery(query, WallPaperItem.class)
                .build();

        adapter= new FirebaseRecyclerAdapter<WallPaperItem, WallPaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final WallPaperViewHolder holder, int position, @NonNull final WallPaperItem model) {

                 Picasso.with(getActivity()).load(model.getImageUrl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.imgbackground, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                //try again online if the first time faild to lod from cash !!
                                Picasso.with(getActivity()).load(model.getImageUrl())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.imgbackground, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Toast.makeText(getActivity(), "not able to load image", Toast.LENGTH_SHORT).show();

                                            }


                                        });
                            }


                        });




                holder.setMyItemClickListener(new MyItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //go to details of this image !!

                        Common.WALLPAPERITEM_KEY= adapter.getRef(position).getKey();
                        Common.wallPaperItem=model ;
                        startActivity(new Intent(getActivity(),ViewWallPaperActivity.class));
                    }
                });
            }

            @Override
            public WallPaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                 View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_wallpaper_item,parent,false);

                return new WallPaperViewHolder(view);
            }
        };
          numberOfItem = adapter.getItemCount();


    }

    public static TrendingFragment getInstance(){
        if (Instance==null) {
            Instance= new TrendingFragment();
        }

        return Instance;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_trending, container, false);
        ButterKnife.bind(this,view);


        recyclerViewTrending.setHasFixedSize(true);
          layoutManager= new LinearLayoutManager(getActivity());
        //we need to reverse the order because is order assending and we want the first one be large!!
        layoutManager.setReverseLayout(true);

        recyclerViewTrending.setLayoutManager(layoutManager);


        adapter.startListening();
        recyclerViewTrending.setAdapter(adapter);




        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                //I need the first position so 0 , but because i revese it .. it will be the last element for example 1000
                //the item on the top is the last element ,

           recyclerViewTrending.scrollToPosition(adapter.getItemCount()-1);//-1 is very impoetant !!!!!

            }
        });
        return view;
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

}
