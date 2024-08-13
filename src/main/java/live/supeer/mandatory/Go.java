package live.supeer.mandatory;

import co.aikar.idb.DbRow;
import live.supeer.mandatory.utils.LocationUtil;
import live.supeer.mandatory.utils.Utilities;
import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Go {
    private String name;
    private String displayName;
    private UUID ownerUUID;
    private long createDate;
    private List<UUID> whitelistPlayers;
    private Location position;
    private boolean charge;
    private boolean open;
    private boolean whitelistcharge;

    public Go(DbRow data) {
        this.name = data.getString("goName");
        this.displayName = data.getString("displayName");
        this.ownerUUID = UUID.fromString(data.getString("ownerUUID"));
        this.createDate = data.getLong("createDate");
        String whiteListPlayersString = data.getString("whitelistPlayers");
        this.whitelistPlayers = new ArrayList<>(Utilities.stringToUUIDList(whiteListPlayersString));
        this.position = LocationUtil.stringToLocation(data.getString("position"));
        this.charge = data.get("charge");
        this.open = data.get("open");
        this.whitelistcharge = data.get("whitelistcharge");
    }
}
