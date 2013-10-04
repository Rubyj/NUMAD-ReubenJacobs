package edu.neu.madcourse.dankreymer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
	private final static Map<Character, Integer> letterPoints;
	
	static
	{
		letterPoints = new HashMap<Character, Integer>();
		letterPoints.put('a', 1);
		letterPoints.put('b', 3);
		letterPoints.put('c', 3);
		letterPoints.put('d', 2);
		letterPoints.put('e', 1);
		letterPoints.put('f', 4);
		letterPoints.put('g', 2);
		letterPoints.put('h', 4);
		letterPoints.put('i', 1);
		letterPoints.put('j', 8);
		letterPoints.put('k', 5);
		letterPoints.put('l', 1);
		letterPoints.put('m', 3);
		letterPoints.put('n', 1);
		letterPoints.put('o', 1);
		letterPoints.put('p', 3);
		letterPoints.put('q', 10);
		letterPoints.put('r', 1);
		letterPoints.put('s', 1);
		letterPoints.put('t', 1);
		letterPoints.put('u', 1);
		letterPoints.put('v', 4);
		letterPoints.put('w', 4);
		letterPoints.put('x', 8);
		letterPoints.put('y', 4);
		letterPoints.put('z', 10);
	}
	
	private final static long maxTime = 300;

	private final static int numTiles = 18;
	
	private CountDownTimer timer;
	
	private Set<String> dictionary;
	private List<String> lettersLoaded;

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
		
		dictionary = new HashSet<String>();
		lettersLoaded = new ArrayList<String>();
		
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

	private void loadWords(String letter) {
		if (!lettersLoaded.contains(letter)) {
			BufferedReader br = null;
			String word;

			try {
				br = new BufferedReader(new InputStreamReader(getAssets().open(
						"d_" + letter + ".txt")));
				while ((word = br.readLine()) != null) {
					dictionary.add(word);
				}

				br.close();
			} catch (IOException e1) {
				throw new RuntimeException();
			}

			lettersLoaded.add(letter);
		}
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
		score = 0;
		int tempScore;
		if (checkWord(1))
		{
			tempScore = 0;
			tempScore += letterPoints.get(tiles[0]) + letterPoints.get(tiles[1]) + letterPoints.get(tiles[2]);
			score += tempScore * 5;
		}
		
		tempScore = 0;
		if (checkWord(2))
		{
			tempScore = 0;
			tempScore += letterPoints.get(tiles[3]) + letterPoints.get(tiles[4]) + letterPoints.get(tiles[5])
						+ letterPoints.get(tiles[6]);
			score += tempScore * 10;
		}

		if (checkWord(3))
		{
			tempScore = 0;
			tempScore += letterPoints.get(tiles[7]) + letterPoints.get(tiles[8]) + letterPoints.get(tiles[9])
					+ letterPoints.get(tiles[10]) + letterPoints.get(tiles[11]);
			score += tempScore * 20;
		}

		if (checkWord(4))
		{
			tempScore = 0;
			tempScore += letterPoints.get(tiles[12]) + letterPoints.get(tiles[13]) + letterPoints.get(tiles[14])
					+ letterPoints.get(tiles[15]) + letterPoints.get(tiles[16]) + letterPoints.get(tiles[17]);
			score += tempScore * 40;
		}
		
		return Integer.toString(score);
	}
	
	protected boolean checkWord(int row){
		String word;
		if (row == 1)
		{
			loadWords(Character.toString(tiles[0]));
			word = Character.toString(tiles[0]) + Character.toString(tiles[1]) + Character.toString(tiles[2]);
			return dictionary.contains(word);
		}
		else if (row == 2)
		{
			loadWords(Character.toString(tiles[3]));
			word = Character.toString(tiles[3]) + Character.toString(tiles[4]) + Character.toString(tiles[5])
					+ Character.toString(tiles[6]);
			return dictionary.contains(word);
		}
		else if (row == 3)
		{
			loadWords(Character.toString(tiles[7]));
			word = Character.toString(tiles[7]) + Character.toString(tiles[8]) + Character.toString(tiles[9])
					+ Character.toString(tiles[10]) + Character.toString(tiles[11]);
			return dictionary.contains(word);
		}
		else
		{
			loadWords(Character.toString(tiles[12]));
			word = Character.toString(tiles[12]) + Character.toString(tiles[13]) + Character.toString(tiles[14])
					+ Character.toString(tiles[15]) + Character.toString(tiles[16]) + Character.toString(tiles[17]);
			return dictionary.contains(word);
		}
	}
	
	protected void showHint()
	{
		Intent i = new Intent(this, DabbleHint.class);
		i.putExtra("solution1", solution.get(0));
		i.putExtra("solution2", solution.get(1));
		i.putExtra("solution3", solution.get(2));
		i.putExtra("solution4", solution.get(3));
		startActivity(new Intent(i));
	}
}
