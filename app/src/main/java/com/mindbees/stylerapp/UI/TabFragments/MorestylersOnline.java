package com.mindbees.stylerapp.UI.TabFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Adapter.GRIDHOMEADAPTER;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.DETAIL.DetailActivity;
import com.mindbees.stylerapp.UI.Models.GridHomeModel;
import com.mindbees.stylerapp.UI.Models.NewStylers.ModelnewStylers;
import com.mindbees.stylerapp.UI.Models.NewStylers.NewStyler;
import com.mindbees.stylerapp.UI.Models.NewStylers.OnlineStyler;
import com.mindbees.stylerapp.UI.Models.NewStylers.Result;
import com.mindbees.stylerapp.UTILS.Constants;
import com.mindbees.stylerapp.UTILS.ItemClickSupport;
import com.mindbees.stylerapp.UTILS.Retrofit.APIService;
import com.mindbees.stylerapp.UTILS.Retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 06-04-2017.
 */

public class MorestylersOnline extends BaseActivity {
    RecyclerView recyclerview;
    ImageView back;
    RecyclerView.LayoutManager mLayoutManager;
    TextView text;
    List<NewStyler> newStylers;
    List<OnlineStyler>onlineStylers;
    GRIDHOMEADAPTER adpter;
    public ArrayList<GridHomeModel> list1;
    String LAT="",LONG="";
    boolean isonline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_stylers_online);
        iniui();
        list1=new ArrayList<>();
        try{
            Bundle Extras=getIntent().getExtras();
            LAT=Extras.getString("LAT");
            LONG=Extras.getString("LONG");
            isonline=Extras.getBoolean("IS_ONLINE",false);
            if (isonline)
            {
                text.setText("Online");
            }


        }catch (Exception e)
        {

        }
        setrecyclerview();
        setadapter();
        getWebService();
        setui();

    }

    private void setui() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ItemClickSupport.addTo(recyclerview).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (isonline)
                {
                    String userid=onlineStylers.get(position).getUserId();
                    String online=onlineStylers.get(position).getOnlineStatus();
                    String distance=onlineStylers.get(position).getDistance();
                    String userphoto=onlineStylers.get(position).getUserPhoto();
                    Intent intent=new Intent(MorestylersOnline.this, DetailActivity.class);
                    intent.putExtra("user_id",userid);
                    intent.putExtra("photo",userphoto);
                    intent.putExtra("online",online);
                    intent.putExtra("distance",distance);
                    startActivity(intent);
                }
                else
                {
                    String userid=newStylers.get(position).getUserId();
                    String online=newStylers.get(position).getOnlineStatus();
                    String distance=newStylers.get(position).getDistance();
                    String userphoto=newStylers.get(position).getUserPhoto();
                    Intent intent=new Intent(MorestylersOnline.this, DetailActivity.class);
                    intent.putExtra("user_id",userid);
                    intent.putExtra("photo",userphoto);
                    intent.putExtra("online",online);
                    intent.putExtra("distance",distance);
                    startActivity(intent);
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


        super.onBackPressed();
        overridePendingTransition(0,0);
    }
    private void getWebService() {
        showProgress();
        HashMap<String, String> params = new HashMap<>();
        String userid=getPref(Constants.USER_ID);
        params.put("userid",userid);
//        LAT=getPref(Constants.LAT);
//        LONG=getPref(Constants.LONG);
        if (!LAT.isEmpty())
        {
            params.put("myLat",LAT);
        }
        if (!LONG.isEmpty())
        {
            params.put("myLon",LONG);
        }
//        showToast("Success",true);
//        params.put("myLat",Lat);
//        params.put("myLong",Long);
        params.put("limit","40");
        APIService service = ServiceGenerator.createService(APIService.class, MorestylersOnline.this);
        Call<ModelnewStylers> call=service.getnewstylers(params);
        call.enqueue(new Callback<ModelnewStylers>() {
            @Override
            public void onResponse(Call<ModelnewStylers> call, Response<ModelnewStylers> response) {
                if (response.isSuccessful())
                {
//                    showToast("Success",true);
                    hideProgress();

                    if (response.body()!=null&&response.body().getResult()!=null)
                    {if (isonline)
                    {
                        ModelnewStylers modelnewStylers=response.body();
                        Result result=modelnewStylers.getResult();
                        onlineStylers=result.getOnlineStylers();
                        list1.clear();

                        for (int i=0;i<onlineStylers.size();i++)
                        {
                            GridHomeModel model=new GridHomeModel();
                            model.setImagename(onlineStylers.get(i).getUsername());
                            model.setImagepath(onlineStylers.get(i).getUserPhoto());
                            String onlinestatus=onlineStylers.get(i).getOnlineStatus();
                            model.setOnline(Integer.valueOf(onlinestatus));
                            list1.add(model);

                        }
                        adpter.notifyDataSetChanged();
                    }
                    else
                    {
                        ModelnewStylers modelnewStylers=response.body();
                        Result result=modelnewStylers.getResult();
                        newStylers=result.getNewStylers();
                        list1.clear();

                        for (int i=0;i<newStylers.size();i++)
                        {
                            GridHomeModel model=new GridHomeModel();
                            model.setImagename(newStylers.get(i).getUsername());
                            model.setImagepath(newStylers.get(i).getUserPhoto());
                            String onlinestatus=newStylers.get(i).getOnlineStatus();
                            model.setOnline(Integer.valueOf(onlinestatus));
                            list1.add(model);

                        }
                        adpter.notifyDataSetChanged();
                    }


                    }
                }
            }

            @Override
            public void onFailure(Call<ModelnewStylers> call, Throwable t) {
                showToast("NETWORK ERROR",false);
                hideProgress();
            }

        });
    }

    private void setadapter() {
        adpter = new GRIDHOMEADAPTER(MorestylersOnline.this, list1);
        recyclerview.setAdapter(adpter);
    }

    private void setrecyclerview() {
        recyclerview.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 4);
        recyclerview.setLayoutManager(mLayoutManager);
    }

    private void iniui() {
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/brandon_grotesque_bold.ttf");
        Typeface typeface1=Typeface.createFromAsset(getAssets(),"fonts/BrandonGrotesque-Regular.ttf");
        recyclerview= (RecyclerView) findViewById(R.id.recyclerViewmore);
        back= (ImageView) findViewById(R.id.back);
        text= (TextView) findViewById(R.id.text_head_newStylers);
        text.setTypeface(typeface);
    }


}
