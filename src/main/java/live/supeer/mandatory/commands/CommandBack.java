package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.TeleportUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

@CommandAlias("back")
public class CommandBack extends BaseCommand {
    private final HashMap<Player, Location> playerLocation = new HashMap<>();

    @Default
    @CommandCompletion("@players")
    public void onBack(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            if (!player.hasPermission("mandatory.command.back")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (!Mandatory.backLocations.containsKey(player)) {
                Mandatory.sendMessage(player, "messages.error.noBackLocation");
                return;
            }
            playerLocation.put(player, player.getLocation());
            TeleportUtil.teleport(player, Mandatory.backLocations.get(player), Mandatory.getMessage("messages.names.back"), false);
            Mandatory.backLocations.replace(player, playerLocation.get(player));
            playerLocation.remove(player);
        } else {
            if (!player.hasPermission("mandatory.command.back.others")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (!Mandatory.backLocations.containsKey(target.getPlayer())) {
                Mandatory.sendMessage(player, "messages.error.noBackLocationOther", "%player%", target.getPlayer().getName());
                return;
            }
            playerLocation.put(target.getPlayer(), target.getPlayer().getLocation());
            TeleportUtil.teleport(target.getPlayer(), Mandatory.backLocations.get(target.getPlayer()), Mandatory.getMessage("messages.names.back"), true);
            Mandatory.backLocations.replace(target.getPlayer(), playerLocation.get(target.getPlayer()));
            playerLocation.remove(target.getPlayer());
            Mandatory.sendMessage(player, "messages.teleport.backOther", "%player%", target.getPlayer().getName());
        }
    }
}
