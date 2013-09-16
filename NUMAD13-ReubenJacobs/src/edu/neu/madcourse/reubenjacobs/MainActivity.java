package edu.neu.madcourse.reubenjacobs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import edu.neu.mobileClass.*;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "edu.neu.madcourse.reubenjacobs.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		PhoneCheckAPI.doAuthorization(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void about(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		startActivity(intent);
	}
	
	public void generateError(View view) {
		int x = 5 / 0;
	}
	
	public void exit(View view) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	public void startSudoku(View view) {
		Intent intent = new Intent(this, Sudoku.class);
		startActivity(intent);
	}

}
