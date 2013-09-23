package edu.neu.madcourse.dankreymer;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Dictionary extends Activity implements OnClickListener{
	private static final String TAG = "Dictionary";
	private EditText wordBox;
	private TextView wordList;
	
	   public void onCreate(Bundle savedInstanceState) 
	   {
		      super.onCreate(savedInstanceState);
		      setContentView(R.layout.dictionary);
		      
		      View clearButton = findViewById(R.id.clear_button);
		      clearButton.setOnClickListener(this);
		      
		      wordBox = (EditText)findViewById(R.id.word_box);
		      wordList = (TextView)findViewById(R.id.word_list);
		      
		      wordBox.addTextChangedListener(new TextWatcher() 
		      {

				@Override
				public void afterTextChanged(Editable editable) {
					wordList.setText(editable.toString() + "\n" + wordList.getText());
					
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
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
			break;
		}	
	}
}
