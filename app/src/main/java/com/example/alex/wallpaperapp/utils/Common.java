package com.example.alex.wallpaperapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.example.alex.wallpaperapp.model.RecentItem;
import com.example.alex.wallpaperapp.model.UserImage;
import com.example.alex.wallpaperapp.model.WallPaperItem;
import com.example.alex.wallpaperapp.ui.NoInternetActivity;

public final class Common  {
    public static final String REF_CATEGORY_BACKGROUND="CategoryBackground";
    public static final String REF_WALLPAPER  = "wallPaper";
    public static final int SIGN_IN_GOOGLE_REQUEST_CODE = 666;
    public static   String CATEGORY_SELECTED ;
    public static   String CATEGORY_SELECTED_ID ;

    public static int PERMISSION_REQUEST_CODE=555;

    public static WallPaperItem wallPaperItem = new WallPaperItem();
    public static String WALLPAPERITEM_KEY;


    public static final int CHOOSE_IMAGE_REQUEST_CODE = 456;
    public static UserImage userImage= new UserImage();
    public static String DELETE_KEY_IMAGE_USER="";


    public static String PATH_FILE_IMAGE;







    public static void  checkInternetAndHandelViewInternet(Context context) {
        if (Connectivity.isConnected(context )&&Connectivity.isConnectedWifi(context)){

        }else {
            context.startActivity(new Intent(context,NoInternetActivity.class));
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


}
