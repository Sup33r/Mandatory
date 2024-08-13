package live.supeer.mandatory.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import org.bukkit.entity.Player;

@CommandAlias("reply|r")
public class CommandReply {
    public static Mandatory plugin;

    @Default
    public void onReply(Player player, String message) {
        if (!player.hasPermission("mandatory.command.reply")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (!plugin.replyPlayers.containsKey(player)) {
            plugin.sendMessage(player, "messages.error.noReply");
            return;
        }
        Player target = plugin.replyPlayers.get(player);
        MandatoryChat.sendPrivateMessage(player, target, message);
    }

}
