package live.supeer.mandatory;

import live.supeer.apied.ApiedAPI;
import live.supeer.apied.MPlayer;
import live.supeer.metropolis.Metropolis;
import live.supeer.metropolis.MetropolisAPI;
import live.supeer.metropolis.city.City;
import live.supeer.metropolis.city.Member;
import live.supeer.metropolis.city.Role;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class MandatoryChat {

    public static void modChatHelping(Player helper, Player player) {
        Channel channel = ChannelManager.getChannel("modzone");
        if (channel != null) {
            for (UUID memberUUID : channel.getMembers()) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member == null) {
                    continue;
                }
                player.sendMessage("");
                Mandatory.sendMessage(member, "messages.modchat.helping", "%helper%", helper.getName(), "%player%", player.getName());
                player.sendMessage("");
            }
        }
    }

    public static void modChatAnnouncement(String message) {
        Channel channel = ChannelManager.getChannel("modzone");
        if (channel != null) {
            for (UUID memberUUID : channel.getMembers()) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member == null) {
                    continue;
                }
                Mandatory.sendMessage(member, "messages.modchat.announcement", "%message%", message);
            }
        }
    }

    public static void helpopMessage(Player player, String message) {
        boolean recieved = false;
        Channel channel = ChannelManager.getChannel("modzone");
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            if (channel != null) {
                for (UUID memberUUID : channel.getMembers()) {
                    Player member = Bukkit.getPlayer(memberUUID);
                    if (member == null) {
                        continue;
                    }
                    recieved = true;
                    Mandatory.sendMessage(onlinePlayer, "messages.helpop.message", "%player%", player.getName(), "%message%", Mandatory.cleanedMessage(message));
                }
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
            onlinePlayer.playSound(onlinePlayer.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
            Mandatory.sendMessage(onlinePlayer, "messages.broadcast.public", "%player%", player.getName(), "%message%", Mandatory.cleanedMessage(message));
        }
    }

    public static void sendBroadcast(String message) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
            Mandatory.sendMessage(onlinePlayer, "messages.broadcast.admin", "%message%", message);
        }
    }

    public static String getPrefix(Player player) {
        MPlayer mPlayer = ApiedAPI.getPlayer(player);
        if (mPlayer == null) {
            return "";
        }
        if (mPlayer.getChatprefix() != null) {
            return mPlayer.getChatprefix();
        } else {
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
    }

    public static boolean isDuplicateMessage(Player player, String message) {
        if (Mandatory.lastMessage.containsKey(player)) {
            if (Mandatory.lastMessage.get(player).equals(message)) {
                return true;
            }
        }
        Mandatory.lastMessage.put(player, message);
        return false;
    }

    public static boolean isFilteredMessage(String message, Player player) {
        if (player.hasPermission("mandatory.bypass.filter")) {
            return false;
        }
        List<String> bannedWords = Mandatory.configuration.getBannedWords();
        String lowerCaseMessage = message.toLowerCase();

        Map<Character, Character> replacements = new HashMap<>();
        replacements.put('a', '4');
        replacements.put('e', '3');
        replacements.put('i', '1');
        replacements.put('o', '0');
        replacements.put('s', '5');
        replacements.put('t', '7');

        for (String bannedWord : bannedWords) {
            String lowerCaseBannedWord = bannedWord.toLowerCase();
            if (lowerCaseMessage.contains(lowerCaseBannedWord)) {
                return true;
            }

            StringBuilder regexBuilder = new StringBuilder();
            for (char c : lowerCaseBannedWord.toCharArray()) {
                if (replacements.containsKey(c)) {
                    regexBuilder.append("[").append(c).append(replacements.get(c)).append("]");
                } else {
                    regexBuilder.append(c);
                }
            }
            String regex = regexBuilder.toString();
            if (lowerCaseMessage.matches(".*" + regex + ".*")) {
                return true;
            }
        }

        return false;
    }

    public static String outlinedBannedWords(String message) {
        List<String> bannedWords = Mandatory.configuration.getBannedWords();
        String lowerCaseMessage = message.toLowerCase();

        Map<Character, Character> replacements = new HashMap<>();
        replacements.put('a', '4');
        replacements.put('e', '3');
        replacements.put('i', '1');
        replacements.put('o', '0');
        replacements.put('s', '5');
        replacements.put('t', '7');

        for (String bannedWord : bannedWords) {
            String lowerCaseBannedWord = bannedWord.toLowerCase();

            // Replace exact match
            message = message.replaceAll("(?i)" + lowerCaseBannedWord, "<#FF8D8D>" + bannedWord + "</#FF8D8D>");

            // Replace variations with number replacements
            StringBuilder regexBuilder = new StringBuilder();
            for (char c : lowerCaseBannedWord.toCharArray()) {
                if (replacements.containsKey(c)) {
                    regexBuilder.append("[").append(c).append(replacements.get(c)).append("]");
                } else {
                    regexBuilder.append(c);
                }
            }
            String regex = regexBuilder.toString();
            message = message.replaceAll("(?i)" + regex, "<#FF8D8D>" + bannedWord + "</#FF8D8D>");
        }

        return message;
    }

    public static void handleFilteredMessage(String message, Player player) {
        modChatAnnouncement(player.getName() + " försökte skicka ett meddelande som innehöll blockerade ord: <white>" + outlinedBannedWords(message));
        Mandatory.sendMessage(player, "messages.chat.filtered");
    }

    public static void sendMessage(Player player, TextComponent component) {
        String message = component.content();
        Channel channel = Mandatory.activeChannel.get(player.getUniqueId());
        sendChannelMessage(player, channel, message);
    }

    public static void sendMessage(Player player, Channel channel, TextComponent component) {
        String message = component.content();
        sendChannelMessage(player, channel, message);
    }

    public static void sendMeetingMessage(Player player, TextComponent component) {
        List<Player> playerList = MetropolisAPI.playersInPlotAtPlayer(player);
        String message = component.content();
        if (playerList == null) {
            return;
        }
        if (isDuplicateMessage(player, message)) {
            Mandatory.sendMessage(player, "messages.chat.duplicate");
            return;
        }
        if (MandatoryChat.isFilteredMessage(message, player)) {
            MandatoryChat.handleFilteredMessage(message, player);
            return;
        }
        for (Player onlinePlayer : playerList) {
            Mandatory.sendMessage(onlinePlayer, "messages.chat.meeting", "%player%", player.getName(), "%message%", message);
        }
    }

    private static void sendChannelMessage(Player player, Channel channel, String message) {
        if (channel != null) {
            if (channel.isDisabled() && !Mandatory.chatOverride.contains(player)) {
                Mandatory.sendMessage(player, "messages.chat.channelDisabled");
                return;
            }
            MPlayer mPlayer = ApiedAPI.getPlayer(player);
            if (mPlayer == null) {
                return;
            }
            if (!mPlayer.isModzoneAccess()) {
                message = Mandatory.cleanedMessage(message);
            }
            if (mPlayer.hasFlag('c')) {
                message = "<aqua>" + message + "</aqua>";
            }
            if (isDuplicateMessage(player, message) && !mPlayer.isModzoneAccess()) {
                Mandatory.sendMessage(player, "messages.chat.duplicate");
                return;
            }
            if (MandatoryChat.isFilteredMessage(message, player)) {
                MandatoryChat.handleFilteredMessage(message, player);
                return;
            }

            // Check for mentions and highlight them
            Map<Player, String> personalizedMessages = new HashMap<>();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                String username = onlinePlayer.getName();
                if (message.toLowerCase().contains(username.toLowerCase())) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
                    String personalizedMessage = message.replaceAll("(?i)" + username, "<#FFDE9F>" + username + "</#FFDE9F>");
                    personalizedMessages.put(onlinePlayer, personalizedMessage);
                }
            }

            for (UUID memberUUID : channel.getMembers()) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member != null) {
                    String finalMessage = personalizedMessages.getOrDefault(member, message);
                    if (channel.getType().equals(ChannelType.CITY)) {
                        String cityname = MetropolisAPI.getCityByName(channel.getName()).getCityName();
                        Mandatory.sendMessage(member, "messages.chat.city", "%player%", player.getName(), "%message%", finalMessage, "%cityname%", cityname);
                        if (!memberUUID.equals(player.getUniqueId())) {
                            Mandatory.getInstance().logger.info("[CITYCHAT] [" + cityname + "] " + player.getName() + ": " + finalMessage);
                        }
                        continue;
                    }
                    if (channel.getType().equals(ChannelType.MOD)) {
                        Mandatory.sendMessage(member, "messages.chat.mod", "%player%", player.getName(), "%message%", finalMessage);
                        if (!memberUUID.equals(player.getUniqueId())) {
                            Mandatory.getInstance().logger.info("[MODCHAT] " + player.getName() + ": " + finalMessage);
                        }
                        continue;
                    }
                    if (channel.getType().equals(ChannelType.GLOBAL)) {
                        Mandatory.sendMessage(member, "messages.chat.global", "%player%", player.getName(), "%message%", finalMessage, "%prefixes%", getPrefix(player));
                        if (!memberUUID.equals(player.getUniqueId())) {
                            Mandatory.getInstance().logger.info("[GLOBAL] " + player.getName() + ": " + finalMessage);
                        }
                        continue;
                    }
                    Mandatory.sendMessage(member, "messages.chat.other", "%player%", player.getName(), "%message%", finalMessage, "%channel%", channel.getName().toLowerCase());
                    if (!memberUUID.equals(player.getUniqueId())) {
                        Mandatory.getInstance().logger.info("[CHANNEL] " + player.getName() + " -> " + channel.getName() + ": " + finalMessage);
                    }
                }
            }
        } else {
            Mandatory.sendMessage(player, "messages.chat.noActiveChannel");
        }
    }

    public static void sendPrivateMessage(Player player, Player target, String message) {
        if (MandatoryChat.isFilteredMessage(message, player)) {
            MandatoryChat.handleFilteredMessage(message, player);
            return;
        }
        message = Mandatory.cleanedMessage(message);
        boolean isAfk = Metropolis.afkPlayers.containsKey(target.getUniqueId());
        target.playSound(target.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
        if (isAfk) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Component recipientMessage = Mandatory.getMessageComponent("messages.message.receivedAfk","%time%", format.format(now), "%sender%", player.getName(), "%message%", message);
            Metropolis.afkPlayers.get(target.getUniqueId()).add(recipientMessage);
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
        message = Mandatory.cleanedMessage(message);
        if (isDuplicateMessage(player, message)) {
            Mandatory.sendMessage(player, "messages.chat.duplicate");
            return;
        }
        if (MandatoryChat.isFilteredMessage(message, player)) {
            MandatoryChat.handleFilteredMessage(message, player);
            return;
        }
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

    public static void sendJoinLeaveMessage(String message) {
        for (Player onlinePlayer : Mandatory.getInstance().getServer().getOnlinePlayers()) {
            MPlayer mPlayer = ApiedAPI.getPlayer(onlinePlayer);
            if (mPlayer == null || mPlayer.hasFlag('j')) {
                continue;
            }
            onlinePlayer.sendMessage(message);
        }
    }
}
