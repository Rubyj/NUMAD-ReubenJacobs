package edu.neu.madcourse.reubenjacobs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import edu.neu.mhealth.api.KeyValueAPI;

public class CommGame extends Activity {
	
	private String opponentName;
	private String userName;
	private Integer secondsLeft;
	private Integer moveNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comm_game);
		
		Bundle extras = getIntent().getExtras();
		this.userName = extras.getString("USER");
		this.opponentName = extras.getString("OPPONENT");
		
		if (this.userName != null && this.opponentName != null) {
			new SyncNotificationTask(this).execute(this.userName, this.opponentName);
			new AsyncNotificationTask(this).execute(this.userName, this.opponentName);
		}
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comm_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onSendScore(View view) {
		//get the move number
		new LoadMoveNumberTask(this).doInBackground(this.userName, this.opponentName);
		//increment the move number in this class
		this.moveNumber++;
		//send the score with the new movenumber
		new SendScoreTask(this).execute(this.userName, this.opponentName, this.secondsLeft.toString(), this.moveNumber.toString());
	}
	
	public int getSecondsLeft() {
		return this.secondsLeft;
	}
	
	public int getMove() {
		return this.moveNumber;
	}
	
	public String getUser() {
		return this.userName;
	}
	
	public String getOpp() {
		return this.opponentName;
	}
	
	public void setMove(int num) {
		this.moveNumber = num;
	}
	
	public void showMoveAlert() {
		   AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
	       builder1.setMessage("You're opponent has made a move! You have either lost or won or it is your turn");
	       builder1.setCancelable(true);
	       builder1.setNegativeButton("Back",
	    	    new DialogInterface.OnClickListener() {
	            	public void onClick(DialogInterface dialog, int id) {
	            		dialog.cancel();
	            	}
	        	}
	       );

	       AlertDialog alert11 = builder1.create();
	       alert11.show();
	}
	
	public void showLoseAlert() {
		   AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
	       builder1.setMessage("You're opponent has made a move! You have lost!");
	       builder1.setCancelable(true);
	       builder1.setNegativeButton("Back",
	    	    new DialogInterface.OnClickListener() {
	            	public void onClick(DialogInterface dialog, int id) {
	            		dialog.cancel();
	            	}
	        	}
	       );

	       AlertDialog alert11 = builder1.create();
	       alert11.show();
	}
	
	public void showWinAlert() {
		   AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
	       builder1.setMessage("You're opponent has made a move! You have won");
	       builder1.setCancelable(true);
	       builder1.setNegativeButton("Back",
	    	    new DialogInterface.OnClickListener() {
	            	public void onClick(DialogInterface dialog, int id) {
	            		dialog.cancel();
	            	}
	        	}
	       );

	       AlertDialog alert11 = builder1.create();
	       alert11.show();
	}
	
	class SendScoreTask extends AsyncTask<String, Void, Void> {
		CommGame instance;
		
		public SendScoreTask(CommGame l) {
			instance = l;
		}
		
		protected Void doInBackground(String... strings) {
			  String game = KeyValueAPI.get("sloth_nation", "fromunda", strings[0] + "-" + strings[1]);
			  String gameRev = KeyValueAPI.get("sloth_nation", "fromunda", strings[1] + "-" + strings[0]);
			  
			  int timeF = Integer.parseInt(game.substring(0, game.indexOf("-")));
			  int timeR = Integer.parseInt(gameRev.substring(0, gameRev.indexOf("-")));
			  
			  Boolean updateScore = false;
			  if (game.contains("game")) {
				  updateScore = true;
			  } else if (instance.getSecondsLeft() > timeF) {
				  updateScore = true;
			  }
			  
			  Boolean updateScoreRev = false;
			  if (gameRev.contains("game")) {
				  updateScoreRev = true;
			  } else if (instance.getSecondsLeft() > timeR) {
				  updateScoreRev = true;
			  }
			  
			  //Stores the score by time. (User-Opponent, Score-UserWhoSentScore:Move#)
			  if (!game.contains("Error") && updateScore) {
				  KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], strings[2] + "-" + strings[0] + ":" + strings[3]); //2 is score and 3 is move number
			  } else if (!gameRev.contains("Error") && updateScoreRev) {
				  KeyValueAPI.put("sloth_nation", "fromunda", strings[1] + "-" + strings[0], strings[2] + "-" + strings[0] + ":" + strings[3]);
			  }
		      return null;
		}
	}

	class LoadMoveNumberTask extends AsyncTask<String, Void, Void> {
		CommGame instance;
		Integer moveNum;
		
		public LoadMoveNumberTask(CommGame l) {
			this.instance = l;
		}
		
		protected Void doInBackground(String... strings) {
			  
			  String value = KeyValueAPI.get("sloth_nation", "fromunda", strings[0] + "-" + strings[1]);
			  if (value.contains("Error")) {
				  value = KeyValueAPI.get("sloth_nation", "fromunda", strings[1] + "-" + strings[0]);
			  }
			  
			  
			  String moveNumber = value.substring(value.indexOf(":") + 1);
			  
			  moveNum = Integer.parseInt(moveNumber);
		      
		      return null;
		}
		
		@Override
		protected void onPostExecute(Void x) {
			this.instance.setMove(this.moveNum);
		}
	}
}

