package edu.neu.madcourse.dankreymer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Dabble extends Activity implements OnClickListener {

	protected static String GAME_STATUS_KEY = "STATUS";
	protected static String NEW_GAME = "NEW_GAME";
	protected static String RESUME_GAME = "RESUME_GAME";
	private static String RESUME_BUTTON_ENABLED_KEY = "RESUME_BUTTON_ENABLED";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble);
		
		if (getPreferences(MODE_PRIVATE).contains(RESUME_BUTTON_ENABLED_KEY))
		{
			findViewById(R.id.dabble_resume_game_button).setEnabled(true);
		}

		// Set up click listeners for all the buttons
		View newButton = findViewById(R.id.dabble_new_game_button);
		newButton.setOnClickListener(this);
		View resumeButton = findViewById(R.id.dabble_resume_game_button);
		resumeButton.setOnClickListener(this);
		View acknowledgementsButton = findViewById(R.id.dabble_acknowledgements_button);
		acknowledgementsButton.setOnClickListener(this);
		View quitButton = findViewById(R.id.dabble_quit_button);
		quitButton.setOnClickListener(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
		editor.putBoolean(RESUME_BUTTON_ENABLED_KEY, true);
		editor.commit();
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
		case R.id.dabble_quit_button:
			finish();
			break;
		}
	}

	private void startNewGame(String val) {
		Intent intent = new Intent(this, DabbleGame.class);
		intent.putExtra(GAME_STATUS_KEY, val);
		startActivity(intent);
	}
}
