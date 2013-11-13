package edu.neu.madcourse.dankreymer.trickystress;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.multiplayer.DabbleMMulti;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TrickyStressMain extends Activity implements OnClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tricky_stress_main);
		
		View quitButton = findViewById(R.id.tricky_quit_button);
		quitButton.setOnClickListener(this);
		View periodicSurveyButton = findViewById(R.id.tricky_periodic_survey_button);
		periodicSurveyButton.setOnClickListener(this);
		View phoneSurveyButton = findViewById(R.id.tricky_phone_survey_button_label);
		phoneSurveyButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tricky_periodic_survey_button:
			Intent intent = new Intent(this, TrickyStressBT.class);
			startActivity(intent);
			break;
		case R.id.tricky_quit_button:
			finish();
			break;
		}
	}
}
