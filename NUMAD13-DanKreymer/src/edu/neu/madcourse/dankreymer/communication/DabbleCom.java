package edu.neu.madcourse.dankreymer.communication;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.id;
import edu.neu.madcourse.dankreymer.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class DabbleCom extends Activity implements OnClickListener {

	protected static String GAME_STATUS_KEY = "STATUS";
	protected static String NEW_GAME = "NEW_GAME";
	protected static String RESUME_GAME = "RESUME_GAME";
	
	protected static final String USERNAME = "USER";
	
	private static int GAME_REQUEST_CODE = 1;
	private static int USERNAME_REQUEST_CODE = 2;
	
	SharedPreferences pref;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_com);
		
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		
		if (pref.getString(USERNAME, "") == "")
		{
			Intent intent = new Intent(this, DabbleComUsername.class);
			startActivityForResult(intent, USERNAME_REQUEST_CODE);
		}

		// Set up click listeners for all the buttons
		View newButton = findViewById(R.id.dabble_new_game_button);
		newButton.setOnClickListener(this);
		View resumeButton = findViewById(R.id.dabble_resume_game_button);
		resumeButton.setOnClickListener(this);
		View acknowledgementsButton = findViewById(R.id.dabble_acknowledgements_button);
		acknowledgementsButton.setOnClickListener(this);
		View instructionsButon = findViewById(R.id.dabble_instructions_button);
		instructionsButon.setOnClickListener(this);
		View highScores = findViewById(R.id.dabble_high_scores_button);
		highScores.setOnClickListener(this);
		View quitButton = findViewById(R.id.dabble_quit_button);
		quitButton.setOnClickListener(this);
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
		case R.id.dabble_instructions_button:
			startActivity(new Intent(this, DabbleComInstructions.class));
			break;
		case R.id.dabble_high_scores_button:
			startActivity(new Intent(this, DabbleComHighScores.class));
			break;
		case R.id.dabble_acknowledgements_button:
			startActivity(new Intent(this, DabbleComAcknowledgements.class));
			break;
		case R.id.dabble_quit_button:
			finish();
			break;
		}
	}

	private void startNewGame(String val) {
		Intent intent = new Intent(this, DabbleComGame.class);
		intent.putExtra(GAME_STATUS_KEY, val);
		intent.putExtra(USERNAME, getPreferences(MODE_PRIVATE).getString(USERNAME, ""));
		startActivityForResult(intent, GAME_REQUEST_CODE);
	}
}
