package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("gamemode|g")
public class CommandGamemode extends BaseCommand {
    public static Mandatory plugin;

    @Default
    @CommandCompletion("@gamemode @players")
    public void onGamemode(Player player, String mode, @Optional OnlinePlayer target) {
        if (target == null) {
            if (!player.hasPermission("mandatory.command.gamemode")) {
                plugin.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (mode.equalsIgnoreCase("survival") || mode.equalsIgnoreCase("0")) {
                player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                plugin.sendMessage(player, "messages.gamemode.changed", "%gamemode%", "SURVIVAL");
            } else if (mode.equalsIgnoreCase("creative") || mode.equalsIgnoreCase("1")) {
                player.setGameMode(org.bukkit.GameMode.CREATIVE);
                plugin.sendMessage(player, "messages.gamemode.changed", "%gamemode%", "CREATIVE");
            } else if (mode.equalsIgnoreCase("adventure") || mode.equalsIgnoreCase("2")) {
                player.setGameMode(org.bukkit.GameMode.ADVENTURE);
                plugin.sendMessage(player, "messages.gamemode.changed", "%gamemode%", "ADVENTURE");
            } else if (mode.equalsIgnoreCase("spectator") || mode.equalsIgnoreCase("3")) {
                player.setGameMode(org.bukkit.GameMode.SPECTATOR);
                plugin.sendMessage(player, "messages.gamemode.changed", "%gamemode%", "SPECTATOR");
            } else {
                plugin.sendMessage(player, "messages.gamemode.invalid");
            }
        } else {
            if (!player.hasPermission("mandatory.command.gamemode.others")) {
                plugin.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (mode.equalsIgnoreCase("survival") || mode.equalsIgnoreCase("0")) {
                target.getPlayer().setGameMode(org.bukkit.GameMode.SURVIVAL);
                plugin.sendMessage(player, "messages.gamemode.changedOther", "%player%", target.getPlayer().getName(), "%gamemode%", "SURVIVAL");
            } else if (mode.equalsIgnoreCase("creative") || mode.equalsIgnoreCase("1")) {
                target.getPlayer().setGameMode(org.bukkit.GameMode.CREATIVE);
                plugin.sendMessage(player, "messages.gamemode.changedOther", "%player%", target.getPlayer().getName(), "%gamemode%", "CREATIVE");
            } else if (mode.equalsIgnoreCase("adventure") || mode.equalsIgnoreCase("2")) {
                target.getPlayer().setGameMode(org.bukkit.GameMode.ADVENTURE);
                plugin.sendMessage(player, "messages.gamemode.changedOther", "%player%", target.getPlayer().getName(), "%gamemode%", "ADVENTURE");
            } else if (mode.equalsIgnoreCase("spectator") || mode.equalsIgnoreCase("3")) {
                target.getPlayer().setGameMode(org.bukkit.GameMode.SPECTATOR);
                plugin.sendMessage(player, "messages.gamemode.changedOther", "%player%", target.getPlayer().getName(), "%gamemode%", "SPECTATOR");
            } else {
                plugin.sendMessage(player, "messages.gamemode.invalid");
            }
        }
    }
}
