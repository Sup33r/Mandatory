package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("feed")
public class CommandFeed extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onFeed(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            if (!player.hasPermission("mandatory.command.feed")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (player.getFoodLevel() == 20) {
                Mandatory.sendMessage(player, "messages.feed.fullFood");
                return;
            }
            player.setFoodLevel(20);
            player.setExhaustion(0F);
            Mandatory.sendMessage(player, "messages.feed.fed");
        } else {
            if (!player.hasPermission("mandatory.command.feed.others")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (target.getPlayer().getFoodLevel() == 20) {
                Mandatory.sendMessage(player, "messages.feed.fullFoodOther", "%player%", target.getPlayer().getName());
                return;
            }
            target.getPlayer().setFoodLevel(20);
            target.getPlayer().setExhaustion(0F);
            Mandatory.sendMessage(player, "messages.feed.fedOther", "%player%", target.getPlayer().getName());
        }
    }
}
