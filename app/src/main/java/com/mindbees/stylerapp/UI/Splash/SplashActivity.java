package com.mindbees.stylerapp.UI.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.Landing.LandingActivity;
import com.mindbees.stylerapp.UTILS.Constants;


/**
 * Created by User on 01-03-2017.
 */

public class SplashActivity extends BaseActivity {
    private static final long TAG_TIMER_DELAY = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlayoutactivity);
        initializeTimerTask();
    }
    private void initializeTimerTask() {



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (getPref(Constants.TAG_ISLOGGED_IN, false)) {

//                    Intent i = new Intent(Splash_Activity.this, Home.class);
//                    startActivity(i);
//                    MainActivity.start(SplashActivity.this);
                } else {
                   startActivity(new Intent(SplashActivity.this,LandingActivity.class));
//                    InitialActivity.start(SplashActivity.this);
                }
                finish();
            }


        }, TAG_TIMER_DELAY);
    }

}
