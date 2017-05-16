package com.mindbees.stylerapp.UI.HOME.Gallery;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Adapter.ImageAdapter;
import com.mindbees.stylerapp.UI.Base.BaseActivity;
import com.mindbees.stylerapp.UI.Models.Gallery.Gallery;
import com.mindbees.stylerapp.UI.Models.Gallery.ModelGallery;
import com.mindbees.stylerapp.UI.Models.SupportImages;
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
 * Created by User on 12-04-2017.
 */

public class ImageActivity extends BaseActivity {
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    ImageAdapter adapter;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private ArrayList<SupportImages> images;
    ImageView back;
    TextView nolabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageactivity);
        initUi();
        images=new ArrayList<>();
        setRecyclerView();
        if (isNetworkAvailable())
        {
            getwebservice();
        }
//        int[]imagearray={R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image6,R.drawable.image7};
//        for (int i=0;i<imagearray.length;i++)
//        {
//            SupportImages supportImages=new SupportImages();
//            supportImages.setImages(imagearray[i]);
//            images.add(supportImages);
//        }
        adapter.notifyDataSetChanged();
        initlistener();

    }

    private void getwebservice() {
        showProgress();
        HashMap<String, String> params = new HashMap<>();
        final String userid=getPref(Constants.USER_ID);
        params.put("user_id",userid);
        params.put("limit","40");
        APIService service= ServiceGenerator.createService(APIService.class,ImageActivity.this);
        Call<ModelGallery>call=service.getgallery(params);
        call.enqueue(new Callback<ModelGallery>() {
            @Override
            public void onResponse(Call<ModelGallery> call, Response<ModelGallery> response) {
                if (response.isSuccessful())
                {
                    hideProgress();
                    if (response.body()!=null&&response.body().getGallery()!=null)
                    {

                        ModelGallery gallery=response.body();
                       List<Gallery> list=gallery.getGallery();
                        if (list.size()>0) {
                            nolabel.setVisibility(View.GONE);
                            for (int i = 0; i < list.size(); i++) {
                                SupportImages image = new SupportImages();
                                image.setImages(list.get(i).getImageUrl());
                                image.setId(list.get(i).getImageId());
                                images.add(image);

                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            adapter.notifyDataSetChanged();
                            showToast("No photos!",false);
                            nolabel.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelGallery> call, Throwable t) {
                hideProgress();

            }
        });
    }

    private void initlistener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("images",images);
                bundle.putInt("position",position);


                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();

                newFragment.setArguments(bundle);
                newFragment.show(mFragmentTransaction,"slideshow");

            }
        });
    }

    private void setRecyclerView() {
        adapter=new ImageAdapter(this,images);
        recyclerView.setAdapter(adapter);
    }

    private void initUi() {
        back= (ImageView) findViewById(R.id.back);
        nolabel= (TextView) findViewById(R.id.nolabel);
        recyclerView = (RecyclerView) findViewById(R.id.Recycleview_imageActivity);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(mLayoutManager);
    }
}
