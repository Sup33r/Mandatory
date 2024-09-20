package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import live.supeer.metropolis.MetropolisAPI;
import live.supeer.metropolis.acf.annotation.CommandPermission;
import live.supeer.metropolis.utils.TeleportUtil;
import live.supeer.metropolis.Metropolis;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

@CommandAlias("back")
public class CommandBack extends BaseCommand {
    private final HashMap<Player, Location> playerLocation = new HashMap<>();

    @Default
    @CommandCompletion("@players")
    @CommandPermission("mandatory.command.back")
    public void onBack(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            if (!Metropolis.backLocations.containsKey(player)) {
                Mandatory.sendMessage(player, "messages.error.noBackLocation");
                return;
            }
            if (!MetropolisAPI.playerHasLocationPermissionFlags(player.getUniqueId(), Metropolis.backLocations.get(player), 't') && !Metropolis.overrides.contains(player)) {
                Mandatory.sendMessage(player, "messages.teleport.cityNotAllowed");
                return;
            }
            playerLocation.put(player, player.getLocation());
            TeleportUtil.teleport(player, Metropolis.backLocations.get(player), Mandatory.getMessage("messages.names.back"), false);
            Metropolis.backLocations.replace(player, playerLocation.get(player));
            playerLocation.remove(player);
        } else {
            if (!player.hasPermission("mandatory.command.back.others")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (!Metropolis.backLocations.containsKey(target.getPlayer())) {
                Mandatory.sendMessage(player, "messages.error.noBackLocationOther", "%player%", target.getPlayer().getName());
                return;
            }
            playerLocation.put(target.getPlayer(), target.getPlayer().getLocation());
            TeleportUtil.teleport(target.getPlayer(), Metropolis.backLocations.get(target.getPlayer()), Mandatory.getMessage("messages.names.back"), true);
            Metropolis.backLocations.replace(target.getPlayer(), playerLocation.get(target.getPlayer()));
            playerLocation.remove(target.getPlayer());
            Mandatory.sendMessage(player, "messages.teleport.backOther", "%player%", target.getPlayer().getName());
        }
    }
}
