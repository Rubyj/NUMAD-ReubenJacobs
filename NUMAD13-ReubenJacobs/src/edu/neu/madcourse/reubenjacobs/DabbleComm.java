
package edu.neu.madcourse.reubenjacobs;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import edu.neu.mhealth.api.KeyValueAPI;

public class DabbleComm extends Activity {
   
   private String userName;
   private String opponentUserName;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      //Retrieve the username entered on the welcomeComm activity
      Bundle extras = getIntent().getExtras();
      this.userName = extras.getString("USER");
     
      setContentView(R.layout.activity_dabble_comm);
      
      new CreateTask().execute(this.getUser());
      new ConnectTask().execute(this.getUser());
      
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

class DisconnectTask extends AsyncTask<String, Void, Void> {
	protected Void doInBackground(String... strings) {
		  KeyValueAPI.put("sloth_nation", "fromunda", strings[0], "inactive");
	      
	      return null;
	}
}

class SendScoreTask extends AsyncTask<String, Void, Void> {
	protected Void doInBackground(String... strings) {
		  
		  //Stores the score by time. (User-Opponent:UserWhoSentScore, TimeLeft)
		  KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], strings[2]);
	      
	      return null;
	}
}

class CreateGameTask extends AsyncTask<String, Void, Void> {
	protected Void doInBackground(String... strings) {
		  //Stores the game (User-Opponent, game)
		  KeyValueAPI.put("sloth_nation", "fromunda", strings[0] + "-" + strings[1], "game");
	      
	      return null;
	}
}


