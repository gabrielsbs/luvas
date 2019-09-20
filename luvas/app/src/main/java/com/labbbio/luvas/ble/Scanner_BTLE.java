/**
 * Scanner for bluetooth le devices. Only consider the devices with a minimal signal strength
 */

package com.labbbio.luvas.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;
import com.labbbio.luvas.fragments.BluetoothFragment;

public class Scanner_BTLE {

    private MainActivity mainActivity;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private String TAG = "SCANNER_BTLE";

    private static final int BLUETOOTH_FRAGMENT = 3;

    private long scanPeriod;
    private int signalStrength;

    public Scanner_BTLE(MainActivity mainActivity, long scanPeriod, int signalStrength) {
        this.mainActivity = mainActivity;

        mHandler = new Handler();

        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager =
                (BluetoothManager) this.mainActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    public boolean isScanning() {
        return mScanning;
    }

    public void start() {
        scanLeDevice(true);
    }

    public void stop() {
        scanLeDevice(false);
    }


    private void scanLeDevice(final boolean enable) {
        if (enable && !mScanning) {

            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    mainActivity.stopScan();
                }
            }, scanPeriod);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
        else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {

                    final int new_rssi = rssi;
                    if (rssi > signalStrength) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "deviceName = " + device.getName());
                                Log.d(TAG, "RSSI = " + rssi);
                                if(mainActivity.getCurrentFragment() == BLUETOOTH_FRAGMENT)
                                    ((BluetoothFragment)mainActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container)).addDevice(device, new_rssi);
                            }
                        });
                    }
                }
            };

}
