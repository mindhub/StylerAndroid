package com.mindbees.stylerapp.App;

import android.app.Application;
import android.content.Context;



import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.mindbees.stylerapp.UTILS.Constants;

import com.mindbees.stylerapp.UTILS.Util;

import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBSettings;


/**
 * Created by User on 01-03-2017.
 */

public class Styler extends Application{
public static Styler instance;
    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        new Util(this);


    }

    private void initApplication() {
        instance = this;

        initQb();


    }



    private void initQb() {
        QBChatService.setDebugEnabled(true);
        QBSettings.getInstance().init(getApplicationContext(), Constants.APP_ID,Constants.AUTH_KEY, Constants.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constants.ACCOUNT_KEY);
    }
}
