package com.labbbio.bluetoothleapi;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;


public class BluetoothReceiver extends BroadcastReceiver {
    private static final String TAG = "BluetoothReceiver";

    public ArrayList<BTLE_Device> mBTDevices = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals("Device_Found")){
            Log.d(TAG, "onReceive: ACTION FOUND.");
            BTLE_Device device = intent.getParcelableExtra ("device");
            if(!mBTDevices.contains(device)){
                mBTDevices.add(device);
            }
            Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
            Intent newIntent = new Intent("Device_Added");
            newIntent.putExtra("arrayDevices", mBTDevices);
            LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
        }

        else if(action.equals("Bond_Change")){
            BTLE_Device mDevice = intent.getParcelableExtra("device");
            //3 cases:
            //case1: bonded already
            if (mDevice.getBluetoothDevice().getBondState() == BluetoothDevice.BOND_BONDED){
                Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                //inside BroadcastReceiver4
                Intent newIntent = new Intent("Bonded");
                newIntent.putExtra("bondedDevice", mDevice.getBluetoothDevice());
                LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
            }
            //case2: creating a bone
            if (mDevice.getBluetoothDevice().getBondState() == BluetoothDevice.BOND_BONDING) {
                Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
            }
            //case3: breaking a bond
            if (mDevice.getBluetoothDevice().getBondState() == BluetoothDevice.BOND_NONE) {
                Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
            }
        }
    }
}
