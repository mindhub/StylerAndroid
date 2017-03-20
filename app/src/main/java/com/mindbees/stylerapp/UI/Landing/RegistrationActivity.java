package com.mindbees.stylerapp.UI.Landing;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Adapter.SpinnerAdapter;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.Models.Ethnicity.ModelEthnicity;
import com.mindbees.stylerapp.UI.Models.Ethnicity.Result;
import com.mindbees.stylerapp.UI.Models.Register.ModelRegister;
import com.mindbees.stylerapp.UI.Models.SpinnerModel;
import com.mindbees.stylerapp.UI.PrivacyPolicy.PrivacyPolicy;
import com.mindbees.stylerapp.UTILS.Constants;
import com.mindbees.stylerapp.UTILS.InternalStorageContentProvider;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;
import com.mindbees.stylerapp.UTILS.RoundedImageView;
import com.mindbees.stylerapp.UTILS.Util;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import eu.janmuller.android.simplecropimage.CropImage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.mindbees.stylerapp.R.id.radio;


/**
 * Created by User on 01-03-2017.
 */

public class RegistrationActivity extends BaseActivity implements LocationListener{
    String Fname,Lname,Semail,Spassword,Sdob,Sloc,Slike,Sweight,Sheight,Sethnicity,Slookingfor,SuserName;
    String gender="f";
    TextView iam;
    String EthnicityTitle="";
    String EthnicityId="";
    File f;
    String iLike="mf";
    int pos;
    String userID;

    String fbid="",fbname="",email,genderfb,first_name,last_name;
    LinearLayout likemale,likefemale,likeboth;
    TextView textlikemale,textlikefemale,textlikeboth,textheadlike;



    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private static final int ACCESS_FINE_LOCATION = 0;
    private LocationManager locationManager;
    private String provider;
    RoundedImageView userpic;
    LatLng southwest,northeast;
    String cityname;
    String Lat;
    String LONG;
    ImageView imageViewSpinner;
    boolean others=false;
    LatLng temp;
    LatLngBounds BOUNDS_MOUNTAIN_VIEW;
    public static final int PLACE_PICKER_REQUEST = 00;
    ImageView picedit,reg_next;
    private File mFileTemp;
    LinearLayout Linearmale,Linearfemale;
    TextView textmale,textfemale,textilike,textloooking;
    EditText editemail,editpass,editdob,editloc,editheight,editweight,editethnicity,editlookingfor,editTextfirstname,editTextlastname,editTextUsername;
    AppCompatSpinner spinethnic;
    CheckBox checkBoxprivacy;
    TextView textViewPrivacy;
    RadioGroup rg;
    double lat,lng;
    boolean selected=false;
    public List<SpinnerModel> listitems;
    List<Result>list;
    ArrayList<String>listelements;
    public static final int REQUEST_CODE_GALLERY      = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;
    String []interval={"No Interval","15 Minutes","30 Minutes","1 Hour","2 Hour","4 Hour","6 Hour","8 Hour","12 Hour","5 Minutes"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        listitems=new ArrayList<SpinnerModel>();

        initui();
        Initializepicker();
        if (isNetworkAvailable())
        {
            getEthnicity();

        }
        else {
            showToast("NETWORK NOT AVAILABLE");
        }
        setupUi();
        try{
            Bundle i=getIntent().getExtras();
            fbid =i.getString("fbid");
            fbname=i.getString("name");
            genderfb=i.getString("gender");
            email=i.getString("email");
//            first_name=i.getString("first_name");
//            last_name=i.getString("last_name");
            SetFb();

        }catch (Exception e){}
      try
      {
          Getlocation();
      }
      catch (Exception e)
      {

      }


        Resources res = getResources();

//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(RegistrationActivity.this,R.layout.spinner_item,interval);
//        adapter.setDropDownViewResource(R.layout.spinner_item);
//        spinethnic.setAdapter(adapter);
        BOUNDS_MOUNTAIN_VIEW=new LatLngBounds(southwest,northeast);

    }

