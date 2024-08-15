package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import org.bukkit.entity.Player;

@CommandAlias("message|msg")
public class CommandMessage extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onMessage(Player player, OnlinePlayer target, String message) {
        if (!player.hasPermission("mandatory.command.message")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (target == null) {
            Mandatory.sendMessage(player, "messages.error.playerNotFound");
            return;
        }
        MandatoryChat.sendPrivateMessage(player, target.getPlayer(), message);
    }
}
