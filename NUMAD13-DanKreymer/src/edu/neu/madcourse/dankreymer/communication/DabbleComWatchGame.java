package edu.neu.madcourse.dankreymer.communication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.keys.Keys;
import edu.neu.madcourse.dankreymer.keys.ServerError;

public class DabbleComWatchGame extends Activity implements OnClickListener{

	private Spinner spinner;
	private TextView userStatus; 
	private TextView userGameplay;
	private String user;
	private Context context;
	private Timer timer;
	private Handler handler;
	private Runnable runnable;
	private volatile boolean usersLoaded;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_watch_game);

		Bundle bundle = getIntent().getExtras();
		user = bundle.getString(DabbleCom.USERNAME);
		
		context = this;
		usersLoaded = false;
		
		spinner = (Spinner) findViewById(R.id.dabble_player_select);
		userStatus = (TextView) findViewById(R.id.dabble_player_status);
		userGameplay = (TextView) findViewById(R.id.dabble_game_moves);
		
		View instructionsButon = findViewById(R.id.dabble_back_button);
		instructionsButon.setOnClickListener(this);
		
		timer = new Timer();
		
		new updatePlayersTask().execute();
		
		timer.scheduleAtFixedRate(toTimerTask(), new Date(), 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dabble_back_button:
			finish();
			break;
		}
	}
	
	private TimerTask toTimerTask()
	{
		return new TimerTask(){

			@Override
			public void run() {
				new updatePlayersStatusTask().execute();
			}};
	}
	
	protected void onDestroy()
	{
		super.onDestroy();
		timer.cancel();
		timer.purge();
	}
	
	private class updatePlayersTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(ServerError.NO_CONNECTION.getText()) && 
					!result.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				List<String> users = new ArrayList<String>(Arrays.asList(result.split(",")));
				
				users.remove(user);
				
				ArrayAdapter<String> adapter = 
						new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, users);
				
				
				spinner.setAdapter(adapter);
			}
			
			spinner.invalidate();
			usersLoaded = true;
		}
		
		@Override
		protected String doInBackground(String... parameter) { 
			return Keys.get(Keys.USERS);
		}
	}
	
	private class updatePlayersStatusTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String result) {
			String[] split = result.split(";");
			String status = split[0];
			String gameplay = split[1];
			
			if (status.equals(ServerError.NO_CONNECTION.getText()) || 
					status.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				userStatus.setText("Status: ");
			}
			else
			{
				userStatus.setText("Status: " + status);
			}
			setTextColor(status);
			userStatus.invalidate();
			
			if (gameplay.equals(ServerError.NO_CONNECTION.getText()) || 
					gameplay.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				userGameplay.setText("");
			}
			else
			{
				char[] a = gameplay.toCharArray();
				userGameplay.setText(a[0] + " " +  a[1] + " " + a[2] + "\n" + 
								     a[3] + " " +  a[4] + " " + a[5] + " " + a[6] + "\n" + 
								     a[7] + " " +  a[8] + " " + a[9] + " " + a[10] + " " + a[11] +  "\n" +
								     a[13] + " " +  a[14] + " " + a[15] + " " +  a[16] + " " + a[17]) ;
			}		
			userGameplay.invalidate();
			
		}
		
		@Override
		protected String doInBackground(String... parameter) { 
			if (usersLoaded){
				String user = spinner.getSelectedItem().toString();
				return Keys.get(Keys.userStatusKey(user)) + ";" + Keys.get(Keys.userGameplayKey(user));
			}
			else
			{
				return ServerError.NO_SUCH_KEY.getText() + ";" + ServerError.NO_SUCH_KEY.getText();
			}
		}
		
		private void setTextColor(String status)
		{
			if (status.equals(Keys.STATUS_OFFLINE))
			{
				userStatus.setTextColor(getResources().getColor(R.color.dabble_red_text));
			}
			else if (status.equals(Keys.STATUS_ONLINE))
			{
				userStatus.setTextColor(getResources().getColor(R.color.dabble_green_text));
			}
			else if (status.equals(Keys.STATUS_IN_GAME))
			{
				userStatus.setTextColor(getResources().getColor(R.color.dabble_selected_tile));
			}
			else
			{
				userStatus.setTextColor(getResources().getColor(R.color.dabble_background_text));
			}
		}
	}
	
}
