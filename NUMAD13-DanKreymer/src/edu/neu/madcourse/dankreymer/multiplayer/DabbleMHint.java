package edu.neu.madcourse.dankreymer.multiplayer;

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

public class DabbleMHint extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_hint);
		TextView textBox = (TextView) findViewById(R.id.dabble_hint_text);
		Bundle bundle = getIntent().getExtras();
		String newline = System.getProperty("line.separator");
		textBox.setText(bundle.getString(DabbleMGame.KEY_SOLUTION_1) + newline
				+ bundle.getString(DabbleMGame.KEY_SOLUTION_2) + newline
				+ bundle.getString(DabbleMGame.KEY_SOLUTION_3) + newline
				+ bundle.getString(DabbleMGame.KEY_SOLUTION_4));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getIntent().getExtras().getBoolean(DabbleMGame.KEY_MUSIC))
		{
			Music.play(this, R.raw.dabble_hint);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (getIntent().getExtras().getBoolean(DabbleMGame.KEY_MUSIC))
		{
			Music.stop(this);
		}
		finish();
	}
}