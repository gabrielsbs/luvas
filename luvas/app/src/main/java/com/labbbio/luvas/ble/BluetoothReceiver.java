package com.labbbio.luvas.ble;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;


public class BluetoothReceiver extends BroadcastReceiver {
    private static final String TAG = "BluetoothReceiver";

    public ArrayList<BTLE_Device> mBTDevices = new ArrayList<>();
    private DeviceControlActivity activity;
    private boolean mConnected = false;


    public BluetoothReceiver(DeviceControlActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothLEService.ACTION_GATT_CONNECTED.equals(action)) {
            mConnected = true;
        }
        else if (BluetoothLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
            mConnected = false;
            Log.d(TAG, "Disconnected From Device");
            activity.finish();
        }
        else if (BluetoothLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            activity.updateServices();
        }
        else if (BluetoothLEService.ACTION_DATA_AVAILABLE.equals(action)) {

//            String uuid = intent.getStringExtra(Service_BTLE_GATT.EXTRA_UUID);
//            String data = intent.getStringExtra(Service_BTLE_GATT.EXTRA_DATA);

            activity.updateCharacteristic();
        }

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.d(TAG, "Bluetooth is off");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.d(TAG, "Bluetooth is turning off...");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.d(TAG, "Bluetooth is on");
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.d(TAG, "Bluetooth is turning on...");
                    break;
            }
        }
    }
}
