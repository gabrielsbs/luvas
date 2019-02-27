package com.labbbio.luvas.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.labbbio.apiluvas.BluetoothService;
import com.labbbio.apiluvas.DeviceListAdapter;
import com.labbbio.luvas.LuvasApp;
import com.labbbio.luvas.R;

import java.util.ArrayList;
import java.util.UUID;

public class BluetoohFragment extends Fragment{

    private static final String TAG = "BluetoothFragment";

    BluetoothDevice mBTDevice;

    UUID uuid =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothService mBluetoothConnection;

    BluetoothAdapter mBluetoothAdapter;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    public DeviceListAdapter mDeviceListAdapter;

    ListView lvNewDevices;

    private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("Device_Added")){
                if (intent != null && intent.getExtras() != null) {
                    mBTDevices = intent.getExtras().getParcelableArrayList("arrayDevices");

                    Log.d(TAG,"onReceive: "+mBTDevices.get(0).getName());
                    if (mBTDevices != null) {
                        mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                        lvNewDevices.setAdapter(mDeviceListAdapter);
                    }
                }
            }
        }
    };

    private BroadcastReceiver bondReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getExtras() != null) {
                mBTDevice= (BluetoothDevice) intent.getExtras().get("bondedDevice");
            }
        }
    };



    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        lvNewDevices = view.findViewById(R.id.lvNewDevices);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(discoveryReceiver,
                new IntentFilter("Device_Added"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bondReceiver,
                new IntentFilter("Bonded"));

        lvNewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //first cancel discovery because its very memory intensive.
                mBluetoothAdapter.cancelDiscovery();

                Log.d(TAG, "onItemClick: You Clicked on a device.");
                String deviceName = mBTDevices.get(i).getName();
                String deviceAddress = mBTDevices.get(i).getAddress();
                mBTDevices.get(i).fetchUuidsWithSdp();

                Log.d(TAG, "onItemClick: deviceName = " + deviceName);
                Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

                //create the bond.
                //NOTE: Requires API 17+? I think this is JellyBean
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                    Log.d(TAG, "Trying to pair with " + deviceName);
                    mBTDevices.get(i).createBond();

                    mBTDevice = mBTDevices.get(i);


                    startConnection();
                }
            }
          }
        );
        return view;
    }



    public void startConnection(){

        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(mBTDevice, uuid);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(bondReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(discoveryReceiver);
    }
}
