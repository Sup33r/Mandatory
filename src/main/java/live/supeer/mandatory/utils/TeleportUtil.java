package live.supeer.mandatory.utils;

import live.supeer.apied.ApiedAPI;
import live.supeer.apied.MPlayer;
import live.supeer.mandatory.*;
import live.supeer.metropolis.Metropolis;
import live.supeer.metropolis.MetropolisAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static live.supeer.metropolis.utils.TeleportUtil.teleportingPlayers;
import static live.supeer.metropolis.utils.TeleportUtil.teleportingTasks;
import static live.supeer.metropolis.utils.TeleportUtil.startingLocations;


public class TeleportUtil {
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
            Metropolis.backLocations.put(player, player.getLocation());
            player.teleport(afkLocation);
            Metropolis.afkPlayers.put(player.getUniqueId(), new ArrayList<>());
            Objects.requireNonNull(player.getPlayer()).playerListName(ApiedAPI.getTABPrefix(player.getPlayer(), true));
            Mandatory.sendMessage(player, "messages.teleport.success","%name%", Mandatory.getMessage("messages.names.afk"));
            return;
        }
        if (teleportingPlayers.contains(player)) {
            Mandatory.sendMessage(player, "messages.teleport.hasTeleport");
            return;
        }
        teleportingPlayers.add(player);
        int cooldown = Metropolis.configuration.getTeleportCooldown();
        Mandatory.sendMessage(player, "messages.teleport.started", "%cooldown%", String.valueOf(cooldown));
        BukkitTask task = Mandatory.getInstance().getServer().getScheduler().runTaskLater(Mandatory.getInstance(), () -> {
            if (!teleportingPlayers.contains(player)) {
                sendAfkMessages(player);
                Mandatory.sendMessage(player, "messages.teleport.failed");
                return;
            }
            sendAfkMessages(player);
            Metropolis.backLocations.put(player, player.getLocation());
            player.teleport(afkLocation);
            Metropolis.afkPlayers.put(player.getUniqueId(), new ArrayList<>());
            Objects.requireNonNull(player.getPlayer()).playerListName(ApiedAPI.getTABPrefix(player.getPlayer(), true));
            Mandatory.sendMessage(player, "messages.teleport.success", "%name%", Mandatory.getMessage("messages.names.afk"));
            teleportingPlayers.remove(player);
            teleportingTasks.remove(player);
            startingLocations.remove(player);
        }, 20L * cooldown);
        teleportingTasks.put(player, task);
        startingLocations.put(player, player.getLocation());
    }

    public static void teleportGo(Player player, Go go, boolean bypassCooldown, boolean allowNature) {
        if (!player.hasPermission("mandatory.command.go.use")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (go == null) {
            Mandatory.sendMessage(player, "messages.go.notFound");
            return;
        }
        if (!MetropolisAPI.isInCity(go.getLocation().toBlockLocation()) && !allowNature) {
            Mandatory.sendMessage(player, "messages.go.notInCity", "%name%", go.getName());
            return;
        }
        if (!go.isOpen() && player.hasPermission("mandatory.go.override") || !go.isOpen() && go.isWhitelisted(player.getUniqueId()) || !go.isOpen() && go.getOwnerUUID().equals(player.getUniqueId()) || go.isOpen()) {
            if (go.getChargeAmount() > 0) {
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
                teleportMethod(player, go, goName);
                return;
            }
            if (teleportingPlayers.contains(player)) {
                Mandatory.sendMessage(player, "messages.teleport.hasTeleport");
                return;
            }
            teleportingPlayers.add(player);
            int cooldown = Metropolis.configuration.getTeleportCooldown();
            BukkitTask task = Mandatory.getInstance().getServer().getScheduler().runTaskLater(Mandatory.getInstance(), () -> {
                if (!teleportingPlayers.contains(player)) {
                    sendAfkMessages(player);
                    Mandatory.sendMessage(player, "messages.teleport.failed");
                    return;
                }
                teleportMethod(player, go, goName);
                teleportingPlayers.remove(player);
                teleportingTasks.remove(player);
                startingLocations.remove(player);
            }, 20L * cooldown);
            teleportingTasks.put(player, task);
            startingLocations.put(player, player.getLocation());
        } else {
            Mandatory.sendMessage(player, "messages.go.notOpen");
        }
    }

    private static void teleportMethod(Player player, Go go, String goName) {
        sendAfkMessages(player);
        Metropolis.backLocations.put(player, player.getLocation());
        player.teleport(go.getLocation());
        Mandatory.sendMessage(player, "messages.teleport.success", "%name%", goName);
        GoManager.logGoUsage(player.getUniqueId(), go);
        if (go.getChargeAmount() > 0) {
            if (go.isWhitelistcharge() || !go.isWhitelisted(player.getUniqueId())) {
                Mandatory.promptedPayment.remove(player);
                Mandatory.sendMessage(player, "messages.go.charged", "%charge%", String.valueOf(go.getChargeAmount()));
                MPlayer mPlayer = ApiedAPI.getPlayer(player);
                if (mPlayer != null) {
                    mPlayer.removeBalance(go.getChargeAmount(), "{ \"type\": \"go\", \"subtype\": \"teleport\", \"goName\": \"" + go.getName() + "}");
                }
            }
        }
    }

    public static void sendAfkMessages(Player player) {
        if (Metropolis.afkPlayers.containsKey(player.getUniqueId())) {
            List<Component> messages = Metropolis.afkPlayers.get(player.getUniqueId());
            for (Component message : messages) {
                player.sendMessage(message);
            }
            Objects.requireNonNull(player.getPlayer()).playerListName(ApiedAPI.getTABPrefix(player.getPlayer(), false));
            Metropolis.afkPlayers.remove(player.getUniqueId());
        }
    }
}
