package edu.neu.madcourse.dankreymer.dabble;

import java.util.List;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.id;
import edu.neu.madcourse.dankreymer.R.layout;
import edu.neu.madcourse.dankreymer.R.raw;
import edu.neu.madcourse.dankreymer.misc.Music;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DabbleGameOver extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_game_over);
		TextView textBox = (TextView) findViewById(R.id.dabble_score_text);
		Bundle bundle = getIntent().getExtras();
		textBox.setText(bundle.getString(DabbleGame.KEY_GET_SCORE));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);        
		finish();
	}
}