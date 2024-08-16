package live.supeer.mandatory.utils;

import live.supeer.mandatory.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportUtil {
    public static List<Player> teleportingPlayers = new ArrayList<>();
    

    public static void teleport(Player player, Location location, String name, boolean bypassCooldown) {
        if (player.isInsideVehicle()) {
            Mandatory.sendMessage(player, "messages.error.teleport.failed");
            return;
        }
        if (player.hasPermission("mandatory.teleport.nowait") || bypassCooldown) {
            Mandatory.backLocations.put(player, player.getLocation());
            player.teleport(location);
            sendAfkMessages(player);
            Mandatory.sendMessage(player, "messages.teleport.success","%name%", name);
            return;
        }
        if (teleportingPlayers.contains(player)) {
            Mandatory.sendMessage(player, "messages.teleport.hasTeleport");
            return;
        }
        teleportingPlayers.add(player);
        int cooldown = Mandatory.configuration.getTeleportCooldown();
        Mandatory.sendMessage(player, "messages.teleport.started", "%cooldown%", String.valueOf(cooldown));
        Mandatory.getInstance().getServer().getScheduler().runTaskLater(Mandatory.getInstance(), () -> {
            if (!teleportingPlayers.contains(player)) {
                sendAfkMessages(player);
                Mandatory.sendMessage(player, "messages.teleport.failed");
                return;
            }
            sendAfkMessages(player);
            Mandatory.backLocations.put(player, player.getLocation());
            player.teleport(location);
            Mandatory.sendMessage(player, "messages.teleport.success", "%name%", name);
            teleportingPlayers.remove(player);
        }, 20L * cooldown);
    }

    public static void teleportAfk(Player player, boolean bypassCooldown) {
        if (player.isInsideVehicle()) {
            Mandatory.sendMessage(player, "messages.error.teleport.failed");
            return;
        }
        Location afkLocation = Mandatory.configuration.getAfkLocation();
        if (afkLocation == null) {
            Mandatory.sendMessage(player, "messages.error.noAFKSet");
            return;
        }
        if (player.hasPermission("mandatory.teleport.nowait") || bypassCooldown) {
            Mandatory.backLocations.put(player, player.getLocation());
            player.teleport(afkLocation);
            Mandatory.afkPlayers.put(player.getUniqueId(), new ArrayList<>());
            Mandatory.sendMessage(player, "messages.teleport.success","%name%", Mandatory.getMessage("messages.names.afk"));
            return;
        }
        if (teleportingPlayers.contains(player)) {
            Mandatory.sendMessage(player, "messages.teleport.hasTeleport");
            return;
        }
        teleportingPlayers.add(player);
        int cooldown = Mandatory.configuration.getTeleportCooldown();
        Mandatory.sendMessage(player, "messages.teleport.started", "%cooldown%", String.valueOf(cooldown));
        Mandatory.getInstance().getServer().getScheduler().runTaskLater(Mandatory.getInstance(), () -> {
            if (!teleportingPlayers.contains(player)) {
                sendAfkMessages(player);
                Mandatory.sendMessage(player, "messages.teleport.failed");
                return;
            }
            sendAfkMessages(player);
            Mandatory.backLocations.put(player, player.getLocation());
            player.teleport(afkLocation);
            Mandatory.afkPlayers.put(player.getUniqueId(), new ArrayList<>());
            Mandatory.sendMessage(player, "messages.teleport.success", "%name%", Mandatory.getMessage("messages.names.afk"));
            teleportingPlayers.remove(player);
        }, 20L * cooldown);
    }

    public static void teleportGo(Player player, Go go, boolean bypassCooldown, boolean allowNature) {
        if (!player.hasPermission("mandatory.command.go.use")) {
            Mandatory.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (go == null) {
            Mandatory.sendMessage(player, "messages.go.notFound");
            return;
        }
        if (!go.isOpen() && !player.hasPermission("mandatory.go.override") || !go.isOpen() && !go.isWhitelisted(player.getUniqueId())) {
            Mandatory.sendMessage(player, "messages.go.notOpen");
            return;
        }
        //if (!allowNature)
        if (go.getChargeAmount() > 0) {
            //den kostar
            if (go.isWhitelistcharge() || !go.isWhitelisted(player.getUniqueId())) {
                if (!Mandatory.promptedPayment.containsKey(player)) {
                    Mandatory.promptedPayment.put(player, go);
                    Mandatory.sendMessage(player, "messages.go.charges", "%charge%", String.valueOf(go.getChargeAmount()));
                    return;
                }
            }
        }
        String goName = go.getDisplayname();
        if (player.isInsideVehicle()) {
            Mandatory.sendMessage(player, "messages.error.teleport.failed");
            return;
        }
        if (player.hasPermission("mandatory.teleport.nowait") || bypassCooldown) {
            sendAfkMessages(player);
            Mandatory.backLocations.put(player, player.getLocation());
            player.teleport(go.getLocation());
            Mandatory.sendMessage(player, "messages.teleport.success", "%name%", goName);
            GoManager.logGoUsage(player.getUniqueId(),go);
            if (go.getChargeAmount() > 0) {
                if (go.isWhitelistcharge() || !go.isWhitelisted(player.getUniqueId())) {
                    Mandatory.promptedPayment.remove(player);
                    Mandatory.sendMessage(player, "messages.go.charged", "%charge%", String.valueOf(go.getChargeAmount()));
                    MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
                    if (mPlayer != null) {
                        mPlayer.removeBalance(go.getChargeAmount(),"{ \"type\": \"go\", \"subtype\": \"teleport\", \"goName\": \"" + go.getName() + "}");
                    }
                }
            }
            return;
        }
        if (teleportingPlayers.contains(player)) {
            Mandatory.sendMessage(player, "messages.teleport.hasTeleport");
            return;
        }
        teleportingPlayers.add(player);
        int cooldown = Mandatory.configuration.getTeleportCooldown();
        Mandatory.getInstance().getServer().getScheduler().runTaskLater(Mandatory.getInstance(), () -> {
            if (!teleportingPlayers.contains(player)) {
                sendAfkMessages(player);
                Mandatory.sendMessage(player, "messages.teleport.failed");
                return;
            }
            sendAfkMessages(player);
            Mandatory.backLocations.put(player, player.getLocation());
            player.teleport(go.getLocation());
            Mandatory.sendMessage(player, "messages.teleport.success", "%name%", goName);
            GoManager.logGoUsage(player.getUniqueId(),go);
            if (go.getChargeAmount() > 0) {
                if (go.isWhitelistcharge() || !go.isWhitelisted(player.getUniqueId())) {
                    Mandatory.promptedPayment.remove(player);
                    Mandatory.sendMessage(player, "messages.go.charged", "%charge%", String.valueOf(go.getChargeAmount()));
                    MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
                    if (mPlayer != null) {
                        mPlayer.removeBalance(go.getChargeAmount(),"{ \"type\": \"go\", \"subtype\": \"teleport\", \"goName\": \"" + go.getName() + "}");
                    }
                }
            }
            teleportingPlayers.remove(player);
        }, 20L * cooldown);
    }

    public static void sendAfkMessages(Player player) {
        if (Mandatory.afkPlayers.containsKey(player.getUniqueId())) {
            List<Component> messages = Mandatory.afkPlayers.get(player.getUniqueId());
            for (Component message : messages) {
                player.sendMessage(message);
            }
            Mandatory.afkPlayers.remove(player.getUniqueId());
        }
    }
}
