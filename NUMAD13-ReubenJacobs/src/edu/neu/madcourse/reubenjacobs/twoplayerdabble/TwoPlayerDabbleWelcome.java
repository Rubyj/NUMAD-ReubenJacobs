package edu.neu.madcourse.reubenjacobs.twoplayerdabble;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import edu.neu.madcourse.reubenjacobs.DabbleComm;
import edu.neu.madcourse.reubenjacobs.R;
import edu.neu.mhealth.api.KeyValueAPI;
public class TwoPlayerDabbleWelcome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_player_dabble_welcome);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.two_player_dabble_welcome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onNext(View view) {
        if (this.isNetworkOnline()) {
            TextView tv = (TextView)findViewById(R.id.enterUserName);
            String userName = tv.getText().toString();
        
            new CreateTask().execute(userName);
            new ConnectTask().execute(userName);
        
            Intent intent = new Intent(this, TwoPlayerDabbleHome.class);
            intent.putExtra("USER", userName);
        
            finish();
            startActivity(intent);
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("No internet available");
            builder1.setCancelable(true);
            builder1.setNegativeButton("Back",
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        
	}
	
    public boolean isNetworkOnline() {
        boolean status=false;
           try{
               ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
               NetworkInfo netInfo = cm.getNetworkInfo(0);
               if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                   status= true;
               }else {
                   netInfo = cm.getNetworkInfo(1);
                   if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                       status= true;
               }
           }catch(Exception e){
               e.printStackTrace();  
               return false;
           }
           return status;
   }  
   
   class CreateTask extends AsyncTask<String, Void, Void> {
       protected Void doInBackground(String... strings) {
           
             String users = KeyValueAPI.get("sloth_nation", "fromunda", "users");
             Boolean isUserCreated = users.contains(strings[0]);
             
             //If the username is not found on the server store it
             if (!isUserCreated && !users.contains("Error")) {
                 KeyValueAPI.put("sloth_nation", "fromunda", "users", KeyValueAPI.get("sloth_nation", "fromunda", "users") + " " + strings[0]);
             } else if (users.contains("Error")) {
                 KeyValueAPI.put("sloth_nation", "fromunda", "users", strings[0]);
             }
             
             return null;
       }
   }
   
   class ConnectTask extends AsyncTask<String, Void, Void> {
       protected Void doInBackground(String... strings) {
             KeyValueAPI.put("sloth_nation", "fromunda", strings[0], "active");

             return null;
       }
   }
}
