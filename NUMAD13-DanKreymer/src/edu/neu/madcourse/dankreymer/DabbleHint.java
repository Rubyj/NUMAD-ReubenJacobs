package edu.neu.madcourse.dankreymer;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DabbleHint extends Activity {
	@Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.dabble_hint);
	      TextView textBox = (TextView) findViewById(R.id.dabble_hint_text);
	      Bundle solutions = getIntent().getExtras();
	      String newline = System.getProperty("line.separator");
	      textBox.setText(solutions.getString("solution1") + newline + solutions.getString("solution2") + newline
	    		  			+ solutions.getString("solution3") + newline + solutions.getString("solution4"));
	   }
	}