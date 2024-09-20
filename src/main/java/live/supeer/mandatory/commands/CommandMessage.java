package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.apied.MPlayer;
import live.supeer.apied.MPlayerManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import live.supeer.metropolis.acf.annotation.CommandPermission;
import org.bukkit.entity.Player;

@CommandAlias("message|msg|tell|whisper|w")
public class CommandMessage extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    @CommandPermission("mandatory.command.message")
    public void onMessage(Player player, OnlinePlayer target, String message) {
        if (target == null) {
            Mandatory.sendMessage(player, "messages.error.playerNotFound");
            return;
        }
        MPlayer mTarget = MPlayerManager.getPlayerFromPlayer(target.getPlayer());
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        if (mPlayer.getUuid().equals(mTarget.getUuid())) {
            Mandatory.sendMessage(player, "messages.message.cantMessageSelf");
            return;
        }
        if (mTarget.getIgnores().contains(player.getUniqueId())) {
            Mandatory.sendMessage(player, "messages.message.ignored");
            return;
        }
        if (mPlayer.getIgnores().contains(mTarget.getUuid())) {
            Mandatory.sendMessage(player, "messages.message.ignoring");
            return;
        }
        MandatoryChat.sendPrivateMessage(player, target.getPlayer(), message);
    }
}
