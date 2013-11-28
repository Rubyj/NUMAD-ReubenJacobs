package edu.neu.madcourse.dankreymer.trickystress;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import edu.neu.madcourse.dankreymer.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class TrickyStressBT extends Activity{
    
    final int REQUEST_ENABLE_BT = 1;
    private String deviceID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tricky_stress_bt);
        
        colorButtons();     
    }
    
    private void colorButtons()
    {
        Button b1 = (Button) findViewById(R.id.tricky_survey1);
        b1.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey1), PorterDuff.Mode.MULTIPLY);
        Button b2 = (Button) findViewById(R.id.tricky_survey2);
        b2.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey2), PorterDuff.Mode.MULTIPLY);
        Button b3 = (Button) findViewById(R.id.tricky_survey3);
        b3.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey3), PorterDuff.Mode.MULTIPLY);
        Button b4 = (Button) findViewById(R.id.tricky_survey4);
        b4.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey4), PorterDuff.Mode.MULTIPLY);
        Button b5 = (Button) findViewById(R.id.tricky_survey5);
        b5.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey5), PorterDuff.Mode.MULTIPLY);
        Button b6 = (Button) findViewById(R.id.tricky_survey6);
        b6.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey6), PorterDuff.Mode.MULTIPLY);
        Button b7 = (Button) findViewById(R.id.tricky_survey7);
        b7.getBackground().setColorFilter(getResources().getColor(R.color.tricky_survey7), PorterDuff.Mode.MULTIPLY);
    }
    
    public void onSurveyClick(View v)
    {
        finish();
    }
    
    public void onSurveyBTClick(View v) {
    	Intent i = new Intent(this, TrickyBluetoothService.class);
    	startService(i);
    	finish();
    }
}
