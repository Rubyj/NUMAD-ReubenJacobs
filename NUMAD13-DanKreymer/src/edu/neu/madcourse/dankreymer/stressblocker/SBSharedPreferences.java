package edu.neu.madcourse.dankreymer.stressblocker;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

public class SBSharedPreferences {
	private static String PREF = "SB_PREF";
	private static String CONTACTS = "SB_CONTACTS";
	private static String FILTERING = "SB_FILTERING";
	private static String PERIODIC_INTERVAL = "SB_PERIODIC_INTERVAL";
	private static String ACTIVE = "SB_ACTIVE";
	
	
	public static void clearAll(Context context)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
		editor.clear();
		editor.commit();
	}
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
				ret.add(new SBContact(parts[0], parts[1]));
			}
			
			return ret;
		}
	}
	
	public static boolean getFilteringStatus(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(PREF, 0);
		return pref.getBoolean(FILTERING, false);
	}
	
	public static void toggleFilteringStatus(Context context)
	{
		boolean currentStatus = getFilteringStatus(context);
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
		editor.putBoolean(FILTERING, !currentStatus);
		editor.commit();
	}
	
	public static boolean getActiveStatus(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(PREF, 0);
		return pref.getBoolean(ACTIVE, true);
	}
	
	public static void toggleActiveStatus(Context context)
	{
		boolean currentStatus = getActiveStatus(context);
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
		editor.putBoolean(ACTIVE, !currentStatus);
		editor.commit();
	}
	
	public static void putPeriodicInterval(Context context, String interval)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
		editor.putString(PERIODIC_INTERVAL, interval);
		editor.commit();
	}
}
