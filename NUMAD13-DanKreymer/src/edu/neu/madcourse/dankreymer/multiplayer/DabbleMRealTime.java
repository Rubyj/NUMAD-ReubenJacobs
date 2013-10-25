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

	private volatile Spinner spinner;
	private TextView userStatus; 
	private String user;
	private Context context;
	private Timer timer;
	private Button challengeButton;
	private Button playButton;
	private volatile boolean usersLoaded;
	private boolean inviteSent;
	private boolean inviteReceived;
	private String otherPlayer;
	
	protected static final String USERNAME = "USER";
	protected static final String OTHER_USERNAME = "OTHER_USER";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dabble_m_realtime, container, false);
		
		Bundle bundle = getActivity().getIntent().getExtras();
		user = bundle.getString(DabbleM.USERNAME);
		
		context = this.getActivity();
		usersLoaded = false;
		inviteSent = false;
		inviteReceived = false;
		
		spinner = (Spinner) view.findViewById(R.id.dabble_multiplayer_select);
		userStatus = (TextView) view.findViewById(R.id.dabble_multiplayer_status);
		
		View backButton = view.findViewById(R.id.dabble_back_button);
		backButton.setOnClickListener(this);
		
		challengeButton = (Button) view.findViewById(R.id.dabble_challenge_button);
		challengeButton.setOnClickListener(this);
		
		playButton = (Button) view.findViewById(R.id.dabble_accept_challenge_button);
		playButton.setOnClickListener(this);
		
        return view;
    }
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dabble_back_button:
			onDestroyView();
			this.getActivity().finish();
			break;
		case R.id.dabble_challenge_button:
			new sendInviteTask().execute();
			break;
		case R.id.dabble_accept_challenge_button:
			new challengeAcceptedTask().execute();
			new removeInviteTask().execute();
			Intent intent = new Intent(this.getActivity(), DabbleMRealTimeGame.class);
			intent.putExtra(USERNAME,user);
			intent.putExtra(OTHER_USERNAME,otherPlayer);
			startActivity(intent);
			break;
		}
	}
	
	private TimerTask statusTimerTask()
	{
		return new TimerTask(){

			@Override
			public void run() {
				new updatePlayersStatusTask().execute();
			}};
	}
	
	private TimerTask checkInvitesTimerTask()
	{
		return new TimerTask(){

			@Override
			public void run() {
				new checkInviteTask().execute();
			}};
	}
	
	private TimerTask checkGameStartTimerTask()
	{
		return new TimerTask(){

			@Override
			public void run() {
				new gameStartListenerTask().execute();
			}};
	}
	
	public void onPause()
	{
		super.onPause();
		new removeInviteTask().execute();
		timer.cancel();
		timer.purge();
	}
	
	public void onResume()
	{
		super.onResume();
		timer = new Timer();
		
		inviteReceived = false;
		inviteSent = false;
		
		new UserLookingTask().execute();
		new updatePlayersTask().execute();
		
		challengeButton.setText("Challenge!");
		
		timer.scheduleAtFixedRate(statusTimerTask(), new Date(), 1000);
		timer.scheduleAtFixedRate(checkInvitesTimerTask(), new Date(), 1000);
		timer.scheduleAtFixedRate(checkGameStartTimerTask(), new Date(), 500);
	}
	
	public void onDestroyView()
	{
		super.onDestroyView();
		new removeInviteTask().execute();
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
			
			if (status.equals(Keys.STATUS_LOOKING) && !inviteSent && !inviteReceived)
			{
				challengeButton.setEnabled(true);
			}
			else
			{
				challengeButton.setEnabled(false);
			}
		}
		
		@Override
		protected String doInBackground(String... parameter) { 
			if (usersLoaded && spinner != null && spinner.getSelectedItem() != null){
				String user = spinner.getSelectedItem().toString();
				if (user != null)
				{
					return Keys.get(Keys.userStatusKey(user)) + ";" + Keys.get(Keys.userGameplayKey(user));
				}
				else
				{
					return ServerError.NO_SUCH_KEY.getText() + ";" + ServerError.NO_SUCH_KEY.getText();
				}
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
			else if (status.equals(Keys.STATUS_ONLINE) || status.equals(Keys.STATUS_LOOKING))
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
	
	private class UserLookingTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			return Keys.put(Keys.userStatusKey(user), Keys.STATUS_LOOKING);
		}
		
	}
	
	private class sendInviteTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(ServerError.NO_CONNECTION.getText()) && 
					!result.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				challengeButton.setEnabled(false);
				challengeButton.setText("Challenge Sent!");
				inviteSent = true;
			}
			else if (result.equals(ServerError.NO_CONNECTION.getText()))
			{
				challengeButton.setEnabled(false);
				challengeButton.setText("Challenge!!");
			}
			
			challengeButton.invalidate();
		}
		
		@Override
		protected String doInBackground(String... parameter) { 
			otherPlayer = spinner.getSelectedItem().toString();
			return Keys.put(Keys.realTimeInviteKey(otherPlayer), user);
		}
	}
	
	private class checkInviteTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(ServerError.NO_CONNECTION.getText()) && 
					!result.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				playButton.setText("Play " + result);
				otherPlayer = result;
				playButton.setVisibility(View.VISIBLE);
				
				inviteReceived = true;
			}
			else
			{
				playButton.setVisibility(View.INVISIBLE);
			}
			
			playButton.invalidate();
		}
		
		@Override
		protected String doInBackground(String... parameter) { 
			return Keys.get(Keys.realTimeInviteKey(user));
		}
	}
	
	private class removeInviteTask extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... parameter) { 
			if (otherPlayer != null)
			{
				return Keys.clearKey(Keys.realTimeInviteKey(otherPlayer));
			}
			else return "";
		}
	}
	
	private class challengeAcceptedTask extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... parameter) { 
			if (otherPlayer != null)
			{
				return Keys.put(Keys.realTimeGameKey(user,  otherPlayer), Keys.STATUS_REALTIME_INGAME);
			}
			else return "";
		}
	}
	
	private class gameStartListenerTask extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPostExecute(String result) {
			if (result.equals(Keys.STATUS_REALTIME_INGAME))
			{
				new removeInviteTask().execute();
				
				Intent intent = new Intent(getActivity(), DabbleMRealTimeGame.class);
				intent.putExtra(USERNAME,user);
				intent.putExtra(OTHER_USERNAME,otherPlayer);
				startActivity(intent);
				
			}
		}
		
		@Override
		protected String doInBackground(String... parameter) {
			if (otherPlayer != null)
			{
				return Keys.get(Keys.realTimeGameKey(user, otherPlayer));
			}
			else return "";
		}
	}
}
