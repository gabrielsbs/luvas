package com.labbbio.luvas.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
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
import com.labbbio.luvas.LuvasApp;
import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;
import com.labbbio.luvas.ble.BluetoothLEService;
import com.labbbio.luvas.ble.DeviceControlActivity;
import com.labbbio.luvas.ble.BTLE_Device;
import com.labbbio.luvas.ble.DeviceListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BluetoothFragment extends Fragment{

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private int mConnectionState = STATE_DISCONNECTED;

    private int connectionState = STATE_DISCONNECTED;


    private static final String TAG = "BluetoothFragment";
    public static final int REQUEST_ENABLE_BT = 1;
    public static final int BTLE_SERVICES = 2;

    BluetoothDevice btDevice;

    UUID uuid =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothService bluetoothConnection;
    BluetoothAdapter bluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;

    public ArrayList<BTLE_Device> btDevices = new ArrayList<>();
    private ArrayList<BTLE_Device> temp;

    private HashMap<String, BTLE_Device> btDevicesHashMap = new HashMap<>();

    public DeviceListAdapter deviceListAdapter;

    ListView lvNewDevices;
    private TextView title;
    private String color;
    private int size;



    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        ((MainActivity) this.getActivity()).sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is carried out as per profile specifications.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;

            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }

            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));

        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }
        ((MainActivity) this.getActivity()).sendBroadcast(intent);
    }

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
        deviceListAdapter = new DeviceListAdapter(this.getActivity(), R.layout.device_adapter_view, btDevices);
        deviceListAdapter.setTextColor(color);
        lvNewDevices.setAdapter(deviceListAdapter);
        uuid =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        if(temp != null){
            btDevices = temp;
            deviceListAdapter = new DeviceListAdapter(this.getContext(), R.layout.device_adapter_view, btDevices);
            deviceListAdapter.setTextColor(color);
            lvNewDevices.setAdapter(deviceListAdapter);
        }



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothConnection = ((LuvasApp) this.getActivity().getApplication()).getBluetoothService();

        //LocalBroadcastManager.getInstance(getActivity()).registerReceiver(discoveryReceiver,
         //       new IntentFilter("Device_Added"));
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


    public boolean startConnection() {

        String address = btDevice.getAddress();
        Log.d(TAG, "Trying to connect");
        if (bluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");

            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            }
            else {
                return false;
            }
        }

        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }

        mBluetoothGatt = device.connectGatt(this.getContext(), true, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==BTLE_SERVICES){
            Log.d(TAG,"Finishing DeviceControl Activity");
            this.getActivity().finishActivity(BTLE_SERVICES);
        }
    }

    public final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                connectionState = STATE_CONNECTED;
                Log.i(TAG, "Connected to GATT server.");
                mBluetoothGatt.discoverServices();

            } else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
                connectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
            }else if(status != BluetoothGatt.GATT_SUCCESS){
                mBluetoothGatt.disconnect();
            }
        }

        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(connectionReceiver);
        ((MainActivity)getActivity()).stopScan();
       // LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(discoveryReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(connectionReceiver);
        //LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(discoveryReceiver);
        temp = btDevices;

    }

}
