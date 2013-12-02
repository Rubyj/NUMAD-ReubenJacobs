package edu.neu.madcourse.dankreymer.stressblocker;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SBBluetoothStoreService extends Service {
	public static final String FOUND_DEVICE_ID = "FOUND_DEVICE_ID";
	
	private String foundDeviceId;
	private Context context;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		context = this;
		Bundle bundle = intent.getExtras();
		foundDeviceId = bundle.getString(FOUND_DEVICE_ID);
		
		String contact = SBSharedPreferences.getBTDeviceLink(this, foundDeviceId);
		if (contact.equals(""))
		{
			SBSharedPreferences.incrementBTDeviceCount(this, foundDeviceId);
			SBSharedPreferences.addToBTQueue(this, foundDeviceId);
		}
		else
		{
			SBSharedPreferences.putBTContactData(this, contact, Long.toString(System.currentTimeMillis()));
		}
		System.out.println("CONTACT: "  + contact);
		System.out.println("CONTACT DATA: "  + SBSharedPreferences.getContactData(this, contact));
		System.out.println("COUNT: "  + SBSharedPreferences.getBTDeviceCount(this, foundDeviceId));
		
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
