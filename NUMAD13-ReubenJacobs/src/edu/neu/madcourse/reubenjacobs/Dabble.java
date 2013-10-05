/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
***/
package edu.neu.madcourse.reubenjacobs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class Dabble extends Activity {
 
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
      setContentView(R.layout.activity_dabble);
      
      populateMap();
      
      setWord3();
      setWord4();
      setWord5();
      setWord6();
      
      setArray();
      
      gridView = (GridView) findViewById(R.id.gridView1);
      
      CustomAdapter<String> adapter = new CustomAdapter<String>(this,
				android.R.layout.simple_list_item_1, numbers);
	
      gridView.setAdapter(adapter);
		
		
      gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				
				if (counter == 0) {
					firstLetterClicked = ((TextView) v).getText().toString();
					v.setBackgroundColor(Color.GREEN);
					firstPosition = position;
					counter++;
				} else {
					nextLetterClicked = ((TextView) v).getText().toString();
					nextPosition = position;
					counter = 0;
					
					reloadGrid();
					
					gridView = (GridView) findViewById(R.id.gridView1);
					checkMatches(3);
					checkMatches(4);
					checkMatches(5);
					checkMatches(6);
				}
				
			   Toast.makeText(getApplicationContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
			}
		});
   }

   @Override
   protected void onResume() {
      super.onResume();
   }

   @Override
   protected void onPause() {
	   super.onPause();
   }
   
   protected void reloadGrid() {
		numbers[firstPosition] = nextLetterClicked;
		numbers[nextPosition] = firstLetterClicked;
	   
	   CustomAdapter<String> adapter = new CustomAdapter<String>(this,
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
   
   protected void checkMatches(int wordLength) {
	   CustomAdapter<String> adapter = new CustomAdapter<String>(this,
				android.R.layout.simple_list_item_1, numbers);
	   
	   String threeWord = "";
	   String fourWord = "";
	   String fiveWord = "";
	   String sixWord = "";
	   
	   String firstLetter = "";
	   TextView tempView;
	   
	   if (wordLength == 3) {
		   threeWord = numbers[2] + numbers[3] + numbers[4];
		   firstLetter = numbers[2].toLowerCase(Locale.US);
	   } else if (wordLength == 4) {
		   fourWord = numbers[8] + numbers[9] + numbers[10] + numbers[11];
		   firstLetter = numbers[8].toLowerCase(Locale.US);
	   } else if (wordLength == 5) {
		   fiveWord = numbers[13] + numbers[14] + numbers[15] + numbers[16] + numbers[17];
		   firstLetter = numbers[13].toLowerCase(Locale.US);
	   } else if (wordLength == 6) {
		   sixWord = numbers[18] + numbers[19] + numbers[20] + numbers[21] + numbers[22] + numbers[23];
		   firstLetter = numbers[18].toLowerCase(Locale.US);
	   }
	   
	   /*
	   try {
				instream = getAssets().open(firstLetter + "wordlist.jet");
				Log.d("firstLetter", firstLetter);
				
				if (instream != null) {
					inputreader = new InputStreamReader(instream);
					buffreader = new BufferedReader(inputreader);
					
					
					MainLoop:
					while ((line = buffreader.readLine()) != null) {
						Log.d("line", line);
						Log.d("threeword", threeWord);
							if (wordLength == 3 && line.equalsIgnoreCase(threeWord)) {
								tempView = (TextView)adapter.getView(2, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(3, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(4, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								break MainLoop;
							} else if (wordLength == 4 && line.equalsIgnoreCase(fourWord)) {
								tempView = (TextView)adapter.getView(8, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(9, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(10, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(11, null, gridView);
								tempView.setBackgroundColor(Color.RED);

								break MainLoop;
							} else if (wordLength == 5 && line.equalsIgnoreCase(fiveWord)) {
								tempView = (TextView)adapter.getView(13, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(14, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(15, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(16, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(17, null, gridView);
								tempView.setBackgroundColor(Color.RED);
											
								break MainLoop;
							} else if (wordLength == 6 && line.equalsIgnoreCase(sixWord)) {
								tempView = (TextView)adapter.getView(18, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(19, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(20, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(21, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(22, null, gridView);
								tempView.setBackgroundColor(Color.RED);
								
								tempView = (TextView)adapter.getView(23, null, gridView);
								tempView.setBackgroundColor(Color.RED);
						
								break MainLoop;
							}
					}
					
					//lineCounter = 0;
					buffreader.close();
					inputreader.close();
					instream.close();
				
				}
			} catch (java.io.IOException e) {
			
			}
			*/
   }
   
   public void setViewColor(int[] positionArray) {
	   
   }
}

class CustomAdapter<T> extends ArrayAdapter<T> {
	
	public CustomAdapter(Dabble dabble, int resource, T[] objects) {
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
