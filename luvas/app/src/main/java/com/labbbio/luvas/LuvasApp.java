package com.labbbio.luvas;

import android.app.Application;
import android.support.design.widget.TabLayout;

import com.labbbio.apiluvas.BluetoothService;

public class LuvasApp extends Application {
    private BluetoothService bluetoothService;

    private int fontSize = 15;

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

    public int getFontSize(){
        return fontSize;
    }

    public void setFontSize(int size){
        this.fontSize = size;
    }

}
