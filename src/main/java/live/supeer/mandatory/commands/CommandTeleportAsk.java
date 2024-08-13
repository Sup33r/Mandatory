package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("teleportask|tpask|tpa")
public class CommandTeleportAsk extends BaseCommand {
    public static Mandatory plugin;

    @Default
    public void onTeleportAsk(Player player, OnlinePlayer target) {
        if (!player.hasPermission("mandatory.command.teleportask")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (target == null) {
            plugin.sendMessage(player, "messages.error.playerNotFound");
            return;
        }
        if (target.getPlayer() == player) {
            plugin.sendMessage(player, "messages.teleport.teleportSelf");
            return;
        }
        plugin.teleportRequests.put(target.getPlayer(), player);
        plugin.sendMessage(player, "messages.teleport.teleportRequestSent", target.getPlayer().getName());
        plugin.sendMessage(target.getPlayer(), "messages.teleport.teleportRequestReceived", player.getName());
    }
}
