package edu.neu.madcourse.dankreymer;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

public class DabbleGame extends Activity {
	private static final String TAG = "Dabble";

	private static final String KEY_GET_TILES = "TILES";
	private static final String KEY_GET_SELECTED = "SELECTED";
	private static final String KEY_GET_TIME = "TIME";
	private static final String KEY_GET_SCORE = "SCORE";
	
	private static long maxTime = 300;

	private static int numTiles = 18;
	
	private CountDownTimer timer;

	private int selected;
	private int time;
	private long startTime;
	private int score;

	private DabbleView dabbleView;

	private ArrayList<String> solution;
	private char[] tiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();

		if (bundle.getString(Dabble.GAME_STATUS_KEY).equals(Dabble.NEW_GAME)) {
			selected = -1;
			score = 0;
			startTime = maxTime * 1000;
			generateSolution();
			generateTiles();
		}
		else
		{
			SharedPreferences pref = getPreferences(MODE_PRIVATE);
			stringToTiles(pref.getString(KEY_GET_TILES, ""));
			selected = Integer.parseInt(pref.getString(KEY_GET_SELECTED, ""));
			startTime = stringToSeconds(pref.getString(KEY_GET_TIME, "")) * 1000;
			score = Integer.parseInt(pref.getString(KEY_GET_SCORE, ""));
		}
		
		initTimer(startTime);

		dabbleView = new DabbleView(this);
		setContentView(dabbleView);
		dabbleView.requestFocus();
	}
	
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
		editor.putString(KEY_GET_TILES, tilesToString());
		editor.putString(KEY_GET_SELECTED, Integer.toString(selected));
		editor.putString(KEY_GET_TIME, secondsToString());
		editor.putString(KEY_GET_SCORE, Integer.toString(score));
		editor.commit();
	}

	private void generateSolution() {
		// TODO: use dictionary!
		solution = new ArrayList<String>();
		solution.add("cow");
		solution.add("plop");
		solution.add("crops");
		solution.add("lemons");
	}

	private void generateTiles() {
		String wordsCombined = solution.get(0) + solution.get(1)
				+ solution.get(2) + solution.get(3);
		Random rand = new Random();

		tiles = new char[numTiles];
		int index;
		int count = 0;
		while (wordsCombined != "") {
			index = rand.nextInt(wordsCombined.length());
			char c = wordsCombined.charAt(index);
			tiles[count] = c;
			wordsCombined = wordsCombined.replaceFirst(Character.toString(c),
					"");
			count++;
		}
	}

	private void initTimer(long startTime) {
		timer = new CountDownTimer(startTime, 1000) {

			public void onTick(long millisUntilFinished) {
				time = (int) (millisUntilFinished / 1000);
				dabbleView.invalidate();
			}

			public void onFinish() {

			}
		}.start();
	}

	protected String getTileLetter(int i) {
		return Character.toString(tiles[i]);
	}

	protected void swapTiles(int i, int j) {
		char temp = tiles[i];
		tiles[i] = tiles[j];
		tiles[j] = temp;
	}

	private String tilesToString() {
		return new String(tiles);
	}

	private void stringToTiles(String string) {
		tiles = string.toCharArray();
	}

	protected void setSelected(int i) {
		selected = i;
	}

	protected String getSelected() {
		return Integer.toString(selected);
	}
	
	private int stringToSeconds(String string){
		String[] split = string.split(":");
		return Integer.parseInt(split[0]) * 60 + Integer.parseInt(split[1]);
	}
	
	private String secondsToString(){
		int minutes = time / 60;
		int seconds = time - (minutes * 60);
		
		String secondString = seconds < 10 ? "0" + seconds : "" + seconds;
		
		return minutes + ":" + secondString;
	}
	
	protected String getTime(){
		return secondsToString();
	}
	
	protected String getScore(){
		return Integer.toString(score);
	}
}
