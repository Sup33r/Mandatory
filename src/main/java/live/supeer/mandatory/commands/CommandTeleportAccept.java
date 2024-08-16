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
import java.util.Map;

@CommandAlias("teleportaccept|tpaccept|tpyes")
public class CommandTeleportAccept extends BaseCommand {

    @Default
    public void onTeleportAccept(Player player, @Optional OnlinePlayer target) {
        if (!player.hasPermission("mandatory.command.teleportaccept")) {
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
                Mandatory.sendMessage(player, "messages.teleportRequests.accept.multipleRequests");
                return;
            }
            //find the key that corresponds to the player as a value
            Player requester = Mandatory.teleportRequests.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(player))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
            if (requester != null) {
                Mandatory.teleportRequests.remove(requester);
                Mandatory.sendMessage(player, "messages.teleportRequests.accept.selfOne", "%target%", requester.getName());
                Mandatory.sendMessage(requester, "messages.teleportRequests.accept.other", "%player%", player.getName());
                TeleportUtil.teleport(requester, player.getLocation(), player.getName(),false);
            }
            return;
        }
        if (target.getPlayer() != null && Mandatory.teleportRequests.containsKey(target.getPlayer()) && Mandatory.teleportRequests.get(target.getPlayer()).equals(player)) {
            Mandatory.teleportRequests.remove(target.getPlayer());
            Mandatory.sendMessage(player, "messages.teleportRequests.accept.selfOne", "%target%", target.getPlayer().getName());
            Mandatory.sendMessage(target.getPlayer(), "messages.teleportRequests.accept.other", "%player%", player.getName());
            TeleportUtil.teleport(target.getPlayer(), player.getLocation(), player.getName(),false);
        } else {
            Mandatory.sendMessage(player, "messages.teleportRequests.accept.noRequestPlayer", "%player%", target.getPlayer().getName());
        }
    }

    @Private
    @CommandAlias("teleportaccept!|tpaccept!|tpyes!")
    public void onTeleportAcceptAll(Player player) {
        if (!player.hasPermission("mandatory.command.teleportaccept")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (Mandatory.teleportRequests.containsValue(player)) {
            List<Player> requesters = Mandatory.teleportRequests.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(player))
                    .map(Map.Entry::getKey)
                    .toList();
            requesters.forEach(requester -> {
                Mandatory.teleportRequests.remove(requester);
                Mandatory.sendMessage(requester, "messages.teleportRequests.accept.other", "%player%", player.getName());
                TeleportUtil.teleport(requester, player.getLocation(), player.getName(), false);
            });
            Mandatory.sendMessage(player, "messages.teleportRequests.accept.selfAll");
        } else {
            Mandatory.sendMessage(player, "messages.teleportRequests.noRequest");
        }
    }
}
