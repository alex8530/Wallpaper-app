package com.alex.wallpaper.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.wallpaper.R;
import com.alex.wallpaper.interfaces.MyItemClickListener;
import com.alex.wallpaper.model.Category;
import com.alex.wallpaper.utils.Common;
import com.alex.wallpaper.viewHolder.CategoryViewHolder;
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
public class CategoryFragment extends Fragment {
    private static final String TAG = "CategoryFragment";
    private static final String SCROLL_POSITION_KEY ="key" ;


    FirebaseDatabase database;
    DatabaseReference categoriesBakeground;
     int positin;

    //firebaseUi adapter
    FirebaseRecyclerOptions<Category> options;
    FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter;


    @BindView(R.id.fragment_category_rv)
    RecyclerView recyclerViewCategory;

    GridLayoutManager gridLayoutManager;

    public CategoryFragment() {



        database=FirebaseDatabase.getInstance();
        categoriesBakeground=database.getReference(Common.REF_CATEGORY_BACKGROUND);

        Query query = categoriesBakeground;//in this case, i need all , without filter like  .limitToLast(50);



        options = new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query, Category.class)
                        .build();

        adapter= new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
             @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position, @NonNull final Category model) {
                 Common.checkInternetAndHandelViewInternet(getActivity());


                Picasso.with(getActivity()).load(model.getImageLink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.imgbackground, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                //try again online if the first time faild to lod from cash !!
                                Picasso.with(getActivity()).load(model.getImageLink()).placeholder(R.drawable.ic_terrain_black_24dp)
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.imgbackground, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {

                                            }


                                        });
                            }


                        });

                holder.tvName.setText(model.getName());



                holder.setMyItemClickListener(new MyItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //go to details of this image !!

                        Common.CATEGORY_SELECTED_ID= adapter.getRef(position).getKey();
                        Common.CATEGORY_SELECTED=model.getName();
                        startActivity(new Intent(getActivity(),ListWallPaperActivity.class));
                    }
                });
            }

            @Override
            public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Log.d(TAG, "onCreateViewHolder: ");
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_item,parent,false);

                return new CategoryViewHolder(view);
            }
        };

    }

    private static CategoryFragment Instance;


    public static CategoryFragment getInstance(){
        if (Instance==null) {
            Instance= new CategoryFragment();
        }

        return Instance;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
         ButterKnife.bind(this,view);
        recyclerViewCategory.setHasFixedSize(true);
        int numberOfColumn=getResources().getInteger(R.integer.grid_number_column);
          gridLayoutManager= new GridLayoutManager(getActivity(),numberOfColumn);
        recyclerViewCategory.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerViewCategory.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                recyclerViewCategory.scrollToPosition(positin);


            }
        });



        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null){
            Log.d(TAG, "onStart: ");
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (gridLayoutManager!=null){

            positin = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            outState.putInt(SCROLL_POSITION_KEY, positin);

        }

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle state) {
        super.onViewStateRestored(state);
        if (state != null) {
            positin = state.getInt(SCROLL_POSITION_KEY, 0);
            recyclerViewCategory.scrollToPosition(positin);

        }

    }
}
