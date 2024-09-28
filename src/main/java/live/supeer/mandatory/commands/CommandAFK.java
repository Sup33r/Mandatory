package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.apied.ApiedAPI;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import live.supeer.mandatory.utils.TeleportUtil;
import live.supeer.metropolis.Metropolis;
import org.bukkit.entity.Player;

@CommandAlias("afk")
public class CommandAFK extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    @CommandPermission("mandatory.command.afk")
    public static void onAFK(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            if (Metropolis.afkPlayers.containsKey(player.getUniqueId())) {
                Mandatory.sendMessage(player, "messages.afk.alreadyAfk");
                return;
            }
            TeleportUtil.teleportAfk(player, false);
        } else {
            if (!player.hasPermission("mandatory.command.afk.others")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (Metropolis.afkPlayers.containsKey(target.getPlayer().getUniqueId())) {
                Mandatory.sendMessage(player, "messages.afk.alreadyAfkOther", "%player%", target.getPlayer().getName());
                return;
            }
            TeleportUtil.teleportAfk(target.getPlayer(), true);
            Mandatory.sendMessage(player, "messages.successful.afk", "%player%", target.getPlayer().getName());
        }
    }
}
