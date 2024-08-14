package live.supeer.mandatory;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import live.supeer.mandatory.utils.LocationUtil;
import live.supeer.mandatory.utils.Utilities;
import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class MPlayer {
    private UUID uuid;
    private String name;
    private long firstJoin;
    private long latestJoin;
    private long latestLeave;
    private char[] flags;
    private int balance;
    private List<UUID> ignores;
    private Location lastLocation;

    public MPlayer(DbRow data) {
        this.uuid = UUID.fromString(data.getString("uuid"));
        this.name = data.getString("name");
        this.firstJoin = data.getLong("firstJoin");
        this.latestJoin = data.getLong("latestJoin");
        this.latestLeave = data.getLong("latestLeave");
        this.flags = data.getString("flags") == null ? new char[0] : data.getString("flags").toCharArray();
        this.balance = data.getInt("balance");
        this.ignores = new ArrayList<>(Utilities.stringToUUIDList(data.getString("ignores")));
        this.lastLocation = null;
    }

    public void setLastLocation(Location lastLocation) {
        try {
            this.lastLocation = lastLocation;
            DB.executeUpdate("UPDATE `md_players` SET `position` = " + Database.sqlString(LocationUtil.locationToString(lastLocation)) + " WHERE `uuid` = " + Database.sqlString(uuid.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBalance(int newBalance, String json) {
        try {
            BalanceManager.createBalanceLogEntry(uuid, balance, newBalance, json);
            this.balance = newBalance;
            DB.executeUpdate("UPDATE `md_players` SET `balance` = " + newBalance + " WHERE `uuid` = " + Database.sqlString(uuid.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBalance(int amount, String json) {
        try {
            BalanceManager.createBalanceLogEntry(uuid, balance, balance + amount, json);
            this.balance += amount;
            DB.executeUpdate("UPDATE `md_players` SET `balance` = " + balance + " WHERE `uuid` = " + Database.sqlString(uuid.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeBalance(int amount, String json) {
        try {
            BalanceManager.createBalanceLogEntry(uuid, balance, balance - amount, json);
            this.balance -= amount;
            DB.executeUpdate("UPDATE `md_players` SET `balance` = " + balance + " WHERE `uuid` = " + Database.sqlString(uuid.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasFlag(char needle) {
        if (this.flags == null) {
            return false;
        }

        for (char option : this.flags) {
            if (option == needle) {
                return true;
            }
        }
        return false;
    }

    public void setflags(String flags) {
        this.flags = flags.toCharArray();
        DB.executeUpdateAsync(
                "UPDATE `md_players` SET `flags` = "
                        + Database.sqlString(flags)
                        + " WHERE `uuid` = "
                        + uuid
                        + ";");
    }

    public void addIgnore(UUID playerUUID) {
        if (!ignores.contains(playerUUID)) {
            ignores.add(playerUUID);
            DB.executeUpdateAsync("UPDATE `md_players` SET `ignores` = " + Database.sqlString(Utilities.uuidListToString(ignores)) + " WHERE `uuid` = " + Database.sqlString(uuid.toString()));
        }
    }

    public void removeIgnore(UUID playerUUID) {
        if (ignores.contains(playerUUID)) {
            ignores.remove(playerUUID);
            DB.executeUpdateAsync("UPDATE `md_players` SET `ignores` = " + Database.sqlString(Utilities.uuidListToString(ignores)) + " WHERE `uuid` = " + Database.sqlString(uuid.toString()));
        }
    }
}
