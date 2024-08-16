package live.supeer.mandatory;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CopyrightManager {

    public static boolean hasCopyright(ItemStack itemStack) {
       return NBT.get(itemStack, nbt -> nbt.getString("copyrighter") != null);
    }

    public static UUID getCopyrighter(ItemStack itemStack) {
        return UUID.fromString(NBT.get(itemStack, nbt -> {
            return nbt.getString("copyrighter");
        }));
    }


    public static ItemStack createCopyright(Player player, ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        TextComponent lore = Component.text("Kopieringsskyddad av: " + player.getName()).color(TextColor.fromCSSHexString("#ab8aff"));
        List<Component> loreList = new ArrayList<>();
        loreList.add(lore);
        meta.lore(loreList);
        itemStack.setItemMeta(meta);
        NBT.modify(itemStack, nbt -> {
            nbt.setString("copyrighter", player.getUniqueId().toString());
        });
        return itemStack;
    }

    public static ItemStack updateCopyright(ItemStack item) {
        MPlayer mPlayer = MPlayerManager.getPlayerFromUUID(getCopyrighter(item));
        if (mPlayer == null) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        TextComponent lore = Component.text("Kopieringsskyddad av: " + mPlayer.getName()).color(TextColor.fromCSSHexString("#ab8aff"));
        Objects.requireNonNull(meta.lore()).add(lore);
        item.setItemMeta(meta);
        return item;
    }
}
