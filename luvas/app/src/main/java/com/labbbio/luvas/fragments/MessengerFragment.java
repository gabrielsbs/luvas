package com.labbbio.luvas.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.labbbio.luvas.LuvasApp;
import com.labbbio.luvas.R;
import com.labbbio.luvas.TalkBackLuvas;

import java.nio.charset.Charset;

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

        this.getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container).getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));

        outputText = view.findViewById(R.id.outputText);
        inputText = view.findViewById(R.id.incommingMessage);
        btnSend = view.findViewById(R.id.buttonSend);
        fab = view.findViewById(R.id.fab);

        setTextSize();

        final Activity activity = this.getActivity();

        if(temp != null){
            messages = temp;
            inputText.setText(messages);
        }else{
            messages = new StringBuilder();
            messages.append("Mensagens Recebidas: \n");
        }


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Button pressed");
                byte[] bytes = outputText.getText().toString().getBytes(Charset.defaultCharset());
                ((LuvasApp) activity.getApplication()).getBluetoothService().write(bytes);
                outputText.setText("");
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

        IntentFilter intentF = new IntentFilter("incommingMessage");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(incommingMessageReceiver,intentF);

        return view;
    }

    private BroadcastReceiver incommingMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Incomming","Broadcast OK");
            String text = intent.getStringExtra("message");
            messages.append(text + "\n");

            inputText.setText(messages);
        }
    };

    public void setTextSize(){
        int size = ((LuvasApp) getActivity().getApplication()).getFontSize();
        outputText.setTextSize(size);
        inputText.setTextSize(size);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Incomming","Destroy");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(incommingMessageReceiver);
        temp = messages;
    }

}
