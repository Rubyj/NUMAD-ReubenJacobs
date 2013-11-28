package edu.neu.madcourse.dankreymer.trickystress;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.multiplayer.DabbleMMulti;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TrickyStressMain extends Activity implements OnClickListener {	
	private boolean enabled;
	private Button phoneSurveyButton;
	private ComponentName receiver;
	private PackageManager pm;
	private static final String WAS_ENABLED = "WAS_ENABLED";
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tricky_stress_main);
		
		receiver = new ComponentName(this, TrickyPhoneCallReceiver.class);
		pm = getPackageManager();
		
		if (!enabled)
		{
			pm.setComponentEnabledSetting(receiver, 
					  PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					  PackageManager.DONT_KILL_APP);
		}
		
		View quitButton = findViewById(R.id.tricky_quit_button);
		quitButton.setOnClickListener(this);
		View periodicSurveyButton = findViewById(R.id.tricky_periodic_survey_button);
		periodicSurveyButton.setOnClickListener(this);
		
		phoneSurveyButton = (Button) findViewById(R.id.tricky_phone_survey_button);
		phoneSurveyButton.setOnClickListener(this);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		enabled = getPreferences(Context.MODE_PRIVATE).getBoolean(WAS_ENABLED, false);
		setPhoneSurveyButtonText();
	}
	
	@Override
	public void onPause()
	{
		SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
		editor.putBoolean(WAS_ENABLED, enabled);
		editor.commit();
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tricky_periodic_survey_button:
			Intent intent = new Intent(this, TrickyStressBT.class);
			startActivity(intent);
			break;
		case R.id.tricky_phone_survey_button:
			toggleEnabled();
			break;
		case R.id.tricky_quit_button:
			finish();
			break;
		}
	}
	
	private void setPhoneSurveyButtonText()
	{
		if (enabled)
		{
			phoneSurveyButton.setText(getResources().getString(R.string.tricky_phone_survey_button_disable_label));
			
		}
		else
		{
			
			phoneSurveyButton.setText(getResources().getString(R.string.tricky_phone_survey_button_label));
		}
	}
	
	private void toggleEnabled()
	{
		enabled = !enabled;
		
		if (enabled)
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
		
		setPhoneSurveyButtonText();
	}
}
