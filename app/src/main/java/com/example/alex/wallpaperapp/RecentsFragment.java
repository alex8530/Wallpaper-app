package com.example.alex.wallpaperapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.alex.wallpaperapp.adapter.RecentAdapter;
import com.example.alex.wallpaperapp.interfaces.MyItemClickListener;
import com.example.alex.wallpaperapp.model.RecentItem;
import com.example.alex.wallpaperapp.utils.Common;
import com.example.alex.wallpaperapp.viewmodel.RecentFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentsFragment extends Fragment      {
    private static final String TAG = "RecentsFragment";
    MyItemClickListener myItemClickListener;
    @BindView(R.id.fragment_recent_rv)
    RecyclerView recyclerView;

    RecentAdapter recentAdapter;
    List<RecentItem> recentItems= new ArrayList<>();

    public RecentsFragment() {

        // Required empty public constructor

        Log.d(TAG, "RecentsFragment: cons");
/**
 * because im this constractor run befor other method .. so the contxt retern null getActivity() .. so we pass it as arq
 * getActivity() returns null if onAttach isn't called yet.
 *
 * Instead of passing around the application context,
 * create a static context inside your Application class and initialize it onCreate()
 * : MyApplication.sContext = getApplicationContext();
 * then you can access it from any activity/fragment without worrying about detachment
 *
 *
        so .... i will use onCreateView because i need context and i do not need to do that ...
 */
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }





    private static RecentsFragment Instance ;



    public static RecentsFragment getInstance( ){
        if (Instance==null) {
            Instance= new RecentsFragment();
        }

        return Instance;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_recents, container, false);
        ButterKnife.bind(  this,view);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);

        recentAdapter= new RecentAdapter(getContext());
        recentAdapter.setRecentItemsList(recentItems);

        setUpViewModel();

        return view;
    }

    private void setUpViewModel() {

        //get object from view model
        Log.d(TAG, "setUpViewModel: ");

        RecentFragmentViewModel viewModel = ViewModelProviders.of(this).get(RecentFragmentViewModel.class);
        viewModel.getAllRecents().observe(this, new Observer<List<RecentItem>>() {
            @Override
            public void onChanged(@Nullable List<RecentItem> recentItems) {
                if (recentItems != null) {

                     Log.d(TAG, "onChanged: " + recentItems.toString());
                     recentAdapter.setRecentItemsList(recentItems);
                    recyclerView.setAdapter(recentAdapter);
                    recentAdapter.notifyDataSetChanged();

                }
            }
        });

    }


}
