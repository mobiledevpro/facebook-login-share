package com.mobiledevpro.facebook;

/**
 * Facebook permissions
 * <p>
 * Created by Dmitriy V. Chernysh on 08.12.17.
 * <p>
 * https://fb.com/mobiledevpro/
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */

class FBPermissions {

    static final String EMAIL = "email";
    static final String PUBLIC_PROFILE = "public_profile";

    private static final String PAGE_MANAGE = "manage_pages";
    private static final String PAGE_PUBLISH = "publish_pages";

    static final String[] PAGES_PUBLISH_ACCESS = {
            PAGE_MANAGE,
            PAGE_PUBLISH
    };
}
