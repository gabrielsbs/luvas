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

    CardView messengerCard, learningCard, deviceCard;
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

        view.setBackgroundColor(Color.parseColor( ((LuvasApp) this.getActivity().getApplication()).getBackgroundColor() ));
        setCardsColor();


        changeCardText();

        int size = ((LuvasApp) this.getActivity().getApplication()).getFontSize();
        changeHomeTextSize(size);

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
    }

    public void changeHomeTextSize(int size){

        for(int i = 0; i<gridLayout.getChildCount();i++){
            CardView childCard = (CardView) gridLayout.getChildAt(i);
            LinearLayout linearLayout = (LinearLayout) childCard.getChildAt(0);
            TextView tv = (TextView) linearLayout.getChildAt(1);
            tv.setTextSize(size);
        }

    }

    public void changeCardText() {
        LinearLayout linearLayout = (LinearLayout) deviceCard.getChildAt(0);
        TextView txt = (TextView) linearLayout.getChildAt(1);
        if(((MainActivity) getActivity()).getLuvasName() != null){
            String color =((LuvasApp) this.getActivity().getApplication()).getHighlightCardColor();
            deviceCard.setCardBackgroundColor(Color.parseColor(color));
            String s = new String("Conectado à: " + ( (MainActivity) getActivity()).getLuvasName() );
            txt.setText(s);
        }
        else{
            String color =((LuvasApp) this.getActivity().getApplication()).getCardColor();
            deviceCard.setCardBackgroundColor(Color.parseColor(color));
            txt.setText("Conectar Bluetooth");
        }

    }

    private void setCardsColor() {
        String color =((LuvasApp) this.getActivity().getApplication()).getCardColor();
        messengerCard.setCardBackgroundColor(Color.parseColor(color));
        learningCard.setCardBackgroundColor(Color.parseColor(color));
        deviceCard.setCardBackgroundColor(Color.parseColor(color));
    }




}
