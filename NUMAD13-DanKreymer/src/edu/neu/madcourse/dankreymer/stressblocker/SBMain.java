package edu.neu.madcourse.dankreymer.stressblocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import edu.neu.madcourse.dankreymer.R;

public class SBMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stress_blocker_main);
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
}
