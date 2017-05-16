package com.mindbees.stylerapp.UI.Landing;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.applozic.audiovideo.activity.AudioCallActivityV2;
import com.applozic.audiovideo.activity.VideoActivity;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.PushNotificationTask;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.location.LocationListener;
import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.HOME.HomeActivity;
import com.mindbees.stylerapp.UI.Models.Fblogin.ModelFblogin;
import com.mindbees.stylerapp.UTILS.Constants;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by User on 01-03-2017.
 */

public class LandingActivity extends BaseActivity implements LocationListener{
    Bundle bundle;
    private LocationManager locationManager;
    private String provider;
    RelativeLayout Fbconnect;
    LinearLayout Login,register;
    CallbackManager callbackManager;
    Bitmap mIcon1 = null;
    String imgUrl = "";
    String id = "";
    private UserLoginTask mAuthTask = null;
    private static final int ACCESS_FINE_LOCATION = 0;
    String name = "";
    String email = "";
    ProgressDialog pd;
    BroadcastReceiver breceiver;
    //    public boolean isFbLogin;
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String GENDER = "gender";
    private static final String FIELDS = "fields";
    private static final String REQUEST_FIELDS =
            TextUtils.join(",", new String[]{ID, NAME, EMAIL, GENDER});
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_layout);
        facebookSDKInitialize();
        initUi();

        try
        {
            PackageInfo info=getPackageManager().getPackageInfo("com.mindbees.stylerapp", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures)
            {
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash: ", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try
        {
          Getlocation();
        }
        catch (Exception e)
        {

        }
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

//              showToast(" face book Login success");

//                tools.showLog(loginResult.getAccessToken() + "", 1);
                showLog(loginResult.getAccessToken() + "", 1);
//                isFbLogin = true;
                fetchUserInfo();

            }

            @Override
            public void onCancel() {


            }

            @Override
            public void onError(FacebookException e) {

                showToast("LOGIN ERROR",false);

            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.KEY_FILTER+".ACTION_RQST_FNSH");
        breceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub

                finish();

            }
        };



        registerReceiver(breceiver, intentFilter);


    }
    private void Getlocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//      Criteria criteria = new Criteria();
