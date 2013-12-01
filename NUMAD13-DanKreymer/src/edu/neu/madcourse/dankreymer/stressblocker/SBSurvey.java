package edu.neu.madcourse.dankreymer.stressblocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.trickystress.TrickyBluetoothService;
import android.graphics.PorterDuff;

public class SBSurvey extends Activity {
	private String number;
	private String type;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stress_blocker_survey);
		Bundle b = this.getIntent().getExtras();
		number = b.getString(SBPhoneCallReceiver.PHONE_NUMBER);
		type = b.getString(SBPhoneCallReceiver.SURVEY_TYPE);

		colorButtons();
	}

	private void colorButtons() {
		Button b1 = (Button) findViewById(R.id.sb_survey1);
		b1.getBackground().setColorFilter(
				getResources().getColor(R.color.sb_survey1),
				PorterDuff.Mode.MULTIPLY);
		Button b2 = (Button) findViewById(R.id.sb_survey2);
		b2.getBackground().setColorFilter(
				getResources().getColor(R.color.sb_survey2),
				PorterDuff.Mode.MULTIPLY);
		Button b3 = (Button) findViewById(R.id.sb_survey3);
		b3.getBackground().setColorFilter(
				getResources().getColor(R.color.sb_survey3),
				PorterDuff.Mode.MULTIPLY);
		Button b4 = (Button) findViewById(R.id.sb_survey4);
		b4.getBackground().setColorFilter(
				getResources().getColor(R.color.sb_survey4),
				PorterDuff.Mode.MULTIPLY);
		Button b5 = (Button) findViewById(R.id.sb_survey5);
		b5.getBackground().setColorFilter(
				getResources().getColor(R.color.sb_survey5),
				PorterDuff.Mode.MULTIPLY);
		Button b6 = (Button) findViewById(R.id.sb_survey6);
		b6.getBackground().setColorFilter(
				getResources().getColor(R.color.sb_survey6),
				PorterDuff.Mode.MULTIPLY);
		Button b7 = (Button) findViewById(R.id.sb_survey7);
		b7.getBackground().setColorFilter(
				getResources().getColor(R.color.sb_survey7),
				PorterDuff.Mode.MULTIPLY);
	}

	public void onSurveyClick1(View v) {
		if (type.equals("p")) {
			SBSharedPreferences.putCurrentStress(this, "1");
		} else {
			SBSharedPreferences.putContactData(this, number, type,
					Long.toString(System.currentTimeMillis()), "1");
		}
		finish();
	}

	public void onSurveyClick2(View v) {
		if (type.equals("p")) {
			SBSharedPreferences.putCurrentStress(this, "2");
		} else {
			SBSharedPreferences.putContactData(this, number, type,
					Long.toString(System.currentTimeMillis()), "2");
		}
		finish();
	}

	public void onSurveyClick3(View v) {
		if (type.equals("p")) {
			SBSharedPreferences.putCurrentStress(this, "3");
		} else {
			SBSharedPreferences.putContactData(this, number, type,
					Long.toString(System.currentTimeMillis()), "3");
		}
		finish();
	}

	public void onSurveyClick4(View v) {
		if (type.equals("p")) {
			SBSharedPreferences.putCurrentStress(this, "4");
		} else {
			SBSharedPreferences.putContactData(this, number, type,
					Long.toString(System.currentTimeMillis()), "4");
		}
		finish();
	}

	public void onSurveyClick5(View v) {
		if (type.equals("p")) {
			SBSharedPreferences.putCurrentStress(this, "5");
			startService(new Intent(this, SBBluetoothService.class));
			askBTDevice();
		} else {
			SBSharedPreferences.putContactData(this, number, type,
					Long.toString(System.currentTimeMillis()), "5");
		}
		finish();
	}

	public void onSurveyClick6(View v) {
		if (type.equals("p")) {
			startService(new Intent(this, SBBluetoothService.class));
			askBTDevice();
		} else {
			SBSharedPreferences.putContactData(this, number, type,
					Long.toString(System.currentTimeMillis()), "6");
		}
		finish();
	}

	public void onSurveyClick7(View v) {
		if (type.equals("p")) {
			SBSharedPreferences.putCurrentStress(this, "7");
	    	startService(new Intent(this, SBBluetoothService.class));
	    	askBTDevice();
		} else {
			SBSharedPreferences.putContactData(this, number, type,
					Long.toString(System.currentTimeMillis()), "7");
		}
		finish();
	}
	
	private void askBTDevice()
	{
		String queue = SBSharedPreferences.getBTQueue(this);
		if (!queue.equals(""))
		{
			String[] split = queue.split(",");
			//split[0]
					
		    SBSharedPreferences.removeFirstBTQueue(this);
		}
	}
}
