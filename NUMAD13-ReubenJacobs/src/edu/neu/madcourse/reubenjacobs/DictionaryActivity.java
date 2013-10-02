package edu.neu.madcourse.reubenjacobs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class DictionaryActivity extends Activity {
	
	HashMap<String, String> aTable = new HashMap<String, String>();
	HashMap<String, String> alreadyChecked = new HashMap<String, String>();
	HashMap<String, String> alreadyFound = new HashMap<String, String>();
	
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
		
		/*
		try {
			instream = getAssets().open("wordlist.jet");
			
			if (instream != null) {
				inputreader = new InputStreamReader(instream);
				buffreader = new BufferedReader(inputreader);
				
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
		*/
		
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
				String sString = s.toString().toLowerCase(Locale.US);
				
				if ((sString.length() >= 3) && alreadyFound.get(sString) == null) {
					//String firstLetter = s.subSequence(0, 1).toString().toLowerCase(Locale.US);
					//System.out.println(firstLetter);
					
					//if (alreadyChecked.get(firstLetter) == null) {
						try {
							instream = getAssets().open("wordlist.jet");
					
							if (instream != null) {
								inputreader = new InputStreamReader(instream);
								buffreader = new BufferedReader(inputreader);
								alreadyFound.put(sString, sString); //Tells the program we have already searched for this word before even if it's not an actual word
							
								while ((line = buffreader.readLine()) != null && line.compareToIgnoreCase(sString) < 1) {
									//if (line.substring(0, 1).equals(firstLetter)) {
									
									//if (s.length() >= 3) {
										
										
										
										if (line.equals(sString)) {
											TextView wordList = (TextView) findViewById(R.id.wordList);
											String currentList = wordList.getText().toString();
											if (i == 0) { wordList.setText(sString); }
											else {wordList.setText(sString + ", " + currentList); }
					 						
											i++;
										    
											
										    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
										    toneG.startTone(ToneGenerator.TONE_PROP_BEEP);
										    break;
										}
									//}										
										
										
										
										
										
										//aTable.put(line, line);
										//System.out.println(line);
									//}
								}
							
								//alreadyChecked.put(firstLetter, firstLetter);
								buffreader.close();
								inputreader.close();
								instream.close();
							
							}
						} catch (java.io.IOException e) {
						
						}
					//}
				}
			}
		});
		
		//Scale the button margins to screen size
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		
		int sideMargin = (int)outMetrics.widthPixels/5;
		int topMargin = (int)outMetrics.heightPixels/25;
		
		EditText inputText = (EditText) this.findViewById(R.id.wordInput);
		TextView textView = (TextView) this.findViewById(R.id.wordList);
		Button menuButton = (Button) this.findViewById(R.id.menuButton);
		Button akButton = (Button) this.findViewById(R.id.akButton);
		Button clearButton = (Button) this.findViewById(R.id.clearButton);
		
		LayoutParams inputParams = (RelativeLayout.LayoutParams) inputText.getLayoutParams();
		inputParams.setMargins(sideMargin, topMargin*2, sideMargin, topMargin);
		inputText.setLayoutParams(inputParams);
		
		LayoutParams textParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
		textParams.setMargins(sideMargin, topMargin, sideMargin, topMargin);
		textView.setLayoutParams(textParams);
		
		LayoutParams menuParams = (RelativeLayout.LayoutParams) menuButton.getLayoutParams();
		menuParams.setMargins(sideMargin, topMargin, sideMargin, topMargin);
		menuButton.setLayoutParams(menuParams);
		
		LayoutParams akParams = (RelativeLayout.LayoutParams) akButton.getLayoutParams();
		akParams.setMargins(sideMargin, topMargin, sideMargin, topMargin);
		akButton.setLayoutParams(akParams);

		LayoutParams clearParams = (RelativeLayout.LayoutParams) clearButton.getLayoutParams();
		clearParams.setMargins(sideMargin, topMargin, sideMargin, topMargin);
		clearButton.setLayoutParams(clearParams);
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
	
	public void openAck(View view) {
		Intent intent = new Intent(this, Acknowledgements.class);
		startActivity(intent);
	}
	
	public void returnToMenu(View view) {
		finish();
	}

}
