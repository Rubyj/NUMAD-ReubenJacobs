package edu.neu.madcourse.dankreymer.trickystress;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import edu.neu.madcourse.dankreymer.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class TrickyStressBT extends Activity{
    
    final int REQUEST_ENABLE_BT = 1;
    private HashMap<String, Integer> mArray;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tricky_stress_bt);
        
        this.mArray = new HashMap<String, Integer>();
        
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        
        colorButtons();     
    }
    
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
    
    private void colorButtons()
    {
        Button b1 = (Button) findViewById(R.id.tricky_survey1);
        b1.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey1), PorterDuff.Mode.MULTIPLY);
        Button b2 = (Button) findViewById(R.id.tricky_survey2);
        b2.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey2), PorterDuff.Mode.MULTIPLY);
        Button b3 = (Button) findViewById(R.id.tricky_survey3);
        b3.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey3), PorterDuff.Mode.MULTIPLY);
        Button b4 = (Button) findViewById(R.id.tricky_survey4);
        b4.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey4), PorterDuff.Mode.MULTIPLY);
        Button b5 = (Button) findViewById(R.id.tricky_survey5);
        b5.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey5), PorterDuff.Mode.MULTIPLY);
        Button b6 = (Button) findViewById(R.id.tricky_survey6);
        b6.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey6), PorterDuff.Mode.MULTIPLY);
        Button b7 = (Button) findViewById(R.id.tricky_survey7);
        b7.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey7), PorterDuff.Mode.MULTIPLY);
    }
    
    public void onSurveyClick(View v)
    {
        finish();
    }
    
    public void onSurveyBTClick(View v) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //device does not support BT
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        mBluetoothAdapter.startDiscovery();
        
    }
    
    public boolean isNetworkOnline() {
        boolean status=false;
           try{
               ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
               NetworkInfo netInfo = cm.getNetworkInfo(0);
               if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                   status= true;
               }else {
                   netInfo = cm.getNetworkInfo(1);
                   if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                       status= true;
               }
           }catch(Exception e){
               e.printStackTrace();  
               return false;
           }
           return status;
    }
    
    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
       public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
               // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("testReceive", device.getName() + "\n" + device.getAddress());
                // Add the name and address to an array adapter to show in a ListView
                TrickyStressBT.this.mArray.put(device.getName(), 0);
                
                TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String deviceId = tManager.getDeviceId();
                new StoreDataTask().execute(deviceId, device.getName());
            }
       }
    };
       
    private final class StoreDataTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            if (TrickyStressBT.this.isNetworkOnline()) {
                if (KeyValueAPI.isServerAvailable()) {
                    String numSeenString = KeyValueAPI.get("sloth_nation", "fromunda", strings[0] + "-" + strings[1]);
                    Log.d("numSeenString:", numSeenString);
                    if (!numSeenString.contains("Error") && !numSeenString.contains("ERROR")) {
                        Integer numSeen = Integer.parseInt(numSeenString);
                        numSeen++;
                        KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], numSeen.toString());
                        TrickyStressBT.this.mArray.put(strings[1], numSeen);
                        
                        if (numSeen >= 5) {

                        }
                    } else {
                        KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], "0");
                        TrickyStressBT.this.mArray.put(strings[1], 0);
                    }
                }
            }
            return null;
        }
        
    } 
}
