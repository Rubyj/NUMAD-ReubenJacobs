package edu.neu.madcourse.dankreymer.multiplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.keys.Keys;
import edu.neu.madcourse.dankreymer.keys.ServerError;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.ArrayAdapter;

public class DabbleMNotificationService extends Service{
	private String user;
	private Timer timer;
	private List<String> games;
	private NotificationManager nManager;
	private Context context;

	@Override
	public void onCreate()
	{
		context = this;
		timer = new Timer();
		nManager =
			    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		games = new ArrayList<String>();
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		user = intent.getExtras().getString(DabbleM.USERNAME);
		
		timer.scheduleAtFixedRate(checkGamesTimerTask(), new Date(), 1000);
		timer.scheduleAtFixedRate(checkGameStatusTimerTask(), new Date(), 1000);
		return Service.START_REDELIVER_INTENT;
	}
	
	private TimerTask checkGamesTimerTask()
	{
		return new TimerTask(){

			@Override
			public void run() {
				new checkGamesTask().execute();
			}};
	}
	
	private TimerTask checkGameStatusTimerTask()
	{
		return new TimerTask(){

			@Override
			public void run() {
				for (String game : games)
				{
					new checkGameStatusTask().execute(game);
				}
			}};
	}
	
	private class checkGameStatusTask extends AsyncTask<String, String, String> {
		
		private String otherUser;
		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(ServerError.NO_CONNECTION.getText()) && 
					!result.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				if (result.equals(Keys.TURN_BASED_INVITED))
				{
					NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Dabble Invite!")
                    .setContentText("You have been invited to a new turn-based dabble game!");
					
					new setGameStatusInviteSeenTask().execute(otherUser);
					
					Intent targetIntent = new Intent(context, DabbleMMulti.class);
					targetIntent.putExtra(DabbleM.USERNAME, user);
					PendingIntent contentIntent = 
							PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					builder.setContentIntent(contentIntent);
					nManager.notify(1, builder.build());
				}
				else if (result.equals(Keys.TURN_BASED_YOUR_TURN))
				{
					NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Dabble!")
                    .setContentText("It's your turn in one of your current turn-based dabble games!");
					
					new setGameStatusYourTurnSeenTask().execute(otherUser);
					
					Intent targetIntent = new Intent(context, DabbleMMulti.class);
					targetIntent.putExtra(DabbleM.USERNAME, user);
					PendingIntent contentIntent = 
							PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					builder.setContentIntent(contentIntent);
					nManager.notify(1, builder.build());
				}
			}
		}
		
		@Override
		protected String doInBackground(String... parameter) { 
			otherUser = parameter[0];
			return Keys.get(Keys.turnBasedGameKey(user, parameter[0]));
		}
	}
	
	private class setGameStatusYourTurnSeenTask extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... parameter) { 
			return Keys.put(Keys.turnBasedGameKey(user, parameter[0]), Keys.TURN_BASED_YOUR_TURN_SEEN);
		}
	}
	
	private class setGameStatusInviteSeenTask extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... parameter) { 
			return Keys.put(Keys.turnBasedGameKey(user, parameter[0]), Keys.TURN_BASED_INVITED_SEEN);
		}
	}
	
	private class checkGamesTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(ServerError.NO_CONNECTION.getText()) && 
					!result.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				games = new ArrayList<String>(Arrays.asList(result.split(",")));
			}
		}
		
		@Override
		protected String doInBackground(String... parameter) { 
			return Keys.get(Keys.turnBasedGamesForPlayerKey(user));
		}
	}

}
