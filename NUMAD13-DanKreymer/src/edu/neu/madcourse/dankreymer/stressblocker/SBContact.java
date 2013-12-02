package edu.neu.madcourse.dankreymer.stressblocker;

import java.util.HashMap;

public class SBContact extends HashMap<String, String>{	
	public static final String NAME_KEY = "NAME";
	public static final String PHONE_KEY = "PHONE";
	
	public SBContact(String p, String n)
	{
		super();
		put(PHONE_KEY, p);
		put(NAME_KEY, n);
	}
	
	public String getPhoneNumber()
	{
		return get(PHONE_KEY);
	}
	
	public String getName()
	{
		return get(NAME_KEY);
	}
	
	public String toString()
	{
		return get(PHONE_KEY) + "," + get(NAME_KEY);
	}
}
