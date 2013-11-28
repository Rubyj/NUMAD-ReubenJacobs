package edu.neu.madcourse.dankreymer.trickystress;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class TrickyBluetoothFoundReceiver extends BroadcastReceiver {

	@Override
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
   }

}
