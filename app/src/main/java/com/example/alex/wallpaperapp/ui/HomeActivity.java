package com.example.alex.wallpaperapp.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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


import com.example.alex.wallpaperapp.R;
import com.example.alex.wallpaperapp.adapter.MyFragmentAdapter;
import com.example.alex.wallpaperapp.utils.Common;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
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

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);



        toolbar.setTitle("ALEX WallPaper");
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        setUpActionBarDrawerToggle();

        setUpTapLayout();

        loadUserInfo();
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
          Picasso.with(this).load(currentUSer.getPhotoUrl()) .into(mUserProfilePicture) ;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_photos) {
          // Handle the camera action

        }else if (id == R.id.nav_sign_out){
            signOut();

        }

        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

}
