package com.mobiledevpro.facebook;

/**
 * FB Account model class
 * <p>
 * Created by Dmitriy V. Chernysh on 08.12.17.
 * <p>
 * https://fb.com/mobiledevpro/
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */

class FBAccount {
    private String id;
    private String name;
    private String accessToken;

    FBAccount(String id, String name, String accessToken) {
        this.id = id;
        this.name = name;
        this.accessToken = accessToken;
    }

    public String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getAccessToken() {
        return accessToken;
    }
}
