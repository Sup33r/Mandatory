package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("fly")
public class CommandFly extends BaseCommand {
    public static Mandatory plugin;

    @Default
    @CommandCompletion("@players")
    public void onFly(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            if (!player.hasPermission("mandatory.command.fly")) {
                plugin.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                plugin.sendMessage(player, "messages.fly.disabled");
            } else {
                player.setAllowFlight(true);
                plugin.sendMessage(player, "messages.fly.enabled");
            }
        } else {
            if (!player.hasPermission("mandatory.command.fly.others")) {
                plugin.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (target.getPlayer().getAllowFlight()) {
                target.getPlayer().setAllowFlight(false);
                plugin.sendMessage(player, "messages.fly.disabledOther", "%player%", target.getPlayer().getName());
            } else {
                target.getPlayer().setAllowFlight(true);
                plugin.sendMessage(player, "messages.fly.enabledOther", "%player%", target.getPlayer().getName());
            }
        }
    }

}
