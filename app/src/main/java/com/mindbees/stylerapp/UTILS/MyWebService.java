package com.mindbees.stylerapp.UTILS;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mindbees.stylerapp.App.Styler;
import com.mindbees.stylerapp.UI.Models.update_profile.ModelUpdateProfile;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 10-05-2017.
 */

public class MyWebService extends Service {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
//     * @param name Used to name the worker thread, important only for debugging.
     */
//    public MyWebService(String name) {
//        super(name);
//    }
//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        HashMap<String, String> params = new HashMap<>();
//        String Userid=intent.getStringExtra("USER_ID");
//        params.put("user_id",Userid);
//        params.put("online_status","0");
//        final APIService service = ServiceGenerator.createService(APIService.class, getApplicationContext());
//        Call<ModelUpdateProfile> call=service.updateprofile(params);
//        call.enqueue(new Callback<ModelUpdateProfile>() {
//            @Override
//            public void onResponse(Call<ModelUpdateProfile> call, Response<ModelUpdateProfile> response) {
//                if (response.isSuccessful()) {
//
//                    if (response.body() != null && response.body().getResult() != null) {
//                        ModelUpdateProfile modelUpdateProfile = response.body();
//                        int value = modelUpdateProfile.getResult().getValue();
//                        if (value == 1) {
//                            String msg = modelUpdateProfile.getResult().getMessage();
////                            showToast(msg, true);
////                            if (switchtemp)
////                            {
////                                aSwitch.setChecked(true);
////                            }
////                            else {
////                                aSwitch.setChecked(false);
////                            }
//
//                        } else {
//                            String msg = modelUpdateProfile.getResult().getMessage();
//
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ModelUpdateProfile> call, Throwable t) {
//
//
//            }
//        });
//    }
    public void webservice()
    {
        HashMap<String, String> params = new HashMap<>();
        String userid=Util.getUtils().getPref(Constants.USER_ID);

        params.put("user_id",userid);
        params.put("online_status","0");
        final APIService service = ServiceGenerator.createService(APIService.class, getApplicationContext());
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
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
        webservice();


        return super.onStartCommand(intent, flags, startId);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
