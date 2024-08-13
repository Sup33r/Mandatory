package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import org.bukkit.entity.Player;

@CommandAlias("modchat|mc")
public class CommandModchat extends BaseCommand {
    public static Mandatory plugin;

    @Default
    @CatchUnknown
    public void onModchat(Player player, String message) {
        if (!player.hasPermission("mandatory.command.modchat")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MandatoryChat.sendModChatMessage(player, message);
    }

    @Subcommand("help")
    public void onHelp(Player player, OnlinePlayer target) {
        if (!player.hasPermission("mandatory.command.modchat")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MandatoryChat.modChatHelping(player, target.getPlayer());
    }
}
