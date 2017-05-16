package com.mindbees.stylerapp.UI.HOME;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Adapter.GridRecycleAdapter;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.Models.Tribes.Female;
import com.mindbees.stylerapp.UI.Models.Tribes.Male;
import com.mindbees.stylerapp.UI.POPUPS.PopUpAddTribe;
import com.mindbees.stylerapp.UI.POPUPS.PopupdeleteTribe;
import com.mindbees.stylerapp.UI.Models.ImagegridModel;
import com.mindbees.stylerapp.UI.Models.Profile.ModelProfile;
import com.mindbees.stylerapp.UI.Models.Profile.OtherTribe;
import com.mindbees.stylerapp.UI.Models.Profile.Tribe;
import com.mindbees.stylerapp.UI.Models.Tribes.ModelTribes;
import com.mindbees.stylerapp.UI.Models.Tribes.Result;
import com.mindbees.stylerapp.UI.Models.update_profile.ModelUpdateProfile;
import com.mindbees.stylerapp.UTILS.Constants;
import com.mindbees.stylerapp.UTILS.ItemClickSupport;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 16-04-2017.
 */

public class EditTribesActivity extends BaseActivity {
    boolean loaded=false;
    int k;
    //    AppCompatSpinner spin;
    MaterialDialog builder;
    private AlertDialog progressDialog;
    StringBuilder idadder;
    String OTHERTRIBENAME="";
    LinearLayout othertextlayout;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    //    public List<SpinnerModel> listitems;
    private RecyclerView recyclerView;
    List<Result> list;
    List<Male>list1;
    List<Female>list2;
    RelativeLayout other;
    ImageView Register_next;
    FrameLayout enlarged;
    RelativeLayout tribelayout;
    ImageView closeButton;
    Button selectedButton;
    TextView selected_text_name;
    GridRecycleAdapter adapter;
    ImageView image_enlarged;
    public ArrayList<ImagegridModel> temp;
    public ArrayList<ImagegridModel>gridItems;
    TextView othertext;
    LinearLayout savenow;
    StringBuilder otherbuild;
    //    ImageView userpic,reg_next;
//    SpinnerAdapter adapter;
    TextView style;
    //    EditText tribes;
    RecyclerView.LayoutManager mLayoutManager;
    ScrollView scrollview;
    List<Tribe>tribes;
    List<OtherTribe>othertribes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittribeslayout);
        initUi();
        otherbuild=new StringBuilder();
        idadder=new StringBuilder();
        initRecyclerview();
        gridItems=new ArrayList<>();
        setAdapter();
        tribes=new ArrayList<>();
