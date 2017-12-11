package com.mobiledevpro.facebook;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.File;
import java.util.Arrays;

/**
 * Helper for working with Facebook SDK
 * <p>
 * Created by Dmitriy V. Chernysh on 07.12.17.
 * <p>
 * https://fb.com/mobiledevpro/
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */

public class FBLoginShareHelper implements IFBLoginShareHelper {

    public static final int REQUEST_CODE = 10001;

    private static FBLoginShareHelper sHelper;
    private CallbackManager mLoginCallbackManager;
    private IFBLoginResultCallbacks mLoginResultCallbacks;

    private FBLoginShareHelper() {
    }

    public static FBLoginShareHelper getInstance() {
        if (sHelper == null) {
            sHelper = new FBLoginShareHelper();
        }

        return sHelper;
    }

    /**
     * Init Facebook SDK
     * <p>
     * NOTE: call this in the main Application class in onCreate() method
     *
     * @param app   Application class
     * @param appID Facebook App ID (from Facebook developer console)
     */
    @Override
    public void init(Application app, String appID) {
        FacebookSdk.setApplicationId(appID);
        FacebookSdk.sdkInitialize(app.getApplicationContext(), REQUEST_CODE);
        AppEventsLogger.activateApp(app);
    }

    /**
     * Login to Facebook Page where user is admin
     *
     * @param fragment Fragment
     */
    @Override
    public void loginAsPageAdmin(final Fragment fragment, @NonNull IFBLoginResultCallbacks outCallbacks) {
        mLoginResultCallbacks = outCallbacks;
        final LoginManager loginManager = LoginManager.getInstance();

        //logout before new login
        loginManager.logOut();

        mLoginCallbackManager = CallbackManager.Factory.create();
        loginManager.registerCallback(mLoginCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //show chooser to select facebook account/page
                FBGraphApiHelper.showAccountChooser(fragment.getActivity(), loginResult.getAccessToken(), mLoginResultCallbacks);
            }

            @Override
            public void onCancel() {
                mLoginResultCallbacks.onFail("Login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                mLoginResultCallbacks.onFail("Login error: " + error.toString());
            }
        });

        //fo login with publish access
        loginManager.logInWithPublishPermissions(
                fragment,
                Arrays.asList(FBPermissions.PAGES_PUBLISH_ACCESS)
        );
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
    }

    /**
     * Handle login result
     * <p>
     * NOTE: call this on onActivityResult() by request code (REQUEST_CODE)
     *
     * @param requestCode Request code
     * @param resultCode  Result code
     * @param data        Result data
     */
    @Override
    public void onLoginResult(int requestCode, int resultCode, Intent data) {
        if (mLoginCallbackManager == null) return;
        mLoginCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void uploadVideoToPageAsync(String accessToken,
                                       String userOrPageId,
                                       @NonNull File localVideoFile,
                                       String videoTitle,
                                       String videoDescription,
                                       @NonNull IFBVideoUploadResultCallbacks callbacks) {
        AccessToken token = new AccessToken(
                accessToken,
                AccessToken.getCurrentAccessToken().getApplicationId(),
                userOrPageId,
                Arrays.asList(FBPermissions.PAGES_PUBLISH_ACCESS),
                null,
                AccessToken.getCurrentAccessToken().getSource(),
                AccessToken.getCurrentAccessToken().getExpires(),
                AccessToken.getCurrentAccessToken().getLastRefresh()
        );

        FBGraphApiHelper.uploadVideoAsync(
                token,
                userOrPageId,
                localVideoFile,
                videoTitle,
                videoDescription,
                callbacks
        );
    }
}
