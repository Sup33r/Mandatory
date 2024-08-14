package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.InfractionManager;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("kick")
public class CommandKick extends BaseCommand {
    public static Mandatory plugin;

    public void onKick(Player player, OnlinePlayer target, String reason) {
        if (!player.hasPermission("mandatory.command.kick")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (reason.equals("spam")) {
            reason = plugin.getMessage("messages.kick.reasons.spam");
        }
        if (reason.equals("language")) {
            reason = plugin.getMessage("messages.kick.reasons.language");
        }
        InfractionManager.kickPlayer(target.getPlayer(), player, reason);
        plugin.sendMessage(player, "messages.kick.successful", "%player%", target.getPlayer().getName(), "%reason%", reason);
    }
}
