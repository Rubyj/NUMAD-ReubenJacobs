package edu.neu.madcourse.reubenjacobs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "edu.neu.madcourse.reubenjacobs.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		
		/*
		float density = getResources().getDisplayMetrics().density;
		float dpHeight = outMetrics.heightPixels / density;
		float dpWidth = outMetrics.widthPixels / density;
		*/
		setContentView(R.layout.activity_main);
		
		Button aboutButton = (Button) this.findViewById(R.id.aboutButton);
		Button sudokuButton = (Button) this.findViewById(R.id.sudokuButton);
		Button errorButton = (Button) this.findViewById(R.id.errorButton);
		Button quitButton = (Button) this.findViewById(R.id.quitButton);
		
		LayoutParams aboutParams = (RelativeLayout.LayoutParams) aboutButton.getLayoutParams();
		aboutParams.setMargins((int)outMetrics.widthPixels/3, (int)outMetrics.heightPixels/15, (int)outMetrics.widthPixels/3, (int)outMetrics.heightPixels/15);
		aboutButton.setLayoutParams(aboutParams);
		
		LayoutParams sudokuParams = (RelativeLayout.LayoutParams) sudokuButton.getLayoutParams();
		sudokuParams.setMargins(0, 0, (int)outMetrics.widthPixels/3, 0);
		sudokuButton.setLayoutParams(sudokuParams);
		
		LayoutParams errorParams = (RelativeLayout.LayoutParams) errorButton.getLayoutParams();
		errorParams.setMargins(0, (int)outMetrics.heightPixels/15, (int)outMetrics.widthPixels/3, 0);
		errorButton.setLayoutParams(errorParams);
		
		LayoutParams quitParams = (RelativeLayout.LayoutParams) quitButton.getLayoutParams();
		quitParams.setMargins(0, (int)outMetrics.heightPixels/15, (int)outMetrics.widthPixels/3, 0);
		quitButton.setLayoutParams(quitParams);
		
		
		//Uncomment for authorization
		//PhoneCheckAPI.doAuthorization(this); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void about(View view) {
		Intent intent = new Intent(this, AboutActivity.class);
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
