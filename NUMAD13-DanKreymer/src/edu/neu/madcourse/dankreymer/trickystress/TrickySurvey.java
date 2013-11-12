package edu.neu.madcourse.dankreymer.trickystress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import edu.neu.madcourse.dankreymer.R;

public class TrickySurvey extends Activity implements OnClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tricky_survey);
		
	}
	
	@Override
	public void onClick(View v) {
		finish();

	}
}
