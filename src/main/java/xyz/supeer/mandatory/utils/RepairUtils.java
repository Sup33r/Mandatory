package xyz.supeer.mandatory.utils;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

public class RepairUtils {

    public static void repairhand(Player p) {
            if(p.getInventory().getItemInMainHand().getType() != null) {

                p.getInventory().getItemInMainHand().setDurability((short)0);
            }
        }
    }

