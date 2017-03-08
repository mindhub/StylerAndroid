package com.mindbees.stylerapp.App;

import android.accounts.Account;
import android.app.Application;


import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.mindbees.stylerapp.UTILS.Constants;
import com.mindbees.stylerapp.UTILS.Util;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBSettings;

/**
 * Created by User on 01-03-2017.
 */

public class Styler extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        new Util(this);
        QBSettings.getInstance().init(getApplicationContext(), Constants.APP_ID,Constants.AUTH_KEY, Constants.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constants.ACCOUNT_KEY);
        QBChatService.setDebugEnabled(true);
    }
}
