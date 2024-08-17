package live.supeer.mandatory;

import co.aikar.idb.DB;
import live.supeer.mandatory.utils.DateUtil;

import java.sql.SQLException;
import java.util.UUID;

public class BalanceManager {

    public static void createBalanceLogEntry(UUID uuid, double balanceBefore, double balanceAfter, String reason) {
        try {
            DB.executeUpdate("INSERT INTO md_balancelogs (playerUUID, dateTime, balanceBefore, balanceAfter, jsonLog) VALUES (?, ?, ?, ?, ?)", uuid.toString(), DateUtil.getTimestamp(), balanceBefore, balanceAfter, Database.sqlString(reason));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
