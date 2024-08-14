package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.MPlayerManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.DateUtil;
import org.bukkit.entity.Player;

@CommandAlias("love")
public class CommandLove extends BaseCommand {
    public static Mandatory plugin;

    @Default
    public void onLove(Player player, OnlinePlayer receiver, @Optional String reason) {
        if (!player.hasPermission("mandatory.command.love")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (plugin.loveCooldown.contains(player)) {
            plugin.sendMessage(player, "messages.love.cooldown", "%time%", DateUtil.formatDateDiff(Mandatory.configuration.getLoveCooldown()+System.currentTimeMillis()));
            return;
        }

        if( player.equals(receiver.getPlayer())) {
            plugin.sendMessage(player, "messages.love.self");
            return;
        }
        plugin.loveCooldown.add(player);
        MPlayerManager.handleLoveSent(player, receiver.getPlayer(), reason);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.loveCooldown.remove(player), 20L * Mandatory.configuration.getLoveCooldown());
    }

    @Subcommand("all")
    public void onAllLove(Player player, @Optional String reason) {
        if (!player.hasPermission("mandatory.command.love.all")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (plugin.loveCooldown.contains(player)) {
            plugin.sendMessage(player, "messages.love.cooldown", "%time%", DateUtil.formatDateDiff(Mandatory.configuration.getLoveCooldown()+System.currentTimeMillis()));
            return;
        }
        plugin.loveCooldown.add(player);
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            MPlayerManager.handleLoveSent(player, onlinePlayer, reason);
        }
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.loveCooldown.remove(player), 20L * Mandatory.configuration.getLoveCooldown());
    }
}
