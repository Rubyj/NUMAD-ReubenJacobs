package edu.neu.madcourse.dankreymer.trickystress;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.layout;
import edu.neu.madcourse.dankreymer.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class TrickyStressBTResult extends Activity {
    
    private String seenDeviceID;  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tricky_stress_btresult);

        seenDeviceID = getIntent().getExtras().getString(TrickyBluetoothStoreService.FOUND_DEVICE_ID);
        TextView tv = (TextView) findViewById(R.id.tricky_seen_device_name);
        tv.setText(seenDeviceID);
    }
    
    public void onClickOK(View v)
    {
    	finish();
    }

}
