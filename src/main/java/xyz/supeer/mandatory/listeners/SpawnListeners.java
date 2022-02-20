package xyz.supeer.mandatory.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import xyz.supeer.mandatory.Main;

public class SpawnListeners implements Listener {

    private final Main plugin;

    public SpawnListeners(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();


        if (!e.getPlayer().hasPlayedBefore()) {
            Location location = plugin.getCustomConfig().getLocation("firstspawn");
            if (location == null) {
                p.sendMessage("§cKunde inte teleportera dig till första startpunkten, då den inte finns.");
                return;
            } else {
                p.teleport(location);
            }

        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Location location = plugin.getCustomConfig().getLocation("spawn");
        if (!e.isAnchorSpawn() || !e.isBedSpawn()) {
            if (location != null) {
                e.setRespawnLocation(location);
            }
        }


    }

}
