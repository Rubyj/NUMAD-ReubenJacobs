package edu.neu.madcourse.reubenjacobs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import edu.neu.mhealth.api.KeyValueAPI;

public class CommPlayerlist extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comm_playerlist);
		
		new GetPlayerListTask(this).execute();
		
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
		getMenuInflater().inflate(R.menu.comm_playerlist, menu);
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
	
	class GetPlayerListTask extends AsyncTask<String, Void, Void> {

		SparseArray<String> playerList = new SparseArray<String>();
		TextView tv;
		String users;
		String[] userList;
		CommPlayerlist instance;
		
		public GetPlayerListTask(CommPlayerlist l) {
			instance = l;
		}
		
		protected Void doInBackground(String... params) {
			users = KeyValueAPI.get("sloth_nation", "fromunda", "users");
			
			userList = users.split("\\s+");
			
			System.out.println(users);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void x) {
			tv = (TextView)instance.findViewById(R.id.playerList);
			
			for (int i = 0; i < userList.length; i++) {
				tv.setText(tv.getText() + "\n" + userList[i]);
			}
		}
		
	}

}


