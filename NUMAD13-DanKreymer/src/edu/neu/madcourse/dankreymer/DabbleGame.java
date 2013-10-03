package edu.neu.madcourse.dankreymer;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class DabbleGame extends Activity {
	private static final String TAG = "Dabble";
	
	private static int numTiles = 18;

	private DabbleView dabbleView;
	
	private ArrayList<String> solution;
	private char[] tiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		generateSolution();
		generateTiles();

		dabbleView = new DabbleView(this);
		setContentView(dabbleView);
		dabbleView.requestFocus();

	}
	
	private void generateSolution(){
		//TODO: use dictionary!
		solution = new ArrayList<String>();
		solution.add("cow");
		solution.add("plop");
		solution.add("crops");
		solution.add("lemons");
	}
	
	private void generateTiles(){
		String wordsCombined = solution.get(0) + solution.get(1) + solution.get(2) + solution.get(3);
		Random rand = new Random();
		
		tiles = new char[numTiles];
		for (int i = 0; i < numTiles; i++)
		{
			tiles[i] = wordsCombined.charAt(rand.nextInt(18));
		}
	}
	
	private String tilesToString()
	{
		return new String(tiles);
	}
	
	private void StringToTiles(String string)
	{
		tiles = string.toCharArray();
	}
}
