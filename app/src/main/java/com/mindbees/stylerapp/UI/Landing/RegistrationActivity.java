package com.mindbees.stylerapp.UI.Landing;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
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
import com.mindbees.stylerapp.UI.Models.SpinnerModel;
import com.mindbees.stylerapp.UTILS.Constants;
import com.mindbees.stylerapp.UTILS.InternalStorageContentProvider;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;
import com.mindbees.stylerapp.UTILS.RoundedImageView;
import com.mindbees.stylerapp.UTILS.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import eu.janmuller.android.simplecropimage.CropImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mindbees.stylerapp.R.id.editTextDob;
import static com.mindbees.stylerapp.R.id.radio;
import static com.mindbees.stylerapp.R.id.spinnerEthnicity;


/**
 * Created by User on 01-03-2017.
 */

public class RegistrationActivity extends BaseActivity implements LocationListener{
    private static final int ACCESS_FINE_LOCATION = 0;
    private LocationManager locationManager;
    private String provider;
    RoundedImageView userpic;
    LatLng southwest,northeast;
    String cityname;
    String Lat;
    String LONG;
    LatLng temp;
    LatLngBounds BOUNDS_MOUNTAIN_VIEW;
    public static final int PLACE_PICKER_REQUEST = 00;
    ImageView picedit,reg_next;
    private File mFileTemp;
    LinearLayout Linearmale,Linearfemale;
    TextView textmale,textfemale,textilike,textloooking;
    EditText editemail,editpass,editdob,editloc,editheight,editweight,editethnicity,editlookingfor;
    AppCompatSpinner spinethnic;
    RadioGroup rg;
    double lat,lng;
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
        setupUI(findViewById(R.id.registrationlayout));
        listitems=new ArrayList<SpinnerModel>();
        initui();
        setupUi();
        Getlocation();
        Initializepicker();
        if (isNetworkAvailable())
        {
            getEthnicity();

        }
        else {
            showToast("NETWORK NOT AVAILABLE");
        }
        Resources res = getResources();

//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(RegistrationActivity.this,R.layout.spinner_item,interval);
//        adapter.setDropDownViewResource(R.layout.spinner_item);
//        spinethnic.setAdapter(adapter);
        BOUNDS_MOUNTAIN_VIEW=new LatLngBounds(southwest,northeast);

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
        editethnicity.setFocusable(false);
        editemail.setFocusable(false);
        editpass.setFocusable(false);
        editloc.setFocusable(false);
        editheight.setFocusable(false);
        editweight.setFocusable(false);
        editemail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editemail.setFocusableInTouchMode(true);
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

            }
        });
        Linearfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linearfemale.setBackgroundColor(getResources().getColor(R.color.black));
                textfemale.setTextColor(getResources().getColor(R.color.white));
                Linearmale.setBackgroundColor(getResources().getColor(R.color.white));
                textmale.setTextColor(getResources().getColor(R.color.black));
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
                int Month=calender.get(Calendar.MONTH);
                int Date=calender.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datepicker=new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                       editdob.setText(dayOfMonth+"/"+(month+1)+"/"+year);



                    }
                },Year,Month,Date);
                datepicker.setTitle("Select Date");
                datepicker.show();
            }
        });
        editloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);

                    Intent intent = intentBuilder.build(( getApplicationContext()));
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

//        spinethnic.performClick();
//        editethnicity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                spinethnic.setVisibility(View.VISIBLE);
//
//
//            }
//        });
        spinethnic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0) {
                    String name = list.get(position).getEthnicityTitle();
//                    editethnicity.setText(name);
//                    spinethnic.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        reg_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegistrationActivity.this,Registration_2_Activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
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
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
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
        File f =new  File(Environment.getExternalStorageDirectory()
                + File.separator + "STYLER"+File.separator+"Images"+File.separator+"Profile"+File.separator+Constants.TEMP_PHOTO_FILE);
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

//        UploadImageTask(f);
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
