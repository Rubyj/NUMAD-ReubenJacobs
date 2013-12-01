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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.neu.madcourse.dankreymer.R;

public class SBContacts extends Activity{
	
	private ContentResolver contResv;
	private ListView contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contResv = getContentResolver();
        		
        setContentView(R.layout.stress_blocker_contacts);
        contactsList = (ListView) findViewById(R.id.contactsList);
        fillContactsList();
    }
    
    private void fillContactsList()
    {
    	ArrayList<SBContact> contacts = new ArrayList<SBContact>();
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
    	
    	SimpleAdapter adapter = new SimpleAdapter(this, contacts, android.R.layout.simple_list_item_2, 
    			new String[] {SBContact.NAME_KEY, SBContact.PHONE_KEY}, new int[] {android.R.id.text1, android.R.id.text2 })
    	{
    		
    		 public View getView(int position, View convertView, ViewGroup parent) {
    	            View view = super.getView(position, convertView, parent);
    	            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
    	            SBContact contact = (SBContact) this.getItem(position);
    	            
    	            //SET COLOR HERE. Look at contact's stress level and modify.
    	            //text1.setTextColor(getResources().getColor(SOMECOLOR));

    	            return view;

    	        };
    	};
    	
    	contactsList.setAdapter(adapter);
    	contactsList.invalidate();
    	
    	
    }
    
    
}
