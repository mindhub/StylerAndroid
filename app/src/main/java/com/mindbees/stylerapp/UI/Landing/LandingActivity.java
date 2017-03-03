package com.mindbees.stylerapp.UI.Landing;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UTILS.Constants;


/**
 * Created by User on 01-03-2017.
 */

public class LandingActivity extends BaseActivity {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_layout);
        initUi();
    }

    private void initUi() {
        if(getPref(Constants.FirstTime_TERMS,true))
        {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            PopUpTermsAndConditions p=PopUpTermsAndConditions.newInstance();
            p.show(mFragmentTransaction,"POPUP");
        }

    }
}
