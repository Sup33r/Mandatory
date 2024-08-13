package live.supeer.mandatory;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import live.supeer.mandatory.utils.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class Player {
    private UUID playerUUID;
    private String playerName;
    private long firstJoin;
    private long latestJoin;
    private long latestLeave;
    private String membership;
    private int playerBalance;
    private Location lastLocation;

    public Player(DbRow data) {
        this.playerUUID = UUID.fromString(data.getString("playerUUID"));
        this.playerName = data.getString("playerName");
        this.firstJoin = data.getLong("firstJoin");
        this.latestJoin = data.getLong("latestJoin");
        this.latestLeave = data.getLong("latestLeave");
        this.membership = data.getString("membership");
        this.playerBalance = data.getInt("playerBalance");
        this.lastLocation = null;
    }

    public void setLastLocation(Location lastLocation) {
        try {
            this.lastLocation = lastLocation;
            DB.executeUpdate("UPDATE `md_players` SET `position` = " + Database.sqlString(LocationUtil.locationToString(lastLocation)) + " WHERE `playerUUID` = " + Database.sqlString(playerUUID.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
