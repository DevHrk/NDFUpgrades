package me.nd.upgrades.integration;

import org.bukkit.entity.Player;

public interface Integration {
    public String getName(Player var1);

    public String getTag(Player var1);

    public String getNameByValue(String var1);

    public Boolean hasFaction(Player var1);

    public void addMembros(Player var1, Integer var2);

    public void addPoder(Player var1, Integer var2);
}
