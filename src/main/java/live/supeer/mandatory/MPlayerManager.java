package live.supeer.mandatory;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import live.supeer.mandatory.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MPlayerManager {

    @Getter
    private static final List<MPlayer> players = new ArrayList<>();

    public static void addPlayer(MPlayer player) {
        players.add(player);
    }

    public static void removePlayer(MPlayer player) {
        players.remove(player);
    }

    public static MPlayer createPlayer(Player player) {
        try {
            DB.executeInsert("INSERT INTO `md_players` (`uuid`, `name`, `firstJoin`, `latestJoin`, `balance`) VALUES (?, ?, ?, ?, ?)",
                    player.getUniqueId().toString(),
                    player.getName(),
                    DateUtil.getTimestamp(),
                    DateUtil.getTimestamp(),
                    Mandatory.configuration.getStartingBalance());
            return new MPlayer(DB.getFirstRow("SELECT * FROM `md_players` WHERE `uuid` = ?", player.getUniqueId().toString()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MPlayer getPlayerFromUUID(UUID uuid) {
        try {
            DbRow row = DB.getFirstRow("SELECT * FROM `md_players` WHERE `uuid` = ?", uuid.toString());
            return row != null ? new MPlayer(row) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static MPlayer getPlayerFromName(String name) {
        try {
            DbRow row = DB.getFirstRow("SELECT * FROM `md_players` WHERE `name` = ?", name);
            return row != null ? new MPlayer(row) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MPlayer getPlayerFromPlayer(Player player) {
        for (MPlayer mPlayer : players) {
            if (mPlayer.getUuid().equals(player.getUniqueId())) {
                return mPlayer;
            }
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
            throw new RuntimeException(e);
        }
        return ignoredByPlayers;
    }

    public static void handleLoveSent(Player from, Player to, String reason) {
        Mandatory.sendMessage(from, "messages.love.sent", "%player%", to.getName());
        if (reason == null) {
            Mandatory.sendMessage(to, "messages.love.received.noReason", "%player%", from.getName());
        } else {
            Mandatory.sendMessage(to, "messages.love.received.reason", "%player%", from.getName(), "%reason%", reason);
        }
        try {
            DB.executeUpdate("INSERT INTO md_loves (playerUUID, senderUUID, reason, dateTime) VALUES (?, ?, ?, ?)", from.getUniqueId().toString(), to.getUniqueId().toString(), Database.sqlString(reason), DateUtil.getTimestamp());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long createLoginLog(UUID uuid, String ip) {
        try {
            Long dateTime = DateUtil.getTimestamp();
            DB.executeUpdate("INSERT INTO md_loginlogs (playerUUID, playerIp, dateTime) VALUES (?, ?, ?)", uuid.toString(), ip, dateTime);
            return dateTime;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateLoginLogPlaytime(UUID uuid, long dateTime, long playtime) {
        try {
            DB.executeUpdate("UPDATE md_loginlogs SET playtime = ? WHERE playerUUID = ? AND dateTime = ?", playtime, uuid.toString(), dateTime);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