//       provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION);

            return;
        }
        Location location = getLastKnownLocation();


        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            showSnackBar("location not available please turn on gps", false);
        }
    }
    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String bprovider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);


            }
            Location l = locationManager.getLastKnownLocation(bprovider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
                provider=bprovider;

            }
        }
        return bestLocation;
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void fetchUserInfo() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object,
                                                GraphResponse response) {
                            // TODO Auto-generated method stub

                            showLog(object+"", 2);
                            showLog(response+"",3);

                            parseUserInfo(object);


//	                            user = me;
//	                            updateUI();

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString(FIELDS, REQUEST_FIELDS);
            request.setParameters(parameters);
            GraphRequest.executeBatchAsync(request);

//            LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList(PERMISSION));
        } else {

            showLog("access token is null", 2);
        }
    }
    private void parseUserInfo(JSONObject me) {


        String name = "";
        String email = "";
        String birthday = "";
        String gender ="";
        String pictureUrl=null;


        try {
            id = me.getString("id");

            name = me.getString("name");
            email = me.getString("email");
            imgUrl = me.getJSONObject("picture").getJSONObject("data").getString("url");
            gender=me.getString("gender");
//            JSONObject picture = me.getJSONObject("picture");
//            JSONObject data = picture.getJSONObject("data");
////              Returns a 50x50 profile picture
//            pictureUrl = data.getString("url");



            /*HashMap<String, String> params = new HashMap<>();
            params.put("user_email", email);
            params.put("fb_id", fbid);
            params.put("full_name", name);
            params.put("user_age", );
            params.put("gender", gender);
            params.put("hw_id", "123");
            params.put("device_push_id", "2345");*/
            showLog(id, 1);
            showLog(name, 1);
            showLog(email, 1);
            showLog(imgUrl, 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        bundle = new Bundle();

        if (id != null && !id.equalsIgnoreCase(""))
            bundle.putString("fbid", id);


        if (name != null && !name.equalsIgnoreCase(""))
            bundle.putString("name", name);

       if (email != null && !email.equalsIgnoreCase(""))
            bundle.putString("email", email);
        if (gender != null && !gender.equalsIgnoreCase(""))
            bundle.putString("gender", gender);
//
//        nextmethod();
        getwebservice();



//        FacebookLogin(bundle);



    }

    private void getwebservice() {
        HashMap<String, String> params = new HashMap<>();
        params.put("fb_id",id);
        APIService service = ServiceGenerator.createService(APIService.class,LandingActivity.this);
        Call<ModelFblogin>call=service.fblogin(params);
        call.enqueue(new Callback<ModelFblogin>() {
            @Override
            public void onResponse(Call<ModelFblogin> call, Response<ModelFblogin> response) {
                if (response.isSuccessful())
                {
                    if (response.body()!=null&&response.body().getResult()!=null)
                    {
                        ModelFblogin fblogin=response.body();
                        int value=fblogin.getResult().getValue();
                        if (value==0)
                        {
                            Intent intent=new Intent(LandingActivity.this,RegistrationActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else {
//                            UserLoginTask.TaskListener listener=new UserLoginTask.TaskListener() {
//                                @Override
//                                public void onSuccess(RegistrationResponse registrationResponse, Context context) {
//
//                                    ApplozicClient.getInstance(context).setContextBasedChat(true).setHandleDial(true).setIPCallEnabled(true);
//                                    Map<ApplozicSetting.RequestCode, String> activityCallbacks = new HashMap<ApplozicSetting.RequestCode, String>();
//                                    activityCallbacks.put(ApplozicSetting.RequestCode.AUDIO_CALL, AudioCallActivityV2.class.getName());
//                                    activityCallbacks.put(ApplozicSetting.RequestCode.VIDEO_CALL, VideoActivity.class.getName());
//                                    ApplozicSetting.getInstance(context).setActivityCallbacks(activityCallbacks);
////                                    ApplozicSetting.getInstance(context).setChatBackgroundColorOrDrawableResource(R.drawable.bg_splash);
////                                    ApplozicSetting.getInstance(context).setSentMessageBackgroundColor(R.color.white);
////                                    ApplozicSetting.getInstance(context).setReceivedMessageBackgroundColor(R.color.sentmessage_black);
////                                    ApplozicSetting.getInstance(context).setSentMessageTextColor(R.color.black);
////                                    ApplozicSetting.getInstance(context).setReceivedMessageTextColor(R.color.white);
//                                    PushNotificationTask.TaskListener pushNotificationTaskListener=  new PushNotificationTask.TaskListener() {
//                                        @Override
//                                        public void onSuccess(RegistrationResponse registrationResponse) {
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
//
//                                        }
//                                    };
//                                    PushNotificationTask pushNotificationTask = new PushNotificationTask(Applozic.getInstance(context).getDeviceRegistrationId(),pushNotificationTaskListener,context);
//                                    pushNotificationTask.execute((Void)null);
//                                }
//
//                                @Override
//                                public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
//
//                                }
//                            };
//                            User user = new User();
//                            String userid=fblogin.getResult().getUserId();
//                            user.setUserId(userid);
////                            user.setDisplayName(username);
//                            user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());
//                            List<String> featureList =  new ArrayList<>();
//                            featureList.add(User.Features.IP_AUDIO_CALL.getValue());// FOR AUDIO
//                            featureList.add(User.Features.IP_VIDEO_CALL.getValue());// FOR VIDEO
//                            user.setFeatures(featureList);
//                            mAuthTask = new UserLoginTask(user, listener, LandingActivity.this);
//                            mAuthTask.execute((Void) null);
//                            savePref(Constants.GENDER,gender);
//                            savePref(Constants.USER_ID,userid);
//                            savePref(Constants.PASSWORD,Spass);
//                            savePref(Constants.FBREGISTRATION,false);
//                            savePref(Constants.EMAIL_REGISTRATION,true);
//                            savePref(Constants.TAG_ISLOGGED_IN,true);
//                            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
//                            startActivity(intent);
                            String gender=fblogin.getResult().getUserInfo().getGender();
                            savePref(Constants.GENDER,gender);
                            Intent i = new Intent(LandingActivity.this, HomeActivity.class);
                            String userid=fblogin.getResult().getUserId();
                            String username=fblogin.getResult().getUserInfo().getUsername();
                            savePref(Constants.USERNAME,username);
//                            savePref(Constants.USERNAME,"");
                            savePref(Constants.USER_ID,userid);
                            savePref(Constants.FBREGISTRATION,true);
                            savePref(Constants.TAG_ISLOGGED_IN,true);
                            startActivity(i);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelFblogin> call, Throwable t) {

            }
        });
    }

    //
    private void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(breceiver);
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void initUi() {
//        if(getPref(Constants.FirstTime_TERMS,true))
//        {
//            mFragmentManager = getSupportFragmentManager();
//            mFragmentTransaction = mFragmentManager.beginTransaction();
//            PopUpTermsAndConditions p=PopUpTermsAndConditions.newInstance();
//            p.show(mFragmentTransaction,"POPUP");
//        }
        Login= (LinearLayout) findViewById(R.id.LoginButton);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LandingActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        register= (LinearLayout) findViewById(R.id.RegisterButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LandingActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });
        Fbconnect= (RelativeLayout) findViewById(R.id.Fbconnect);
        Fbconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    LoginManager.getInstance().logInWithReadPermissions(LandingActivity.this, Arrays.asList("public_profile", "user_friends", "email"));

            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {
        double Lat=location.getLatitude();
        double Long=location.getLongitude();
        String LAT=String.valueOf(Lat);
        String LONG=String.valueOf(Long);
        savePref(Constants.LAT,LAT);
        savePref(Constants.LONG,LONG);

    }
}
