package com.mindbees.stylerapp.UI.TabFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mindbees.stylerapp.R;
import com.mindbees.stylerapp.UTILS.NonSwipeableViewPager;

/**
 * Created by User on 28-03-2017.
 */

public class HomeTabfragment extends Fragment {
    TabLayout tabLayout;
    NonSwipeableViewPager viewPager;
    public static int int_items = 4 ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ex = inflater.inflate(R.layout.home_tab_layout, null);
        tabLayout = (TabLayout) ex.findViewById(R.id.tabs);
        viewPager = (NonSwipeableViewPager) ex.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);


        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
//                for (int i=0;i<4;i++) {
//                   tabLayout.getTabAt(3).setCustomView(R.layout.bdged_tab);
//                }
            }
        });
        return ex;
    }
    private class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager childFragmentManager) {
            super(childFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
               case 0 : return new GlobalFragment();
                case 1 : return new NearByFragment();
                case 2:return new SearchFragment();
                case 3:return  new MessagesFragment();


            }

            return null;
        }

        @Override
        public int getCount() {
            return int_items;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:return "GLOBAL";

                case 1 :
                  return "NEARBY";
                case 2 :
                    return "SEARCH";
                case 3:return "MESSAGES";

            }
            return null;

        }
    }
}
