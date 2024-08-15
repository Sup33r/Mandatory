package live.supeer.mandatory;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import live.supeer.mandatory.utils.LocationUtil;
import live.supeer.mandatory.utils.Utilities;
import lombok.Getter;
import org.bukkit.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Go {
    private String name;
    private String displayName;
    private UUID ownerUUID;
    private final long createDate;
    private final List<UUID> whitelistPlayers;
    private Location location;
    private int chargeAmount;
    private boolean open;
    private boolean whitelistcharge;

    public Go(DbRow data) {
        this.name = data.getString("goName");
        this.displayName = data.getString("displayName");
        this.ownerUUID = UUID.fromString(data.getString("ownerUUID"));
        this.createDate = data.getLong("createDate");
        String whiteListPlayersString = data.getString("whitelistPlayers");
        this.whitelistPlayers = new ArrayList<>(Utilities.stringToUUIDList(whiteListPlayersString));
        this.location = LocationUtil.stringToLocation(data.getString("location"));
        this.chargeAmount = data.getInt("chargeAmount");
        this.open = data.get("open");
        this.whitelistcharge = data.get("whitelistcharge");
    }

    public void addToWhitelist(UUID playerUUID) {
        if (!whitelistPlayers.contains(playerUUID)) {
            whitelistPlayers.add(playerUUID);
            try {
                DB.executeUpdate("UPDATE `md_goes` SET `whitelistPlayers` = ? WHERE `goName` = ?", Utilities.uuidListToString(whitelistPlayers), name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removeFromWhitelist(UUID playerUUID) {
        whitelistPlayers.remove(playerUUID);
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `whitelistPlayers` = ? WHERE `goName` = ?", Utilities.uuidListToString(whitelistPlayers), name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isWhitelisted(UUID playerUUID) {
        return whitelistPlayers.contains(playerUUID);
    }

    public String getDisplayname() {
        if (displayName == null) {
            return name;
        }
        return displayName;
    }

    public void setChargeAmount(int chargeAmount) {
        this.chargeAmount = chargeAmount;
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `chargeAmount` = ? WHERE `goName` = ?", chargeAmount, name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOpen(boolean open) {
        this.open = open;
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `open` = ? WHERE `goName` = ?", open, name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setWhitelistcharge(boolean whitelistcharge) {
        this.whitelistcharge = whitelistcharge;
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `whitelistcharge` = ? WHERE `goName` = ?", whitelistcharge, name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLocation(Location location) {
        this.location = location;
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `location` = ? WHERE `goName` = ?", LocationUtil.locationToString(location), name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `displayName` = ? WHERE `goName` = ?", displayName, name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(String name) {
        this.name = name;
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `goName` = ? WHERE `goName` = ?", name, this.name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        try {
            DB.executeUpdate("UPDATE `md_goes` SET `ownerUUID` = ? WHERE `goName` = ?", ownerUUID.toString(), name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
