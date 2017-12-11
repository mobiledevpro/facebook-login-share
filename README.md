# Facebook Login & Share module
This module makes easier to use a main Facebook features (login &amp; share)

## Init module

* Main Application class:
```java
public class App extends Application {

    private static final String FACEBOOK_APP_ID = "APP ID from Facebook Developer Console";
    
    ..........
    
    @Override
    public void onCreate() {
        super.onCreate();

        //init facebook helper
        FBLoginShareHelper.getInstance().init(this, FACEBOOK_APP_ID);
    }
    
  
}
```

* App-level build.gradle
```java
dependencies {
   ...............
   ...............
   
   compile project(':facebook-login-share')
}
```


## Login. Get access to Facebook Page to publish content.

* Start login from fragment:
```java
FBLoginShareHelper.getInstance().loginAsPageAdmin(fragment, new IFBLoginShareHelper.IFBLoginResultCallbacks() {
    @Override
    public void onSuccess(String accessToken, final String userOrPageName, String userOrPageId) {
       //here your code
    }

    @Override
    public void onFail(String message) {
       //show error
       Toast.makeText(mView.getActivity(), message, Toast.LENGTH_SHORT).show();
    }
});
```

* Handle result from onActivityResult() in the fragment:
```java
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
        ................
        ................
        ................
            
        case FBLoginShareHelper.REQUEST_CODE:
            FBLoginShareHelper.getInstance().onLoginResult(requestCode, resultCode, data);
            break;
    }
}
```


![ezgif-5-5ed0eacacb](https://user-images.githubusercontent.com/5750211/33781034-acdaa16a-dc5b-11e7-9de4-3a554aaa173f.gif)


## Logout 

```java
FBLoginShareHelper.getInstance().logout();
```

## Upload video to Facebook page.

```java
FBLoginShareHelper.getInstance().uploadVideoToPageAsync(
    "[page access token string]",
    "[page id]",
    [video file here],
    "[Video title here]",
    "[Video description here]",
    new IFBLoginShareHelper.IFBVideoUploadResultCallbacks() {
        @Override
        public void onSuccess() {
              //your code
        }

        @Override
         public void onFail(String message) {
              //your code
         }
    }
);
```
