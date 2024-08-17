package live.supeer.mandatory;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import lombok.Getter;

import java.sql.SQLException;
import java.util.*;

@Getter
public class Channel {
    private final String name;
    private final ChannelType type;
    private final Set<UUID> members;

    public Channel(DbRow data) {
        this.name = data.getString("channelName");
        this.type = ChannelType.getByName(data.getString("channelType"));
        this.members = new HashSet<>();
        try {
            List<DbRow> rows = DB.getResults("SELECT playerUUID FROM md_channelmembers WHERE channelName = ?", name);
            for (DbRow row : rows) {
                this.members.add(UUID.fromString(row.getString("playerUUID")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasMember(UUID playerUUID) {
        return members.contains(playerUUID);
    }

    public Set<UUID> getMembers() {
        return new HashSet<>(members);
    }

    public void addMember(UUID playerUUID) {
        if (!Mandatory.playerChannels.containsKey(playerUUID)) {
            Mandatory.playerChannels.put(playerUUID, new ArrayList<>());
        }
        members.add(playerUUID);
        Mandatory.playerChannels.get(playerUUID).add(this);
        DB.executeUpdateAsync("INSERT INTO md_channelmembers (channelName, playerUUID) VALUES (?, ?)", name, playerUUID.toString());
    }

    public void removeMember(UUID playerUUID) {
        members.remove(playerUUID);
        Mandatory.playerChannels.get(playerUUID).remove(this);
        DB.executeUpdateAsync("DELETE FROM md_channelmembers WHERE channelName = ? AND playerUUID = ?", name, playerUUID.toString());
    }

}
