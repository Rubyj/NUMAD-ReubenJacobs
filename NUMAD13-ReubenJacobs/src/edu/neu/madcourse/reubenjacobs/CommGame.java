package edu.neu.madcourse.reubenjacobs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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
		
		new CreateGameTask().execute(this.userName, this.opponentName);
		
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
		new LoadMoveNumberTask(this).doInBackground(this.userName, this.opponentName);
		this.moveNumber++;
		new SendScoreTask(this).execute(this.userName, this.opponentName, this.secondsLeft.toString(), this.moveNumber.toString());
	}
	
	public int getSecondsLeft() {
		return this.secondsLeft;
	}
	
	public void setMove(int num) {
		this.moveNumber = num;
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
			  
			//Stores the game (User-Opponent, game)
			  String value = KeyValueAPI.get("sloth_nation", "fromunda", strings[0] + "-" + strings[1]);
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

class CreateGameTask extends AsyncTask<String, Void, Void> {
	protected Void doInBackground(String... strings) {
		  
		//Stores the game (User-Opponent, game)
		  KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], "game:0");
	      
	      return null;
	}
}