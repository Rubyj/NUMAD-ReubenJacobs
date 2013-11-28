package edu.neu.madcourse.dankreymer.keys;

public enum ServerError {

	NO_SUCH_KEY("Error: No Such Key"),
	
	NO_CONNECTION("ERROR: IOException");
	
	private String errorText;
	
	ServerError(String text)
	{
		errorText = text;
	}
	
	public String getText()
	{
		return errorText;
	}
}
