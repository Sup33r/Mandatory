package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import live.supeer.apied.ShopManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.Utilities;
import live.supeer.metropolis.Metropolis;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("online")
public class CommandOnline extends BaseCommand {

    @Default
    @CommandPermission("mandatory.command.online")
    public void onOnline(Player player) {
        List<String> admins = new ArrayList<>();
        List<String> moderators = new ArrayList<>();
        List<String> players = new ArrayList<>();

        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("group.admin")) {
                if (Metropolis.afkPlayers.containsKey(onlinePlayer.getUniqueId())) {
                    admins.add("<italic>" + onlinePlayer.getName() + "</italic>");
                } else {
                    admins.add(onlinePlayer.getName());
                }
            } else if (onlinePlayer.hasPermission("group.mod")) {
                if (Metropolis.afkPlayers.containsKey(onlinePlayer.getUniqueId())) {
                    moderators.add("<italic>" + onlinePlayer.getName() + "</italic>");
                } else {
                    moderators.add(onlinePlayer.getName());
                }
            } else {
                if (Metropolis.afkPlayers.containsKey(onlinePlayer.getUniqueId())) {
                    players.add("<italic>" + onlinePlayer.getName() + "</italic>");
                } else {
                    players.add(onlinePlayer.getName());
                }
            }
        }

        Mandatory.sendMessage(player, "messages.online.header", "%count%", admins.size() + moderators.size() + players.size() + "");
        Mandatory.sendMessage(player, "messages.online.empty");
        if (!admins.isEmpty()) {
            Mandatory.sendMessage(player, "messages.online.admin.header");
            Mandatory.sendMessage(player, "messages.online.list", "%list%", Utilities.formatStringList(admins));
            Mandatory.sendMessage(player, "messages.online.empty");
        }
        if (!moderators.isEmpty()) {
            Mandatory.sendMessage(player, "messages.online.mod.header");
            Mandatory.sendMessage(player, "messages.online.list", "%list%", Utilities.formatStringList(moderators));
            Mandatory.sendMessage(player, "messages.online.empty");
        }
        if (!players.isEmpty()) {
            Mandatory.sendMessage(player, "messages.online.player.header");
            Mandatory.sendMessage(player, "messages.online.list", "%list%", Utilities.formatStringList(players));
        }
    }
}
