package com.mindbees.stylerapp.UI.HOME;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

/**
 * Created by User on 11-04-2017.
 */

public class ChangePasswordActivity extends BaseActivity {
    ImageView back;
    String Sold,Snew;
    EditText oldpass,newpass;
    RelativeLayout eroroldpass,errornewpass;
    TextView erroroldpassText,errornewpasswordtext;
    LinearLayout saveNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepasswordactivity);
        initui();
        setupui();
    }

    private void setupui() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        oldpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                eroroldpass.setVisibility(View.GONE);
                return false;
            }
        });
        newpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                errornewpass.setVisibility(View.GONE);
                return false;
            }
        });
        saveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkold()&&checknew())
                {
                    getwebservice();
                }
            }
        });
    }

    private void getwebservice() {
        showProgress();
        HashMap<String, String> params = new HashMap<>();
        String userid=getPref(Constants.USER_ID);
        params.put("user_id",userid);
        params.put("new_password",Snew);
        APIService service= ServiceGenerator.createService(APIService.class,ChangePasswordActivity.this);
        Call<ModelUpdateProfile>call=service.updateprofile(params);
        call.enqueue(new Callback<ModelUpdateProfile>() {
            @Override
            public void onResponse(Call<ModelUpdateProfile> call, Response<ModelUpdateProfile> response) {
                if (response.isSuccessful())
                {
                    hideProgress();
                    if (response.body()!=null&&response.body().getResult()!=null)
                    {
                        ModelUpdateProfile updateprofile=response.body();
                        String msg=updateprofile.getResult().getMessage();
                        int value=updateprofile.getResult().getValue();
                        if (value==1)
                        {
                            showToast(msg,true);
                            savePref(Constants.PASSWORD,Snew);
                        }
                        else {
                            showToast(msg,false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelUpdateProfile> call, Throwable t) {
hideProgress();
            }
        });
    }

    private boolean checkold() {

        Sold=oldpass.getText().toString().trim();
        if (Sold.isEmpty())
        {
            eroroldpass.setVisibility(View.VISIBLE);
            oldpass.requestFocus();
            return false;
        }
        else {
            String temp=getPref(Constants.PASSWORD);
            if (Sold.equals(temp))
            {
                return true;
            }
            else {
                erroroldpassText.setText("Invalid old password");
                eroroldpass.setVisibility(View.VISIBLE);
                oldpass.requestFocus();
                return false;
            }
        }

    }

    private boolean checknew() {
        Snew=newpass.getText().toString().trim();
        if (Snew.isEmpty())
        {
            errornewpass.setVisibility(View.VISIBLE);
            newpass.requestFocus();
            return false;
        }
        else {
            if (Snew.length()<6)
            {
                errornewpasswordtext.setText("Password needs  minimum 6 characters");
                errornewpass.setVisibility(View.VISIBLE);
                newpass.requestFocus();
                return false;


            }
            else {
                return true;
            }

        }

    }

    private void initui() {
        oldpass= (EditText) findViewById(R.id.editold_passord);
        newpass= (EditText) findViewById(R.id.editnew_password);
        eroroldpass= (RelativeLayout) findViewById(R.id.erroroldpassword);
        errornewpass= (RelativeLayout) findViewById(R.id.errornewpassword);
        erroroldpassText= (TextView) findViewById(R.id.erroroldpasswordtext);
        errornewpasswordtext= (TextView) findViewById(R.id.textViewerrornewpassword);
        saveNow= (LinearLayout) findViewById(R.id.layoutsave);
        back= (ImageView) findViewById(R.id.back);

    }
}
