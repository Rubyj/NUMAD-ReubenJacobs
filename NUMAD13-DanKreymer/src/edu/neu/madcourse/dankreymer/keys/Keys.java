package edu.neu.madcourse.dankreymer.keys;

import edu.neu.mhealth.api.KeyValueAPI;

public class Keys {
	private static final String TEAMNAME = "dkreymer";
	private static final String PASSWORD = "mallard";
	public static final String HIGHSCORES = "high_scores";
	public static final String USERS = "users";
	public static final String STATUS_OFFLINE = "offline";
	public static final String STATUS_ONLINE = "online";
	public static final String STATUS_LOOKING = "looking";
	public static final String STATUS_IN_GAME = "in game";
	public static final String STATUS_REALTIME_INGAME= "real_time_ingame";
	private static final String USER_STATUS = "user_status";
	private static final String USER_GAMEPLAY = "user_game_play";
	private static final String USER_REAL_TIME_INVITE = "user_real_time_invites";
	private static final String REAL_TIME_GAME = "real_time_game";
	private static final String ROW_KEY = "row";
	
	public static String get(String key)
	{
		return KeyValueAPI.get(Keys.TEAMNAME, Keys.PASSWORD, key);
	}
	
	public static String put(String key, String val)
	{
		return KeyValueAPI.put(Keys.TEAMNAME, Keys.PASSWORD, key, val);
	}
	
	public static String clearKey(String key)
	{
		return KeyValueAPI.clearKey(Keys.TEAMNAME, Keys.PASSWORD, key);
	}
	
	public static String userStatusKey(String user)
	{
		return USER_STATUS + "_" + user;
	}
	
	public static String userGameplayKey(String user)
	{
		return USER_GAMEPLAY + "_" + user;
	}
	
	public static String realTimeInviteKey(String player)
	{
		return USER_REAL_TIME_INVITE + "_" + player;
	}
	
	public static String realTimeGameKey(String player1, String player2)
	{
		if (player1.compareTo(player2) < 0)
		{
			return REAL_TIME_GAME + "_" + player1 + "_" + player2;
		}
		else
		{
			return REAL_TIME_GAME + "_" + player2 + "_" + player1;
		}
	}
	
	public static String realTimeGameRowKey(String player1, String player2, String player)
	{
		return realTimeGameKey(player1, player2) + "_" + player + "_" + ROW_KEY;
	}
}
