package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("ping")
public class CommandPing extends BaseCommand {

    @Default
    @CommandPermission("mandatory.command.ping")
    public void onPing(Player player) {
        Mandatory.sendMessage(player, "messages.ping", "%ping%", String.valueOf(player.getPing()));
    }
}
