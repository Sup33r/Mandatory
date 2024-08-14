package live.supeer.mandatory;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import live.supeer.mandatory.utils.LocationUtil;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoManager {
    public static Mandatory plugin;

    public static void initDBSync() throws SQLException {
        loadGoes();
    }

    private static void loadGoes() throws SQLException {
        var dbRows = DB.getResults("SELECT * FROM `md_goes`;");
        for (DbRow dbRow : dbRows) {
            Go go = new Go(dbRow);
            // Add to a list or map if you want to keep goes in memory
        }
    }

    public static Go createGo(String name, Player player) {
        try {
            DB.executeInsert("INSERT INTO `md_goes` (`goName`, `ownerUUID`, `createDate`, `position`, `charge`, `open`, `whitelistcharge`) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    name, player.getUniqueId().toString(), System.currentTimeMillis(), LocationUtil.locationToString(player.getLocation()), 0, false, false);
            return new Go(DB.getFirstRow("SELECT * FROM `md_goes` WHERE `goName` = ?", name));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getLatestUse(Go go) {
        try {
            DbRow result = DB.getFirstRow("SELECT MAX(dateTime) as latestUse FROM md_gologs WHERE goName = ?", go.getName());
            if (result != null) {
                return result.getLong("latestUse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Log the error
            plugin.getLogger().warning("Error fetching latest use for go: " + go.getName());
        }
        return 0; // Return 0 if no usage found or in case of an error
    }

    public static boolean goExists(String name) {
        try {
            return DB.getFirstRow("SELECT * FROM `md_goes` WHERE LOWER(`goName`) = ?", name.toLowerCase()) != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Go getGo(String name) {
        try {
            DbRow row = DB.getFirstRow("SELECT * FROM `md_goes` WHERE LOWER(`goName`) = ?", name.toLowerCase());
            return row != null ? new Go(row) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
            e.printStackTrace();
        }
        return list;
    }

    public static void deleteGo(Go go) {
        try {
            DB.executeUpdate("DELETE FROM `md_goes` WHERE `goName` = ?", go.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateGoOwner(String name, UUID newOwnerUUID) {
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `ownerUUID` = ? WHERE LOWER(`goName`) = ?", newOwnerUUID.toString(), name.toLowerCase());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateGoLocation(String name, Location newLocation) {
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `position` = ? WHERE LOWER(`goName`) = ?", LocationUtil.locationToString(newLocation), name.toLowerCase());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void toggleGoOpen(String name) {
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `open` = NOT `open` WHERE LOWER(`goName`) = ?", name.toLowerCase());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void logGoUsage(UUID playerUUID, Go go) {
        try {
            DB.executeInsert("INSERT INTO `md_gologs` (`goName`, `playerUUID`, `dateTime`, `charge`) VALUES (?, ?, ?, ?)",
                    go.getName(), playerUUID.toString(), System.currentTimeMillis(), go.getChargeAmount());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}