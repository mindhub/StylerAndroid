package com.mindbees.stylerapp.UI.Landing;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.HOME.HomeActivity;
import com.mindbees.stylerapp.UI.Models.update_profile.ModelUpdateProfile;
import com.mindbees.stylerapp.UTILS.Constants;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;

import java.util.HashMap;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mindbees.stylerapp.R.id.register_next;

/**
 * Created by User on 10-03-2017.
 */

public class Registratiom_3_activity extends BaseActivity {
    String Sdesigners,StyleIcons,Snevergo;
    private AlertDialog progressDialog;
    MaterialDialog builder;
    RelativeLayout errordesigner,errorstyleicons,errornevrgo;

    TextView nevergo_ext;
    EditText editTextdesigners,editTextstyleIcons,editTextnevergo;
    TextView textViewdesigners,textViewstyleIcons,textViewnevergo;
    ImageView reg_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_3_layout);
        setupUI(findViewById(R.id.registrationlayout_3));
        initUI();
        if (isNetworkAvailable())
        {
            setupui();
        }
        else {
            showToast("Please check network connectivity",false);
        }

//        progressDialog = new SpotsDialog(this, R.style.Custom);
    }

    private void setupui() {
        editTextdesigners.setHorizontallyScrolling(false);
        editTextdesigners.setMaxLines(2);
        editTextnevergo.setHorizontallyScrolling(false);
        editTextnevergo.setMaxLines(2);
        editTextstyleIcons.setHorizontallyScrolling(false);
        editTextstyleIcons.setMaxLines(2);
        editTextdesigners.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                errordesigner.setVisibility(View.GONE);
                return false;
            }
        });
        editTextstyleIcons.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                errorstyleicons.setVisibility(View.GONE);
                return false;
            }
        });
        editTextnevergo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                errornevrgo.setVisibility(View.GONE);
                return false;
            }
        });
        reg_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkdesigners()&&checkstyleicons()&&checknevergo())
                {
                    Submitslection();

                }
            }




        });

    }

    private void Submitslection() {
//        final ProgressDialog pd = new ProgressDialog(this);
//        pd.setMessage("Loading");
//        pd.setCancelable(false);
//        pd.show();
        showProgress();

//        builder= new MaterialDialog.Builder(this)
//                .backgroundColor(Color.TRANSPARENT)
//                .backgroundColorRes(R.color.thistle)
//                .content(R.string.please_wait)
//                .progress(true, 0)
//                .show();
//        progressDialog.show();
        HashMap<String, String> params = new HashMap<>();
        String userid=getPref(Constants.USER_ID);
        params.put("user_id", userid);
        params.put("userbrands",Sdesigners);
        params.put("user_styles_icons",StyleIcons);
        params.put("user_unliked",Snevergo);
        final APIService service = ServiceGenerator.createService(APIService.class, Registratiom_3_activity.this);
        Call<ModelUpdateProfile>call=service.updateprofile(params);
        call.enqueue(new Callback<ModelUpdateProfile>() {
            @Override
            public void onResponse(Call<ModelUpdateProfile> call, Response<ModelUpdateProfile> response) {
                if (response.isSuccessful())
                {
//                    builder.dismiss();
                    hideProgress();
                    if (response.body()!=null&&response.body().getResult()!=null)
                    {
                        ModelUpdateProfile model=response.body();
                        int value=model.getResult().getValue();
                        if (value==1)
                        {
                            String msg=model.getResult().getMessage();
                            showToast("Registered successfully",true);
                            Intent intent=new Intent(Registratiom_3_activity.this,HomeActivity.class);
                            savePref(Constants.TAG_ISLOGGED_IN,true);
                            startActivity(intent);
                        }
                        else {
                            String msg=model.getResult().getMessage();
                            showToast(msg,false);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ModelUpdateProfile> call, Throwable t) {
                hideProgress();

//                builder.dismiss();
            }
        });
    }

    private boolean checknevergo() {
        Snevergo=editTextnevergo.getText().toString().trim();
        if (Snevergo.isEmpty())
        {
//            editTextnevergo.setError("Plese enter your choice");
//            showToast("Please enter your choice");
            errornevrgo.setVisibility(View.VISIBLE);
            editTextnevergo.requestFocus();
            return false;
        }
        else {
            return true;
        }

    }

    @Override
    public void onBackPressed() {

    }

    private boolean checkstyleicons() {
        StyleIcons=editTextstyleIcons.getText().toString().trim();
        if (StyleIcons.isEmpty())
        {
//            editTextstyleIcons.setError("Please enter your favourite style icons");
//            showToast("Please enter your favorite style icons");
            errorstyleicons.setVisibility(View.VISIBLE);
            editTextstyleIcons.requestFocus();
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean checkdesigners() {
        Sdesigners=editTextdesigners.getText().toString().trim();
        if (Sdesigners.isEmpty())
      {
//          showToast("Please enter your favorite designers");
          errordesigner.setVisibility(View.VISIBLE);
//            editTextdesigners.setError("Please enter your favourite designers");
            editTextdesigners.requestFocus();
            return false;
        }
        else {
            return true;
        }

    }

    private void initUI() {
        editTextdesigners= (EditText) findViewById(R.id.editfavourite_Designer);
        editTextstyleIcons= (EditText) findViewById(R.id.editstyle_icons);
        editTextnevergo= (EditText) findViewById(R.id.editnever_go);
        textViewdesigners= (TextView) findViewById(R.id.text_designerbrands);
        textViewstyleIcons= (TextView) findViewById(R.id.text_style_icons);
        textViewnevergo= (TextView) findViewById(R.id.text_never_go);
        nevergo_ext= (TextView) findViewById(R.id.text_never_go_ext);
        nevergo_ext.setText("(Don't be shy, we've all thought of this at least once!)");
        reg_next= (ImageView) findViewById(register_next);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/brandon_grotesque_bold.ttf");
        Typeface typeface1=Typeface.createFromAsset(getAssets(),"fonts/BrandonGrotesque-Regular.ttf");
        editTextstyleIcons.setTypeface(typeface1);
        editTextnevergo.setTypeface(typeface1);
        editTextdesigners.setTypeface(typeface1);
        textViewdesigners.setTypeface(typeface);
        textViewnevergo.setTypeface(typeface);
        textViewstyleIcons.setTypeface(typeface);
        errordesigner= (RelativeLayout) findViewById(R.id.errordesigners);
        errorstyleicons= (RelativeLayout) findViewById(R.id.errorstyleicons);
        errornevrgo= (RelativeLayout) findViewById(R.id.errornevergo);


    }
}
