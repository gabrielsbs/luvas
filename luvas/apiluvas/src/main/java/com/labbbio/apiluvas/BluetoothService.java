package com.labbbio.apiluvas;

import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by User on 12/21/2016.
 */

public class BluetoothService extends Service {
    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "Luvas";

    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter AppAdapter;
    Context context;

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice luvas;
    private UUID luvasUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread connectedThread;

    public BluetoothService(Context contextIn) {
        context = contextIn;
        AppAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /* Thread opera como um servidor: Após fica iniciada esperando por uma conexão. Fica rodando até que uma conexão
        é aceita(ou quando é cancelada)
     */
    private class AcceptThread extends Thread {

        // Server socket local
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            // Cria o servidor
            try{
                tmp = AppAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            mmServerSocket = tmp;
        }

        public void run(){
            Log.d(TAG, "run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try{

                Log.d(TAG, "run: RFCOM server socket start.....");
                // Essa função bloqueia a operação, retornando apenas quando
                // consegue se conectar ou por causa de uma exceção
                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection.");

            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            if(socket != null){
                connected(socket,luvas);
            }

            Log.i(TAG, "END mAcceptThread ");

        }

        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
            }
        }

    }

    /**
     * Essa thread roda equanto tenta se conectar com um dispostitvo.
     * Roda sem parar tanto quando há sucesso ou falha.
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket luvasSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started.");
            luvas = device;
            luvasUUID = uuid;
        }


        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread ");

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                        +luvasUUID );
                tmp = luvas.createRfcommSocketToServiceRecord(luvasUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }

            luvasSocket = tmp;

            // Cancela a discoberta de novos dispostitivos, para acelerar a conexão
            AppAdapter.cancelDiscovery();

            // Faz a conexão com o Socket do luvas

            try {
                // Essa função bloqueia a operação, retornando apenas quando
                // consegue se conectar ou por causa de uma exceção
                luvasSocket.connect();

                Log.d(TAG, "run: ConnectThread connected.");
            } catch (IOException e) {
                // Close the socket
                try {
                    luvasSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE  + e);
            }

            connected(luvasSocket,luvas);
        }
        public void cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                luvasSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of luvasSocket in Connectthread failed. " + e.getMessage());
            }
        }
    }



   
    

    /**
     * Thread responsável por manter a conexão Bluetooth, mandar e receber dados.
     **/
    private class ConnectedThread extends Thread {
        private final BluetoothSocket luvasSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");

            luvasSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            String name = luvas.getName();
            Intent connected_sucessful = new Intent("CONNECTION_ESTABLISHED");
            connected_sucessful.putExtra("Device_Name",name);
            LocalBroadcastManager.getInstance(context).sendBroadcast(connected_sucessful);

            //dismiss the progressdialog when connection is established
            try{
                mProgressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


            try {
                tmpIn = luvasSocket.getInputStream();
                tmpOut = luvasSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];

            int bytesReaded; // bytesReaded lidos

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                // Read from the InputStream
                try {
                    bytesReaded = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytesReaded);
                    Log.d(TAG, "InputStream: " + incomingMessage);

                    Intent incommingMessageIntent = new Intent("incommingMessage");
                    incommingMessageIntent.putExtra("message",incomingMessage);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(incommingMessageIntent);

                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                    break;
                }
            }
        }

        //Call this from the main activity to send data to the remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
            }
        }

        /* Chamada pela MainActivity para encerrar a conexão*/
        public void cancel() {
            try {
                luvasSocket.close();
            } catch (IOException e) { }
        }
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */


    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    /**
     AcceptThread starts and sits waiting for a connection.
     Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
     **/

    public void startClient(BluetoothDevice device,UUID uuid){
        Log.d(TAG, "startClient: Started.");

        //init progress dialog
        mProgressDialog = ProgressDialog.show(context,"Connecting Bluetooth"
                ,"Please Wait...",true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    private void connected(BluetoothSocket luvasSocket, BluetoothDevice luvas) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        connectedThread = new ConnectedThread(luvasSocket);
        connectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        //perform the write
        connectedThread.write(out);
    }

}