class SyncNotificationTask extends AsyncTask<String, Void, Void> {
	CommGame instance;
	Integer moveNum;
	Boolean moveChanged;
	String user;
	String string0;
	String string1;
	
	public SyncNotificationTask(CommGame l) {
		this.instance = l;
		this.moveNum = this.instance.getMove();
		this.moveChanged = false;
	}
	
	protected Void doInBackground(String... strings) {
		string0 = strings[0];
		string1 = strings[1];
		
		String value = KeyValueAPI.get("sloth_nation", "fromunda", strings[0] + "-" + strings[1]);
		if (value.contains("Error")) {
			value = KeyValueAPI.get("sloth_nation", "fromunda", strings[1] + "-" + strings[0]);
		}
		
		this.user = value.substring(value.indexOf("-") + 1, value.indexOf(":"));
		
		String tempNumString = value.substring(value.indexOf(":") + 1);
		
		int tempNum = Integer.parseInt(tempNumString);
		
		if (tempNum != this.moveNum) {
			this.moveChanged = true;
			this.instance.setMove(tempNum);
			this.moveNum = tempNum;
		}
		
		return null;
	}
	
	@Override 
	protected void onPostExecute(Void x){
		if (this.moveChanged && this.moveNum > 1) {
			if (this.user.toLowerCase().equals(this.instance.getUser())) {
				this.instance.showWinAlert();
			} else if (this.user.toLowerCase().equals(this.instance.getOpp())) {
				this.instance.showLoseAlert();
			}
		} else if (this.moveChanged && this.moveNum > 0) {
			this.instance.showMoveAlert();
		}
		
		try {
			Thread.sleep(120000);
			this.doInBackground(this.string0, this.string1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class AsyncNotificationTask extends AsyncTask<String, Void, Void> {
	CommGame instance;
	Integer moveNum;
	Boolean moveChanged;
	Context context;
	String string0;
	String string1;
	
	public AsyncNotificationTask(CommGame l) {
		this.instance = l;
		this.moveNum = this.instance.getMove();
		this.moveChanged = false;
		this.context = this.instance.getApplicationContext();
	}
	
	protected Void doInBackground(String... strings) {
		this.string0 = strings[0];
		this.string1 = strings[1];
		
		String value = KeyValueAPI.get("sloth_nation", "fromunda", strings[0] + "-" + strings[1]);
		if (value.contains("Error")) {
			value = KeyValueAPI.get("sloth_nation", "fromunda", strings[1] + "-" + strings[0]);
		}
		
		String tempNumString = value.substring(value.indexOf(":") + 1);
		
		int tempNum = Integer.parseInt(tempNumString);
		
		if (tempNum != this.moveNum) {
			this.moveChanged = true;
			this.instance.setMove(tempNum);
			this.moveNum = tempNum;
		}
		
		return null;
	}
	
	@Override 
	protected void onPostExecute(Void x){
		
		Intent launchGame = new Intent(this.context, CommGame.class);
		launchGame.putExtra("USER", this.instance.getUser());
		launchGame.putExtra("OPPONENT", this.instance.getOpp());
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.context)
			.setContentTitle("CommGame").setContentText("An opponent has played a move. Launch Comm!");
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.context);
		stackBuilder.addParentStack(CommGame.class);
		stackBuilder.addNextIntent(launchGame);
		PendingIntent pendingLaunch = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pendingLaunch);
		
		if (this.moveChanged && this.moveNum > 0) {
			NotificationManager mNot = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNot.notify("Comm", 1959, mBuilder.build());
		}
		
		try {
			Thread.sleep(1200000);
			this.doInBackground(this.string0, this.string1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
