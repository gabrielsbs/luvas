package com.labbbio.luvas.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.labbbio.luvas.LuvasApp;
import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;

import java.nio.charset.Charset;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

public class MessengerFragment extends Fragment {
    EditText outputText;
    TextView inputText;
    StringBuilder messages;
    ImageButton btnSend;
    StringBuilder temp;
    FloatingActionButton fab;

    final String TAG = "Messenger";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messenger, container, false);
        setRetainInstance(true);

        view.setBackgroundColor(Color.parseColor( ((LuvasApp) this.getActivity().getApplication()).getBackgroundColor() ));

        outputText = view.findViewById(R.id.outputText);
        inputText = view.findViewById(R.id.incommingMessage);
        btnSend = view.findViewById(R.id.buttonSend);
        fab = view.findViewById(R.id.fab);


        setTextSize();

        if(temp != null){
            messages = temp;
            inputText.setText(messages);
        }else{
            messages = new StringBuilder();
            messages.append("Mensagens Recebidas: \n");
        }

        if (outputText.requestFocus()) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
            );
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Button pressed");
                String msg = outputText.getText().toString();
                ((MainActivity) getActivity()).sendMessage(msg);
                if(outputText.length()>0)
                    outputText.getText().clear();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Delete button");
                messages.setLength(0);
                messages.append("Mensagens Recebidas: \n");
                inputText.setText(messages);
            }
        });

        IntentFilter intentF = new IntentFilter("incomingMessage");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(incomingMessageReceiver,intentF);

        return view;
    }


    private BroadcastReceiver incomingMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Incoming","Broadcast OK");
            String text = intent.getStringExtra("message");
            messages.append(text);

            inputText.setText(messages);
        }
    };

    public void setTextSize(){
        int size = ((LuvasApp) getActivity().getApplication()).getFontSize();
        outputText.setTextSize(size);
        inputText.setTextSize(size);

        String color = (((LuvasApp) getActivity().getApplication()).getTextColor());
        outputText.setTextColor(Color.parseColor(color));
        inputText.setTextColor(Color.parseColor(color));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"Destroy");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(incomingMessageReceiver);
        temp = messages;
    }

}
