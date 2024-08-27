package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.entity.Player;

@CommandAlias("near")
public class CommandNear extends BaseCommand {

    @Default
    @CommandCompletion("@range:3000")
    public void onNear(Player player, @Optional Integer radius) {
        if (!player.hasPermission("mandatory.command.near")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (radius == null) {
            radius = 200;
        } else if (!player.hasPermission("mandatory.command.near.custom")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        } else if (radius > 3000) {
            Mandatory.sendMessage(player, "messages.error.nearTooLarge");
            return;
        }
        StringBuilder players = new StringBuilder();
        Integer finalRadius = radius;
        player.getWorld().getPlayers().forEach(p -> {
            if (p.getLocation().distance(player.getLocation()) <= finalRadius) {
                players.append(p.getName()).append(", ");
            }
        });
        Mandatory.sendMessage(player, "messages.near.");
    }
}
