package edu.neu.madcourse.dankreymer.stressblocker;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.multiplayer.DabbleMTurnBasedGame;
import edu.neu.madcourse.dankreymer.stressblocker.SBBluetoothStoreService;

public class SBBTIdentification extends Activity {
	  private String foundDeviceID; 
	  private ListView contactsList;
	  private ContentResolver contResv;
	  private Context context;
	  
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.stress_blocker_identification);
	        
	        context = this;
	        foundDeviceID = getIntent().getExtras().getString(SBBluetoothStoreService.FOUND_DEVICE_ID);
	        TextView tv = (TextView) findViewById(R.id.sb_device_seen_name);
	        tv.setText(foundDeviceID);
	        contactsList = (ListView) findViewById(R.id.contactsList);
	        contResv = getContentResolver();
	        fillContactsList(); 
	        contactsList.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					registerForContextMenu(contactsList);
					contactsList.showContextMenu();
					
					TextView tv = (TextView) arg1.findViewById(android.R.id.text1);
					String name = tv.getText().toString();
					SBSharedPreferences.putBTDeviceLink(context, foundDeviceID, name);
					finish();
				}});
	    }
	    
	    private void fillContactsList()
	    {
	    	ArrayList<SBContact> contacts =  new ArrayList<SBContact>();
    		Cursor cursor = contResv.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        	if(cursor.moveToFirst())
        	{
        	    do
        	    {
        	    	String name = cursor.getString(cursor.getColumnIndexOrThrow(Phone.DISPLAY_NAME));
        	    	String number = cursor.getString(cursor.getColumnIndexOrThrow(Phone.NUMBER));
        	    	
        	    	contacts.add(new SBContact(number, name));

        	    } while (cursor.moveToNext()) ;
        	}
        	SBSharedPreferences.putContactsList(this, contacts);
	    	
	    	SimpleAdapter adapter = new SimpleAdapter(this, contacts, android.R.layout.simple_list_item_2, 
	    			new String[] {SBContact.NAME_KEY, SBContact.PHONE_KEY}, new int[] {android.R.id.text1, android.R.id.text2 });
	    	
	    	contactsList.setAdapter(adapter);
	    	contactsList.invalidate();
	    }
	    
	    public void onClickDontKnow(View v)
	    {
	    	finish();
	    }

}
