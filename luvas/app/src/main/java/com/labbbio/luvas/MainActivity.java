package com.labbbio.luvas;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.labbbio.apiluvas.BluetoothReceiver;
import com.labbbio.apiluvas.BluetoothService;
import com.labbbio.bluetoothleapi.BTLE_Device;
import com.labbbio.luvas.fragments.BluetoohFragment;
import com.labbbio.luvas.fragments.HomeFragment;
import com.labbbio.luvas.fragments.LearningFragment;
import com.labbbio.luvas.fragments.MessengerFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout menu_lateral;
    private NavigationView navigationView;

    private static final int HOME_FRAGMENT = 0;
    private static final int MESSENGER_FRAGMENT = 1;
    private static final int LEARNING_FRAGMENT = 2;
    private static final int BLUETOOTH_FRAGMENT = 3;


    private int currentFragment = HOME_FRAGMENT;
    private int lastFragment = HOME_FRAGMENT;

    private Scanner_BTLE scanner_btle;


    private static final String TAG = "MainActivity";

    BluetoothAdapter mBluetoothAdapter;
    public ArrayList<BTLE_Device> btDevices;

    private String luvasName = null;

    public String getLuvasName() {
        return luvasName;
    }

    public void setLuvasName(String luvasName) {
        this.luvasName = luvasName;
    }

    BluetoothReceiver broadcastReceiver = new BluetoothReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btDevices = new ArrayList<>();

        registerReceiver();

        scanner_btle = new Scanner_BTLE(this,3000, -90);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter.isEnabled())
            createBtConnection();

        menu_lateral = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTheme(R.style.AppTheme_NoActionBar_Font);
        changeMenuFontSize();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, menu_lateral, toolbar, R.string.navigation_opener, R.string.navigation_closer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes. Will hide the keyboard
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer closes. Will hide the keyboard
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };

        menu_lateral.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


    }


    private void registerReceiver() {

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("Device_Found"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, new IntentFilter("Bond_Change"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(auxiliarReceiver, new IntentFilter(BluetoothDevice.ACTION_UUID));
        registerReceiver(auxiliarReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        registerReceiver(auxiliarReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(auxiliarReceiver, BTIntent);

    }


    public void setLastFragment(int lastFragment) {
        this.lastFragment = lastFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                currentFragment = HOME_FRAGMENT;
                break;
            case R.id.nav_messenger:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessengerFragment()).commit();
                currentFragment = MESSENGER_FRAGMENT;
                break;
            case R.id.nav_treinamento:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LearningFragment()).commit();
                currentFragment = LEARNING_FRAGMENT;
                break;
            case R.id.nav_devices:
                currentFragment = BLUETOOTH_FRAGMENT;
                btFragmentStart();
                break;
        }
        menu_lateral.closeDrawer(GravityCompat.START);
        return true;
    }

    // Vai ser chamado quando o botão voltar for clickado. Volta para o fragmento home.
    @Override
    public void onBackPressed() {
        if (menu_lateral.isDrawerOpen(GravityCompat.START)) {
            menu_lateral.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment != HOME_FRAGMENT)
                homeFragmentStart();
            else
                super.onBackPressed();
        }


    }

    // As funções seguintes iniciam um fragmento, HomeFragment precisa delas.
    public void homeFragmentStart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);
        currentFragment = HOME_FRAGMENT;
    }

    public void btFragmentStart() {
        navigationView.setCheckedItem(R.id.nav_devices);
        if(!mBluetoothAdapter.isEnabled())
            enableDisableBT();
        else{
            startScan();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BluetoohFragment()).commit();
            currentFragment = BLUETOOTH_FRAGMENT;
        }
    }

    public void messengerFragmentStart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessengerFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_messenger);
        currentFragment = MESSENGER_FRAGMENT;
    }

    public void learningFragmentStart() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LearningFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_treinamento);
        currentFragment = LEARNING_FRAGMENT;
    }

    //Reinicia o fragmento atual para atualizar a mudança da fonte
    public void refreshFragment() {
        switch (currentFragment) {
            case HOME_FRAGMENT:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case MESSENGER_FRAGMENT:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessengerFragment()).commit();
                break;
            case LEARNING_FRAGMENT:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LearningFragment()).commit();
                break;
            case BLUETOOTH_FRAGMENT:
                btFragmentStart();
                break;
        }

    }

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

    private void changeMenuFontSize() {
        int size = ((LuvasApp)getApplication()).getFontSize();
        Spannable span = new SpannableString("Home");
        span.setSpan(new AbsoluteSizeSpan(size, true), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        navigationView.getMenu().findItem(R.id.nav_home).setTitle(span);
        span = new SpannableString("Mensagens");
        span.setSpan(new AbsoluteSizeSpan(size, true), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        navigationView.getMenu().findItem(R.id.nav_messenger).setTitle(span);
        span = new SpannableString("Exercícios");
        span.setSpan(new AbsoluteSizeSpan(size, true), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        navigationView.getMenu().findItem(R.id.nav_treinamento).setTitle(span);
        span = new SpannableString("Conectar Luvas");
        span.setSpan(new AbsoluteSizeSpan(size, true), 0, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        navigationView.getMenu().findItem(R.id.nav_devices).setTitle(span);
        span = new SpannableString("Bluetooth");
        span.setSpan(new AbsoluteSizeSpan(size, true), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

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

    // Resultado do pedido para ligar o bluetooth
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BluetoohFragment()).commit();
                currentFragment = BLUETOOTH_FRAGMENT;
            }
        }
    }

    //Registrador auxiliar usado para tratar eventos relacionados ao bluetooth
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
                        Log.d(TAG, "Creating bt connection");
                        createBtConnection();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        if (currentFragment == HOME_FRAGMENT) {
                            HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            luvasName = null;
                            fragment.changeCardText();
                        } else if (currentFragment == LEARNING_FRAGMENT) {
                            LearningFragment fragment = (LearningFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            luvasName = null;
                            fragment.changeCardText();
                        }
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
                    HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    luvasName = null;
                    fragment.changeCardText();
                } else if (currentFragment == LEARNING_FRAGMENT) {
                    LearningFragment fragment = (LearningFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    luvasName = null;
                    fragment.changeCardText();
                }
            }


        }
    };

    public void startScan(){
        Log.d(TAG,"Starting scan");

        scanner_btle.start();

    }

    public void stopScan(){
        Log.d(TAG,"Stopping scan");

        scanner_btle.stop();

    }


    public void createBtConnection() {

        BluetoothService mBluetoothConnection = new BluetoothService(this);
        ((LuvasApp) this.getApplication()).setBluetoothService(mBluetoothConnection);

    }



    public void btnDiscover() {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();


            mBluetoothAdapter.startDiscovery();
            IntentFilter it = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(auxiliarReceiver, it);
        }
        if (!mBluetoothAdapter.isDiscovering()) {

            //check BT permissions in manifest

            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter it = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(auxiliarReceiver, it);

        }
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
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            }
            if (permissionCheck != 0) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
                }
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "OnDestroy");
        super.onDestroy();
        stopScan();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(auxiliarReceiver);
        unregisterReceiver(auxiliarReceiver);
    }

}

