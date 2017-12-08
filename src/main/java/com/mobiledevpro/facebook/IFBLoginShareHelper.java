package com.mobiledevpro.facebook;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Interface for Facebook helper
 * <p>
 * Created by Dmitriy V. Chernysh on 07.12.17.
 * <p>
 * https://fb.com/mobiledevpro/
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */

public interface IFBLoginShareHelper {

    interface IFBLoginResultCallbacks {
        void onSuccess(String accessToken, String userOrPageName, String userOrPageId);

        void onFail(String message);
    }

    void init(Application app, String appID);

    void loginAsPageAdmin(Fragment fragment, @NonNull IFBLoginResultCallbacks outCallbacks);

    void onLoginResult(int requestCode, int resultCode, Intent data);

    void logout();
}
