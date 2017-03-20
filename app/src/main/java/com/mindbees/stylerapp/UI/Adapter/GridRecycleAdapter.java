package com.mindbees.stylerapp.UI.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Models.ImagegridModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by User on 10-03-2017.
 */

public class GridRecycleAdapter extends RecyclerView.Adapter<GridRecycleAdapter.VIEWHOLDER> {
    private Context context;
    public ArrayList<ImagegridModel>imagegridModels;
    public GridRecycleAdapter(Context context,ArrayList<ImagegridModel>imagegridModels)
    {
        this.context=context;
        this.imagegridModels=imagegridModels;
    }
    public ImagegridModel getObject(int position)
    {
        return imagegridModels.get(position);
    }
    @Override
    public GridRecycleAdapter.VIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row, parent, false);
        return new VIEWHOLDER(itemView);

    }

    @Override
    public void onBindViewHolder(GridRecycleAdapter.VIEWHOLDER holder, int position) {
        Picasso.with(context).load(imagegridModels.get(position).getTribeImage()).into(holder.tribe_image);
        holder.tribe_name.setText(imagegridModels.get(position).getTribeName());
        if (imagegridModels.get(position).isSelected())
        {
            holder.tribe_selected.setVisibility(View.VISIBLE);
        }
        else {
            holder.tribe_selected.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return this.imagegridModels.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        TextView tribe_name;
        ImageView tribe_image;
        RelativeLayout tribe_selected;


        public VIEWHOLDER(View itemView) {
            super(itemView);
            tribe_image= (ImageView) itemView.findViewById(R.id.imageTribe);
            tribe_name= (TextView) itemView.findViewById(R.id.imageTribeName);
            tribe_selected= (RelativeLayout) itemView.findViewById(R.id.image_select);

        }
    }
}
