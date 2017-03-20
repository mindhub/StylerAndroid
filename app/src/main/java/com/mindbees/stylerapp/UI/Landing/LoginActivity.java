package com.mindbees.stylerapp.UI.Landing;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.Models.Login.ModelLogin;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PATCH;


/**
 * Created by User on 01-03-2017.
 */

public class LoginActivity extends BaseActivity {
    EditText Email,Password;
    LinearLayout Login;
    String Semail,Spass;
    TextView Forgotpassword;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initUI();
        SetUi();
    }

    private void SetUi() {
        Email.setFocusable(false);
        Password.setFocusable(false);
        Email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Email.setFocusableInTouchMode(true);
                return false;
            }
        });
        Password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Password.setFocusableInTouchMode(true);
                return false;
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmail()&&checkPassword())

                {
                    SubmitLogin();

                }
            }
        });
        Forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                PopUpForgotPassword popUpForgotPassword=PopUpForgotPassword.newInstance();
                popUpForgotPassword.show(mFragmentTransaction,"POPOPFORGOT");
            }
        });
    }

    private void SubmitLogin() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("username",Semail);
        params.put("password",Spass);
        final APIService service = ServiceGenerator.createService(APIService.class, LoginActivity.this);
        Call<ModelLogin>call=service.login(params);
        call.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                if (response.isSuccessful())
                {if (pd.isShowing()) { pd.dismiss(); }
                    if (response.body()!=null&&response.body().getResult()!=null)
                    {
                        ModelLogin modelLogin=response.body();
                        String status=modelLogin.getResult().get(0).getUserStatus();
                        if (status.equals("1"))
                        {
                            showToast("Success");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                if (pd.isShowing()) { pd.dismiss(); }
            }
        });
    }

    private boolean checkPassword() {
        Spass= Password.getText().toString().trim();
        if (Spass.isEmpty())
        {
            Password.setError("Please Enter a Valid Password");
            Password.requestFocus();
            return  false;
        }
        else {
            return true;
        }

    }

    private boolean checkEmail() {
        Semail=Email.getText().toString().trim();
        if (Semail.isEmpty())
        {
            Email.setError("Please enter Valid email");
            Email.requestFocus();
            return false;
        }
        else {
            return true;
        }




    }

    private void initUI() {
        Email= (EditText) findViewById(R.id.editextloginEmail);
        Password= (EditText) findViewById(R.id.editextloginPassword);
        Login= (LinearLayout) findViewById(R.id.LOGIN_NOW_BUTTON);
        Forgotpassword= (TextView) findViewById(R.id.forgotPasswordButton);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/brandon_grotesque_bold.ttf");
        Typeface typeface1=Typeface.createFromAsset(getAssets(),"fonts/BrandonGrotesque-Regular.ttf");
        Email.setTypeface(typeface1);
        Password.setTypeface(typeface1);
        Forgotpassword.setTypeface(typeface);

    }
}
