
package edu.neu.madcourse.reubenjacobs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import edu.neu.mhealth.api.KeyValueAPI;

public class DabbleComm extends Activity {
 
   private int counter = 0;
   private int firstPosition;
   private int nextPosition;
   private String firstLetterClicked;
   private String nextLetterClicked;
   
   private Random rand = new Random();
   private int rand3 = rand.nextInt(26);
   private int rand4 = rand.nextInt(26);
   private int rand5 = rand.nextInt(26);
   private int rand6 = rand.nextInt(26);
   private int skip = rand.nextInt(501);
   
   private int lineCounter = 0;
   
   private Random randPlacement = new Random();
   
   private String word3;
   private String word4;
   private String word5;
   private String word6;
   
   private InputStream instream;
   private InputStreamReader inputreader;
   private BufferedReader buffreader;
   private String line;

   private GridView gridView;
   
   private MediaPlayer player1;
   
   private long timerNumber = 120;
   
   private Runnable mUpdateTimeTask;
   private Handler mHandler;
   
   private boolean player1Muted = false;
   private boolean gameWon = false;
   
   private String userName;
   
   private SparseArray<String> goTo = new SparseArray<String>();
   
   String[] numbers = new String[] { 
			" ", " ", 
			"C", "D", "E", //3 Letter Word
			" ", " ", " ", 
			"J", "K", "L", "M", //4 Letter Word
			" ", 
			"Q", "R", "S", "T", "U", //5 Letter Word
			"V", "W", "X", "Y", "Z", "A"}; //6 Letter Word
   
   ArrayList<String> letters = new ArrayList<String>();
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      //Retrieve the username entered on the welcomeComm activity
      Bundle extras = getIntent().getExtras();
      this.userName = extras.getString("USER");
      String isUserCreated = KeyValueAPI.get("sloth_nation", "fromunda", this.userName);
      
      //If the username is not found on the server store it
      if (isUserCreated == null) {
    	  KeyValueAPI.put("sloth_nation", "fromunda", this.userName, "active");
      }
     
      setContentView(R.layout.activity_dabble);
      
      Display display = getWindowManager().getDefaultDisplay();
      DisplayMetrics outMetrics = new DisplayMetrics();
      display.getMetrics(outMetrics);
		
      int sideMargin = (int)outMetrics.widthPixels/25;
      int topMargin = (int)outMetrics.heightPixels/35 ;
      
      RadioButton radio1 = (RadioButton)findViewById(R.id.radioButton1);
	  RadioButton radio2 = (RadioButton)findViewById(R.id.radioButton2);
	  RadioButton radio3 = (RadioButton)findViewById(R.id.radioButton3);
	  RadioButton radio4 = (RadioButton)findViewById(R.id.radioButton4);
	  
	  LayoutParams radio1Params = (RelativeLayout.LayoutParams) radio1.getLayoutParams();
	  radio1Params.setMargins(sideMargin, topMargin, sideMargin, topMargin-20);
	  radio1.setLayoutParams(radio1Params);
	  
	  LayoutParams radio2Params = (RelativeLayout.LayoutParams) radio2.getLayoutParams();
	  radio2Params.setMargins(0, topMargin, sideMargin, topMargin-10);
	  radio2.setLayoutParams(radio2Params);
	  
	  LayoutParams radio3Params = (RelativeLayout.LayoutParams) radio3.getLayoutParams();
	  radio3Params.setMargins(0, 5, sideMargin, topMargin-10);
	  radio3.setLayoutParams(radio3Params);
	  
	  LayoutParams radio4Params = (RelativeLayout.LayoutParams) radio4.getLayoutParams();
	  radio4Params.setMargins(0, topMargin-10, sideMargin, topMargin);
	  radio4.setLayoutParams(radio4Params);
	  
	  final TextView timerView = (TextView)findViewById(R.id.timerView);
      
      populateMap();

      setWord3();
      setWord4();
      setWord5();
      setWord6();
      setArray();
 
