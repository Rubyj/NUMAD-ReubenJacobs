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
import android.view.MotionEvent;
import android.view.View;

public class DabbleView extends View {
	private static final String TAG = "Dabble";
	private static final int GAP = 20;
	private static final int TOP_START = 30;

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
		// getRect(selX, selY, selRect);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);

		drawBoard(canvas);
		drawButtons(canvas);
		drawTimer(canvas);
		drawScore(canvas);
	}

	private void drawBoard(Canvas canvas)
	{
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
		
		Paint selectedPaint = new Paint();
		selectedPaint.setColor(getResources().getColor(R.color.dabble_selected_tile));
		selectedPaint.setStyle(Paint.Style.STROKE);
		selectedPaint.setStrokeWidth(3);

		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(size * 0.75f);
		foreground.setTextScaleX(1);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float text_x = size / 2;
		// Centering in Y: measure ascent/descent first
		float text_y = size / 2 - (fm.ascent + fm.descent) / 2;

		float left, top;

		top = TOP_START;
		left = (getWidth() - size * 3 - GAP * 2) / 2;

		int letterCount = 0;
		
		int selected = Integer.parseInt(dabbleGame.getSelected());

		for (int i = 0; i < 3; i++) {
			canvas.drawRect(left, top, left + size, top + size, dark);
			canvas.drawText(dabbleGame.getTileLetter(letterCount), left
					+ text_x, top + text_y, foreground);
			if (letterCount == selected)
			{
				canvas.drawRect(left, top, left + size, top + size, selectedPaint);
			}
			
			left += size + GAP;
			letterCount++;
		}

		top += size + GAP;
		
		left = (getWidth() - size * 4 - GAP * 3) / 2;

		for (int i = 0; i < 4; i++) {
			canvas.drawRect(left, top, left + size, top + size, dark);
			canvas.drawText(dabbleGame.getTileLetter(letterCount), left
					+ text_x, top + text_y, foreground);
			if (letterCount == selected)
			{
				canvas.drawRect(left, top, left + size, top + size, selectedPaint);
			}
			
			left += size + GAP;
			letterCount++;
		}

		top += size + GAP;
		
		left = (getWidth() - size * 5 - GAP * 4) / 2;

		for (int i = 0; i < 5; i++) {
			canvas.drawRect(left, top, left + size, top + size, dark);
			canvas.drawText(dabbleGame.getTileLetter(letterCount), left
					+ text_x, top + text_y, foreground);
			if (letterCount == selected)
			{
				canvas.drawRect(left, top, left + size, top + size, selectedPaint);
			}
			
			left += size + GAP;
			letterCount++;
		}

		top += size + GAP;

		left = (getWidth() - size * 6 - GAP * 5) / 2;

		for (int i = 0; i < 6; i++) {
			canvas.drawRect(left, top, left + size, top + size, dark);
			canvas.drawText(dabbleGame.getTileLetter(letterCount), left
					+ text_x, top + text_y, foreground);
			if (letterCount == selected)
			{
				canvas.drawRect(left, top, left + size, top + size, selectedPaint);
			}
			
			left += size + GAP;
			letterCount++;
		}
	}
	private void drawButtons(Canvas canvas)
	{
		int size_x = 120;
		int size_y = 80;
		
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));

		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(size_y * 0.5f);
		foreground.setTextScaleX(1);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float text_x = size_x / 2;
		// Centering in Y: measure ascent/descent first
		float text_y = size_y / 2 - (fm.ascent + fm.descent) / 2;

		float left, top;

		top = getHeight() - size_y;
		left = 0;

		canvas.drawRect(left, top, left + size_x, top + size_y, dark);
		canvas.drawText("Hint", left + text_x, top + text_y, foreground);
		
		top = getHeight() - size_y;
		left = getWidth() - size_x;

		canvas.drawRect(left, top, left + size_x, top + size_y, dark);
		canvas.drawText("Music", left + text_x, top + text_y, foreground);
	}
	
	private void drawTimer(Canvas canvas)
	{
		int size_x = 120;
		int size_y = 80;
		
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));

		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(size_y * 0.5f);
		foreground.setTextScaleX(1);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float text_x = size_x / 2;
		// Centering in Y: measure ascent/descent first
		float text_y = size_y / 2 - (fm.ascent + fm.descent) / 2;

		float left, top;

		top = 0;
		left = 0;

		canvas.drawRect(left, top, left + size_x, top + size_y, dark);
		canvas.drawText(dabbleGame.getTime(), left + text_x, top + text_y, foreground);
	}
	
	private void drawScore(Canvas canvas)
	{
		int size_x = 120;
		int size_y = 80;
		
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));

		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(size_y * 0.5f);
		foreground.setTextScaleX(1);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float text_x = size_x / 2;
		// Centering in Y: measure ascent/descent first
		float text_y = size_y / 2 - (fm.ascent + fm.descent) / 2;

		float left, top;

		top = 0;
		left = getWidth() - size_x;

		canvas.drawRect(left, top, left + size_x, top + size_y, dark);
		canvas.drawText(dabbleGame.getScore(), left + text_x, top + text_y, foreground);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);

		int selected = Integer.parseInt(dabbleGame.getSelected());
		if (selected == -1)
		{
			markSelected(event.getX(), event.getY());
		}
		else
		{
			swapSelected(selected, event.getX(), event.getY());
		}
		
		return true;
	}
	
	private void swapSelected(int selected1, float x, float y)
	{
		int selected2 = getSelectedTile(x,y);
		if (selected2 != -1)
		{
			dabbleGame.swapTiles(selected1, selected2);
		}
		dabbleGame.setSelected(-1);
		invalidate();
	}
	
	private void markSelected(float x, float y)
	{
		int selected = getSelectedTile(x, y);
		dabbleGame.setSelected(selected);
		invalidate();
	}
	
	private int getSelectedTile(float x, float y)
	{
		int count = 0;
		
		float left, top;
		left = (getWidth() - size * 3 - GAP * 2) / 2;	
		top = TOP_START;

		for (int i = 0; i < 3; i++) {
			if ((x >= left && x <= left + size) && (y >= top && y <= top + size))
			{
				return count;
			}
			left += size + GAP;
			count++;		
		}
		
		top += size + GAP;
		left = (getWidth() - size * 4 - GAP * 3) / 2;
		
		for (int i = 0; i < 4; i++) {
			if ((x >= left && x <= left + size) && (y >= top && y <= top + size))
			{
				return count;
			}
			left += size + GAP;
			count++;		
		}
		
		top += size + GAP;
		left = (getWidth() - size * 5 - GAP * 4) / 2;
		
		for (int i = 0; i < 5; i++) {
			if ((x >= left && x <= left + size) && (y >= top && y <= top + size))
			{
				return count;
			}
			left += size + GAP;
			count++;		
		}
		
		top += size + GAP;
		left = (getWidth() - size * 6 - GAP * 5) / 2;
		
		for (int i = 0; i < 6; i++) {
			if ((x >= left && x <= left + size) && (y >= top && y <= top + size))
			{
				return count;
			}
			left += size + GAP;
			count++;		
		}
		
		return -1;
	}

}
