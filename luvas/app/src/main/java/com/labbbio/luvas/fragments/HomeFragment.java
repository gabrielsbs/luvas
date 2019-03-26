package com.labbbio.luvas.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.util.Log;
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

    private static final int HOME_FRAGMENT = 0;


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

        view.setBackgroundColor(Color.parseColor( ((LuvasApp) this.getActivity().getApplication()).getBackgroundColor() ));
        setCardsColor();



        int size = ((LuvasApp) this.getActivity().getApplication()).getFontSize();
        changeHomeTextSize(size);

        if(((MainActivity)getActivity()).btIsEnabled()){
            btCard.setCardBackgroundColor(Color.parseColor(((LuvasApp) getActivity().getApplication()).getHighlightCardColor()));
        }

        if(((MainActivity) this.getActivity()).getFontState())
            fontCard.setCardBackgroundColor(Color.parseColor(((LuvasApp) getActivity().getApplication()).getHighlightCardColor()));

        setClickEvent();

        return view;
    }


    private void setClickEvent() {
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
                ((MainActivity)getActivity()).setLastFragment(HOME_FRAGMENT);
                ((MainActivity)getActivity()).btFragmentStart();
            }
        });

        btCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String color = getCardColor(btCard);
                String cardColor = ((LuvasApp) getActivity().getApplication()).getCardColor();
                if(color.equals(cardColor)){
                    ((MainActivity) getActivity()).setBtSwitch();
                    Log.d("HomeFragment","Card change");
                    btCard.setCardBackgroundColor(Color.parseColor(((LuvasApp) getActivity().getApplication()).getHighlightCardColor()));
                }else{
                    ((MainActivity) getActivity()).setBtSwitch();
                    btCard.setCardBackgroundColor(Color.parseColor(((LuvasApp) getActivity().getApplication()).getCardColor()));
                }

            }
        });

        accessCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String color = getCardColor(accessCard);
                String cardColor = ((LuvasApp) getActivity().getApplication()).getCardColor();
                if(color.equals(cardColor)){
                    ((MainActivity)getActivity()).accessibilityFragmentStart();
                    accessCard.setCardBackgroundColor(Color.parseColor(((LuvasApp) getActivity().getApplication()).getHighlightCardColor()));

                }else{
                    ((MainActivity)getActivity()).accessibilityFragmentStart();
                    accessCard.setCardBackgroundColor(Color.parseColor(((LuvasApp) getActivity().getApplication()).getCardColor()));

                }

            }
        });

        fontCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String color = getCardColor(fontCard);
                String cardColor = ((LuvasApp) getActivity().getApplication()).getCardColor();
                if(color.equals(cardColor)){
                    ((MainActivity)getActivity()).setFontSwitch();
                    fontCard.setCardBackgroundColor(Color.parseColor(((LuvasApp) getActivity().getApplication()).getHighlightCardColor()));

                }else{
                    ((MainActivity)getActivity()).setFontSwitch();
                    fontCard.setCardBackgroundColor(Color.parseColor(((LuvasApp) getActivity().getApplication()).getCardColor()));

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
            btCard.setCardBackgroundColor(Color.parseColor(((LuvasApp) getActivity().getApplication()).getHighlightCardColor()));
        else
            btCard.setCardBackgroundColor(Color.parseColor(((LuvasApp) getActivity().getApplication()).getCardColor()));
    }

    private void setCardsColor() {
        String color =((LuvasApp) this.getActivity().getApplication()).getCardColor();
        messengerCard.setCardBackgroundColor(Color.parseColor(color));
        learningCard.setCardBackgroundColor(Color.parseColor(color));
        btCard.setCardBackgroundColor(Color.parseColor(color));
        deviceCard.setCardBackgroundColor(Color.parseColor(color));
        accessCard.setCardBackgroundColor(Color.parseColor(color));
        fontCard.setCardBackgroundColor(Color.parseColor(color));
    }

    private String getCardColor(CardView card){
        int colorNumber = card.getCardBackgroundColor().getDefaultColor();
        String color = Integer.toHexString(colorNumber).substring(2);
        color = '#'+color;
        return color;
    }



}
