package com.wallpaper.alex.wallpaperapp.imageProcessing;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class SaveImageHelper implements Target {


    Context  context;
    WeakReference<AlertDialog> alertDialogWeakReference;
    WeakReference<ContentResolver> contentResolverWeakReference;
    String name;
    String desc;
    public SaveImageHelper(Context context,  AlertDialog  alertDialog ,  ContentResolver contentResolver , String name, String desc) {
        this.context = context;
        this.alertDialogWeakReference = new  WeakReference<AlertDialog> (alertDialog);
        this.contentResolverWeakReference = new  WeakReference<ContentResolver> (contentResolver);
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        ContentResolver contentResolver= contentResolverWeakReference.get();
        AlertDialog alertDialog= alertDialogWeakReference.get();
        if (contentResolver!=null){
            MediaStore.Images.Media.insertImage(contentResolver,bitmap,name,desc);

            alertDialog.dismiss();
            Toast.makeText(context, "Download Successful ", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
