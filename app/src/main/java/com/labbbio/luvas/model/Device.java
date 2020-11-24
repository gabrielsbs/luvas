package com.labbbio.luvas.model;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;

public class Device {

    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public Device(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    @SuppressLint("MissingPermission")
    public String getName() {
        return bluetoothDevice.getName();
    }

    public void setRSSI(int rssi) {
        this.rssi = rssi;
    }

    public int getRSSI() {
        return rssi;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }
}
