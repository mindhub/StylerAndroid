package com.mindbees.stylerapp.UI.Landing;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment ;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UTILS.Util;

/**
 * Created by User on 20-03-2017.
 */

public class PopUpForgotPassword extends DialogFragment {
    TextView heading,sub;
    EditText editTextForgot;
    ImageView closebutton;
    LinearLayout submit;
    String Email;

    public static PopUpForgotPassword newInstance()
    {
        PopUpForgotPassword p=new PopUpForgotPassword();
        return p;
    }
    View view;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.popup_forgot_password,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(1);
        initUi(view);
        return view;
    }

    private void initUi(View view) {
        closebutton= (ImageView) view.findViewById(R.id.close_button);
        editTextForgot= (EditText) view.findViewById(R.id.editTextForgot);
        heading= (TextView) view.findViewById(R.id.textViewForgotHeading);
        sub= (TextView) view.findViewById(R.id.textViewForgot);
        submit= (LinearLayout) view.findViewById(R.id.layout_submit_Forgot);
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"fonts/brandon_grotesque_bold.ttf");
        Typeface typeface1=Typeface.createFromAsset(getContext().getAssets(),"fonts/BrandonGrotesque-Regular.ttf");
        editTextForgot.setTypeface(typeface1);
        heading.setTypeface(typeface);
        sub.setTypeface(typeface1);
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmail())
                {

                }
            }
        });


    }

    private boolean checkEmail() {
        Email=editTextForgot.getText().toString().trim();
        if (Email.isEmpty())
        {
            editTextForgot.setError("Please Enter a valid email Address");
            editTextForgot.requestFocus();
            return false;
        }
        else
        {
            if (Util.getUtils().isValidEmail(Email))
            {
                return true;
            }
            else {
                editTextForgot.setError("Please Enter a valid email Address");
                editTextForgot.requestFocus();
                return false;
            }
        }
    }
}
