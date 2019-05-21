package com.labbbio.luvas.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;

import java.util.ArrayList;

public class OptionsDialog extends AppCompatDialogFragment {
    private ListView listView;
    private ArrayList<String> options;
    private ArrayAdapter<String> arrayAdapter;


    private static final int VOICE_OPTION = 5;
    private static final int GESTURE_OPTION = 6;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout,null);

        builder.setView(view)
                .setTitle("Escolha como deseja responder");

        options = new ArrayList<>();
        options.add("Por voz");
        options.add("Escrever na tela");
        arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.opt_adapter_view,options);
        listView = view.findViewById(R.id.optListView);
        listView.setAdapter(arrayAdapter);
        listView.setItemChecked(0, true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, true);
                switch (position){
                    case 0:
                        setOption(VOICE_OPTION);
                        break;
                    case 1:
                        setOption(GESTURE_OPTION);
                        break;
                }
                dismiss();
            }
        });

        return builder.create();
    }


    public void setOption(int option){
        ((MainActivity)this.getActivity()).setOption(option);
    }

}
