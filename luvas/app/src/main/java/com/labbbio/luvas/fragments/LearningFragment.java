package com.labbbio.luvas.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.labbbio.luvas.LuvasApp;
import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;

import java.util.ArrayList;
import java.util.List;

public class LearningFragment extends Fragment {

    TabLayout tabs;
    ViewPager viewPager;
    CardView buttonBT;

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
        buttonBT = view.findViewById(R.id.devCard);
        view.setBackgroundColor(Color.parseColor( ((LuvasApp) this.getActivity().getApplication()).getBackgroundColor() ));
        setTabFontSize();




        changeCardText();

        buttonBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Button BT");
                callBtFragment();
            }
        });

        return view;
    }


    public void changeCardText() {
        LinearLayout linearLayout = (LinearLayout) buttonBT.getChildAt(0);
        TextView txt = (TextView) linearLayout.getChildAt(1);
        if(((MainActivity) getActivity()).getLuvasName() != null){
            String color =((LuvasApp) this.getActivity().getApplication()).getHighlightCardColor();
            buttonBT.setCardBackgroundColor(Color.parseColor(color));
            String s = new String("Conectado à: " + ( (MainActivity) getActivity()).getLuvasName() );
            txt.setText(s);
        }
        else{
            String color =((LuvasApp) this.getActivity().getApplication()).getCardColor();
            buttonBT.setCardBackgroundColor(Color.parseColor(color));
            txt.setText("Conecter Bluetooth");
        }

    }

    private void callBtFragment() {
        ((MainActivity) this.getActivity()).setLastFragment(LEARNING_FRAGMENT);
        ((MainActivity) this.getActivity()).btFragmentStart();
    }

    private void setTabFontSize() {
        int size = ((LuvasApp) this.getActivity().getApplication()).getFontSize();
        LinearLayout ln =((LinearLayout) tabs.getChildAt(0));
        int i = ln.getChildCount();
        FrameLayout.LayoutParams layoutParams;
        Log.d(TAG,"childs: "+i);
        if(size == 15)
            layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,50);
        else
            layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,100);
        ln.setLayoutParams(layoutParams);

        String textColor = ((LuvasApp) this.getActivity().getApplication()).getTextColor();

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

        String color =((LuvasApp) this.getActivity().getApplication()).getCardColor();
        buttonBT.setCardBackgroundColor(Color.parseColor(color));
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
