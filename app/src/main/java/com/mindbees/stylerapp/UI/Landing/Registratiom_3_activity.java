package com.mindbees.stylerapp.UI.Landing;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;

import static com.mindbees.stylerapp.R.id.register_next;

/**
 * Created by User on 10-03-2017.
 */

public class Registratiom_3_activity extends BaseActivity {
    EditText editTextdesigners,editTextstyleIcons,editTextnevergo;
    TextView textViewdesigners,textViewstyleIcons,textViewnevergo;
    ImageView reg_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_3_layout);
        initUI();
        setupui();
    }

    private void setupui() {

    }

    private void initUI() {
        editTextdesigners= (EditText) findViewById(R.id.editfavourite_Designer);
        editTextstyleIcons= (EditText) findViewById(R.id.editstyle_icons);
        editTextnevergo= (EditText) findViewById(R.id.editnever_go);
        textViewdesigners= (TextView) findViewById(R.id.text_designerbrands);
        textViewstyleIcons= (TextView) findViewById(R.id.text_style_icons);
        textViewnevergo= (TextView) findViewById(R.id.text_never_go);
        reg_next= (ImageView) findViewById(register_next);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/brandon_grotesque_bold.ttf");
        Typeface typeface1=Typeface.createFromAsset(getAssets(),"fonts/BrandonGrotesque-Regular.ttf");
        editTextstyleIcons.setTypeface(typeface1);
        editTextnevergo.setTypeface(typeface1);
        editTextdesigners.setTypeface(typeface1);
        textViewdesigners.setTypeface(typeface);
        textViewnevergo.setTypeface(typeface);
        textViewstyleIcons.setTypeface(typeface);


    }
}
