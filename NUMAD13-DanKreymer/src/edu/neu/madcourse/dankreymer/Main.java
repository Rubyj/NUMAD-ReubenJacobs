/**
 * Main landing screen!
 */
package edu.neu.madcourse.dankreymer;

import edu.neu.madcourse.dankreymer.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.mobileClass.*;

public class Main extends Activity implements OnClickListener {
   private static final String TAG = "Main";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      
      //Check that this phone is authorized
      PhoneCheckAPI.doAuthorization(this);

      // Set up click listeners for all the buttons
      View errorButton = findViewById(R.id.error_button);
      errorButton.setOnClickListener(this);
      View newButton = findViewById(R.id.sudoku_button);
      newButton.setOnClickListener(this);
      View aboutButton = findViewById(R.id.about_button);
      aboutButton.setOnClickListener(this);
      View dictionaryButton = findViewById(R.id.dictionary_button);
      dictionaryButton.setOnClickListener(this);
      View dabbleButton = findViewById(R.id.dabble_button);
      dabbleButton.setOnClickListener(this);
      View exitButton = findViewById(R.id.quit_button);
      exitButton.setOnClickListener(this);
   }

   public void onClick(View v) {
      switch (v.getId()) {
      case R.id.error_button:
    	 //Force an error!
         int[] stupidArray = null;
         stupidArray[6]++;
         break;
      case R.id.dabble_button:
          startActivity(new Intent(this, Dabble.class));
          break;
      case R.id.dictionary_button:
          startActivity(new Intent(this, Dictionary.class));
          break;
      case R.id.about_button:
         startActivity(new Intent(this, About.class));
         break;
      case R.id.sudoku_button:
    	  startActivity(new Intent(this, Sudoku.class));
         break;
      case R.id.quit_button:
         finish();
         break;
      }
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.settings:
         startActivity(new Intent(this, Prefs.class));
         return true;
      // More items go here (if any) ...
      }
      return false;
   }
}