package edu.neu.madcourse.dankreymer.trickystress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

public class TrickyPhoneCallReceiver extends BroadcastReceiver {
	public static final String PHONE_NUMBER = "PHONE_NUMBER";
	private static boolean notInCall = true;
	
	public void onReceive(Context context, Intent intent) {
	    String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
	    
	    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING) || (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) && notInCall)) {
	    	String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
	    	
	    	SharedPreferences.Editor editor = context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();
	    	editor.putString(PHONE_NUMBER, number);
	    	editor.commit();
	    	
	        Intent i = new Intent(context, TrickySurvey.class);
	        i.putExtra(PHONE_NUMBER, number);
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        context.startActivity(i);
	        
	        notInCall = false;

	    }
	    else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
	    	SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
	        Intent i = new Intent(context, TrickySurvey.class);
	        i.putExtra(PHONE_NUMBER, prefs.getString(PHONE_NUMBER, ""));
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        context.startActivity(i);
	        
	        notInCall = true;

	    }
	}

}
