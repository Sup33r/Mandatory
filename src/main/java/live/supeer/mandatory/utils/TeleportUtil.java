package live.supeer.mandatory.utils;

import live.supeer.mandatory.Mandatory;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportUtil {
    public static Mandatory plugin;
    public static List<Player> teleportingPlayers = new ArrayList<Player>();

    public TeleportUtil(Mandatory plugin) {
        TeleportUtil.plugin = plugin;
    }

    public static void teleport(Player player, Location location, String name, boolean bypassCooldown) {
        if (player.isInsideVehicle()) {
            plugin.sendMessage(player, "messages.error.teleport.failed");
            return;
        }
        if (player.hasPermission("mandatory.teleport.nowait") || bypassCooldown) {
            player.teleport(location);
            plugin.sendMessage(player, "messages.teleport.success", name);
            return;
        }
        if (teleportingPlayers.contains(player)) {
            plugin.sendMessage(player, "messages.teleport.hasTeleport");
            return;
        }
        teleportingPlayers.add(player);
        int cooldown = Mandatory.configuration.getTeleportCooldown();
        plugin.sendMessage(player, "messages.teleport.started", "%cooldown%", String.valueOf(cooldown));
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!teleportingPlayers.contains(player)) {
                sendAfkMessages(player);
                plugin.sendMessage(player, "messages.teleport.failed");
                return;
            }
            sendAfkMessages(player);
            player.teleport(location);
            plugin.backLocations.put(player, player.getLocation());
            plugin.sendMessage(player, "messages.teleport.success", name);
            teleportingPlayers.remove(player);
        }, 20L * cooldown);
    }

    public static void sendAfkMessages(Player player) {
        if (plugin.afkPlayers.containsKey(player)) {
            List<Component> messages = plugin.afkPlayers.get(player);
            for (Component message : messages) {
                player.sendMessage(message);
            }
            plugin.afkPlayers.remove(player);
        }
    }
}
