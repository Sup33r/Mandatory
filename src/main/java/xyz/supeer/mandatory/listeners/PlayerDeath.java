package xyz.supeer.mandatory.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.supeer.mandatory.Main;

public class PlayerDeath implements Listener {

    private static Main plugin;

    public PlayerDeath (Main plugin) {


        PlayerDeath.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player) {

            Player killer = e.getEntity().getKiller();
            Player p = e.getEntity();
            e.setDeathMessage("" + ChatColor.GRAY + p.getDisplayName() + " blev dödad av " + killer.getDisplayName());
            return;

        }else {
            Player p = e.getEntity();
            e.setDeathMessage("" + ChatColor.GRAY + p.getDisplayName() + " dog.");
        }




    }

}
