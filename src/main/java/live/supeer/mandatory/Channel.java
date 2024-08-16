package live.supeer.mandatory;

import co.aikar.idb.DbRow;
import lombok.Getter;

@Getter
public class Channel {
    private final String name;
    private final ChannelType type;

    public Channel(DbRow data) {
        this.name = data.getString("channelName");
        this.type = ChannelType.getByName(data.getString("channelType"));
    }
}
