package com.example.alex.wallpaperapp.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.alex.wallpaperapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthUiActivity extends AppCompatActivity {
     private static final int RC_SIGN_IN = 100;
    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";
    private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google.com/policies/privacy/";
    private static final String FIREBASE_PRIVACY_POLICY_URL = "https://firebase.google.com/terms/analytics/#7_privacy";

    private static final String TAG = "AuthUiActivity";

    @BindView(R.id.root)
    View mRootView;


    @NonNull
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, AuthUiActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_ui);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.sign_in)
    public void signIn() {
        AuthUI.SignInIntentBuilder builder =  AuthUI.getInstance().createSignInIntentBuilder()
                .setTheme(AuthUI.getDefaultTheme() )
                .setLogo(R.drawable.fui_ic_googleg_color_24dp)
                .setAvailableProviders(getSelectedProviders())
                .setIsSmartLockEnabled(true)
                .setPrivacyPolicyUrl(FIREBASE_TOS_URL)
                .setTosUrl(GOOGLE_TOS_URL);

        startActivityForResult(builder.build(), RC_SIGN_IN);
    }

    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();

      selectedProviders.add(new AuthUI.IdpConfig.GoogleBuilder() .setScopes(getGoogleScopes()).build());

        return selectedProviders;
    }

    private List<String> getGoogleScopes() {
        List<String> result = new ArrayList<>();

            result.add("https://www.googleapis.com/auth/youtube.readonly");

        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

                handleSignInResponse(resultCode, data);
        }
    }


    private void handleSignInResponse(int resultCode, @Nullable Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            startHomeActivity(response);
            finish();
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            showSnackbar(R.string.unknown_error);
            Log.e(TAG, "Sign-in error: ", response.getError());
        }
    }

    private void startHomeActivity(IdpResponse response) {
        startActivity(HomeActivity.createIntent(this, response));
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
