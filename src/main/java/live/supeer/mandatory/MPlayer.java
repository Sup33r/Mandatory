package live.supeer.mandatory;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import live.supeer.mandatory.utils.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class MPlayer {
    private UUID uuid;
    private String name;
    private long firstJoin;
    private long latestJoin;
    private long latestLeave;
    private String membership;
    private int balance;
    private Location lastLocation;

    public MPlayer(DbRow data) {
        this.uuid = UUID.fromString(data.getString("uuid"));
        this.name = data.getString("name");
        this.firstJoin = data.getLong("firstJoin");
        this.latestJoin = data.getLong("latestJoin");
        this.latestLeave = data.getLong("latestLeave");
        this.membership = data.getString("membership");
        this.balance = data.getInt("balance");
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
}
