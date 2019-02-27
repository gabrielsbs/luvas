package com.labbbio.luvas;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.labbbio.apiluvas.BluetoothReceiver;
import com.labbbio.apiluvas.BluetoothService;
import com.labbbio.luvas.fragments.BluetoohFragment;
import com.labbbio.luvas.fragments.HomeFragment;
import com.labbbio.luvas.fragments.LearningFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout menu_lateral;
    private NavigationView navigationView;

    private static final String TAG = "MainActivity";

    BluetoothAdapter mBluetoothAdapter;

    Switch btSwitch;

    BluetoothReceiver broadcastReceiver = new BluetoothReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerReceiver();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        menu_lateral = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, menu_lateral,toolbar, R.string.navigation_opener, R.string.navigation_closer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };

        menu_lateral.addDrawerListener(toggle);
        toggle.syncState();




        navigationView.getMenu().findItem(R.id.nav_font).setActionView(new Switch(this));
        Switch fontSwitch = (Switch) navigationView.getMenu().findItem(R.id.nav_font).getActionView();

        fontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

                if (isChecked) {
                    setTheme(R.style.AppTheme_NoActionBar_Font);
                    Spannable span = new SpannableString("Home");
                    span.setSpan(new AbsoluteSizeSpan(35,true),0,4,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_home).setTitle(span);
                    span = new SpannableString("Exercícios");
                    span.setSpan(new AbsoluteSizeSpan(35,true),0,10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_treinamento).setTitle(span);
                    span = new SpannableString("Conectar Luvas");
                    span.setSpan(new AbsoluteSizeSpan(35,true),0,14,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_devices).setTitle(span);
                    span = new SpannableString("Bluetooth");
                    span.setSpan(new AbsoluteSizeSpan(35,true),0,9,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_bt).setTitle(span);
                    span = new SpannableString("Acessibilidade");
                    span.setSpan(new AbsoluteSizeSpan(35,true),0,14,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_acessibilidade).setTitle(span);
                    span = new SpannableString("Fonte");
                    span.setSpan(new AbsoluteSizeSpan(35,true),0,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_font).setTitle(span);
                } else {
                    setTheme(R.style.AppTheme_NoActionBar);
                    Spannable span = new SpannableString("Home");
                    span.setSpan(new AbsoluteSizeSpan(15,true),0,4,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_home).setTitle(span);
                    span = new SpannableString("Exercícios");
                    span.setSpan(new AbsoluteSizeSpan(15,true),0,10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_treinamento).setTitle(span);
                    span = new SpannableString("Conectar Luvas");
                    span.setSpan(new AbsoluteSizeSpan(15,true),0,14,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_devices).setTitle(span);
                    span = new SpannableString("Ligar Bluetooth");
                    span.setSpan(new AbsoluteSizeSpan(15,true),0,14,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_bt).setTitle(span);
                    span = new SpannableString("Acessibilidade");
                    span.setSpan(new AbsoluteSizeSpan(15,true),0,14,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_acessibilidade).setTitle(span);
                    span = new SpannableString("Aumentar Fonte");
                    span.setSpan(new AbsoluteSizeSpan(15,true),0,14,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    navigationView.getMenu().findItem(R.id.nav_font).setTitle(span);
                }
            }
        });
        navigationView.getMenu().findItem(R.id.nav_bt).setActionView(new Switch(this));
        btSwitch = (Switch) navigationView.getMenu().findItem(R.id.nav_bt).getActionView();

        btSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(!mBluetoothAdapter.isEnabled()) {
                        enableDisableBT();
                    }
                } else {
                    enableDisableBT();
                }
            }
        });

        if(mBluetoothAdapter.isEnabled()){
            btSwitch.setChecked(true);
        }

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


    }



    private void registerReceiver(){

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("Device_Found"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver,new IntentFilter("Bond_Change"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(auxiliarReceiver,new IntentFilter( BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(auxiliarReceiver,new IntentFilter( BluetoothDevice.ACTION_UUID));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_treinamento:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LearningFragment()).commit();
                break;
            case R.id.nav_bt:
                return true;
            case R.id.nav_devices:
                if(!mBluetoothAdapter.isEnabled()){
                    btSwitch.toggle();
                }
                btnEnableDisable_Discoverable();
                btnDiscover();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BluetoohFragment()).commit();
                break;
            case R.id.nav_acessibilidade:
                Toast.makeText(this, "Acessibilidade", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_font:
                return true;
        }
        menu_lateral.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(menu_lateral.isDrawerOpen(GravityCompat.START)) {
            menu_lateral.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    public void enableDisableBT(){
        if(mBluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(auxiliarReceiver, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(auxiliarReceiver, BTIntent);
        }

    }

    private BroadcastReceiver auxiliarReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();


            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                Log.d(TAG, "onReceive: ACTION FOUND.");
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                Intent discoverDevicesIntent = new Intent("Device_Found");
                discoverDevicesIntent.putExtra("device",device);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(discoverDevicesIntent);

            }
            else if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                Log.d(TAG, "onReceive:BOND CHANGED.");
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                Intent discoverDevicesIntent = new Intent("Bond_Change");
                discoverDevicesIntent.putExtra("device",device);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(discoverDevicesIntent);
            }

            else if (action.equals(BluetoothDevice.ACTION_UUID)){

                BluetoothDevice deviceExtra = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                Log.d(TAG,"DeviceExtra address - " + deviceExtra.getAddress());
                if (uuidExtra != null) {
                    for (Parcelable p : uuidExtra) {
                        Log.d(TAG,"uuidExtra - " + p);
                    }
                } else {
                    Log.d(TAG,"uuidExtra is still null");
                }
            }

            if(action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch(state){
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG,"Creating bt connection");
                        createBtConnection();
                        break;
                    case BluetoothAdapter.STATE_OFF:

                        break;
                }
            }


        }
    };

    public void createBtConnection(){

        BluetoothService mBluetoothConnection = new BluetoothService(this);
        ((LuvasApp) this.getApplication()).setBluetoothService(mBluetoothConnection);

    }

    public void btnEnableDisable_Discoverable() {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

    }


    public void btnDiscover() {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();


            mBluetoothAdapter.startDiscovery();
            IntentFilter it = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(auxiliarReceiver,it);
        }
        if(!mBluetoothAdapter.isDiscovering()){

            //check BT permissions in manifest

            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter it = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(auxiliarReceiver,it);

        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
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
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(auxiliarReceiver);
    }
}

