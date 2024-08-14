package live.supeer.mandatory;

import co.aikar.idb.DB;
import live.supeer.mandatory.utils.DateUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class InfractionManager {
    public static Mandatory plugin;

    public static void kickPlayer(Player player, Player placer, String reason) {
        Component component = plugin.getMessageComponent("messages.kick.message", "%player%", player.getName(), "%reason%", reason);
        player.kick(component);
        try {
            DB.executeUpdate("INSERT INTO md_kicks (playerUUID, placerUUID, reason, dateTime) VALUES (?, ?, ?, ?)", player.getUniqueId().toString(), placer.getUniqueId().toString(), Database.sqlString(reason), DateUtil.getTimestamp());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
