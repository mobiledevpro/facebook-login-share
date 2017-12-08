package com.mobiledevpro.facebook;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

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

    private static FBLoginShareHelper sHelper;

    private FBLoginShareHelper() {
    }

    public static FBLoginShareHelper getInstance() {
        if (sHelper == null) {
            sHelper = new FBLoginShareHelper();
        }

        return sHelper;
    }

    public void init(Application app, String appID) {
        FacebookSdk.setApplicationId(appID);
        FacebookSdk.sdkInitialize(app.getApplicationContext());
        AppEventsLogger.activateApp(app);
    }

    public void signIn(Fragment fragment) {

    }

    public void signIn(Activity activity) {

    }

}
