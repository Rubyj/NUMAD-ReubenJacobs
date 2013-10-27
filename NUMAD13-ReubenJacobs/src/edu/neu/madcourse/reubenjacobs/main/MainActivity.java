package edu.neu.madcourse.reubenjacobs.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import edu.neu.madcourse.reubenjacobs.DabbleComm;
import edu.neu.madcourse.reubenjacobs.DictionaryActivity;
import edu.neu.madcourse.reubenjacobs.R;
import edu.neu.madcourse.reubenjacobs.WelcomeComm;
import edu.neu.madcourse.reubenjacobs.WelcomeDabble;
import edu.neu.madcourse.reubenjacobs.sudoku.Sudoku;
import edu.neu.mobileClass.PhoneCheckAPI;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "edu.neu.madcourse.reubenjacobs.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("Reuben Jacobs");
		super.onCreate(savedInstanceState);
		//Uncomment for authorization
		PhoneCheckAPI.doAuthorization(this);
		setContentView(R.layout.activity_main);
		
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		
		int sideMargin = (int)outMetrics.widthPixels/4;
		int topMargin = (int)outMetrics.heightPixels/65 ;
		
		Button aboutButton = (Button) this.findViewById(R.id.aboutButton);
		Button sudokuButton = (Button) this.findViewById(R.id.sudokuButton);
		Button errorButton = (Button) this.findViewById(R.id.errorButton);
		Button quitButton = (Button) this.findViewById(R.id.quitButton);
		Button dictButton = (Button) this.findViewById(R.id.dictionaryButton);
		Button dabButton = (Button) this.findViewById(R.id.dabButton);
		Button comButton = (Button) this.findViewById(R.id.commButton);
		Button twoButton = (Button) this.findViewById(R.id.twoButton);
		
		LayoutParams aboutParams = (RelativeLayout.LayoutParams) aboutButton.getLayoutParams();
		aboutParams.setMargins(sideMargin, topMargin*3, sideMargin, topMargin);
		aboutButton.setLayoutParams(aboutParams);
		
		LayoutParams sudokuParams = (RelativeLayout.LayoutParams) sudokuButton.getLayoutParams();
		sudokuParams.setMargins(0, topMargin, sideMargin, topMargin);
		sudokuButton.setLayoutParams(sudokuParams);
		
		LayoutParams errorParams = (RelativeLayout.LayoutParams) errorButton.getLayoutParams();
		errorParams.setMargins(0, topMargin, sideMargin, topMargin);
		errorButton.setLayoutParams(errorParams);
		
		LayoutParams quitParams = (RelativeLayout.LayoutParams) quitButton.getLayoutParams();
		quitParams.setMargins(0, topMargin, sideMargin, topMargin);
		quitButton.setLayoutParams(quitParams);
		
		LayoutParams dictParams = (RelativeLayout.LayoutParams) dictButton.getLayoutParams();
		dictParams.setMargins(0, topMargin, sideMargin, topMargin);
		dictButton.setLayoutParams(dictParams);

		LayoutParams dabParams = (RelativeLayout.LayoutParams) dabButton.getLayoutParams();
		dabParams.setMargins(0, topMargin, sideMargin, topMargin);
		dabButton.setLayoutParams(dabParams);
		
		LayoutParams comParams = (RelativeLayout.LayoutParams) comButton.getLayoutParams();
		comParams.setMargins(0, topMargin, sideMargin, topMargin);
		comButton.setLayoutParams(comParams);
		
		LayoutParams twoParams = (RelativeLayout.LayoutParams) twoButton.getLayoutParams();
		twoParams.setMargins(0, topMargin, sideMargin, topMargin);
		twoButton.setLayoutParams(twoParams);
		
		
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
	
	public void startDictionary(View view) {
		Intent intent = new Intent(this, DictionaryActivity.class);
		startActivity(intent);
	}
	
	public void startDabble(View view) {
		Intent intent = new Intent(this, WelcomeDabble.class);
		startActivity(intent);
	}
	
	public void startComm(View view) {
		Intent intent = new Intent(this, WelcomeComm.class);
		startActivity(intent);
	}

}
