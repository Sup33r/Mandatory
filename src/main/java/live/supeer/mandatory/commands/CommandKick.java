package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.InfractionManager;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("kick")
public class CommandKick extends BaseCommand {
    @Default
    @CommandCompletion("@players @reasons")
    public static void onKick(Player player, OnlinePlayer target, String reason) {
        if (!player.hasPermission("mandatory.command.kick")) {
            Mandatory.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (reason.equals("spam")) {
            reason = Mandatory.getMessage("messages.kick.reasons.spam");
        }
        assert reason != null;
        if (reason.equals("language")) {
            reason = Mandatory.getMessage("messages.kick.reasons.language");
        }
        assert reason != null;
        if (reason.equals("advertising")) {
            reason = Mandatory.getMessage("messages.kick.reasons.advertising");
        }
        InfractionManager.kickPlayer(target.getPlayer(), player, reason);
        Mandatory.sendMessage(player, "messages.kick.successful", "%player%", target.getPlayer().getName(), "%reason%", reason);
    }
}
