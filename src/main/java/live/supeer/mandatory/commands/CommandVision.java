package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CommandAlias("vision")
public class CommandVision extends BaseCommand {
    public static Mandatory plugin;

    @Default
    @CommandCompletion("@players")
    public void onVision(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            if (!player.hasPermission("mandatory.command.vision")) {
                plugin.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(org.bukkit.potion.PotionEffectType.NIGHT_VISION);
                plugin.sendMessage(player, "messages.vision.removed");
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 2, false, false));
                plugin.sendMessage(player, "messages.vision.added");
            }
        } else {
            if (!player.hasPermission("mandatory.command.vision.others")) {
                plugin.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (target.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                target.getPlayer().removePotionEffect(org.bukkit.potion.PotionEffectType.NIGHT_VISION);
                plugin.sendMessage(player, "messages.vision.removedOther", "%player%", target.getPlayer().getName());
            } else {
                target.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 2, false, false));
                plugin.sendMessage(player, "messages.vision.addedOther", "%player%", target.getPlayer().getName());
            }
        }
    }
}
