package edu.neu.madcourse.dankreymer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Dictionary extends Activity implements OnClickListener{
	private static final String TAG = "Dictionary";
	
	   public void onCreate(Bundle savedInstanceState) 
	   {
		      super.onCreate(savedInstanceState);
		      setContentView(R.layout.dictionary);
		      
		      View wordBox = findViewById(R.id.word_box);
		      wordBox.setOnClickListener(this);
	   }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.word_box):
			
		}
		
	}
}