    private void SetFb() {
        if(!fbname.isEmpty()) {
//            editTextfirstname.setText(fbname);
            String[]Splitted=fbname.split("\\s");
            editTextfirstname.setText(Splitted[0]);
            editTextlastname.setText(Splitted[1]);
        }
//        if(!last_name.isEmpty()) {
//            editTextlastname.setText(fbname);
//        }
//        showToast(email);
        if (!email.isEmpty()){
         editemail.setText(email);
      }

       editpass.setVisibility(View.GONE);
        if (!gender.isEmpty())
        {
            if (gender.equals("f"))
           {
                Linearfemale.setBackgroundColor(getResources().getColor(R.color.black));
                textfemale.setTextColor(getResources().getColor(R.color.white));
                Linearmale.setBackgroundColor(getResources().getColor(R.color.white));
                textmale.setTextColor(getResources().getColor(R.color.black));
                gender="f";
            }
            else {
                Linearmale.setBackgroundColor(getResources().getColor(R.color.black));
                textmale.setTextColor(getResources().getColor(R.color.white));
                Linearfemale.setBackgroundColor(getResources().getColor(R.color.white));
                textfemale.setTextColor(getResources().getColor(R.color.black));
                gender="m";
            }

        }
       String user_photo="https://graph.facebook.com/" + fbid + "/picture?type=large";
        Picasso.with(this).load(user_photo).into(userpic);
        try {
            setImageBitmap(user_photo);
        }catch (Exception e)
        {

        }
    }

    private void setImageBitmap(String imageurl) throws IOException{
        URL url = new URL(imageurl);
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        bimapTofile(bmp);
    }

    private void getEthnicity() {
        HashMap<String, String> params = new HashMap<>();
        APIService service = ServiceGenerator.createService(APIService.class, RegistrationActivity.this);
        Call<ModelEthnicity>call=service.getethnic(params);
        call.enqueue(new Callback<ModelEthnicity>() {
            @Override
            public void onResponse(Call<ModelEthnicity> call, Response<ModelEthnicity> response) {
                if(response.isSuccessful())
                {
                    listelements=new ArrayList<String>();
                    if (response.body()!=null&&response.body().getResult()!=null)
                    {
                        ModelEthnicity model=response.body();
                        list=model.getResult();
                        final String[]items=new String[list.size()];
                        SpinnerModel model2=new SpinnerModel();
                        model2.setName("ETHNICITY");
                        listitems.add(model2);


                        for(int i=0;i<list.size();i++)
                        {
                            SpinnerModel model1=new SpinnerModel();
                            model1.setName(list.get(i).getEthnicityTitle());
                            listitems.add(model1);
//                            String item=list.get(i).getEthnicityTitle();
//                            items[i]=item;
                        }
//                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(RegistrationActivity.this,R.layout.spinner_item,items);
//                        adapter.setDropDownViewResource(R.layout.spinner_item);
//                        spinethnic.setAdapter(adapter);
                        SpinnerModel model3=new SpinnerModel();
                        model3.setName("OTHERS");
                        listitems.add(model3);
                        SpinnerAdapter adapter=new SpinnerAdapter(RegistrationActivity.this,listitems);
                        spinethnic.setAdapter(adapter);
                    }
                }

            }

            @Override
            public void onFailure(Call<ModelEthnicity> call, Throwable t) {

            }
        });

    }

