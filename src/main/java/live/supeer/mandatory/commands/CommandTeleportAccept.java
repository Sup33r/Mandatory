package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Private;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.TeleportUtil;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("teleportaccept|tpaccept|tpyes")
public class CommandTeleportAccept extends BaseCommand {
    public static Mandatory plugin;


    @Default
    public void onTeleportAccept(Player player, @Optional OnlinePlayer target) {
        if (!player.hasPermission("mandatory.command.teleportaccept")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (!plugin.teleportRequests.containsKey(player)) {
            plugin.sendMessage(player, "messages.teleportRequests.noRequests");
            return;
        }
        if (target == null) {
            //if plugin.teleportRequests has the player as value in more than one value, then error:
            if (plugin.teleportRequests.values().stream().filter(p -> p.equals(player)).count() > 1) {
                plugin.sendMessage(player, "messages.teleportRequests.accept.multipleRequests");
                return;
            }
            //teleport the player to the requester
            Player requester = plugin.teleportRequests.remove(player);
            plugin.sendMessage(player, "messages.teleportRequests.accept.selfOne", "%target%", requester.getName());
            plugin.sendMessage(requester, "messages.teleportRequests.accept.other", "%player%", player.getName());
            TeleportUtil.teleport(requester, player.getLocation(), player.getName(),false);
            return;
        }
        if (plugin.teleportRequests.containsKey(target.getPlayer()) && plugin.teleportRequests.get(target.getPlayer()).equals(player)) {
            plugin.teleportRequests.remove(target.getPlayer());
            plugin.sendMessage(player, "messages.teleportRequests.accept.selfOne", "%target%", target.getPlayer().getName());
            plugin.sendMessage(target.getPlayer(), "messages.teleportRequests.accept.other", "%player%", player.getName());
            TeleportUtil.teleport(target.getPlayer(), player.getLocation(), player.getName(),false);
        } else {
            plugin.sendMessage(player, "messages.teleportRequests.accept.noRequestPlayer", "%player%", target.getPlayer().getName());
        }
    }

    @Private
    @CommandAlias("teleportaccept!|tpaceept!|tpyes!")
    public void onTeleportAcceptAll(Player player) {
        if (!player.hasPermission("mandatory.command.teleportaccept")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (plugin.teleportRequests.containsKey(player)) {
            List<Player> requesters = plugin.teleportRequests.entrySet().stream().filter(entry -> entry.getValue().equals(player)).map(entry -> entry.getKey()).toList();
            requesters.forEach(requester -> {
                plugin.teleportRequests.remove(requester);
                plugin.sendMessage(requester, "messages.teleportRequests.accept.other", "%player%", player.getName());
                TeleportUtil.teleport(requester, player.getLocation(), player.getName(),false);
            });
            plugin.sendMessage(player, "messages.teleportRequests.accept.selfAll");
        } else {
            plugin.sendMessage(player, "messages.teleportRequests.noRequest");
        }
    }
}
