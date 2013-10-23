package edu.neu.madcourse.reubenjacobs;

import java.util.Timer;
import java.util.TimerTask;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import edu.neu.mhealth.api.KeyValueAPI;

public class CommGame extends Activity {
	
	private String opponentName;
	private String userName;
	private Integer secondsLeft;
	private Integer moveNumber;
	private String gameType;
	private Boolean moveNumSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comm_game);
		
		Bundle extras = getIntent().getExtras();
		this.userName = extras.getString("USER");
		this.opponentName = extras.getString("OPPONENT");
		this.gameType = extras.getString("GAME");
		this.moveNumSet = false;
		this.secondsLeft = 120; //For Testing
		
		if (gameType.equals("NEW")) {
			this.moveNumber = 0;
			this.moveNumSet = true;;
			TextView tv = (TextView) findViewById(R.id.gameUsers);
			tv.setText("New Game: " + this.userName + "-" + this.opponentName + "/Move#" + this.moveNumber );
		} else if (gameType.equals("JOIN")) { 
			new LoadMoveNumberTask().execute(this.userName, this.opponentName); 
		}
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		SimpleTimerTask aTimerTask = new SimpleTimerTask(this, this.userName, this.opponentName, true);
		SimpleTimerTask sTimerTask = new SimpleTimerTask(this, this.userName, this.opponentName, false);
		long delay = 0;
		long period = 5000;
		Timer myTimer = new Timer();
		
		if (this.userName != null && this.opponentName != null && moveNumSet) {
			myTimer.schedule(aTimerTask, 0, 5000);
			myTimer.schedule(sTimerTask, 0, 5000);
		}
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
		new LoadMoveNumberTask().doInBackground(this.userName, this.opponentName);
		//increment the move number in this class
		this.moveNumber++;
		//send the score with the new movenumber
		Log.d("PRESEND SCORE MOVE#", moveNumber.toString());
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
			  Boolean forward = false;
			  Boolean reverse = false;
			  Boolean updateScore = false;
			  Boolean updateScoreRev = false;
			  
			  
			  if (!game.contains("Error")) {
				  forward = true;
			  } else if (!gameRev.contains("Error")) {
				  reverse = true;
			  }
			  
			  Log.d("VALUE BEFORE SPLIT", game);
			  
			  int timeF;
			  int timeR;
			  
			  if (forward && game.contains("game")) {
				  updateScore = true;
			  } else if (forward) {
				  timeF = Integer.parseInt(game.substring(0, game.indexOf("-")));
				  if (instance.getSecondsLeft() > timeF) {
					  updateScore = true;
				  }
			  }
			  
			  if (reverse && gameRev.contains("game")) {
				  updateScoreRev = true;
			  } else if (reverse){
				  timeR = Integer.parseInt(gameRev.substring(0, gameRev.indexOf("-")));
				  if (instance.getSecondsLeft() > timeR) {
					  updateScoreRev = true;
				  }
			  }
			  
			  //Stores the score by time. (User-Opponent, Score-UserWhoSentScore:Move#)
			  if (updateScore) {
				  KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], strings[2] + "-" + strings[0] + ":" + strings[3]); //2 is score and 3 is move number
			  } else if (updateScoreRev) {
				  KeyValueAPI.put("sloth_nation", "fromunda", strings[1] + "-" + strings[0], strings[2] + "-" + strings[0] + ":" + strings[3]);
			  } else if (forward) {
				  Integer num = Integer.parseInt(game.substring(game.length() - 1));
				  KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], game.substring(0, game.length() - 1) + strings[3]);
			  } else if (reverse) {
				  KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], gameRev.substring(0, game.length() - 1) + strings[3]);
			  }
			  
		      return null;
		}
		
		@Override
		public void onPostExecute(Void x){
			TextView tv = (TextView) findViewById(R.id.gameUsers);
			String text  = tv.getText().toString();
			Integer num = Integer.parseInt(text.substring(text.length() - 1));
			tv.setText(text.substring(0, text.length() - 1) + num);
		}
	} 

	class LoadMoveNumberTask extends AsyncTask<String, Void, Void> {
		Integer moveNum;
		
		protected Void doInBackground(String... strings) {
			  
			  String value = KeyValueAPI.get("sloth_nation", "fromunda", strings[0] + "-" + strings[1]);
			  if (value.contains("Error")) {
				  value = KeyValueAPI.get("sloth_nation", "fromunda", strings[1] + "-" + strings[0]);
			  }
			  
			  String moveNumberString = value.substring(value.indexOf(":") + 1);
			  
			  moveNumber = Integer.parseInt(moveNumberString);
			  moveNumSet = true;
			  
			  if (gameType.equals("CREATE")) {
				  moveNumber = 0;
			  }
			  
		      return null;
		}
		
		@Override
		protected void onPostExecute(Void x) {
			TextView tv = (TextView) findViewById(R.id.gameUsers);
			tv.setText("Joined Game: " + userName + "-" + opponentName + "/Move#" + moveNumber);
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
				if (!isFinishing() && this.user.toLowerCase().equals(this.instance.getUser())) {
					showWinAlert();
				} else if (!isFinishing() && this.user.toLowerCase().equals(this.instance.getOpp())) {
					showLoseAlert();
				}
			} else if (!isFinishing() && this.moveChanged && this.moveNum > 0) {
				showMoveAlert();
			}
		}
	}
	
	class SimpleTimerTask extends TimerTask {
		CommGame instance;
		String userName;
		String opponent;
		Boolean aSync;
		
		SimpleTimerTask(CommGame instance, String userName, String opponent, Boolean aSync) {
			this.instance = instance;
			this.userName = userName;
			this.opponent = opponent;
			this.aSync = aSync;
		}
		public void run() {
			if (this.aSync) {
				new AsyncNotificationTask(this.instance).execute(this.userName, this.opponent);
			} else {
				new SyncNotificationTask(this.instance).execute(this.userName, this.opponent);
			}
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
	}
}