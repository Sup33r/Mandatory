package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import de.tr7zw.changeme.nbtapi.NBTItem;
import live.supeer.mandatory.CopyrightManager;
import live.supeer.mandatory.MPlayer;
import live.supeer.mandatory.MPlayerManager;
import live.supeer.mandatory.Mandatory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

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
        if (!item.getType().equals(Material.FILLED_MAP)) {
            Mandatory.sendMessage(player, "messages.update.copyright.noCopyright");
            return;
        }
        if (!CopyrightManager.hasCopyright(item)) {
            Mandatory.sendMessage(player, "messages.update.copyright.noCopyright");
            return;
        }
        UUID copyrighter = CopyrightManager.getCopyrighter(item);
        assert copyrighter != null;
        MPlayer mPlayer = MPlayerManager.getPlayerFromUUID(copyrighter);
        if (mPlayer == null) {
            Mandatory.sendMessage(player, "messages.update.copyright.noCopyright");
            return;
        }
        ItemStack itemStack = CopyrightManager.updateCopyright(item);
        //player.getInventory().getItemInMainHand().setAmount(0);
        player.getInventory().addItem(itemStack);
        Mandatory.sendMessage(player, "messages.update.copyright.success");
    }
}
