package live.supeer.mandatory;

import co.aikar.idb.DbRow;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Ban {
    private UUID playerUUID;
    private UUID placerUUID;
    private String Ip;
    private long placeTime;
    private long length;
    private String reason;

    public Ban(DbRow data) {
        this.playerUUID = UUID.fromString(data.getString("playerUUID"));
        this.placerUUID = UUID.fromString(data.getString("placerUUID"));
        this.Ip = data.getString("playerIp");
        this.placeTime = data.getLong("placeTime");
        this.length = data.getLong("length");
        this.reason = data.getString("reason");
    }
}
