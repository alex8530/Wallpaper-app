package com.example.alex.wallpaperapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentsFragment extends Fragment {


    public RecentsFragment() {
        // Required empty public constructor
    }

    private static RecentsFragment Instance ;



    public static RecentsFragment getInstance(){
        if (Instance==null) {
            Instance= new RecentsFragment();
        }

        return Instance;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recents, container, false);
    }

}
