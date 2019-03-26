package com.labbbio.luvas;

import android.app.Application;
import android.support.design.widget.TabLayout;

import com.labbbio.apiluvas.BluetoothService;

public class LuvasApp extends Application {
    private BluetoothService bluetoothService;
    private String backgroundColor = "#FFFFFF";
    private String cardColor = "#FFFFFF";
    private String textColor = "#000000";
    private String highlightCardColor = "#FF6F00";
    private int fontSize = 15;

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getCardColor() {
        return cardColor;
    }

    public String getHighlightCardColor() {
        return highlightCardColor;
    }

    public void setHighlightCardColor(String highlightCardColor) {
        this.highlightCardColor = highlightCardColor;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }
}
