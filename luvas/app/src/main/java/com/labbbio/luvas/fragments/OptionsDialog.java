package com.labbbio.luvas.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

    private String[] options = {"Por voz","Escrever na tela"};

    private static final int VOICE_OPTION = 5;
    private static final int GESTURE_OPTION = 6;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);

        builder.setTitle("Escolha como deseja responder");

        int checkedItem = ((MainActivity) this.getActivity()).getAnswerOption() - 5; //
        builder.setSingleChoiceItems(options, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
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
        builder.setNegativeButton("Cancelar", null);

        return builder.create();
    }


    public void setOption(int option){
        ((MainActivity)this.getActivity()).setOption(option);
    }

}
