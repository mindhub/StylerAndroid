package com.mindbees.stylerapp.UI.TabFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Adapter.SpinnerAdapter;
import com.mindbees.stylerapp.UI.Adapter.SpinnerwithimageAdapter;
import com.mindbees.stylerapp.UI.Base.BaseFragment;
import com.mindbees.stylerapp.UI.Landing.Registration_2_Activity;
import com.mindbees.stylerapp.UI.Models.SpinnerModel;
import com.mindbees.stylerapp.UI.Models.Spinnermodelwithimage;
import com.mindbees.stylerapp.UI.Models.Tribes.Female;
import com.mindbees.stylerapp.UI.Models.Tribes.Male;
import com.mindbees.stylerapp.UI.Models.Tribes.ModelTribes;
import com.mindbees.stylerapp.UI.Models.Tribes.Result;
import com.mindbees.stylerapp.UTILS.Constants;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 03-04-2017.
 */

public class SearchFragment extends BaseFragment {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    TextView tribeText,profile_text,location_text,SearchageText,fromText,toText,brandText;
    EditText edittribe,editprofile,editlocation,editfrom,editto,editbrand;
    AppCompatSpinner spinnertribe,spinnerfrom,spinnerto;
    public List<Spinnermodelwithimage> listtribes;
    public List<SpinnerModel>fromlist;
    public List<SpinnerModel>Tolist;
    List<Male>list1;
    List<Female>list2;
    List<Result>list;
    String tribe_id;
    ImageView cancelpofile,cancellocation,cancelbrand;
    Handler handler;
    String Agefrom="",Ageto="",Profilename="",LAT="",LONG="",Brandname="";
    RelativeLayout relativeLayout;
    ModelTribes tribes;
    SpinnerAdapter adapter2,adapter3;
    SpinnerwithimageAdapter adapter1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.search_fragment_layout,null);
        setupUI(v.findViewById(R.id.searchlayout));
        initUi(v);
        setupui();
        listtribes=new ArrayList<Spinnermodelwithimage>();
        fromlist=new ArrayList<SpinnerModel>();
        Tolist=new ArrayList<SpinnerModel>();
        SpinnerModel model1=new SpinnerModel();
        model1.setName("Select");
        fromlist.add(model1);
        for (int i=18;i<=90;i++)
        {
            SpinnerModel model=new SpinnerModel();
            model.setName(String.valueOf(i));
            fromlist.add(model);
        }
        SpinnerModel model2=new SpinnerModel();
        model2.setName("Select");
                Tolist.add(model2);
        for (int i=18;i<=90;i++)
        {
            SpinnerModel model=new SpinnerModel();
            model.setName(String.valueOf(i));
            Tolist.add(model);
        }
        adapter1=new SpinnerwithimageAdapter(getContext(),listtribes);
        spinnertribe.setAdapter(adapter1);
        adapter2=new SpinnerAdapter(getContext(),fromlist);
        spinnerfrom.setAdapter(adapter2);
        adapter3=new SpinnerAdapter(getContext(),Tolist);
        spinnerto.setAdapter(adapter3);
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                gettribes();
           }
        },2000);
