package live.supeer.mandatory;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import live.supeer.mandatory.utils.LocationUtil;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoManager {

    public static Go createGo(String name, Player player) {
        try {
            DB.executeInsert("INSERT INTO `md_goes` (`goName`, `ownerUUID`, `createDate`, `location`, `chargeAmount`, `open`, `whitelistcharge`) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    name, player.getUniqueId().toString(), System.currentTimeMillis(), LocationUtil.locationToString(player.getLocation()), 0, false, false);
            return new Go(DB.getFirstRow("SELECT * FROM `md_goes` WHERE `goName` = ?", name));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getLatestUse(Go go) {
        try {
            DbRow result = DB.getFirstRow("SELECT MAX(dateTime) as latestUse FROM md_gologs WHERE goName = ?", go.getName());
            if (result != null) {
                return result.getLong("latestUse");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0; // Return 0 if no usage found or in case of an error
    }

    public static boolean goExists(String name) {
        try {
            return DB.getFirstRow("SELECT * FROM `md_goes` WHERE LOWER(`goName`) = ?", name.toLowerCase()) != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Go getGo(String name) {
        try {
            DbRow row = DB.getFirstRow("SELECT * FROM `md_goes` WHERE LOWER(`goName`) = ?", name.toLowerCase());
            return row != null ? new Go(row) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Go> getGoList(Player player) {
        List<Go> list = new ArrayList<>();
        try {
            if (player.hasPermission("mandatory.go.override")) {
                var dbRows = DB.getResults("SELECT * FROM `md_goes`;");
                for (DbRow dbRow : dbRows) {
                    list.add(new Go(dbRow));
                }
            } else {
                //select all that are open, or belong to the player
                var dbRows = DB.getResults("SELECT * FROM `md_goes` WHERE `open` = ? OR `ownerUUID` = ?", 1, player.getUniqueId().toString());
                for (DbRow dbRow : dbRows) {
                    list.add(new Go(dbRow));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static void deleteGo(Go go) {
        try {
            DB.executeUpdate("DELETE FROM `md_goes` WHERE `goName` = ?", go.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logGoUsage(UUID playerUUID, Go go) {
        try {
            DB.executeInsert("INSERT INTO `md_gologs` (`goName`, `playerUUID`, `dateTime`, `charge`) VALUES (?, ?, ?, ?)",
                    go.getName(), playerUUID.toString(), System.currentTimeMillis(), go.getChargeAmount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}