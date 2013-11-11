package edu.neu.madcourse.dankreymer.trickystress;

import edu.neu.madcourse.dankreymer.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TrickyStressMain extends Activity implements OnClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tricky_stress_main);
		
		View quitButton = findViewById(R.id.tricky_quit_button);
		quitButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()){
		case R.id.tricky_quit_button:
			finish();
			break;
		}

	}
}