////        gettribes();






        return v;
    }

    private void gettribes() {
        showProgress();
        HashMap<String, String> params = new HashMap<>();
        final String gender=getPref(Constants.GENDER);

        params.put("gender","mf");
        APIService service = ServiceGenerator.createService(APIService.class, getContext());
        Call<ModelTribes>call=service.gettribes(params);
        call.enqueue(new Callback<ModelTribes>() {
            @Override
            public void onResponse(Call<ModelTribes> call, Response<ModelTribes> response) {
                if (response.isSuccessful())
                {
                    hideProgress();
                    if (response.body()!=null&&response.body().getResult()!=null)
                    {
                         tribes=response.body();
//                        list=tribes.getResult();
                        Spinnermodelwithimage model1=new Spinnermodelwithimage();
                        model1.setName("SELECT TRIBE");
                        model1.setImagepath("");
                        listtribes.add(model1);
                        if (gender.equals("m")) {
                            list1=tribes.getResult().getMale();
                            for (int i = 0; i < list1.size(); i++) {
                                Spinnermodelwithimage model = new Spinnermodelwithimage();
                                model.setImagepath(list1.get(i).getTribeThumb());
                                model.setName(list1.get(i).getTribeTitle());
                                listtribes.add(model);
                            }
                        }
                        if (gender.equals("f"))
                        {
                            list2=tribes.getResult().getFemale();
                            for (int i = 0; i < list2.size(); i++) {
                                Spinnermodelwithimage model = new Spinnermodelwithimage();
                                model.setImagepath(list2.get(i).getTribeThumb());
                                model.setName(list2.get(i).getTribeTitle());
                                listtribes.add(model);
                            }
                        }
                        adapter1.notifyDataSetChanged();


                    }

                }
            }

            @Override
            public void onFailure(Call<ModelTribes> call, Throwable t) {
                hideProgress();

                showToast("Network error",false);
            }
        });
    }

    private void setupui() {
        edittribe.setFocusable(false);
        editprofile.setFocusable(false);
        editbrand.setFocusable(false);
        editbrand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                editbrand.setFocusableInTouchMode(true);
                return false;
            }
        });
        editprofile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editprofile.setFocusableInTouchMode(true);
                return false;
            }
        });
        editto.setFocusable(false);
        editfrom.setFocusable(false);
        editlocation.setFocusable(false);
        edittribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnertribe.setAdapter(null);
                listtribes.clear();
                Spinnermodelwithimage model3=new Spinnermodelwithimage();
                model3.setName("SELECT TRIBE");
                model3.setImagepath("");
                listtribes.add(model3);
                String gender=getPref(Constants.GENDER);
                if (gender.equals("m")) {
                    for (int i = 0; i < list1.size(); i++) {
                        Spinnermodelwithimage model = new Spinnermodelwithimage();
                        model.setName(list1.get(i).getTribeTitle());
                        model.setImagepath(list1.get(i).getTribeThumb());
                        listtribes.add(model);
                    }
                }
                if (gender.equals("f"))
                {
                    for (int i = 0; i < list2.size(); i++) {
                        Spinnermodelwithimage model = new Spinnermodelwithimage();
                        model.setName(list2.get(i).getTribeTitle());
                        model.setImagepath(list2.get(i).getTribeThumb());
                        listtribes.add(model);
                    }
                }
                adapter1=new SpinnerwithimageAdapter(getContext(),listtribes);
                spinnertribe.setAdapter(adapter1);

                spinnertribe.setVisibility(View.VISIBLE);
                spinnertribe.performClick();
            }
        });
        spinnertribe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0) {
                    String gender=getPref(Constants.GENDER);
                    if (gender.equals("m")) {
                        edittribe.setText(listtribes.get(position).getName());
                        tribe_id = list1.get(position - 1).getTribeId();
                        spinnertribe.setVisibility(View.INVISIBLE);
                    }
                    if (gender.equals("f"))
                    {
                        edittribe.setText(listtribes.get(position).getName());
                        tribe_id = list2.get(position - 1).getTribeId();
                        spinnertribe.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    spinnertribe.setVisibility(View.INVISIBLE);
                    edittribe.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        editfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerfrom.setAdapter(null);
                fromlist.clear();
                SpinnerModel model1=new SpinnerModel();
                model1.setName("Select");
                fromlist.add(model1);
                for (int i=18;i<=90;i++)
                {
                    SpinnerModel model=new SpinnerModel();
                    model.setName(String.valueOf(i));
                    fromlist.add(model);
                }
                adapter2=new SpinnerAdapter(getContext(),fromlist);
                spinnerfrom.setAdapter(adapter2);
                spinnerfrom.setVisibility(View.VISIBLE);
                spinnerfrom.performClick();
            }
        });
        editto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerto.setAdapter(null);
                Tolist.clear();
                SpinnerModel model2=new SpinnerModel();
                model2.setName("Select");
                Tolist.add(model2);
                for (int i=18;i<=90;i++)
                {
                    SpinnerModel model=new SpinnerModel();
                    model.setName(String.valueOf(i));
                    Tolist.add(model);
                }
                adapter3=new SpinnerAdapter(getContext(),Tolist);
                spinnerto.setAdapter(adapter3);
                spinnerto.setVisibility(View.VISIBLE);
                spinnerto.performClick();
            }
        });
        spinnerfrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0) {
                    int h;
                    editfrom.setText(fromlist.get(position).getName());
                    spinnerfrom.setVisibility(View.INVISIBLE);
//                    String gg=fromlist.get(position).getName();
//                    if (gg.equals("Select"))
//                    {
//                      h=Integer.valueOf(fromlist.get(position+1).getName())  ;
//                    }
//                    else {
//                    h =Integer.valueOf(fromlist.get(position).getName());
//                    }
//                    Tolist.clear();
//                    SpinnerModel model2=new SpinnerModel();
//                    model2.setName("Select");
//                    Tolist.add(model2);
//                    for (int i = (h+1); i <= 90; i++) {
//                        SpinnerModel model = new SpinnerModel();
//                        model.setName(String.valueOf(i));
//                        Tolist.add(model);
//                    }
//                    adapter3.notifyDataSetChanged();
                }
                else
                {
                    editfrom.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0) {
                    int h;
                    editto.setText(Tolist.get(position).getName());
                    spinnerto.setVisibility(View.INVISIBLE);
                    String gg=editfrom.getText().toString().trim();
                    if (!gg.isEmpty())
                    {
                        h=Integer.valueOf(editfrom.getText().toString().trim());
                        if (h>=(Integer.valueOf(editto.getText().toString().trim())))
                        {
                            showToast("To age should be greater than From age",false);
                            editto.setText("");
                        }
                    }




//                    String gg=Tolist.get(position).getName();
//                    if (gg.equals("Select"))
//                    {
//                      h  = Integer.valueOf(Tolist.get(position+1).getName());
//                    }
//                    else
//                    {
//                        h  = Integer.valueOf(Tolist.get(position).getName());
//                    }
//
//                    fromlist.clear();
//                    SpinnerModel model1=new SpinnerModel();
//                    model1.setName("Select");
//                    fromlist.add(model1);
//                    for (int i = 18; i < h; i++) {
//                        SpinnerModel model = new SpinnerModel();
//                        model.setName(String.valueOf(i));
//                        fromlist.add(model);
//                    }
//                    adapter2.notifyDataSetChanged();
                }
                else
                {
                    editto.setText("");
            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checktribe()&&checkprofilename()&&checklocation()&&checkAge()&&checkbrand()) {
                    Intent intent = new Intent(getActivity(), SearchListActivity.class);
                    intent.putExtra("TRIBE_ID",tribe_id);
                    intent.putExtra("PROFILE_NAME",Profilename);
                    intent.putExtra("LAT",LAT);
                    intent.putExtra("LONG",LONG);
                    intent.putExtra("AGE_FROM",Agefrom);
                    intent.putExtra("AGE_TO",Ageto);
                    intent.putExtra("BRAND",Brandname);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,2);
                    getActivity().overridePendingTransition(0, 0);
                }
            }
        });

    }

    private boolean checkAge() {
        String temp=editfrom.getText().toString().trim();
        if (temp.equals("0"))
        {
            Agefrom="";
        }
        else
        {
            Agefrom=temp;
        }
        String temp1=editto.getText().toString().trim();
        if (temp1.equals("0"))
        {
            Ageto="";

        }
        else
        {
            Ageto=temp1;
        }
        if (!Agefrom.isEmpty()&&Ageto.isEmpty())
        {
            Ageto="90";
        }
        if (Agefrom.isEmpty()&&(!Ageto.isEmpty()))
        {
            Agefrom="18";
        }
        return true;
    }

    private boolean checklocation() {
        String temp=editlocation.getText().toString().trim();
        if (temp.isEmpty())
        {
            LONG="";
            LAT="";
        }
        return true;
    }

    private boolean checkprofilename() {
        String temp=editprofile.getText().toString().trim();
        if (temp.isEmpty())
        {
            Profilename="";
        }
        else
        {
            Profilename=temp;
        }
        return true;
    }

    private boolean checktribe() {
        String temp=edittribe.getText().toString().trim();
        if (temp.isEmpty())
        {
            tribe_id="";
        }
        return true;
    }

    private boolean checkbrand() {
        String temp=editbrand.getText().toString().trim();
        if (temp.isEmpty())
        {
            Brandname="";
        }
        else
        {
            Brandname=temp;
        }
        return true;
    }

    private void initUi(View v) {
        tribeText= (TextView) v.findViewById(R.id.searchTextStylertribe);
        profile_text= (TextView) v.findViewById(R.id.searchTextProfileName);
        location_text= (TextView) v.findViewById(R.id.searchTextlocation);
        fromText= (TextView) v.findViewById(R.id.searchtextfrom);
        toText= (TextView) v.findViewById(R.id.searchtextto);
        cancelbrand= (ImageView) v.findViewById(R.id.cancelbrandimage);
        cancelbrand.setVisibility(View.GONE);
        cancellocation= (ImageView) v.findViewById(R.id.cancellocationimage);
        cancelpofile= (ImageView) v.findViewById(R.id.cancelprofileimage);
        cancelpofile.setVisibility(View.GONE);
        brandText= (TextView) v.findViewById(R.id.searchTextbrand);
        SearchageText= (TextView) v.findViewById(R.id.searchTextAGE);
        edittribe= (EditText) v.findViewById(R.id.editTextSearchtribe);
        editprofile= (EditText) v.findViewById(R.id.editTextSearchprofileName);
        editlocation= (EditText) v.findViewById(R.id.editTextSearchlocation);
        editfrom= (EditText) v.findViewById(R.id.editTextFrom);
        editto= (EditText) v.findViewById(R.id.editTextTo);
        editbrand= (EditText) v.findViewById(R.id.editTextSearchbrand);
        spinnertribe= (AppCompatSpinner) v.findViewById(R.id.spinnersearchStylerTribe);
        spinnerfrom= (AppCompatSpinner) v.findViewById(R.id.spinnerSearchFrom);
        spinnerto= (AppCompatSpinner) v.findViewById(R.id.spinnerSearchTo);
        relativeLayout= (RelativeLayout) v.findViewById(R.id.layout6);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        cancelbrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editbrand.setText("");
            }
        });
        cancelpofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editprofile.setText("");
            }
        });
        cancellocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editlocation.setText("");
                LONG="";
                LAT="";
            }
        });
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinnerfrom);
            int h=height/2;

            // Set popupWindow height to 500px
            popupWindow.setHeight(h-10);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinnerto);
            int h=height/2;

            // Set popupWindow height to 500px
            popupWindow.setHeight(h-10);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                editlocation.setText(place.getName());
                LatLng m=place.getLatLng();
                LONG=String.valueOf(m.longitude);
                LAT=String.valueOf(m.latitude);
