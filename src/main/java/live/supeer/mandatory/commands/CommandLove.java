package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Database;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("love")
public class CommandLove extends BaseCommand {

    @Default
    public void onLove(Player player, OnlinePlayer receiver, @Optional String reason) {
        if (!player.hasPermission("mandatory.command.love")) {
            Mandatory.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (Mandatory.loveCooldown.contains(player)) {
            Mandatory.sendMessage(player, "messages.love.cooldown", "%time%", DateUtil.formatDateDiff(Mandatory.configuration.getLoveCooldown()+System.currentTimeMillis()));
            return;
        }

        if( player.equals(receiver.getPlayer())) {
            Mandatory.sendMessage(player, "messages.love.self");
            return;
        }
        Mandatory.loveCooldown.add(player);
        Database.handleLoveSent(player, receiver.getPlayer(), reason);
        Bukkit.getServer().getScheduler().runTaskLater(Mandatory.getInstance(), () -> Mandatory.loveCooldown.remove(player), 20L * Mandatory.configuration.getLoveCooldown());
    }

    @Subcommand("all")
    public void onAllLove(Player player, @Optional String reason) {
        if (!player.hasPermission("mandatory.command.love.all")) {
            Mandatory.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (Mandatory.loveCooldown.contains(player)) {
            Mandatory.sendMessage(player, "messages.love.cooldown", "%time%", DateUtil.formatDateDiff(Mandatory.configuration.getLoveCooldown()+System.currentTimeMillis()));
            return;
        }
        Mandatory.loveCooldown.add(player);
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            Database.handleLoveSent(player, onlinePlayer, reason);
        }
        Mandatory.getInstance().getServer().getScheduler().runTaskLater(Mandatory.getInstance(), () -> Mandatory.loveCooldown.remove(player), 20L * Mandatory.configuration.getLoveCooldown());
    }
}
