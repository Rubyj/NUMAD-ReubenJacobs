package edu.neu.madcourse.dankreymer.stressblocker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import edu.neu.madcourse.dankreymer.R;

public class SBMain extends Activity {
	private boolean isActive;
	private Button isActiveButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stress_blocker_main);
		
		isActiveButton = (Button) findViewById(R.id.sb_isactive_button);
		
		colorButton();
	}
	
	private void colorButton()
	{
		isActive = SBSharedPreferences.getActiveStatus(this);
		
		if (isActive)
		{
			isActiveButton.getBackground().setColorFilter(getResources().getColor(R.color.sb_green_button), PorterDuff.Mode.MULTIPLY);
			isActiveButton.setText(getResources().getString(R.string.sb_active));
		}
		else
		{
			isActiveButton.getBackground().setColorFilter(getResources().getColor(R.color.sb_red_button), PorterDuff.Mode.MULTIPLY);
			isActiveButton.setText(getResources().getString(R.string.sb_inactive));
		}
		
		isActiveButton.invalidate();
	}

	public void onQuit(View view) {
		finish();
    }

    public void onSettings(View view) {
        Intent intent = new Intent(this, SBSettings.class);
        startActivity(intent);
    }
    
    public void onContacts(View view) {
        Intent intent = new Intent(this, SBContacts.class);
        startActivity(intent);
    }
    
    public void onClickActive(View view){
    	SBSharedPreferences.toggleActiveStatus(this);
    	colorButton();
    }
}
