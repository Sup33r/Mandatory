package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import org.bukkit.entity.Player;

@CommandAlias("helpop")
public class CommandHelpop extends BaseCommand {
    public static Mandatory plugin;

    @Default
    public void onHelpop(Player player, String message) {
        if (!player.hasPermission("mandatory.command.helpop")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MandatoryChat.helpopMessage(player, message);
    }
}
