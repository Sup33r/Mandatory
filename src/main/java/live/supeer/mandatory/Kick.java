package live.supeer.mandatory;

import co.aikar.idb.DbRow;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Kick {
    private final UUID playerUUID;
    private final UUID placerUUID;
    private final String reason;
    private final long dateTime;

    public Kick(DbRow data) {
        this.playerUUID = UUID.fromString(data.getString("playerUUID"));
        this.placerUUID = UUID.fromString(data.getString("placerUUID"));
        this.reason = data.getString("reason");
        this.dateTime = data.getLong("dateTime");
    }
}
