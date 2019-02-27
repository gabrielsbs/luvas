package com.labbbio.luvas;

import android.app.Application;

import com.labbbio.apiluvas.BluetoothService;

public class LuvasApp extends Application {
    private BluetoothService bluetoothService;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public BluetoothService getBluetoothService() {
        return bluetoothService;
    }

    public void setBluetoothService(BluetoothService mBluetoothConnectedThread) {
        this.bluetoothService = mBluetoothConnectedThread;
    }

}
