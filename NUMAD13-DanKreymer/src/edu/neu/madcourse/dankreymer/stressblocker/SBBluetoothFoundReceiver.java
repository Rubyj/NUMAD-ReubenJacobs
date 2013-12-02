package edu.neu.madcourse.dankreymer.stressblocker;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class SBBluetoothFoundReceiver extends BroadcastReceiver {

	@Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
           // Get the BluetoothDevice object from the Intent
            String deviceName = intent.getExtras().getString(BluetoothDevice.EXTRA_NAME);
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
          System.out.println("DEVICE: " + deviceName);
            Intent i = new Intent(context, SBBluetoothStoreService.class);
            i.putExtra(SBBluetoothStoreService.FOUND_DEVICE_ID, deviceName);
            context.startService(i);
        }
   }

}
