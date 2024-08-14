package live.supeer.mandatory.utils;

import live.supeer.mandatory.Go;
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

    public static void teleportGo(Player player, Go go, boolean bypassCooldown, boolean allowNature) {
        if (!player.hasPermission("mandatory.command.go.use")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (go == null) {
            plugin.sendMessage(player, "messages.go.notFound");
            return;
        }
        if (!go.isOpen() && !player.hasPermission("mandatory.go.override") || !go.isOpen() && !go.isWhitelisted(player.getUniqueId())) {
            plugin.sendMessage(player, "messages.go.notOpen");
            return;
        }
        //if (!allowNature)
        if (go.getChargeAmount() > 0) {
            //den kostar
            if (go.isWhitelistcharge() || !go.isWhitelisted(player.getUniqueId())) {
                if (!plugin.promptedPayment.containsKey(player)) {
                    plugin.promptedPayment.put(player, go);
                    plugin.sendMessage(player, "messages.go.charges", "%charge%", String.valueOf(go.getChargeAmount()));
                    return;
                }
            }
        }
        String goName = go.getDisplayname();
        if (player.isInsideVehicle()) {
            plugin.sendMessage(player, "messages.error.teleport.failed");
            return;
        }
        if (player.hasPermission("mandatory.teleport.nowait") || bypassCooldown) {
            sendAfkMessages(player);
            player.teleport(go.getLocation());
            plugin.backLocations.put(player, player.getLocation());
            plugin.sendMessage(player, "messages.teleport.success", goName);
            if (go.getChargeAmount() > 0) {
                if (go.isWhitelistcharge() || !go.isWhitelisted(player.getUniqueId())) {
                    plugin.promptedPayment.remove(player);
                    plugin.sendMessage(player, "messages.go.charged", "%charge%", String.valueOf(go.getChargeAmount()));
                    // TAKE PAYMENT
                }
            }
            return;
        }
        if (teleportingPlayers.contains(player)) {
            plugin.sendMessage(player, "messages.teleport.hasTeleport");
            return;
        }
        teleportingPlayers.add(player);
        int cooldown = Mandatory.configuration.getTeleportCooldown();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!teleportingPlayers.contains(player)) {
                sendAfkMessages(player);
                plugin.sendMessage(player, "messages.teleport.failed");
                return;
            }
            sendAfkMessages(player);
            player.teleport(go.getLocation());
            plugin.backLocations.put(player, player.getLocation());
            plugin.sendMessage(player, "messages.teleport.success", goName);
            if (go.getChargeAmount() > 0) {
                if (go.isWhitelistcharge() || !go.isWhitelisted(player.getUniqueId())) {
                    plugin.promptedPayment.remove(player);
                    plugin.sendMessage(player, "messages.go.charged", "%charge%", String.valueOf(go.getChargeAmount()));
                    // TAKE PAYMENT
                }
            }
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
