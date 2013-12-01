package edu.neu.madcourse.dankreymer.trickystress;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TrickyPhoneSurveyLaunchService extends Service{
	String phoneNumber;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		phoneNumber = intent.getExtras().getString(TrickyPhoneCallReceiver.PHONE_NUMBER);
		
        Intent i = new Intent(this, TrickySurvey.class);
        i.putExtra(TrickyPhoneCallReceiver.PHONE_NUMBER, phoneNumber);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
		
		
		//stopService(intent);
		
		return Service.START_NOT_STICKY;
	}
	

}
