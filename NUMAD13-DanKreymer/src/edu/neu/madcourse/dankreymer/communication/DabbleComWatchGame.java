package edu.neu.madcourse.dankreymer.communication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import edu.neu.madcourse.dankreymer.R;

public class DabbleComWatchGame extends Activity implements OnClickListener{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_watch_game);
		
		View instructionsButon = findViewById(R.id.dabble_back_button);
		instructionsButon.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dabble_back_button:
			finish();
			break;
		}
	}
}
