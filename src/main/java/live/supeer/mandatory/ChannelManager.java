package live.supeer.mandatory;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import live.supeer.metropolis.MetropolisAPI;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class ChannelManager {
    private static final Map<String, Channel> channels = new HashMap<>();

    public static void loadChannels() {
        try {
            List<DbRow> rows = DB.getResults("SELECT * FROM md_channels");
            for (DbRow row : rows) {
                Channel channel = new Channel(row);
                channels.put(channel.getName(), channel);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initiatePlayer(Player player) {
        try {
            DbRow playerRow = DB.getFirstRow("SELECT * FROM md_players WHERE uuid = ?", player.getUniqueId());
            if (playerRow != null) {
                Channel activeChannel = getChannel(playerRow.getString("chatChannel"));
                if (activeChannel != null) {
                    Mandatory.activeChannel.put(player.getUniqueId(), activeChannel);
                }
            }
            List<Channel> playerChannels = new ArrayList<>();
            List<DbRow> rows = DB.getResults("SELECT * FROM md_channelmembers WHERE playerUUID = ?", player.getUniqueId());
            if (rows.isEmpty()) {
                for (Channel channel : channels.values()) {
                    if (channel.getType().equals(ChannelType.GLOBAL)) {
                        if (!player.hasPermission("mandatory.chat."+channel.getName().toLowerCase())) {
                            continue;
                        }
                        playerChannels.add(channel);
                        setActiveChannel(player, channel.getName().toLowerCase());
                        updateActiveChannel(player, channel.getName().toLowerCase());
                        channel.addMember(player.getUniqueId());
                    }
                }
            } else {
                for (DbRow row : rows) {
                    String channelName = row.getString("channelName");
                    Channel channel = getChannel(channelName);
                    if (channel != null) {
                        playerChannels.add(channel);
                    }
                }
            }

            Mandatory.playerChannels.put(player.getUniqueId(), playerChannels);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void joinChannel(Player player, String channelName) {
        channelName = channelName.replace("#", "");
        Channel channel = channels.get(channelName.toLowerCase());
        if (channel != null) {
            if (channel.hasMember(player.getUniqueId())) {
                Mandatory.sendMessage(player, "messages.chat.channels.alreadyInChannel");
                return;
            }
            if (channel.getType() == ChannelType.CITY) {
                if (MetropolisAPI.isPlayerCityMember(player, channelName.toLowerCase())) {
                    channel.addMember(player.getUniqueId());
                    setActiveChannel(player, channelName.toLowerCase());
                    Mandatory.sendMessage(player, "messages.chat.channels.joined", "%channel%", channelName.toLowerCase());
                } else {
                    Mandatory.sendMessage(player, "messages.chat.channels.notInCity");
                }
            } else if (player.hasPermission("mandatory.chat." + channelName.toLowerCase())) {
                channel.addMember(player.getUniqueId());
                setActiveChannel(player, channelName.toLowerCase());
                Mandatory.sendMessage(player, "messages.chat.channels.joined", "%channel%", channelName.toLowerCase());
            } else {
                Mandatory.sendMessage(player, "messages.chat.channels.noPermission");
            }
        } else {
            Mandatory.sendMessage(player, "messages.chat.channels.notFound");
        }
    }

    public static void leaveChannel(Player player, String channelName) {
        channelName = channelName.replace("#", "");
        Channel channel = channels.get(channelName);
        if (channel != null && channel.hasMember(player.getUniqueId())) {
            channel.removeMember(player.getUniqueId());
            setActiveChannelToOther(player);
            Mandatory.sendMessage(player, "messages.chat.channels.left", "%channel%", channelName.toLowerCase());
        } else {
            Mandatory.sendMessage(player, "messages.chat.channels.notInChannel");
        }
    }

    public static void switchChannel(Player player, String channelName) {
        channelName = channelName.replace("#", "");
        Channel channel = channels.get(channelName.toLowerCase());
        if (channel != null && channel.hasMember(player.getUniqueId())) {
            setActiveChannel(player, channelName.toLowerCase());
            Mandatory.sendMessage(player, "messages.chat.channels.switched", "%channel%", channelName.toLowerCase());
        } else {
            Mandatory.sendMessage(player, "messages.chat.channels.notInChannel");
        }
    }

    public static List<String> getChannels(Player player) {
        List<String> channelNames = new ArrayList<>();
        for (Channel channel : channels.values()) {
            if (channel.hasMember(player.getUniqueId())) {
                if (isActiveChannel(player, channel)) {
                    channelNames.add("<italic>#" + channel.getName().toLowerCase() + "</italic>");
                } else {
                    channelNames.add("#" + channel.getName().toLowerCase());
                }
            }
        }
        return channelNames;
    }

    public static String parseCityChannelName(String channelName) {
        String parsedName = channelName.replace("#", "");
        parsedName = parsedName.replace(" ", "_");
        parsedName = parsedName.toLowerCase();
        return parsedName;
    }

    // Other utility methods as needed
    public static void createChannel(String name, ChannelType type) {
        name = name.replace("#", "");
        try {
            DB.executeInsert("INSERT INTO md_channels (channelName, channelType) VALUES (?, ?)", name.toLowerCase(), type.getType());
            channels.put(name, new Channel(DB.getFirstRow("SELECT * FROM md_channels WHERE channelName = ?", name.toLowerCase())));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteChannel(String name) {
        name = name.replace("#", "");
        try {
            DB.executeUpdate("DELETE FROM md_channels WHERE channelName = ?", name.toLowerCase());
            channels.remove(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Channel getChannel(String channelName) {
        channelName = channelName.replace("#", "");
        return channels.get(channelName.toLowerCase());
    }

    public static boolean isActiveChannel(Player player, Channel channel) {
        return Mandatory.activeChannel.get(player.getUniqueId()).equals(channel);
    }

    public static void setActiveChannel(Player player, String channelName) {
        channelName = channelName.replace("#", "");
        Mandatory.activeChannel.put(player.getUniqueId(), channels.get(channelName.toLowerCase()));
        updateActiveChannel(player, channelName.toLowerCase());
    }

    public static void setActiveChannelToOther(Player player) {
        List<Channel> playerChannels = Mandatory.playerChannels.get(player.getUniqueId());
        if (playerChannels != null && !playerChannels.isEmpty()) {
            Channel currentActiveChannel = Mandatory.activeChannel.get(player.getUniqueId());
            Optional<Channel> otherChannel = playerChannels.stream()
                    .filter(channel -> !channel.equals(currentActiveChannel))
                    .findAny();
            otherChannel.ifPresent(channel -> {
                Mandatory.activeChannel.put(player.getUniqueId(), channel);
                updateActiveChannel(player, channel.getName());
            });
        }
    }

    public static void updateActiveChannel(Player player, String channelName) {
        channelName = channelName.replace("#", "");
        try {
            DB.executeUpdate("UPDATE md_players SET chatChannel = ? WHERE uuid = ?", channelName.toLowerCase(), player.getUniqueId().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
