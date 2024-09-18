package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Channel;
import live.supeer.mandatory.ChannelManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import live.supeer.metropolis.MetropolisAPI;
import live.supeer.metropolis.city.City;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

@CommandAlias("modchat|mc")
public class CommandModchat extends BaseCommand {

    @Default
    @CatchUnknown
    public void onModchat(Player player, @Optional String message) {
        if (!player.hasPermission("mandatory.command.modchat")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        Channel channel = ChannelManager.getChannel("modzone");
        if (channel == null) {
            Mandatory.sendMessage(player, "messages.modchat.doesNotExist");
            return;
        }
        if (!ChannelManager.getPlayerChannels(player).contains("modzone")) {
            Mandatory.sendMessage(player, "messages.modchat.missing.channel");
            return;
        }
        if (message == null) {
            if (ChannelManager.isActiveChannel(player, channel)) {
                ChannelManager.setActiveChannel(player, "global");
                Mandatory.sendMessage(player, "messages.modchat.switched.global");
                return;
            }
            ChannelManager.setActiveChannel(player, channel.getName());
            Mandatory.sendMessage(player, "messages.modchat.switched.modchat");
            return;
        }
        MandatoryChat.sendMessage(player, channel, (TextComponent) Mandatory.stringToMiniMessage(message));
    }

    @Subcommand("help")
    public void onHelp(Player player, OnlinePlayer target) {
        if (!player.hasPermission("mandatory.command.modchat")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MandatoryChat.modChatHelping(player, target.getPlayer());
    }
}
