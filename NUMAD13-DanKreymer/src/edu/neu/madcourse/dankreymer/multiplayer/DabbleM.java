package edu.neu.madcourse.dankreymer.multiplayer;

import java.util.Arrays;
import java.util.List;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.id;
import edu.neu.madcourse.dankreymer.R.layout;
import edu.neu.madcourse.dankreymer.keys.Keys;
import edu.neu.madcourse.dankreymer.keys.ServerError;
import edu.neu.mhealth.api.KeyValueAPI;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class DabbleM extends Activity implements OnClickListener {

	protected static String GAME_STATUS_KEY = "STATUS";
	protected static String NEW_GAME = "NEW_GAME";
	protected static String RESUME_GAME = "RESUME_GAME";
	
	protected static final String USERNAME = "USER";
	
	private static int GAME_REQUEST_CODE = 1;
	private static int USERNAME_REQUEST_CODE = 2;
	
	SharedPreferences pref;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_m);
		
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		
		if (pref.getString(USERNAME, "") == "")
		{
			Intent intent = new Intent(this, DabbleMUsername.class);
			startActivityForResult(intent, USERNAME_REQUEST_CODE);
		}

		// Set up click listeners for all the buttons
		View newButton = findViewById(R.id.dabble_new_game_button);
		newButton.setOnClickListener(this);
		View resumeButton = findViewById(R.id.dabble_resume_game_button);
		resumeButton.setOnClickListener(this);
		View watchButton = findViewById(R.id.dabble_watch_game_button);
		watchButton.setOnClickListener(this);
		View multiButton = findViewById(R.id.dabble_multiplayer_button);
		multiButton.setOnClickListener(this);
		View acknowledgementsButton = findViewById(R.id.dabble_acknowledgements_button);
		acknowledgementsButton.setOnClickListener(this);
		View instructionsButon = findViewById(R.id.dabble_instructions_button);
		instructionsButon.setOnClickListener(this);
		View highScores = findViewById(R.id.dabble_high_scores_button);
		highScores.setOnClickListener(this);
		View quitButton = findViewById(R.id.dabble_quit_button);
		quitButton.setOnClickListener(this);
	}
	
	public void onResume()
	{
		super.onResume();
		if (!getUser().equals(""))
		{
			new RegisterUserTask().execute();
		}
	}
	
	public void onDestroy()
	{
		new UserOfflineTask().execute();
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GAME_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				findViewById(R.id.dabble_resume_game_button).setEnabled(false);
			}
			if (resultCode == RESULT_CANCELED) {
				findViewById(R.id.dabble_resume_game_button).setEnabled(true);
			}
		}
		else if (requestCode == USERNAME_REQUEST_CODE)
		{
			if (resultCode == RESULT_OK) {
			
				SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
				editor.putString(USERNAME, data.getStringExtra(USERNAME));
				editor.commit();
				
				new RegisterUserTask().execute();
			}
			
			if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dabble_new_game_button:
			startNewGame(NEW_GAME);
			break;
		case R.id.dabble_resume_game_button:
			startNewGame(RESUME_GAME);
			break;
		case R.id.dabble_multiplayer_button:
			Intent intent = new Intent(this, DabbleMMulti.class);
			intent.putExtra(USERNAME,getUser());
			startActivity(intent);
			break;
		case R.id.dabble_watch_game_button:
			Intent intent2 = new Intent(this, DabbleMWatchGame.class);
			intent2.putExtra(USERNAME,getUser());
			startActivity(intent2);
			break;
		case R.id.dabble_instructions_button:
			startActivity(new Intent(this, DabbleMInstructions.class));
			break;
		case R.id.dabble_high_scores_button:
			startActivity(new Intent(this, DabbleMHighScores.class));
			break;
		case R.id.dabble_acknowledgements_button:
			startActivity(new Intent(this, DabbleMAcknowledgements.class));
			break;
		case R.id.dabble_quit_button:
			finish();
			break;
		}
	}

	private void startNewGame(String val) {
		Intent intent = new Intent(this, DabbleMGame.class);
		intent.putExtra(GAME_STATUS_KEY, val);
		intent.putExtra(USERNAME,getUser());
		startActivityForResult(intent, GAME_REQUEST_CODE);
	}
	
	private String getUser()
	{
		return getPreferences(MODE_PRIVATE).getString(USERNAME, "");
	}
	
	private class UserOfflineTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			return Keys.put(Keys.userStatusKey(getUser()), Keys.STATUS_OFFLINE);
		}
		
	}
	
	private class RegisterUserTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String usersListRaw = Keys.get(Keys.USERS);
			
			if (usersListRaw.equals(ServerError.NO_CONNECTION.getText()))
			{
				return "";
			}
			
			Keys.put(Keys.userStatusKey(getUser()), Keys.STATUS_ONLINE);
			
			if (usersListRaw.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				usersListRaw = "";
			}
			
			List<String> usersList = 
					Arrays.asList(Keys.get(Keys.USERS).split(","));
			
			if (usersList.contains(getUser()))
			{
				return "";
			}
			else
			{
				String newList;
				if (usersListRaw == "")
				{
					newList = getUser();
				}
				else
				{
					newList = usersListRaw + "," + getUser();
				}
				
				Keys.put(Keys.USERS, newList);
				Keys.put(Keys.userStatusKey(getUser()), Keys.STATUS_ONLINE);
				Keys.clearKey(Keys.userGameplayKey(getUser())); //in case game was shut down accidentally.
				return "";
			}
		}
	}
}
