package com.mindbees.stylerapp.UI.HOME;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applozic.audiovideo.activity.AudioCallActivityV2;
import com.applozic.audiovideo.activity.VideoActivity;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.PushNotificationTask;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.Landing.LandingActivity;
import com.mindbees.stylerapp.UI.Landing.MyInterface;
import com.mindbees.stylerapp.UI.TabFragments.FavouriteActivity;
import com.mindbees.stylerapp.UI.TabFragments.HomeTabfragment;
import com.mindbees.stylerapp.UTILS.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 21-03-2017.
 */

public class HomeActivity extends BaseActivity implements MyInterface{
    FragmentManager mFragmentManager;
    private int position = 0;
    FragmentTransaction mFragmentTransaction;
    Button close;
    RelativeLayout browselayout,matchlayout,prolayout,checkinlayout;
    ImageView imageViewBroswe,imageViewmatch,imageViewpro,imageViewcheckin;
    TextView textViewbrowse,textViewmatch,textViewpro,textViewcheckin;
    ImageView fav,settings;
    FrameLayout layout1,layout2;
    private UserLoginTask mAuthTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        setTitle("");
//        getSupportActionBar().setCustomView();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        initUi();
        selectFragment();
        initializapplozic();
    }

    private void initializapplozic() {
        UserLoginTask.TaskListener listener=new UserLoginTask.TaskListener() {
                                @Override
                                public void onSuccess(RegistrationResponse registrationResponse, Context context) {

                                    ApplozicClient.getInstance(context).setContextBasedChat(true).setHandleDial(true).setIPCallEnabled(true);
                                    Map<ApplozicSetting.RequestCode, String> activityCallbacks = new HashMap<ApplozicSetting.RequestCode, String>();
                                    activityCallbacks.put(ApplozicSetting.RequestCode.AUDIO_CALL, AudioCallActivityV2.class.getName());
                                    activityCallbacks.put(ApplozicSetting.RequestCode.VIDEO_CALL, VideoActivity.class.getName());
                                    ApplozicSetting.getInstance(context).setActivityCallbacks(activityCallbacks);
                                    ApplozicClient.getInstance(context).hideChatListOnNotification();
//                                    ApplozicSetting.getInstance(context).setChatBackgroundColorOrDrawableResource(R.drawable.bg_splash);
//                                    ApplozicSetting.getInstance(context).setSentMessageBackgroundColor(R.color.white);
//                                    ApplozicSetting.getInstance(context).setReceivedMessageBackgroundColor(R.color.sentmessage_black);
//                                    ApplozicSetting.getInstance(context).setSentMessageTextColor(R.color.black);
//                                    ApplozicSetting.getInstance(context).setReceivedMessageTextColor(R.color.white);
                                    PushNotificationTask.TaskListener pushNotificationTaskListener=  new PushNotificationTask.TaskListener() {
                                        @Override
                                        public void onSuccess(RegistrationResponse registrationResponse) {

                                        }

                                        @Override
                                        public void onFailure(RegistrationResponse registrationResponse, Exception exception) {

                                        }
                                    };
                                    PushNotificationTask pushNotificationTask = new PushNotificationTask(Applozic.getInstance(context).getDeviceRegistrationId(),pushNotificationTaskListener,context);
                                    pushNotificationTask.execute((Void)null);
                                }

                                @Override
                                public void onFailure(RegistrationResponse registrationResponse, Exception exception) {

                                }
                            };
                            User user = new User();
                            String userid=getPref(Constants.USER_ID);
                            if (userid!=null) {
                                user.setUserId(userid);
                            }
                            String username=getPref(Constants.USERNAME);
                            if (username!=null)
                            {
                                                            user.setDisplayName(username);
                            }

                            user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());
                            List<String> featureList =  new ArrayList<>();
                            featureList.add(User.Features.IP_AUDIO_CALL.getValue());// FOR AUDIO
                            featureList.add(User.Features.IP_VIDEO_CALL.getValue());// FOR VIDEO
                            user.setFeatures(featureList);
                            mAuthTask = new UserLoginTask(user, listener, HomeActivity.this);
                            mAuthTask.execute((Void) null);
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
    public void setFragmentView(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }
    private void selectFragment() {
        switch (position) {
            case 0:
                HomeTabfragment fragment=new HomeTabfragment();

                setFragmentView(fragment);
                break;
//           case 1:
//                FavouriteActivity fragmen1=new FavouriteActivity();
//               setFragmentView(fragmen1);
//               break;
//            case 3:SettingsFragment fragment1=new SettingsFragment();
//                setFragmentView(fragment1);
//                break;
//            default:
//                TabFragment fragment2=new TabFragment();
//                setFragmentView(fragment2);
//                break;


        }
    }
    private void initUi() {
        close= (Button) findViewById(R.id.close_button);
        layout1= (FrameLayout) findViewById(R.id.frame);
        fav= (ImageView) findViewById(R.id.favButton);
        browselayout= (RelativeLayout) findViewById(R.id.Browselayout);
        imageViewBroswe= (ImageView) findViewById(R.id.imagebrowse);
        textViewbrowse= (TextView) findViewById(R.id.textbrowse);
        matchlayout= (RelativeLayout) findViewById(R.id.Matchlayout);
        imageViewmatch= (ImageView) findViewById(R.id.imagematch);
        textViewmatch= (TextView) findViewById(R.id.textmatch);
        prolayout= (RelativeLayout) findViewById(R.id.Prolayout);
        imageViewpro= (ImageView) findViewById(R.id.imagepro);
        textViewpro= (TextView) findViewById(R.id.textpro);
        checkinlayout= (RelativeLayout) findViewById(R.id.checkinlayout);
        imageViewcheckin= (ImageView) findViewById(R.id.imagecheckin);
        textViewcheckin= (TextView) findViewById(R.id.textcheckin);
        browselayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewBroswe.setImageResource(R.drawable.btm1a);
                imageViewmatch.setImageResource(R.drawable.btm2);
                imageViewcheckin.setImageResource(R.drawable.btm4);
                imageViewpro.setImageResource(R.drawable.btm3);
                textViewbrowse.setTextColor(getResources().getColor(R.color.dark_slate_blue));
                textViewmatch.setTextColor(getResources().getColor(R.color.white));
                textViewpro.setTextColor(getResources().getColor(R.color.white));
                textViewcheckin.setTextColor(getResources().getColor(R.color.white));

            }
        });
        matchlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewBroswe.setImageResource(R.drawable.btm1);
                imageViewmatch.setImageResource(R.drawable.btm2a);
                imageViewcheckin.setImageResource(R.drawable.btm4);
                imageViewpro.setImageResource(R.drawable.btm3);
                textViewbrowse.setTextColor(getResources().getColor(R.color.white));
                textViewmatch.setTextColor(getResources().getColor(R.color.dark_slate_blue));
                textViewpro.setTextColor(getResources().getColor(R.color.white));
                textViewcheckin.setTextColor(getResources().getColor(R.color.white));
            }
        });
        prolayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewBroswe.setImageResource(R.drawable.btm1);
                imageViewmatch.setImageResource(R.drawable.btm2);
                imageViewcheckin.setImageResource(R.drawable.btm4);
                imageViewpro.setImageResource(R.drawable.btm3a);
                textViewbrowse.setTextColor(getResources().getColor(R.color.white));
                textViewmatch.setTextColor(getResources().getColor(R.color.white));
                textViewpro.setTextColor(getResources().getColor(R.color.dark_slate_blue));
                textViewcheckin.setTextColor(getResources().getColor(R.color.white));
            }
        });
        checkinlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewBroswe.setImageResource(R.drawable.btm1);
                imageViewmatch.setImageResource(R.drawable.btm2);
                imageViewcheckin.setImageResource(R.drawable.btm4a);
                imageViewpro.setImageResource(R.drawable.btm3);
                textViewbrowse.setTextColor(getResources().getColor(R.color.white));
                textViewmatch.setTextColor(getResources().getColor(R.color.white));
                textViewpro.setTextColor(getResources().getColor(R.color.white));
                textViewcheckin.setTextColor(getResources().getColor(R.color.dark_slate_blue));
            }
        });
        settings= (ImageView) findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,FavouriteActivity.class);
                startActivity(intent);
            }
        });
        layout2= (FrameLayout) findViewById(R.id.container);
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

    @Override
    public void setVisibility(boolean visibility) {
        if (visibility==true)
        {
            layout2.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.GONE);
        }
        else
        {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }
    }
}
