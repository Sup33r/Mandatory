package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Private;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("teleportdeny|tpdeny|tpno")
public class CommandTeleportDeny extends BaseCommand {
    public static Mandatory plugin;

    @Default
    public void onTeleportDeny(Player player, @Optional OnlinePlayer target) {
        if (!player.hasPermission("mandatory.command.teleportdeny")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (!plugin.teleportRequests.containsValue(player)) {
            plugin.sendMessage(player, "messages.teleportRequests.noRequests");
            return;
        }
        if (target == null) {
            //if plugin.teleportRequests has the player as value in more than one value, then error:
            if (plugin.teleportRequests.values().stream().filter(p -> p.equals(player)).count() > 1) {
                plugin.sendMessage(player, "messages.teleportRequests.deny.multipleRequests");
                return;
            }
            //remove the player from the plugin.teleportRequests map
            Player requester = plugin.teleportRequests.entrySet().stream().filter(entry -> entry.getValue().equals(player)).findFirst().get().getKey();
            plugin.teleportRequests.remove(requester);
            plugin.sendMessage(player, "messages.teleportRequests.deny.selfOne", "%target%", requester.getName());
            plugin.sendMessage(requester, "messages.teleportRequests.deny.other", "%player%", player.getName());
            return;
        }
        if (plugin.teleportRequests.containsKey(target.getPlayer()) && plugin.teleportRequests.get(target.getPlayer()).equals(player)) {
            plugin.teleportRequests.remove(target.getPlayer());
            plugin.sendMessage(player, "messages.teleportRequests.deny.selfOne", "%target%", target.getPlayer().getName());
            plugin.sendMessage(target.getPlayer(), "messages.teleportRequests.deny.other", "%player%", player.getName());
        } else {
            plugin.sendMessage(player, "messages.teleportRequests.deny.noRequestPlayer", "%player%", target.getPlayer().getName());
        }
    }

    @Private
    @CommandAlias("teleportdeny!|tpdeny!|tpno!")
    public void onTeleportDenyAll(Player player) {
        if (!player.hasPermission("mandatory.command.teleportdeny")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (plugin.teleportRequests.containsValue(player)) {
            List<Player> requesters = plugin.teleportRequests.entrySet().stream().filter(entry -> entry.getValue().equals(player)).map(entry -> entry.getKey()).toList();
            requesters.forEach(requester -> {
                plugin.teleportRequests.remove(requester);
                plugin.sendMessage(requester, "messages.teleportRequests.deny.other", "%player%", player.getName());
            });
            plugin.sendMessage(player, "messages.teleportRequests.deny.selfAll");
        } else {
            plugin.sendMessage(player, "messages.teleportRequests.noRequests");
        }
    }
}