      gridView = (GridView) findViewById(R.id.gridView1);
      
      CustomAdapterComm<String> adapter = new CustomAdapterComm<String>(this,
				android.R.layout.simple_list_item_1, numbers);
	
      gridView.setAdapter(adapter);
		
		
      gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				
				ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
				toneG.startTone(ToneGenerator.TONE_PROP_BEEP);
				toneG.release();
				
				if (counter == 0) {
					firstLetterClicked = ((TextView) v).getText().toString();
					((TextView) v).setTextColor(Color.RED);
					firstPosition = position;
					counter++;
				} else {
					nextLetterClicked = ((TextView) v).getText().toString();
					nextPosition = position;
					counter = 0;
					
					reloadGrid(true);
					
					gridView = (GridView) findViewById(R.id.gridView1);
					
					int point3;
					int point4;
					int point5;
					int point6;
					
					point3 = checkMatches3();
					point4 = checkMatches4();
					point5 = checkMatches5();
					point6 = checkMatches6();
					
					int totalPoints = point3 + point4 + point5 + point6;
					
					setPoints(totalPoints);
					
					if (totalPoints >= 18) {
						onWin();
					}
				}
			}
		});
      
      mHandler = new Handler();
      
      mUpdateTimeTask = new Runnable() {
          public void run() {

              final long start = timerNumber;
              int seconds = (int) start;
              int minutes = seconds / 60;
              seconds     = seconds % 60;
              
              if (gameWon) {
            	  return;
              } else if (minutes + seconds < 1) {
            	timerView.setText("You Lose!");
            	onLose();
              } else if (seconds < 10) {
                 timerView.setText("" + minutes + ":0" + seconds);
                 if (minutes == 0 && seconds < 10) {
                	 timerView.setTextColor(Color.BLUE);
                 }
              } else {
                  timerView.setText("" + minutes + ":" + seconds);            
              } 
              
              timerNumber--;

              // add a delay to adjust for computation time
              if (minutes + seconds >=1 || gameWon) {
            	  mHandler.postDelayed(this, 1000);
              }
          }
       };
   }

   @Override
   protected void onResume() {
      super.onResume();
      player1 = MediaPlayer.create(DabbleComm.this, R.raw.quicksilver);
      player1.setLooping(true);
      player1.start();
      
      mHandler.post(mUpdateTimeTask);
   }

   @Override
   protected void onPause() {
	   super.onPause();
	   mHandler.removeCallbacks(mUpdateTimeTask);
	   player1.stop();
	   player1.release();
	   KeyValueAPI.put("sloth_nation", "fromunda", this.userName, "inactive");
   }
   
   @Override
   protected void onStop() {
	   super.onStop();
	   KeyValueAPI.put("sloth_nation", "fromunda", this.userName, "inactive");
   }
   
   @Override
   protected void onDestroy() {
	   super.onDestroy();
	   KeyValueAPI.put("sloth_nation", "fromunda", this.userName, "inactive");
   }

   protected void reloadGrid(boolean switchLetters) {
	   
	   if (switchLetters == true) {
		   numbers[firstPosition] = nextLetterClicked;
		   numbers[nextPosition] = firstLetterClicked;
	   }
	   
	   CustomAdapterComm<String> adapter = new CustomAdapterComm<String>(this,
					android.R.layout.simple_list_item_1, numbers);
	   
	   gridView.setAdapter(adapter);
   }
   
   protected void populateMap() {
	      goTo.put(0, "a");
	      goTo.put(1, "b");
	      goTo.put(2, "c");
	      goTo.put(3, "d");
	      goTo.put(4, "e");
	      goTo.put(5, "f");
	      goTo.put(6, "g");
	      goTo.put(7, "h");
	      goTo.put(8, "i");
	      goTo.put(9, "j");
	      goTo.put(10, "k");
	      goTo.put(11, "l");
	      goTo.put(12, "m");
	      goTo.put(13, "n");
	      goTo.put(14, "o");
	      goTo.put(15, "p");
	      goTo.put(16, "q");
	      goTo.put(17, "r");
	      goTo.put(18, "s");
	      goTo.put(19, "t");
	      goTo.put(20, "u");
	      goTo.put(21, "v");
	      goTo.put(22, "w");
	      goTo.put(23, "x");
	      goTo.put(24, "y");
	      goTo.put(25, "z");      
   }
   
   protected void setWord3() {
	      try {
				instream = getAssets().open(goTo.get(rand3) + "wordlist.jet");
				
				
				if (instream != null) {
					inputreader = new InputStreamReader(instream);
					buffreader = new BufferedReader(inputreader);
					
					//buffreader.skip((long)skip);
					
					while ((line = buffreader.readLine()) != null) {
						lineCounter++;
						if (lineCounter >= skip && line.length() == 3) {
							word3 = line;
							break;
						}
					}
				
					//alreadyChecked.put(firstLetter, firstLetter);
					lineCounter = 0;
					buffreader.close();
					inputreader.close();
					instream.close();
				
				}
			} catch (java.io.IOException e) {
			
			}
	      
	      if (word3 != null) {
	    	  String letter1 = line.substring(0, 1).toUpperCase(Locale.US);
	    	  String letter2 = line.substring(1, 2).toUpperCase(Locale.US);
	    	  String letter3 = line.substring(2, 3).toUpperCase(Locale.US);
	    	  
	    	  letters.add(letter1);
	    	  System.out.println(letter1);
	    	  letters.add(letter2);
	    	  System.out.println(letter2);
	    	  letters.add(letter3);
	    	  System.out.println(letter3);
	      }
   }
   
   protected void setWord4() {
	      try {
				instream = getAssets().open(goTo.get(rand4) + "wordlist.jet");
				
				
				if (instream != null) {
					inputreader = new InputStreamReader(instream);
					buffreader = new BufferedReader(inputreader);
					
					//buffreader.skip((long)skip);
					
					while ((line = buffreader.readLine()) != null) {
						lineCounter++;
						if (lineCounter >= skip && line.length() == 4) {
							word4 = line;
							break;
						}
					}
				
					//alreadyChecked.put(firstLetter, firstLetter);
					lineCounter = 0;
					buffreader.close();
					inputreader.close();
					instream.close();
				
				}
			} catch (java.io.IOException e) {
			
			}
	      
	      if (word4 != null) {
	    	  String letter1 = line.substring(0, 1).toUpperCase(Locale.US);
	    	  String letter2 = line.substring(1, 2).toUpperCase(Locale.US);
	    	  String letter3 = line.substring(2, 3).toUpperCase(Locale.US);
	    	  String letter4 = line.substring(3, 4).toUpperCase(Locale.US);
	    	  
	    	  letters.add(letter1);
	    	  System.out.println(letter1);
	    	  letters.add(letter2);
	    	  System.out.println(letter2);
	    	  letters.add(letter3);
	    	  System.out.println(letter3);
	    	  letters.add(letter4);
	    	  System.out.println(letter4);
	      }
   }
   
   protected void setWord5() {
	      try {
				instream = getAssets().open(goTo.get(rand5) + "wordlist.jet");
				
				
				if (instream != null) {
					inputreader = new InputStreamReader(instream);
					buffreader = new BufferedReader(inputreader);
					
					//buffreader.skip((long)skip);
					
					while ((line = buffreader.readLine()) != null) {
						lineCounter++;
						if (lineCounter >= skip && line.length() == 5) {
							word5 = line;
							break;
						}
					}
				
					//alreadyChecked.put(firstLetter, firstLetter);
					lineCounter = 0;
					buffreader.close();
					inputreader.close();
					instream.close();
				
				}
			} catch (java.io.IOException e) {
			
			}
	      
	      if (word5 != null) {
	    	  String letter1 = line.substring(0, 1).toUpperCase(Locale.US);
	    	  String letter2 = line.substring(1, 2).toUpperCase(Locale.US);
	    	  String letter3 = line.substring(2, 3).toUpperCase(Locale.US);
	    	  String letter4 = line.substring(3, 4).toUpperCase(Locale.US);
	    	  String letter5 = line.substring(4, 5).toUpperCase(Locale.US);
	    	  
	    	  letters.add(letter1);
	    	  System.out.println(letter1);
	    	  letters.add(letter2);
	    	  System.out.println(letter2);
	    	  letters.add(letter3);
	    	  System.out.println(letter3);
	    	  letters.add(letter4);
	    	  System.out.println(letter4);
	    	  letters.add(letter5);
	    	  System.out.println(letter5);
	      }
   }
   
   protected void setWord6() {
	      try {
				instream = getAssets().open(goTo.get(rand6) + "wordlist.jet");
				
				
				if (instream != null) {
					inputreader = new InputStreamReader(instream);
					buffreader = new BufferedReader(inputreader);
					
					//buffreader.skip((long)skip);
					
					while ((line = buffreader.readLine()) != null) {
						lineCounter++;
						if (lineCounter >= skip && line.length() == 6) {
							word6 = line;
							break;
						}
					}
				
					//alreadyChecked.put(firstLetter, firstLetter);
					lineCounter = 0;
					buffreader.close();
					inputreader.close();
					instream.close();
				
				}
			} catch (java.io.IOException e) {
			
			}
	      
	      if (word6 != null) {
	    	  String letter1 = line.substring(0, 1).toUpperCase(Locale.US);
	    	  String letter2 = line.substring(1, 2).toUpperCase(Locale.US);
	    	  String letter3 = line.substring(2, 3).toUpperCase(Locale.US);
	    	  String letter4 = line.substring(3, 4).toUpperCase(Locale.US);
	    	  String letter5 = line.substring(4, 5).toUpperCase(Locale.US);
	    	  String letter6 = line.substring(5, 6).toUpperCase(Locale.US);
	    	  
	    	  letters.add(letter1);
	    	  System.out.println(letter1);
	    	  letters.add(letter2);
	    	  System.out.println(letter2);
	    	  letters.add(letter3);
	    	  System.out.println(letter3);
	    	  letters.add(letter4);
	    	  System.out.println(letter4);
	    	  letters.add(letter5);
	    	  System.out.println(letter5);
	    	  letters.add(letter6);
	    	  System.out.println(letter6);
	      }
   }
   
   protected void setArray() {
	  
	  int random;
	  ArrayList<String> tempArray;
	  tempArray = (ArrayList<String>)letters.clone();
 	  
	  random = randPlacement.nextInt(18);
 	  numbers[2] = tempArray.get(random);
 	  tempArray.remove(random);
 	  
	  random = randPlacement.nextInt(17);
 	  numbers[3] = tempArray.get(random);
 	  tempArray.remove(random);
 	  
	  random = randPlacement.nextInt(16);
 	  numbers[4] = tempArray.get(random);
 	  tempArray.remove(random);
 	  
	  random = randPlacement.nextInt(15);
 	  numbers[8] = tempArray.get(random);
 	  tempArray.remove(random);
 	  
	  random = randPlacement.nextInt(14);
 	  numbers[9] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(13);
 	  numbers[10] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(12);
 	  numbers[11] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(11);
 	  numbers[13] = tempArray.get(random);
 	  tempArray.remove(random);
 	  
	  random = randPlacement.nextInt(10);
 	  numbers[14] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(9);
 	  numbers[15] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(8);
 	  numbers[16] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(7);
 	  numbers[17] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(6);
 	  numbers[18] = tempArray.get(random);
 	  tempArray.remove(random);
 	  
	  random = randPlacement.nextInt(5);
 	  numbers[19] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(4);
 	  numbers[20] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(3);
 	  numbers[21] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(2);
 	  numbers[22] = tempArray.get(random);
 	  tempArray.remove(random);
	  
	  random = randPlacement.nextInt(1);
 	  numbers[23] = tempArray.get(random);
 	  tempArray.remove(random);
   }
   
   protected int checkMatches3() {
	   
	   int points = 0;
	   RadioButton radio1 = (RadioButton)findViewById(R.id.radioButton1);
	   String firstLetter = numbers[2].toLowerCase(Locale.US);
	   
	   String threeWord = numbers[2] + numbers[3] + numbers[4];
	   
	   try {
			instream = getAssets().open(firstLetter + "wordlist.jet");
			
			if (instream != null) {
				inputreader = new InputStreamReader(instream);
				buffreader = new BufferedReader(inputreader);
				
				MainLoop:
				while ((line = buffreader.readLine()) != null && threeWord.compareToIgnoreCase(line) >= 0) {
						if (line.equalsIgnoreCase(threeWord)) {
							radio1.setChecked(true);
							points = 3;
							
							ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
							toneG.startTone(ToneGenerator.TONE_PROP_BEEP);
							toneG.release();
							
							break MainLoop;
						} else if (!line.equalsIgnoreCase(threeWord)){
							radio1.setChecked(false);
						}
				}
				
				if (!line.equalsIgnoreCase(threeWord)){
					radio1.setChecked(false);
				}

				buffreader.close();
				inputreader.close();
				instream.close();
			
			}
		} catch (java.io.IOException e) {
		
		} 
	   
	   return points;
   }
   
   protected int checkMatches4() {
	   
	   int points = 0;
	   RadioButton radio2 = (RadioButton)findViewById(R.id.radioButton2);
	   String firstLetter = numbers[8].toLowerCase(Locale.US);
	   
	   String threeWord = numbers[8] + numbers[9] + numbers[10] + numbers[11];
	   
	   try {
			instream = getAssets().open(firstLetter + "wordlist.jet");
			
			if (instream != null) {
				inputreader = new InputStreamReader(instream);
				buffreader = new BufferedReader(inputreader);
				
				MainLoop:
				while ((line = buffreader.readLine()) != null && threeWord.compareToIgnoreCase(line) >= 0) {
						if (line.equalsIgnoreCase(threeWord)) {
							radio2.setChecked(true);
							points = 4;
							
							ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
							toneG.startTone(ToneGenerator.TONE_PROP_BEEP);
							toneG.release();
							
							break MainLoop;
						} else if (!line.equalsIgnoreCase(threeWord)){
							radio2.setChecked(false);
						}
				}
				
				if (!line.equalsIgnoreCase(threeWord)){
					radio2.setChecked(false);
				}
				
				buffreader.close();
				inputreader.close();
				instream.close();
			
			}
		} catch (java.io.IOException e) {
		
		}
	   return points;
   }
   protected int checkMatches5() {
	   
	   int points = 0;
	   RadioButton radio3 = (RadioButton)findViewById(R.id.radioButton3);
	   String firstLetter = numbers[13].toLowerCase(Locale.US);
	   
	   String threeWord = numbers[13] + numbers[14] + numbers[15] + numbers[16] + numbers[17];
	   
	   try {
			instream = getAssets().open(firstLetter + "wordlist.jet");
			
			if (instream != null) {
				inputreader = new InputStreamReader(instream);
				buffreader = new BufferedReader(inputreader);
				
				MainLoop:
				while ((line = buffreader.readLine()) != null && threeWord.compareToIgnoreCase(line) >= 0) {
						if (line.equalsIgnoreCase(threeWord)) {
							radio3.setChecked(true);
							points = 5;
							
							ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
							toneG.startTone(ToneGenerator.TONE_PROP_BEEP);
							toneG.release();
							
							break MainLoop;
						} else if (!line.equalsIgnoreCase(threeWord)){
							radio3.setChecked(false);
						}
				}
				
				if (!line.equalsIgnoreCase(threeWord)){
					radio3.setChecked(false);
				}
				
				buffreader.close();
				inputreader.close();
				instream.close();
			
			}
		} catch (java.io.IOException e) {
		
		}
	   return points;
   }
   protected int checkMatches6() {
	   
	   int points = 0;
	   RadioButton radio4 = (RadioButton)findViewById(R.id.radioButton4);
	   String firstLetter = numbers[18].toLowerCase(Locale.US);
	   
	   String threeWord = numbers[18] + numbers[19] + numbers[20] + numbers[21] + numbers[22] + numbers[23];
	   
	   try {
			instream = getAssets().open(firstLetter + "wordlist.jet");
			
			if (instream != null) {
				inputreader = new InputStreamReader(instream);
				buffreader = new BufferedReader(inputreader);
				
				MainLoop:
				while ((line = buffreader.readLine()) != null && threeWord.compareToIgnoreCase(line) >= 0) {
						if (line.equalsIgnoreCase(threeWord)) {
							radio4.setChecked(true);
							points = 6;
							
							ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
							toneG.startTone(ToneGenerator.TONE_PROP_BEEP);
							toneG.release();
							
							break MainLoop;
						} else if (!line.equalsIgnoreCase(threeWord)){
							radio4.setChecked(false);
						}
				}
				
				if (!line.equalsIgnoreCase(threeWord)){
					radio4.setChecked(false);
				}

				buffreader.close();
				inputreader.close();
				instream.close();
			}
		} catch (java.io.IOException e) {
		
		}
	   return points;
   }
   
   public void onHint(View view) {
	   AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
       builder1.setMessage(letters.get(0) + letters.get(1) + letters.get(2) + ", " + letters.get(3) + letters.get(4) + letters.get(5) + letters.get(6) + 
        				", " + letters.get(7) + letters.get(8) + letters.get(9) + letters.get(10) + letters.get(11) + ", " + 
        				letters.get(12) + letters.get(13) + letters.get(14) + letters.get(15) + letters.get(16) + letters.get(17));
       builder1.setCancelable(true);
       builder1.setNegativeButton("Back",
    	    new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int id) {
            		dialog.cancel();
            	}
        	}
       );

       AlertDialog alert11 = builder1.create();
       alert11.show();
   }
   
   public void onPauseClick(View view) {
		mHandler.removeCallbacks(mUpdateTimeTask);
	   	Intent intent = new Intent(this, PauseDabble.class);
		startActivity(intent);
   }
   
   public void onLose() {
	   	finish();
	   	Intent intent = new Intent(this, WelcomeDabble.class);
		startActivity(intent);
   }
   
   public void setPoints(int totalPoints) {
	   	TextView pointsView = (TextView)findViewById(R.id.pointsView);
	   	pointsView.setText("Points: " + totalPoints);
  }
   
   public void onMute(View view){
	   TextView tv = (TextView)findViewById(R.id.muteButton);
	   
	   if (!player1Muted) {
		   player1.setVolume(0, 0);
		   player1Muted = true;
		   tv.setText("Unmute");
	   } else {
		   player1.setVolume(1.0F, 1.0F);
		   tv.setText("Mute");
		   player1Muted = false;
	   }
   }
   
   public void onQuit(View view) {
	   finish();
   }
   
   public void onWin(){
	   	gameWon = true;
	   	
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("YOU WON!");
        builder1.setCancelable(true);
        builder1.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        
        TextView tv = (TextView)findViewById(R.id.timerView);
        tv.setText("You won!");
   }
}

class CustomAdapterComm<T> extends ArrayAdapter<T> {
	
	public CustomAdapterComm(DabbleComm dabble, int resource, T[] objects) {
		super(dabble, resource, objects);
	}
	
	public boolean isEnabled(int position) {
		if (position < 2) {
			return false;
		} else if (position > 4 && position < 8) {
			return false;
		} else if (position > 11 && position < 13) {
			return false;
		} else {
			return true;
		}
	}
	
}
