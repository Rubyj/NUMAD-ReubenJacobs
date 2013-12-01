package edu.neu.madcourse.dankreymer.stressblocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

public class SBPhoneCallReceiver extends BroadcastReceiver {
	private static boolean notInCall = true;
	private static String PHONE_NUMBER = "PHONE_NUMBER";
	
	public void onReceive(Context context, Intent intent) {
	    String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
	    
	    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING) || (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) && notInCall)) {
	    	String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
	    	
	    	SBSharedPreferences.putCurrentNumber(context, number);
	    	
	        Intent i = new Intent(context, SBSurvey.class);
	        i.putExtra(PHONE_NUMBER, number);
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        context.startActivity(i);
	        
	        notInCall = false;

	    }
	    else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
	        Intent i = new Intent(context, SBSurvey.class);
	        i.putExtra(PHONE_NUMBER, SBSharedPreferences.getCurrentNumber(context));
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        context.startActivity(i);
	        
	        notInCall = true;

	    }
	}

}
