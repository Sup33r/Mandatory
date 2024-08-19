package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import live.supeer.mandatory.Channel;
import live.supeer.mandatory.ChannelManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.MandatoryChat;
import live.supeer.metropolis.MetropolisAPI;
import live.supeer.metropolis.city.City;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

@CommandAlias("citychat|cc")
public class CommandCityChat extends BaseCommand {

    @Default
    public void onCityChat(Player player, @Optional String message) {
        if (!player.hasPermission("mandatory.command.citychat")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        City city = MetropolisAPI.getHomeCity(player);
        if (city == null) {
            Mandatory.sendMessage(player, "messages.citychat.missing.city");
            return;
        }
        Channel channel = ChannelManager.getChannel(ChannelManager.parseCityChannelName(city.getCityName()));
        Channel global = ChannelManager.getChannel("global");
        if (channel == null) {
            Mandatory.sendMessage(player, "messages.citychat.missing.channel");
            return;
        }
        if (ChannelManager.isActiveChannel(player, channel)) {
            if (message == null) {
                ChannelManager.setActiveChannel(player, "global");
                Mandatory.sendMessage(player, "messages.citychat.switched.global");
                return;
            }
            MandatoryChat.sendMessage(player, global , (TextComponent) Mandatory.stringToMiniMessage(message));
        } else {
            if (message == null) {
                ChannelManager.setActiveChannel(player, channel.getName());
                Mandatory.sendMessage(player, "messages.citychat.switched.city", "%channel%", channel.getName());
                return;
            }
            MandatoryChat.sendMessage(player, channel, (TextComponent) Mandatory.stringToMiniMessage(message));
        }
    }
}
