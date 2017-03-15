package com.mindbees.stylerapp.UI.Landing;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Adapter.SpinnerAdapter;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.Models.Tribes.Result;
import com.mindbees.stylerapp.UI.Models.SpinnerModel;
import com.mindbees.stylerapp.UI.Models.Tribes.ModelTribes;
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
 * Created by User on 08-03-2017.
 */

public class Registration_2_Activity extends BaseActivity{
    AppCompatSpinner spin;
    public List<SpinnerModel> listitems;
    List<Result>list;
    ImageView userpic,reg_next;
    SpinnerAdapter adapter;
    TextView style;
    EditText tribes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_2_layout);
        initUi();

        listitems=new ArrayList<SpinnerModel>();
       if (isNetworkAvailable())
       {
           getTribes();
       }
       else {
           showToast("NETWORK UNAVAILABLE");
       }
        setupUI();
    }

    private void getTribes() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("gender","m");
        APIService service = ServiceGenerator.createService(APIService.class, Registration_2_Activity.this);
        Call<ModelTribes>call=service.gettribes(params);
        call.enqueue(new Callback<ModelTribes>() {
            @Override
            public void onResponse(Call<ModelTribes> call, Response<ModelTribes> response) {
                if (response.isSuccessful())
                {
                    if (pd.isShowing()) { pd.dismiss(); }
                    if (response.body()!=null&&response.body().getResult()!=null)
                    {
                        ModelTribes modeltribes=response.body();
                        list=modeltribes.getResult();
//                        SpinnerModel model=new SpinnerModel();
//                        model.setName("Mens Tribes");
//                        listitems.add(model);
                        for (int i=0;i<list.size();i++)
                        {
                            SpinnerModel model2=new SpinnerModel();
                            model2.setName(list.get(i).getTribeTitle());
                            listitems.add(model2);
                        }
                        adapter=new SpinnerAdapter(Registration_2_Activity.this,listitems);
                        spin.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelTribes> call, Throwable t) {
                if (pd.isShowing()) { pd.dismiss(); }
                showToast("Network Error");

            }
        });
    }

    private void setupUI() {
        tribes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin.setVisibility(View.VISIBLE);
                spin.performClick();
            }
        });


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tribes.setText(list.get(position).getTribeTitle());

                    String url=list.get(position).getTribeImg();
                    showToast(url);
                    Picasso.with(Registration_2_Activity.this).load(url+list.get(position).getTribeId()).into(userpic);
                spin.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initUi() {
        spin= (AppCompatSpinner) findViewById(R.id.spinnertribes);
        style= (TextView) findViewById(R.id.style_select_text);
        tribes= (EditText) findViewById(R.id.editTexttribes);
        tribes.setFocusable(false);
        reg_next= (ImageView) findViewById(R.id.register_next);


        userpic= (ImageView) findViewById(R.id.image_tribes);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/brandon_grotesque_bold.ttf");
        Typeface typeface1=Typeface.createFromAsset(getAssets(),"fonts/BrandonGrotesque-Regular.ttf");
        style.setTypeface(typeface);
        tribes.setTypeface(typeface1);

    }
}
