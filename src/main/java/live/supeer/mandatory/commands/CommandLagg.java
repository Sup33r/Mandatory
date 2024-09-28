package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandAlias("lagg")
public class CommandLagg extends BaseCommand {

    @Default
    @CommandPermission("mandatory.command.lagg")
    public void onLagg(CommandSender sender) {
        double tps = Bukkit.getTPS()[0];
        if (tps < 18.0) {
            Mandatory.sendMessage(sender, "messages.lagg.lagging");
        } else {
            Mandatory.sendMessage(sender, "messages.lagg.notLagging");
        }
    }
}
