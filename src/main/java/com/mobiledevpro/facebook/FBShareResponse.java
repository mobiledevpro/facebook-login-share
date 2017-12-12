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

class FBShareResponse {

    private static final String FB_ADDRESS = "https://fb.com";
    
    private int respCode;
    private String respMessage;
    private String postId;

    FBShareResponse(int respCode, String respMessage, String postId) {
        this.respCode = respCode;
        this.respMessage = respMessage;
        this.postId = postId;
    }

    boolean isSuccess() {
        return respCode == 200 || respCode == 201;
    }

    String getMessage() {
        return !TextUtils.isEmpty(respMessage) ? respMessage : "Facebook response error: Something went wrong";
    }

    String getPostUrl(String graphPath) {
        return !TextUtils.isEmpty(postId) && !TextUtils.isEmpty(graphPath)
                ? FB_ADDRESS + graphPath + "/" + postId + "/"
                : "";
    }
}
