package me.nd.upgrades.cache;

import java.util.*;
import java.util.function.*;

import me.nd.upgrades.model.UpgradesModel;

public class UpgradesCache
{
    private static final UpgradesCache instance;
    private HashMap<String, UpgradesModel> poder;
    private HashMap<String, UpgradesModel> membros;
    private HashMap<String, UpgradesModel> mobs;
    private HashMap<String, UpgradesModel> blocos;
    private HashMap<String, UpgradesModel> attack;
    private HashMap<String, UpgradesModel> defesas;
    
    public static <T> T get(final Function<UpgradesCache, T> function) {
        return function.apply(UpgradesCache.instance);
    }
    
    private UpgradesCache() {
        this.poder = new HashMap<String, UpgradesModel>();
        this.membros = new HashMap<String, UpgradesModel>();
        this.mobs = new HashMap<String, UpgradesModel>();
        this.blocos = new HashMap<String, UpgradesModel>();
        this.attack = new HashMap<String, UpgradesModel>();
        this.defesas = new HashMap<String, UpgradesModel>();
    }
    
    public static UpgradesCache getInstance() {
        return UpgradesCache.instance;
    }
    
    public HashMap<String, UpgradesModel> getPoder() {
        return this.poder;
    }
    
    public HashMap<String, UpgradesModel> getMembros() {
        return this.membros;
    }
    
    public HashMap<String, UpgradesModel> getMobs() {
        return this.mobs;
    }
   
    public HashMap<String, UpgradesModel> getBlocos() {
        return this.blocos;
    }
    
    public HashMap<String, UpgradesModel> getAttack() {
        return this.attack;
    }
    
    public HashMap<String, UpgradesModel> getDefesas() {
        return this.defesas;
    }
    
    static {
        instance = new UpgradesCache();
    }
}
