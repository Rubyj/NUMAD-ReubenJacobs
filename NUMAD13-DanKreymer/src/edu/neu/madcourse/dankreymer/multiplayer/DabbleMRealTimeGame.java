package edu.neu.madcourse.dankreymer.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.raw;
import edu.neu.madcourse.dankreymer.keys.Keys;
import edu.neu.madcourse.dankreymer.keys.ServerError;
import edu.neu.madcourse.dankreymer.misc.Music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

public class DabbleMRealTimeGame extends Activity{
	private static final String TAG = "Dabble";

	//various keys
	private static final String KEY_GET_TILES = "TILES";
	private static final String KEY_GET_SELECTED = "SELECTED";
	private static final String KEY_GET_TIME = "TIME";
	protected static final String KEY_GET_SCORE = "SCORE";
	protected static final String KEY_MUSIC = "music";
	protected static final String KEY_SOLUTION_1 = "solution1";
	protected static final String KEY_SOLUTION_2 = "solution2";
	protected static final String KEY_SOLUTION_3 = "solution3";
	protected static final String KEY_SOLUTION_4 = "solution4";
	protected static final String KEY_CLOSE_HINT = "finish";
	protected static final String KEY_GAME_OVER = "over";
	
	protected static final String KEY_WIN = "win";
	protected static final String KEY_LOSE = "lose";
	protected static final String KEY_GAME_RESULT = "result";

	private static final int REQUEST_CODE = 1;
	
	//the list of point values assigned to letters
	private static final Map<Character, Integer> letterPoints;
	
	//list of all letters
	private static final List<Character> letters;
	private static final int numLetters;
	
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
		
