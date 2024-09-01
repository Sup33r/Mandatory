package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.Home;
import live.supeer.apied.MPlayer;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("homes")
public class CommandHomes extends BaseCommand {

    @Default
    public void onHomes(Player player, @Optional String name) {
        if (name == null) {
            if (!player.hasPermission("mandatory.command.homes")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = ApiedAPI.getPlayer(player);
            if (mPlayer == null) {
                Mandatory.sendMessage(player,"messages.error.playerNotFound");
                return;
            }
            List<Home> homes = mPlayer.getHomes();
            if (homes.isEmpty()) {
                Mandatory.sendMessage(player,"messages.homes.none");
                return;
            }
            String names = Utilities.formatHomeList(homes);
            String max;
            if (player.hasPermission("mandatory.command.homes.extra")) {
                max = String.valueOf(Mandatory.configuration.getMaxHomesExtra());
            } else {
                max = String.valueOf(Mandatory.configuration.getMaxHomes());
            }
            Mandatory.sendMessage(player,"messages.homes.own", "%count%", String.valueOf(homes.size()), "%max%", max, "%list%", names);
        } else {
            if (!player.hasPermission("mandatory.command.homes.others")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = ApiedAPI.getPlayer(name);
            if (mPlayer == null) {
                Mandatory.sendMessage(player,"messages.error.playerNotFound");
                return;
            }
            List<Home> homes = mPlayer.getHomes();
            if (homes.isEmpty()) {
                Mandatory.sendMessage(player,"messages.homes.none");
                return;
            }
            String names = Utilities.formatStringList(homes.stream().map(Home::getName).toList());
            String max = String.valueOf(Mandatory.configuration.getMaxHomes());
            Mandatory.sendMessage(player,"messages.homes.others","%player%", mPlayer.getName(), "%count%", String.valueOf(homes.size()), "%max%", max, "%list%", names);
        }
    }
}
