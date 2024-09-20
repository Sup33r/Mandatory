package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import live.supeer.mandatory.Mandatory;
import live.supeer.metropolis.acf.annotation.CommandAlias;
import live.supeer.metropolis.acf.annotation.CommandPermission;
import live.supeer.metropolis.acf.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("discord|dc")
public class CommandDiscord extends BaseCommand {

    @Default
    @CommandPermission("mandatory.command.discord")
    public void onDiscord(Player player) {
        Mandatory.sendMessage(player, "messages.discord");
    }
}
