package com.labbbio.luvas.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.nio.charset.Charset;

public class HomeFragment extends Fragment {
    EditText outputText;
    TextView inputText;
    StringBuilder messages;
    ImageButton btnSend;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        outputText = view.findViewById(R.id.outputText);
        inputText = view.findViewById(R.id.incommingMessage);
        btnSend = view.findViewById(R.id.buttonSend);

        final Activity actiity = this.getActivity();

        messages = new StringBuilder();
        messages.append("Mensagens Recebidas: \n");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sendind","Button pressed");
                byte[] bytes = outputText.getText().toString().getBytes(Charset.defaultCharset());
                ((LuvasApp) actiity.getApplication()).getBluetoothService().write(bytes);
                outputText.setText("");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Incomming","Destroy");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(incommingMessageReceiver);
    }

}
