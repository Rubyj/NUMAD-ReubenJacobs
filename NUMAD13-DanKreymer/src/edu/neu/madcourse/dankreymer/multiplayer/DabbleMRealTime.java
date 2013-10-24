package edu.neu.madcourse.dankreymer.multiplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.keys.Keys;
import edu.neu.madcourse.dankreymer.keys.ServerError;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class DabbleMRealTime extends Fragment implements OnClickListener{

	private Spinner spinner;
	private TextView userStatus; 
	private String user;
	private Context context;
	private Timer timer;
	private Button challengeButton;
	private volatile boolean usersLoaded;
	
	protected static final String USERNAME = "USER";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dabble_m_realtime, container, false);
		
		Bundle bundle = getActivity().getIntent().getExtras();
		user = bundle.getString(DabbleM.USERNAME);
		
		context = this.getActivity();
		usersLoaded = false;
		
		spinner = (Spinner) view.findViewById(R.id.dabble_multiplayer_select);
		userStatus = (TextView) view.findViewById(R.id.dabble_multiplayer_status);
		
		View backButton = view.findViewById(R.id.dabble_back_button);
		backButton.setOnClickListener(this);
		
		challengeButton = (Button) view.findViewById(R.id.dabble_challenge_button);
		challengeButton.setOnClickListener(this);
		
		timer = new Timer();
		
		new updatePlayersTask().execute();
		
		timer.scheduleAtFixedRate(toTimerTask(), new Date(), 1000);
		
        return view;
    }
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dabble_back_button:
			this.getActivity().finish();
			break;
		case R.id.dabble_challenge_button:
			Intent intent = new Intent(this.getActivity(), DabbleMRealTimeGame.class);
			intent.putExtra(USERNAME,user);
			startActivity(intent);
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
	
	public void onDestroyView()
	{
		super.onDestroyView();
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
			
//			if (status.equals(Keys.STATUS_ONLINE))
//			{
				challengeButton.setEnabled(true);
//			}
//			else
//			{
//				challengeButton.setEnabled(false);
//			}
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
