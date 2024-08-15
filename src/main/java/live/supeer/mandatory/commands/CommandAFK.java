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

import java.util.ArrayList;

@CommandAlias("afk")
public class CommandAFK extends BaseCommand {
    public static Mandatory plugin;

    @Default
    @CommandCompletion("@players")
    public static void onAFK(Player player, @Optional OnlinePlayer target) {
        Location spawnLocation = Mandatory.configuration.getSpawnLocation();
        if (spawnLocation == null) {
            plugin.sendMessage(player, "messages.error.noAFKSet");
            return;
        }
        if (target == null) {
            if (!player.hasPermission("mandatory.command.afk")) {
                plugin.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (plugin.afkPlayers.containsKey(player)) {
                plugin.sendMessage(player, "messages.afk.alreadyAfk");
            }
            plugin.afkPlayers.put(player, new ArrayList<>());
            TeleportUtil.teleport(player, spawnLocation, Mandatory.getMessage("messages.names.afk"), false);
        } else {
            if (!player.hasPermission("mandatory.command.afk.others")) {
                plugin.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (plugin.afkPlayers.containsKey(target.getPlayer())) {
                plugin.sendMessage(player, "messages.afk.alreadyAfkOther", "%player%", target.getPlayer().getName());
            }
            plugin.afkPlayers.put(target.getPlayer(), new ArrayList<>());
            TeleportUtil.teleport(target.getPlayer(), spawnLocation, Mandatory.getMessage("messages.names.afk"), true);
            plugin.sendMessage(player, "messages.successful.afk", "%player%", target.getPlayer().getName());
        }
    }
}
