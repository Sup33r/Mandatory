package live.supeer.mandatory;

import co.aikar.idb.DB;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import live.supeer.mandatory.utils.DateUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class InfractionManager {
    public static void kickPlayer(Player player, Player placer, String reason) {
        Mandatory.kickedPlayers.add(player);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("kick");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(reason);
        player.sendPluginMessage(Mandatory.getInstance(), "proxie:proxie", out.toByteArray());
        try {
            DB.executeUpdate("INSERT INTO md_kicks (playerUUID, placerUUID, reason, dateTime) VALUES (?, ?, ?, ?)", player.getUniqueId().toString(), placer.getUniqueId().toString(), reason, DateUtil.getTimestamp());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
