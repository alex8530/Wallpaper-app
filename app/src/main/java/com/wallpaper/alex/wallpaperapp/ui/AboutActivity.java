package com.wallpaper.alex.wallpaperapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.wallpaper.alex.wallpaperapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.wallpaper.alex.wallpaperapp.utils.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {


    @BindView(R.id.aboutCollapse)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.imgFacebook)
    ImageView imgFacebook;
    @BindView(R.id.imgGithup)
    ImageView imgGithup;
    @BindView(R.id.imgLinkedin)
    ImageView imgLinkedIn;
    @BindView(R.id.imgTweeter)
    ImageView imgTweeter;

    public static String FACEBOOK_URL = "https://www.facebook.com/AlexAbuHarb";
    public static String FACEBOOK_PAGE_ID = "AlexAbuHarb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);



        MobileAds.initialize(this, Common.PANNER_1);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        //init
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsAppbar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
         mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.titleExpandAbout));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    @OnClick(R.id.imgFacebook)
    public void openFace(){
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(this);
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);

    }

    @OnClick(R.id.imgTweeter)
    public void openTweeter(){
        String url = "https://twitter.com/AliAbuHarb8530";
        Intent twitterAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        twitterAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(twitterAppIntent);

    }
    @OnClick(R.id.imgLinkedin)
    public void openLinkedin(){
        String url = "https://www.linkedin.com/in/ali-abu-harb/";
        Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(linkedInAppIntent);

    }

    @OnClick(R.id.imgGithup)
    public void openGithub(){
        String url = "https://github.com/alex8530";
        Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(linkedInAppIntent);

    }

}
