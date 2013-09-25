package edu.neu.madcourse.dankreymer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class Dictionary extends Activity implements OnClickListener {
	private static final String TAG = "Dictionary";
	private EditText wordBox;
	private TextView wordList;
	private Set<String> dictionary;
	private List<String> lettersLoaded;
	private List<String> wordsDisplayed;
	private SoundPool sp;
	private int soundID;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary);
		
		dictionary = new HashSet<String>();
		lettersLoaded = new ArrayList<String>();
		wordsDisplayed = new ArrayList<String>();
		
		final SoundPool sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		soundID = sp.load(this, R.raw.coin, 1);

		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);
		View returnButton = findViewById(R.id.return_to_menu__button);
		returnButton.setOnClickListener(this);
		View acknowledgementsButton = findViewById(R.id.acknowledgements_button);
		acknowledgementsButton.setOnClickListener(this);

		wordBox = (EditText) findViewById(R.id.word_box);
		wordList = (TextView) findViewById(R.id.word_list);
		wordList.setMovementMethod(new ScrollingMovementMethod());

			//Set up listener for when text is entered into the EditView
			wordBox.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable editable) {
					String editableText = editable.toString();
					String firstLetter = editableText.equals("") ? "" : editableText.substring(0,1);
					
					if (!firstLetter.equals("") && !lettersLoaded.contains(firstLetter))
					{
						loadWords(firstLetter);
					}
					
					if (dictionary.contains(editableText) && !wordsDisplayed.contains(editableText))
					{
						wordList.setText(editableText + "\n" + wordList.getText());
						wordsDisplayed.add(editableText);
						
						sp.play(soundID, 1, 1, 0, 0, 1);
					}

				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub

				}

			});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case (R.id.clear_button):
			wordBox.setText("");
			wordList.setText("");
			wordsDisplayed.clear();
			break;
		case (R.id.return_to_menu__button):
			finish();
			break;
		case (R.id.acknowledgements_button):
			startActivity(new Intent(this, AcknowledgementsDictionary.class));
			break;
		}
	}
	
	private void loadWords(String letter)
	{
		BufferedReader br = null;
		String word;
		
		try {
			br = new BufferedReader(new InputStreamReader(getAssets().open(letter + ".txt")));
			while ((word = br.readLine()) != null)
			{
				dictionary.add(word);
			}
			
			br.close();
		} catch (IOException e1) {
			throw new RuntimeException();
		}
		
		lettersLoaded.add(letter);
	}
}
