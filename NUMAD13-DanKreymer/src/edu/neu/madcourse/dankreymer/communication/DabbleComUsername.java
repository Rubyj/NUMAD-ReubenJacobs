package edu.neu.madcourse.dankreymer.communication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.neu.madcourse.dankreymer.R;

public class DabbleComUsername extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_username);
		
		final EditText editText = (EditText) findViewById(R.id.dabble_username_box);
		
		final Button doneButton = (Button) findViewById(R.id.dabble_done_button);
		
		doneButton.setOnClickListener(new View.OnClickListener() {
			String username = editText.getText().toString();
			
			public void onClick(View view) {
				if (username != ""){
					Intent returnIntent = new Intent();
					returnIntent.putExtra(DabbleCom.USERNAME, username);
					setResult(RESULT_OK, returnIntent);        
					finish();
				}
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);        
		finish();
	}
}
