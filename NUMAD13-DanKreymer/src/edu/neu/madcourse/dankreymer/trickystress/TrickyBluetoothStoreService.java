package edu.neu.madcourse.dankreymer.trickystress;

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
import edu.neu.madcourse.dankreymer.keys.Keys;
import edu.neu.madcourse.dankreymer.keys.ServerError;

public class TrickyBluetoothStoreService extends Service{
	public static final String DEVICE_ID = "DEVICE_ID";
	public static final String FOUND_DEVICE_ID = "FOUND_DEVICE_ID";
	
	private String deviceId;
	private String foundDeviceId;
	private Context context;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		context = this;
		Bundle bundle = intent.getExtras();
		foundDeviceId = bundle.getString(FOUND_DEVICE_ID);
		deviceId = bundle.getString(DEVICE_ID);
		foundDeviceId = bundle.getString(FOUND_DEVICE_ID);
		
		new StoreBluetoothDataTask().execute();
		
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    private final class StoreBluetoothDataTask extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
        	String checkCurrent = Keys.get(Keys.TrickyDeviceSeenKey(deviceId, foundDeviceId));
        	int current;
        	
        	if (checkCurrent.equals(ServerError.NO_CONNECTION.getText()))
        	{
        		current = 0;
        	}
        	else if (checkCurrent.equals(ServerError.NO_SUCH_KEY.getText()))
        	{
        		Keys.put(Keys.TrickyDeviceSeenKey(deviceId, foundDeviceId), "1");
        		current = 1;
        	}
        	else
        	{
        		current = Integer.parseInt(checkCurrent);
        		if (current != 5)
        		{
        			current = current + 1;
        			Keys.put(Keys.TrickyDeviceSeenKey(deviceId, foundDeviceId), Integer.toString(current));
        		}
        	}
        	
        	return current;
        }
        
        @Override
        protected void onPostExecute(Integer ret) {
        	if (ret == 5)
        	{
                Intent intent = new Intent(context, TrickyStressBTResult.class);
                intent.putExtra(FOUND_DEVICE_ID, foundDeviceId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                context.stopService(intent);
        	}
        }
    }
}
