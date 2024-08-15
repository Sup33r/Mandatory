package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import org.bukkit.entity.Player;

@CommandAlias("reply|r")
public class CommandReply extends BaseCommand {

    @Default
    public void onReply(Player player, String message) {
        if (!player.hasPermission("mandatory.command.reply")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (!Mandatory.replyPlayers.containsKey(player)) {
            Mandatory.sendMessage(player, "messages.error.noReply");
            return;
        }
        Player target = Mandatory.replyPlayers.get(player);
        MandatoryChat.sendPrivateMessage(player, target, message);
    }

}
