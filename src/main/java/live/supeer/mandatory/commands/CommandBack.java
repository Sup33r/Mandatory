package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.TeleportUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

@CommandAlias("back")
public class CommandBack extends BaseCommand {
    private final HashMap<Player, Location> playerLocation = new HashMap<>();
    public static Mandatory plugin;

    @Default
    public void onBack(Player player) {
        if (!player.hasPermission("mandatory.command.back")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (!plugin.backLocations.containsKey(player)) {
            plugin.sendMessage(player, "messages.error.noBackLocation");
            return;
        }
        playerLocation.put(player, player.getLocation());
        TeleportUtil.Teleport(player, plugin.backLocations.get(player), plugin.getMessage("messages.names.back"), false);
        plugin.backLocations.replace(player, playerLocation.get(player));
        playerLocation.remove(player);
    }
}
