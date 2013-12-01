package edu.neu.madcourse.dankreymer.stressblocker;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class SBPeriodicSurveyService extends Service{

	private static long FOUR = 14400000; //4 hours
	private static long EIGHT = 28800000; // 8
	private static long TWELVE = 43200000; //etc
	
	private long interval;
	private Timer timer;
	private Context context;
	private long updateInterval;
	private Intent intent;
	
	@Override
	public void onCreate()
	{
		String intervalString = SBSharedPreferences.getPeriodicInterval(this);
		setInterval(intervalString);
		
		updateInterval = 60000; //Update interval every minute (so we know when to kill this service).
		
		timer = new Timer();
		context= this;
	}
	
	@Override
	public int onStartCommand(Intent i, int flags, int startId) {
		intent = i;
		timer.scheduleAtFixedRate(launchPeriodicSurvey(), new Date(), interval);
		timer.scheduleAtFixedRate(updateSettings(), new Date(), updateInterval);
		
		return Service.START_STICKY; //make sure this is correct.
	}
	
	private void setInterval(String intervalString)
	{
		if (intervalString.equals("4"))
		{
			interval = FOUR;
		}
		else if (intervalString.equals("8"))
		{
			interval = EIGHT;
		}
		else
		{
			interval = TWELVE;
		}
	}
	
	private void dead()
	{
		System.out.println("SERVICE DEAD");
	}
	
	private void undead()
	{
		System.out.println("STILL GOING");
	}
	
	private void endSerivce()
	{
		timer.cancel();
		timer.purge();
		context.stopService(intent);
	}
	
	private TimerTask updateSettings()
	{
		return new TimerTask(){

			@Override
			public void run() {
		        String newInterval = SBSharedPreferences.getPeriodicInterval(context);
		        if (newInterval.equals(""))
		        {
		        	dead();
		        	endSerivce();
		        }
		        else
		        {
		        	undead();
		        	setInterval(newInterval);
		        }
			}};
	}
	
	private TimerTask launchPeriodicSurvey()
	{
		return new TimerTask(){

			@Override
			public void run() {
		        Intent i = new Intent(context, SBSurvey.class);
		        i.putExtra(SBPhoneCallReceiver.SURVEY_TYPE, "p");
		        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		        context.startActivity(i);
			}};
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
