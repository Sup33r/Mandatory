package live.supeer.mandatory;

import co.aikar.idb.DB;

import java.util.UUID;

public class BalanceManager {
    public static Mandatory plugin;


    public static void createBalanceLogEntry(UUID uuid, int balanceBefore, int balanceAfter, String reason) {
        try {
            DB.executeUpdate("INSERT INTO md_balancelogs (playerUUID, balanceBefore, balanceAfter, reason) VALUES (?, ?, ?, ?)", uuid.toString(), balanceBefore, balanceAfter, Database.sqlString(reason));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
