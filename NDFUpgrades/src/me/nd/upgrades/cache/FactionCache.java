package me.nd.upgrades.cache;

import java.util.*;
import java.util.function.*;

import me.nd.upgrades.model.FactionModel;

public class FactionCache
{
	  private static final FactionCache instance = new FactionCache();
	  
	  public static FactionCache getInstance() {
	    return instance;
	  }
	  
	  private HashMap<String, FactionModel> cache = new HashMap<>();
	  
	  public HashMap<String, FactionModel> getCache() {
	    return this.cache;
	  }
	  
	  public static <T> T get(Function<FactionCache, T> function) {
	    return function.apply(instance);
	  }
	  
	}
