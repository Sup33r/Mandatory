package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.MandatoryChat;
import org.bukkit.entity.Player;

@CommandAlias("broadcast|bc")
public class CommandBroadcast extends BaseCommand {

    @Default
    @CommandPermission("mandatory.command.broadcast")
    public static void onBroadcast(Player player, String message) {
        MandatoryChat.sendBroadcast(message);
    }
}
