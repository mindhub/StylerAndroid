package com.mindbees.stylerapp.UI.Landing;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
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
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
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
                {if (pd.isShowing()) { pd.dismiss(); }
                    if (response.body()!=null&&response.body().getResult()!=null)
                    {
                        ModelUpdateProfile model=response.body();
                        int value=model.getResult().getValue();
                        if (value==1)
                        {
                            String msg=model.getResult().getMessage();
                            showToast(msg);
                        }
                        else {
                            String msg=model.getResult().getMessage();
                            showToast(msg);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ModelUpdateProfile> call, Throwable t) {
                if (pd.isShowing()) { pd.dismiss(); }
            }
        });
    }

    private boolean checknevergo() {
        Snevergo=editTextnevergo.getText().toString().trim();
        if (Snevergo.isEmpty())
        {
            editTextnevergo.setError("Plese enter your choice");
            editTextnevergo.requestFocus();
            return false;
        }
        else {
            return true;
        }

    }

    private boolean checkstyleicons() {
        StyleIcons=editTextstyleIcons.getText().toString().trim();
        if (StyleIcons.isEmpty())
        {
            editTextstyleIcons.setError("Please Enter your favourite Style icons");
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
            editTextdesigners.setError("Please Enter your Favourite designers");
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
