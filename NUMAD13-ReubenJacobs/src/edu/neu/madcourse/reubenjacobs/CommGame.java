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
		this.moveNumber = 0;
		
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
		new SendScoreTask().execute(this.userName, this.opponentName, this.secondsLeft.toString(), this.moveNumber.toString());
	}
}

class CreateGameTask extends AsyncTask<String, Void, Void> {
	protected Void doInBackground(String... strings) {
		  
		//Stores the game (User-Opponent, game)
		  KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], "game:0");
	      
	      return null;
	}
}

class SendScoreTask extends AsyncTask<String, Void, Void> {
	protected Void doInBackground(String... strings) {
		  
		  //Stores the score by time. (User-Opponent, Score-UserWhoSentScore:Move#)
		  KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], strings[2] + "-" + strings[0] + ":" + strings[3]); //2 is score and 3 is move number
	      
	      return null;
	}
}
 
