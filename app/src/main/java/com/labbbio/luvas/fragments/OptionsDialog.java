/**
 * OptionsDialog: special fragment, a popup window where the user can choose the response method for reception exercise,
 * between: Voice response or drawn gestures on screen.
 */

package com.labbbio.luvas.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;

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
