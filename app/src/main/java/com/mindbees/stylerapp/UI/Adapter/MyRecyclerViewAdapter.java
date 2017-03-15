package com.mindbees.stylerapp.UI.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 10-03-2017.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.VIEWHOLDER> {
    @Override
    public MyRecyclerViewAdapter.VIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.VIEWHOLDER holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        public VIEWHOLDER(View itemView) {
            super(itemView);
        }
    }
}
