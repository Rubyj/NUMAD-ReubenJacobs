package edu.neu.madcourse.dankreymer.communication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.id;
import edu.neu.madcourse.dankreymer.R.layout;
import edu.neu.madcourse.dankreymer.R.raw;
import edu.neu.madcourse.dankreymer.keys.Keys;
import edu.neu.madcourse.dankreymer.keys.ServerError;
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
	private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_game_over);
		TextView textBox = (TextView) findViewById(R.id.dabble_score_text);
		Bundle bundle = getIntent().getExtras();
		score = bundle.getString(DabbleComGame.KEY_GET_SCORE);
		username = bundle.getString(DabbleCom.USERNAME);
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
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("M/d, H:mm");
		
		@Override
		protected String doInBackground(String... parameter) { 
			String scoreData = KeyValueAPI.get(Keys.TEAMNAME, Keys.PASSWORD, Keys.HIGHSCORES);

			
			if (scoreData.equals(ServerError.NO_CONNECTION.getText()))
			{
				return "";
			}
			
			if (scoreData.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				scoreData = "";
			}
			
			if (scoreData == "")
			{
				scoreData = score + " (" + username + ", " + sdf.format(c.getTime()) + ")";
			}
			else
			{
				scoreData = addNewScore(scoreData);
			}
			
			return KeyValueAPI.put(Keys.TEAMNAME, Keys.PASSWORD, Keys.HIGHSCORES, scoreData);
		}
		
		private String addNewScore(String scoreData) {
			List<String> scoreList = new ArrayList<String>(
					Arrays.asList(scoreData.split(";")));

			scoreList.add(score + " (" + username + ", " + sdf.format(c.getTime()) + ")");

			Collections.sort(scoreList, new Comparator<String>() {
				public int compare(String a, String b) {
					String numA = a.split(" ")[0];
					String numB = b.split(" ")[0];
					return Integer.signum(parse(numB) - parse(numA));
				}

				private int parse(String s) {
					return Integer.parseInt(s);
				}
			});
			
			if (scoreList.size() > 10) {
				scoreList = scoreList.subList(0, 10);
			}
			
			String ret = "";
			for (String score : scoreList) {
				ret += score + ";";
			}

			return ret.substring(0, ret.length() - 1);
		}
	}
}