package com.mindbees.stylerapp.UI.HOME;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.Landing.LandingActivity;
import com.mindbees.stylerapp.UTILS.Constants;

/**
 * Created by User on 21-03-2017.
 */

public class HomeActivity extends BaseActivity {
    Button close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUi();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void initUi() {
        close= (Button) findViewById(R.id.close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomeActivity.this,R.style.AppCompatAlertDialogStyle)
                        .setTitle("LOG OUT")
                        .setMessage("Are you Sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                savePref(Constants.TAG_ISLOGGED_IN,false);
//                                savePref(Constants.USER_ID,"");
                            Intent i  =  new Intent(HomeActivity.this,LandingActivity.class);
                                startActivity(i);


                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }
}
