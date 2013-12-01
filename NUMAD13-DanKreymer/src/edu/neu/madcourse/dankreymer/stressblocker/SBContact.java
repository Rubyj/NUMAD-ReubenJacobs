package edu.neu.madcourse.dankreymer.stressblocker;

import java.util.HashMap;

public class SBContact extends HashMap<String, String>{	
	public static final String NAME_KEY = "NAME";
	public static final String PHONE_KEY = "PHONE";
	public static final String STRESS_PERCENT_KEY = "STRESS";
	
	public SBContact(String p, String n)
	{
		this(p,n,"0");
	}
	
	public SBContact(String p, String n, String s)
	{
		super();
		put(PHONE_KEY, p);
		put(NAME_KEY, n);
		put(STRESS_PERCENT_KEY, s);
	}
	
	public String getPhoneNumber()
	{
		return get(PHONE_KEY);
	}
	
	public String getName()
	{
		return get(NAME_KEY);
	}
	
	public String getStressPercent()
	{
		return get(STRESS_PERCENT_KEY);
	}
	
	public void setStressPercent(String s)
	{
		put(STRESS_PERCENT_KEY, s);
	}
}
