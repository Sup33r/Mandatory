package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.MPlayer;
import live.supeer.mandatory.Mandatory;
import live.supeer.metropolis.Metropolis;
import live.supeer.metropolis.MetropolisAPI;
import org.bukkit.entity.Player;

@CommandAlias("teleportask|tpask|tpa")
public class CommandTeleportAsk extends BaseCommand {

    @Default
    public void onTeleportAsk(Player player, OnlinePlayer target) {
        if (!player.hasPermission("mandatory.command.teleportask")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (target == null) {
            Mandatory.sendMessage(player, "messages.error.playerNotFound");
            return;
        }
        if (target.getPlayer() == player) {
            Mandatory.sendMessage(player, "messages.teleportRequests.self");
            return;
        }
        if (!MetropolisAPI.playerHasLocationPermissionFlags(player.getUniqueId(), target.getPlayer().getLocation().toBlockLocation(), 't') && !Metropolis.overrides.contains(player)) {
            Mandatory.sendMessage(player, "messages.teleportRequests.cityNotAllowed", "%target%", target.getPlayer().getName());
            return;
        }
        MPlayer mPlayerTarget = ApiedAPI.getPlayer(target.getPlayer());
        if (mPlayerTarget != null && mPlayerTarget.hasFlag('t')) {
            Mandatory.sendMessage(player, "messages.teleportRequests.teleportsNotAllowed", "%target%", target.getPlayer().getName());
            return;
        }
        if (Mandatory.teleportRequests.containsKey(player)) {
            Player previousTarget = Mandatory.teleportRequests.get(player);
            Mandatory.sendMessage(player,"messages.teleportRequests.cancelledSelf","%target%", previousTarget.getName());
            Mandatory.sendMessage(previousTarget,"messages.teleportRequests.cancelledOther","%player%", player.getName());
            Mandatory.teleportRequests.remove(player);
            return;
        }
        Mandatory.teleportRequests.put(player, target.getPlayer());
        Mandatory.sendMessage(player, "messages.teleportRequests.sentSelf", "%target%", target.getPlayer().getName());
        Mandatory.sendMessage(target.getPlayer(), "messages.teleportRequests.sentOther", "%player%", player.getName());
    }
}
