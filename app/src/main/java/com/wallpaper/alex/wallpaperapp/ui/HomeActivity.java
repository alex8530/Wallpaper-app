package com.wallpaper.alex.wallpaperapp.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.wallpaper.alex.wallpaperapp.R;
import com.wallpaper.alex.wallpaperapp.adapter.MyFragmentAdapter;
import com.wallpaper.alex.wallpaperapp.utils.Common;
import com.wallpaper.alex.wallpaperapp.utils.Connectivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivity";
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(android.R.id.content) View mRootView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;


    private InterstitialAd mInterstitialAd;


    public static final String IDP_RESPONSE = "extra_idp_response";

    FirebaseUser currentUSer;
    MyFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUSer = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUSer == null) {
            startActivity(AuthUiActivity.createIntent(this));
            finish();
            return;
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},Common.PERMISSION_REQUEST_CODE);
            }
        }


        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Common.checkInternetAndHandelViewInternet(this);

        if (!Connectivity.isConnectedWifi(this)){
            //if he go to the noInternet activity and not connect to internet and juest press back ..
            //so here check another one..
            finish();

            Toast.makeText(this, "please check your conniection ", Toast.LENGTH_SHORT).show();
        }

        setUpToolBar();
        setUpActionBarDrawerToggle();
        setUpTapLayout();
        loadUserInfo();
         setUpAd();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId()==R.id.action_upload){
                    showInterstitialAdd();
//                    startActivity(new Intent(HomeActivity.this,UploadPhotoActivity.class));
                    //add listoner after close the add
                    mInterstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this,UploadPhotoActivity.class));
                            //this is for second show after the first one show
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());


                        }
                    });
                    return true;

                }
                return false;
            }
        });
    }

    private void setUpAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4680912109247750/9328209467");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }

    public void showInterstitialAdd(){
        //show ad
        boolean a = mInterstitialAd.isLoaded();
        if (a) {
            mInterstitialAd.show();
            Toast.makeText(this, "show", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "hide", Toast.LENGTH_SHORT).show();
         }
    }

    private void setUpToolBar() {

        toolbar.setTitle("ALEX WallPaper");
        setSupportActionBar(toolbar);

    }


    private void setUpTapLayout() {
          adapter= new MyFragmentAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpActionBarDrawerToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadUserInfo() {
      if (currentUSer!=null){
          View view=   navigationView.getHeaderView(0);
          TextView txt_email = view.findViewById(R.id.nav_email);
          ImageView mUserProfilePicture=view.findViewById(R.id.user_profile_picture);

          txt_email.setText(currentUSer.getEmail());
          Picasso.with(this).load(currentUSer.getPhotoUrl())  .into(mUserProfilePicture) ;

          String messamge="Welcome "+ currentUSer.getEmail() ;
          Snackbar.make(drawer ,messamge,Snackbar.LENGTH_LONG).show();
      }

    }

    @NonNull
    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, HomeActivity.class)
                .putExtra( IDP_RESPONSE, response);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode==Common.SIGN_IN_GOOGLE_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                //thats mean , Now the user sign in successfuly , so show messamge to welcome
                String messamge="Welcome "+ currentUSer.getEmail().toString();
                Snackbar.make(drawer ,messamge,Snackbar.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
       if (id == R.id.nav_sign_out){
            signOut();

        }
        if (id == R.id.nav_images){
           if (Connectivity.isConnectedWifi(this)){
               goToImagesUser();
           }else {
               Toast.makeText(this, "No Internot Connection", Toast.LENGTH_SHORT).show();
           }


        }

        if (id==R.id.nav_about){
        startActivity(new Intent(this,AboutActivity.class));


        }


        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToImagesUser() {
        startActivity(new Intent(this,ListUserImagesActivity.class));

    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(AuthUiActivity.createIntent(HomeActivity.this));
                            finish();
                        } else {
                            Log.w(TAG, "signOut:failure", task.getException());
                            showSnackbar(R.string.sign_out_failed);
                        }
                    }
                });
    }
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==Common.PERMISSION_REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show();


            }else {
                Toast.makeText(this, "You must accept this permission to be able to download images", Toast.LENGTH_LONG).show();
            }

        }
    }
}
