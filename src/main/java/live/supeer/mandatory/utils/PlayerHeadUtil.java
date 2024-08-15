package live.supeer.mandatory.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class PlayerHeadUtil {

    public static ItemStack getPlayerHead(Player player) {

        boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).toList().contains("PLAYER_HEAD");

        Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
        assert type != null;
        ItemStack item = new ItemStack(type, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
        item.setItemMeta(meta);
        return item;
    }

}
