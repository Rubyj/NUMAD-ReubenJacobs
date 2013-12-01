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
	private static String CURRENT_CALL_NUMBER = "SB_CURRENT_CALL_NUMBER";
	private static String CONTACT_DATA = "SB_CONTACT_DATA";
	private static String CURRENT_STRESS = "SB_CURRENT_STRESS";
	private static String BT_DEVICE_COUNT = "BT_DEVICE_COUNT";
	private static String BT_DEVICE_LINK = "BT_DEVICE_LINK";
	private static String BT_DEVICE_QUEUE = "BT_DEVICE_QUEUE";
	
	private static long TWELVE_HOURS = 43200000;
	
	private static String getBTDeviceCountKey(String contact)
	{
		return BT_DEVICE_COUNT + "_" + contact;
	}
	
	private static String getBTDeviceLinkKey(String contact)
	{
		return BT_DEVICE_LINK + "_" + contact;
	}
	
	private static String getContactKey(String contact)
	{
		return CONTACT_DATA + "_" + contact;
	}
	
	
	public static String getBTDeviceCount(Context context, String contact)
	{
		SharedPreferences pref = context.getSharedPreferences(PREF, 0);
		return pref.getString(getBTDeviceCountKey(contact), "");
	}
	
	public static void incrementBTDeviceCount(Context context, String contact)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
		
		String count = getBTDeviceCount(context, contact);
		if (count.equals(""))
		{
			editor.putString(getBTDeviceCountKey(contact), "1");
			editor.commit();
		}
		else if (count.equals("IGNORE"))
		{
			return;
		}
		else
		{
			int countInt = Integer.parseInt(count);
			if (countInt < 5)
			{
				editor.putString(getBTDeviceCountKey(contact), Integer.toString(countInt + 1));
				editor.commit();
			}
		}
	}
	
	private static void dontIncrementBTDevice(Context context, String contact)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();

		editor.putString(getBTDeviceCountKey(contact), "IGNORE");
		editor.commit();

	}
	
	public static String getBTQueue(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(PREF, 0);
		return pref.getString(BT_DEVICE_QUEUE, "");
	}
	
	public static void addToBTQueue(Context context, String contact)
	{
		String count = getBTDeviceCount(context, contact);
		
		if (count.equals("5"))
		{
			dontIncrementBTDevice(context,contact);
			String queue = getBTQueue(context);
			
			if (queue.equals(""))
			{
				queue = contact;
			}
			else
			{
				queue = queue + ";" + contact;
			}
			
			SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
			editor.putString(BT_DEVICE_QUEUE, queue);
			editor.commit();
			
		}
				
		
	}
	
	public static String getContactData(Context context, String contact)
	{
		SharedPreferences pref = context.getSharedPreferences(PREF, 0);
		return pref.getString(getContactKey(contact), "");
	}
	
	private static String trimOldEntries(String full, String currentTime)
	{
		if (full.equals(""))
		{
			return "";
		}
		
		String[] entries = full.split(";");
		String ret = "";
		
		//if we remove a before entry, we remove the after one as well since it is now useless.
		boolean removeNext = false;
		
		for (String entry : entries)
		{
			String[] split = entry.split(",");
			
			if (removeNext && split[0].equals("a"))
			{
				removeNext = false;
			}
			else if (Long.parseLong(currentTime) - Long.parseLong(split[1]) >= TWELVE_HOURS)
			{
				if (split[0].equals("b"))
				{
					removeNext = true;
				}
			}
			else 
			{
				ret += entry + ";";
			}
		}
		
		return ret;
	}
	
	public static void putContactData(Context context, String contact, String type, String time, String value)
	{
		String entry = type + "," + time + "," + value + ";";
		
		String fullEntry = getContactData(context, contact);
		fullEntry = trimOldEntries(fullEntry, time);
		fullEntry = fullEntry + entry;
		
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
		editor.putString(getContactKey(contact), fullEntry);
		editor.commit();
	}
	
	public static void putCurrentNumber(Context context, String number)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
		editor.putString(CURRENT_CALL_NUMBER, number);
		editor.commit();
	}
	
	public static String getCurrentNumber(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(PREF, 0);
		return pref.getString(CURRENT_CALL_NUMBER, "");
	}
	
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
	
	public static void setFilteringStatus(Context context, Boolean val)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
		editor.putBoolean(FILTERING, val);
		editor.commit();
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
	
	public static String getPeriodicInterval(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(PREF, 0);
		return pref.getString(PERIODIC_INTERVAL, "");
	}
	
	public static void putCurrentStress(Context context, String stress)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF, 0).edit();
		editor.putString(CURRENT_STRESS, stress);
		editor.commit();
	}
	
	public static String getCurrentStress(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(PREF, 0);
		return pref.getString(CURRENT_STRESS, "");
	}
}
