package com.mindbees.stylerapp.UI.HOME;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;

/**
 * Created by User on 11-04-2017.
 */

public class PrivacyActivity extends BaseActivity{
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacyactivitylayout);
        initui();

    }

    private void initui() {
        back= (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
