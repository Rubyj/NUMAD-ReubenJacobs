package edu.neu.madcourse.dankreymer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

public class DabbleView extends View {
	private static final String TAG = "Dabble";

	private float size;

	private DabbleGame dabbleGame;

	public DabbleView(Context context) {
		super(context);
		dabbleGame = (DabbleGame) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		size = h / 6f;
	//	getRect(selX, selY, selRect);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	   protected void onDraw(Canvas canvas) {
	      // Draw the background...
	      Paint background = new Paint();
	      background.setColor(getResources().getColor(
	            R.color.puzzle_background));
	      canvas.drawRect(0, 0, getWidth(), getHeight(), background);
	      
	      Paint dark = new Paint();
	      dark.setColor(getResources().getColor(R.color.puzzle_dark));
	      
	      float left, top;
	      float gap = 20;
	      
	      top = 50;
	      left = (getWidth() - size * 3 - gap * 2) / 2;
	      
	      for (int i = 0; i < 3; i++){
	    	  canvas.drawRect(left, top, left + size, top + size, dark);
	    	  left += size + gap;
	      }
	      
	      top += size + gap;;
	      left = (getWidth() - size * 4 - gap * 3) / 2;
	      
	      for (int i = 0; i < 4; i++){
	    	  canvas.drawRect(left, top, left + size, top + size, dark);
	    	  left += size + gap;
	      }
	      
	      top += size + gap;;
	      left = (getWidth() - size * 5 - gap * 4) / 2;
	      
	      for (int i = 0; i < 5; i++){
	    	  canvas.drawRect(left, top, left + size, top + size, dark);
	    	  left += size + gap;
	      }
	      
	      top += size + gap;;
	      left = (getWidth() - size * 6 - gap * 5) / 2;
	      
	      for (int i = 0; i < 6; i++){
	    	  canvas.drawRect(left, top, left + size, top + size, dark);
	    	  left += size + gap;
	      }

	}

}
