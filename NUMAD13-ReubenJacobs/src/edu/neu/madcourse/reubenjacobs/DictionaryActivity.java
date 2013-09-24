package edu.neu.madcourse.reubenjacobs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class DictionaryActivity extends Activity {
	
	Hashtable<String, String> aTable = new Hashtable<String, String>();
	Hashtable<String, String> bTable = new Hashtable<String, String>();
	Hashtable<String, String> cTable = new Hashtable<String, String>();
	Hashtable<String, String> dTable = new Hashtable<String, String>();
	Hashtable<String, String> eTable = new Hashtable<String, String>();
	Hashtable<String, String> fTable = new Hashtable<String, String>();
	Hashtable<String, String> gTable = new Hashtable<String, String>();
	Hashtable<String, String> hTable = new Hashtable<String, String>();
	Hashtable<String, String> iTable = new Hashtable<String, String>();
	Hashtable<String, String> jTable = new Hashtable<String, String>();
	Hashtable<String, String> kTable = new Hashtable<String, String>();
	Hashtable<String, String> lTable = new Hashtable<String, String>();
	Hashtable<String, String> mTable = new Hashtable<String, String>();
	Hashtable<String, String> nTable = new Hashtable<String, String>();
	Hashtable<String, String> oTable = new Hashtable<String, String>();
	Hashtable<String, String> pTable = new Hashtable<String, String>();
	Hashtable<String, String> qTable = new Hashtable<String, String>();
	Hashtable<String, String> rTable = new Hashtable<String, String>();
	Hashtable<String, String> sTable = new Hashtable<String, String>();
	Hashtable<String, String> tTable = new Hashtable<String, String>();
	Hashtable<String, String> uTable = new Hashtable<String, String>();
	Hashtable<String, String> vTable = new Hashtable<String, String>();
	Hashtable<String, String> wTable = new Hashtable<String, String>();
	Hashtable<String, String> xTable = new Hashtable<String, String>();
	Hashtable<String, String> yTable = new Hashtable<String, String>();
	Hashtable<String, String> zTable = new Hashtable<String, String>();
	
	InputStream instream;
	InputStreamReader inputreader;
	BufferedReader buffreader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionary);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		try {
			instream = getAssets().open("wordlist.jet");
			
			if (instream != null) {
				inputreader = new InputStreamReader(instream);
				buffreader = new BufferedReader(inputreader, 5000000);
				
				
				//Build the hashtable for words that begin with a
				String line;
				String firstLetter = "a";
				while ((line = buffreader.readLine()) != null) {
						if (line.substring(0, 1).equals(firstLetter)) {
							aTable.put(line, line);
							System.out.println(line);
						}
				}
			}
		} catch (java.io.IOException e) {
			
		}
		
		EditText textInput = (EditText) findViewById(R.id.wordInput);
		textInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				System.out.println("foo");
				
			}
			
		});
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
		getMenuInflater().inflate(R.menu.dictionary, menu);
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

}
