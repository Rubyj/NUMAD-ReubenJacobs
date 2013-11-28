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

public class DabblePause extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_pause);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}