package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.MPlayer;
import live.supeer.apied.MPlayerManager;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("unban")
public class CommandUnban extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onUnban(Player player, String target) {
        if (!player.hasPermission("mandatory.command.unban")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MPlayer targetPlayer = ApiedAPI.getPlayer(target);
        if (targetPlayer == null) {
            Mandatory.sendMessage(player, "messages.error.playerNotFound");
            return;
        }
        if (!targetPlayer.isBanned()) {
            Mandatory.sendMessage(player, "messages.unban.notBanned");
            return;
        }
        MPlayerManager.removeBan(targetPlayer.getUuid());
        Mandatory.sendMessage(player, "messages.unban.success", "%player%", targetPlayer.getName());
    }
}
