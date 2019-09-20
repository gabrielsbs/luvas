/**
 * BluetoothFragment: Fragment where the user can connect the app to the Luvas.
 * The view has the following elements:
 *  - List of Bluetooth devices found: the user can click in one item of the list to establish a
 *                                     connection with the device.
 */
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;
import com.labbbio.luvas.ble.BTLE_Device;
import com.labbbio.luvas.ble.DeviceListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BluetoothFragment extends Fragment{

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private int mConnectionState = STATE_DISCONNECTED;

    private int connectionState = STATE_DISCONNECTED;

    private static final String TAG = "BluetoothFragment";
    BluetoothDevice btDevice;
    UUID uuid;
    BluetoothAdapter bluetoothAdapter;
    public ArrayList<BTLE_Device> btDevices = new ArrayList<>();
    private ArrayList<BTLE_Device> temp;
    private HashMap<String, BTLE_Device> btDevicesHashMap = new HashMap<>();
    public DeviceListAdapter deviceListAdapter;
    ListView lvNewDevices;

    /**
     * Receiver the is called when the connection is established
     */
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
        lvNewDevices = view.findViewById(R.id.lvNewDevices);
        deviceListAdapter = new DeviceListAdapter(this.getActivity(), R.layout.device_adapter_view, btDevices);
        lvNewDevices.setAdapter(deviceListAdapter);
        uuid =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        if(temp != null){
            btDevices = temp;
            deviceListAdapter = new DeviceListAdapter(this.getContext(), R.layout.device_adapter_view, btDevices);
            lvNewDevices.setAdapter(deviceListAdapter);
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(connectionReceiver,
                new IntentFilter("CONNECTION_ESTABLISHED"));

        //ClickListen for the discovered devices list
        lvNewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //First cancel discovery because its very memory intensive.
                bluetoothAdapter.cancelDiscovery();

                Animation animation = new AlphaAnimation(0.3f, 1.0f);
                animation.setDuration(500);
                view.startAnimation(animation);

                Log.d(TAG, "onItemClick: You Clicked on a device.");
                String deviceName = btDevices.get(i).getName();
                String deviceAddress = btDevices.get(i).getAddress();
                btDevices.get(i).getBluetoothDevice().fetchUuidsWithSdp();

                Log.d(TAG, "onItemClick: deviceName = " + deviceName);
                Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

                //create the bond.
                //NOTE: Requires API 17+? I think this is JellyBean
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                    Log.d(TAG, "Trying to pair with " + deviceName);
                    btDevices.get(i).getBluetoothDevice().createBond();
                    btDevice = btDevices.get(i).getBluetoothDevice();
                    Log.d(TAG, "onItemClick: deviceName = " + btDevice.getName());
                    startConnection();
                }
            }
          }
        );
        return view;
    }

    public void startConnection(){
        Log.d(TAG,"Starting Connection");
        ((MainActivity) getActivity()).startConnection(btDevice.getAddress());
    }

    //Adds device to the list
    public void addDevice(BluetoothDevice device, int new_rssi){
        Log.d(TAG,"Add Device");
        String address = device.getAddress();
        if (!btDevicesHashMap.containsKey(address)) {
            BTLE_Device btleDevice = new BTLE_Device(device);
            btleDevice.setRSSI(new_rssi);

            btDevicesHashMap.put(address, btleDevice);
            btDevices.add(btleDevice);
        }
        else {
            btDevicesHashMap.get(address).setRSSI(new_rssi);
        }
        deviceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(connectionReceiver);
        ((MainActivity)getActivity()).stopScan();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(connectionReceiver);
        temp = btDevices;
    }
}
