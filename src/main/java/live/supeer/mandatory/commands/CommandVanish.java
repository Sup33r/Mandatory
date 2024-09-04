package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("vanish|v")
@Private
public class CommandVanish extends BaseCommand {
    //TODO: Fixa allt det här vid ett senare skede.
    @Default
    @Private
    public void onVanish(Player player, @Optional OnlinePlayer target) {
        if (!player.hasPermission("mandatory.command.vanish")) {
            player.sendMessage("messages.error.permissionDenied");
            return;
        }
        if (target == null) {
            if (!player.hasPermission("mandatory.command.vanish.self")) {
                player.sendMessage("messages.error.permissionDenied");
                return;
            }
            if (player.isInvisible()) {
                player.sendMessage("messages.vanish.selfOff");
                Mandatory.vanishedPlayers.remove(player);
                //showPlayer(player);
                return;
            }
            player.sendMessage("messages.vanish.self");
            Mandatory.vanishedPlayers.add(player);
            //hidePlayer(player);
        } else {
            if (!player.hasPermission("mandatory.command.vanish.others")) {
                player.sendMessage("messages.error.permissionDenied");
                return;
            }
            if (target.getPlayer().isInvisible()) {
                player.sendMessage("messages.vanish.otherOff", "%player%", target.getPlayer().getName());
                Mandatory.vanishedPlayers.remove(player);
                //showPlayer(target.getPlayer());
                return;
            }
            player.sendMessage("messages.vanish.other", "%player%", target.getPlayer().getName());
            Mandatory.vanishedPlayers.add(target.getPlayer());
            //hidePlayer(target.getPlayer());
        }
    }
}
