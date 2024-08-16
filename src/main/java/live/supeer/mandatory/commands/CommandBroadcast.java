package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import org.bukkit.entity.Player;

@CommandAlias("broadcast|bc")
public class CommandBroadcast extends BaseCommand {

    @Default
    public static void onBroadcast(Player player, String message) {
        if (!player.hasPermission("mandatory.command.broadcast")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MandatoryChat.sendBroadcast(message);
    }
}
