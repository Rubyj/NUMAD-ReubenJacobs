package edu.neu.madcourse.dankreymer.stressblocker;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

public class SBSharedPreferences {
	private static String PREF = "SB_PREF";
	private static String CONTACTS = "SB_CONTACTS";
	public static void putContactsList(Context context, ArrayList<SBContact> list)
	{
		String string = "";
		for (SBContact c : list)
		{
			string = string + c.toString() + ";";
		}
		
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
		editor.putString(CONTACTS, string);
		editor.commit();
	}
	
	public static ArrayList<SBContact> getContactsList(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(PREF, 0);
		String contacts = pref.getString(CONTACTS, "");
		if (contacts.equals(""))
		{
			return null;
		}
		else
		{
			ArrayList<SBContact> ret = new  ArrayList<SBContact>();
			String[] entries = contacts.split(";");
			for (String entry : entries)
			{
				String[] parts = entry.split(",");
				ret.add(new SBContact(parts[0], parts[1], parts[2]));
			}
			
			return ret;
		}
	}
}
