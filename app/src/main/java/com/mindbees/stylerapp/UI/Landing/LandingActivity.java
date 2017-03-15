package com.mindbees.stylerapp.UI.Landing;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.Models.Fblogin.ModelFblogin;
import com.mindbees.stylerapp.UTILS.Constants;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by User on 01-03-2017.
 */

public class LandingActivity extends BaseActivity {
    Bundle bundle;
    ImageView Fbconnect;
    LinearLayout Login,register;
    CallbackManager callbackManager;
    Bitmap mIcon1 = null;
    String imgUrl = "";
    String id = "";
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
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                showToast("Login success");

//                tools.showLog(loginResult.getAccessToken() + "", 1);

//                isFbLogin = true;
                fetchUserInfo();

            }

            @Override
            public void onCancel() {
//                tools.showToastMessage("Login cancel");

            }

            @Override
            public void onError(FacebookException e) {

                showToast("LOGIN ERROR");

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

    private void fetchUserInfo() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {


                            parseUserInfo(me);


                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString(FIELDS, REQUEST_FIELDS);
            request.setParameters(parameters);
            GraphRequest.executeBatchAsync(request);

//            LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList(PERMISSION));
        } else {

//            showLog("facebook", "accessToken null");
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
//            first_name=me.getString("first_name");
//            last_name=me.getString("last_name");



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
            params.put("fb_id", id);
            params.put("full_name", name);
            params.put("user_age", );
            params.put("gender", gender);
            params.put("hw_id", "123");
            params.put("device_push_id", "2345");*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
        bundle = new Bundle();

        if (id != null && !id.equalsIgnoreCase(""))
            bundle.putString("id", id);


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
        if(getPref(Constants.FirstTime_TERMS,true))
        {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            PopUpTermsAndConditions p=PopUpTermsAndConditions.newInstance();
            p.show(mFragmentTransaction,"POPUP");
        }
        Login= (LinearLayout) findViewById(R.id.LoginButton);
        register= (LinearLayout) findViewById(R.id.RegisterButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LandingActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });
        Fbconnect= (ImageView) findViewById(R.id.Fbconnect);
        Fbconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LandingActivity.this, Arrays.asList("public_profile", "user_friends","email"));
            }
        });


    }
}
