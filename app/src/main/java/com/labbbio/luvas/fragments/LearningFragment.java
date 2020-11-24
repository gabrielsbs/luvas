/**
 * LearningFragment: Fragment that contains the exercises for the deafblinds. It's divided in 2 Fragments: PosLingFragment and PreLingFragment
 * The view has the following elements:
 *  - Viewpager with two tabs, one for the PreLingFragment and the other for the PosLingFragment.
 *      - In each tab there will be a list of exercises
 *  - Clickable CardView: the user can use this button to start the last exercise he/she has done.
 */

package com.labbbio.luvas.fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.labbbio.luvas.LuvasApp;
import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;

public class LearningFragment extends Fragment {

    TabLayout tabs;
    ViewPager viewPager;
    CardView buttonBT;
    Adapter adapter;

    private int posLingLastExercise;
    private int preLingLastExercise;
    private int lastView;
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

        String color =((LuvasApp) this.getActivity().getApplication()).getCardColor();
        buttonBT.setCardBackgroundColor(Color.parseColor(color));

        buttonBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Começar");
                startExercise();
            }
        });

        viewPager.setCurrentItem(((MainActivity) this.getActivity()).getLastViewLearning());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            /**
             * @param i is equal to 0 means the selected page is the PreLingFragment, when
             * it is equal to 1, means the selected page is the PosLingFragment
             */
            @Override
            public void onPageSelected(int i) {
                Log.d(TAG,"Page: "+i);
                setLastViewPage(i);
                if (i == 0){
                    if(preLingLastExercise>0){
                        changeCardText(new String("Voltar de onde parei"));
                    }
                    else{
                        changeCardText(new String("Começar"));
                    }

                }else if(i == 1){
                    posLingLastExercise = ((PosLingFragment)adapter.mFragmentList.get(1)).getLastExercise();
                    Log.d(TAG,"LastExercise: "+posLingLastExercise);
                    if(posLingLastExercise>0){
                        changeCardText(new String("Voltar de onde parei"));
                    }
                    else{
                        changeCardText(new String("Começar"));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        return view;
    }

    public void setLastViewPage(int i){
        ((MainActivity) this.getActivity()).setLastViewLearning(i);
    }

    private void startExercise() {
        if(viewPager.getCurrentItem()==0){

        }
        else{
            ((PosLingFragment)adapter.mFragmentList.get(1)).callExerciseFragment(posLingLastExercise+1);
        }
    }

    public void changeCardText(String s) {
        LinearLayout linearLayout = (LinearLayout) buttonBT.getChildAt(0);
        TextView txt = (TextView) linearLayout.getChildAt(0);
        txt.setText(s);
    }

    private void setTabFontSize() {
        String[] tabNames = {"Pré-Linguístico", "Pós-Linguistíco"};
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setCustomView(R.layout.custom_tab);
            TextView tab_name = (TextView) tabs.getTabAt(i).getCustomView().findViewById(R.id.tab);
            tab_name.setText("" + tabNames[i]);
        }

        String color =((LuvasApp) this.getActivity().getApplication()).getCardColor();
        buttonBT.setCardBackgroundColor(Color.parseColor(color));
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getChildFragmentManager());
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

    @Override
    public void onDestroy() {
        Log.d(TAG,"Destroy");
        super.onDestroy();
    }

}
