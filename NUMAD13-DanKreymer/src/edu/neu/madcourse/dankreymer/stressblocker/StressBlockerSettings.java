package edu.neu.madcourse.dankreymer.stressblocker;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.layout;
import edu.neu.madcourse.dankreymer.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class StressBlockerSettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stress_blocker_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stress_blocker_settings, menu);
        return true;
    }

}
