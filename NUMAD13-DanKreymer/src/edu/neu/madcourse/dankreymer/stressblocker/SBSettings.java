package edu.neu.madcourse.dankreymer.stressblocker;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.layout;
import edu.neu.madcourse.dankreymer.R.menu;
import edu.neu.madcourse.dankreymer.trickystress.TrickyBluetoothService;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SBSettings extends Activity {

	private Button filteringButton;
	private Button exclusionListButton;
	private Spinner periodicSurveySpinner;
	private TextView timeChoicesText;
	private boolean filterEnabled = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stress_blocker_settings);
        
        filteringButton = (Button) findViewById(R.id.sb_filtering_button);
        exclusionListButton = (Button) findViewById(R.id.sb_exclusion_list_button);
        periodicSurveySpinner = (Spinner) findViewById(R.id.sb_time_choices);
        timeChoicesText = (TextView) findViewById(R.id.sb_time_choices_text);
        
        getCurrentSettings();  
    }
    
    private void getCurrentSettings()
    {
    	if (SBSharedPreferences.getFilteringStatus(this))
    	{
    		filteringButton.setText(getResources().getText(R.string.sb_disable_filtering));
    		exclusionListButton.setEnabled(true);
    		periodicSurveySpinner.setVisibility(View.VISIBLE);
    		timeChoicesText.setVisibility(View.VISIBLE);
    		filterEnabled = true;
    	}
    	else
    	{
    		filteringButton.setText(getResources().getText(R.string.sb_enable_filtering));
    		exclusionListButton.setEnabled(false);
    		periodicSurveySpinner.setVisibility(View.INVISIBLE);
    		timeChoicesText.setVisibility(View.INVISIBLE);
    		filterEnabled = false;
    	}
    }
    
    public void onClickFiltering(View v)
    {
    	SBSharedPreferences.toggleFilteringStatus(this);
    	getCurrentSettings();
    }
    
    public void onSave(View v)
    {
    	if (filterEnabled && periodicSurveySpinner.getSelectedItem() != null)
    	{
    		SBSharedPreferences.putPeriodicInterval(this, periodicSurveySpinner.getSelectedItem().toString());
        	Intent i = new Intent(this, SBPeriodicSurveyService.class);
        	startService(i);
    		
    		//Store Exclusion list crap here.
    	}
    	else
    	{
    		SBSharedPreferences.putPeriodicInterval(this, "");
    	}
    	
    	finish();
    }
    
}
