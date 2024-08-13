package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@CommandAlias("repair")
public class CommandRepair extends BaseCommand {
    public static Mandatory plugin;

    @Default
    public void onRepair(Player player) {
        if (!player.hasPermission("mandatory.command.repair")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (player.getInventory().getItemInMainHand().getType().getMaxDurability() == 0) {
            plugin.sendMessage(player, "messages.repair.notRepairable");
            return;
        }
        List<String> repairableItems = Mandatory.configuration.getAllowedRepairItems();
        if (!repairableItems.contains(player.getInventory().getItemInMainHand().getType().name())) {
            plugin.sendMessage(player, "messages.repair.notRepairable");
            return;
        }
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        Damageable damageable = (Damageable) meta;
        damageable.resetDamage();
        player.getInventory().getItemInMainHand().setItemMeta(meta);
        plugin.sendMessage(player, "messages.repair.success");
    }
}
