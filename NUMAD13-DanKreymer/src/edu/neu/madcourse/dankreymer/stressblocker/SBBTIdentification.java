package edu.neu.madcourse.dankreymer.stressblocker;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.neu.madcourse.dankreymer.R;
import edu.neu.madcourse.dankreymer.stressblocker.SBBluetoothStoreService;

public class SBBTIdentification extends Activity{
	  private String foundDeviceID; 
	  private ListView contactsList;
	  private ContentResolver contResv;
	  
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.stress_blocker_identification);

	        foundDeviceID = getIntent().getExtras().getString(SBBluetoothStoreService.FOUND_DEVICE_ID);
	        TextView tv = (TextView) findViewById(R.id.sb_device_seen_name);
	        tv.setText(foundDeviceID);
	        contactsList = (ListView) findViewById(R.id.contactsList);
	        contResv = getContentResolver();
	        fillContactsList(); 
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
