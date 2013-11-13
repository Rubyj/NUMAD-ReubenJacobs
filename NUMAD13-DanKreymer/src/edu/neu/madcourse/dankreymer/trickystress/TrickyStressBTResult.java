package edu.neu.madcourse.dankreymer.trickystress;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.layout;
import edu.neu.madcourse.dankreymer.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class TrickyStressBTResult extends Activity {
    
    private String deviceID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tricky_stress_btresult);
        // Show the Up button in the action bar.
        
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        deviceID = extras.getString("ID");
        TextView tv = (TextView) findViewById(R.id.idView);
        tv.setText(deviceID + " has been seen 5 times." );
    }

}
