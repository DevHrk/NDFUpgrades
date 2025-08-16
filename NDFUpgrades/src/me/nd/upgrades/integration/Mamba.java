package me.nd.upgrades.integration;

import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;

public class Mamba implements Integration {
    @Override
    public String getName(Player p) {
        MPlayer mp = MPlayer.get((Object)p);
        return mp.getFactionName();
    }

    @Override
    public String getTag(Player p) {
        MPlayer mp = MPlayer.get((Object)p);
        return mp.getFactionTag();
    }

	@Override
    public String getNameByValue(String tag) {
        AtomicReference name = new AtomicReference();
        FactionColl.get().getAll().forEach(faction -> {
            if (faction.getTag().equals(tag) && faction.getName() != null) {
                name.set(faction.getName());
            }
        });
        return (String)name.get();
    }

    @Override
    public Boolean hasFaction(Player p) {
    	MPlayer mp = MPlayer.get(p);
        return mp.hasFaction();
    }

    @Override
    public void addMembros(Player p, Integer value) {
    	MPlayer mp = MPlayer.get(p);
        mp.getFaction().setMemberBoost(Integer.valueOf(mp.getFaction().getMemberBoost() + value));
    }

    @Override
    public void addPoder(Player p, Integer value) {
    	MPlayer mp = MPlayer.get(p);
        mp.getFaction().setPowerBoost(Double.valueOf(mp.getFaction().getPowerBoost() + value));
    }
}