		letters = new ArrayList<Character>(letterPoints.keySet());
		numLetters = letters.size();
	}
	
	//seconds of gameplay at the start of a game
	private final static int maxTime = 180;

	private final static int numTiles = 18;
	
	private final static int fullBoardBonus = 500;
	
	private DabbleMRealTimeGame context = this;
	
	private CountDownTimer timer;
	private Timer checkGameOverTimer;
	
	private Set<String> dictionary;
	private List<String> lettersLoaded;

	private DabbleMRealTimeView dabbleView;
	
	private SoundPool sp;
	private int soundID_letter;
	private int soundID_word;
	private int soundID_game_over;
	private int soundID_game_won;
	private int soundID_shake;

	private ArrayList<String> solution;
	private char[] tiles;
	
	private int selected;
	private int time;
	private int score;
	
	private boolean gameOver;
	private boolean paused;
	private boolean playMusic;
	
	private int shakeUpCounter;
	
	private Random rand;
	
	private String username;
	private String otherUsername;
	
	private boolean surrender;
	private boolean[] otherPlayerRows;

	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity

	private long last, current;
	
	private static long SHAKE_INTERVAL = 5000;
	
	private SensorManager sensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		
		last = System.currentTimeMillis();
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;

		dictionary = new HashSet<String>();
		lettersLoaded = new ArrayList<String>();
		surrender = false;
		gameOver = false;
		
		shakeUpCounter = 2;
		
		otherPlayerRows = new boolean[4];
		otherPlayerRows[0] = false;
		otherPlayerRows[1] = false;
		otherPlayerRows[2] = false;
		otherPlayerRows[3] = false;
		
		//set up sound pool.
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		soundID_letter = sp.load(this, R.raw.dabble_letter_clicked, 1);
		soundID_word = sp.load(this, R.raw.dabble_new_word, 1);
		soundID_game_over = sp.load(this, R.raw.dabble_game_over, 1);
		soundID_game_won = sp.load(this, R.raw.dabble_game_won, 1);
		soundID_shake = sp.load(this, R.raw.dabble_shakeup, 1);
		
		Bundle bundle = getIntent().getExtras();
		
		username = bundle.getString(DabbleMRealTime.USERNAME);
		otherUsername = bundle.getString(DabbleMRealTime.OTHER_USERNAME);

		selected = -1;
		score = 0;
		rand = new Random();
		generateSolution();
		generateTiles();
		playMusic = true;
		time = maxTime;
		paused = false;
		gameOver = false;
		
		new UserInGameTask().execute();
		startTimer();
		Music.play(this, R.raw.dabble_music);
		
		dabbleView = new DabbleMRealTimeView(this);
		setContentView(dabbleView);
		dabbleView.requestFocus();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		pauseTimer();
		if (playMusic){
			Music.stop(this);
		}
	}
	
	@Override
	public void onBackPressed() {
		goBack();
	}
	
	protected void onPause(){
		sensorManager.unregisterListener(mSensorListener);
		super.onPause();
		checkGameOverTimer.cancel();
		checkGameOverTimer.purge();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(mSensorListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		if (playMusic){
			Music.play(this, R.raw.dabble_music);
		}
		
		checkGameOverTimer = new Timer();
		checkGameOverTimer.scheduleAtFixedRate(CheckGameOverTimerTask(), new Date(), 500);
		checkGameOverTimer.scheduleAtFixedRate(GetRowCompletedTimerTask(), new Date(), 250);
		checkGameOverTimer.scheduleAtFixedRate(GetShakeTimerTask(), new Date(), 500);
	}
	
	private TimerTask GetShakeTimerTask()
	{
		return new TimerTask(){

			@Override
			public void run() {
				new GetShakeTask().execute();
			}};
	}
	
	private TimerTask CheckGameOverTimerTask()
	{
		return new TimerTask(){

			@Override
			public void run() {
				new CheckGameOverTask().execute();
			}};
	}

	private TimerTask GetRowCompletedTimerTask()
	{
		return new TimerTask(){

			@Override
			public void run() {
				new GetRowCompletedTask().execute();
			}};
	}
	
	protected boolean getOtherPlayerRow(int row)
	{
		return otherPlayerRows[row - 1];
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == REQUEST_CODE) {
		     if (resultCode == RESULT_CANCELED) {   
		 		 Intent returnIntent = new Intent();
				 setResult(RESULT_OK, returnIntent); 
		         finish();
		     }
		  }
		}

	private void generateSolution() {
		Random rand = new Random();
		solution = new ArrayList<String>();
		
		Character letter;
		
		letter = letters.get(rand.nextInt(numLetters));
		loadWords(letter.toString());
		solution.add(randomWord(3));
		clearDictionary();
		
		letter = letters.get(rand.nextInt(numLetters));
		loadWords(letter.toString());
		solution.add(randomWord(4));
		clearDictionary();
		
		letter = letters.get(rand.nextInt(numLetters));
		loadWords(letter.toString());
		solution.add(randomWord(5));
		clearDictionary();
		
		letter = letters.get(rand.nextInt(numLetters));
		loadWords(letter.toString());
		solution.add(randomWord(6));
		clearDictionary();
	}
	
	private void clearDictionary()
	{
		dictionary.clear();
		lettersLoaded.clear();
	}
	
	//grab a random word of specified size from what's loaded in the dictionary.
	private String randomWord(int size)
	{
		String[] array = dictionary.toArray(new String[0]);
		String word = "";
		
		while (word.length() != size)
		{
			word = (String) array[rand.nextInt(array.length)];
		}
		
		return word;
	}

	//randomize the solution and create the board.
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
	
	//randomize the solution and create the board.
	private void shakeUp() {
		String wordsCombined = solution.get(0) + solution.get(1)
				+ solution.get(2) + solution.get(3);
		Random rand = new Random();

		char[] temp_tiles = new char[numTiles];
		int index;
		int count = 0;
		while (wordsCombined != "") {
			index = rand.nextInt(wordsCombined.length());
			char c = wordsCombined.charAt(index);
			temp_tiles[count] = c;
			wordsCombined = wordsCombined.replaceFirst(Character.toString(c),
					"");
			count++;
		}
		
		playShakeUpSound();
		tiles = temp_tiles;
	}

	protected void startTimer() {
		long startTime = time * 1000;
		timer = new CountDownTimer(startTime, 100) {

			public void onTick(long millisUntilFinished) {
				time = (int) (millisUntilFinished / 1000);
				dabbleView.invalidate();
			}

			public void onFinish() {
				if (playMusic)
				{
					toggleMusic();
				}
				selected = -1;
				dabbleView.invalidate();
				gameOver(KEY_LOSE);
			}
		}.start();
	}
	
	protected void pauseTimer() {
		timer.cancel();
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
		
		new UserInGameTask().execute(); //in case internet connection comes on midgame
	}
	
	protected void updateRows(boolean[] rows)
	{
		String ret = "";
		if (rows[0])
		{
			ret += ";" + "0";
		}
		
		if (rows[1])
		{
			ret += ";" + "1";
		}
		
		if (rows[2])
		{
			ret += ";" + "2";
		}
		
		if (rows[3])
		{
			ret += ";" + "3";
		}
		
		if (!ret.equals(""))
		{
			ret.replaceFirst(";", "");
		}
		
		new NewRowCompletedTask().execute(ret);
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
	
	protected int getTimeInSeconds(){
		return time;
	}
	
	//Compute the score using the letterPoints assigned earlier.
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
	
	protected boolean checkOtherPlayerRow(int row)
	{
		return otherPlayerRows[row-1];
	}
	
	//check if a row (1,2,3,4) has a valid word.
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
	
	//show the hint activity.
	protected void showHint()
	{
		Intent i = new Intent(this, DabbleMHint.class);
		i.putExtra(KEY_SOLUTION_1, solution.get(0));
		i.putExtra(KEY_SOLUTION_2, solution.get(1));
		i.putExtra(KEY_SOLUTION_3, solution.get(2));
		i.putExtra(KEY_SOLUTION_4, solution.get(3));
		i.putExtra(KEY_MUSIC, playMusic);
		startActivity(new Intent(i));
	}
	
	protected void toggleMusic()
	{
		playMusic = !playMusic;
		if (playMusic && time > 0)
		{
			Music.play(this, R.raw.dabble_music);
		}
		else
		{
			Music.stop(this);
		}
		
		dabbleView.invalidate();
	}
	
	protected boolean getPlayMusic()
	{
		return playMusic;
	}
	
	protected boolean getPaused()
	{
		return paused;
	}
	
	protected void goBack()
	{
		surrender = true;
		gameOver(KEY_LOSE);
	}
	
	protected void playLetterSound()
	{
		sp.play(soundID_letter, 1, 1, 1, 0, 1f);
	}
	
	protected void playWordSound()
	{
		sp.play(soundID_word, 1, 1, 1, 0, 1f);
	}
	
	protected void playShakeUpSound()
	{
		sp.play(soundID_shake, 1, 1, 1, 0, 1f);
	}
	
	private void playOutOfTimeSound()
	{
		sp.play(soundID_game_over, 1, 1, 1, 0, 1f);
	}
	
	private void playYouWinSound()
	{
		sp.play(soundID_game_won, 1, 1, 1, 0, 1f);
	}
	
	protected void gameOver(String status)
	{
		Intent i = new Intent(this, DabbleMRealTimeGameOver.class);
		pauseTimer();
		gameOver = true;
		if (playMusic){
			Music.stop(this);
		}
		
		if (status.equals(KEY_LOSE))
		{
			playOutOfTimeSound();
			i.putExtra(KEY_GAME_RESULT, KEY_LOSE);
			new GameOutcomeTask().execute(KEY_LOSE);
		}
		else
		{
			score += fullBoardBonus;
			playYouWinSound();
			i.putExtra(KEY_GAME_RESULT, KEY_WIN);
			new GameOutcomeTask().execute(KEY_WIN);
		}
		
		i.putExtra(KEY_GET_SCORE, Integer.toString(score));
		i.putExtra(DabbleMRealTime.USERNAME, username);
		i.putExtra(DabbleMRealTime.OTHER_USERNAME, username);
		startActivityForResult(i, REQUEST_CODE);
	}
	
	protected int getShakeupCounter()
	{
		return shakeUpCounter;
	}
	
	private void useShakeUp()
	{
		if (shakeUpCounter > 0)
		{
			shakeUpCounter =- 1;
			new ShakeUpTask().execute();
		}
	}
	
	private class ShakeUpTask extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... params) {
			return Keys.put(Keys.realTimeGameShakeKey(username, otherUsername, otherUsername), Keys.STATUS_IN_GAME);
		}
	}
	
	private class GetShakeTask extends AsyncTask<String, String, String>{

		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(ServerError.NO_CONNECTION.getText()) && !result.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				context.shakeUp();
			}
		}
		
		@Override
		protected String doInBackground(String... params) {		
			String key = Keys.get(Keys.realTimeGameShakeKey(username, otherUsername, username));
			if (!key.equals(ServerError.NO_CONNECTION.getText()) && !key.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				Keys.clearKey(Keys.realTimeGameShakeKey(username, otherUsername, username));
			}
			return key;
		}
	}
	
	private class UserInGameTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			return Keys.put(Keys.userStatusKey(username), Keys.STATUS_IN_GAME);
		}
	}
	
	private class GameOutcomeTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			return Keys.put(Keys.realTimeGameKey(username, otherUsername), params[0]);
		}
	}
	
	private class NewRowCompletedTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {			
			return Keys.put(Keys.realTimeGameRowKey(username, otherUsername, username), params[0]);
		}
	}
	
	private class GetRowCompletedTask extends AsyncTask<String, String, String>{

		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(ServerError.NO_CONNECTION.getText()) && !result.equals(ServerError.NO_SUCH_KEY.getText()))
			{
				List<String> list = Arrays.asList(result.split(";"));
				
				otherPlayerRows[0] = list.contains("0");
				otherPlayerRows[1] = list.contains("1");
				otherPlayerRows[2] = list.contains("2");
				otherPlayerRows[3] = list.contains("3");
			}
		}
		
		@Override
		protected String doInBackground(String... params) {			
			return Keys.get(Keys.realTimeGameRowKey(username, otherUsername, otherUsername));
		}
	}
	
	private class CheckGameOverTask extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPostExecute(String result) {
			if (result.equals(KEY_WIN) && !gameOver)
			{
				gameOver = true;
				gameOver(KEY_LOSE);
			}
			else if (result.equals(KEY_LOSE) && !gameOver)
			{
				gameOver = true;
				gameOver(KEY_WIN);
			}
		}
		@Override
		protected String doInBackground(String... params) {
			String key = Keys.get(Keys.realTimeGameKey(username, otherUsername));
			if (key.equals(KEY_WIN) || key.equals(KEY_LOSE))
			{
				Keys.clearKey(Keys.realTimeGameKey(username, otherUsername));
			}
			return key;
		}
	}
	
	private final SensorEventListener mSensorListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent se) {
			current = System.currentTimeMillis();
			
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
			mAccelLast = mAccelCurrent;
			mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
			float delta = mAccelCurrent - mAccelLast;
			mAccel = mAccel * 0.9f + delta;
			if (mAccel > 8 && current - last > SHAKE_INTERVAL)
			{
				last = System.currentTimeMillis();
				context.useShakeUp();
			}
			
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
}
