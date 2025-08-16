package me.nd.upgrades.api;

public class APIS {

	public static void load() 
	{
		try 
		{
			TitleAPI.load();
			ActionBarAPI.load();
		}
			catch (Throwable e) {
		}
	}
	
}
