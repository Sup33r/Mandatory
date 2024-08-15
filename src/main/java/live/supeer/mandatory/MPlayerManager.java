package live.supeer.mandatory;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import live.supeer.mandatory.utils.DateUtil;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MPlayerManager {
    public static Mandatory plugin;

    @Getter
    private static List<MPlayer> players = new ArrayList<>();

    public static MPlayer getPlayerFromUUID(UUID uuid) {
        try {
            DbRow row = DB.getFirstRow("SELECT * FROM `md_players` WHERE `playerUUID` = ?", uuid.toString());
            return row != null ? new MPlayer(row) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static MPlayer getPlayerFromName(String name) {
        try {
            DbRow row = DB.getFirstRow("SELECT * FROM `md_players` WHERE `name` = ?", name);
            return row != null ? new MPlayer(row) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MPlayer getPlayerFromPlayer(Player player) {
        try {
            DbRow row = DB.getFirstRow("SELECT * FROM `md_players` WHERE `playerUUID` = ?", player.getUniqueId().toString());
            return row != null ? new MPlayer(row) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getPlayerNamesFromUUIDs(List<UUID> uuids) {
        List<String> names = new ArrayList<>();
        for (UUID uuid : uuids) {
            MPlayer player = getPlayerFromUUID(uuid);
            if (player != null) {
                names.add(player.getName());
            }
        }
        return names;
    }


    public static List<String> getIgnoredByPlayers(UUID playerUUID) {
        List<String> ignoredByPlayers = new ArrayList<>();
        try {
            for (DbRow ignoredBy : DB.getResults("SELECT * FROM `md_players` WHERE `ignores` LIKE ? AND `ignores` IS NOT NULL", "%" + playerUUID.toString() + "%")) {
                ignoredByPlayers.add(ignoredBy.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ignoredByPlayers;
    }

    public static void handleLoveSent(Player from, Player to, String reason) {
        plugin.sendMessage(from, "messages.love.sent", "%player%", to.getName());
        if (reason == null) {
            plugin.sendMessage(to, "messages.love.received.noReason", "%player%", from.getName());
        } else {
            plugin.sendMessage(to, "messages.love.received.reason", "%player%", from.getName(), "%reason%", reason);
        }
        try {
            DB.executeUpdate("INSERT INTO md_loves (playerUUID, senderUUID, reason, dateTime) VALUES (?, ?, ?, ?)", from.getUniqueId().toString(), to.getUniqueId().toString(), Database.sqlString(reason), DateUtil.getTimestamp());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