//                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
//                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode==2)
        {
            if (resultCode==Activity.RESULT_OK)
            {
                spinnertribe.setAdapter(null);
               listtribes.clear();
                Spinnermodelwithimage model3=new Spinnermodelwithimage();
                model3.setName("SELECT TRIBE");
                model3.setImagepath("");
                listtribes.add(model3);
                String gender=getPref(Constants.GENDER);
                if (gender.equals("m")) {
                    for (int i=0;i<list1.size();i++)
                    {
                        Spinnermodelwithimage model=new Spinnermodelwithimage();
                        model.setName(list1.get(i).getTribeTitle());
                        model.setImagepath(list1.get(i).getTribeThumb());
                        listtribes.add(model);
                    }
                }
                if (gender.equals("f"))
                {
                    for (int i=0;i<list2.size();i++)
                    {
                        Spinnermodelwithimage model=new Spinnermodelwithimage();
                        model.setName(list2.get(i).getTribeTitle());
                        model.setImagepath(list2.get(i).getTribeThumb());
                        listtribes.add(model);
                    }
                }





                fromlist.clear();
                Tolist.clear();
                SpinnerModel model1=new SpinnerModel();
                model1.setName("SELECT");

                fromlist.add(model1);
                for (int i=18;i<=90;i++)
                {
                    SpinnerModel model=new SpinnerModel();
                    model.setName(String.valueOf(i));
                    fromlist.add(model);
                }
                SpinnerModel model2=new SpinnerModel();
                model2.setName("SELECT");
                Tolist.add(model2);
                for (int i=18;i<=90;i++)
                {
                    SpinnerModel model=new SpinnerModel();
                    model.setName(String.valueOf(i));
                    Tolist.add(model);
                }
                adapter2=new SpinnerAdapter(getContext(),fromlist);
                spinnerfrom.setAdapter(adapter2);
                adapter3=new SpinnerAdapter(getContext(),Tolist);
                spinnerto.setAdapter(adapter3);
                adapter1=new SpinnerwithimageAdapter(getContext(),listtribes);
                spinnertribe.setAdapter(adapter1);
//                editto.setText("");
//                editfrom.setText("");
//                edittribe.setText("");
//                editlocation.setText("");
                LAT="";
                LONG="";
                editbrand.setText("");
                editprofile.setText("");
                editlocation.setText("");

            }
        }
    }
}
