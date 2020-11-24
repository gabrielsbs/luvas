package com.labbbio.luvas.ble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.labbbio.luvas.R;
import com.labbbio.luvas.model.Device;

import java.util.ArrayList;


public class DeviceListAdapter extends ArrayAdapter<Device> {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Device> mDevices;
    private int  mViewResourceId;
    String color = "#000000";

    public DeviceListAdapter(Context context, int tvResourceId, ArrayList<Device> devices){
        super(context, tvResourceId,devices);
        this.mDevices = devices;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = tvResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(mViewResourceId, null);

        Device device = mDevices.get(position);

        if (device != null) {
            TextView deviceName =  convertView.findViewById(R.id.tvDeviceName);
            TextView deviceAdress = convertView.findViewById(R.id.tvDeviceAddress);

            if (deviceName != null) {
                deviceName.setText(device.getName());
            //    deviceName.setTextColor(Color.parseColor(color));
            }
            if (deviceAdress != null) {
                deviceAdress.setText(device.getAddress());
              //  deviceAdress.setTextColor(Color.parseColor(color));
            }
        }

        return convertView;
    }

    public void setTextColor(String color){
        this.color = color;
    }

}
