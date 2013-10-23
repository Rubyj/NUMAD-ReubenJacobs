
package edu.neu.madcourse.reubenjacobs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import edu.neu.mhealth.api.KeyValueAPI;

public class DabbleComm extends Activity {
   
   private String userName;
   private String user2Name;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      //Retrieve the username entered on the welcomeComm activity
      Bundle extras = getIntent().getExtras();
      this.userName = extras.getString("USER");
     
      setContentView(R.layout.activity_dabble_comm);
            
      Display display = getWindowManager().getDefaultDisplay();
      DisplayMetrics outMetrics = new DisplayMetrics();
      display.getMetrics(outMetrics);
		
      int sideMargin = (int)outMetrics.widthPixels/25;
      int topMargin = (int)outMetrics.heightPixels/35 ;
      
   }

   @Override
   protected void onResume() {
      super.onResume();
   }

   @Override
   protected void onPause() {
	   super.onPause();
   }
   
   @Override
   protected void onStop() {
	   super.onStop();
	   new DisconnectTask().execute(this.userName);
   }
   
   public String getUser() {
	   return this.userName;
   }
   
   public void onList(View view) {
		Intent intent = new Intent(this, CommPlayerlist.class);
		startActivity(intent);
   }
   
   public void onNewGame(View view) {
		TextView tv = (TextView)findViewById(R.id.oppponentName);
		String opponentName = tv.getText().toString();
		
		Intent intent = new Intent(this, CommGame.class);
		intent.putExtra("USER", this.userName);
		intent.putExtra("OPPONENT", opponentName);
		intent.putExtra("GAME", "NEW");
		this.user2Name = opponentName;
		
		if (isNetworkOnline()) {
			new CreateGameTask().execute(this.userName, opponentName);
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
   
   public void onJoinGame(View view) {
		TextView tv = (TextView)findViewById(R.id.oppponentName);
		String opponentName = tv.getText().toString();
		
		Intent intent = new Intent(this, CommGame.class);
		intent.putExtra("USER", this.userName);
		intent.putExtra("OPPONENT", opponentName);
		intent.putExtra("GAME", "JOIN");
		this.user2Name = opponentName;
		
		if (isNetworkOnline()) {
			new JoinGameTask().execute(this.userName, opponentName);
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
   
   class CreateGameTask extends AsyncTask<String, Void, Void> {
		protected Void doInBackground(String... strings) {
			  
			  //Stores the game (User-Opponent, game)
			  KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], "game:0");
		      
		      return null;
		      
		      //Do something to store the dabble board so opponent can receive
		}
	}
   
   class JoinGameTask extends AsyncTask<String, Void, Void> {
		protected Void doInBackground(String... strings) {
			  String value = KeyValueAPI.get("sloth_nation", "fromunda", strings[0] + "-" + strings[1]);
		      
		      //Do something to ensure that the dabble board loaded is the same as the users who started the game
		      
		      return null;
		}
	}
}

class DisconnectTask extends AsyncTask<String, Void, Void> {
	protected Void doInBackground(String... strings) {
		  KeyValueAPI.put("sloth_nation", "fromunda", strings[0], "inactive");
	      
	      return null;
	}
}