package com.labbbio.luvas.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.labbbio.apiluvas.BluetoothService;
import com.labbbio.apiluvas.DeviceListAdapter;
import com.labbbio.luvas.LuvasApp;
import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;

import java.util.ArrayList;
import java.util.UUID;

public class BluetoohFragment extends Fragment{

    private static final String TAG = "BluetoothFragment";

    BluetoothDevice btDevice;

    UUID uuid =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothService bluetoothConnection;

    BluetoothAdapter bluetoothAdapter;

    public ArrayList<BluetoothDevice> btDevices = new ArrayList<>();
    private ArrayList<BluetoothDevice> temp;

    public DeviceListAdapter deviceListAdapter;

    ListView lvNewDevices;
    private TextView title;
    private String color;
    private int size;

    private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("Device_Added")){
                if (intent != null && intent.getExtras() != null) {
                    btDevices = intent.getExtras().getParcelableArrayList("arrayDevices");

                    Log.d(TAG,"onReceive: "+btDevices.get(0).getName());
                    if (btDevices != null) {
                        deviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, btDevices);
                        deviceListAdapter.setTextColor(((LuvasApp)getActivity().getApplication()).getTextColor());
                        lvNewDevices.setAdapter(deviceListAdapter);
                    }
                }
            }
        }
    };

    private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"Connected");
            String name = intent.getExtras().getString("Device_Name");
            ((MainActivity) getActivity()).setLuvasName(name);
            ((MainActivity) getActivity()).goLastFragment();
        }
    };



    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);


        Log.d(TAG,"onCreateView");
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        view.setBackgroundColor(Color.parseColor( ((LuvasApp) this.getActivity().getApplication()).getBackgroundColor() ));
        color = ((LuvasApp)getActivity().getApplication()).getTextColor();
        size = ((LuvasApp)getActivity().getApplication()).getFontSize();
        title = view.findViewById(R.id.title);
        title.setTextColor(Color.parseColor(color));
        title.setTextSize(size);

        lvNewDevices = view.findViewById(R.id.lvNewDevices);
        uuid =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        if(temp != null){
            btDevices = temp;
            deviceListAdapter = new DeviceListAdapter(this.getContext(), R.layout.device_adapter_view, btDevices);
            deviceListAdapter.setTextColor(color);
            lvNewDevices.setAdapter(deviceListAdapter);
        }



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothConnection = ((LuvasApp) this.getActivity().getApplication()).getBluetoothService();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(discoveryReceiver,
                new IntentFilter("Device_Added"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(connectionReceiver,
                new IntentFilter("CONNECTION_ESTABLISHED"));

        lvNewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //first cancel discovery because its very memory intensive.
                bluetoothAdapter.cancelDiscovery();

                Log.d(TAG, "onItemClick: You Clicked on a device.");
                String deviceName = btDevices.get(i).getName();
                String deviceAddress = btDevices.get(i).getAddress();
                btDevices.get(i).fetchUuidsWithSdp();

                Log.d(TAG, "onItemClick: deviceName = " + deviceName);
                Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

                //create the bond.
                //NOTE: Requires API 17+? I think this is JellyBean
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                    Log.d(TAG, "Trying to pair with " + deviceName);
                    btDevices.get(i).createBond();

                    btDevice = btDevices.get(i);

                    Log.d(TAG, "onItemClick: deviceName = " + btDevice.getName());

                    startConnection();

                }
            }
          }
        );
        return view;
    }



    public void startConnection(){

        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        bluetoothConnection.startClient(btDevice, uuid);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(connectionReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(discoveryReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(connectionReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(discoveryReceiver);
        temp = btDevices;

    }

}
