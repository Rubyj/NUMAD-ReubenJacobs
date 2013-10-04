package edu.neu.madcourse.dankreymer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DabbleScore extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_score);
		TextView textBox = (TextView) findViewById(R.id.dabble_score);
		Bundle bundle = getIntent().getExtras();
		textBox.setText(Integer.toString(bundle.getInt(DabbleGame.KEY_GET_SCORE)));			
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Intent intent = new Intent(this, Dabble.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(Dabble.RESUME_BUTTON_ENABLED_KEY, false);
		startActivity(intent);
	}
}