package edu.neu.madcourse.reubenjacobs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DictionaryActivity extends Activity {
	
	Hashtable<String, String> aTable = new Hashtable<String, String>();
	Hashtable<String, String> alreadyChecked = new Hashtable<String, String>();
	Hashtable<String, String> alreadyFound = new Hashtable<String, String>();
	
	InputStream instream;
	InputStreamReader inputreader;
	BufferedReader buffreader;
	String line;
	String firstLetter;
	
	int i = 0; //keep track of where we are in the displayed list
	
	
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
				firstLetter = "a";
				while ((line = buffreader.readLine()) != null) {
						if (line.substring(0, 1).equals(firstLetter)) {
							aTable.put(line, line);
							//System.out.println(line);
						}
				}
				
				alreadyChecked.put(firstLetter, firstLetter);
				buffreader.close();
				inputreader.close();
				instream.close();
				
				
			}
		} catch (java.io.IOException e) {
			
		}
		
		EditText textInput = (EditText) findViewById(R.id.wordInput);
		textInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if ((s.length() != 0)) {
					String firstLetter = s.subSequence(0, 1).toString();
					
					if (alreadyChecked.get(firstLetter) == null) {
						try {
							instream = getAssets().open("wordlist.jet");
					
							if (instream != null) {
								inputreader = new InputStreamReader(instream);
								buffreader = new BufferedReader(inputreader, 5000000);
							
								while ((line = buffreader.readLine()) != null) {
									if (line.substring(0, 1).equals(firstLetter)) {
										aTable.put(line, line);
										System.out.println(line);
									}
								}
							
								alreadyChecked.put(firstLetter, firstLetter);
								buffreader.close();
								inputreader.close();
								instream.close();
							
							}
						} catch (java.io.IOException e) {
						
						}
					}
				}
			
				
				if (s.length() >= 3) {
					String sString = s.toString();
					
					if (aTable.get(s.toString()) != null && alreadyFound.get(sString) == null) {
						TextView wordList = (TextView) findViewById(R.id.wordList);
						String currentList = wordList.getText().toString();
						if (i == 0) { wordList.setText(sString); }
						else {wordList.setText(sString + ", " + currentList); }
 						alreadyFound.put(sString, sString);
						i++;
					}
				}
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
	
	public void onClear(View view) {
		TextView wordList = (TextView) findViewById(R.id.wordList);
		EditText textInput = (EditText) findViewById(R.id.wordInput);
		
		textInput.setText("");
		wordList.setText("");
		alreadyFound.clear();
		i = 0;
	}

}
