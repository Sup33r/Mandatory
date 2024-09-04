package live.supeer.mandatory;

import co.aikar.idb.DB;
import live.supeer.mandatory.utils.DateUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class InfractionManager {

    public static void kickPlayer(Player player, Player placer, String reason) {
        Component component = Mandatory.getMessageComponent("messages.kick.message", "%player%", player.getName(), "%reason%", reason);
        Mandatory.kickedPlayers.add(player);
        player.kick(component);
        try {
            DB.executeUpdate("INSERT INTO md_kicks (playerUUID, placerUUID, reason, dateTime) VALUES (?, ?, ?, ?)", player.getUniqueId().toString(), placer.getUniqueId().toString(), reason, DateUtil.getTimestamp());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
