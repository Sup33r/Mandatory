package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import live.supeer.mandatory.ChannelManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("chat")
@CommandPermission("mandatory.command.chat")
public class CommandChat extends BaseCommand {

    @Default
    public void onChat(Player player) {
        if (!player.hasPermission("mandatory.command.chat")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        List<String> channels = ChannelManager.getChannels(player);
        if (channels.isEmpty()) {
            Mandatory.sendMessage(player, "messages.chat.channels.noChannels");
            return;
        }
        String channelsString = Utilities.formatStringList(channels);
        Mandatory.sendMessage(player, "messages.chat.channels.list", "%channels%", channelsString);
    }

    @Subcommand("join")
    public void onJoin(Player player, String channelName) {
        if (!player.hasPermission("mandatory.command.chat.join")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        ChannelManager.joinChannel(player, channelName);
    }

    @Subcommand("leave")
    public void onLeave(Player player, String channelName) {
        if (!player.hasPermission("mandatory.command.chat.leave")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        ChannelManager.leaveChannel(player, channelName);
    }

    @Subcommand("switch")
    public void onSwitch(Player player, String channelName) {
        if (!player.hasPermission("mandatory.command.chat.switch")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        ChannelManager.switchChannel(player, channelName);
    }
}