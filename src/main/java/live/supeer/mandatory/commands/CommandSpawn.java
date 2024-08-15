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

@CommandAlias("spawn|s")
public class CommandSpawn extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public static void onSpawn(Player player, @Optional OnlinePlayer target) {
        Location spawnLocation = Mandatory.configuration.getSpawnLocation();
        if (target == null) {
            if (!player.hasPermission("mandatory.command.spawn")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }

            if (spawnLocation == null) {
                Mandatory.sendMessage(player, "messages.error.noSpawnSet");
                return;
            }
            TeleportUtil.teleport(player, spawnLocation, Mandatory.getMessage("messages.names.spawn"), false);
        } else {
            if (!player.hasPermission("mandatory.command.spawn.others")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (spawnLocation == null) {
                Mandatory.sendMessage(player, "messages.error.noSpawnSet");
                return;
            }
            TeleportUtil.teleport(target.getPlayer(), spawnLocation, Mandatory.getMessage("messages.names.spawn"), true);
            Mandatory.sendMessage(player, "messages.successful.spawn", "%player%", target.getPlayer().getName());
        }
    }
}