    private void setupUi() {
        editlookingfor.setFocusable(false);
        editTextfirstname.setFocusable(false);
        editTextlastname.setFocusable(false);
        editethnicity.setFocusable(false);
        editemail.setFocusable(false);
        editpass.setFocusable(false);
        editloc.setFocusable(false);
        editheight.setFocusable(false);
        editweight.setFocusable(false);
        editTextUsername.setFocusable(false);
        editTextUsername.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editTextUsername.setFocusableInTouchMode(true);
                return false;
            }
        });
        editTextfirstname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editTextfirstname.setFocusableInTouchMode(true);
                return false;
            }
        });
        editTextlastname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editTextlastname.setFocusableInTouchMode(true);
                return false;
            }
        });
        editemail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editemail.setFocusableInTouchMode(true);
                return false;
            }
        });
        editloc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                editloc.setFocusableInTouchMode(true);
                return false;
            }
        });
        editpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editpass.setFocusableInTouchMode(true);
                return false;
            }
        });
        editweight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editweight.setFocusableInTouchMode(true);
                return false;
            }
        });
        editheight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editheight.setFocusableInTouchMode(true);
                return false;
            }
        });
        editlookingfor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editlookingfor.setFocusableInTouchMode(true);
                return false;
            }
        });
        Linearmale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linearmale.setBackgroundColor(getResources().getColor(R.color.black));
                textmale.setTextColor(getResources().getColor(R.color.white));
                Linearfemale.setBackgroundColor(getResources().getColor(R.color.white));
                textfemale.setTextColor(getResources().getColor(R.color.black));
                gender="m";

            }
        });
        Linearfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linearfemale.setBackgroundColor(getResources().getColor(R.color.black));
                textfemale.setTextColor(getResources().getColor(R.color.white));
                Linearmale.setBackgroundColor(getResources().getColor(R.color.white));
                textmale.setTextColor(getResources().getColor(R.color.black));
                gender="f";
            }
        });
        likemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textlikemale.setTextColor(getResources().getColor(R.color.white));
                likemale.setBackgroundColor(getResources().getColor(R.color.black));

                likefemale.setBackgroundColor(getResources().getColor(R.color.white));
                likeboth.setBackgroundColor(getResources().getColor(R.color.white));
                textlikeboth.setTextColor(getResources().getColor(R.color.black));
                textlikefemale.setTextColor(getResources().getColor(R.color.black));
                iLike="m";
            }
        });
        likefemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likefemale.setBackgroundColor(getResources().getColor(R.color.black));
                textlikefemale.setTextColor(getResources().getColor(R.color.white));
                likemale.setBackgroundColor(getResources().getColor(R.color.white));
                likeboth.setBackgroundColor(getResources().getColor(R.color.white));
                textlikeboth.setTextColor(getResources().getColor(R.color.black));
                textlikemale.setTextColor(getResources().getColor(R.color.black));
                iLike="f";
            }
        });
        likeboth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeboth.setBackgroundColor(getResources().getColor(R.color.black));
                textlikeboth.setTextColor(getResources().getColor(R.color.white));
                likemale.setBackgroundColor(getResources().getColor(R.color.white));
                likefemale.setBackgroundColor(getResources().getColor(R.color.white));
                textlikemale.setTextColor(getResources().getColor(R.color.black));
                textlikefemale.setTextColor(getResources().getColor(R.color.black));
                iLike="mf";
            }
        });
        picedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFileTemp= Util.createFile(RegistrationActivity.this);
                pickPhotoDialog();
            }
        });
        editdob.setFocusable(false);
        ((RadioButton)rg.getChildAt(0)).setChecked(true);
        editdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calender=Calendar.getInstance(TimeZone.getDefault());
                int Year=calender.get(Calendar.YEAR);
                final int Month=calender.get(Calendar.MONTH);
                int Date=calender.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datepicker=new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                       editdob.setText(year+"-"+(month+1)+"-"+dayOfMonth);



                    }
                },Year,Month,Date);
                datepicker.setTitle("Select Date");
                datepicker.show();
            }
        });
//        editloc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    PlacePicker.IntentBuilder intentBuilder =
//                            new PlacePicker.IntentBuilder();
//                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
//
//                    Intent intent = intentBuilder.build(( getApplicationContext()));
//                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        textViewPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                PrivacyPolicy privacyPolicy=PrivacyPolicy.newInstance();
                privacyPolicy.show(mFragmentTransaction,"POP_UP_PRIVACY");
            }
        });

        editethnicity.setText("ETHNICITY");
        editethnicity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                }
                return false;

            }
        });
        imageViewSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editethnicity.post(new Runnable() {
                    @Override
                    public void run() {
                        editethnicity.setFocusable(false);

                        editethnicity.setFocusableInTouchMode(false);
                        editethnicity.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                editethnicity.setFocusableInTouchMode(false);
                                return false;
                            }
                        });
                    }
                });
                editethnicity.setText("ETHNICITY");
                others=false;
                spinethnic.setVisibility(View.VISIBLE);
                spinethnic.performClick();
                selected=true;


            }
        });

        spinethnic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
              if(selected) {
                  if(position!=0) {
                      if(!listitems.get(position).getName().equals("OTHERS")){

                          EthnicityId = list.get(position - 1).getEthnicityId();
                          String name = list.get(position - 1).getEthnicityTitle();
//                    showToast(EthnicityTitle);
                          editethnicity.setText(name);
                          spinethnic.setVisibility(View.INVISIBLE);
                          selected = false;
                      }
                      else {
                          spinethnic.setVisibility(View.INVISIBLE);
                          others=true;
                          EthnicityId="0";

                          editethnicity.post(new Runnable() {
                              @Override
                              public void run() {
                                  editethnicity.setFocusable(true);

                                  editethnicity.setFocusableInTouchMode(true);
                                  editethnicity.requestFocus();
                                  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                  imm.showSoftInput(editethnicity, InputMethodManager.SHOW_IMPLICIT);
                              }
                          });

                          editethnicity.setHint("Please Specify");
                          editethnicity.setText("");
                          editethnicity.setOnTouchListener(new View.OnTouchListener() {
                              @Override
                              public boolean onTouch(View v, MotionEvent event) {
                                  editethnicity.setFocusableInTouchMode(true);
                                  return false;
                              }
                          });




                      }
                  }
              }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editethnicity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!others) {
                    spinethnic.setVisibility(View.VISIBLE);


                    spinethnic.performClick();

                    selected = true;
                }



            }
        });
