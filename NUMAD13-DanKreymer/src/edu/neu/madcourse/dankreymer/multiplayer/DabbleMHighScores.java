package edu.neu.madcourse.dankreymer.multiplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.keys.Keys;
import edu.neu.madcourse.dankreymer.keys.ServerError;
import edu.neu.mhealth.api.KeyValueAPI;

public class DabbleMHighScores extends Activity implements OnClickListener{

	private static String highScores = "";
	private static final String NO_SCORES = "No Scores Reported";
	private TextView text;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_high_scores);
		text = (TextView)findViewById(R.id.dabble_high_scores);
		
		new HighScoreTask().execute();
		
		View instructionsButon = findViewById(R.id.dabble_back_button);
		instructionsButon.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dabble_back_button:
			finish();
			break;
		}
	}
	
	private String parseScores(String string)
	{
		if (string.equals(ServerError.NO_CONNECTION.getText()) ||
			string.equals(ServerError.NO_SUCH_KEY.getText()))
		{
			string = NO_SCORES;
		}
		else
		{
			string = string.replace(";", "\n");
		}
		
		return string;
	}
	
	private class HighScoreTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String result) {
			highScores = parseScores(result);
			
			if (highScores == "")
			{
				text.setText(NO_SCORES);
			}
			else
			{
				text.setText(highScores);
			}
			
			text.invalidate();
		}
		
		@Override
		protected String doInBackground(String... parameter) { 
			return Keys.get(Keys.HIGHSCORES);
		}
	}
}
