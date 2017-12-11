package com.mobiledevpro.facebook;

import android.text.TextUtils;

/**
 * Common Facebook Response
 * <p>
 * Created by Dmitriy V. Chernysh on 11.12.17.
 * <p>
 * https://fb.com/mobiledevpro/
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */

class FBResponse {
    private int respCode;
    private String respMessage;

    FBResponse(int respCode, String respMessage) {
        this.respCode = respCode;
        this.respMessage = respMessage;
    }

    boolean isSuccess() {
        return respCode == 200 || respCode == 201;
    }

    String getMessage() {
        return !TextUtils.isEmpty(respMessage) ? respMessage : "Facebook response error: Something went wrong";
    }
}
