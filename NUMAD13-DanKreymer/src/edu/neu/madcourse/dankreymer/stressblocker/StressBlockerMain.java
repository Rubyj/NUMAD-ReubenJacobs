package edu.neu.madcourse.dankreymer.stressblocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import edu.neu.madcourse.dankreymer.R;

public class StressBlockerMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stress_blocker_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stress_blocker_main, menu);
        return true;
    }
    
    public void onQuit(View view) {
        finish();
    }

    public void onSettings(View view) {
        Intent intent = new Intent(this, StressBlockerSettings.class);
        startActivity(intent);
    }
}
