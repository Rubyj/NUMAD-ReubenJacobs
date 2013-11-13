package edu.neu.madcourse.dankreymer.trickystress;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import edu.neu.madcourse.dankreymer.multiplayer.DabbleM;
import edu.neu.mhealth.api.KeyValueAPI;

import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class TrickyBluetoothService extends Service{

	private static long bluetoothCutoff = 12000; //12 seconds
	private Handler handler;
	private BroadcastReceiver receiver;
	private Context context;
	private IntentFilter filter;
	private BluetoothAdapter mBluetoothAdapter;
	private Intent startIntent;
	
	@Override
	public void onCreate()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		context = this;
		receiver = new TrickyBluetoothFoundReceiver();
		filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		handler = new Handler()
		{
			 @Override
			    public void handleMessage(Message msg) {
				 	context.unregisterReceiver(receiver);
				 	context.stopService(startIntent);
			    }
		};
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onDestroy()
	{
		mBluetoothAdapter.disable();
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        if (mBluetoothAdapter == null) {
        	return Service.START_NOT_STICKY;
        }
        startIntent = intent;
        
        mBluetoothAdapter.enable();
       
        while (mBluetoothAdapter.getState() == mBluetoothAdapter.STATE_TURNING_ON)
        {
        	//Wait a bit for the bluetooth scan to be ready!
        	//TODO: probably a better way to do this.
        }
        
        //Go!
        mBluetoothAdapter.startDiscovery();
        
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                   // Get the BluetoothDevice object from the Intent
                    String deviceName = intent.getExtras().getString(BluetoothDevice.EXTRA_NAME);
                    TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    String s = tManager.getDeviceId();
                   
                    Intent i = new Intent(context, TrickyBluetoothStoreService.class);
                    i.putExtra(TrickyBluetoothStoreService.DEVICE_ID, tManager.getDeviceId());
                    i.putExtra(TrickyBluetoothStoreService.FOUND_DEVICE_ID, deviceName);
                    context.startService(i);
                }
           }};
        
		registerReceiver(receiver, filter);
		handler.sendMessageDelayed(new Message(), bluetoothCutoff);
		
		return Service.START_NOT_STICKY;
	}
	

}
