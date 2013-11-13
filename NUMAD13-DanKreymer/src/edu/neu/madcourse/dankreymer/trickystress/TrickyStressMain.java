package edu.neu.madcourse.dankreymer.trickystress;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.multiplayer.DabbleMMulti;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TrickyStressMain extends Activity implements OnClickListener {
	private boolean enabled;
	private Button phoneSurveyButton;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tricky_stress_main);
		
		enabled = false;
		
		View quitButton = findViewById(R.id.tricky_quit_button);
		quitButton.setOnClickListener(this);
		View periodicSurveyButton = findViewById(R.id.tricky_periodic_survey_button);
		periodicSurveyButton.setOnClickListener(this);
		
		phoneSurveyButton = (Button) findViewById(R.id.tricky_phone_survey_button);
		phoneSurveyButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tricky_periodic_survey_button:
			Intent intent = new Intent(this, TrickyStressBT.class);
			startActivity(intent);
			break;
		case R.id.tricky_phone_survey_button:
			togglePhoneSurveyButton();
			break;
		case R.id.tricky_quit_button:
			finish();
			break;
		}
	}
	
	private void togglePhoneSurveyButton()
	{
		enabled = !enabled;
		
		if (enabled)
		{
			phoneSurveyButton.setText(getResources().getString(R.string.tricky_phone_survey_button_disable_label));
		}
		else
		{
			phoneSurveyButton.setText(getResources().getString(R.string.tricky_phone_survey_button_label));
		}
	}
}
