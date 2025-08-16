package me.nd.upgrades.utils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.nd.upgrades.api.*;

public class MessageUtils {

    public static void send(Player p, Object... rawmsg) {
        Stream.of(rawmsg)
            .flatMap(o -> o instanceof List ? ((List<?>) o).stream() : Stream.of(o))
            .filter(o -> o instanceof String)
            .map(o -> (String) o)
            .forEach(msg -> processMessage(p, msg));
    }

    private static void processMessage(Player p, String msg) {
        String message = msg.replace("{player}", p.getName()).replace("\\n", "\n").replace("&", "§");

        MessageHandler handler = MessageHandlers.getHandler(message);
        if (handler != null) {
            handler.handle(p, message);
        }
    }

    @FunctionalInterface
    interface MessageHandler {
        void handle(Player p, String message);
    }

    enum MessageHandlers implements MessageHandler {
        MENSAGEM("mensagem: ", (p, message) -> p.sendMessage(message.replace("mensagem: ", ""))),
        BROADCAST("broadcast: ", (p, message) -> Bukkit.broadcastMessage(message.replace("broadcast: ", ""))),
        JSON("json: ", (p, message) -> JsonMessage.send(p, message.replace("json: ", ""))),
        ACTION_BAR("actionbar: ", (p, message) -> ActionBarAPI.sendActionBarMessage(p, message.replace("actionbar: ", ""))),
        TITLE("title: ", (p, message) -> TitleAPI.sendTitle(p, 20, 30, 20, message.replace("title: ", ""), "")),
        TITLE_SUB("titlesub: ", (p, message) -> {
            String[] parts = message.replace("titlesub: ", "").split(":");
            TitleAPI.sendTitle(p, 20, 30, 20, parts[0], parts.length > 1 ? parts[1] : "");
        });
        private final String prefix;
        private final BiConsumer<Player, String> handler;

        MessageHandlers(String prefix, BiConsumer<Player, String> handler) {
            this.prefix = prefix;
            this.handler = handler;
        }

        public static MessageHandler getHandler(String message) {
            for (MessageHandlers handler : values()) {
                if (message.startsWith(handler.prefix)) {
                    return handler;
                }
            }
            return null;
        }

        @Override
        public void handle(Player p, String message) {
            handler.accept(p, message.replace(prefix, ""));
        }
    }
}
