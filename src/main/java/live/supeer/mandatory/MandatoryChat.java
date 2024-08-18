package live.supeer.mandatory;

import live.supeer.apied.ApiedAPI;
import live.supeer.apied.MPlayer;
import live.supeer.metropolis.MetropolisAPI;
import live.supeer.metropolis.city.City;
import live.supeer.metropolis.city.Member;
import live.supeer.metropolis.city.Role;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
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

    public static void sendBroadcast(String message) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("mandatory.broadcast")) {
                Mandatory.sendMessage(onlinePlayer, "messages.broadcast.admin", "%message%", message);
            }
        }
    }

    public static String getPrefix(Player player) {
        MPlayer mPlayer = ApiedAPI.getPlayer(player);
        if (mPlayer == null) {
            return "";
        }
        boolean externalPrefix = mPlayer.hasFlag('e');
        boolean isVip = player.hasPermission("mandatory.vip");
        boolean isMod = player.hasPermission("mandatory.mod");
        StringBuilder prefix = new StringBuilder();
        City city = MetropolisAPI.getHomeCity(player);
        if (!(city == null) && !externalPrefix) {
            prefix.append(Mandatory.getRawMessage("messages.chat.prefix.city.name", "%cityname%", city.getCityName()));
            if (city.getCityMember(player.getUniqueId().toString()) != null) {
                Member member = city.getCityMember(player.getUniqueId().toString());
                if (member.getCityRole().equals(Role.INVITER)) {
                    prefix.append(Mandatory.getRawMessage("messages.chat.prefix.city.role.inviter"));
                } else if (member.getCityRole().equals(Role.ASSISTANT)) {
                    prefix.append(Mandatory.getRawMessage("messages.chat.prefix.city.role.assistant"));
                } else if (member.getCityRole().equals(Role.VICE_MAYOR)) {
                    prefix.append(Mandatory.getRawMessage("messages.chat.prefix.city.role.vicemayor"));
                } else if (member.getCityRole().equals(Role.MAYOR)) {
                    prefix.append(Mandatory.getRawMessage("messages.chat.prefix.city.role.mayor"));
                }
            }
        }
        if (isVip && !externalPrefix) {
            prefix.append(Mandatory.getRawMessage("messages.chat.prefix.vip"));
        }
        if (isMod) {
            prefix.append(Mandatory.getRawMessage("messages.chat.prefix.mod"));
        }
        return prefix.toString();
    }

    public static Component getTABPrefix(Player player) {
        MPlayer mPlayer = ApiedAPI.getPlayer(player);
        if (mPlayer == null) {
            return null;
        }
        if (mPlayer.getChatprefix() != null && !mPlayer.getChatprefix().isEmpty()) {
            return Mandatory.stringToMiniMessage(mPlayer.getChatprefix() + player.getName());
        }
        if (player.hasPermission("mandatory.mod")) {
            return Mandatory.getMessageComponent("messages.chat.prefix.tab.mod", "%player%", player.getName());
        }
        return null;
    }

    public static void sendMessage(Player player, TextComponent component) {
        String message = component.content();
        MPlayer mPlayer = ApiedAPI.getPlayer(player);
        if (mPlayer == null) {
            return;
        }
        if (mPlayer.hasFlag('c')) {
            message = "<aqua>" + message + "</aqua>";
        }
        Channel channel = Mandatory.activeChannel.get(player.getUniqueId());
        if (channel != null) {
            for (UUID memberUUID : channel.getMembers()) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member != null) {
                    if (channel.getType().equals(ChannelType.CITY)) {
                        String cityname = MetropolisAPI.getCityByName(channel.getName()).getCityName();
                        Mandatory.sendMessage(member, "messages.chat.city", "%player%", player.getName(), "%message%", message, "%cityname%", cityname);
                        Mandatory.getInstance().logger.info("[CITYCHAT] [" + cityname + "] " + player.getName() + ": " + message);
                        return;
                    }
                    if (channel.getType().equals(ChannelType.MOD)) {
                        Mandatory.sendMessage(member, "messages.chat.mod", "%player%", player.getName(), "%message%", message);
                        Mandatory.getInstance().logger.info("[MODCHAT] " + player.getName() + ": " + message);
                        return;
                    }
                    if (channel.getType().equals(ChannelType.GLOBAL)) {
                        Mandatory.sendMessage(member, "messages.chat.global", "%player%", player.getName(), "%message%", message, "%prefixes%", getPrefix(player));
                        Mandatory.getInstance().logger.info("[GLOBAL] " + player.getName() + ": " + message);
                        return;
                    }
                    Mandatory.sendMessage(member, "messages.chat.other", "%player%", player.getName(), "%message%", message, "%channel%", channel.getName().toLowerCase());
                    Mandatory.getInstance().logger.info("[CHANNEL] " + player.getName() + " -> " + channel.getName() + ": " + message);
                }
            }
        } else {
            Mandatory.sendMessage(player, "messages.chat.noActiveChannel");
        }
    }

    public static void sendPrivateMessage(Player player, Player target, String message) {
        boolean isAfk = Mandatory.afkPlayers.containsKey(target.getUniqueId());
        if (isAfk) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Component recipientMessage = Mandatory.getMessageComponent("messages.message.receivedAfk","%time%", format.format(now), "%sender%", player.getName(), "%message%", message);
            Mandatory.afkPlayers.get(target.getUniqueId()).add(recipientMessage);
            Mandatory.sendMessage(player, "messages.message.sentAfk", "%receiver%", target.getName(), "%message%", message);
            Mandatory.sendMessage(target,"messages.message.received", "%sender%", player.getName(), "%message%", message);
        } else {
            Mandatory.sendMessage(player, "messages.message.sent", "%receiver%", target.getName(), "%message%", message);
            Mandatory.sendMessage(target,"messages.message.received", "%sender%", player.getName(), "%message%", message);
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
