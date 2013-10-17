package edu.neu.madcourse.dankreymer.keys;

import edu.neu.mhealth.api.KeyValueAPI;

public class Keys {
	private static final String TEAMNAME = "dkreymer";
	private static final String PASSWORD = "mallard";
	public static final String HIGHSCORES = "high_scores";
	public static final String USERS = "users";
	public static final String STATUS_OFFLINE = "offline";
	public static final String STATUS_ONLINE = "online";
	public static final String STATUS_IN_GAME = "in game";
	private static final String USER_STATUS = "user_status";
	
	
	public static String get(String key)
	{
		return KeyValueAPI.get(Keys.TEAMNAME, Keys.PASSWORD, key);
	}
	
	public static String put(String key, String val)
	{
		return KeyValueAPI.put(Keys.TEAMNAME, Keys.PASSWORD, key, val);
	}
	
	public static String userStatusKey(String user)
	{
		return USER_STATUS + "_" + user;
	}
}