//        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                int pos=rg.indexOfChild(findViewById(checkedId));
//                switch (pos)
//                {
//                    case 0:iLike="m";
//                        break;
//                    case 1:iLike="f";
//                        break;
//                    case 2:iLike="mf";
//                        break;
//                    default:
//                        iLike="m";
//                        break;
//                }
//
//
//            }
//        });
        reg_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegistrationActivity.this,Registration_2_Activity.class);
                startActivity(intent);
//                UploadImageTask(f);
//               checkRegistration();
            }
        });

    }

    private void checkRegistration() {
        if(checkname()&&checklastname()&&checkUsername()&&checkemail()&&checkpassword()&&checkdob()&&checkloc()&&checkheight()&&checkweight()&&checkethnicity()&&checkrequired()&&checkprivacy())
        {
            submitreg();
//            showToast(iLike);
        }
    }

    private boolean checkUsername() {
        SuserName=editTextUsername.getText().toString().trim();
        if (SuserName.isEmpty())
        {
            editTextUsername.setError("Please enter user name");
            editTextUsername.post(new Runnable() {
                @Override
                public void run() {
                    editTextUsername.requestFocus();
                    editTextUsername.setFocusable(true);
                    editTextUsername.setFocusableInTouchMode(true);
                }
            });

            return false;
        }
        else
        {
            return true;
        }

    }

    private boolean checkpassword() {

        Spassword=editpass.getText().toString().trim();
        if (fbid.isEmpty()) {
            if (Spassword.isEmpty()) {
                editpass.setError("Please enter valid password");
                editpass.post(new Runnable() {
                    @Override
                    public void run() {
                        editpass.setFocusable(true);
                        editpass.setFocusableInTouchMode(true);
                        editpass.requestFocus();
                    }
                });

                return false;
            } else {
                if (Spassword.length() < 6) {
                    editpass.setError("Password length must be greater than 6");
                    editpass.post(new Runnable() {
                        @Override
                        public void run() {
                            editpass.setFocusable(true);
                            editpass.setFocusableInTouchMode(true);
                            editpass.requestFocus();
                        }
                    });
                    return false;

                } else {
                    return true;
                }
            }
        }
        else {
            return true;
        }
    }

    private boolean checklastname() {
        Lname=editTextlastname.getText().toString().trim();
        if (Lname.isEmpty())
        {
            editTextlastname.setError("Please enter Last name");
            editTextlastname.post(new Runnable() {
                @Override
                public void run() {
                    editTextlastname.setFocusable(true);
                    editTextlastname.setFocusableInTouchMode(true);
                    editTextlastname.requestFocus();
                }
            });

            return false;
        }
       else
        {
            return true;
        }
    }

    private boolean checkprivacy() {
        if (checkBoxprivacy.isChecked())
        {
            return true;
        }
        else {
            showSnackBar("Please Accept Privacy Policy",false);
            return false;
        }
    }



    private boolean checkrequired() {
        Slookingfor=editlookingfor.getText().toString().trim();


        return true;
    }

    private boolean checkethnicity() {
//        Sethnicity=listitems.get(pos).getName();
        Sethnicity=editethnicity.getText().toString().trim();
        if (EthnicityId.equals("0"))
        {
            if (!Sethnicity.isEmpty())
            {
                EthnicityTitle=Sethnicity;
                return true;
            }
            else {
                showToast("Please Select Ethnicity");
                return false;
            }
        }
        else {
            if (Sethnicity.equals("ETHNICITY"))
            {
                showToast("Please select Ethnicity");
                return false;
            }
            else {
                return true;
            }
        }



    }

    private boolean checkheight() {
        Sheight=editheight.getText().toString().trim();
//        if (Sheight.isEmpty())
//        {
//            editheight.setError("please Enter Weight");
//            return false;
//        }

//        else
//        {
            return true;
//        }
    }

    private boolean checkweight() {
        Sweight=editweight.getText().toString().trim();
//        if (Sweight.isEmpty())
//        {
//            editweight.setError("please enter weight");
//            return false;
//        }
//        else
//        {
            return true;
//        }
    }

    private boolean checkloc() {
        Sloc=editloc.getText().toString().trim();
        if (Sloc.isEmpty())
        {

            editloc.setError("please select location");
            editloc.requestFocus();
            return false;
        }
        else
        {
            cityname=Sloc;
            return true;
        }
    }

    private boolean checkdob() {
        Sdob=editdob.getText().toString().trim();
        if (Sdob.isEmpty())
        {
            editdob.setError("please select date of Birth");
            editdob.requestFocus();
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean checkemail() {
        Semail=editemail.getText().toString().trim();
        if (Semail.isEmpty())
        {
            editemail.setError("please enter Valid email");
            editemail.post(new Runnable() {
                @Override
                public void run() {
                    editemail.setFocusable(true);
                    editemail.setFocusableInTouchMode(true);
                    editemail.requestFocus();
                }
            });

            return false;
        }
        else {
            if (isValidEmail(Semail))
            {
                return true;
            }
            else {
                editemail.setError("please enter Valid email");
                editemail.post(new Runnable() {
                    @Override
                    public void run() {
                        editemail.setFocusable(true);
                        editemail.setFocusableInTouchMode(true);
                        editemail.requestFocus();
                    }
                });
                return false;
            }
        }
    }

    private boolean checkname() {
        Fname=editTextfirstname.getText().toString().trim();
        if (Fname.isEmpty())
        {
            editTextfirstname.setError("Please Enter first name");
            editTextfirstname.post(new Runnable() {
                @Override
                public void run() {
                    editTextfirstname.setFocusable(true);
                    editTextfirstname.setFocusableInTouchMode(true);
                    editTextfirstname.requestFocus();
                }
            });

            return false;
        }
        else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        if (others)
        {
            editethnicity.setFocusable(true);

        }

    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void initui() {
        userpic= (RoundedImageView) findViewById(R.id.user_profilepic);
        iam= (TextView) findViewById(R.id.iam);
        picedit= (ImageView) findViewById(R.id.imageEdit);
        Linearfemale= (LinearLayout) findViewById(R.id.layout_female);
        Linearmale= (LinearLayout) findViewById(R.id.layout_male);
        textfemale= (TextView) findViewById(R.id.textfemale);
        textmale= (TextView) findViewById(R.id.textMale);
        textilike= (TextView) findViewById(R.id.textilike);
        textloooking= (TextView) findViewById(R.id.textiamlookingfor);
        editemail= (EditText) findViewById(R.id.editTextemail);
        editpass= (EditText) findViewById(R.id.editTextpass);
        editdob= (EditText) findViewById(R.id.editTextDob);
        editloc= (EditText) findViewById(R.id.editTextlocation);
        rg= (RadioGroup) findViewById(radio);
        editheight= (EditText) findViewById(R.id.editTextHeight);
        editweight= (EditText) findViewById(R.id.editTextWeight);
        editethnicity= (EditText) findViewById(R.id.editTextehnic);
        editlookingfor= (EditText) findViewById(R.id.editTextiamLookingfor);
        spinethnic= (AppCompatSpinner) findViewById(R.id.spinnerEthnicity);
        reg_next= (ImageView) findViewById(R.id.register_next);
        editTextfirstname= (EditText) findViewById(R.id.editextfirstName);
        editTextlastname= (EditText) findViewById(R.id.editextlastname);
        checkBoxprivacy= (CheckBox) findViewById(R.id.checkboxPrivacy);
        textViewPrivacy= (TextView) findViewById(R.id.textprivacypolicy);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/brandon_grotesque_bold.ttf");
        Typeface typeface1=Typeface.createFromAsset(getAssets(),"fonts/BrandonGrotesque-Regular.ttf");
        textilike.setTypeface(typeface1);
        textloooking.setTypeface(typeface1);
        editTextfirstname.setTypeface(typeface1);
        editTextlastname.setTypeface(typeface1);
        editemail.setTypeface(typeface1);
        editpass.setTypeface(typeface1);
        editdob.setTypeface(typeface1);
        editloc.setTypeface(typeface1);
        editheight.setTypeface(typeface1);
        editweight.setTypeface(typeface1);
        editlookingfor.setTypeface(typeface1);
        iam.setTypeface(typeface1);
        editethnicity.setTypeface(typeface1);
        likemale= (LinearLayout) findViewById(R.id.layout_like_male);
        likefemale= (LinearLayout) findViewById(R.id.layout_like_female);
        likeboth= (LinearLayout) findViewById(R.id.layout_like_both);
        textlikemale= (TextView) findViewById(R.id.text_like_Male);
        textlikefemale= (TextView) findViewById(R.id.text_like_female);
        textlikeboth= (TextView) findViewById(R.id.text_like_both);
        textheadlike= (TextView) findViewById(R.id.ilike);
        textlikeboth.setTypeface(typeface);
        textlikefemale.setTypeface(typeface);
        textlikemale.setTypeface(typeface);
        textheadlike.setTypeface(typeface1);
        imageViewSpinner= (ImageView) findViewById(R.id.imageSpinner);
        editTextUsername= (EditText) findViewById(R.id.editTextusername);
        editTextUsername.setTypeface(typeface1);

    }
    private void submitreg() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("user_email",Semail);
        params.put("full_name",Fname);
        params.put("username",SuserName);

        params.put("dob",Sdob);
        if (!fbid.isEmpty())
        {
            params.put("fb_id", fbid);
        }
        else {
            params.put("user_password",Spassword);
        }
        params.put("location",cityname);
//        params.put("latitude",Lat);
//        params.put("longitude",LONG);
        params.put("gender",gender);
        params.put("height",Sheight);
        params.put("preference",iLike);
        if (EthnicityId.equals("0"))
        {
            params.put("ethnicity_id",EthnicityId);
            params.put("other_ethinicity",EthnicityTitle);
        }
        else {
            params.put("ethnicity_id",EthnicityId);
        }

        params.put("weight",Sweight);

//        params.put("looking_for",Slookingfor);
//        params.put("preference",iLike);
        APIService service = ServiceGenerator.createService(APIService.class, RegistrationActivity.this);
        Call<ModelRegister>call=service.register(params);
        call.enqueue(new Callback<ModelRegister>() {
            @Override
            public void onResponse(Call<ModelRegister> call, Response<ModelRegister> response) {
                if (response.isSuccessful())
                { if (pd.isShowing()) { pd.dismiss(); }
                    if (response.body()!=null&&response.body().getResult()!=null)
                    {
                        ModelRegister register=response.body();
                        String msg=register.getResult().getMessage();
                        String id=register.getResult().getUserId();
                        int value=register.getResult().getValue();
                        if (value==1)
                        {
                            showToast(msg);
                            savePref(Constants.USER_ID,id);
                            userID=id;
                            savePref(Constants.GENDER,gender);
                            if (fbid!=null) {
                                savePref(Constants.EMAIL_REGISTRATION, true);
                                savePref(Constants.FBREGISTRATION,false);
                            }
                            else {
                                savePref(Constants.EMAIL_REGISTRATION, false);
                                savePref(Constants.FBREGISTRATION,true);
                            }
                            if (f.length()!=0) {
                                UploadImageTask(f);
                            }
                            Intent intent=new Intent(RegistrationActivity.this,Registration_2_Activity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            showToast("msg");
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ModelRegister> call, Throwable t) {
                showLog(t.toString(),2);
                if (pd.isShowing()) { pd.dismiss(); }
            }
        });
    }
    void pickPhotoDialog() {
        String[] item = { "Take Picture", "Upload From Library" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this, R.style.AppCompatAlertDialogStyle);

        builder.setTitle("UPLOAD IMAGE").setItems(item,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                takePicture();
                                break;
                            case 1:

                                openGallery();


                                break;
                            default:
                                break;
                        }
                    }


                }).setNegativeButton("CANCEL",null);
        builder.show();
    }
    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            }
            else {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

//            Log.d(TAG, "cannot take picture", e);
        }
    }
    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }
    private void startCropImage() {

        Intent intent = new Intent(RegistrationActivity.this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 3);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    try {

                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();

                        startCropImage();

                    } catch (Exception e) {

                    }
                }
                break;
            case REQUEST_CODE_TAKE_PICTURE:

                if (resultCode == Activity.RESULT_OK) {
                    startCropImage();
                }
                break;
            // ACTION_TAKE_PHOTO_S
            case REQUEST_CODE_CROP_IMAGE :
                if (resultCode == Activity.RESULT_OK) {

                    String path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (path == null) {

                        return;
                    }

                    final Bitmap photo = BitmapFactory.decodeFile(mFileTemp.getPath());
                    try {
                        bimapTofile(photo);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    picedit.post(new Runnable() {

                        @Override
                        public void run() {
                            try {

                                userpic.setImageBitmap(photo);

//                                img_cover_pic.setImageBitmap(photo);
                                AlphaAnimation alpha = new AlphaAnimation(0.3F, 0.3F);
                                alpha.setDuration(0); // Make animation instant
                                alpha.setFillAfter(true); // Tell it to persist after the animation ends
//                                img_cover_pic.startAnimation(alpha);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
            case PLACE_PICKER_REQUEST:
                if(resultCode==Activity.RESULT_OK)
                {
                    Place place = PlacePicker.getPlace(data, this);
                    StringBuilder builder=new StringBuilder();
//               String Name= String.valueOf(place.getName());
//               builder.append(Name);
                    String Adress=String.valueOf(place.getAddress());
                    String str[]=Adress.split(",");
                    int j=str.length;
                    if(j>4)
                    {
                        String addr=str[1];
                        String s2=str[2];
                        builder.append(addr);
                        builder.append(s2);
                    }
                    if(j==4)
                    {
                        String addr=str[1];
                        builder.append(addr);
                    }
                    else if(j<=3)
                    {
                        String addr=str[0];
                        builder.append(addr);
                    }
//                builder.append(" ")



                    String toastMsg=builder.toString();
                    cityname=toastMsg;
                    editloc.setText(toastMsg);

                    temp=  place.getLatLng();
                    Lat=String.valueOf(temp.latitude);
                    LONG=String.valueOf(temp.longitude);
//                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }
                else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;

        }
    }
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
    public void bimapTofile(Bitmap bitmap) throws IOException{

        //create a file to write bitmap data
         f =new  File(Environment.getExternalStorageDirectory()
                + File.separator + Constants.TEMP_PHOTO_FILE);
        f.createNewFile();

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();


//		String encodedimage = encodeImage(bitmapdata);
        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);

        fos.flush();
        fos.close();



    }

    private void UploadImageTask(File f) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        APIService service = new Retrofit.Builder().baseUrl("http://preview.proyectoweb.com/stylerapp/").client(client).build().create(APIService.class);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", f.getName(), reqFile);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"),userID );
        Call<ResponseBody>req=service.postImage(body,userid);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    try{
                        String image=response.body().toString();
//                        showToast(image);
                    }catch (Exception e){}

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void Getlocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//      Criteria criteria = new Criteria();
//       provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION);

            return;
        }
        Location location = getLastKnownLocation();


        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            showSnackBar("location not available please turn on gps", false);
        }
    }
    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String bprovider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);


            }
            Location l = locationManager.getLastKnownLocation(bprovider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
                provider=bprovider;

            }
        }
        return bestLocation;
    }
    private void Initializepicker() {
        showProgress();
        LatLng latng=new LatLng(lat,lng );
        toBounds(latng,5000);



    }
    public LatLngBounds toBounds(LatLng center, double radius) {
        southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        hideProgress();
        return new LatLngBounds(southwest, northeast);
    }
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }


}
