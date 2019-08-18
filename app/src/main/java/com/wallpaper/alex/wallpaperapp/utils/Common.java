package com.wallpaper.alex.wallpaperapp.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wallpaper.alex.wallpaperapp.model.UserImage;
import com.wallpaper.alex.wallpaperapp.model.WallPaperItem;
import com.wallpaper.alex.wallpaperapp.ui.NoInternetActivity;

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




    public static final String PANNER_1 ="ca-app-pub-4680912109247750/2271846524";







    public static void  checkInternetAndHandelViewInternet(Context context) {
        if (Connectivity.isConnected(context )&&Connectivity.isConnectedWifi(context)){

        }else {
            context.startActivity(new Intent(context,NoInternetActivity.class));
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


}
