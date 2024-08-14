package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("buy")
public class CommandBuy extends BaseCommand {
    public static Mandatory plugin;

    @Default
    public void onBuy(Player player) {
        if (!player.hasPermission("mandatory.command.buy")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        plugin.sendMessage(player, "messages.buy.syntax");
    }

    @Subcommand("broadcast")
    public void onBroadcast(Player player) {
        if (!player.hasPermission("mandatory.command.buy.broadcast")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        plugin.sendMessage(player, "messages.buy.syntax");
    }

    @Subcommand("go")
    public void onGo(Player player) {
        if (!player.hasPermission("mandatory.command.buy.go")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        plugin.sendMessage(player, "messages.buy.syntax");
    }

    @Subcommand("head")
    public void onHead(Player player) {
        if (!player.hasPermission("mandatory.command.buy.head")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        plugin.sendMessage(player, "messages.buy.syntax");
    }

    @Subcommand("copyright")
    public void onCopyright(Player player) {
        if (!player.hasPermission("mandatory.command.buy.copyright")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        plugin.sendMessage(player, "messages.buy.syntax");
    }
}
