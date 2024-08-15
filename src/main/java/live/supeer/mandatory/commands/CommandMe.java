package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import org.bukkit.entity.Player;

@CommandAlias("me")
public class CommandMe extends BaseCommand {

    @Default
    public void onMe(Player player, String message) {
        if (!player.hasPermission("mandatory.command.me")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MandatoryChat.sendMeMessage(player, message);
    }
}
