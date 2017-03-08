package com.mindbees.stylerapp.UI.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Models.SpinnerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 07-03-2017.
 */

public class SpinnerAdapter extends BaseAdapter {
    public List<SpinnerModel> list ;
    Context _activity;
    LayoutInflater inflator;
    public SpinnerAdapter(Context context, List<SpinnerModel> objects) {
        this.list = objects;
        this._activity = context;
        inflator = LayoutInflater.from(_activity);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View v = convertView;

        ViewHolder holder;
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();

            holder.tvtitle = (TextView) v.findViewById(R.id.textspinner);
//            holder.ivselector = (ImageView) v.findViewById(R.id.imgSelector);
//			holder.imgShop = (ImageView) v.findViewById(R.id.categryIcon);

            v.setTag(holder);
        }
        else{
            holder = (ViewHolder) v.getTag();
        }

        holder.tvtitle.setText(list.get(position).getName());
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        ViewHolder holder;
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);
            holder = new ViewHolder();

            holder.tvtitle = (TextView) v.findViewById(R.id.company);
//            holder.ivselector = (ImageView) v.findViewById(R.id.imgSelector);
//			holder.imgShop = (ImageView) v.findViewById(R.id.categryIcon);

            v.setTag(holder);
        }
        else{
            holder = (ViewHolder) v.getTag();
        }

        holder.tvtitle.setText(list.get(position).getName());
        return v;
    }

    class ViewHolder {
        TextView tvtitle;

    }
}
