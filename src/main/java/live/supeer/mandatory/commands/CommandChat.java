package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import live.supeer.mandatory.ChannelManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("chat")
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
        ChannelManager.joinChannel(player, channelName);
    }

    @Subcommand("leave")
    public void onLeave(Player player, String channelName) {
        ChannelManager.leaveChannel(player, channelName);
    }

    @Subcommand("switch")
    public void onSwitch(Player player, String channelName) {
        ChannelManager.switchChannel(player, channelName);
    }

    @CommandAlias("citychat|cc")
    public class CommandCityChat extends BaseCommand {

        @Default
        public void onCityChat(Player player, @Optional String message) {
            if (message == null) {
                ChannelManager.switchChannel(player, "city");
            } else {
                // Send message to city chat
                // Implement this method
            }
        }
    }

    @CommandAlias("modchat|mc")
    public class CommandModChat extends BaseCommand {

        @Default
        public void onModChat(Player player, @Optional String message) {
            if (message == null) {
                ChannelManager.switchChannel(player, "mod");
            } else {
                // Send message to mod chat
                // Implement this method
            }
        }
    }
}