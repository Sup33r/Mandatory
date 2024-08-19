package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("speed")
public class CommandSpeed extends BaseCommand {

    @Default
    @CommandCompletion("@range:10 @players")
    public void onSpeed(Player player, int speed, @Optional OnlinePlayer target) {
        float scaledSpeed = (float) speed / 10f;
        if (target == null) {
            if (!player.hasPermission("mandatory.command.speed")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (speed < 0 || speed > 10) {
                Mandatory.sendMessage(player, "messages.speed.invalid");
                return;
            }
            if (player.isFlying()) {
                player.setFlySpeed(scaledSpeed);
                Mandatory.sendMessage(player, "messages.speed.setFly", "%speed%", String.valueOf(speed));
            } else {
                player.setWalkSpeed(scaledSpeed);
                Mandatory.sendMessage(player, "messages.speed.setWalk", "%speed%", String.valueOf(speed));
            }
        } else {
            if (!player.hasPermission("mandatory.command.speed.others")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (speed < 0 || speed > 10) {
                Mandatory.sendMessage(player, "messages.speed.invalid");
                return;
            }
            if (target.getPlayer().isFlying()) {
                target.getPlayer().setFlySpeed(scaledSpeed);
                Mandatory.sendMessage(player, "messages.speed.setFlyOther", "%player%", target.getPlayer().getName(), "%speed%", String.valueOf(speed));
            } else {
                target.getPlayer().setWalkSpeed(scaledSpeed);
                Mandatory.sendMessage(player, "messages.speed.setWalkOther", "%player%", target.getPlayer().getName(), "%speed%", String.valueOf(speed));
            }
        }
    }
}