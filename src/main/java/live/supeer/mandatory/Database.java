package live.supeer.mandatory;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.DB;
import live.supeer.mandatory.utils.DateUtil;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class Database {

    public static void initialize() {
        try {
            BukkitDB.createHikariDatabase(
                    Mandatory.getPlugin(),
                    Mandatory.configuration.getSqlUsername(),
                    Mandatory.configuration.getSqlPassword(),
                    Mandatory.configuration.getSqlDatabase(),
                    Mandatory.configuration.getSqlHost()
                            + ":"
                            + Mandatory.configuration.getSqlPort());
            createTables();
        } catch (Exception e) {
            Mandatory.getInstance().getLogger().warning("Failed to initialize database, disabling plugin.\nError: " + e.getMessage());
            Mandatory.getInstance().getServer().getPluginManager().disablePlugin(Mandatory.getInstance());
        }
    }

//    public static void synchronize() {
//        try {
//            CityDatabase.initDBSync();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            plugin.getLogger().warning("Failed to synchronize database, disabling plugin.");
//            plugin.getServer().getPluginManager().disablePlugin(plugin);
//        }
//    }

    public static String sqlString(String string) {
        return string == null ? "NULL" : "'" + string.replace("\\", "\\\\").replace("'", "\\'") + "'";
    }

    public static void createTables() {
        try {
            DB.executeUpdate(
                    """
                              CREATE TABLE IF NOT EXISTS `md_channels` (
                                `channelName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `channelType` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                PRIMARY KEY (channelName)
                              ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;""");
            DB.executeUpdate(
                    """
                              CREATE TABLE IF NOT EXISTS `md_channelmembers` (
                                `channelName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `playerUUID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
                                PRIMARY KEY (`channelName`, `playerUUID`),
                                FOREIGN KEY (`channelName`) REFERENCES `md_channels` (`channelName`) ON DELETE CASCADE
                              ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;""");
            DB.executeUpdate(
                    """
                              CREATE TABLE IF NOT EXISTS `md_namechanges` (
                                `playerUUID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `playerName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `dateTime` bigint(30) DEFAULT NULL,
                                PRIMARY KEY (playerUUID,dateTime)
                              ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;""");
            DB.executeUpdate(
                    """
                              CREATE TABLE IF NOT EXISTS `md_loginlogs` (
                                `playerUUID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `playerIp` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `dateTime` bigint(30) DEFAULT '0',
                                `playTime` bigint(30) DEFAULT '0',
                                PRIMARY KEY (`playerUUID`, `dateTime`, `playTime`)
                              ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;""");
            DB.executeUpdate(
                    """
                              CREATE TABLE IF NOT EXISTS `md_loves` (
                                `playerUUID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `senderUUID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                `dateTime` bigint(30) NOT NULL,
                                PRIMARY KEY (`playerUUID`, `senderUUID`, `dateTime`)
                              ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;""");
            DB.executeUpdate(
                    """
                              CREATE TABLE IF NOT EXISTS `md_kicks` (
                                `playerUUID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `placerUUID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                `dateTime` bigint(30) DEFAULT NULL,
                                PRIMARY KEY (`playerUUID`, `dateTime`)
                              ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;""");
            DB.executeUpdate(
                    """
                              CREATE TABLE IF NOT EXISTS `md_bans` (
                                `playerUUID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `placerUUID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `playerIp` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `placeTime` bigint(30) DEFAULT NULL,
                                `length` bigint(30) DEFAULT NULL,
                                `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                PRIMARY KEY (`playerUUID`, `placeTime`)
                              ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;""");
            DB.executeUpdate(
                    """
                              CREATE TABLE IF NOT EXISTS `md_goes` (
                                `goName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `displayName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `ownerUUID` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                `createDate` bigint(30) DEFAULT NULL,
                                `whitelistPlayers` text COLLATE utf8mb4_unicode_ci,
                                `location` varchar(255) COLLATE utf8mb4_unicode_ci,
                                `chargeAmount` int(25) DEFAULT '0',
                                `open` tinyint(1) DEFAULT '0',
                                `whitelistcharge` tinyint(1) DEFAULT '0',
                                PRIMARY KEY (`goName`)
                              ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;""");
            DB.executeUpdate(
                    """
                              CREATE TABLE IF NOT EXISTS `md_gologs` (
                                `goName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `playerUUID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `dateTime` bigint(30) DEFAULT NULL,
                                `charge` int(25) DEFAULT '0',
                                PRIMARY KEY (`playerUUID`, `dateTime`, `goName`)
                              ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;""");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
