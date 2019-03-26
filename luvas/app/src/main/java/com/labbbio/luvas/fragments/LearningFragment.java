package com.labbbio.luvas.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labbbio.apiluvas.BluetoothService;
import com.labbbio.apiluvas.DeviceListAdapter;
import com.labbbio.luvas.LuvasApp;
import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LearningFragment extends Fragment {

    TabLayout tabs;
    ViewPager viewPager;
    FloatingActionButton fab2;

    private static final int LEARNING_FRAGMENT = 2;


    final String TAG = "Learning";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.fragment_learning,container, false);
        // Setting ViewPager for each Tabs
        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        fab2 = view.findViewById(R.id.fab2);
        view.setBackgroundColor(Color.parseColor( ((LuvasApp) this.getActivity().getApplication()).getBackgroundColor() ));
        setTabFontSize();


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Delete button");
                callBtFragment();
            }
        });

        return view;
    }

    private void callBtFragment() {
        ((MainActivity) this.getActivity()).setLastFragment(LEARNING_FRAGMENT);
        ((MainActivity) this.getActivity()).btFragmentStart();
    }

    private void setTabFontSize() {
        String textColor = ((LuvasApp) this.getActivity().getApplication()).getTextColor();
        int size = ((LuvasApp) this.getActivity().getApplication()).getFontSize();
        TextView textView = (TextView) LayoutInflater.from(this.getContext()).inflate(R.layout.custom_tab,null);
        textView.setText("PRÉ-LINGUÍSTICO");
        textView.setTextSize(size);
        textView.setTextColor(Color.parseColor(textColor));
        tabs.getTabAt(0).setCustomView(textView);

        TextView textView2 = (TextView) LayoutInflater.from(this.getContext()).inflate(R.layout.custom_tab,null);
        textView2.setText("PÓS-LINGUÍSTICO");
        textView2.setTextSize(size);
        textView2.setTextColor(Color.parseColor(textColor));
        tabs.getTabAt(1).setCustomView(textView2);
    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new PreLingFragment(), "Pré-Linguistico");
        adapter.addFragment(new PosLingFragment(), "Pós-Linguistico");
        viewPager.setAdapter(adapter);



    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




}
