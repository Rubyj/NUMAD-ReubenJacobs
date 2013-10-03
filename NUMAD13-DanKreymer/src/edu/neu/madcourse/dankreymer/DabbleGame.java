package edu.neu.madcourse.dankreymer;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class DabbleGame extends Activity {
	private static final String TAG = "Dabble";

	private static final String KEY_RESUME_GAME = "RESUME";
	private static final String KEY_GET_TILES = "TILES";
	private static final String KEY_GET_SELECTED = "SELECTED";

	private static int numTiles = 18;

	private int selected = -1;

	private DabbleView dabbleView;

	private ArrayList<String> solution;
	private char[] tiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    SharedPreferences pref = getPreferences(MODE_PRIVATE);
		if (!pref.contains(KEY_GET_TILES)) {
			generateSolution();
			generateTiles();
		}
		else
		{
			stringToTiles(pref.getString(KEY_GET_TILES, ""));
			selected = Integer.parseInt(pref.getString(KEY_GET_SELECTED, ""));
		}

		dabbleView = new DabbleView(this);
		setContentView(dabbleView);
		dabbleView.requestFocus();
	}
	
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
		editor.putString(KEY_GET_TILES, tilesToString());
		editor.putString(KEY_GET_SELECTED, Integer.toString(selected));
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
}
