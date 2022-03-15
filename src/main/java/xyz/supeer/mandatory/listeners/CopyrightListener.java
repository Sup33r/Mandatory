package xyz.supeer.mandatory.listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class CopyrightListener implements Listener{

    @EventHandler
    public void onPlayerInventory(InventoryClickEvent e) {
        if (e.getClickedInventory().getType().toString().equalsIgnoreCase("Cartography")) {
            if (e.getSlot() == 2) {
                if (e.getCurrentItem().hasItemMeta()) {
                    ItemStack item = e.getCurrentItem();
                    NBTItem nbti = new NBTItem(item);
                    if (!Objects.equals(nbti.getString("copyrighter"), e.getWhoClicked().getUniqueId().toString())) {
                        e.setCancelled(true);
                    }
                }
            }
        }

        if (e.getClickedInventory().getType().toString().equalsIgnoreCase("Workbench")) {
            Bukkit.broadcastMessage(String.valueOf(e.getSlot()));
        }

        if (e.getClickedInventory().getType().toString().equalsIgnoreCase("Crafting")) {
            Bukkit.broadcastMessage(String.valueOf(e.getSlot()));
        }
    }

    @EventHandler
    public void onCrafting(CraftItemEvent e) {

    }
}