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

    @Default
    @CommandCompletion("@players")
    public static void onAFK(Player player, @Optional OnlinePlayer target) {
        Location spawnLocation = Mandatory.configuration.getSpawnLocation();
        if (spawnLocation == null) {
            Mandatory.sendMessage(player, "messages.error.noAFKSet");
            return;
        }
        if (target == null) {
            if (!player.hasPermission("mandatory.command.afk")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (Mandatory.afkPlayers.containsKey(player)) {
                Mandatory.sendMessage(player, "messages.afk.alreadyAfk");
            }
            Mandatory.afkPlayers.put(player, new ArrayList<>());
            TeleportUtil.teleport(player, spawnLocation, Mandatory.getMessage("messages.names.afk"), false);
        } else {
            if (!player.hasPermission("mandatory.command.afk.others")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (Mandatory.afkPlayers.containsKey(target.getPlayer())) {
                Mandatory.sendMessage(player, "messages.afk.alreadyAfkOther", "%player%", target.getPlayer().getName());
            }
            Mandatory.afkPlayers.put(target.getPlayer(), new ArrayList<>());
            TeleportUtil.teleport(target.getPlayer(), spawnLocation, Mandatory.getMessage("messages.names.afk"), true);
            Mandatory.sendMessage(player, "messages.successful.afk", "%player%", target.getPlayer().getName());
        }
    }
}
