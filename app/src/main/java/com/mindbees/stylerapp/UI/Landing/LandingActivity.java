package com.mindbees.stylerapp.UI.Landing;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UTILS.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by User on 01-03-2017.
 */

public class LandingActivity extends BaseActivity {
    LinearLayout Login,register;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_layout);
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
                Intent i=new Intent(LandingActivity.this,Registration_2_Activity.class);
                startActivity(i);
            }
        });

    }
}
