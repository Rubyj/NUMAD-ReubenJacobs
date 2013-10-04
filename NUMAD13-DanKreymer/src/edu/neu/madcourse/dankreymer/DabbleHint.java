package edu.neu.madcourse.dankreymer;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DabbleHint extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_hint);
		TextView textBox = (TextView) findViewById(R.id.dabble_hint_text);
		Bundle bundle = getIntent().getExtras();
		String newline = System.getProperty("line.separator");
		textBox.setText(bundle.getString(DabbleGame.KEY_SOLUTION_1) + newline
				+ bundle.getString(DabbleGame.KEY_SOLUTION_2) + newline
				+ bundle.getString(DabbleGame.KEY_SOLUTION_3) + newline
				+ bundle.getString(DabbleGame.KEY_SOLUTION_4));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getIntent().getExtras().getBoolean(DabbleGame.KEY_MUSIC))
		{
			Music.play(this, R.raw.dabble_hint);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}
}