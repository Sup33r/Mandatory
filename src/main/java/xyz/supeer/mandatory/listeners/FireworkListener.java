package xyz.supeer.mandatory.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;

public class FireworkListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onFirework(PlayerInteractEvent e) {
        Player p = (Player) e.getPlayer();
        if (p != null && e.hasItem())
        {
            if (!p.hasPermission("mandatory.allow.boost")) {
            if (e.getAction() == RIGHT_CLICK_AIR)
            {
                {
                    if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.getMaterial(String.valueOf(Material.FIREWORK_ROCKET)))) {
                        e.setCancelled(true);
                        p.sendMessage("§cÅtkomst nekad.");
                    } else if (e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.getMaterial(String.valueOf(Material.FIREWORK_ROCKET)))) {
                        e.setCancelled(true);
                        p.sendMessage("§cÅtkomst nekad.");
                    }
                }
                }
            }
        }

    }

}
