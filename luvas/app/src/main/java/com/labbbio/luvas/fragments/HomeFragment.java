/**
 * HomeFragment: Initial fragment, only has the menu to open the other Fragments
 * The view has the following elements:
 *  - Menu: A GridView with 3 rows, in each row is a clickable CardView, that opens one of the other 3 Fragments.
 */
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

    public void changeCardText() {
        LinearLayout linearLayout = (LinearLayout) deviceCard.getChildAt(0);
        TextView txt = (TextView) linearLayout.getChildAt(1);
        if(((MainActivity) getActivity()).getLuvasName() != null){
            String color =((LuvasApp) this.getActivity().getApplication()).getHighlightCardColor();
            deviceCard.setCardBackgroundColor(Color.parseColor(color));
            Log.d("HomeFragment", ((MainActivity) getActivity()).getLuvasName());
            String s = new String("Conectado à: " + ( (MainActivity) getActivity()).getLuvasName() );
            txt.setText(s);
        }
        else{
            String color =((LuvasApp) this.getActivity().getApplication()).getCardColor();
            deviceCard.setCardBackgroundColor(Color.parseColor(color));
            txt.setText("Conectar");
        }
    }

    private void setCardsColor() {
        String color =((LuvasApp) this.getActivity().getApplication()).getCardColor();
        messengerCard.setCardBackgroundColor(Color.parseColor(color));
        learningCard.setCardBackgroundColor(Color.parseColor(color));
        deviceCard.setCardBackgroundColor(Color.parseColor(color));
    }

}
