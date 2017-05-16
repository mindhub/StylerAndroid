package com.mindbees.stylerapp.UI.TabFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UI.Base.BaseFragment;

/**
 * Created by User on 03-04-2017.
 */

public class MessagesFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.messages_fragment_layout,null);
        return v;
    }
}
