package me.nd.upgrades.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.nd.upgrades.minecraft.version.GetVersions;
import me.nd.upgrades.minecraft.version.Version;
import me.nd.upgrades.utils.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ActionBarAPI {
	    
    private static Method a;
    private static Object typeMessage;
    private static Constructor<?> chatConstructor;
    
    static {
        load();
    }
    
    public static void sendActionBarMessage(Player player, String message) {
        try {
            Object chatMessage = a.invoke(null, "{\"text\":\"" + message + "\"}");
            Object packet = chatConstructor.newInstance(chatMessage, typeMessage);
            ReflectionUtils.sendPacket(player, packet);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    public static void broadcastActionBar(String message) {
        try {
            Object chatMessage = a.invoke(null, "{\"text\":\"" + message + "\"}");
            Object packet = chatConstructor.newInstance(chatMessage, typeMessage);
            for (Player player : Bukkit.getOnlinePlayers()) {
                ReflectionUtils.sendPacket(player, packet);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    static void load() {
        try {
            Class<?> icbc;
            Class<?> ppoc;
            Class<?> typeMessageClass;

            if (Version.getVersion() == Version.v1_12) {
                icbc = ReflectionUtils.getNMSClass("net.minecraft.network.chat.IChatBaseComponent");
                ppoc = ReflectionUtils.getNMSClass("net.minecraft.network.protocol.game.PacketPlayOutChat");
            } else {
                icbc = ReflectionUtils.getNMSClass("IChatBaseComponent");
                ppoc = ReflectionUtils.getNMSClass("PacketPlayOutChat");
            }

            if (icbc.getDeclaredClasses().length > 0) {
                a = icbc.getDeclaredClasses()[0].getMethod("a", String.class);
            } else {
                a = ReflectionUtils.getNMSClass("ChatSerializer").getMethod("a", String.class);
            }

            if (Version.getVersion() == Version.v1_12 || GetVersions.isVeryNewVersion()) {
                typeMessageClass = ReflectionUtils.getNMSClass("net.minecraft.network.chat.ChatMessageType");
                typeMessage = typeMessageClass.getEnumConstants()[2];
            } else {
                typeMessageClass = byte.class;
                typeMessage = (byte) 2;
            }

            chatConstructor = ppoc.getConstructor(icbc, typeMessageClass);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize ActionBarAPI", e);
        }
    }
}