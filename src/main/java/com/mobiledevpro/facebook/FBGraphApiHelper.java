package com.mobiledevpro.facebook;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.Sharer;
import com.facebook.share.internal.VideoUploader;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Helper class for Graph API
 * <p>
 * Created by Dmitriy V. Chernysh on 08.12.17.
 * <p>
 * https://fb.com/mobiledevpro/
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */

class FBGraphApiHelper {

    /**
     * Show list of Facebook Accounts/Pages where current user is admin
     *
     * @param userAccessToken Current user access token
     */
    static void showAccountChooser(final Context context,
                                   AccessToken userAccessToken,
                                   @NonNull final IFBLoginShareHelper.IFBLoginResultCallbacks loginResultCallbacks) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                userAccessToken,
                "/me/accounts",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        //parse json
                        ArrayList<FBAccount> pagesList = parseAccountsList(response.getJSONObject());
                        if (pagesList == null || pagesList.isEmpty()) {
                            // "You have no Facebook Pages where you are admin. Please, create at least one Page."
                            loginResultCallbacks.onFail(context.getResources().getString(R.string.message_pages_list_empty));
                            return;
                        }

                        //show pages list chooser dialog
                        showAccountChooserDialog(context, pagesList, loginResultCallbacks);
                    }
                });

        request.executeAsync();
    }


    /**
     * Upload video to Facebook
     *
     * @param accessToken      AccessToken
     * @param userOrPageId     User's or Page's ID
     * @param videoFile        Local Video File
     * @param videoTitle       Video Title
     * @param videoDescription Video Description
     * @param callbacks        IFBVideoUploadResultCallbacks
     */
    static void uploadVideoAsync(AccessToken accessToken,
                                 final String userOrPageId,
                                 @NonNull File videoFile,
                                 String videoTitle,
                                 String videoDescription,
                                 @NonNull final IFBLoginShareHelper.IFBVideoUploadResultCallbacks callbacks) {

        AccessToken.setCurrentAccessToken(accessToken);

        ShareVideo.Builder shareVideoBuilder = new ShareVideo.Builder();
        shareVideoBuilder.setLocalUrl(Uri.fromFile(videoFile));

        ShareVideoContent.Builder builder = new ShareVideoContent.Builder();
        builder.setContentDescription(videoDescription)
                .setContentTitle(videoTitle)
                .setVideo(shareVideoBuilder.build());

        ShareVideoContent videoContent = builder.build();

        try {
            VideoUploader.uploadAsync(
                    videoContent,
                    userOrPageId,
                    new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {
                            FBShareResponse response = new FBShareResponse(200, "", result.getPostId());
                            callbacks.onSuccess(response.getPostUrl(userOrPageId));
                        }

                        @Override
                        public void onCancel() {
                            callbacks.onFail("Facebook upload cancelled");
                        }

                        @Override
                        public void onError(FacebookException error) {
                            callbacks.onFail("Facebook upload video error: " + error.getLocalizedMessage());
                        }
                    }
            );
        } catch (FileNotFoundException e) {
            callbacks.onFail("Facebook upload video error: " + e.getLocalizedMessage());
        }
    }

    /**
     * Parse accounts/pages list
     *
     * @param jsonObject JSON object from Graph Api response
     * @return List of FBAccount
     */
    private static ArrayList<FBAccount> parseAccountsList(JSONObject jsonObject) {
        ArrayList<FBAccount> pagesList = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            JSONObject pageObject;
            FBAccount fbAccount;
            for (int i = 0, j = jsonArray.length(); i < j; i++) {
                pageObject = jsonArray.getJSONObject(i);

                fbAccount = new FBAccount(
                        pageObject.getString("id"),
                        pageObject.getString("name"),
                        pageObject.getString("access_token")
                );
                pagesList.add(fbAccount);
            }

        } catch (JSONException e) {
            Log.e(FBLoginShareHelper.class.getSimpleName(), "FBLoginShareHelper.parseAccountsList: JSONException - " + e.getLocalizedMessage(), e);
        }

        return pagesList;
    }

    /**
     * Parse upload response
     *
     * @param graphResponse GraphResponse
     * @return FBResponse
     */
    private static FBShareResponse parseUploadResponse(GraphResponse graphResponse) {
        FBShareResponse fbResponse;

        String response = graphResponse.toString();
        response = response.replaceAll("Response:", "");
        try {
            JSONObject jsonObject = new JSONObject(response);
            int respCode = jsonObject.getInt("responseCode");
            String respMessage = "";
            if (jsonObject.has("error") && !(jsonObject.get("error") instanceof JSONObject)) {
                respMessage = jsonObject.getString("error");
            }

            JSONObject graphObject = jsonObject.getJSONObject("graphObject");
            String postId = "";
            if (graphObject != null && graphObject.has("id")) {
                postId = graphObject.getString("id");
            }

            fbResponse = new FBShareResponse(respCode, respMessage, postId);

        } catch (JSONException e) {
            Log.e(FBLoginShareHelper.class.getSimpleName(), "FBLoginShareHelper.parseUploadResponse: JSONException - " + e.getLocalizedMessage(), e);
            fbResponse = new FBShareResponse(400, "Facebook upload exception: " + e.getLocalizedMessage(), null);
        }

        return fbResponse;
    }

    /**
     * Show chooser dialog
     *
     * @param context      Activity context
     * @param accountsList Accounts/Pages list
     */
    private static void showAccountChooserDialog(Context context,
                                                 final ArrayList<FBAccount> accountsList,
                                                 @NonNull final IFBLoginShareHelper.IFBLoginResultCallbacks loginResultCallbacks) {
        ArrayList<String> namesList = new ArrayList<>();
        for (FBAccount fbAccount : accountsList) {
            namesList.add(fbAccount.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DefaultAppTheme_AlertDialog);
        builder.setTitle(context.getResources().getString(R.string.dialog_title_select_page))
                .setItems(namesList.toArray(new CharSequence[namesList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FBAccount fbAccount = accountsList.get(which);
                        loginResultCallbacks.onSuccess(
                                fbAccount.getAccessToken(),
                                fbAccount.getName(),
                                fbAccount.getId()
                        );
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
