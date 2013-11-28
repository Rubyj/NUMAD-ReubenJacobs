package edu.neu.madcourse.dankreymer.dabble;

import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.R.color;
import android.content.Context;
import android.content.Intent;
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
	private static final int GAP = 10;
	private static final int TOP_START = 20;
	private boolean[] wordsAlreadyPlayed;

	private float size, button_size_x, button_size_y;


	private DabbleGame dabbleGame;

	public DabbleView(Context context) {
		super(context);
		dabbleGame = (DabbleGame) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
		wordsAlreadyPlayed = new boolean[4];
		wordsAlreadyPlayed[0] = false;
		wordsAlreadyPlayed[1] = false;
		wordsAlreadyPlayed[2] = false;
		wordsAlreadyPlayed[3] = false;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		size = h / 5f;
		button_size_x = 120;
		button_size_y = 72;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (wordsAlreadyPlayed[0] && wordsAlreadyPlayed[1] && wordsAlreadyPlayed[2] && wordsAlreadyPlayed[3])
		{
			dabbleGame.gameOver();
		}
		
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.dabble_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);

		drawBoard(canvas);
		drawButtons(canvas);
		drawTimer(canvas);
		drawScore(canvas);
		drawPause(canvas);
		drawBack(canvas);
	}

	private void drawBoard(Canvas canvas)
	{
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.dabble_tile_background));
		
		Paint selectedPaint = new Paint();
		selectedPaint.setColor(getResources().getColor(R.color.dabble_selected_tile));
		selectedPaint.setStyle(Paint.Style.STROKE);
		selectedPaint.setStrokeWidth(4);

		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.dabble_text));
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

		if (dabbleGame.checkWord(1))
		{
			foreground.setColor(getResources().getColor(R.color.dabble_valid_word));
			if (!wordsAlreadyPlayed[0]){
				dabbleGame.playWordSound();
				wordsAlreadyPlayed[0] = true;
			}
		}
		else
		{
			wordsAlreadyPlayed[0] = false;
		}
		
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
		
		foreground.setColor(getResources().getColor(R.color.dabble_text));

		top += size + GAP;
		
		left = (getWidth() - size * 4 - GAP * 3) / 2;
		
		if (dabbleGame.checkWord(2))
		{
			foreground.setColor(getResources().getColor(R.color.dabble_valid_word));
			if (!wordsAlreadyPlayed[1]){
				dabbleGame.playWordSound();
				wordsAlreadyPlayed[1] = true;
			}
		}
		else
		{
			wordsAlreadyPlayed[1] = false;
		}

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
		
		foreground.setColor(getResources().getColor(R.color.dabble_text));

		top += size + GAP;
		
		left = (getWidth() - size * 5 - GAP * 4) / 2;

		if (dabbleGame.checkWord(3))
		{
			foreground.setColor(getResources().getColor(R.color.dabble_valid_word));
			if (!wordsAlreadyPlayed[2]){
				dabbleGame.playWordSound();
				wordsAlreadyPlayed[2] = true;
			}
		}
		else
		{
			wordsAlreadyPlayed[2] = false;
		}
		
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
		
		foreground.setColor(getResources().getColor(R.color.dabble_text));

		top += size + GAP;

		left = (getWidth() - size * 6 - GAP * 5) / 2;

		if (dabbleGame.checkWord(4))
		{
			foreground.setColor(getResources().getColor(R.color.dabble_valid_word));
			if (!wordsAlreadyPlayed[3]){
				dabbleGame.playWordSound();
				wordsAlreadyPlayed[3] = true;
			}
		}
		else
		{
			wordsAlreadyPlayed[3] = false;
		}
		
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
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setStyle(Style.FILL);
		foreground.setColor(getResources().getColor(R.color.dabble_background_text));
		foreground.setTextSize(button_size_y * 0.5f);
		foreground.setTextScaleX(1);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float text_x = button_size_x / 2;
		// Centering in Y: measure ascent/descent first
		float text_y = button_size_y / 2 - (fm.ascent + fm.descent) / 2;

		float left, top;

		top = getHeight() - button_size_y;
		left = 0;

		canvas.drawText("Hint", left + text_x, top + text_y, foreground);
		
		top = getHeight() - button_size_y;
		left = getWidth() - button_size_x;
		
		if (dabbleGame.getPlayMusic())
		{
			foreground.setColor(getResources().getColor(R.color.dabble_green_text));
		}
		else
		{
			foreground.setColor(getResources().getColor(R.color.dabble_red_text));
		}
			
		canvas.drawText("Music", left + text_x, top + text_y, foreground);
	}
	
	private void drawTimer(Canvas canvas)
	{		
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(dabbleGame.getTimeInSeconds() > 10 ? 
										R.color.dabble_background_text : R.color.dabble_red_text));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(button_size_y * 0.5f);
		foreground.setTextScaleX(1);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float text_x = button_size_x / 2;
		// Centering in Y: measure ascent/descent first
		float text_y = button_size_y / 2 - (fm.ascent + fm.descent) / 2;

		float left, top;

		top = 0;
		left = 0;

		canvas.drawText(dabbleGame.getTime(), left + text_x, top + text_y, foreground);
	}
	
	private void drawScore(Canvas canvas)
	{
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(dabbleGame.getTimeInSeconds() != 0 ? 
													R.color.dabble_background_text : R.color.dabble_red_text));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(button_size_y * 0.5f);
		foreground.setTextScaleX(1);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float text_x = button_size_x / 2;
		// Centering in Y: measure ascent/descent first
		float text_y = button_size_y / 2 - (fm.ascent + fm.descent) / 2;

		float left, top;

		top = 0;
		left = getWidth() - button_size_x;

		canvas.drawText(dabbleGame.getScore(), left + text_x, top + text_y, foreground);
	}
	
	private void drawPause(Canvas canvas)
	{
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.dabble_background_text));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(button_size_y);
		foreground.setTextScaleX(1);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float text_x = button_size_x / 2;
		// Centering in Y: measure ascent/descent first
		float text_y = button_size_y / 2 - (fm.ascent + fm.descent) / 2;

		float left, top;

		top = (getHeight() - button_size_y) / 2;
		left = getWidth() - button_size_x;
		
		String pause;

		if (dabbleGame.getTimeInSeconds() == 0)
		{
			pause = "";
		}
		else if (dabbleGame.getPaused())
		{
			foreground.setTextSize(button_size_y * 1.2f);
			pause = "▶";
		}
		else
		{
			foreground.setTextSize(button_size_y);
			pause = "❙❙";
		}

		canvas.drawText(pause, left + text_x, top + text_y, foreground);
	}
	
	private void drawBack(Canvas canvas)
	{
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.dabble_background_text));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(button_size_y*1.2f);
		foreground.setTextScaleX(1);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float text_x = button_size_x / 2;
		// Centering in Y: measure ascent/descent first
		float text_y = button_size_y / 2 - (fm.ascent + fm.descent) / 2;

		float left, top;

		top = (getHeight() - button_size_y) / 2;
		left = 0;

		canvas.drawText("↺", left + text_x, top + text_y, foreground);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);

		int selected = Integer.parseInt(dabbleGame.getSelected());
		if (selected == -1)
		{
			handleClickNoSelected(event.getX(), event.getY());
		}
		else
		{
			handleClickSelected(selected, event.getX(), event.getY());
		}
		
		return true;
	}
	
	private void handleClickNoSelected(float x, float y){
		float top, left;
		top = getHeight() - button_size_y;
		left = 0;
		
		if ((x > left && x < left + button_size_x) && (y > top && y < top + button_size_y))
		{
			dabbleGame.showHint();
			return;
		}
		
		top = (getHeight() - button_size_y) / 2;
		left = 0;
		
		if ((x > left && x < left + button_size_x) && (y > top && y < top + button_size_y))
		{
			dabbleGame.goBack();
			return;
		}
		
		
		if (dabbleGame.getTimeInSeconds() != 0)
		{
			top = (getHeight() - button_size_y) / 2;
			left = getWidth() - button_size_x;
			
			if ((x > left && x < left + button_size_x) && (y > top && y < top + button_size_y))
			{
				dabbleGame.pauseGame();
				return;
			}
			
			top = getHeight() - button_size_y;
			left = getWidth() - button_size_x;
			
			if ((x > left && x < left + button_size_x) && (y > top && y < top + button_size_y))
			{
				dabbleGame.toggleMusic();
				return;
			}
	
			markSelected(x, y);
		}
	}
	
	private void handleClickSelected(int selected1, float x, float y){
		float top, left;
		top = (getHeight() - button_size_y) / 2;
		left = getWidth() - button_size_x;
		
		if ((x > left && x < left + button_size_x) && (y > top && y < top + button_size_y))
		{
			dabbleGame.pauseGame();
			return;
		}
		
		top = getHeight() - button_size_y;
		left = 0;
		
		if ((x > left && x < left + button_size_x) && (y > top && y < top + button_size_y))
		{
			dabbleGame.showHint();
			return;
		}
		
		top = (getHeight() - button_size_y) / 2;
		left = 0;
		
		if ((x > left && x < left + button_size_x) && (y > top && y < top + button_size_y))
		{
			dabbleGame.goBack();
			return;
		}
		
		
		if (dabbleGame.getTimeInSeconds() != 0 && !dabbleGame.getPaused())
		{
			swapSelected(selected1, x, y);
		}
	}
	
	private void swapSelected(int selected1, float x, float y)
	{
		int selected2 = getSelectedTile(x,y);
		if (selected2 != -1)
		{
			dabbleGame.swapTiles(selected1, selected2);
			dabbleGame.playLetterSound();
		}
		dabbleGame.setSelected(-1);
		invalidate();
	}
	
	private void markSelected(float x, float y)
	{
		if (!dabbleGame.getPaused())
		{
			int selected = getSelectedTile(x, y);
			dabbleGame.setSelected(selected);
			if (selected != -1)
			{
				dabbleGame.playLetterSound();
			}
			invalidate();
		}
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