//        progressDialog = new SpotsDialog(this, R.style.Custom);
//        listitems=new ArrayList<SpinnerModel>();
        if (isNetworkAvailable())
        {
            getTribes();
        }
        else {
            showToast("Please check network connectivity",false);
        }
        setupUI();
    }

    private void setAdapter() {
        adapter=new GridRecycleAdapter(this,gridItems);
        recyclerView.setAdapter(adapter);

    }

    private void initRecyclerview() {
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);

    }



    private  void getwebservice()
    {
        showProgress();
        HashMap<String, String> params = new HashMap<>();
        String userid=getPref(Constants.USER_ID);
        params.put("user_id",userid);
        APIService service=ServiceGenerator.createService(APIService.class,EditTribesActivity.this);
        Call<ModelProfile>call=service.getprofile(params);
        call.enqueue(new Callback<ModelProfile>() {
            @Override
            public void onResponse(Call<ModelProfile> call, Response<ModelProfile> response) {
                if (response.isSuccessful())
                {
                    hideProgress();
                    if (response.body()!=null)
                    {
                        ModelProfile profile=response.body();
                        tribes=profile.getUser().get(0).getTribes();
                        for (int i=0;i<tribes.size();i++)
                        {
                            String tribetitle=tribes.get(i).getTribeTitle();
                            for (int j=0;j<gridItems.size();j++)
                            {
                                if (gridItems.get(j).getTribeName().equals(tribetitle))
                                {
                                    gridItems.get(j).setSelected(true);
                                }
                            }


                        }
                        othertribes=profile.getUser().get(0).getOtherTribes();
                        if (othertribes!=null)
                        {
                            for (int k=0;k<othertribes.size();k++)
                            {
                                ImagegridModel model = new ImagegridModel();
                                model.setSelected(false);
                                model.setTribeName(othertribes.get(k).getTribeTitle());
                                model.setTribeImage("");
                                gridItems.add(model);
                                recyclerView.smoothScrollToPosition(gridItems.size()-1);
                                adapter.notifyDataSetChanged();
                            }

                        }
                        adapter.notifyDataSetChanged();


                    }
                }
            }

            @Override
            public void onFailure(Call<ModelProfile> call, Throwable t) {
                hideProgress();
            }

        });

    }
    private void getTribes() {
//        final ProgressDialog pd = new ProgressDialog(this);
//        pd.setMessage("Loading");
//        pd.setCancelable(false);
//        pd.show();
//        progressDialog.show();
//        builder= new MaterialDialog.Builder(this)
//                .backgroundColor(Color.TRANSPARENT)
//                .backgroundColorRes(R.color.thistle)
//                .content(R.string.please_wait)
//                .progress(true, 0)
//                .show();
//showProgress();
        HashMap<String, String> params = new HashMap<>();
        final String gender=getPref(Constants.GENDER);
        params.put("gender","mf");
        APIService service = ServiceGenerator.createService(APIService.class, EditTribesActivity.this);
        Call<ModelTribes> call=service.gettribes(params);
        call.enqueue(new Callback<ModelTribes>() {
            @Override
            public void onResponse(Call<ModelTribes> call, Response<ModelTribes> response) {
                if (response.isSuccessful())
                {
//                    hideProgress();
//                    builder.dismiss();
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            hideProgress();
//
//                        }
//
//
//                    }, 1000);

//                    if (pd.isShowing()) { pd.dismiss(); }
//                    progressDialog.dismiss();
                    if (response.body()!=null&&response.body().getResult()!=null)
                    {
                        ModelTribes modeltribes=response.body();
                        if (gender.equals("m")) {
                            list1 = modeltribes.getResult().getMale();
                            gridItems.clear();
                            for (int i = 0; i < list1.size(); i++) {
                                ImagegridModel model = new ImagegridModel();
                                model.setSelected(false);
                                model.setTribeImage(list1.get(i).getTribeImg());
                                model.setTribeName(list1.get(i).getTribeTitle());
                                gridItems.add(model);

                            }
                        }
                        if (gender.equals("f"))
                        {
                            list2 = modeltribes.getResult().getFemale();
                            gridItems.clear();
                            for (int i = 0; i < list2.size(); i++) {
                                ImagegridModel model = new ImagegridModel();
                                model.setSelected(false);
                                model.setTribeImage(list2.get(i).getTribeImg());
                                model.setTribeName(list2.get(i).getTribeTitle());
                                gridItems.add(model);

                            }
                        }
                        getwebservice();
//                        adapter.notifyDataSetChanged();


//                        SpinnerModel model=new SpinnerModel();
//                        model.setName("Mens Tribe");
//                        listitems.add(model);
//                        for (int i=0;i<list.size();i++)
//                        {
//                            SpinnerModel model2=new SpinnerModel();
//                            model2.setName(list.get(i).getTribeTitle());
//                            listitems.add(model2);
//                        }
//                        adapter=new SpinnerAdapter(Registration_2_Activity.this,listitems);
//                        spin.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelTribes> call, Throwable t) {
//               progressDialog.dismiss();
//                builder.dismiss();
                //  hideProgress();
                showToast("Network Error",false);

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==1)
        {
            if (resultCode== Activity.RESULT_OK)
            {
                try
                {
                    String myValue = data.getStringExtra("TribeName");
                    int position=data.getIntExtra("position",0);
                    boolean edit=data.getBooleanExtra("edit",false);
                    if (edit)
                    {
                        gridItems.get(position).setTribeName(myValue);
                        adapter.notifyDataSetChanged();
                    }
                    else {
//                    showToast(myValue);
//                        OTHERTRIBENAME = myValue;
//                        otherbuild.append(OTHERTRIBENAME);
//                        otherbuild.append(",");
//                        idadder.append("0");
//                        idadder.append(",");

                        ImagegridModel model = new ImagegridModel();
                        model.setSelected(false);
                        model.setTribeName(myValue);
                        model.setTribeImage("");
                        gridItems.add(model);
                        recyclerView.smoothScrollToPosition(gridItems.size()-1);
                        adapter.notifyDataSetChanged();
//                        recyclerView.scrollToPosition(gridItems.size()-1);
//                        scrollview.fullScroll(View.FOCUS_DOWN);
//                    othertextlayout.setVisibility(View.VISIBLE);
                    }
//                    othertext.setText(myValue);



                }catch (Exception e)
                {

                }
            }
        }
        if (requestCode==2)
        {
            if (resultCode==Activity.RESULT_OK)
            {
                try {
                    int position=data.getIntExtra("position",0);
                    boolean edit=data.getBooleanExtra("edit",false);
                    boolean delete=data.getBooleanExtra("delete",false);
                    if (delete)
                    {
                        gridItems.remove(position);
                        recyclerView.setAdapter(null);
                        adapter=new GridRecycleAdapter(this,gridItems);
                        recyclerView.setAdapter(adapter);


                    }
                    if (edit)
                    {
                        String name=gridItems.get(position).getTribeName();
                        Intent intent= new Intent(EditTribesActivity.this,PopUpAddTribe.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("position",position);
                        intent.putExtra("name",name);
                        startActivityForResult(intent,1);
                        overridePendingTransition(0,0);
                    }

                }catch (Exception e)
                {

                }
            }
        }
    }

    private void setupUI() {
       /* tribes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin.setVisibility(View.VISIBLE);
                spin.performClick();
            }
        });


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long fbid) {
                tribes.setText(list.get(position).getTribeTitle());

                    String url=list.get(position).getTribeImg();
                    showToast(url);
                    Picasso.with(Registration_2_Activity.this).load(url+list.get(position).getTribeId()).into(userpic);
                spin.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        savenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               Intent intent=new Intent(Registration_2_Activity.this,Registratiom_3_activity.class);
//               startActivity(intent)
                otherbuild.append(OTHERTRIBENAME);
                otherbuild.append(",");
                idadder.append("0");
                idadder.append(",");
                StringBuilder builder=new StringBuilder();
                builder.append("");
                for (int i=0;i<gridItems.size();i++)
                {
                    if (gridItems.get(i).isSelected())
                    { final String gender=getPref(Constants.GENDER);
                        if (gender.equals("m")) {
                            builder.append(list1.get(i).getTribeId());
                            builder.append(",");
                        }
                        if (gender.equals("f"))
                        {
                            builder.append(list2.get(i).getTribeId());
                            builder.append(",");
                        }
                    }
                }
                otherbuild=new StringBuilder();
                for (int i=0;i<gridItems.size();i++)
                {
                    if (gridItems.get(i).getTribeImage().isEmpty())
                    {
                        OTHERTRIBENAME=gridItems.get(i).getTribeName();
                        otherbuild.append(OTHERTRIBENAME);
                        otherbuild.append(",");
                    }
                }
                if (!OTHERTRIBENAME.isEmpty())
                {

                  builder.append("0");
                }
                String build=builder.toString();
//              showToast(builder.toString());
                getWebservice(build);

            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(EditTribesActivity.this,PopUpAddTribe.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent,1);
                overridePendingTransition(0,0);


//               mFragmentManager = getSupportFragmentManager();
//               mFragmentTransaction = mFragmentManager.beginTransaction();
//               PopUpAddTribe p=PopUpAddTribe.newInstance();
//               p.show(mFragmentTransaction,"POPUPTRIBE");
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(final RecyclerView recyclerView, final int position, View v) {
                if (gridItems.get(position).isSelected())
                {
                    gridItems.get(position).setSelected(false);
                    adapter.notifyDataSetChanged();

                }
                else {
                    if (!gridItems.get(position).getTribeImage().isEmpty()) {
                        enlarged.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        other.setVisibility(View.GONE);
                        tribelayout.setBackgroundColor(getResources().getColor(R.color.lavender));
//                        Glide.with(Registration_2_Activity.this).load(gridItems.get(position).getTribeImage()).thumbnail(.5f)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(image_enlarged);
                        Picasso.with(EditTribesActivity.this).load(gridItems.get(position).getTribeImage()).into(image_enlarged);
                        style.setText(gridItems.get(position).getTribeName());
                        selectedButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gridItems.get(position).setSelected(true);
                                adapter.notifyDataSetChanged();
                                recyclerView.setVisibility(View.VISIBLE);
                                other.setVisibility(View.VISIBLE);
                                tribelayout.setBackgroundColor(Color.TRANSPARENT);
                                style.setText(R.string.select_tribe);
                                enlarged.setVisibility(View.GONE);
                            }
                        });
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                style.setText(R.string.select_tribe);
                                recyclerView.setVisibility(View.VISIBLE);
                                other.setVisibility(View.VISIBLE);
                                tribelayout.setBackgroundColor(Color.TRANSPARENT);
                                enlarged.setVisibility(View.GONE);

                            }
                        });
                    }
                    else {
                        Intent intent=new Intent(EditTribesActivity.this,PopupdeleteTribe.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("position",position);
                        startActivityForResult(intent,2);
                        overridePendingTransition(0,0);
                    }
                }



            }
        });

    }

    private void getWebservice(final String build) {
        if (!build.isEmpty()) {
//            builder= new MaterialDialog.Builder(this)
//                    .backgroundColor(Color.TRANSPARENT)
//                    .backgroundColorRes(R.color.thistle)
//                    .content(R.string.please_wait)
//                    .progress(true, 0)
//                    .show();
            showProgress();
//            final ProgressDialog pd = new ProgressDialog(this);
//            pd.setMessage("Loading");
//            pd.setCancelable(false);
//            pd.show();
//            progressDialog.show();
            HashMap<String, String> params = new HashMap<>();
            String userid = getPref(Constants.USER_ID);
            params.put("user_id", userid);
            params.put("usertribes", build);
            if (!OTHERTRIBENAME.isEmpty())
            {
                params.put("other_tribe",otherbuild.toString());
            }
            final APIService service = ServiceGenerator.createService(APIService.class, EditTribesActivity.this);
            Call<ModelUpdateProfile>call=service.updateprofile(params);
            call.enqueue(new Callback<ModelUpdateProfile>() {
                @Override
                public void onResponse(Call<ModelUpdateProfile> call, Response<ModelUpdateProfile> response) {
                    if (response.isSuccessful())
                    {
//                        builder.dismiss();
                        hideProgress();
//                        progressDialog.dismiss();
                        if (response.body()!=null&&response.body().getResult()!=null){
                            ModelUpdateProfile modelUpdateProfile=response.body();
                            int value=modelUpdateProfile.getResult().getValue();
                            if (value==1)
                            {
                                String msg=modelUpdateProfile.getResult().getMessage();
                                showToast("Styler tribes updated ",true);
                               onBackPressed();
                            }
                            else {
                                String msg=modelUpdateProfile.getResult().getMessage();
                                showToast(msg,false);
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<ModelUpdateProfile> call, Throwable t) {
//                    if (pd.isShowing()) { pd.dismiss(); }
//                    progressDialog.dismiss();
//                    builder.dismiss();
                    hideProgress();
                }
            });
        }
        else {
            showSnackBar("Please select a tribe",false);
        }
    }

    private void initUi() {

        style= (TextView) findViewById(R.id.style_select_text);
        other= (RelativeLayout) findViewById(R.id.other_layout);
        enlarged= (FrameLayout) findViewById(R.id.enlarged_layout);
        closeButton= (ImageView) findViewById(R.id.close_button);
        selectedButton= (Button) findViewById(R.id.Button_select_Tribe);
        selected_text_name= (TextView) findViewById(R.id.textViewtribe_selected_name);
        image_enlarged= (ImageView) findViewById(R.id.image_enlarged);
        savenow= (LinearLayout) findViewById(R.id.layoutsave);

//        Register_next= (ImageView) findViewById(R.id.register_next);
        othertext= (TextView) findViewById(R.id.otherText);
        tribelayout= (RelativeLayout) findViewById(R.id.tribe_layout);
        othertextlayout= (LinearLayout) findViewById(R.id.linearlayoutother_text);
//        scrollview= (ScrollView) findViewById(R.id.scrollView);

//        tribes= (EditText) findViewById(R.fbid.editTexttribes);
//        tribes.setFocusable(false);
//        reg_next= (ImageView) findViewById(R.fbid.register_next);


//        userpic= (ImageView) findViewById(R.fbid.image_tribes);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/brandon_grotesque_bold.ttf");
        Typeface typeface1=Typeface.createFromAsset(getAssets(),"fonts/BrandonGrotesque-Regular.ttf");
        style.setTypeface(typeface);
        othertext.setTypeface(typeface1);
//        tribes.setTypeface(typeface1);

    }
}
