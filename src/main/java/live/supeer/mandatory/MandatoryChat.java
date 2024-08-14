package live.supeer.mandatory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MandatoryChat {
    public static Mandatory plugin;

    public static void sendModChatMessage(Player player, String message) {
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("mandatory.modchat")) {
                plugin.sendMessage(onlinePlayer, "messages.modchat.message", "%player%", player.getName(), "%message%", message);
            }
        }
    }

    public static void modChatHelping(Player helper, Player player) {
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("mandatory.modchat")) {
                player.sendMessage("");
                plugin.sendMessage(onlinePlayer, "messages.modchat.helping", "%helper%", helper.getName(), "%player%", player.getName());
                player.sendMessage("");
            }
        }
    }

    public static void helpopMessage(Player player, String message) {
        boolean recieved = false;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("mandatory.modchat")) {
                recieved = true;
                plugin.sendMessage(onlinePlayer, "messages.helpop.message", "%player%", player.getName(), "%message%", message);
            }
        }
        if (!recieved) {
            plugin.sendMessage(player, "messages.helpop.noRecipients");
            return;
        }
        plugin.sendMessage(player, "messages.helpop.sent");
    }

    public static void sendPublicBroadcast(Player player, String message) {
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            plugin.sendMessage(onlinePlayer, "messages.broadcast.public", "%player%", player.getName(), "%message%", message);
        }
    }

    public static void sendPrivateMessage(Player player, Player target, String message) {
        boolean isAfk = plugin.afkPlayers.containsKey(target);
        if (isAfk) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Component recipientMessage = plugin.getMessageComponent("messages.message.receivedAfk","%time%", format.format(now), "%sender%", player.getName(), "%message%", message);
            plugin.afkPlayers.get(target).add(recipientMessage);
            plugin.sendMessage(player, "messages.message.sentAfk", "%receiver%", target.getName(), "%message%", message);
            plugin.sendMessage(player,"messages.message.received", "%sender%", player.getName(), "%message%", message);
        } else {
            plugin.sendMessage(player, "messages.message.sent", "%receiver%", target.getName(), "%message%", message);
            plugin.sendMessage(player,"messages.message.received", "%sender%", player.getName(), "%message%", message);
        }

        plugin.logger.info("[DM] " + player.getName() + " -> " + target.getName() + ": " + message);

        plugin.replyPlayers.put(player, target);
        plugin.replyPlayers.put(target, player);
    }

    public static void sendMeMessage(Player player, String message) {
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            plugin.sendMessage(onlinePlayer, "messages.me", "%player%", player.getName(), "%message%", message);
        }
    }

    public static void sendToAllBut(String message, List<UUID> players) {
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if (!players.contains(onlinePlayer.getUniqueId())) {
                onlinePlayer.sendMessage(message);
            }
        }
    }

    public static void sendToAll(String message) {
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }

    public static void sendToAll(Component message) {
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }
}
