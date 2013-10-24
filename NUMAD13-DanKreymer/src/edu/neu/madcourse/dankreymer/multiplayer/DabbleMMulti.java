package edu.neu.madcourse.dankreymer.multiplayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import edu.neu.madcourse.dankreymer.R;

public class DabbleMMulti extends FragmentActivity{
	
	private FragmentTabHost TabHost;
	private static String REAL_TIME = "Real Time";
	private static String TURN_BASED = "Turn Based";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_m_multi);
		
		TabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		TabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		TabHost.addTab(TabHost.newTabSpec(REAL_TIME).setIndicator(REAL_TIME),
	            DabbleMRealTime.class, null);
		TabHost.addTab(TabHost.newTabSpec(TURN_BASED).setIndicator(TURN_BASED),
				DabbleMTurnBased.class, null);
	    
	}
}
