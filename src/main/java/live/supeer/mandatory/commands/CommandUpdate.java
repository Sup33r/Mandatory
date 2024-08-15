package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import de.tr7zw.changeme.nbtapi.NBTItem;
import live.supeer.mandatory.MPlayer;
import live.supeer.mandatory.MPlayerManager;
import live.supeer.mandatory.Mandatory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@CommandAlias("update")
public class CommandUpdate extends BaseCommand {

    @Subcommand("copyright")
    public void onCopyright(Player player) {
        if (!player.hasPermission("mandatory.command.update.copyright")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            Mandatory.sendMessage(player, "messages.update.copyright.noItemInHand");
            return;
        }
        if (item.getType() != Material.FILLED_MAP || item.getType() != Material.WRITTEN_BOOK || !item.getItemMeta().hasLore()) {
            Mandatory.sendMessage(player, "messages.update.copyright.noCopyright");
            return;
        }
        NBTItem nbtItem = new NBTItem(item);
        String copyrighter = nbtItem.getString("copyrighter");
        MPlayer mPlayer = MPlayerManager.getPlayerFromUUID(player.getUniqueId());
        if (copyrighter == null || mPlayer == null) {
            Mandatory.sendMessage(player, "messages.update.copyright.noCopyright");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§dKopieringsskyddad av " + mPlayer.getName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.getInventory().getItemInMainHand().setAmount(0);
        player.getInventory().addItem(item);
        Mandatory.sendMessage(player, "messages.update.copyright.success");
    }
}
