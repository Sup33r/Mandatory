package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
        List<Player> nearbyPlayers = getNearbyPlayers(player, radius);

        if (nearbyPlayers.isEmpty()) {
            Mandatory.sendMessage(player, "messages.near.noPlayers");
            return;
        }
        Mandatory.sendMessage(player, "messages.near.header", "%radius%", String.valueOf(radius));
        Mandatory.sendMessage(player, "messages.near.players", "%players%", formatPlayerListWithDistances(player, nearbyPlayers));
    }


    private List<Player> getNearbyPlayers(Player player, int radius) {
        List<Player> nearby = new ArrayList<>();
        for (Player p : player.getWorld().getPlayers()) {
            if (p != player && p.getLocation().distance(player.getLocation()) <= radius) {
                nearby.add(p);
            }
        }
        return nearby;
    }


    private String formatPlayerListWithDistances(Player source, List<Player> players) {
        List<String> formattedEntries = new ArrayList<>();
        for (Player p : players) {
            int distance = (int) source.getLocation().distance(p.getLocation());
            formattedEntries.add(String.format("<green>%s <dark_green>(<green>%d meter<dark_green>)", p.getName(), distance));
        }
        return Utilities.formatStringList(formattedEntries);
    }
}
