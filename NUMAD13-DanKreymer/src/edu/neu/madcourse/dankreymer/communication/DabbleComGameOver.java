package edu.neu.madcourse.dankreymer.communication;

import java.util.List;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.id;
import edu.neu.madcourse.dankreymer.R.layout;
import edu.neu.madcourse.dankreymer.R.raw;
import edu.neu.madcourse.dankreymer.keys.Keys;
import edu.neu.madcourse.dankreymer.misc.Music;
import edu.neu.mhealth.api.KeyValueAPI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DabbleComGameOver extends Activity {
	private String score;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_game_over);
		TextView textBox = (TextView) findViewById(R.id.dabble_score_text);
		Bundle bundle = getIntent().getExtras();
		score = bundle.getString(DabbleComGame.KEY_GET_SCORE);
		textBox.setText(score);
		new SaveScoreTask().execute();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);        
		finish();
	}
	
	private class SaveScoreTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... parameter) { 
			String scoreData = KeyValueAPI.get(Keys.TEAMNAME, Keys.PASSWORD, Keys.HIGHSCORES);
			if (scoreData.contains("Error"))
			{
				scoreData = "";
			}
			
			if (scoreData == "")
			{
				scoreData = score;
			}
			else
			{
				scoreData = scoreData + ";" + score;
			}
			
			return KeyValueAPI.put(Keys.TEAMNAME, Keys.PASSWORD, Keys.HIGHSCORES, scoreData);
		}
	}
}