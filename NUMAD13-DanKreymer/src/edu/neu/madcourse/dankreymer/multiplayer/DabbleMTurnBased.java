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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class DabbleMTurnBased extends Fragment implements OnClickListener{
	private volatile Spinner spinner;
	private volatile ListView gameList;
	private TextView userStatus; 
	private String user;
	private Context context;
	private Timer timer;
	private Button newGameButton;
	private Button playButton;
	private volatile boolean usersLoaded;
	private boolean inviteSent;
	private boolean inviteReceived;
	private String otherPlayer;
	
	private List<String> activeGames;
	
	protected static final String USERNAME = "USER";
	protected static final String OTHER_USERNAME = "OTHER_USER";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dabble_m_turnbased, container, false);
		
		Bundle bundle = getActivity().getIntent().getExtras();
		user = bundle.getString(DabbleM.USERNAME);
		
		context = this.getActivity();
		usersLoaded = false;
		activeGames = new ArrayList<String>();
		
		spinner = (Spinner) view.findViewById(R.id.dabble_multiplayer_select);
		gameList = (ListView) view.findViewById(R.id.dabble_game_list);
		
		View backButton = view.findViewById(R.id.dabble_back_button);
		backButton.setOnClickListener(this);
		
		newGameButton = (Button) view.findViewById(R.id.dabble_challenge_button);
		newGameButton.setOnClickListener(this);
		
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
			new newGameTask().execute();
			break;
		}
	}
	
	public void onPause()
	{
		super.onPause();
		timer.cancel();
		timer.purge();
	}
	
	public void onResume()
	{
		super.onResume();
		timer = new Timer();
		
		inviteReceived = false;
		inviteSent = false;
		
		new updatePlayersTask().execute();
		
		timer.scheduleAtFixedRate(updateGamesTimerTask(), new Date(), 1000);
	}
	
	private TimerTask updateGamesTimerTask()
	{
		return new TimerTask(){

			@Override
			public void run() {
				new updateGamesTask().execute();
			}};
	}
	
	private class updateGamesTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(ServerError.NO_CONNECTION.getText()) && 
					!result.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				List<String> games = new ArrayList<String>(Arrays.asList(result.split(",")));
				
				activeGames = games;
				
				ArrayAdapter<String> adapter = 
						new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, games);
				
				
				gameList.setAdapter(adapter);
			}
			else
			{
				gameList.setAdapter(null);
			}
			
			gameList.invalidate();
			
	    	if (activeGames.contains(spinner.getSelectedItem().toString()))
			{
	    		newGameButton.setEnabled(false);
			}
	    	else
	    	{
	    		newGameButton.setEnabled(true);
	    	}
		}
		
		@Override
		protected String doInBackground(String... parameter) { 
			return Keys.get(Keys.turnBasedGamesForPlayerKey(user));
		}
	}
	
	private class updatePlayersTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(ServerError.NO_CONNECTION.getText()) && 
					!result.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				List<String> users = new ArrayList<String>(Arrays.asList(result.split(",")));
				
				users.remove(user);
				
				for (String s : activeGames)
				{
					users.remove(s);
				}
				
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
	
	private class newGameTask extends AsyncTask<String, String, String> {		
		@Override
		protected String doInBackground(String... parameter) { 
			otherPlayer = spinner.getSelectedItem().toString();
			String userGames = Keys.get(Keys.turnBasedGamesForPlayerKey(user));
			String otherGames = Keys.get(Keys.turnBasedGamesForPlayerKey(otherPlayer));
			
			if (!userGames.equals(ServerError.NO_CONNECTION.getText()))
			{
				if (userGames.equals(ServerError.NO_SUCH_KEY.getText()))
				{
					Keys.put(Keys.turnBasedGamesForPlayerKey(user), otherPlayer);
				}
				else
				{
					Keys.put(Keys.turnBasedGamesForPlayerKey(user), userGames + "," + otherPlayer);
				}
			}
			
			if (!otherGames.equals(ServerError.NO_CONNECTION.getText()))
			{
				if (otherGames.equals(ServerError.NO_SUCH_KEY.getText()))
				{
					Keys.put(Keys.turnBasedGamesForPlayerKey(otherPlayer), user);
				}
				else
				{
					Keys.put(Keys.turnBasedGamesForPlayerKey(otherPlayer), otherGames + "," + user);
				}
			}
			
			return Keys.put(Keys.turnBasedGameKey(user, otherPlayer), Keys.TURN_BASED_INVITED);
		}
	}
	
	
}
