package edu.neu.madcourse.dankreymer.stressblocker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.trickystress.TrickyPhoneCallReceiver;

public class SBMain extends Activity {
	private boolean isActive;
	private Button isActiveButton;
	private ComponentName receiver;
	private PackageManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stress_blocker_main);
		
		isActiveButton = (Button) findViewById(R.id.sb_isactive_button);
		
		receiver = new ComponentName(this, SBPhoneCallReceiver.class);
		pm = getPackageManager();
		
		isActive = SBSharedPreferences.getActiveStatus(this);
		
		toggleBroadcastReceiever();
		colorButton();
	}
	
	private void colorButton()
	{
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
	
	private void toggleBroadcastReceiever()
	{
		if (isActive)
		{
			pm.setComponentEnabledSetting(receiver, 
			  PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
			  PackageManager.DONT_KILL_APP);
			
		}
		else
		{
			pm.setComponentEnabledSetting(receiver, 
					  PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					  PackageManager.DONT_KILL_APP);
		}
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
    	
		isActive = !isActive;
		
		toggleBroadcastReceiever();
		colorButton();
    }
}
