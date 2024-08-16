package live.supeer.mandatory;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.DB;

import java.sql.SQLException;

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
                              CREATE TABLE IF NOT EXISTS `md_players` (
                                `uuid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `firstJoin` bigint(30) DEFAULT NULL,
                                `latestJoin` bigint(30) DEFAULT NULL,
                                `latestLeave` bigint(30) DEFAULT NULL,
                                `flags` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                `balance` int(25) NOT NULL DEFAULT 0,
                                `ignores` text COLLATE utf8mb4_unicode_ci,
                                `chatprefix` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                `backLocation` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                PRIMARY KEY (`uuid`)
                              ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;""");

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
                              CREATE TABLE IF NOT EXISTS `md_balancelogs` (
                                `logId` int(11) NOT NULL AUTO_INCREMENT,
                                `playerUUID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `dateTime` bigint(30) DEFAULT NULL,
                                `balanceBefore` int(25) NOT NULL,
                                `balanceAfter` int(25) NOT NULL,
                                `jsonLog` text NOT NULL,
                                PRIMARY KEY (logId)
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
                              CREATE TABLE IF NOT EXISTS `md_homes` (
                                `playerUUID` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                `homeName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `location` varchar(255) COLLATE utf8mb4_unicode_ci,
                                PRIMARY KEY (`playerUUID`, `homeName`)
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

}
