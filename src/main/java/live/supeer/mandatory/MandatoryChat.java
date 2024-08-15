package live.supeer.mandatory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MandatoryChat {

    public static void sendModChatMessage(Player player, String message) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("mandatory.modchat")) {
                Mandatory.sendMessage(onlinePlayer, "messages.modchat.message", "%player%", player.getName(), "%message%", message);
            }
        }
    }

    public static void modChatHelping(Player helper, Player player) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("mandatory.modchat")) {
                player.sendMessage("");
                Mandatory.sendMessage(onlinePlayer, "messages.modchat.helping", "%helper%", helper.getName(), "%player%", player.getName());
                player.sendMessage("");
            }
        }
    }

    public static void modChatAnnouncement(String message) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("mandatory.modchat")) {
                Mandatory.sendMessage(onlinePlayer, "messages.modchat.announcement", "%message%", message);
            }
        }
    }

    public static void helpopMessage(Player player, String message) {
        boolean recieved = false;
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("mandatory.modchat")) {
                recieved = true;
                Mandatory.sendMessage(onlinePlayer, "messages.helpop.message", "%player%", player.getName(), "%message%", message);
            }
        }
        if (!recieved) {
            Mandatory.sendMessage(player, "messages.helpop.noRecipients");
            return;
        }
        Mandatory.sendMessage(player, "messages.helpop.sent");
    }

    public static void sendPublicBroadcast(Player player, String message) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            Mandatory.sendMessage(onlinePlayer, "messages.broadcast.public", "%player%", player.getName(), "%message%", message);
        }
    }

    public static void sendPrivateMessage(Player player, Player target, String message) {
        boolean isAfk = Mandatory.afkPlayers.containsKey(target);
        if (isAfk) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Component recipientMessage = Mandatory.getMessageComponent("messages.message.receivedAfk","%time%", format.format(now), "%sender%", player.getName(), "%message%", message);
            Mandatory.afkPlayers.get(target).add(recipientMessage);
            Mandatory.sendMessage(player, "messages.message.sentAfk", "%receiver%", target.getName(), "%message%", message);
            Mandatory.sendMessage(player,"messages.message.received", "%sender%", player.getName(), "%message%", message);
        } else {
            Mandatory.sendMessage(player, "messages.message.sent", "%receiver%", target.getName(), "%message%", message);
            Mandatory.sendMessage(player,"messages.message.received", "%sender%", player.getName(), "%message%", message);
        }

        Mandatory.getInstance().logger.info("[DM] " + player.getName() + " -> " + target.getName() + ": " + message);

        Mandatory.replyPlayers.put(player, target);
        Mandatory.replyPlayers.put(target, player);
    }

    public static void sendMeMessage(Player player, String message) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            Mandatory.sendMessage(onlinePlayer, "messages.me", "%player%", player.getName(), "%message%", message);
        }
    }

    public static void sendToAllBut(String message, List<UUID> players) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            if (!players.contains(onlinePlayer.getUniqueId())) {
                onlinePlayer.sendMessage(message);
            }
        }
    }

    public static void sendToAll(String message) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }

    public static void sendToAll(Component message) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }
}
