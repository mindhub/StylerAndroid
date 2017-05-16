package com.mindbees.stylerapp.App;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.applozic.mobicomkit.ApplozicClient;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.mindbees.stylerapp.UI.Landing.LoginActivity;
import com.mindbees.stylerapp.UI.Models.update_profile.ModelUpdateProfile;
import com.mindbees.stylerapp.UTILS.Constants;

import com.mindbees.stylerapp.UTILS.MyWebService;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.Foreground;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;
import com.mindbees.stylerapp.UTILS.Util;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by User on 01-03-2017.
 */

public class Styler extends MultiDexApplication{
    private static final String TAG = "STyler";
    public static Styler instance;
    boolean isLoggedin;
    String Userid;
    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        new Util(this);
        Foreground.init(this);
        Foreground.get(this).addListener(myListener);
       isLoggedin =Util.getUtils().getPref(Constants.TAG_ISLOGGED_IN,false);
        if (isLoggedin)
        {
            Userid=Util.getUtils().getPref(Constants.USER_ID);
        }

    }
    Foreground.Listener myListener = new Foreground.Listener() {
        public void onBecameForeground() {
            boolean isLoggedin=Util.getUtils().getPref(Constants.TAG_ISLOGGED_IN);
            if (isLoggedin)
            {
                String userid=Util.getUtils().getPref(Constants.USER_ID);
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id",userid);
                params.put("online_status","1");
                final APIService service = ServiceGenerator.createService(APIService.class, Styler.this);
                Call<ModelUpdateProfile> call=service.updateprofile(params);
                call.enqueue(new Callback<ModelUpdateProfile>() {
                    @Override
                    public void onResponse(Call<ModelUpdateProfile> call, Response<ModelUpdateProfile> response) {
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getResult() != null) {
                                ModelUpdateProfile modelUpdateProfile = response.body();
                                int value = modelUpdateProfile.getResult().getValue();
                                if (value == 1) {
                                    String msg = modelUpdateProfile.getResult().getMessage();
//                            showToast(msg, true);
//                            if (switchtemp)
//                            {
//                                aSwitch.setChecked(true);
//                            }
//                            else {
//                                aSwitch.setChecked(false);
//                            }

                                } else {
                                    String msg = modelUpdateProfile.getResult().getMessage();

                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelUpdateProfile> call, Throwable t) {


                    }
                });
            }

            Log.e(TAG, "Foreground!");
        }

        public void onBecameBackground() {

            if (isLoggedin)
            {
//                context.startService(new Intent(context, ReminderService.class));

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id",Userid);
                params.put("online_status","0");
                final APIService service = ServiceGenerator.createService(APIService.class, Styler.this);
                Call<ModelUpdateProfile> call=service.updateprofile(params);
                call.enqueue(new Callback<ModelUpdateProfile>() {
                    @Override
                    public void onResponse(Call<ModelUpdateProfile> call, Response<ModelUpdateProfile> response) {
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getResult() != null) {
                                ModelUpdateProfile modelUpdateProfile = response.body();
                                int value = modelUpdateProfile.getResult().getValue();
                                if (value == 1) {
                                    String msg = modelUpdateProfile.getResult().getMessage();
//                            showToast(msg, true);
//                            if (switchtemp)
//                            {
//                                aSwitch.setChecked(true);
//                            }
//                            else {
//                                aSwitch.setChecked(false);
//                            }

                                } else {
                                    String msg = modelUpdateProfile.getResult().getMessage();

                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelUpdateProfile> call, Throwable t) {


                    }
                });
           }

            Log.e(TAG, "Background!");
        }
    };


    private void initApplication() {
        instance = this;
//        ApplozicClient.getInstance(getApplicationContext()).enableNotification();
        initQb();


    }
//    @Override
//    public void onDestroy(){
//        super.onCreate();
//        Foreground.get(this).removeListener(myListener);
//    }


    private void initQb() {

    }
}
