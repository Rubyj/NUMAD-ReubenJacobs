package edu.neu.madcourse.dankreymer.trickystress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import edu.neu.madcourse.dankreymer.R;
import android.graphics.PorterDuff;

public class TrickySurvey extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tricky_survey);
		
		colorButtons();		
	}
	
	private void colorButtons()
	{
		Button b1 = (Button) findViewById(R.id.tricky_survey1);
		b1.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey1), PorterDuff.Mode.MULTIPLY);
		Button b2 = (Button) findViewById(R.id.tricky_survey2);
		b2.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey2), PorterDuff.Mode.MULTIPLY);
		Button b3 = (Button) findViewById(R.id.tricky_survey3);
		b3.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey3), PorterDuff.Mode.MULTIPLY);
		Button b4 = (Button) findViewById(R.id.tricky_survey4);
		b4.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey4), PorterDuff.Mode.MULTIPLY);
		Button b5 = (Button) findViewById(R.id.tricky_survey5);
		b5.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey5), PorterDuff.Mode.MULTIPLY);
		Button b6 = (Button) findViewById(R.id.tricky_survey6);
		b6.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey6), PorterDuff.Mode.MULTIPLY);
		Button b7 = (Button) findViewById(R.id.tricky_survey7);
		b7.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey7), PorterDuff.Mode.MULTIPLY);
	}
	
	public void onSurveyClick(View v)
	{
		finish();
	}
}
