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
import java.util.Map;

@CommandAlias("teleportdeny|tpdeny|tpno")
public class CommandTeleportDeny extends BaseCommand {
    @Default
    public void onTeleportDeny(Player player, @Optional OnlinePlayer target) {
        if (!player.hasPermission("mandatory.command.teleportdeny")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (!Mandatory.teleportRequests.containsValue(player)) {
            Mandatory.sendMessage(player, "messages.teleportRequests.noRequests");
            return;
        }
        if (target == null) {
            //if Mandatory.teleportRequests has the player as value in more than one value, then error:
            if (Mandatory.teleportRequests.values().stream().filter(p -> p.equals(player)).count() > 1) {
                Mandatory.sendMessage(player, "messages.teleportRequests.deny.multipleRequests");
                return;
            }
            //remove the player from the Mandatory.teleportRequests map
            Player requester = Mandatory.teleportRequests.entrySet().stream().filter(entry -> entry.getValue().equals(player)).findFirst().get().getKey();
            Mandatory.teleportRequests.remove(requester);
            Mandatory.sendMessage(player, "messages.teleportRequests.deny.selfOne", "%target%", requester.getName());
            Mandatory.sendMessage(requester, "messages.teleportRequests.deny.other", "%player%", player.getName());
            return;
        }
        if (Mandatory.teleportRequests.containsKey(target.getPlayer()) && Mandatory.teleportRequests.get(target.getPlayer()).equals(player)) {
            Mandatory.teleportRequests.remove(target.getPlayer());
            Mandatory.sendMessage(player, "messages.teleportRequests.deny.selfOne", "%target%", target.getPlayer().getName());
            Mandatory.sendMessage(target.getPlayer(), "messages.teleportRequests.deny.other", "%player%", player.getName());
        } else {
            Mandatory.sendMessage(player, "messages.teleportRequests.deny.noRequestPlayer", "%player%", target.getPlayer().getName());
        }
    }

    @Private
    @CommandAlias("teleportdeny!|tpdeny!|tpno!")
    public void onTeleportDenyAll(Player player) {
        if (!player.hasPermission("mandatory.command.teleportdeny")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (Mandatory.teleportRequests.containsValue(player)) {
            List<Player> requesters = Mandatory.teleportRequests.entrySet().stream().filter(entry -> entry.getValue().equals(player)).map(Map.Entry::getKey).toList();
            requesters.forEach(requester -> {
                Mandatory.teleportRequests.remove(requester);
                Mandatory.sendMessage(requester, "messages.teleportRequests.deny.other", "%player%", player.getName());
            });
            Mandatory.sendMessage(player, "messages.teleportRequests.deny.selfAll");
        } else {
            Mandatory.sendMessage(player, "messages.teleportRequests.noRequests");
        }
    }
}
