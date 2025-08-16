package me.nd.upgrades.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import me.nd.upgrades.Main;
import me.nd.upgrades.list.*;
import me.nd.upgrades.list.fragmentos.*;
import me.nd.upgrades.list.item.RemovedorListener;
import me.nd.upgrades.list.upgrades.*;

public class Listeners {
	
	public static void setupListeners() {
	    PluginManager pm = Bukkit.getPluginManager();
	    Listener[] listeners = {
	        new Plantacao(),
	        new EntityDeath(),
	        new Mining(),
	        new BlocoPlace(),
	        new MobSpawnerList(),
	        new PlantacaoListenner(),
	        new FlyListenner(),
	        new FacEvent(),
	        new Moedalistener(),
	        new CommandListenner(),
	        new AttackListenner(),
	        new Defesas(),
	        new RemovedorListener()
	    };

	    for (Listener listener : listeners) {
	        try {
	            pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, listener, Main.get());
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	}

}
