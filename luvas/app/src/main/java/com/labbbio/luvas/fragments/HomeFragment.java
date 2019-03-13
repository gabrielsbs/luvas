package com.labbbio.luvas.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.labbbio.luvas.LuvasApp;
import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;



public class HomeFragment extends Fragment {

    CardView messengerCard, btCard, learningCard, accessCard, fontCard, deviceCard;
    GridLayout gridLayout;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        gridLayout = view.findViewById(R.id.grid);
        messengerCard = view.findViewById(R.id.messengerCard);
        learningCard = view.findViewById(R.id.exercizeCard);
        deviceCard = view.findViewById(R.id.deviceCard);
        btCard = view.findViewById(R.id.btCard);
        accessCard = view.findViewById(R.id.acessCard);
        fontCard = view.findViewById(R.id.fontCard);

        int size = ((LuvasApp) this.getActivity().getApplication()).getFontSize();
        changeHomeTextSize(size);

        if(((MainActivity)getActivity()).btIsEnabled()){
            btCard.setCardBackgroundColor(Color.parseColor("#FF6F00"));
        }

        if(((MainActivity) this.getActivity()).getFontState())
            fontCard.setCardBackgroundColor(Color.parseColor("#FF6F00"));

        setSingleEvent();

        return view;
    }

    private void setSingleEvent() {
        messengerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).messengerFragmentStart();
            }
        });
        learningCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).learningFragmentStart();
            }
        });

        deviceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).btFragmentStart();
            }
        });

        btCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btCard.getCardBackgroundColor().getDefaultColor() == -1){
                    ((MainActivity) getActivity()).setBtSwitch();
                    btCard.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                }else{
                    ((MainActivity) getActivity()).setBtSwitch();
                    btCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                }

            }
        });

        accessCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accessCard.getCardBackgroundColor().getDefaultColor() == -1){

                }

            }
        });

        fontCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fontCard.getCardBackgroundColor().getDefaultColor() == -1){
                    ((MainActivity)getActivity()).setFontSwitch();
                    fontCard.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                   // changeHomeTextSize(35);

                }else{
                    ((MainActivity)getActivity()).setFontSwitch();
                    fontCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
               //     changeHomeTextSize(15);
                }

            }
        });

    }

    public void changeHomeTextSize(int size){

        for(int i = 0; i<gridLayout.getChildCount();i++){
            CardView childCard = (CardView) gridLayout.getChildAt(i);
            LinearLayout linearLayout = (LinearLayout) childCard.getChildAt(0);
            TextView tv = (TextView) linearLayout.getChildAt(1);
            tv.setTextSize(size);
        }

    }

    public void setBtCard(boolean state){
        if (state)
            btCard.setCardBackgroundColor(Color.parseColor("#FF6F00"));
        else
            btCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
    }



}
