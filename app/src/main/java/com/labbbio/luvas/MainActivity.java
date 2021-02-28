/*
--------------------------------------------------------------
-------------- Glover's Messenger App ------------------------
--------------------------------------------------------------
                                      Gabriel Santos, Feb/2019
*/

/**
 * The application is structured in 4 major Fragments:
 *  - HomeFragment: Initial fragment, only has the menu to open the other Fragments
 *  - BluetoothFragment: Fragment where the user can connect the app to the Luvas
 *  - MessengerFragment: Fragment where the user can communicate with the Luvas
 *  - LearningFragment: Fragment that contains the exercises for the deafblinds. It's divided in 2 Fragments: PosLingFragment and PreLingFragment
 * The app uses a SQL database to store, even when the app is closed, the last exercises done by the user.
 */

package com.labbbio.luvas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Toast;

import com.labbbio.luvas.ble.BluetoothLeService;
import com.labbbio.luvas.ble.Scanner_BTLE;

import com.labbbio.luvas.fragments.BluetoothFragment;
import com.labbbio.luvas.fragments.ExerciseFragment;
import com.labbbio.luvas.fragments.HomeFragment;
import com.labbbio.luvas.fragments.LearningFragment;
import com.labbbio.luvas.fragments.MessengerFragment;
import com.labbbio.luvas.fragments.OptionsDialog;
import com.labbbio.luvas.model.Device;
import com.labbbio.luvas.model.Exercise;
import com.labbbio.luvas.utils.PosLingExercisesLoader;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private static final int HOME_FRAGMENT = 0;
    private static final int MESSENGER_FRAGMENT = 1;
    private static final int LEARNING_FRAGMENT = 2;
    private static final int BLUETOOTH_FRAGMENT = 3;
    private static final int EXERCISE_FRAGMENT = 4;

    private static final int VOICE_OPTION = 5;
    private static final int GESTURE_OPTION = 6;

    private int currentFragment = HOME_FRAGMENT;
    private int lastFragment = HOME_FRAGMENT;

    private int lastViewLearning = 0;
    private int currentQuestionNumeber;
    private String currentQuestionType;

    private ArrayList<Exercise> posExerciseItems = new ArrayList<>();

    private Scanner_BTLE scanner_btle;

    private static final String TAG = "MainActivity";

    BluetoothAdapter mBluetoothAdapter;
    public ArrayList<Device> btDevices;

    private String luvasName = null;
    private String mDeviceAddress;

    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private boolean isBound = false;
    private int answerOption = GESTURE_OPTION;

    /** Bluetooth LE service, implemented in ble/BluetoothLeService */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(TAG,"Service connected");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            isBound = false;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.d(TAG,"GATT Connected");
                mConnected = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                Log.d(TAG,"Services discovered");
                homeFragmentStart();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //displayData(intent.getStringExtra(mBluetoothLeService.EXTRA_DATA));
                Log.d(TAG,"Data Available");

                String incomingMessage = intent.getStringExtra(EXTRA_DATA);
                Log.d(TAG, "InputStream: " + incomingMessage);

                Intent incomingMessageIntent = new Intent("incomingMessage");
                incomingMessageIntent.putExtra("message",incomingMessage);
                LocalBroadcastManager.getInstance(context).sendBroadcast(incomingMessageIntent);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            finish();
        }*/

        checkBTPermissions();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // btDevices = new ArrayList<>();

        registerReceiver();

        posExerciseItems = PosLingExercisesLoader.createExerciseList();
        //scanner_btle = new Scanner_BTLE(this,3000, -75);
        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        setTheme(R.style.AppTheme_NoActionBar_Font);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }


    /** Toolbar Menu, has only one option: open response method for reception exercises selection*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_conf, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.opt_menu:
                openDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**Opens the response method selection*/
    private void openDialog(){
        OptionsDialog optionsDialog = new OptionsDialog();
        optionsDialog.show(getSupportFragmentManager(),"Options Dialog");
    }

    /** Set the selected type. If the current fragment is the the Exercise Fragment, the fragment is refreshed */
    public void setOption(int option){
        answerOption = option;
        Log.d(TAG,"Option Selected");
        if(currentFragment == EXERCISE_FRAGMENT){
            refreshExerciseFragment();
        }
    }

    /** Function to register the BroadcastReceiver*/
    private void registerReceiver() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(auxiliarReceiver, new IntentFilter(BluetoothDevice.ACTION_UUID));
        registerReceiver(auxiliarReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        registerReceiver(auxiliarReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(auxiliarReceiver, BTIntent);

    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    /**Set the lastFragment: Exercise, Messenger, Bluetooth, Learning or Home*/
    public void setLastFragment(int lastFragment) {
        this.lastFragment = lastFragment;
    }


    /** This function is called when the back button is pressed. The possibles situations are:
     *  - If current fragment is Exercise Fragment, it goes back to the Learning Fragment.
     *  - If current fragment is Home Fragment, the app is closed.
     *  - Goes back to Home Fragment otherwise.
    */
    @Override
    public void onBackPressed() {
            if (currentFragment != HOME_FRAGMENT && currentFragment != EXERCISE_FRAGMENT)
                homeFragmentStart();
            else if(currentFragment == EXERCISE_FRAGMENT)
                learningFragmentStart();
            else
                super.onBackPressed();
    }

    // The following functions start a Fragment. They are needed for the Home Fragment
    public void homeFragmentStart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        currentFragment = HOME_FRAGMENT;
    }

    @SuppressLint("MissingPermission")
    public void btFragmentStart() {
        if(!mBluetoothAdapter.isEnabled())
            enableDisableBT();
        else{
            startScan();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BluetoothFragment()).commit();
            currentFragment = BLUETOOTH_FRAGMENT;
        }
    }

    public void messengerFragmentStart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessengerFragment()).commit();
        currentFragment = MESSENGER_FRAGMENT;
    }

    public void learningFragmentStart() {
        LearningFragment f =  new LearningFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,f).commit();
        currentFragment = LEARNING_FRAGMENT;
    }

    public void exerciseFragmentStart(String ExerciseType, int questionNumber){
        currentQuestionType = ExerciseType;
        currentQuestionNumeber = questionNumber;
        Bundle bundle = new Bundle();
        bundle.putString("ExerciseType",ExerciseType);
        bundle.putInt("questionNumber",questionNumber);
        ExerciseFragment exerciseFragment =new ExerciseFragment();
        exerciseFragment.setArguments(bundle);

        if(currentFragment == EXERCISE_FRAGMENT){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.fragment_container,exerciseFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,exerciseFragment).commit();
            currentFragment = EXERCISE_FRAGMENT;
        }
    }

    // Refresh the Exercise Fragment. Called when the user changes the response method
    public void refreshExerciseFragment(){
        Bundle bundle = new Bundle();
        bundle.putString("ExerciseType",currentQuestionType);
        bundle.putInt("questionNumber",currentQuestionNumeber);
        ExerciseFragment exerciseFragment =new ExerciseFragment();
        exerciseFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,exerciseFragment).commit();
        currentFragment = EXERCISE_FRAGMENT;
    }

    // Open the last fragment
    public void goLastFragment() {
        switch (lastFragment) {
            case HOME_FRAGMENT:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                currentFragment = HOME_FRAGMENT;
                break;
            case MESSENGER_FRAGMENT:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessengerFragment()).commit();
                currentFragment = MESSENGER_FRAGMENT;
                break;
            case LEARNING_FRAGMENT:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LearningFragment()).commit();
                currentFragment = LEARNING_FRAGMENT;
                break;
            case BLUETOOTH_FRAGMENT:
                currentFragment = BLUETOOTH_FRAGMENT;
                btFragmentStart();
                break;
        }

    }


    // Function for enabling/disabling the cellphone's bluetooth
    @SuppressLint("MissingPermission")
    public void enableDisableBT() {
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, 1);
        }
        if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();
        }

    }

    // This function is used to get the result of the enabling bluetooth request. If is ok, it starts the bluetooth fragment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.d(TAG, "Request Received");
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Permission Denied");
            }
            else if(resultCode == RESULT_OK){
                startScan();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BluetoothFragment()).commit();
                currentFragment = BLUETOOTH_FRAGMENT;
            }
        }
    }

    private BroadcastReceiver auxiliarReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                Log.d(TAG, "onReceive: ACTION FOUND.");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Intent discoverDevicesIntent = new Intent("Device_Found");
                discoverDevicesIntent.putExtra("device", device);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(discoverDevicesIntent);

            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                Log.d(TAG, "onReceive:BOND CHANGED.");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Intent discoverDevicesIntent = new Intent("Bond_Change");
                discoverDevicesIntent.putExtra("device", device);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(discoverDevicesIntent);
            } else if (action.equals(BluetoothDevice.ACTION_UUID)) {

                BluetoothDevice deviceExtra = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                Log.d(TAG, "DeviceExtra address - " + deviceExtra.getAddress());
                if (uuidExtra != null) {
                    for (Parcelable p : uuidExtra) {
                        Log.d(TAG, "uuidExtra - " + p);
                    }
                } else {
                    Log.d(TAG, "uuidExtra is still null");
                }
            }

            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }

            if (action.equals(mBluetoothAdapter.ACTION_REQUEST_ENABLE)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch (state) {

                }
            }

            if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                Log.d(TAG, "auxiliarReceiver: Device Disconnected");
                if (currentFragment == HOME_FRAGMENT) {
                    Log.d(TAG, "HomeFragment");
//                    HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    luvasName = null;
                }
            }


        }
    };

    /**Functions to start/stop the bluetooth scan*/
    public void startScan(){
        Log.d(TAG,"Starting scan");
        scanner_btle.start();

    }

    public void stopScan(){
        Log.d(TAG,"Stopping scan");
        scanner_btle.stop();

    }

    public void startConnection(String address){
        Log.d(TAG,"Starting Connection");
        mDeviceAddress = address;
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        this.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void sendMessage(String message){
        if(mConnected){
            mBluetoothLeService.writeCharacteristic(message);
        }
    }

    //Getters and Setters

    public int getCurrentFragment(){
        return currentFragment;
    }

    public int getAnswerOption() {
        return answerOption;
    }

    public String getLuvasName() {
        return luvasName;
    }

    public void setLuvasName(String luvasName) {
        this.luvasName = luvasName;
    }

    public void setLastViewLearning(int lastViewLearning){
        this.lastViewLearning = lastViewLearning;
    }

    public int getLastViewLearning(){
        return lastViewLearning;
    }

    public ArrayList<Exercise> getPosExerciseItemsItems() {
        return posExerciseItems;
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     * <p>
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */

    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
                permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
                permissionCheck += this.checkSelfPermission("Manifest.permission.BLUETOOTH");
                permissionCheck += this.checkSelfPermission("Manifest.permission.BLUETOOTH_ADMIN");
            }
            if (permissionCheck != 0) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, 1001); //Any number
                }
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "OnDestroy");
        stopScan();
        if(isBound){
            mBluetoothLeService.close();
            unbindService(mServiceConnection);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(auxiliarReceiver);
        unregisterReceiver(auxiliarReceiver);
        unregisterReceiver(mGattUpdateReceiver);
        super.onDestroy();
    }

}

